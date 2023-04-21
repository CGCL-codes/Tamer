package com.volantis.mps.message;

import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import com.volantis.mps.localization.LocalizationFactory;
import com.volantis.mps.attachment.MessageAttachments;
import com.volantis.mps.attachment.AttachmentUtilities;
import com.volantis.mps.attachment.DeviceMessageAttachment;
import com.volantis.mps.channels.HTTPHelper;
import com.volantis.mps.servlet.MessageStoreServlet;
import com.volantis.mps.assembler.MessageRequestor;
import com.volantis.mps.assembler.MessageAssembler;
import com.volantis.mps.assembler.PlainTextMessageAssembler;
import com.volantis.mps.assembler.MimeMessageAssembler;
import com.volantis.charset.EncodingManager;
import com.volantis.mcs.runtime.Volantis;
import java.net.URL;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.io.IOException;
import javax.mail.internet.MimeMultipart;
import our.apache.commons.httpclient.methods.PostMethod;
import our.apache.commons.httpclient.Header;

/**
 * Implementation of {@link com.volantis.mps.message.MultiChannelMessage}.
 */
public class MultiChannelMessageImpl implements MultiChannelMessage {

    /**
     * The LOGGER to use
     */
    private static final LogDispatcher LOGGER = LocalizationFactory.createLogger(MultiChannelMessageImpl.class);

    /**
     * The exception LOCALIZER instance for this class.
     */
    private static final ExceptionLocalizer LOCALIZER = LocalizationFactory.createExceptionLocalizer(MultiChannelMessageImpl.class);

    /**
     * The encoding manager used to determine valid charsets
     */
    private static final EncodingManager ENCODING_MANAGER = new EncodingManager();

    /**
     * The URL for the message.
     */
    protected URL messageURL;

    /**
     * The xml message.
     */
    protected String message;

    /**
     * The subject header for the message.
     */
    protected String subject;

    private Map rawMessageCache = new HashMap();

    private Map mimeMessageCache = new HashMap();

    private Map allHeaders = new HashMap();

    private Map mhtmlHeaders = new HashMap();

    private Map mmsHeaders = new HashMap();

    /**
     * The list of message attachments.
     */
    protected MessageAttachments messageAttachments;

    /**
     * The URL that can be used with the host deployment URL to reference
     * the servlet that handles the message as a URL.
     */
    protected static final String SERVLET_PARTIAL_URL = "mss";

    /**
     * The path separator to use when building URLs.  Just provided as a
     * constant as a convenience for the code in this class to avoid lots of
     * little <code>String</code>s being created everywhere!
     */
    protected final String URL_PATH_CHARACTER = "/";

    /**
     * The helper object used for making requests.
     */
    protected HTTPHelper httpHelper = HTTPHelper.getDefaultInstance();

    /**
     * The character encoding for this message
     */
    protected String characterEncoding = null;

    /**
     * Creates a new instance of <code>MultiChannelMessage</code>.
     */
    public MultiChannelMessageImpl() {
    }

