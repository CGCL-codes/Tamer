package net.sf.amemailchecker.mail.impl.letter;

import net.sf.amemailchecker.mail.MessageFlag;
import net.sf.amemailchecker.mail.MimeConstants;
import net.sf.amemailchecker.mail.model.Attachment;
import net.sf.amemailchecker.mail.model.Letter;
import net.sf.amemailchecker.mail.model.PostalAddressee;
import java.util.*;

public class LetterImpl implements Letter {

    private String uid;

    private PostalAddressee sender;

    private String subject;

    private Date date;

    private Map<String, List<PostalAddressee>> recipients;

    private Map<String, String> texts;

    private List<Attachment> attachments;

    private List<Attachment> inline;

    private Set<MessageFlag> flags;

    private RawMessagePart rawMessage;

    public LetterImpl() {
        recipients = new HashMap<String, List<PostalAddressee>>();
        attachments = new ArrayList<Attachment>();
        texts = new HashMap<String, String>();
        flags = new HashSet<MessageFlag>();
        inline = new ArrayList<Attachment>();
    }

    public LetterImpl(RawMessagePart rawMessage) {
        this();
        this.rawMessage = rawMessage;
    }

    public LetterImpl(LetterImpl letter, boolean useAttachments) {
        this();
        this.uid = letter.getUid();
        this.sender = letter.getSender();
        this.date = letter.getDate();
        this.subject = letter.getSubject();
        this.texts.putAll(letter.getTexts());
        this.inline.addAll(letter.getInline());
        if (useAttachments) this.attachments.addAll(letter.getAttachments());
        for (String key : letter.recipients.keySet()) {
            List<PostalAddressee> recipients = new ArrayList<PostalAddressee>();
            recipients.addAll(letter.recipients.get(key));
            this.recipients.put(key, recipients);
        }
    }

    public LetterImpl(LetterImpl letter, boolean copyAttachments, boolean copyRaw) {
        this(letter, copyAttachments);
        if (copyRaw) this.rawMessage = new RawMessagePart(letter.getRawMessage());
        if (letter.getTexts() != null) this.texts.putAll(letter.getTexts());
        this.flags.addAll(letter.getFlags());
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public PostalAddressee getSender() {
        return sender;
    }

    public void setSender(PostalAddressee sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<PostalAddressee> getRecipients(String key) {
        return recipients.get(key);
    }

    public void setRecipients(String key, List<PostalAddressee> recipients) {
        this.recipients.put(key, recipients);
    }

    public boolean hasRecipients(String key) {
        return recipients.containsKey(key);
    }

    public Map<String, String> getTexts() {
        return texts;
    }

    public String getText(String key) {
        return texts.get(key);
    }

    public void setText(String key, String text) {
        texts.put(key, text);
    }

    public boolean hasTexts() {
        return texts != null && texts.size() > 0;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public boolean hasAttachments() {
        return attachments != null && attachments.size() > 0;
    }

    public Set<MessageFlag> getFlags() {
        return flags;
    }

    public void addFlag(MessageFlag flag) {
        flags.add(flag);
    }

    public RawMessagePart getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(RawMessagePart rawMessage) {
        this.rawMessage = rawMessage;
    }

    public List<Attachment> getInline() {
        return inline;
    }

    public void setInline(List<Attachment> inline) {
        this.inline = inline;
    }

    public boolean isFetched() {
        return flags.contains(MessageFlag.Fetched) || flags.contains(MessageFlag.Fetching);
    }

    public String getMessageID() {
        return (rawMessage != null) ? rawMessage.getHeaderValue(MimeConstants.HEADER_MESSAGE_ID) : null;
    }

    public void setMessageID(String messageID) {
        rawMessage.getHeaders().put(MimeConstants.HEADER_MESSAGE_ID, messageID);
    }

    public boolean isMimeMessage() {
        return rawMessage.getHeaders().containsKey(MimeConstants.HEADER_MIME_VERSION);
    }

    public boolean isMimeMultipart() {
        return isMimeMessage() && rawMessage.isMultipartBound(rawMessage.getHeaderValue(MimeConstants.HEADER_CONTENT_TYPE));
    }

    @Override
    public boolean equals(Object message) {
        return (message instanceof LetterImpl) && ((LetterImpl) message).getMessageID().equals(this.getMessageID()) && ((LetterImpl) message).getUid().equals(uid);
    }
}
