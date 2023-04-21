package com.cubusmail.mail;

import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.Message.RecipientType;
import javax.mail.internet.ContentType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimePart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.sun.mail.imap.IMAPFolder;
import com.cubusmail.core.BeanFactory;
import com.cubusmail.core.CubusConstants;
import com.cubusmail.gwtui.client.model.GWTAddress;
import com.cubusmail.gwtui.client.model.GWTAttachment;
import com.cubusmail.gwtui.client.model.GWTMessage;
import com.cubusmail.gwtui.domain.Identity;
import com.cubusmail.gwtui.domain.Preferences;
import com.cubusmail.gwtui.domain.UserAccount;
import com.cubusmail.gwtui.server.services.ConvertUtil;
import com.cubusmail.mail.text.MessageTextMode;
import com.cubusmail.mail.text.MessageTextUtil;
import com.cubusmail.mail.util.MessageUtils;
import com.cubusmail.mail.util.MessageUtils.AddressStringType;

/**
 * Wraps the java mail message and includes some mail operations.
 * 
 * @author Juergen Schlierf
 */
public class MessageHandler implements Serializable, ApplicationContextAware {

    private Logger log = Logger.getLogger(getClass().getName());

    private static final String[] PREVIEW_EXTENSIONS = new String[] { "jpg", "gif", "png" };

    private static final String HEADER_NOTIFICATION = "Disposition-Notification-To";

    private static final String HEADER_PRIORITY = "X-Priority";

    private static final long serialVersionUID = -968726069586298205L;

    private Session session;

    private MimeMessage message;

    private String messageTextPlain = "";

    private String messageTextHtml = "";

    private String messageImageHtml = "";

    private boolean htmlMessage;

    private boolean hasPlainText;

    private boolean hasImages;

    private boolean trustImages;

    private boolean readBefore;

    private List<DataSource> composeAttachments;

    private String charset;

    private String subjectEncoding;

    private ApplicationContext applicationContext;

    /**
	 * 
	 */
    private MessageHandler() {
    }

    /**
	 * @param session
	 * @return
	 */
    public static MessageHandler getInstance(Session session) {
        MessageHandler instance = (MessageHandler) BeanFactory.getBean("messageHandler");
        instance.init(session);
        return instance;
    }

    public static MessageHandler getInstance(Session session, MimeMessage message) {
        MessageHandler instance = (MessageHandler) BeanFactory.getBean("messageHandler");
        instance.init(session, message);
        return instance;
    }

    /**
	 * @param session
	 */
    private void init(Session session) {
        init(session, new MimeMessage(session));
    }

    /**
	 * @param session
	 * @param message
	 */
    private void init(Session session, MimeMessage message) {
        this.session = session;
        this.message = message;
        try {
            this.readBefore = this.message.isSet(Flag.SEEN);
            String contentType = message.getContentType();
            ContentType type = new ContentType(contentType);
            String charset = type.getParameter("charset");
            if (charset != null) {
                this.charset = charset;
            } else {
            }
        } catch (MessagingException e) {
            log.warn(e.getMessage());
        }
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getSubject() throws MessagingException {
        String sub = this.message.getHeader("Subject", null);
        if (sub != null) {
            try {
                sub = MimeUtility.unfold(sub);
                sub = MimeUtility.decodeText(sub);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage());
            }
            return sub;
        }
        return sub;
    }

    /**
	 * @param subject
	 * @throws MessagingException
	 */
    public void setSubject(String subject) throws MessagingException {
        try {
            this.message.setSubject(MimeUtility.encodeText(subject, this.charset, subjectEncoding), this.charset);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            this.message.setSubject(subject);
        }
    }

    /**
	 * @param priority
	 * @throws MessagingException
	 */
    public void setPrority(int priority) throws MessagingException {
        this.message.setHeader(HEADER_PRIORITY, Integer.toString(priority));
    }

    /**
	 * @param from
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
    public void setFrom(String from) throws MessagingException {
        if (from != null) {
            this.message.addFrom(MessageUtils.parseInternetAddress(from, this.charset));
        }
    }

    /**
	 * @param to
	 * @throws MessagingException
	 */
    public void setTo(String to) throws MessagingException {
        setAddress(to, RecipientType.TO);
    }

    /**
	 * @param cc
	 * @throws MessagingException
	 */
    public void setCc(String cc) throws MessagingException {
        setAddress(cc, RecipientType.CC);
    }

