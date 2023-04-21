package net.solosky.maplefetion.client.response;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import net.solosky.maplefetion.FetionContext;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Cord;
import net.solosky.maplefetion.bean.Credential;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.bean.User;
import net.solosky.maplefetion.client.SystemException;
import net.solosky.maplefetion.client.dialog.Dialog;
import net.solosky.maplefetion.event.ActionEvent;
import net.solosky.maplefetion.event.action.ActionEventListener;
import net.solosky.maplefetion.event.action.FailureEvent;
import net.solosky.maplefetion.event.action.FailureType;
import net.solosky.maplefetion.sipc.SipcResponse;
import net.solosky.maplefetion.store.FetionStore;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.DigestHelper;
import net.solosky.maplefetion.util.StringHelper;
import net.solosky.maplefetion.util.UriHelper;
import net.solosky.maplefetion.util.XMLHelper;
import org.jdom.Element;

/**
 *
 *
 * @author solosky <solosky772@qq.com>
 */
public class UserAuthResponseHandler extends AbstractResponseHandler {

    /**
     * @param client
     * @param dialog
     * @param listener
     */
    public UserAuthResponseHandler(FetionContext client, Dialog dialog, ActionEventListener listener) {
        super(client, dialog, listener);
    }

    @Override
    protected ActionEvent doActionOK(SipcResponse response) throws FetionException {
        Element root = XMLHelper.build(response.getBody().toSendString());
        FetionStore store = this.context.getFetionStore();
        Element personal = XMLHelper.find(root, "/results/user-info/personal");
        User user = this.context.getFetionUser();
        user.setEmail(personal.getAttributeValue("register-email"));
        int personalVersion = Integer.parseInt(personal.getAttributeValue("version"));
        Element contactList = XMLHelper.find(root, "/results/user-info/contact-list");
        int contactVersion = Integer.parseInt(contactList.getAttributeValue("version"));
        store.getStoreVersion().setPersonalVersion(personalVersion);
        store.getStoreVersion().setContactVersion(contactVersion);
        user.getStoreVersion().setPersonalVersion(personalVersion);
        user.getStoreVersion().setContactVersion(contactVersion);
        BeanHelper.toBean(User.class, user, personal);
        synchronized (store) {
            List list = XMLHelper.findAll(root, "/results/user-info/contact-list/buddy-lists/*buddy-list");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                store.addCord(new Cord(Integer.parseInt(e.getAttributeValue("id")), e.getAttributeValue("name")));
            }
            list = XMLHelper.findAll(root, "/results/user-info/contact-list/buddies/*b");
            it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String uri = e.getAttributeValue("u");
                Buddy b = UriHelper.createBuddy(uri);
                b.setUserId(Integer.parseInt(e.getAttributeValue("i")));
                b.setLocalName(e.getAttributeValue("n"));
                b.setUri(e.getAttributeValue("u"));
                b.setCordId(e.getAttributeValue("l"));
                b.setRelation(Relation.valueOf(Integer.parseInt(e.getAttributeValue("r"))));
                store.addBuddy(b);
            }
            list = XMLHelper.findAll(root, "/results/user-info/contact-list/chat-friends/*c");
            it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                Buddy b = UriHelper.createBuddy(e.getAttributeValue("u"));
                b.setUserId(Integer.parseInt(e.getAttributeValue("i")));
                b.setUri(e.getAttributeValue("u"));
                b.setRelation(Relation.STRANGER);
                store.addBuddy(b);
            }
            list = XMLHelper.findAll(root, "/results/user-info/contact-list/blacklist/*k");
            it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String uri = e.getAttributeValue("u");
                Buddy b = store.getBuddyByUri(uri);
                if (b != null) {
                    b.setRelation(Relation.BANNED);
                }
            }
            Element credentialList = XMLHelper.find(root, "/results/credentials");
            user.setSsiCredential(this.decryptCredential(credentialList.getAttributeValue("kernel")));
            list = XMLHelper.findAll(root, "/results/credentials/*credential");
            it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String domain = e.getAttributeValue("domain");
                Credential c = store.getCredential(domain);
                if (c == null) {
                    c = new Credential(domain, null);
                    store.addCredential(c);
                }
                c.setCredential(this.decryptCredential(e.getAttributeValue("c")));
            }
        }
        return super.doActionOK(response);
    }

    private String decryptCredential(String c) throws FetionException {
        User user = this.context.getFetionUser();
        try {
            byte[] encrypted = StringHelper.base64Decode(c);
            byte[] decrypted = DigestHelper.AESDecrypt(encrypted, user.getAesKey(), user.getAesIV());
            return new String(decrypted, "utf8");
        } catch (IOException ex) {
            throw new SystemException("decrypt credential failed.", ex);
        }
    }

    @Override
    protected ActionEvent doRequestFailure(SipcResponse response) throws FetionException {
        return new FailureEvent(FailureType.REGISTER_FORBIDDEN);
    }

    @Override
    protected ActionEvent doNotAuthorized(SipcResponse response) throws FetionException {
        return new FailureEvent(FailureType.AUTHORIZATION_FAIL);
    }
}
