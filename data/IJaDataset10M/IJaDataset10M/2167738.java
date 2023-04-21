package net.sf.mailsomething.mail.parsers;

import java.util.Date;
import java.util.Vector;
import java.util.StringTokenizer;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import net.sf.mailsomething.mail.*;
import net.sf.mailsomething.util.ProgressListener;

/**
 *  Class which parses the returnvalues from the Imap-server (Imapsession). A
 *  bridge between the mail module and the imapprotocol. If this should be
 *  correct every method should take imap-compliant strings as arguments, and
 *  not mail-module specific components. Eg. When a messageid is required
 *  argument should be String messageid and not Message. But what with
 *  returnvalues? ...... Logic? Inputarguments imapcompliant, output mailmodule
 *  compliant.... ??? Every output should then be interfaces in order to allow
 *  different implementations. Thoughts about this class: The session-class
 *  should implement every possible imap-command, while the imap-controller
 *  decides which to use and how. I should do some interface. With the methods
 *  the imap-account expects to call. Idea is, that this class could be changed
 *  with another controller, eg several controllers for several
 *  servers/imap-implementations etc. There is some collapsing in methods etc.
 *  One could state clear - imapaccount has a method called getProtocol() which
 *  returns "imap" and a imapcontroller should have a method called getRevision
 *  return eg "imaprev4.1". There is an issue with they way it fetches messages,
 *  eg. for the moment it does 1 at a time, since theres no method to check if
 *  the messages all lie in the same range. If they do its possible to fetch all
 *  at a time, which would be a better implementation. This (private) method
 *  should be written and integrated sooner or later. The implementation of the
 *  BasicMailChecker checks for new mails in INBOX. In order for it to traverse
 *  the whole mailbox-hierachy it would be better if the ImapAccount implemented
 *  the BasicMailChecker, since it has references to every mailbox and every
 *  message. Perhabs thats a difference between a Basic mail checker, and a more
 *  complete synchronization!!! The synchronization should be done through the
 *  account class. 
 * 
 * Todo/bugs: use of ssl,
 * 
 * 
 * 
 * 
 *
 *@author     Stig Tanggaard
 *@created    june 28, 2001
 *@changed    November 3, 2001
 *@version    0.31
 */
public class ImapController extends SessionAdapter {

    private static String delimiter = "/";

    private static final String FETCH_UID = "(UID)";

    private static final String FETCH_HEADER = "(FLAGS BODY[HEADER] UID RFC822.SIZE)";

    /**String flags for fetching a full header.  (FLAGS BODY[TEXT])
	 * Seems like BODY[] fetches all body, while BODY[TEXT] fetches
	 * the textpart (which varies upon messages - sometimes attachments seems
	 * to be regarded as text). But since we check the size of the message
	 * before fetching it, we can just fetch full body.
	 * One consideration is - does this also fetch the header??
	 * Header IS included, ie, we need to remove header first.
	 */
    private static final String FETCH_BODY = "(FLAGS BODY[])";

    private static final String FETCH_ID = "(FLAGS BODY[HEADER.FIELDS (MESSAGE-ID)])";

    private static final String FETCH_SIZE = "(FlAGS RFC822.SIZE)";

    transient ImapSession session;

    private ImapAccount account;

    private String selected;

    private int exists = 0;

    private int recent = 0;

    private boolean ioExceptionThrown = false;

    private Date noopdate;

    private Timer noopTimer = null;

    /**
	 *  Constructor for ImapController. As a minimum an acocunt with host, port,
	 *  username and password is required.
	 *
	 *@param  account  Description of Parameter
	 */
    public ImapController(ImapAccount account) {
        super(account.getServerTimeout() * 1000);
        this.account = account;
        session = new ImapSession(account);
    }

    /**
	 *  Method for getting the size from a fetch size reply.
	 *
	 *@param  string  Description of Parameter
	 *@return         Description of the Returned Value
	 */
    private static int parseSize(String[] string) {
        int k;
        int size = 0;
        for (int i = 0; i < string.length; i++) {
            if ((k = string[i].indexOf("RFC822.SIZE")) != -1) {
                String rest = string[i].substring(k + 12);
                char[] array = rest.toCharArray();
                int j = 0;
                while (j < array.length) {
                    try {
                        Integer.parseInt("" + array[j]);
                    } catch (NumberFormatException f) {
                        break;
                    }
                    j++;
                }
                size = Integer.parseInt(rest.substring(0, j));
                return size;
            }
        }
        return -1;
    }

