package org.jmc.pluggable.backends;

import dms.mail.Account;
import dms.mail.Contact;
import java.util.Vector;
import dms.mail.Header;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 *
 * @author jesus
 */
public abstract class GenericBackend implements Backend {

    protected boolean profiled;

    protected String appdataUrl;

    protected String user;

    protected String filePwd;

    protected String userPwd;

    protected int cipher;

    private String name;

    public abstract boolean storeMessage(MimeMessage msg);

    public abstract boolean storeMessages(Vector<MimeMessage> msgs);

    public abstract Vector<Header> retreiveHeaders(String folder);

    public abstract MimeMultipart retreiveMessage(Header header);

    public abstract Vector<MimeMessage> retreiveMessages(String folder);

    public abstract boolean storeContact(Contact contact);

    public abstract Vector<Contact> retreiveContacts(String regex, int TYPE);

    public boolean isProfiled() {
        return profiled;
    }

    private void setProfiled(boolean profiled) {
        this.profiled = profiled;
    }

    public abstract boolean setProfile(String MeinName, String meineEmail);

    public abstract Vector<Account> listAccounts();

    public abstract boolean addAccount(Account acc);

    public abstract boolean deleteAccount(String accountID);

    public boolean init(String appdataPath, String user, String profilePasswd, String FileEcnryptionPwd, int FILE_ENCRYPTION_TYPE) {
        this.appdataUrl = appdataPath;
        this.user = user;
        this.userPwd = profilePasswd;
        this.filePwd = FileEcnryptionPwd;
        this.cipher = FILE_ENCRYPTION_TYPE;
        return initialize();
    }

    public abstract boolean initialize();

    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
}
