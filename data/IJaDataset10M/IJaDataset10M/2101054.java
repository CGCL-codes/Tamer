package com.cubusmail.mail.imap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.event.FolderListener;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.sun.mail.imap.IMAPStore;
import com.sun.mail.imap.Rights.Right;
import com.cubusmail.core.BeanFactory;
import com.cubusmail.core.CubusConstants;
import com.cubusmail.gwtui.domain.UserAccount;
import com.cubusmail.mail.IMailFolder;
import com.cubusmail.mail.IMailbox;
import com.cubusmail.mail.SessionManager;
import com.cubusmail.mail.exceptions.IErrorCodes;
import com.cubusmail.mail.exceptions.MailFolderException;
import com.cubusmail.mail.security.MailboxAuthenticator;

/**
 * Implementation of an imap mailbox.
 * 
 * @author Juergen Schlierf
 */
@SuppressWarnings("serial")
public class IMAPMailbox implements IMailbox {

    private Logger logger = Logger.getLogger(this.getClass());

    private Session session;

    private IMAPStore store = null;

    private IMailFolder currentFolder = null;

    private Map<String, IMailFolder> mailFolderMap;

    private List<IMailFolder> mailFolderList;

    private MailboxAuthenticator mailboxAuthenticator;

    private String personalNameSpace;

    private char folderSeparator;

    private UserAccount userAccount;

    private String username;

    private boolean imapPartialfetch;

    private long imapFetchsize;

    private boolean imapSSL;

    private String imapHost;

    private int imapPort;

    private boolean smtpSSL;

    private String smtpHost;

    private int smtpPort;

    private String domainName;

    public IMAPMailbox() {
        this.mailFolderMap = new TreeMap<String, IMailFolder>();
        this.mailFolderList = new ArrayList<IMailFolder>();
    }

    public void init(String username, String password) {
        this.username = username;
        Properties props = new Properties();
        props.put("mail.user", username);
        props.put("mail.imap.partialfetch", this.imapPartialfetch);
        props.put("mail.imap.fetchsize", this.imapFetchsize);
        String imapProtocol = this.imapSSL ? "imaps" : "imap";
        props.put("mail.store.protocol", imapProtocol);
        props.put("mail." + imapProtocol + ".port", this.imapPort);
        props.put("mail." + imapProtocol + ".host", this.imapHost);
        String smtpProtocol = this.smtpSSL ? "smtps" : "smtp";
        props.put("mail.transport.protocol", smtpProtocol);
        props.put("mail." + smtpProtocol + ".port", this.smtpPort);
        props.put("mail." + smtpProtocol + ".host", this.smtpHost);
        props.put("mail." + smtpProtocol + ".quitwait", "false");
        props.put("mail." + smtpProtocol + ".auth", "true");
        props.put("mail.mime.decodetext.strict", "true");
        props.put("mail.mime.address.strict", "false");
        props.put("mail.mime.charset", CubusConstants.DEFAULT_CHARSET);
        this.mailboxAuthenticator.setUsername(username);
        this.mailboxAuthenticator.setPassword(password);
        Session session = Session.getInstance(props, this.mailboxAuthenticator);
        session.setDebug(false);
        this.session = session;
    }