    /**
	 *  For now this method returns an array with names of mailboxes. If it
	 *  returned an array of mailboxes one would assume those mailboxes where fully
	 *  investigated for mails, which I dont want to do at moment. Maybe I would
	 *  add some method later which investigates the whole mailtree including
	 *  mailboxes, mails, mailboxes in mailboxes, etc. If u want to list the root
	 *  supply an empty string as argument.
	 *
	 *@param  mailboxPath  The path of the mailbox in the mailbox heirachy
	 *@return              A list of names of mailboxes in the mailbox given as
	 *      argument.
	 */
    public synchronized MailboxListResult listMailboxes(String mailboxPath) {
        initSession();
        if (!isConnected) return new MailboxListResult();
        String listCommand = doList(mailboxPath);
        String[] returnLines = session.list(listCommand);
        notifyListenersFromBuffer("retrieving a list of folders in: " + mailboxPath, SessionEvent.DEBUG);
        MailboxListResult result = new MailboxListResult(returnLines.length);
        for (int i = 0; i < returnLines.length; i++) {
            returnLines[i] = returnLines[i].trim();
            if (returnLines[i].indexOf("\\HasNoChildren") != -1) {
                result.hasChildren[i] = false;
            } else {
                result.hasChildren[i] = true;
            }
            if (returnLines[i].charAt(returnLines[i].length() - 1) == '"') {
                int index = returnLines[i].length() - 2;
                while (returnLines[i].charAt(index) != '"') {
                    index--;
                    if (index == 0) break;
                }
                returnLines[i] = returnLines[i].substring(index + 1, returnLines[i].length() - 1);
            } else {
                int n = returnLines[i].lastIndexOf(" ");
                if (n > 0) returnLines[i] = returnLines[i].substring(n + 1);
            }
            returnLines[i].trim();
            if (!mailboxPath.equals("")) {
                if (mailboxPath.startsWith("/")) {
                    mailboxPath = mailboxPath.substring(1);
                }
                int n = 0;
                if ((n = returnLines[i].indexOf(mailboxPath)) != -1) {
                    returnLines[i] = returnLines[i].substring(n + mailboxPath.length() + 1);
                }
            }
            if (returnLines[i].endsWith("/")) {
                returnLines[i] = returnLines[i].substring(0, returnLines[i].length() - 1);
            }
            result.mailboxNames[i] = returnLines[i];
        }
        return result;
    }

    /**
	 *  gets the exists value of the last selection.
	 *
	 *@return    nr of messages in mailbox.
	 */
    public synchronized int getExists() {
        return exists;
    }

    /**
	 *  gets the recent value of the last selection.
	 *
	 *@return    nr of recent messages in mailbox.
	 */
    public synchronized int getRecent() {
        return recent;
    }

    /**
	 *  Reluctant I included this method. If the design works I cant see a need for
	 *  it, but while testing it was usefull and then it might is usefull outside
	 *  testing. 
	 *
	 *@param  path  The mailboxpath, if null it assumes the current selected
	 *      mailbox.
	 *@param  nr    the nr-id of the message in the mailbox. Eg. message nr 1, nr2
	 *      etc.
	 *@return       Description of the Returned Value
	 */
    public synchronized Message getMessage(String path, int nr) {
        if (selectMailbox(path) == -1) return null;
        Message m = MailDecoder.decodeMail(session.fetchBytes(FETCH_BODY, nr, null));
        return m;
    }

    /**
	 * For fetching only the header part of the message. This is being used
	 * when messages are bigger than the maxlimit size setting of the
	 * account class. That way, the message will be fetched, but only header,
	 * and user should be able to choose if he wants to download all message.
	 * 
	 * This also sets the UID value of the returned message.
	 * 
	 * 
	 */
    public synchronized Message getHeader(String path, int nr) {
        if (path != null) {
            selectMailbox(path);
        }
        Message m = MailService.createMessage();
        String[] lines = session.fetch(FETCH_HEADER, nr);
        for (int i = lines.length - 1; i >= 0; i--) {
            if (lines[i].indexOf("UID") != -1) {
                m.setUID(MailDecoder.decodeUID(lines[i]));
            }
        }
        MailDecoder.decodeHeader(MailDecoder.unfoldLines(lines), m);
        return m;
    }

    /**
	 * 
	 * 
	 * @param handler
	 * @param path
	 * @param startIndex
	 * @param endIndex
	 */
    public synchronized void getHeader(ObjectHandler handler, String path, int startIndex, int endIndex) {
        if (path != null) {
            selectMailbox(path);
        }
        BufferedReader in = session.fetch(FETCH_HEADER, startIndex, endIndex);
        Message m;
        String command = "FETCH " + startIndex + ":" + endIndex + " " + FETCH_HEADER;
        String tag = session.getLastTag();
        ImapHeaderIterator iterator = new ImapHeaderIterator(in, tag);
        long time = System.currentTimeMillis();
        try {
            while (iterator.hasMore()) {
                handler.handle(iterator.nextHeader());
            }
        } catch (IOException f) {
            notifyIOException(f);
        }
    }

