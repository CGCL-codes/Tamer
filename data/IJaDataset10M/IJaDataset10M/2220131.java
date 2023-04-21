package mujmail;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import mujmail.Lang;
import mujmail.MessageHeader;
import mujmail.MujMail;
import mujmail.PersistentBox;
import mujmail.account.MailAccount;
import mujmail.account.MailAccountPrimary;
import mujmail.ordering.ComparatorStrategy;
import mujmail.ordering.Criterion;
import mujmail.ordering.Ordering;
import mujmail.protocols.InProtocol;
import mujmail.tasks.StoppableProgress;
import mujmail.util.PersistentValueReminder.PersistentIntValueReminder;
import mujmail.account.MailAccountDerived;

/**
 * Stores mails downloaded from email accounts.
 */
public class InBox extends PersistentBox {

    /** The name of this source file */
    private static final String SOURCE_FILE = "InBox";

    /** Flag signals if we want to print debug prints */
    private static final boolean DEBUG = false;

    public final Command retrieve;

    public final Command redownload;

    /** The number of unread mails in the box. Stored in rms. */
    private final PersistentIntValueReminder numUnreadMails;

    /** to map stored mails in the vector storage */
    private Hashtable msgIDs;

    boolean syncOK = true;

    int mailsOnServers = -1;

    /** Flag indicates whether in box have to be sorted again */
    private boolean needResort;

    private Timer pollTimer;

    private MessageHeader lastSafeMail;

    private boolean pushActive = false;

    /** Holds list of (active) accounts to be retrieved into this folder */
    private Vector accounts = new Vector();

    /** 
     *  Holds index in recordstore userMailBox database, 
     *    where info about this InBox instance take place.
     *  Note: Only for BoxList class purposes.
     *  Note: Used only for user mail boxes.
     */
    private int userBoxListDB_recordID = -1;

    /**
     * Creates inbox.
     * 
     * @param DBFile the identifier of RMS database where the mails of this box
     *  will be stored.
     * @param mMail the main object in the application
     * @param name the name of the box
     */
    public InBox(String DBFile, String name) {
        super(DBFile, name);
        numUnreadMails = new PersistentIntValueReminder(DBFile + "_unreadMails");
        msgIDs = new Hashtable();
        retrieve = new Command(Lang.get(Lang.BTN_RTV_NEW_MAILS), Command.ITEM, 8);
        redownload = new Command(Lang.get(Lang.BTN_TB_REDOWNLOAD), Command.ITEM, 6);
        addCommand(redownload);
        addCommand(retrieve);
        Thread t = new RetrieveBoxAccountsTask(RetrieveBoxAccountsTask.ACCOUNTS_LOAD);
        t.start();
    }

    /**
     * Returns true if the synchronization is running.
     * @return true if the synchronization is running.
     */
    public boolean isSyncRunning() {
        return (mailsOnServers != -1);
    }

    public void showBox() {
        super.showBox();
        setCurFirstUnread();
    }

    public boolean isNeedResort() {
        return needResort;
    }

    public void setNeedResort(boolean needResort) {
        this.needResort = needResort;
    }

    private class Polling extends TimerTask {

        public void run() {
            for (Enumeration e = getMujMail().getMailAccounts().elements(); e.hasMoreElements(); ) {
                MailAccount account = (MailAccount) e.nextElement();
                if (account.isPOP3() && account.isActive()) {
                    account.getProtocol().poll(InBox.this);
                }
            }
        }
    }

    private class Pushing extends Thread {

        private final MailAccount pushAccount;

        public Pushing(MailAccount pushAccount) {
            this.pushAccount = pushAccount;
        }

        public synchronized void run() {
            pushAccount.startPushing();
        }
    }

    /**
     * If some error during synchronization occurs, set this.
     */
    public void synchronizationError() {
        syncOK = false;
    }

    public void exit() {
        if (Settings.delOnExit) {
            deleteMarkedFromBoxAndDB();
        }
        super.exit();
    }

    protected boolean isBusy() {
        if (getMailDB().isBusy() || InProtocol.isBusyGlobal()) {
            return true;
        }
        return false;
    }

    protected void paintIsBusy() {
        if (getMailDB().isBusy()) {
            mailDB.getDBLoadingTask().showProgressIfRunning();
        } else if (InProtocol.isBusyGlobal()) {
            InProtocol.getLastInProtocolTask().showProgressIfRunning();
        }
    }

