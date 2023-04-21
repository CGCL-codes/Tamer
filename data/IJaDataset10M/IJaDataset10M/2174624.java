package hambo.pim;

import java.util.Vector;
import java.util.Enumeration;
import java.util.HashSet;
import org.enhydra.xml.xmlc.XMLCUtil;
import hambo.app.core.BusinessObject;
import hambo.app.util.DOMUtil;
import hambo.app.util.Link;
import hambo.config.*;
import hambo.internationalization.CalendarUtil;
import hambo.internationalization.TranslationServiceManager;
import hambo.messaging.*;
import hambo.util.Device;
import hambo.util.XMLUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Locale;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.search.SearchTerm;
import javax.mail.search.FlagTerm;

/**
 * This class handled all access to the emails in the database, now it is a
 * mix of calls to the Hambo Messaging module and markup mangling which should
 * not live here.  One goal is that this class should know only about the Hambo
 * Messaging module, which is the only thing that knows how to access the
 * actual database.  That means <em>no database code</em> in this class.
 * Another (long-term) goal is that this class should cease to exist.
 *
 */
public class MailBroker extends BusinessObject {

    public Device device;

    private String userid;

    /** Used to get the right instance of CalendarUtil . */
    private String timezone;

    /** Used to get the right instance of CalendarUtil . */
    private Locale locale;

    /** Used to get the right instance of CalendarUtil . */
    private String language;

    public static final int NO = 0;

    public static final int UNREAD = 1;

    public static final int TOTAL = 2;

    public static final int UNREAD_TOTAL = 3;

    public static final int NO_ACCOUNTS = -1;

    private String rowColors[] = { "listeven", "listodd" };

    /** The main handle to the Hambo mail storage module. */
    private MailStorage storage;

    public MailBroker() {
        try {
            userid = getContext().getUserId();
            storage = Messaging.getMailStorage(userid);
            timezone = getContext().getSessionAttributeAsString("timezone");
            locale = (Locale) getContext().getSessionAttribute("locale");
            language = getContext().getSessionAttributeAsString("language");
            device = (Device) getContext().getSessionAttribute("device");
        } catch (NoSuchUserException err) {
            userid = null;
            storage = null;
        } catch (NullPointerException err) {
            userid = null;
            storage = null;
        }
    }

    public MailStorage getStorage() {
        return storage;
    }