    /**
	 * Fetches the body of the suplied message. Presumes the header of the
	 * message is allready fetched.
	 * 
	 * Note: this isnt correct regarding parsing server reply.
	 * 
	 * @param path the mailbox path
	 * @param index the index of the message
	 * @param message the message to fetch
	 */
    public synchronized Message getBody(String path, int index, Message message, ProgressListener listener) {
        if (listener != null) {
            listener.workStarted(message.getSize());
        }
        if (path != null) {
            selectMailbox(path);
        }
        byte[] bytes = session.fetchBytes(FETCH_BODY, index, listener);
        notifyListenersFromBuffer("Fetched body of message with index number: " + index, SessionEvent.DEBUG);
        MailDecoder.decodeMail(bytes, message);
        if (listener != null) {
            listener.workStopped();
        }
        return message;
    }

    /**
	 * Gets the index of the message with the specified uid. The index
	 * is the placement of the message in the mailbox relatively to
	 * other messages. While uid is a messageattribute which allways
	 * is incremented, the index is adjusted when messages are deleted
	 * or similar.
	 * 
	 * @param uid
	 * @return
	 */
    public int getIndexOfMessage(String uid) {
        String command = "UID " + uid + ":" + uid;
        try {
            String[] reply = session.search(command);
            boolean ok = false;
            for (int i = 0; i < reply.length; i++) {
                if (reply[i].indexOf(session.getLastTag()) != -1) {
                    if (reply[i].indexOf("OK") != -1) {
                        ok = true;
                        break;
                    }
                }
            }
            if (!ok) {
                notifyListenersFromBuffer("Didnt find uid in mailbox", SessionEvent.DEBUG);
                return -1;
            }
            String[] nr = parseSearch(reply);
            int index = 0;
            if (nr.length > 0) {
                try {
                    index = Integer.parseInt(nr[0]);
                    return index;
                } catch (NumberFormatException f) {
                    return -1;
                }
            } else {
                return -1;
            }
        } catch (IOException f) {
            notifyIOException(f);
            return -1;
        }
    }

    /**
	 *  This method doenst do a initSession (noop and perhabs reconnect) 
	 * since its likely to be
	 * called after getting a list of sequense numbers,
	 *  eg. still connected.
	 *
	 *@param  nr  the current sequence number of the message
	 *@return     The uniqie identifier of the message.
	 */
    public synchronized String getUid(int nr) {
        String reply[] = session.fetch("UID", nr);
        String uidLine = reply[0];
        int k;
        String uid = null;
        if ((k = uidLine.indexOf("UID")) != -1) {
            int kk;
            if ((kk = uidLine.indexOf(")", k + 1)) != -1) {
                uid = uidLine.substring(k + 4, kk);
            }
        }
        return uid;
    }

