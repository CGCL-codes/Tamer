package de.iqcomputing.flap.mbox;

import java.io.*;
import java.util.*;
import javax.mail.*;
import javax.mail.event.*;
import javax.mail.internet.*;
import de.iqcomputing.util.*;

public class MboxFolder extends Folder {

    private static final FileFilter messageFileFilter = new FileFilter() {

        public boolean accept(File f) {
            String name = f.getName();
            if (name.endsWith(".dat")) {
                try {
                    Integer.parseInt(name.substring(0, name.length() - 4));
                    return true;
                } catch (NumberFormatException e) {
                }
            }
            return false;
        }
    };

    private static final FileFilter dirsFileFilter = new FileFilter() {

        public boolean accept(File f) {
            return f.isDirectory();
        }
    };

    private static final Flags permanentFlags;

    private String name;

    private String fullname;

    private boolean open = false;

    private boolean readOnly;

    private ArrayList messages = new ArrayList();

    private long lastOpenedTime = 0;

    static {
        permanentFlags = new Flags();
        permanentFlags.add(Flags.Flag.DELETED);
        permanentFlags.add(Flags.Flag.RECENT);
        permanentFlags.add(Flags.Flag.SEEN);
    }

    protected MboxFolder(Store store, String name, String fullname) {
        super(store);
        this.name = name;
        this.fullname = fullname;
    }

    public void appendMessages(Message[] msgs) throws MessagingException {
        ArrayList appendedMessages = new ArrayList();
        int i;
        int idx = messages.size() + 1;
        MboxMessage msg;
        for (i = 0; i < msgs.length; i++) {
            msg = new MboxMessage((MimeMessage) msgs[i]);
            messages.add(msg);
            msg.setFolder(this);
            msg.setMessageNumber(idx++);
            appendedMessages.add(msg);
        }
        if (msgs.length > 0) notifyMessageAddedListeners((Message[]) appendedMessages.toArray(new Message[0]));
    }

    public synchronized void open(int mode) throws MessagingException {
        if (!open) {
            readOnly = (mode == READ_ONLY);
            readMessages();
            open = true;
            notifyConnectionListeners(ConnectionEvent.OPENED);
        } else throw new IllegalStateException("folder is not closed");
    }

    public synchronized void close(boolean expunge) throws MessagingException {
        if (open) {
            if (expunge) expunge();
            saveMessages();
            open = false;
            notifyConnectionListeners(ConnectionEvent.CLOSED);
        } else throw new IllegalStateException("folder is not open");
    }

    public boolean create(int type) throws MessagingException {
        boolean okay = getDirectory().mkdirs();
        if (okay) notifyFolderListeners(FolderEvent.CREATED);
        return okay;
    }

    public boolean delete(boolean recurse) throws MessagingException {
        if (open) throw new IllegalStateException("folder is open");
        if (!recurse) return false;
        FileTools.delete(getDirectory());
        return true;
    }

    public boolean exists() throws MessagingException {
        File dir = getDirectory();
        return (dir.exists() && dir.isDirectory());
    }

    public Message[] expunge() throws MessagingException {
        ArrayList newMessages = new ArrayList();
        ArrayList expungedMessages = new ArrayList();
        int num = messages.size();
        int i;
        MboxMessage msg;
        for (i = 0; i < num; i++) {
            msg = (MboxMessage) messages.get(i);
            if (msg.getFlags().contains(Flags.Flag.DELETED)) {
                msg.expunge();
                expungedMessages.add(msg);
            } else newMessages.add(msg);
        }
        messages = newMessages;
        renumberMessages();
        {
            Message[] exMsgs = (Message[]) expungedMessages.toArray(new Message[0]);
            notifyMessageRemovedListeners(true, exMsgs);
            return exMsgs;
        }
    }

    private void renumberMessages() throws MessagingException {
        int num = messages.size();
        int i;
        for (i = 0; i < num; i++) ((MboxMessage) messages.get(i)).renumber(i + 1);
    }