    /**
	 * @param bcc
	 * @throws MessagingException
	 */
    public void setBcc(String bcc) throws MessagingException {
        setAddress(bcc, RecipientType.BCC);
    }

    /**
	 * @param replyTo
	 * @throws MessagingException
	 */
    public void setReplyTo(String replyTo) throws MessagingException {
        if (replyTo != null) {
            this.message.setReplyTo(MessageUtils.parseInternetAddress(replyTo, this.charset));
        }
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getFrom() throws MessagingException {
        return MessageUtils.getMailAdressString(this.message.getFrom(), AddressStringType.COMPLETE);
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getTo() throws MessagingException {
        return MessageUtils.getMailAdressString(this.message.getRecipients(RecipientType.TO), AddressStringType.COMPLETE);
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getCc() throws MessagingException {
        return MessageUtils.getMailAdressString(this.message.getRecipients(RecipientType.CC), AddressStringType.COMPLETE);
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getBcc() throws MessagingException {
        return MessageUtils.getMailAdressString(this.message.getRecipients(RecipientType.BCC), AddressStringType.COMPLETE);
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public InternetAddress[] getAllRecipients() throws MessagingException {
        Address[] addresses = this.message.getAllRecipients();
        if (addresses != null) {
            InternetAddress[] internetAddresses = new InternetAddress[addresses.length];
            for (int i = 0; i < addresses.length; i++) {
                internetAddresses[i] = (InternetAddress) addresses[i];
            }
            return internetAddresses;
        }
        return null;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public String getReplyTo() throws MessagingException {
        Address[] addressArray = (Address[]) this.message.getReplyTo();
        if (addressArray != null && addressArray.length > 0) {
            List<Address> replyTo = new ArrayList<Address>();
            for (Address address : addressArray) {
                if (!MessageUtils.findEmailAddress(this.message.getFrom(), address)) {
                    replyTo.add(address);
                }
            }
            return MessageUtils.getMailAdressString(replyTo.toArray(new InternetAddress[0]), AddressStringType.COMPLETE);
        }
        return null;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public GWTAddress[] getReplyToArray() throws MessagingException {
        Address[] addressArray = (Address[]) this.message.getReplyTo();
        if (addressArray != null && addressArray.length > 0) {
            List<Address> replyTo = new ArrayList<Address>();
            for (Address address : addressArray) {
                if (!MessageUtils.findEmailAddress(this.message.getFrom(), address)) {
                    replyTo.add(address);
                }
            }
            return ConvertUtil.convertAddress(replyTo.toArray(new Address[0]));
        }
        return null;
    }

    /**
	 * @param address
	 * @param type
	 * @throws MessagingException
	 * @throws UnsupportedEncodingException
	 */
    private void setAddress(String address, RecipientType type) throws MessagingException {
        if (address != null) {
            this.message.setRecipients(type, MessageUtils.parseInternetAddress(address, this.charset));
        }
    }

    /**
	 * @param source
	 */
    public void addComposeAttachment(DataSource source) {
        if (this.composeAttachments == null) {
            this.composeAttachments = new ArrayList<DataSource>();
        }
        this.composeAttachments.add(source);
    }

    /**
	 * 
	 */
    public void removeAllComposeAttachments() {
        if (this.composeAttachments != null) {
            this.composeAttachments.clear();
        }
    }

    /**
	 * @return Returns the attachments.
	 */
    public List<DataSource> getComposeAttachments() {
        return this.composeAttachments;
    }

    /**
	 * @return Returns the htmlMessage.
	 */
    public boolean isHtmlMessage() {
        return this.htmlMessage;
    }

    /**
	 * @param htmlMessage
	 *            The htmlMessage to set.
	 */
    public void setHtmlMessage(boolean htmlMessage) {
        this.htmlMessage = htmlMessage;
    }

    public void setMessageImageHtml(String messageImageHtml) {
        this.messageImageHtml = messageImageHtml;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public long getId() throws MessagingException {
        if (this.message.getFolder() != null) {
            IMAPFolder imapFolder = (IMAPFolder) this.message.getFolder();
            return imapFolder.getUID(this.message);
        }
        return 0;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public Date getDate() throws MessagingException {
        return this.message.getSentDate();
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public int getSize() throws MessagingException {
        return this.message.getSize();
    }

    /**
	 * @return Returns the message.
	 */
    public Message getMessage() {
        return this.message;
    }

    /**
	 * @return Returns the hasImages.
	 */
    public boolean isHasImages() {
        return this.hasImages;
    }

    /**
	 * @param hasImages
	 *            The hasImages to set.
	 */
    public void setHasImages(boolean hasImages) {
        this.hasImages = hasImages;
    }

    /**
	 * @return Returns the hasPlainText.
	 */
    public boolean isHasPlainText() {
        return this.hasPlainText;
    }

    /**
	 * @param hasPlainText
	 *            The hasPlainText to set.
	 */
    public void setHasPlainText(boolean hasPlainText) {
        this.hasPlainText = hasPlainText;
    }

    /**
	 * @return Returns the messageTextPlain.
	 */
    public String getMessageTextPlain() {
        return this.messageTextPlain;
    }

    /**
	 * @param messageTextPlain
	 *            The messageTextPlain to set.
	 */
    public void setMessageTextPlain(String messageTextPlain) {
        this.messageTextPlain = messageTextPlain;
    }

    /**
	 * @return Returns the messageTextHtml.
	 */
    public String getMessageTextHtml() {
        return this.messageTextHtml;
    }

    /**
	 * @param messageTextHtml
	 *            The messageTextHtml to set.
	 */
    public void setMessageTextHtml(String messageTextHtml) {
        this.messageTextHtml = messageTextHtml;
    }

    /**
	 * @return Returns the draftMessage.
	 * @throws MessagingException
	 */
    public boolean isDraftMessage() throws MessagingException {
        return this.message.isSet(Flags.Flag.DRAFT);
    }

    /**
	 * @param draftMessage
	 *            The draftMessage to set.
	 * @throws MessagingException
	 */
    public void setDraftMessage(boolean draftMessage) throws MessagingException {
        this.message.setFlag(Flags.Flag.DRAFT, draftMessage);
    }

    public boolean isTrustImages() {
        return this.trustImages;
    }

    public void setTrustImages(boolean trustImages) {
        this.trustImages = trustImages;
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    public boolean isAcknowledgement() throws MessagingException {
        String[] notification = this.message.getHeader(HEADER_NOTIFICATION);
        if (notification != null && notification.length > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
	 * @param ack
	 * @throws MessagingException
	 */
    public void setAcknowledgement(boolean ack) throws MessagingException {
        if (ack) {
            try {
                this.message.setHeader(HEADER_NOTIFICATION, MimeUtility.encodeText(getFrom(), this.charset, this.subjectEncoding));
            } catch (UnsupportedEncodingException e) {
                this.message.setHeader(HEADER_NOTIFICATION, getFrom());
            }
        } else {
            this.message.removeHeader(HEADER_NOTIFICATION);
        }
    }

    /**
	 * @param gwtMsg
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void setGWTMessage(GWTMessage gwtMsg) throws MessagingException, IOException {
        UserAccount account = SessionManager.get().getUserAccount();
        Identity identity = account.getIdentityById(gwtMsg.getIdentityId());
        setFrom(identity.getInternetAddress());
        if (!StringUtils.isEmpty(identity.getReplyTo())) {
            setReplyTo(identity.getReplyTo());
        }
        setTo(gwtMsg.getTo());
        setCc(gwtMsg.getCc());
        setBcc(gwtMsg.getBcc());
        setSubject(gwtMsg.getSubject());
        if (gwtMsg.isHtmlMessage()) {
            setMessageTextHtml(gwtMsg.getMessageText());
        } else {
            setMessageTextPlain(gwtMsg.getMessageText());
        }
        setHtmlMessage(gwtMsg.isHtmlMessage());
        setHasImages(gwtMsg.isHasImages());
        setAcknowledgement(gwtMsg.isAcknowledgement());
        setPrority(gwtMsg.getPriority());
        setDraftMessage(gwtMsg.isDraft());
    }

    /**
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
    public GWTMessage getGWTMessage() throws MessagingException, IOException {
        GWTMessage gwtMsg = new GWTMessage();
        gwtMsg.setFrom(getFrom());
        gwtMsg.setFromArray(ConvertUtil.convertAddress(this.message.getFrom()));
        gwtMsg.setTo(getTo());
        gwtMsg.setToArray(ConvertUtil.convertAddress(this.message.getRecipients(RecipientType.TO)));
        gwtMsg.setCc(getCc());
        gwtMsg.setCcArray(ConvertUtil.convertAddress(this.message.getRecipients(RecipientType.CC)));
        gwtMsg.setBcc(getBcc());
        gwtMsg.setReplyTo(getReplyTo());
        gwtMsg.setReplyToArray(getReplyToArray());
        gwtMsg.setSubject(getSubject());
        gwtMsg.setDate(this.message.getSentDate());
        if (isHtmlMessage()) {
            gwtMsg.setMessageText(getMessageTextHtml());
        } else {
            gwtMsg.setMessageText(getMessageTextPlain());
        }
        gwtMsg.setHtmlMessage(isHtmlMessage());
        gwtMsg.setHasImages(isHasImages());
        gwtMsg.setTrustImages(isTrustImages());
        gwtMsg.setAcknowledgement(isAcknowledgement());
        gwtMsg.setReadBefore(this.readBefore);
        gwtMsg.setRead(isRead());
        gwtMsg.setDraft(isDraftMessage());
        long id = getId();
        gwtMsg.setId(id);
        List<MimePart> parts = MessageUtils.attachmentsFromPart(this.message);
        if (parts.size() > 0) {
            GWTAttachment[] attachments = new GWTAttachment[parts.size()];
            for (int i = 0; i < parts.size(); i++) {
                attachments[i] = new GWTAttachment();
                String fileName = parts.get(i).getFileName();
                if (StringUtils.isEmpty(fileName)) {
                    fileName = this.applicationContext.getMessage("message.unknown.attachment", null, SessionManager.get().getLocale());
                }
                attachments[i].setFileName(fileName);
                int size = parts.get(i).getSize();
                if (parts.get(i).getSize() == -1) {
                    try {
                        size = parts.get(i).getInputStream().available();
                    } catch (IOException e) {
                        size = -1;
                    }
                }
                NumberFormat sizeFormat = MessageUtils.createSizeFormat(SessionManager.get().getLocale());
                size = MessageUtils.calculateAttachmentSize(size);
                attachments[i].setSize(size);
                attachments[i].setSizeText(MessageUtils.formatPartSize(attachments[i].getSize(), sizeFormat));
                attachments[i].setMessageId(id);
                attachments[i].setIndex(i);
                String extension = StringUtils.substringAfterLast(parts.get(i).getFileName(), ".");
                if (extension != null) {
                    extension = extension.toLowerCase();
                    if (ArrayUtils.contains(PREVIEW_EXTENSIONS, extension)) {
                        attachments[i].setPreview(true);
                    }
                }
            }
            gwtMsg.setAttachments(attachments);
        }
        return gwtMsg;
    }

    /**
	 * @return Returns the attachments.
	 */
    public GWTAttachment[] getGWTComposeAttachments() {
        if (this.composeAttachments != null) {
            int index = 0;
            GWTAttachment[] gwtAttachments = new GWTAttachment[this.composeAttachments.size()];
            for (DataSource attachment : this.composeAttachments) {
                gwtAttachments[index] = new GWTAttachment();
                gwtAttachments[index].setFileName(attachment.getName());
                int size = -1;
                try {
                    size = attachment.getInputStream().available();
                } catch (IOException e) {
                }
                gwtAttachments[index].setSize(size);
                gwtAttachments[index].setSizeText(MessageUtils.formatPartSize(size, MessageUtils.createSizeFormat(SessionManager.get().getLocale())));
                gwtAttachments[index].setMessageId(0);
                gwtAttachments[index].setIndex(index);
                String extension = StringUtils.substringAfterLast(attachment.getName(), ".");
                if (extension != null) {
                    extension = extension.toLowerCase();
                    if (ArrayUtils.contains(PREVIEW_EXTENSIONS, extension)) {
                        gwtAttachments[index].setPreview(true);
                    }
                }
                index++;
            }
            return gwtAttachments;
        }
        return null;
    }

    /**
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void send() throws MessagingException, IOException {
        buildBodyContent();
        this.message.setHeader("X-Mailer", CubusConstants.APPLICATION_NAME);
        this.message.setSentDate(new Date());
        Transport transport = this.session.getTransport();
        if (!transport.isConnected()) {
            transport.connect();
        }
        transport.sendMessage(this.message, this.message.getAllRecipients());
        transport.close();
    }

    /**
	 * @param folder
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void saveToFolder(IMailFolder folder, boolean draft) throws MessagingException, IOException {
        if (draft) {
            buildBodyContent();
            this.message.setSentDate(new Date());
        }
        Message[] msgs = new Message[1];
        msgs[0] = this.message;
        folder.appendMessages(msgs);
    }

    /**
	 * @param msg
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void createReplyMessage(Message msg, boolean replyAll) throws MessagingException, IOException {
        init();
        this.message = (MimeMessage) msg.reply(replyAll);
        Preferences prefs = SessionManager.get().getPreferences();
        MessageTextUtil.messageTextFromPart(msg, this, true, MessageTextMode.REPLY, prefs, 0);
        addReplyInfo(msg);
    }

    /**
	 * @param msg
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void createForwardMessage(Message msg) throws MessagingException, IOException {
        init();
        setSubject("Fwd: " + msg.getSubject());
        setHtmlMessage(MessageUtils.isHtmlMessage(msg));
        List<MimePart> attachments = MessageUtils.attachmentsFromPart(msg);
        if (attachments != null) {
            for (MimePart part : attachments) {
                DataSource source = part.getDataHandler().getDataSource();
                ByteArrayDataSource newSource = new ByteArrayDataSource(source.getInputStream(), source.getContentType());
                if (StringUtils.isEmpty(source.getName())) {
                    newSource.setName(this.applicationContext.getMessage("message.unknown.attachment", null, SessionManager.get().getLocale()));
                } else {
                    newSource.setName(source.getName());
                }
                addComposeAttachment(newSource);
            }
        }
        Preferences prefs = SessionManager.get().getPreferences();
        MessageTextUtil.messageTextFromPart(msg, this, true, MessageTextMode.REPLY, prefs, 0);
    }

    /**
	 * @throws MessagingException
	 * @throws IOException
	 */
    private void buildBodyContent() throws MessagingException, IOException {
        boolean hasAttachments = (this.composeAttachments != null && this.composeAttachments.size() > 0);
        boolean multipart = hasAttachments || isHtmlMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(this.message, multipart);
        if (isHtmlMessage()) {
            String plainText = MessageTextUtil.convertHtml2PlainText(this.messageTextHtml);
            messageHelper.setText(plainText, this.messageTextHtml);
        } else {
            messageHelper.setText(this.messageTextPlain, false);
        }
        if (hasAttachments) {
            for (DataSource attachment : this.composeAttachments) {
                messageHelper.addAttachment(attachment.getName(), attachment);
            }
        }
        if (!isRead()) {
            this.message.setFlag(Flags.Flag.SEEN, true);
        }
    }

    /**
	 * @param loadImages
	 * @throws MessagingException
	 * @throws IOException
	 */
    public void readBodyContent(boolean loadImages, MessageTextMode mode) throws MessagingException, IOException {
        init();
        Preferences prefs = SessionManager.get().getPreferences();
        MessageTextUtil.messageTextFromPart(this.message, this, loadImages, mode, prefs, 0);
        if (StringUtils.isEmpty(this.messageTextPlain) && StringUtils.isEmpty(this.messageTextHtml)) {
            if (!StringUtils.isEmpty(this.messageImageHtml) && prefs.isShowHtml()) {
                this.messageTextHtml = this.messageImageHtml;
                setHtmlMessage(true);
                setTrustImages(true);
                setHasImages(true);
            }
        }
        if (!isRead()) {
            this.message.setFlag(Flags.Flag.SEEN, true);
        }
    }

    /**
	 * @return
	 * @throws MessagingException
	 */
    private boolean isRead() throws MessagingException {
        return this.message.isSet(Flags.Flag.SEEN);
    }

    /**
	 * @throws NoSuchMessageException
	 * @throws MessagingException
	 */
    private void addReplyInfo(Message originalMessage) throws NoSuchMessageException, MessagingException {
        String fromString = MessageUtils.getMailAdressString(originalMessage.getFrom(), AddressStringType.COMPLETE);
        Locale locale = SessionManager.get().getLocale();
        String[] args = new String[] { fromString };
        String informationText = this.applicationContext.getMessage("message.reply.information.text.withoutDate", args, locale);
        if (!StringUtils.isEmpty(this.messageTextPlain)) {
            this.messageTextPlain = informationText + "\n\n" + this.messageTextPlain;
        }
        if (!StringUtils.isEmpty(this.messageTextHtml)) {
            this.messageTextHtml = "<p>" + informationText + "</p>" + this.messageTextHtml;
        }
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public void setSubjectEncoding(String subjectEncoding) {
        this.subjectEncoding = subjectEncoding;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private void init() {
        this.messageImageHtml = "";
        this.messageTextHtml = "";
        this.messageTextPlain = "";
        this.htmlMessage = false;
        this.hasPlainText = false;
        this.hasImages = false;
        this.trustImages = false;
        this.readBefore = false;
    }
}