    /**
     * Create a new instance of <code>MultiChannelMessage</code> with the specified
     * character encoding. Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param characterEncoding     The message character encoding
     *
     * @throws IllegalArgumentException If the character encoding is not valid.
     *
     */
    public MultiChannelMessageImpl(String characterEncoding) {
        if (characterEncoding != null && ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding", characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.characterEncoding = characterEncoding;
    }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message URL and subject.
     * <p>
     * @param messageURL    The message URL.
     * @param subject       The message subject.
     */
    public MultiChannelMessageImpl(URL messageURL, String subject) {
        this.messageURL = messageURL;
        this.subject = subject;
    }

    /**
     * Create a new instance of <code>MultiChannelMessage</code> with the
     * given message URL and subject using the specified character encoding.
     * Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param messageURL            The message URL
     * @param subject               The message subject
     * @param characterEncoding     The message character encoding
     */
    public MultiChannelMessageImpl(URL messageURL, String subject, String characterEncoding) {
        if (characterEncoding != null && ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding", characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.messageURL = messageURL;
        this.subject = subject;
        this.characterEncoding = characterEncoding;
    }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message content and subject.
     * <p>
     * @param messageContent the message content.
     * @param subject the message subject.
     */
    public MultiChannelMessageImpl(String messageContent, String subject) {
        this.message = messageContent;
        this.subject = subject;
    }

    /**
     * Creates a new instance of <code>MultiChannelMessage</code> with the
     * given message content and subject using the specified character encoding.
     * Supported character sets can be found at:<br/>
     * <ul>
     * <li><a href="http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.4.2/docs/guide/intl/encoding.doc.html</a></li>
     * <li><a href="http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html">
     *      http://java.sun.com/j2se/1.3/docs/guide/intl/encoding.doc.html</a></li>
     * </ul>
     *
     * @param messageContent        The message content
     * @param subject               The message subject
     * @param characterEncoding     The message character set
     */
    public MultiChannelMessageImpl(String messageContent, String subject, String characterEncoding) {
        if (characterEncoding != null && ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding", characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.message = messageContent;
        this.subject = subject;
        this.characterEncoding = characterEncoding;
    }

    public void setCharacterEncoding(String characterEncoding) {
        if (characterEncoding != null && ENCODING_MANAGER.getEncoding(characterEncoding) == null) {
            String message = LOCALIZER.format("unsupported-encoding", characterEncoding);
            throw new IllegalArgumentException(message);
        }
        this.characterEncoding = characterEncoding;
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setMessageURL(URL messageURL) throws MessageException {
        this.messageURL = messageURL;
        if (message == null) {
            rawMessageCache.clear();
        }
    }

    public URL getMessageURL() throws MessageException {
        return messageURL;
    }

    public void setMessage(String messageContent) throws MessageException {
        this.message = messageContent;
        rawMessageCache.clear();
    }

    public String getMessage() throws MessageException {
        return message;
    }

    public void setSubject(String subject) throws MessageException {
        this.subject = subject;
    }

    public String getSubject() throws MessageException {
        return subject;
    }

    public URL generateTargetMessageAsURL(String deviceName, String mssUrl) throws MessageException {
        if (deviceName == null) {
            throw new MessageException(LOCALIZER.format("device-name-null-invalid"));
        }
        URL returnURL = messageURL;
        if (returnURL == null) {
            String id = null;
            String fullURL;
            StringBuffer hostBuffer = new StringBuffer();
            if (mssUrl == null) {
                Volantis bean = getVolantis();
                String internalURL = bean.getInternalURL().getExternalForm();
                String baseURL = bean.getPageBase();
                hostBuffer.append(internalURL);
                if (!internalURL.endsWith(URL_PATH_CHARACTER)) {
                    hostBuffer.append(URL_PATH_CHARACTER);
                }
                hostBuffer.append(baseURL);
                if ((!baseURL.endsWith(URL_PATH_CHARACTER)) && (!SERVLET_PARTIAL_URL.startsWith(URL_PATH_CHARACTER))) {
                    hostBuffer.append(URL_PATH_CHARACTER);
                }
                hostBuffer.append(SERVLET_PARTIAL_URL);
                fullURL = hostBuffer.toString();
            } else {
                hostBuffer.append(mssUrl);
                fullURL = mssUrl;
            }
            PostMethod method = new PostMethod(fullURL);
            method.setRequestBody(message);
            if (characterEncoding != null) {
                method.addRequestHeader("Content-Type", "text/xml; charset=" + characterEncoding);
                method.addRequestHeader("Accept-Charset", characterEncoding);
            } else {
                method.addRequestHeader("Content-Type", "text/xml");
            }
            try {
                int statusCode = httpHelper.executeRequest(method, fullURL);
                if (statusCode == -1) {
                    final String messageKey = "message-store-connection-failure-for-id-url";
                    LOGGER.error(messageKey);
                    throw new MessageException(LOCALIZER.format(messageKey));
                }
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    final String messageKey = "message-store-connection-failure-http-error-of";
                    final Object messageParam = new Integer(statusCode);
                    LOGGER.error(messageKey, messageParam);
                    throw new MessageException(LOCALIZER.format(messageKey, messageParam));
                }
                Header idHeader = method.getResponseHeader(MessageStoreServlet.MESSAGE_RESPONSE_HEADER_NAME);
                if (idHeader == null) {
                    final String messageKey = "message-id-missing-url-construction-failed";
                    LOGGER.error(messageKey);
                    throw new MessageException(LOCALIZER.format(messageKey));
                }
                id = idHeader.getValue();
                hostBuffer.append("%3fpageid%3d");
                hostBuffer.append(id);
                returnURL = new URL(hostBuffer.toString());
            } catch (MalformedURLException mue) {
                final String messageKey = "message-url-invalid-for";
                final Object messageParam = SERVLET_PARTIAL_URL + "%3fid%3d" + id;
                LOGGER.error(messageKey, messageParam);
                throw new MessageException(LOCALIZER.format(messageKey, messageParam), mue);
            } catch (IOException ioe) {
                final String messageKey = "message-store-transport-error";
                LOGGER.error(messageKey);
                throw new MessageException(LOCALIZER.format(messageKey), ioe);
            } finally {
                method.releaseConnection();
            }
        }
        return returnURL;
    }

    protected Volantis getVolantis() {
        return Volantis.getInstance();
    }

    public String generateTargetMessageAsString(String deviceName) throws MessageException {
        if (deviceName != null) {
            ProtocolIndependentMessage rawMessage = (ProtocolIndependentMessage) rawMessageCache.get(deviceName);
            if (rawMessage == null) {
                MessageRequestor messageRequestor = MessageRequestor.getInstance();
                rawMessage = messageRequestor.getChannelIndependentMessage(deviceName, this);
                rawMessageCache.put(deviceName, rawMessage);
            }
            MessageAssembler messageAssembler = new PlainTextMessageAssembler();
            return (String) messageAssembler.assembleMessage(rawMessage, messageAttachments);
        } else {
            throw new MessageException(LOCALIZER.format("device-name-null-invalid"));
        }
    }

    public MimeMultipart generateTargetMessageAsMimeMultipart(String deviceName) throws MessageException {
        if (deviceName != null) {
            MimeMultipart mimeMultipart = (MimeMultipart) mimeMessageCache.get(deviceName);
            if (mimeMultipart == null) {
                ProtocolIndependentMessage rawMessage = (ProtocolIndependentMessage) rawMessageCache.get(deviceName);
                if (rawMessage == null) {
                    MessageRequestor messageRequestor = MessageRequestor.getInstance();
                    rawMessage = messageRequestor.getChannelIndependentMessage(deviceName, this);
                    rawMessageCache.put(deviceName, rawMessage);
                }
                MessageAssembler messageAssembler = new MimeMessageAssembler();
                MessageAttachments attachments = AttachmentUtilities.getAttachmentsForDevice(deviceName, messageAttachments);
                mimeMultipart = (MimeMultipart) messageAssembler.assembleMessage(rawMessage, attachments);
                mimeMessageCache.put(deviceName, mimeMultipart);
            }
            return mimeMultipart;
        } else {
            throw new MessageException(LOCALIZER.format("device-name-null-invalid"));
        }
    }

    public void addHeader(int messageType, String name, String value) throws MessageException {
        Map headers;
        switch(messageType) {
            case ALL:
                {
                    headers = allHeaders;
                    break;
                }
            case MHTML:
                {
                    headers = mhtmlHeaders;
                    break;
                }
            case MMS:
                {
                    headers = mmsHeaders;
                    break;
                }
            default:
                {
                    throw new MessageException(LOCALIZER.format("message-type-invalid"));
                }
        }
        headers.put(name, value);
    }

    public void addAttachments(MessageAttachments messageAttachments) throws MessageException {
        this.messageAttachments = messageAttachments;
    }

    public void removeAttachments() throws MessageException {
        this.messageAttachments = null;
    }

    public Map getHeaders(int messageType) throws MessageException {
        switch(messageType) {
            case ALL:
                {
                    return allHeaders;
                }
            case MHTML:
                {
                    return mhtmlHeaders;
                }
            case MMS:
                {
                    return mmsHeaders;
                }
            default:
                {
                    throw new MessageException(LOCALIZER.format("message-type-invalid"));
                }
        }
    }

    public MessageAttachments getAttachments() {
        return messageAttachments;
    }

    public Object clone() {
        MultiChannelMessageImpl clone = new MultiChannelMessageImpl();
        try {
            clone.setMessageURL(this.getMessageURL());
            clone.setMessage(this.getMessage());
            clone.setSubject(this.getSubject());
            clone.setCharacterEncoding(this.getCharacterEncoding());
            Iterator i = allHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = (String) allHeaders.get(key);
                clone.addHeader(ALL, key, value);
            }
            i = mmsHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = (String) mmsHeaders.get(key);
                clone.addHeader(MMS, key, value);
            }
            i = mhtmlHeaders.keySet().iterator();
            while (i.hasNext()) {
                String key = (String) i.next();
                String value = (String) mhtmlHeaders.get(key);
                clone.addHeader(MHTML, key, value);
            }
            MessageAttachments clonedAttachments = new MessageAttachments();
            if (messageAttachments != null) {
                i = messageAttachments.iterator();
                while (i.hasNext()) {
                    DeviceMessageAttachment clonedAttachment = (DeviceMessageAttachment) i.next();
                    clonedAttachments.addAttachment(clonedAttachment);
                }
                clone.addAttachments(clonedAttachments);
            }
        } catch (Exception e) {
            LOGGER.error(LOCALIZER.format("message-clone-failed"), e);
            clone = null;
        }
        return clone;
    }
}