    public Folder getFolder(String name) {
        return new MboxFolder(store, name, fullname + "/" + name);
    }

    public String getName() {
        return name;
    }

    public String getFullName() {
        if (getParent() != null) return fullname;
        return name;
    }

    public Message getMessage(int idx) throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        if (!open) throw new IllegalStateException("folder is not open");
        if ((idx < 1) || (idx > getMessageCount())) throw new IndexOutOfBoundsException("message index out of bounds: " + idx + " <> " + getMessageCount());
        return (Message) messages.get(idx - 1);
    }

    public int getMessageCount() throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        if (open) return messages.size();
        return -1;
    }

    public Folder getParent() {
        int posLastSlash = fullname.lastIndexOf('/');
        if (posLastSlash <= 0) return null;
        {
            int posSlashBefore = fullname.lastIndexOf('/', posLastSlash - 1);
            return new MboxFolder(store, fullname.substring(posSlashBefore + 1, posLastSlash), fullname.substring(0, posLastSlash));
        }
    }

    public Flags getPermanentFlags() {
        return (Flags) permanentFlags.clone();
    }

    public char getSeparator() throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        return '/';
    }

    public int getType() throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        return HOLDS_MESSAGES + HOLDS_FOLDERS;
    }

    public boolean hasNewMessages() throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        if (open) {
            int num = messages.size();
            int i;
            MboxMessage msg;
            for (i = 0; i < num; i++) {
                msg = (MboxMessage) messages.get(i);
                if (msg.getReceivedDate().getTime() > lastOpenedTime) return true;
            }
        }
        return false;
    }

    public boolean isOpen() {
        return open;
    }

    public boolean isSubscribed() {
        return false;
    }

    public Folder[] list(String pattern) throws MessagingException {
        if (pattern.equals("%")) {
            File[] dirs = getDirectory().listFiles(dirsFileFilter);
            int i;
            if (dirs != null) {
                Folder[] folders = new Folder[dirs.length];
                for (i = 0; i < dirs.length; i++) folders[i] = new MboxFolder(store, dirs[i].getName(), fullname + "/" + dirs[i].getName());
                return folders;
            }
        }
        return new Folder[0];
    }

    public boolean renameTo(Folder f) throws MessagingException {
        if (!exists()) throw new FolderNotFoundException("folder does not exist", this);
        if (f.exists()) throw new IllegalStateException("cannot rename to existing folder");
        if (fullname.equals("")) throw new MethodNotSupportedException("cannot rename root folder");
        {
            MboxFolder folder = (MboxFolder) f;
            boolean okay = getDirectory().renameTo(folder.getDirectory());
            if (okay) {
                this.name = folder.name;
                this.fullname = folder.fullname;
                notifyFolderRenamedListeners(this);
            }
            return okay;
        }
    }

    File getDirectory() throws MessagingException {
        MboxStore store = (MboxStore) this.store;
        File storeDir = store.getDirectory();
        String dir = getFullName();
        if (dir.equals("")) return storeDir;
        if (dir.charAt(0) == '/') dir = dir.substring(1);
        return new File(storeDir, dir.replace('/', File.separatorChar));
    }

    private void readMessages() throws MessagingException {
        File dir = getDirectory();
        File[] files = dir.listFiles(messageFileFilter);
        int i;
        MboxMessage msg;
        messages = new ArrayList();
        for (i = 0; i < files.length; i++) {
            msg = new MboxMessage(this, MboxMessage.getMessageContentStream(this, i + 1), i + 1);
            msg.setMessageNumber(getMessageNumber(files[i].getName()));
            messages.add(msg);
        }
    }

    private int getMessageNumber(String filename) {
        return Integer.parseInt(filename.substring(0, filename.length() - 4));
    }

    private void saveMessages() throws MessagingException {
        int num = messages.size();
        int i;
        for (i = 0; i < num; i++) ((MboxMessage) messages.get(i)).saveChanges();
    }
}