    public void login() throws MessagingException {
        if (this.store == null) {
            this.store = createStore();
        }
        this.store.connect();
        this.personalNameSpace = "";
        try {
            Folder[] namespaces = store.getPersonalNamespaces();
            if (namespaces != null && namespaces.length > 0) {
                this.personalNameSpace = namespaces[0].getFullName();
                if (this.personalNameSpace.length() > 0) {
                    this.personalNameSpace += this.store.getDefaultFolder().getSeparator();
                }
                this.personalNameSpace = this.personalNameSpace.trim();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void logout() throws MessagingException {
        if (this.currentFolder != null && this.currentFolder.isOpen()) {
            this.currentFolder.close(true);
            this.currentFolder = null;
        }
        if (this.store != null) {
            this.store.close();
            this.store = null;
        }
    }

    public boolean isLoggedIn() {
        if (this.store != null && this.store.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    public void setCurrentFolder(IMailFolder folder) throws MessagingException {
        if (this.currentFolder != null && this.currentFolder.isOpen() && !this.currentFolder.equals(folder)) {
            this.currentFolder.close(false);
        }
        this.currentFolder = folder;
    }

    public IMailFolder getCurrentFolder() {
        return this.currentFolder;
    }

    /**
	 * Deliveres the inbox folder.
	 * 
	 * @return
	 */
    private IMailFolder getInboxFolder() {
        return this.mailFolderMap.get(SessionManager.get().getPreferences().getInboxFolderName());
    }

    /**
	 * Deliveres the draft folder.
	 * 
	 * @return
	 */
    public IMailFolder getDraftFolder() {
        return getMailFolderById(this.personalNameSpace + SessionManager.get().getPreferences().getDraftFolderName());
    }

    public IMailFolder getSentFolder() {
        return getMailFolderById(this.personalNameSpace + SessionManager.get().getPreferences().getSentFolderName());
    }

    public IMailFolder getTrashFolder() {
        return getMailFolderById(this.personalNameSpace + SessionManager.get().getPreferences().getTrashFolderName());
    }

    public void moveFolder(String sourceFolderId, String targetFolderId) throws MailFolderException {
        IMailFolder sourceFolder = null;
        IMailFolder targetFolder = null;
        try {
            String newFolderName;
            sourceFolder = getMailFolderById(sourceFolderId);
            targetFolder = getMailFolderById(targetFolderId);
            if (sourceFolder != null) {
                if (targetFolder == null) {
                    newFolderName = sourceFolder.getName();
                } else {
                    newFolderName = targetFolder.getId() + getFolderSeparator() + sourceFolder.getName();
                }
                if (sourceFolder.isOpen()) {
                    sourceFolder.close(false);
                }
                if (targetFolder != null && targetFolder.isOpen()) {
                    targetFolder.close(false);
                }
                Folder newFolder = this.store.getFolder(newFolderName);
                if (!newFolder.exists()) {
                    sourceFolder.renameTo(newFolder);
                } else {
                    throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_ALREADY_EXIST, null, sourceFolder);
                }
                loadMailFolder();
            }
        } catch (MessagingException e) {
            throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_MOVE, e, sourceFolder);
        }
    }

    public void renameFolder(String folderId, String folderName) throws MailFolderException {
        IMailFolder folder = getMailFolderById(folderId);
        try {
            String newName = folder.getId();
            if (newName.lastIndexOf(getFolderSeparator()) >= 0) {
                newName = newName.substring(0, newName.lastIndexOf(getFolderSeparator()) + 1) + folderName;
            } else {
                newName = folderName;
            }
            if (folder.isOpen()) {
                folder.close(false);
            }
            Folder newFolder = this.store.getFolder(newName);
            if (!newFolder.exists()) {
                folder.renameTo(newFolder);
            } else {
                throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_ALREADY_EXIST, null, createMailFolder(newFolder));
            }
            loadMailFolder();
        } catch (MessagingException ex) {
            throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_RENAME, ex, folder);
        }
    }

    public void emptyFolder(String folderId) throws MailFolderException {
        IMailFolder folder = getMailFolderById(folderId);
        try {
            if (!folder.isOpen()) {
                folder.open(Folder.READ_WRITE);
            }
            if (folder.getMessageCount() > 0) {
                Message[] msgs = folder.retrieveMessages(null);
                for (int i = 0; i < folder.getMessageCount(); i++) {
                    msgs[i].setFlag(Flags.Flag.DELETED, true);
                }
            }
            folder.close(true);
        } catch (MessagingException ex) {
            throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_EMPTY, ex, folder);
        }
    }