    /**
     * If there is polling not active, enables polling, if it is active, 
     * cancels polling.
     */
    public void poll() {
        if (pollTimer == null) {
            pollTimer = new Timer();
            pollTimer.scheduleAtFixedRate(new Polling(), 0, Settings.pollInvl >= 5 ? Settings.pollInvl * 1000 : 5 * 1000);
        } else {
            pollTimer.cancel();
            pollTimer = null;
        }
    }

    /**
     * Increases the number of unread messages.
     * 
     * @param val value to increase number of unread e-mails
     */
    public synchronized void changeUnreadMails(int val) {
        setUnreadMails(getUnreadMails() + val);
    }

    public void initHash() {
        if (DEBUG) {
            System.out.println("DEBUG InBox.initHash() - " + this.getName());
        }
        if (getStorage() == null) return;
        msgIDs = new Hashtable(getStorage().getSize());
        MessageHeader header;
        Enumeration messages = getStorage().getEnumeration();
        while (messages.hasMoreElements()) {
            header = (MessageHeader) messages.nextElement();
            msgIDs.put(header.getAccountID() + "@" + header.getMessageID(), header);
        }
    }

    /** Checks if we see this many sometime or it's new comming email
     *  @param ID Identification of email
     *  @return true if seen mail
     */
    public boolean wasOnceDownloaded(String accountID, String messageID) {
        if (DEBUG) {
            System.out.println("DEBUG InBox.wasOnceDownloaded(String, String)");
        }
        if (msgIDs.containsKey(accountID + "@" + messageID)) {
            return true;
        }
        return getMujMail().getMailDBSeen().wasMailSeen(accountID, messageID);
    }

    public synchronized void newMailOnServer() {
        if (mailsOnServers != -1) {
            ++mailsOnServers;
        }
    }

    public synchronized void addToMsgIDs(MessageHeader message) {
        if (message != null && msgIDs != null) {
            msgIDs.put(message.getAccountID() + "@" + message.getMessageID(), message);
        }
    }

    public synchronized void addToOnceDownloaded(MessageHeader header) {
        try {
            getMujMail().getMailDBSeen().addSeen(header);
        } catch (MyException ex) {
            report(Lang.get(Lang.ALRT_SAVING) + Lang.get(Lang.MSGIDS_CACHE) + Lang.get(Lang.FAILED), SOURCE_FILE);
        }
    }