    /**
     * Gets the "main" text from a message.  If the content is a text/plain,
     * that is reported.  If the content is multipart, the first level
     * text/plain parts is concatenated.
     * @param msg the message
     * @return the message text
     */
    public static String getBodyText(MimeMessage msg) throws MessagingException, IOException {
        Object content = msg.getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof Multipart) {
            Multipart mp = (Multipart) content;
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < mp.getCount(); ++i) {
                BodyPart bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    String disposition = bp.getDisposition();
                    if (disposition == null || disposition.equalsIgnoreCase(Part.INLINE)) {
                        if (buf.length() > 0) buf.append("\n");
                        buf.append((String) bp.getContent());
                    }
                }
            }
            if (buf.length() > 0) return buf.toString(); else return "Unable to summarize [" + mp.getContentType() + "]";
        } else if (content instanceof Part) {
            Part part = (Part) content;
            if (part.isMimeType("text/plain")) return (String) part.getContent();
            String desc = part.getDescription();
            if (desc != null) return desc; else return "Unable to summarize [" + part.getContentType() + "]";
        } else {
            return "Unable to summarize [" + msg.getContentType() + "]";
        }
    }

    /**
     * Makes a <code>Folder</code> list by replicating an <code>Element</code>
     * that serves as a blueprint.
     *
     * @param bp				The <code>Element</code> that are used as a blueprint
     * @param remove			A boolean when true removes the blueprint from the DOM tree	after the list is generated
     * @param showSystemFolders	Tells whether System folder (ie INBOX, DRAFTS etc) should be listed
     *
     * @return					The generated list <code>Element</code>
     * @deprecated Use {@link MailStorage#getFolders} or
     * {@link MailStorage#getFolderStats} and specific markup-mangling code
     * instead.
     */
    public Element showFolders(Element bp, boolean remove, boolean showSystemFolders) {
        return showFolders(bp, remove, showSystemFolders, MailBroker.NO, false, null);
    }

    /**
     * Makes a <code>Folder</code> list by replicating an <code>Element</code>
     * that serves as a blueprint.
     *
     * @param bp				The <code>Element</code> that are used as a blueprint
     * @param remove			A boolean when true removes the blueprint from the DOM tree	after the list is generated
	 * @param showSystemFolders	Tells whether System folder (ie INBOX, DRAFTS etc) should be listed
     * @param onlyunread 		If true only folder with unread messages will be listed
     * @param curFold 			The id of the node that indicates the current folder and should
     * be converted to a text node
     *
     * @return					The generated list <code>Element</code>
     * @deprecated Use {@link MailStorage#getFolders} or
     * {@link MailStorage#getFolderStats} and specific markup-mangling code
     * instead.
     */
    public Element showFolders(Element bp, boolean remove, boolean showSystemFolders, int countType, boolean onlyUnread, String curFold) {
        Vector stats = storage.getFolderStats(showSystemFolders, onlyUnread);
        ListBlueprintArray lbpa = new ListBlueprintArray();
        ListBlueprint lbp = null;
        int count = 0;
        Enumeration stat_enum = stats.elements();
        while (stat_enum.hasMoreElements()) {
            FolderStat stat = (FolderStat) stat_enum.nextElement();
            int nrOfMsgs = stat.getMessages();
            int nrOfUnread = stat.getUnread();
            String name = stat.getName();
            lbp = new ListBlueprint("folderName", stat.getNiceName());
            lbp.addAttribute("value", "" + name);
            lbpa.addListBlueprint(lbp);
            String[] masks = { "folderTableName", "folderTableNameNew", "currentFolderTableName", "currentFolderTableNameNew" };
            boolean isSystemFolder = name.equals("!jdrafts!") || name.equals("!sent!") || name.equals("!trash!");
            int maskIndex;
            if (curFold != null && curFold.equals(name)) {
                if (nrOfUnread > 0 && !isSystemFolder) maskIndex = 3; else maskIndex = 2;
            } else {
                if (nrOfUnread > 0 && !isSystemFolder) maskIndex = 1; else maskIndex = 0;
            }
            int tempCountType = countType;
            if (isSystemFolder || nrOfUnread == 0) {
                if (tempCountType != NO && tempCountType != TOTAL) tempCountType--;
            }
            String newStr = "(@menew@)";
            if (tempCountType == NO) lbp = new ListBlueprint(masks[maskIndex], MessengerUtil.checkSystemFolder(name)); else if (tempCountType == UNREAD) lbp = new ListBlueprint(masks[maskIndex], MessengerUtil.checkSystemFolder(name) + ": " + nrOfUnread + " " + newStr); else if (tempCountType == TOTAL) lbp = new ListBlueprint(masks[maskIndex], MessengerUtil.checkSystemFolder(name) + " (" + nrOfMsgs + ")"); else if (tempCountType == UNREAD_TOTAL) lbp = new ListBlueprint(masks[maskIndex], MessengerUtil.checkSystemFolder(name) + " (" + nrOfUnread + " " + newStr + "/" + nrOfMsgs + ")");
            lbp.addAttribute("href", "fn=" + name);
            lbp.setSearchAttrib(true);
            lbpa.addListBlueprint(lbp);
            for (int i = 0; i < masks.length; i++) {
                if (i != maskIndex) {
                    lbp = new ListBlueprint(masks[i], null);
                    lbpa.addListBlueprint(lbp);
                }
            }
            lbp = new ListBlueprint("unread", "" + nrOfUnread);
            lbpa.addListBlueprint(lbp);
            lbp = new ListBlueprint("total", "" + nrOfMsgs);
            lbpa.addListBlueprint(lbp);
            lbp = new ListBlueprint("rowcolor", "");
            lbp.addAttribute("class", rowColors[count % 2]);
            lbpa.addListBlueprint(lbp);
            lbpa.newRow();
            count++;
        }
        return MessengerDOMUtil.makeList(bp, lbpa, remove);
    }

    /**
     * Helper to format dates.
     */
    public class DateFormater {

        DateFormater() {
            locale = (Locale) getContext().getSessionAttribute("locale");
            calUtil = CalendarUtil.getInstance(timezone, locale, language);
        }

        String format(long dateval) {
            java.util.Date date = new java.util.Date(dateval);
            calUtil.cal.setTime(date);
            return CalendarUtil.getShortDateLabel(calUtil.language, calUtil.cal, locale) + " " + CalendarUtil.getTimeLabel(calUtil.cal, locale);
        }

        private CalendarUtil calUtil;

        private Locale locale;
    }

    /**
     * Gets and shows the headers for the <code>Message</code>s in a specific
     * folder.  The messages can be sorted by differnt fields, the user can
     * specify wether to show all messages or just unread, and no more than a
     * specified number of messages is displayed at one page.  Part of this is
     * presentation, which should not be here.
     * @param wantToAddr should be true in drafts / sent folders, where to
     * addresses should be listed rather than from.
     * @return the number of messages showed.
     * @deprecated use {@link MailStorage#getMessageStats} and page-specific
     * markup mangling code instead.
     */
    public int showMessageHeaders(Document page, String folder, boolean unreadOnly, int sortBy, boolean sortType, int curSideNr) {
        Vector msgHdrVect = storage.getMessageStats(Messaging.folderType(folder), unreadOnly, sortBy, sortType);
        return showMessageHeaders(page, msgHdrVect, curSideNr, null);
    }

    /**
     * @deprecated use {@link MailStorage#getMessageStats} and page-specific
     * markup mangling code instead.
     */
    public int showMessageHeaders(Document page, String folder, boolean unreadOnly, int sortBy, boolean sortType, boolean wantToAddr, int curSideNr) {
        Vector msgHdrVect = storage.getMessageStats(Messaging.folderType(folder), unreadOnly, sortBy, sortType);
        return showMessageHeaders(page, msgHdrVect, curSideNr, null);
    }

    /**
     * Gets and shows the headers from some <code>Message</code>s
     * @deprecated use {@link MailStorage#getMessageStats} and page-specific
     * markup mangling code instead.
     */
    public int showSearchResults(Document page, String[] folders, String searchStr, int curSideNr, String xtraParam) {
        if (folders == null || folders.length == 0) {
            removeElement(page, "messageHeaderList");
            getContext().setSessionAttribute("temp2", "0");
            getContext().setSessionAttribute("allmsgs", new Vector());
            return 0;
        }
        Vector msgHdrVect = storage.getMessageStats(folders, searchStr);
        return showMessageHeaders(page, msgHdrVect, curSideNr, xtraParam);
    }

    /**
     * Common presentation logic for the two showMessageHeaders methods
     */
    private int showMessageHeaders(Document page, Vector msgHdrVect, int curSideNr, String xtraParam) {
        int count = 0;
        int pageCount = 0;
        int msgsPerSide = Integer.parseInt(getContext().getSessionAttributeAsString("msgsperside"));
        int curMsgNr = msgsPerSide * (curSideNr - 1);
        DateFormater dateFormater = new DateFormater();
        ListBlueprintArray lbpa = new ListBlueprintArray();
        Vector allMsgsVect = new Vector();
        HashSet oldMsgId = new HashSet();
        for (int i = 0; i < msgHdrVect.size(); i++) {
            MessageStat msgHdr = (MessageStat) msgHdrVect.elementAt(i);
            boolean showingDrafts = msgHdr.getFolderName().equalsIgnoreCase("!jdrafts!");
            boolean showingDraftsOrSent = showingDrafts || msgHdr.getFolderName().equalsIgnoreCase("!sent!");
            if (!oldMsgId.contains(new Long(msgHdr.getId()))) {
                String msgNr = Long.toHexString(msgHdr.getId());
                allMsgsVect.add(msgNr);
                if (count >= curMsgNr && count < (curMsgNr + msgsPerSide)) {
                    pageCount++;
                    int size = Math.round(msgHdr.getSize() / ((float) 1000));
                    String messageType = (msgHdr.isEvent() ? "Event" : "Mail");
                    String from = MessengerUtil.getSender(device.getGroup(), msgHdr.getSender());
                    String dateStr = dateFormater.format(msgHdr.getDate());
                    if (showingDraftsOrSent) {
                        from = msgHdr.getToAddress();
                        from = MessengerUtil.getNiceAddress(device.getGroup(), from, "(@menoto@)");
                        if (count == 0) removeElement(page, "senderstr");
                    } else if (count == 0) {
                        removeElement(page, "tostr");
                    }
                    String subject = (device.isHtmlDevice()) ? MessengerUtil.getSubject(device.getGroup(), msgHdr.getSubject()) : from;
                    ListBlueprint lbp = new ListBlueprint("date", dateStr);
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("folder", MessengerUtil.checkSystemFolder(msgHdr.getFolderName()));
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("size", "" + size + "k");
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("sender", from);
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("dseq", "");
                    lbp.addAttribute("value", msgNr);
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("type", messageType);
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("subject", subject);
                    if (showingDrafts) {
                        Link draftsHandler = new Link("msgemailmain");
                        draftsHandler.addParam("msgnr", msgNr);
                        lbp.addAttribute("href", draftsHandler.toString());
                    } else {
                        Link msg1URL = new Link("msg1");
                        msg1URL.addParam("msgnr", msgNr);
                        msg1URL.addParam("fn", msgHdr.getFolderName());
                        if (device.isHtmlDevice() && xtraParam != null) msg1URL.addParam(xtraParam);
                        lbp.addAttribute("href", msg1URL.toString());
                    }
                    lbpa.addListBlueprint(lbp);
                    boolean alwaysSeen = showingDraftsOrSent;
                    if (msgHdr.isForwarded() && msgHdr.isAnswered()) {
                        lbp = new ListBlueprint("new");
                        lbp.addAttribute("src", "forwnrep.gif");
                    } else if (msgHdr.isForwarded()) {
                        lbp = new ListBlueprint("new");
                        lbp.addAttribute("src", "forwarded.gif");
                    } else if (msgHdr.isAnswered()) {
                        lbp = new ListBlueprint("new");
                        lbp.addAttribute("src", "replied.gif");
                    } else if (alwaysSeen || msgHdr.isSeen()) {
                        lbp = new ListBlueprint("new", null);
                    } else {
                        lbp = new ListBlueprint("new");
                        lbp.addAttribute("src", "new" + msgHdr.getIcon() + ".gif");
                    }
                    lbpa.addListBlueprint(lbp);
                    lbp = new ListBlueprint("rowcolor");
                    lbp.addAttribute("class", rowColors[count % 2]);
                    lbpa.addListBlueprint(lbp);
                    if (!msgHdr.hasAttachment()) {
                        lbp = new ListBlueprint("attachment", null);
                        lbpa.addListBlueprint(lbp);
                    }
                    if (!msgHdr.isEvent()) {
                        lbp = new ListBlueprint("event", null);
                        lbpa.addListBlueprint(lbp);
                    }
                    lbpa.newRow();
                }
                count++;
                oldMsgId.add(new Long(msgHdr.getId()));
            }
        }
        Element bp = getElement(page, "messageHeaderList");
        if (pageCount == 0) {
            DOMUtil.removeNode(bp);
            removeElement(page, "headerrow");
        } else MessengerDOMUtil.makeList(bp, lbpa, true);
        getContext().setSessionAttribute("temp2", String.valueOf(count));
        getContext().setSessionAttribute("allmsgs", allMsgsVect);
        return pageCount;
    }

    /**
     * Get external email.  If there is a failure, throw an exception so the
     * user might now about it
     */
    public int getAccounts() throws MessagingException {
        int new_messages = 0;
        Vector accountVect = storage.getExternalAccounts();
        if (accountVect.size() == 0) return NO_ACCOUNTS;
        for (int i = 0; i < accountVect.size(); i++) {
            ExternalPopAccount account = (ExternalPopAccount) accountVect.elementAt(i);
            if (account.isOk()) {
                Store store = account.getStore();
                Folder rootFolder = store.getFolder("INBOX");
                rootFolder.open(Folder.READ_WRITE);
                Message[] msgs = rootFolder.getMessages();
                for (int j = 0; j < msgs.length; j++) {
                    if (storage.saveMessage(msgs[j], account)) ++new_messages;
                    if (!account.getKeepMsgs()) msgs[j].setFlags(new Flags(Flags.Flag.DELETED), true);
                }
                try {
                    rootFolder.close(true);
                } catch (MessagingException ex) {
                    logInfo("Failed to close remote folder: " + ex);
                }
                try {
                    store.close();
                } catch (MessagingException ex) {
                    logInfo("Failed to close remote store: " + ex);
                }
                if (!account.getKeepMsgs()) storage.cleanSeen(account);
            }
        }
        return new_messages;
    }

    public void sendWelcomeMail(String userid, String language) {
        this.userid = userid;
        BufferedReader in = null;
        try {
            Session session = Session.getDefaultInstance(null, null);
            MimeMessage msg = new MimeMessage(session);
            language = language.toLowerCase();
            String home = ConfigManager.getConfig("server").getProperty("home");
            String default_language = TranslationServiceManager.DEFAULT_LANGUAGE;
            File file = new File(home + "/conf/welcome." + language);
            if (file.exists()) in = new BufferedReader(new FileReader(file)); else in = new BufferedReader(new FileReader(home + "/conf/welcome." + default_language.toLowerCase()));
            if (in == null) throw new RuntimeException("No welcome mail file found");
            String to = userid + ConfigManager.getConfig("server").getProperty("email_domain");
            String from = in.readLine();
            from = from.substring(from.indexOf(": ") + 2);
            String subject = in.readLine();
            subject = subject.substring(subject.indexOf(": ") + 2);
            String buf = null;
            StringBuffer bodyText = new StringBuffer();
            while ((buf = in.readLine()) != null) {
                bodyText.append(buf + "\n");
            }
            InternetAddress[] recipients = { new InternetAddress(to) };
            msg.setRecipients(javax.mail.Message.RecipientType.TO, recipients);
            msg.setSubject(XMLUtil.decode(subject), "iso-8859-1");
            msg.setFrom(new InternetAddress(from));
            msg.setSentDate(new java.util.Date());
            msg.setText(XMLUtil.decode(bodyText.toString()), "iso-8859-1");
            msg.saveChanges();
            storage.saveMessage(msg, new FolderType(FolderType.INBOX));
        } catch (Throwable ex) {
            ex.printStackTrace();
        } finally {
            if (in != null) try {
                in.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Remove an element from its parent. The element and parent must be part
     * of the page.
     * <p><em>Note:</em> if you have the element and its parent as variables,
     * it is more efficient to just use:
     * <pre><em>parent</em>.removeChild(<em>element</em>);</pre>
     * <p><em>Note:</em> This method is shamelessly copied from
     * {@link hambo.pimbase.PresentationPage}.
     * @param id the id of the element to remove.
     */
    protected final void removeElement(Document page, String id) {
        DOMUtil.removeNode(getElement(page, id));
    }

    /**
     * Remove an element from its parent. The element and parent must be part
     * of the page.
     * <p><em>Note:</em> if you have the element and its parent as variables,
     * it is more efficient to just use:
     * <pre><em>parent</em>.removeChild(<em>element</em>);</pre>
     * <p><em>Note:</em> This method is shamelessly copied from
     * {@link hambo.pimbase.PresentationPage}.
     * @param root the element to remove is a subnode of this element.
     * @param id the id of the element to remove.
     */
    public final void removeElement(Element root, String id) {
        DOMUtil.removeNode(getElement(root, id));
    }

    /**
     * Get a specific Element for a given id, the method will try to
     * constuct the page's own method for fetching the Element, by
     * using reflection.
     * <p><em>Note:</em> This method is shamelessly copied from
     * {@link hambo.pimbase.PresentationPage}.
     * @param id the id attribute of the element to get
     */
    public Element getElement(Document page, String id) {
        String firstCharacter = id.substring(0, 1).toUpperCase();
        String endString = id.substring(1, id.length());
        try {
            return (Element) page.getClass().getMethod("getElement" + firstCharacter + endString, null).invoke(page, null);
        } catch (Throwable th) {
            return null;
        }
    }

    /**
     * Get the Element that matches the id argument. The method will only
     * use a sub tree of the DOM object with the given Element as root Node.
     * <p><em>Note:</em> This method is shamelessly copied from
     * {@link hambo.pimbase.PresentationPage}.
     * @param root the root of the sub tree to search in.
     * @param id the id attribute of the element to get
     */
    public Element getElement(Element root, String id) {
        return root != null ? XMLCUtil.getElementById(id, root) : null;
    }
}