    public void deleteFolder(String folderId) throws MailFolderException {
        IMailFolder folder = getMailFolderById(folderId);
        try {
            if (folder.hasRight(Right.DELETE)) {
                if (folder.isOpen()) {
                    folder.close(false);
                }
                if (folder.delete(true)) {
                    loadMailFolder();
                    setCurrentFolder(getInboxFolder());
                } else {
                    throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_DELETE, null, folder);
                }
            }
        } catch (MessagingException ex) {
            throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_DELETE, ex, folder);
        }
    }

    public IMailFolder createFolder(String parentFolderId, String folderName) throws MailFolderException {
        try {
            String newFolderName = null;
            if (!StringUtils.isEmpty(parentFolderId)) {
                newFolderName = parentFolderId + getFolderSeparator() + folderName;
            } else {
                newFolderName = folderName;
            }
            Folder newFolder = this.store.getFolder(newFolderName);
            if (!newFolder.exists()) {
                logger.debug("Createing folder... " + newFolderName);
                newFolder.create(Folder.HOLDS_MESSAGES);
            } else {
                throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_ALREADY_EXIST, null);
            }
            loadMailFolder();
            return createMailFolder(newFolder);
        } catch (MessagingException ex) {
            throw new MailFolderException(IErrorCodes.EXCEPTION_FOLDER_CREATE, ex);
        }
    }

    public void reloadFolder() throws MessagingException {
        this.loadMailFolder();
        this.currentFolder = getInboxFolder();
    }

    public void copyMessages(long[] messageIds, String targetFolderId) throws MessagingException {
        if (!this.currentFolder.isOpen()) {
            this.currentFolder.open(Folder.READ_WRITE);
        }
        IMailFolder targetFolder = getMailFolderById(targetFolderId);
        if (!targetFolder.isOpen()) {
            targetFolder.open(Folder.READ_WRITE);
        }
        this.currentFolder.copyMessages(messageIds, targetFolder);
        targetFolder.close(false);
    }

    public void deleteMessages(long[] messageIds) throws MessagingException {
        if (messageIds != null && messageIds.length > 0) {
            if (!this.currentFolder.isOpen()) {
                this.currentFolder.open(Folder.READ_WRITE);
            }
            Message[] msgs = this.currentFolder.getMessagesById(messageIds);
            for (int i = 0; i < msgs.length; i++) {
                msgs[i].setFlag(Flags.Flag.DELETED, true);
            }
            this.currentFolder.expunge(msgs);
        }
    }

    public IMailFolder getMailFolderById(String id) {
        return this.mailFolderMap.get(id);
    }

    public void addFolderListener(FolderListener listener) {
        this.store.addFolderListener(listener);
    }

    public String getUserName() {
        return this.username;
    }

    public String getEmailAddress() {
        if (this.username.indexOf("@") > 0) {
            return this.username;
        } else {
            return this.username + "@" + this.domainName;
        }
    }

    public String getFullName() {
        return "Juergen Schlierf";
    }

    /**
	 * 
	 */
    private IMAPStore createStore() {
        IMAPStore store = null;
        try {
            store = (IMAPStore) session.getStore();
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return store;
    }

    /**
	 * @throws MessagingException
	 */
    private void loadMailFolder() throws MessagingException {
        logger.debug("loading folder tree...");
        long millis = System.currentTimeMillis();
        this.mailFolderMap.clear();
        this.mailFolderList.clear();
        Folder defaultFolder = this.store.getDefaultFolder();
        this.folderSeparator = defaultFolder.getSeparator();
        List<String> topFolderNames = new ArrayList<String>();
        Folder[] allFolders = defaultFolder.list("*");
        if (allFolders != null && allFolders.length > 0) {
            for (Folder folder : allFolders) {
                this.mailFolderMap.put(folder.getFullName(), createMailFolder(folder));
                if (SessionManager.get().getPreferences().getInboxFolderName().equals(folder.getFullName())) {
                    topFolderNames.add(0, SessionManager.get().getPreferences().getInboxFolderName());
                } else {
                    String folderName = folder.getFullName();
                    if (!StringUtils.isEmpty(this.personalNameSpace) && folderName.startsWith(this.personalNameSpace)) {
                        folderName = StringUtils.substringAfter(folderName, this.personalNameSpace);
                    }
                    if (StringUtils.countMatches(folderName, String.valueOf(getFolderSeparator())) == 0) {
                        topFolderNames.add(folder.getFullName());
                    }
                }
            }
        }
        for (String folderName : topFolderNames) {
            IMailFolder mailFolder = this.mailFolderMap.get(folderName);
            this.mailFolderList.add(mailFolder);
            if (!SessionManager.get().getPreferences().getInboxFolderName().equals(folderName) && mailFolder.hasChildren()) {
                mailFolder.setSubfolders(getSubfolders(mailFolder));
            }
        }
        logger.debug("...finish: " + (System.currentTimeMillis() - millis) + "ms");
    }

    /**
	 * @param mailFolder
	 * @return
	 * @throws MessagingException
	 */
    private List<IMailFolder> getSubfolders(IMailFolder mailFolder) throws MessagingException {
        List<IMailFolder> subfolders = new ArrayList<IMailFolder>();
        String searchKey = mailFolder.getId() + getFolderSeparator();
        Set<String> keys = this.mailFolderMap.keySet();
        for (String key : keys) {
            if (key.startsWith(searchKey) && !StringUtils.contains(StringUtils.substringAfter(key, searchKey), getFolderSeparator())) {
                IMailFolder subfolder = this.mailFolderMap.get(key);
                subfolders.add(subfolder);
                if (subfolder.hasChildren()) {
                    subfolder.setSubfolders(getSubfolders(subfolder));
                }
            }
        }
        return subfolders;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    private char getFolderSeparator() throws MessagingException {
        return this.folderSeparator;
    }

    public Session getJavaMailSession() {
        return session;
    }

    public List<IMailFolder> getMailFolderList() {
        return this.mailFolderList;
    }

    public UserAccount getUserAccount() {
        return this.userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    /**
	 * @param mailFolderList
	 *            The mailFolderList to set.
	 */
    public void setMailFolderList(List<IMailFolder> mailFolderList) {
        this.mailFolderList = mailFolderList;
    }

    /**
	 * @param folderSeparator
	 *            The folderSeparator to set.
	 */
    public void setFolderSeparator(char folderSeparator) {
        this.folderSeparator = folderSeparator;
    }

    /**
	 * @param imapPartialfetch
	 *            The imapPartialfetch to set.
	 */
    public void setImapPartialfetch(boolean imapPartialfetch) {
        this.imapPartialfetch = imapPartialfetch;
    }

    /**
	 * @param imapFetchsize
	 *            The imapFetchsize to set.
	 */
    public void setImapFetchsize(long imapFetchsize) {
        this.imapFetchsize = imapFetchsize;
    }

    /**
	 * @param imapSSL
	 *            The imapSSL to set.
	 */
    public void setImapSSL(boolean imapSSL) {
        this.imapSSL = imapSSL;
    }

    /**
	 * @param imapHost
	 *            The imapHost to set.
	 */
    public void setImapHost(String imapHost) {
        this.imapHost = imapHost;
    }

    /**
	 * @param imapPort
	 *            The imapPort to set.
	 */
    public void setImapPort(int imapPort) {
        this.imapPort = imapPort;
    }

    /**
	 * @param smtpSSL
	 *            The smtpSSL to set.
	 */
    public void setSmtpSSL(boolean smtpSSL) {
        this.smtpSSL = smtpSSL;
    }

    /**
	 * @param smtpHost
	 *            The smtpHost to set.
	 */
    public void setSmtpHost(String smtpHost) {
        this.smtpHost = smtpHost;
    }

    /**
	 * @param smptPort
	 *            The smptPort to set.
	 */
    public void setSmtpPort(int smtpPort) {
        this.smtpPort = smtpPort;
    }

    /**
	 * @param domainName
	 *            The domainName to set.
	 */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
	 * @param mailboxAuthenticator
	 *            The mailboxAuthenticator to set.
	 */
    public void setMailboxAuthenticator(MailboxAuthenticator mailboxAuthenticator) {
        this.mailboxAuthenticator = mailboxAuthenticator;
    }

    /**
	 * @param folder
	 * @return
	 */
    private IMailFolder createMailFolder(Folder folder) {
        IMAPMailFolder mailFolder = (IMAPMailFolder) BeanFactory.getBean("imapMailFolder");
        mailFolder.init(folder);
        return mailFolder;
    }
}