    public void serversSync(StoppableProgress progress) {
        if (!syncOK) {
            syncOK = true;
            mailsOnServers = -1;
            stop();
            getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_SYNCHRONIZING) + Lang.get(Lang.FAILED) + Lang.get(Lang.ALRT_SYS_EXCEPTION_AROSED), MyAlert.DEFAULT, AlertType.ERROR);
            return;
        }
        if (mailsOnServers == -1 && !hasAccountToRetrieve()) {
            getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_AS_NO_ACCOUNT_SET_ACTIVE), MyAlert.DEFAULT, AlertType.WARNING);
        } else {
            if (mailsOnServers == -1) {
                syncOK = true;
                mailsOnServers = 0;
                retrieve();
            } else if (!InProtocol.isBusyGlobal()) {
                synchronized (getStorage()) {
                    if (getStorage().getSize() == mailsOnServers) {
                        mailsOnServers = -1;
                        return;
                    }
                    progress.setTitle(Lang.get(Lang.ALRT_SYNCHRONIZING) + Lang.get(Lang.ALRT_WAIT));
                    progress.updateProgress(0, 0);
                    MessageHeader message;
                    int toDelete = getStorage().getSize() - mailsOnServers;
                    MailAccount account;
                    Enumeration messages = getStorage().getEnumeration();
                    while (messages.hasMoreElements()) {
                        message = (MessageHeader) messages.nextElement();
                        account = (MailAccount) getMujMail().getMailAccounts().get(message.getAccountID());
                        if (account != null && account.isActive() && !account.getProtocol().containsMail(message)) {
                            if (!message.deleted) {
                                ++deleted;
                                message.deleted = true;
                            }
                            if (--toDelete == 0) {
                                break;
                            }
                        }
                    }
                    mailsOnServers = -1;
                }
                deleteMarkedFromBoxAndDB();
            }
        }
    }

    /**
     * @return true if there is any account to retriveve by instance of box, false otherwise
     */
    public boolean hasAccountToRetrieve() {
        return accounts.size() > 0;
    }

    /** Persistently save (and actualize) set of accounts
     *  that should be retrived by instance of InBox */
    public void saveBoxRetrieveAccounts(Vector newAccounts) {
        if (accounts == null) {
            return;
        }
        accounts = newAccounts;
        RetrieveBoxAccountsTask saveTask = new RetrieveBoxAccountsTask(RetrieveBoxAccountsTask.ACCOUNTS_SAVE);
        saveTask.start();
    }

    /**
     * Get list of accounts that retrieve this box
     * @return Vector of accout to be retrieve by box
     */
    public Vector getAccounts() {
        return accounts;
    }

    /**
     * Changes box to retrieve all active mail accounts.
     * Note: Call if this folder is set (unset) as default mujMail Inbox folder.
     */
    public void actualizeActiveAccountList() {
        accounts.removeAllElements();
        for (Enumeration e = getMujMail().getMailAccounts().elements(); e.hasMoreElements(); ) {
            MailAccount account = (MailAccount) e.nextElement();
            if (account.isActive()) {
                accounts.addElement(account);
            }
        }
    }

    /**
     * Retrieves new mails from all accounts.
     * Starts retrieving mails in new thread. Does not retrieve mails from all
     * accounts simultaneously, but serializes retrieving mails.
     */
    public void retrieve() {
        Thread thread = new Thread() {

            public void run() {
                if (!hasAccountToRetrieve()) {
                    getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_AS_NO_ACCOUNT_SET_ACTIVE), MyAlert.DEFAULT, AlertType.WARNING);
                }
                setNeedResort(true);
                for (Enumeration e = accounts.elements(); e.hasMoreElements(); ) {
                    MailAccount account = (MailAccount) e.nextElement();
                    InProtocol.waitForNotBusyGlobal();
                    account.getProtocol().getNewMails(InBox.this);
                }
            }
        };
        thread.start();
    }

    public void retrieveOne(String accountID) {
        if (accountID != null && getMujMail().getMailAccounts().get(accountID) != null) {
            MailAccount account = (MailAccount) getMujMail().getMailAccounts().get(accountID);
            if (account.isActive()) {
                setNeedResort(true);
                account.getProtocol().getNewMails(this);
            } else {
                getMujMail().getAlert().setAlert(this, null, accountID + " " + Lang.get(Lang.INACTIVE), MyAlert.DEFAULT, AlertType.WARNING);
            }
        }
    }

    /** Try to found Mail account with which we download this email
     * 
     * @param header Mail header which account we are interesting
     * @return Mail account where email come from.
     */
    private MailAccount findAccountForHeader(MessageHeader header) {
        if (header == null) {
            return null;
        }
        for (Enumeration e = getAccounts().elements(); e.hasMoreElements(); ) {
            MailAccount account = (MailAccount) e.nextElement();
            if (account.getEmail().equals(header.getAccountID())) {
                return account;
            }
        }
        MailAccount account = (MailAccount) getMujMail().getMailAccounts().get(header.getAccountID());
        if (account == null) {
            return null;
        }
        return account;
    }

    /**
     * Downloads a body of the mail given by header.
     * @param header the header of mail which body will be deleted
     * @param block true if the operation should block. That means that
     *  the thread that called this method will wait until the headers
     *  will be loaded.
     */
    public void getBody(MessageHeader header, boolean block) {
        MailAccount account = findAccountForHeader(header);
        if (account == null) {
            getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_AS_NONEXIST) + ": " + header.getAccountID(), MyAlert.DEFAULT, AlertType.WARNING);
            return;
        }
        if (Settings.safeMode) {
            clearLastSafeMail();
        }
        account.getProtocol().getBody(header, this, block);
    }

    /**
     * Redownloads the mail.
     * @param header identifies the mail which will be redownloaded
     * @param bodyPartNumber value -1 for redownloading the whole mail
     *  higher values for a concrete bodypart
     * @param block true if calling thread should be blocked until the
     *  operation is completed.
     */
    public void regetBody(MessageHeader header, byte bodyPartNumber, boolean block) {
        MailAccount account = findAccountForHeader(header);
        if (account == null) {
            getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_AS_NONEXIST) + ": " + header.getAccountID(), MyAlert.DEFAULT, AlertType.WARNING);
            if (bodyPartNumber != -1) {
                synchronized (header) {
                    header.notify();
                }
            }
            return;
        }
        if (Settings.safeMode) {
            clearLastSafeMail();
        }
        account.getProtocol().regetBody(header, bodyPartNumber, this, block);
    }

    public void clearLastSafeMail() {
        if (getLastSafeMail() != null) {
            try {
                mailDB.clearDb(false);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            getLastSafeMail().deleteAllBodyParts();
            try {
                getMailDB().saveHeader(getLastSafeMail());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            setLastSafeMail(null);
        }
    }

    /** 
     * Delete all mails a databases that uses InBox.
     * Note: Used if removing user mailbox from mujMail
     */
    public void removeAllDBs() {
        try {
            mailDB.clearDb(true);
        } catch (Exception ex) {
            if (DEBUG) {
                System.out.println("DEBUG InBox.removeAllDBs - cleardb problem");
                ex.printStackTrace();
            }
        }
        try {
            RecordStore.deleteRecordStore(getDBFileName() + "_ACC");
        } catch (Exception ex) {
            if (DEBUG) {
                System.out.println("DEBUG InBox.removeAllDBs - accounts db problem");
                ex.printStackTrace();
            }
        }
    }

    public void deleteAllMailsFromBoxAndDB(boolean sure) {
        super.deleteAllMailsFromBoxAndDB(sure);
        if (sure) {
            setLastSafeMail(null);
            if (msgIDs != null) {
                msgIDs.clear();
            }
            setUnreadMails(0);
        }
    }

    /**
     * Deletes mails from inbox and from database.
     * If Settings.moveToTrash is true and Settings.safeMode is false, moves
     * mails to the thrash. 
     * If Settings.delMailFromServer is true, deletes mails from mails server
     * as well.
     */
    public void deleteMarkedFromBoxAndDB() {
        MessageHeader header;
        MailAccount account;
        int x = 0;
        synchronized (getStorage()) {
            Enumeration messages = getStorage().getEnumeration();
            while (messages.hasMoreElements()) {
                header = (MessageHeader) messages.nextElement();
                if (header.deleted) {
                    msgIDs.remove(header.getAccountID() + "@" + header.getMessageID());
                    if (header.readStatus == MessageHeader.NOT_READ) {
                        setUnreadMails(getUnreadMails() - 1);
                    }
                    if (Settings.delMailFromServer) {
                        account = (MailAccount) getMujMail().getMailAccounts().get(header.getAccountID());
                        if (account != null && account.isActive()) {
                            account.getProtocol().addDeleted(header);
                        }
                    }
                    ++x;
                }
                if (x == deleted) {
                    break;
                }
            }
        }
        if (!isSyncRunning() && Settings.delMailFromServer) {
            for (Enumeration e = getMujMail().getMailAccounts().elements(); e.hasMoreElements(); ) {
                account = (MailAccount) e.nextElement();
                if (account.isActive()) {
                    account.getProtocol().removeMsgs(this);
                }
            }
        }
        super.deleteMarkedFromBoxAndDB();
    }

    protected void hideButtons() {
        if (InProtocol.isBusyGlobal()) {
            addCommand(stop);
        }
        if (isBusy()) {
            removeCommand(retrieve);
        } else {
            if (isEmpty()) {
                addCommand(retrieve);
            }
            removeCommand(stop);
        }
        if (!btnsHidden) {
            removeCommand(redownload);
        }
        super.hideButtons();
    }

    protected void showButtons() {
        if (btnsHidden) {
            addCommand(retrieve);
            addCommand(redownload);
            removeCommand(stop);
            super.showButtons();
        }
    }

    protected void keyPressed(int keyCode) {
        if (isBusy()) {
            return;
        }
        final MessageHeader messageHeader = getStorage().getMessageAt(getSelectedIndex());
        if (keyCode == '3' && messageHeader != null) {
            MessageHeader header = messageHeader;
            header.readStatus = (byte) (1 - header.readStatus);
            shiftSelectedIndex(true);
            if (header.readStatus == MessageHeader.READ) {
                getMujMail().getInBox().changeUnreadMails(-1);
            } else {
                getMujMail().getInBox().changeUnreadMails(1);
            }
            repaint();
            try {
                getMailDB().saveHeader(header);
            } catch (MyException ex) {
                ex.printStackTrace();
                report(ex.getDetails(), SOURCE_FILE);
            }
        } else {
            super.keyPressed(keyCode);
        }
    }

    /**
     * Moves the cursor on the first unread mail.
     */
    public void setCurFirstUnread() {
        if (getUnreadMails() != 0) {
            MessageHeader message;
            cur = 0;
            empties = 0;
            for (int i = 0; i < getMessageCount(); i++) {
                message = getMessageHeaderAt(i);
                if (message.readStatus == MessageHeader.NOT_READ) {
                    break;
                }
                shiftSelectedIndex(true);
            }
        }
    }

    public void stop() {
        removeCommand(stop);
        for (Enumeration e = getMujMail().getMailAccounts().elements(); e.hasMoreElements(); ) {
            MailAccount account = (MailAccount) e.nextElement();
            if (account.isActive()) {
                account.getProtocol().stop();
            }
        }
    }

    public MessageHeader storeMail(MessageHeader header) {
        header = super.storeMail(header);
        if (header != null && header.readStatus == MessageHeader.NOT_READ) {
            setUnreadMails(getUnreadMails() + 1);
            System.out.println("msgIDs = " + msgIDs);
            msgIDs.put(header.getAccountID() + "@" + header.getMessageID(), header);
        }
        return header;
    }

    /**
     * Returns true if there is some account with push active. That means that
     * IMAP command idle was executed and it is waiting for new mails.
     * 
     * @return true if there is some account with push active.
     */
    public boolean isPushActive() {
        return pushActive;
    }

    public synchronized void push() {
        if (!pushActive) {
            if (!hasAccountToRetrieve()) {
                getMujMail().getAlert().setAlert(this, null, Lang.get(Lang.ALRT_AS_NO_ACCOUNT_SET_ACTIVE), MyAlert.DEFAULT, AlertType.WARNING);
            } else {
                pushActive = true;
                for (Enumeration e = getMujMail().getMailAccounts().elements(); e.hasMoreElements(); ) {
                    MailAccount pushAccount = (MailAccount) e.nextElement();
                    if (pushAccount.isActive() && pushAccount.getProtocol().isImap()) {
                        pushAccount.prepareForPushing(this);
                        Pushing push = new Pushing(pushAccount);
                        push.start();
                    }
                }
            }
        } else {
            pushActive = false;
        }
    }

    /**
     * Stops pushing new mails in all accounts.
     */
    public synchronized void stopPush() {
        pushActive = false;
    }

    /**
     * Called from MailDB after loading db (loadDB) is done
     * Note: Overwrites generic body
     */
    public void loadedDB() {
        initHash();
        resort();
    }

    public void commandAction(Command c, Displayable d) {
        super.commandAction(c, d);
        if (c == retrieve) {
            retrieve();
        } else if (c == redownload) {
            regetBody(getSelectedHeader(), (byte) -1, false);
        } else if (c == stop) {
            stop();
        }
    }

    public MessageHeader getLastSafeMail() {
        return lastSafeMail;
    }

    public void setLastSafeMail(MessageHeader lastSafeMail) {
        this.lastSafeMail = lastSafeMail;
    }

    public int getUnreadMailsCount() {
        return getUnreadMails();
    }

    public int getUserBoxListDBRecID() {
        return userBoxListDB_recordID;
    }

    public void setUserBoxListDBRecID(int userBoxListDBRecID) {
        userBoxListDB_recordID = userBoxListDBRecID;
    }

    private void setUnreadMails(int unreadMails) {
        numUnreadMails.saveInteger(unreadMails);
    }

    private int getUnreadMails() {
        return numUnreadMails.loadInteger();
    }

    /**
     * Loads or saves (in background) accounts that 
     *  have to be retrieved by (user folder).
     * <p>
     * Loading is done automatically during creating folder {@link #InBox}
     *
     * @see mujMail.InBox#saveBoxRetrieveAccounts 
     */
    private class RetrieveBoxAccountsTask extends Thread {

        public static final byte ACCOUNTS_LOAD = 1;

        public static final byte ACCOUNTS_SAVE = 2;

        private byte mode;

        RetrieveBoxAccountsTask(byte mode) {
            this.mode = mode;
        }

        public void run() {
            if (InBox.DEBUG) {
                System.out.println("DEBUG RetrieveBoxAccountsTask.run()");
            }
            getMujMail().getAccountSettings().waitForAccountsLoading();
            if (InBox.DEBUG) {
                System.out.println("DEBUG RetrieveBoxAccountsTask.run() after loading of accounts");
            }
            try {
                String dbName = getDBFileName() + "_ACC";
                switch(mode) {
                    case ACCOUNTS_LOAD:
                        RecordStore rs1 = RecordStore.openRecordStore(dbName, true);
                        RecordEnumeration en = rs1.enumerateRecords(null, null, false);
                        byte[] data1 = new byte[128];
                        ByteArrayInputStream buffer1 = new ByteArrayInputStream(data1);
                        DataInputStream stream1 = new DataInputStream(buffer1);
                        while (en.hasNextElement()) {
                            int id = en.nextRecordId();
                            int sizeOfRecord = rs1.getRecordSize(id);
                            if (sizeOfRecord > data1.length) {
                                data1 = new byte[sizeOfRecord + 30];
                                buffer1 = new ByteArrayInputStream(data1);
                                stream1 = new DataInputStream(buffer1);
                            }
                            rs1.getRecord(id, data1, 0);
                            stream1.reset();
                            String accountType = stream1.readUTF();
                            if (MailAccountPrimary.CLASS_TYPE_STRING.equalsIgnoreCase(accountType)) {
                                String email = stream1.readUTF();
                                MailAccount ma = (MailAccount) getMujMail().getMailAccounts().get(email);
                                if (ma == null) {
                                    if (true || InBox.DEBUG) {
                                        System.out.println("Error inbox " + getName() + " mail account " + email + " not exists - willnot be retrieved");
                                    }
                                } else {
                                    accounts.addElement(ma);
                                }
                            } else if (MailAccountDerived.CLASS_TYPE_STRING.equalsIgnoreCase(accountType)) {
                                String email = stream1.readUTF();
                                String imapFld = stream1.readUTF();
                                MailAccount ma = (MailAccount) getMujMail().getMailAccounts().get(email);
                                if (ma == null || imapFld == null) {
                                    if (true || InBox.DEBUG) {
                                        System.out.println("Error inbox " + getName() + " mail account " + email + " (DERIVED) not exists - will not be retrieved");
                                    }
                                } else {
                                    accounts.addElement(new MailAccountDerived(ma, imapFld));
                                }
                            }
                        }
                        stream1.close();
                        buffer1.close();
                        rs1.closeRecordStore();
                        break;
                    case ACCOUNTS_SAVE:
                        try {
                            RecordStore.deleteRecordStore(dbName);
                        } catch (Exception e) {
                        }
                        RecordStore rs2 = RecordStore.openRecordStore(dbName, true);
                        for (int i = 0; i < accounts.size(); i++) {
                            MailAccount ma = (MailAccount) accounts.elementAt(i);
                            ByteArrayOutputStream buffer2 = new ByteArrayOutputStream();
                            DataOutputStream stream2 = new DataOutputStream(buffer2);
                            if (ma.getAccountClassType() == MailAccount.ACCOUNT_CLASS_TYPE_PRIMARY) {
                                stream2.writeUTF(MailAccountPrimary.CLASS_TYPE_STRING);
                                stream2.writeUTF(ma.getEmail());
                            } else if (ma.getAccountClassType() == MailAccount.ACCOUNT_CLASS_TYPE_DERIVED) {
                                stream2.writeUTF(MailAccountDerived.CLASS_TYPE_STRING);
                                stream2.writeUTF(ma.getEmail());
                                stream2.writeUTF(ma.getIMAPPprimaryBox());
                            }
                            stream2.flush();
                            rs2.addRecord(buffer2.toByteArray(), 0, buffer2.size());
                            stream2.close();
                            buffer2.close();
                            if (InBox.DEBUG) {
                                System.out.println("RBAT - successfully saved " + ma.getEmail());
                            }
                        }
                        rs2.closeRecordStore();
                        break;
                }
            } catch (Exception e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
        }
    }
}