    /**
	 * Fetched the uid for a sequence of message indexes. 
	 * 
	 * TODO: the implementation in this method should be moved to
	 * imapsession.
	 * 
	 * @param startIndex
	 * @param endIndex
	 * @return An array of uids, or null if an exception occured
	 */
    public String[] fetchUID(int startIndex, int endIndex) {
        Vector buffer = new Vector();
        BufferedReader reader = session.fetch(FETCH_UID, startIndex, endIndex);
        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                session.notifyFrom(line);
                if (line.indexOf(session.getLastTag()) != -1) {
                    break;
                }
                if (line.indexOf("UID") != -1) {
                    String uid = MailDecoder.decodeUID(line);
                    buffer.add(uid);
                }
            }
            return (String[]) buffer.toArray(new String[] {});
        } catch (IOException f) {
            notifyIOException(f);
            return null;
        }
    }

    /**
	 *  Gets the messageSize of the message of the index in the mailbox of
	 * path.
	 * 
	 * 
	 *
	 *@param  path   the mailbox path
	 *@param  index  index of the message
	 *@return        the full message size in bytes
	 */
    public synchronized int getMessageSize(String path, int index) {
        if (path != null) {
            selectMailbox(path);
        }
        String[] reply = session.fetch(FETCH_SIZE, index);
        int size = parseSize(reply);
        return size;
    }

    /**
	 * This is an updated selectcommand. It has better checking and
	 * should be almost finished. 
	 * 
	 * It sets the Exists and Recent values based on the response from
	 * 	the server. 
	 * 
	 * This method calls initSession to check the connection. This
	 * means that methods calling this dont need to call initsession
	 * themself. 
	 * 
	 *
	 *@param  path  the path to the mailbox
	 *@return       1 if the selection succeeded, -1 if not. 0 if its 
	 *				allready selected.
	 */
    public synchronized int selectMailbox(String path) {
        initSession();
        if (!isConnected) return -1;
        if (path.equals(selected)) {
            return 0;
        }
        try {
            String[] replies = session.select(doSelect(path));
            String tag = session.getLastTag();
            String tagReplyLine = null;
            for (int i = 0; i < replies.length; i++) {
                if (replies[i].indexOf(tag) != -1) {
                    tagReplyLine = replies[i];
                    break;
                }
            }
            if (tagReplyLine == null) return -1;
            if (tagReplyLine.indexOf("NO") != -1) {
                return -1;
            } else {
                parseExistsRecent(replies);
                selected = path;
                return 1;
            }
        } catch (IOException f) {
            notifyIOException(f);
            return -1;
        }
    }

    public String getSelectedMailbox() {
        return selected;
    }

    /**
	 *  see searchrecent
	 *
	 *@param  path  Description of Parameter
	 *@return       The messages value
	 */
    public synchronized String[] searchUnseen(String path) {
        initSession();
        if (!isConnected) return new String[] {};
        if (path != null) {
            selectMailbox(path);
        }
        try {
            String[] serverreply = session.search("UNSEEN");
            parseExistsRecent(serverreply);
            return getSequenceNumbersFromExistsRecent();
        } catch (IOException f) {
            notifyIOException(f);
            return new String[] {};
        }
    }

    /**
	 *  Only difference between this and fetchUNSEEN is the flag used in the search
	 *  method.
	 * This method uses the server result to return a string array of 
	 * sequencenumbers. The serverreply is like:
	 * 356 exists
	 * 1 recent
	 * it will then return new String[]{"365"};
	 * 
	 * The calling class doesnt neccessarely need to rely on the 
	 * returnvalue, it can just call 
	 * getRecent
	 * getExists
	 * itself to check the result of the command. 
	 *
	 *@param  path  Description of Parameter
	 *@return       Description of the Returned Value
	 */
    public synchronized String[] searchRecent(String path) {
        initSession();
        if (!isConnected) return new String[] {};
        if (path != null) {
            selectMailbox(path);
        }
        try {
            String[] serverreply = session.search("RECENT");
            parseExistsRecent(serverreply);
            return getSequenceNumbersFromExistsRecent();
        } catch (IOException f) {
            notifyIOException(f);
            return new String[] {};
        }
    }

    public synchronized void fetchMessageIds(String path, String first, String last) {
        selectMailbox(path);
        String[] serverreply = session.fetch(FETCH_ID, first, last);
        for (int i = 0; i < serverreply.length; i++) {
            if (serverreply[i].indexOf("Message") != -1) {
                int start = serverreply[i].indexOf(" ");
                int end = serverreply[i].indexOf(" ", start + 2);
                if (end == -1) {
                } else {
                }
            }
        }
    }

    /**
	 * Method for finding the UID of a message with argument messageid.
	 * Returns null if nothing is found.
	 * 
	 * C: A282 SEARCH FLAGGED SINCE 1-Feb-1994 NOT FROM "Smith"
	 * S: * SEARCH 2 84 882
	 * S: A282 OK SEARCH completed
	 * 
	 * Note: this is a bad implementation.
	 * 
	 */
    public synchronized String searchUID(String path, String messageid) {
        selectMailbox(path);
        try {
            String[] serverreply = session.search("HEADER Message-Id " + messageid);
            String[] nr = parseSearch(serverreply);
            if (nr.length >= 1) return nr[0];
        } catch (IOException f) {
            notifyIOException(f);
        }
        return null;
    }

    /**
	 *  Searches and returns all messageids of mailbox. 
	 * Does this return uid or messageid?? Looks like its uid or 
	 * sequencenumbers and not messageids. 
	 * 
	 * TODO: needs a lookover.
	 *
	 *@param  path  Description of Parameter
	 *@return       Description of the Returned Value
	 */
    public synchronized String[] listMessageIDs(String path) {
        initSession();
        if (!isConnected) return new String[] {};
        selectMailbox(path);
        try {
            String[] serverreply = session.search("ALL");
            String[] nr = parseSearch(serverreply);
            String[] messageid = new String[nr.length];
            for (int i = 0; i < nr.length; i++) {
                messageid[i] = getUid(i + 1);
            }
            return messageid;
        } catch (IOException f) {
            notifyIOException(f);
            return new String[] {};
        }
    }

    /**
	 *  Searches for messages after the date-argument, and returns 
	 * an array of sequencenumbers.
	 * 
	 * TODO: check if and how this work, do some error checking. 
	 * Change date parameter to Date not String.
	 *
	 *@param  path  the mailbox path
	 *@param  date  the date to use. 
	 *@return       Description of the Returned Value
	 */
    public synchronized String[] searchDate(String path, String date) {
        selectMailbox(path);
        try {
            String[] serverreply = session.search("SINCE " + date);
            String[] nr = parseSearch(serverreply);
            return nr;
        } catch (IOException f) {
            notifyIOException(f);
            return new String[] {};
        }
    }

    /**
	 *  The only reason for using this method would be, when client havent been
	 *  invoked on this account before, eg. it doesnt have any date attribute.
	 *
	 *@param  path  Description of Parameter
	 *@return       The messages value
	 */
    public synchronized String[] searchAll(String path) {
        initSession();
        if (!isConnected) return new String[] {};
        selectMailbox(path);
        try {
            String[] serverreply = session.search("ALL");
            return parseSearch(serverreply);
        } catch (IOException f) {
            notifyIOException(f);
        }
        return new String[] {};
    }

    /**
	 *  Method to copy a message from one mailbox to another. This
	 * is prefered to appending the message.
	 * Which is ofcourse obvious when we are talking about
	 * big messages.
	 * 
	 * TODO: should return a value
	 * 
	 *
	 *@param  uid      The uid of the message to copy.
	 *@param  orgpath  The path of the original mailbox
	 *@param  newpath  The path of the new mailbox
	 */
    public synchronized void copyMessage(String uid, String orgpath, String newpath) {
        selectMailbox(orgpath);
        if (!isConnected) return;
        String command = "UID " + uid + ":" + uid;
        try {
            String[] serverreply = session.search(command);
            String[] nr = parseSearch(serverreply);
            if (nr != null & nr.length > 0) {
                session.copy(nr[0], nr[0], doPath(newpath));
            }
        } catch (IOException f) {
            notifyIOException(f);
        }
    }

    /**
	 *  for selecting a mailbox ??? String[] temp =
	 *  session.setMailbox(mailbox.getName() + delimiter);
	 *
	 *  for selecting a mailbox ??? String[] temp =
	 *  session.setMailbox(mailbox.getName() + delimiter); for selecting a mailbox
	 *  ??? String[] temp = session.setMailbox(mailbox.getName() + delimiter);
	 *  Deletes a mailbox. Takes path as argument. eg. inbox/somemailbox
	 *
	 *@param  path  Description of Parameter
	 *@return       Description of the Returned Value
	 *@return       True if mailbox is deleted.
	 */
    public synchronized boolean deleteMailbox(String path) {
        String[] reply = session.delete(doPath(path));
        if (reply.length > 0) if (reply[0].indexOf("OK") != -1) {
            notifyListenersFromBuffer("Deleting mailbox", SessionEvent.DEBUG);
            return true;
        } else {
            notifyListenersFromBuffer("Deleting mailbox", SessionEvent.DEBUG);
            return false;
        }
        return true;
    }

    /**
	 *  See describtion of deleteMailbox. Is missing parsing of returnvalue,
	 *  success or not.
	 *
	 *@param  path  the mailbox path
	 *@return       If the creation was successfull.
	 */
    public synchronized boolean createMailbox(String path) {
        initSession();
        if (!isConnected) return false;
        String[] reply = session.create(doPath(path));
        if (reply.length > 0) if (reply[0].indexOf("OK") != -1) {
            notifyListenersFromBuffer("Creating mailbox", SessionEvent.DEBUG);
            return true;
        } else {
            notifyListenersFromBuffer("Creating mailbox", SessionEvent.DEBUG);
            return false;
        }
        return true;
    }

    /**
	 *  Renames a mailbox. This method works well as a copy mailbox, since the
	 *  mailbox keeps all children. one can rename as: rename("someRootMailbox",
	 *  "someOtherRootMailbox/newMailboxInThat"); which resembles a copy or move
	 *  action.
	 *
	 *@param  oldpath  Path of the mailbox being renamed.
	 *@param  newpath  New path of the mailbox being renamed.
	 *@return          Returns true if successfull.
	 */
    public synchronized boolean renameMailbox(String oldpath, String newpath) {
        initSession();
        if (!isConnected) return false;
        String[] reply;
        try {
            reply = session.rename(doPath(oldpath), doPath(newpath));
        } catch (IOException f) {
            notifyListenersFromBuffer("Renaming mailbox failure", SessionEvent.ERROR);
            return false;
        }
        if (reply[0].indexOf("OK") != -1) {
            notifyListenersFromBuffer("Renaming mailbox success", SessionEvent.DEBUG);
            return true;
        } else {
            notifyListenersFromBuffer("Renaming mailbox failure", SessionEvent.ERROR);
            return false;
        }
    }

    /**
	 *  Testing code for appending messages to an imap folder.
	 * 
	 * There is a good example on the append message command
	 * in the new imap rfc. 
	 * 
	 * it works now. The uid is set (but im not sure the format
	 * is correct, have to do some more testing). 
	 * 
	 * It still misses a couple things, like the method should check
	 * if flags like DRAFT is set, and then store them for the appended
	 * message.
	 *
	 *@param  m  Description of Parameter
	 *@return    Description of the Returned Value
	 */
    public synchronized boolean appendMessage(Message m, String mailboxPath) {
        selectMailbox(mailboxPath);
        if (!isConnected) return false;
        notifyListenersFromBuffer("clearing cache", SessionEvent.INFO);
        if (mailboxPath.startsWith("/")) {
            mailboxPath = mailboxPath.substring(1);
        }
        if (mailboxPath.indexOf(" ") != -1) {
            mailboxPath = '"' + mailboxPath + '"';
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            MailEncoder.encodeMail(m, stream);
            String reply = "";
            if (m.isDraft()) reply = session.appendStart(mailboxPath, "(\\Seen \\Draft)", stream.size()); else if (m.isSent()) reply = session.appendStart(mailboxPath, "(\\Seen $MDNSent)", stream.size()); else reply = session.appendStart(mailboxPath, "(\\Seen)", stream.size());
            if (reply.indexOf("+") == -1) return false;
        } catch (IOException f) {
            notifyIOException(f);
            return false;
        }
        try {
            session.append(stream.toByteArray());
            String[] replies = getConversation();
            for (int i = 0; i < replies.length; i++) {
                if (replies[i].indexOf("APPENDUID ") != -1) {
                    int uidindex = replies[i].indexOf("APPENDUID");
                    int firstSpace = replies[i].indexOf(" ", uidindex);
                    int secondSpace = replies[i].indexOf(" ", firstSpace + 1);
                    int enduid = replies[i].indexOf("]", secondSpace + 1);
                    String uid = replies[i].substring(secondSpace, enduid);
                    uid = uid.trim();
                    try {
                        Integer.parseInt(uid);
                        m.setUID(uid);
                        break;
                    } catch (NumberFormatException f) {
                        f.printStackTrace();
                    }
                }
            }
        } catch (IOException f) {
            notifyIOException(f);
            return false;
        }
        notifyListenersFromBuffer("clearing cache", SessionEvent.INFO);
        return true;
    }

    /**
	 *  Method does a search on uid before deleting. 
	 * 2 other ways of identifying the sequencenumber
	 * would be searching by Header.date, and Header.messageid. 
	 * The methods for those should be done
	 * as a way to handle problems with this method 
	 * (though there shouldnt be any, unless the uids change, 
	 * which on other hand could be solved by a uidupdating method 
	 * (which again needed some way to identify
	 * the messages, eg. date and messageid).
	 * 
	 * TODO: this should return a value, success or not.
	 * 
	 * 
	 * //String command = "HEADER " + MessageConstants.MESSAGE_ID + " " + uid;
		//String command = "HEADER " + MessageConstants.DATE + " " + date;
	 *
	 *@param  path  the path of the mailbox
	 *@param  uid   the unique identifier of the message
	 */
    public synchronized void deleteMessage(String path, String uid) {
        if (selectMailbox(path) == -1) return;
        try {
            int index = getIndexOfMessage(uid);
            if (index == -1) return;
            String[] reply = session.store("+FLAGS (" + "\\" + "Deleted)", index);
            session.expunge();
            notifyListenersFromBuffer("Deleting message", SessionEvent.DEBUG);
        } catch (IOException f) {
            notifyIOException(f);
        }
    }

    /**
	 *  Description of the Method
	 */
    public void deleteAllMessages() {
    }

    /**
	 *  Gets the intValue attribute of the ImapController object
	 *
	 *@param  line  Description of Parameter
	 *@return       The intValue value
	 */
    private int getIntValue(String line) {
        int k = 0;
        int kk = 0;
        while ((k = line.indexOf(" ", k + 1)) != -1) {
            kk = k;
        }
        return Integer.parseInt(line.substring(kk).trim());
    }

    /**
	 *  parsing method for getting eg the value of a headerfield, where u specify
	 *  the headerfield name as searchItem, and inputarray could be the whole
	 *  server-reply.
	 *
	 *@param  searchItem  Description of Parameter
	 *@param  inputArray  Description of Parameter
	 *@return             The returnValue value
	 */
    private String getReturnValue(String searchItem, String[] inputArray) {
        String value = "";
        int k;
        for (int i = 1; i < inputArray.length; i++) {
            if ((k = inputArray[i].indexOf(searchItem)) != -1) {
                value = inputArray[i].substring(k + searchItem.length());
            }
        }
        return value;
    }

    /**
	 *  Method for searching for new mails. Its used by the running mailcheching,
	 *  and I decided to make it private for now, since it would be bad practice if
	 *  anyone started to using this method instead of adding a MailListener. On
	 *  the other hand, the full synchronization of a mailbox might wanna use this
	 *  method. (or not, since it returns recent messages, and the full sync have
	 *  no use for that. One could argue between the use of the RECENT search flag
	 *  instead of the UNSEEN flag. Maybe that should be decided by the listener
	 *  through a setNewMailFlag.... the issue is: using the RECENT flag would
	 *  erase the new mail, eg. after searching they arent recent anymore. While
	 *  unseen on the other hand would return both new mails, but also old mails
	 *  NOT SEEN. For the moment I implement it with RECENT.
	 *
	 *@param  path             Description of Parameter
	 *@return                  Description of the Returned Value
	 *@exception  IOException  Description of Exception
	 */
    private synchronized int searchNewMails(String path) throws IOException {
        selectMailbox(path);
        String[] serverreply = session.search("RECENT");
        String[] nr = parseSearch(serverreply);
        if (nr != null) {
            return nr.length;
        }
        return 0;
    }

    /**
	 *  Private method for fetching an array of messagenumbers. This method doesnt
	 *  do anything but fetching the numbers, eg. no searching or selecting. It
	 *  fetches both header and body, and returns a lib.mail.Message array. Should
	 *  fetch server-uid too..... in case there is no messageid.
	 *
	 *@param  nr  a array of numbers to fetch. Not equal to UID, but the current
	 *      number of the message.
	 *@return     an array of fetched messages.
	 */
    private Message[] fetch(String[] nr) {
        Vector messagebuffer = new Vector();
        for (int i = 0; i < nr.length; i++) {
            try {
                Message m = MailService.createMessage();
                MailDecoder.decodeMail(session.fetchBytes(FETCH_BODY, Integer.parseInt(nr[i]), null), m);
                messagebuffer.add(m);
            } catch (NumberFormatException e) {
            }
        }
        Message[] messages = new Message[messagebuffer.size()];
        messages = (Message[]) messagebuffer.toArray(messages);
        return messages;
    }

    /**
	 *  Method for parsing the reply of a select command. Or a noop
	 * reply which takes the same form. 
	 * This is bulletproof, ie, u can supply several lines as arguments
	 * which isnt important, it will just look for the ones containing
	 * EXISTS or RECENT reply.
	 *
	 *@param  temp  serverreplies.
	 */
    private void parseExistsRecent(String[] temp) {
        int k = 0;
        for (int i = 0; i < temp.length; i++) {
            if ((k = temp[i].indexOf("EXISTS")) != -1) {
                exists = getIntValue(temp[i].substring(0, k - 1).trim());
            }
            if ((k = temp[i].indexOf("RECENT")) != -1) {
                recent = getIntValue(temp[i].substring(0, k - 1).trim());
            }
        }
    }

    /**
	 *  Method for creating a list command. Puts a delimiter in end and removes
	 *  from front. This method seems modulespecific and not imapspecific, this
	 *  should probably be done in the account or mailbox class.
	 *
	 * The list command adds additional chars between "somepath" which makes it impossible to do a 
	 * doPath command instead.
	 *
	 *@param  mailboxPath  Description of Parameter
	 *@return              Description of the Returned Value
	 */
    private String doList(String mailboxPath) {
        String listCommand = "";
        if (mailboxPath.equals("")) {
            return listCommand;
        }
        if (mailboxPath.charAt(0) == '/') {
            mailboxPath = mailboxPath.substring(1);
        }
        listCommand = mailboxPath + delimiter;
        return listCommand;
    }

    public static String doSelect(String mailboxPath) {
        String listCommand = "";
        if (mailboxPath.equals("")) {
            return listCommand;
        }
        if (mailboxPath.charAt(0) == '/') {
            mailboxPath = mailboxPath.substring(1);
        }
        listCommand = mailboxPath;
        return listCommand;
    }

    /**
	 *  Check if the connection is alive, and connects and resets some 
	 * fields if it isnt. Should probably have some timer, eg. only 
	 * doing a noop if some time has passed. Its stupid to do a noop 
	 * when eg. fetching a mailboxheirachy.
	 */
    private void initSession() {
        if (socket == null || socket.isClosed() || socket.isInputShutdown()) {
            isConnected = false;
        }
        if (isConnected) {
        } else {
            connect(session, account);
            if (!isConnected) return;
            login();
            notifyListenersFromBuffer("Logging into server", SessionEvent.DEBUG);
            selected = null;
        }
    }

    /**
	 *  The old do-list command included a check for space character. And sourounds
	 *  string with "in case of spaces" .
	 *
	 *@param  mailboxPath  Description of Parameter
	 *@return              Description of the Returned Value
	 */
    public static String doPath(String mailboxPath) {
        String listCommand = "";
        if (mailboxPath.equals("")) {
            return listCommand;
        }
        if (mailboxPath.charAt(0) == '/') {
            mailboxPath = mailboxPath.substring(1);
        }
        listCommand = mailboxPath + delimiter;
        if (listCommand.indexOf(" ") != -1) {
            listCommand = '"' + listCommand + '"';
        }
        return listCommand;
    }

    /**
	 *  Method for parsing a searchreply, when the reply consists of numbers of
	 *  messages. Returns a String array of numbers, which could seem a litle
	 *  contradictive :)
	 *
	 *@param  reply  Description of Parameter
	 *@return        Description of the Returned Value
	 */
    private static String[] parseSearch(String[] reply) {
        Vector num = new Vector();
        for (int i = 0; i < reply.length; i++) {
            StringTokenizer st = new StringTokenizer(reply[i], " ");
            while (st.hasMoreTokens()) {
                String next = st.nextToken();
                try {
                    Integer.parseInt(next);
                    num.add(next);
                } catch (NumberFormatException e) {
                }
            }
        }
        String[] output = new String[num.size()];
        output = (String[]) num.toArray(output);
        return output;
    }

    class NoopTimer implements ActionListener {

        public NoopTimer() {
        }

        public void actionPerformed(ActionEvent f) {
            if (!isConnected) {
                initSession();
                if (!isConnected) return;
                try {
                    String[] reply = session.noop();
                    noopdate = new Date();
                    parseExistsRecent(reply);
                    notifyListenersFromBuffer("Checking existing connecting for validity", SessionEvent.DEBUG);
                } catch (IOException e) {
                    notifyIOException(e);
                    selected = null;
                }
            }
        }
    }

    /**
	 * Method to delete a message by using the messageid of the message. This
	 * method first searches for the messageid, gets the index of that message
	 * at server, and then stores the message with the deleted flag. 
	 * 
	 * This method is bulletproof (almost) - in case messageid has some
	 * obscure characters it currently doesnt work. 
	 * Note: this is slow, and is only to be considered as an alternative
	 * to deleteMessage in case that fails. 
	 * 
	 * @param path the mailbox containing the message
	 * @param id
	 * @return boolean
	 */
    public synchronized boolean deleteByID(String path, String id) {
        if (selectMailbox(path) == -1) return false;
        try {
            String command = "HEADER " + RFC822.MESSAGE_ID + " " + id;
            String[] serverreply = session.search(command);
            String[] nr = parseSearch(serverreply);
            if (nr != null & nr.length > 0) {
                serverreply = session.store("+FLAGS (" + "\\" + "Deleted)", Integer.parseInt(nr[0]));
            } else {
                return true;
            }
            session.expunge();
            notifyListenersFromBuffer("Deleting message", SessionEvent.DEBUG);
            return true;
        } catch (IOException f) {
            notifyIOException(f);
            return false;
        }
    }

    /**
	 * TODO: Theres no error checking at all here!? 
	 */
    protected void login() {
        try {
            if (isConnected) session.login(account.getUserName(), account.getPassword());
        } catch (IOException f) {
            notifyIOException(f);
        }
    }

    public void disconnect() {
        if (isConnected) {
            session.logof();
        }
        isConnected = false;
        if (socket != null) {
            if (socket.isConnected()) try {
                socket.close();
            } catch (IOException f) {
                notifyIOException(f);
            }
        }
        if (noopTimer != null) noopTimer.stop();
    }

    /**
	 * 
	 * A class to encapsulate mailbox list results. We wants to
	 * return the mailboxnames and the flags for theese. 
	 * 
	 * @author Stig Tanggaard
	 * 19-09-2003
	 * 
	 **/
    public class MailboxListResult {

        String[] mailboxNames;

        boolean[] hasChildren;

        public MailboxListResult() {
            this(0);
        }

        public MailboxListResult(int length) {
            mailboxNames = new String[length];
            hasChildren = new boolean[length];
        }

        public String[] getMailboxNames() {
            return mailboxNames;
        }

        public boolean hasChildren(int index) {
            return hasChildren[index];
        }
    }

    /**
	 * This starts a timer which calls noop to keep the connection
	 * alive. I think it is up to the mailaccount if this is desired.
	 * For example, if the mailaccount runs in its own thread, it could
	 * easily check the noop result (exists recent) and use this as 
	 * a base for checking for new mail. 
	 * 
	 * If a nooptimer is allready running this will be stopped first. 
	 * Only one can run for each account at a time. 
	 * 
	 */
    public void startNoopTimer(int msec) {
        if (noopTimer != null) noopTimer.stop();
        noopTimer = new Timer(msec, new NoopTimer());
        noopTimer.setRepeats(true);
        noopTimer.start();
    }

    /**
	 * 
	 * Method to check if server has send unrequested messages. 
	 * Use getSelectedMailbox and getExists, getRecent to get
	 * the result of any unrequested messages. 
	 * 
	 * @return
	 */
    public boolean hasServerMessages() {
        if (!isConnected) return false;
        try {
            String[] replies = session.checkServerForMessages();
            if (replies == null) return false;
            parseExistsRecent(replies);
            return true;
        } catch (IOException f) {
            notifyIOException(f);
            return false;
        }
    }

    /**
	 * Method to get an array of sequence numbers from the last
	 * exist/recent values set. Maybe this should be synchronized
	 * or similar. We dont want another method to change the
	 * values while they are being read. 
	 * 
	 * It is allowed for another class to call this. 
	 * 
	 * @return
	 */
    public String[] getSequenceNumbersFromExistsRecent() {
        if (getRecent() == 0) {
            return new String[] {};
        } else {
            int length = getRecent();
            int end = getExists();
            String[] seqenceNumbers = new String[length];
            for (int i = 0; i < length; i++) {
                seqenceNumbers[i] = "" + ((end - length + 1) + i);
            }
            return seqenceNumbers;
        }
    }

    public boolean useSSL() {
        return account.useSSL();
    }
}
