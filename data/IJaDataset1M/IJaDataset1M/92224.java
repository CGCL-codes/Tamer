package net.solosky.maplefetion.client.notify;

import java.util.Iterator;
import java.util.List;
import net.solosky.maplefetion.FetionException;
import net.solosky.maplefetion.bean.Buddy;
import net.solosky.maplefetion.bean.Relation;
import net.solosky.maplefetion.client.dialog.MutipartyDialog;
import net.solosky.maplefetion.sipc.SipcNotify;
import net.solosky.maplefetion.util.BeanHelper;
import net.solosky.maplefetion.util.UriHelper;
import net.solosky.maplefetion.util.XMLHelper;
import org.jdom.Element;

/**
 *
 *	用户进入对话后的通知
 *
 * @author solosky <solosky772@qq.com> 
 */
public class ConversationNotifyHandler extends AbstractNotifyHandler {

    @Override
    public void handle(SipcNotify notify) throws FetionException {
        Element root = XMLHelper.build(notify.getBody().toSendString());
        List list = XMLHelper.findAll(root, "/events/*event");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            Element event = (Element) it.next();
            String type = event.getAttributeValue("type");
            if (type.equals("UserEntered")) {
                this.userEntered(event);
            } else if (type.equals("UserLeft")) {
                this.userLeft(event);
            } else if (type.equals("UserFailed")) {
                this.userFailed(event);
            } else {
                logger.warn("Unknown converstion event type:" + type);
            }
        }
    }

    /**
     * 用户进入了会话
     */
    private void userEntered(Element event) {
        if (this.dialog instanceof MutipartyDialog) {
            MutipartyDialog cd = (MutipartyDialog) this.dialog;
            List list = event.getChildren("member");
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Element member = (Element) it.next();
                    String uri = member.getAttributeValue("uri");
                    Buddy buddy = this.context.getFetionStore().getBuddyByUri(uri);
                    if (buddy == null) {
                        buddy = UriHelper.createBuddy(uri);
                        BeanHelper.setValue(buddy, "relation", Relation.STRANGER);
                    }
                    cd.buddyEntered(buddy);
                    logger.debug("Buddy entered this dialog:" + uri);
                }
            }
        }
    }

    /**
     * 用户离开了回话
     * @throws Exception 
     */
    private void userLeft(Element event) {
        if (this.dialog instanceof MutipartyDialog) {
            MutipartyDialog cd = (MutipartyDialog) this.dialog;
            List list = event.getChildren("member");
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Element member = (Element) it.next();
                    String uri = member.getAttributeValue("uri");
                    Buddy buddy = this.context.getFetionStore().getBuddyByUri(uri);
                    if (buddy == null) {
                        buddy = UriHelper.createBuddy(uri);
                    }
                    cd.buddyLeft(buddy);
                    logger.debug("Buddy left this dialog:" + uri);
                }
            }
        }
    }

    /**
     * 被邀请用户的用户未能进入会话
     * @param event
     */
    private void userFailed(Element event) {
        if (this.dialog instanceof MutipartyDialog) {
            MutipartyDialog cd = (MutipartyDialog) this.dialog;
            List list = event.getChildren("member");
            if (list != null) {
                Iterator it = list.iterator();
                while (it.hasNext()) {
                    Element member = (Element) it.next();
                    String uri = member.getAttributeValue("uri");
                    Buddy buddy = this.context.getFetionStore().getBuddyByUri(uri);
                    if (buddy == null) {
                        buddy = UriHelper.createBuddy(uri);
                    }
                    cd.buddyFailed(buddy);
                    logger.debug("Buddy failed to enter this dialog:" + uri);
                }
            }
        }
    }
}
