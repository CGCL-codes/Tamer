package net.community.chest.net.proto.text.http.hotmail;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.TreeMap;
import net.community.chest.util.datetime.DateUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Aug 3, 2008 9:27:01 AM
 */
public final class MessagesHandler {

    private MessagesHandler() {
    }

    public static final String HotmailMsgsNamespace = "urn:schemas:mailheader:", EntityTypePropName = "isfolder", ContentLengthPropName = "getcontentlength", SenderPropName = "from", SubjectPropName = "subject", RcvDatePropName = "date", RfcIdPropName = "message-id";

    /**
	 * XML payload used to retrieve message envelope information
	 */
    public static final String envInfoXml = "<?xml version=\"1.0\"?>\r\n" + "<D:propfind xmlns:D=\"DAV:\" xmlns:hm=\"" + HotmailProtocol.HOTMAIL_XML_NAMESPACE + "\" xmlns:m=\"" + HotmailMsgsNamespace + "\">\r\n" + "\t<D:prop>\r\n" + "\t\t<D:" + EntityTypePropName + "/>\r\n" + "\t\t<D:" + ContentLengthPropName + "/>\r\n" + "\t\t<m:" + SenderPropName + "/>\r\n" + "\t\t<m:" + SubjectPropName + "/>\r\n" + "\t\t<m:" + RcvDatePropName + "/>\r\n" + "\t\t<m:" + RfcIdPropName + "/>\r\n" + "\t</D:prop>\r\n" + "</D:propfind>";

    /**
	 * XML payload used to retrieve quick message information
	 */
    public static final String quickInfoXml = "<?xml version=\"1.0\"?>\r\n" + "<D:propfind xmlns:D=\"DAV:\" xmlns:hm=\"" + HotmailProtocol.HOTMAIL_XML_NAMESPACE + "\" xmlns:m=\"" + HotmailMsgsNamespace + "\">\r\n" + "\t<D:prop>\r\n" + "\t\t<D:" + EntityTypePropName + "/>\r\n" + "\t\t<D:" + ContentLengthPropName + "/>\r\n" + "\t\t<m:" + RcvDatePropName + "/>\r\n" + "\t</D:prop>\r\n" + "</D:propfind>";

    /**
	 * Basic/Quick message information
	 * @author lyorg
	 * 14/07/2004
	 */
    public static class MsgInfo {

        private String _href;

        private int _size = Integer.MIN_VALUE;

        private Calendar _rcvDate;

        public MsgInfo() {
            super();
        }

        /**
		 * @return message reference to be used as unique identifier (null/empty if not initialized)
		 */
        public String getHRef() {
            return _href;
        }

        /**
		 * Updates the unique message reference
		 * @param href unique reference - if null/empty, then reference is cleared
		 */
        public void setHRef(String href) {
            _href = href;
        }

        /**
		 * @return message size (bytes) - if <= 0 then not initialized
		 */
        public int getSize() {
            return _size;
        }

        /**
		 * Updates the current message size
		 * @param size new message size - if <=0 then size is cleared
		 */
        public void setSize(int size) {
            _size = size;
        }

        /**
		 * @return date/time at which this message was received (null if not initialized)
		 */
        public Calendar getReceiveDate() {
            return _rcvDate;
        }

        /**
		 * Updates the received date
		 * @param rcvDate new receive date - if null then value is cleared
		 */
        public void setReceiveDate(Calendar rcvDate) {
            _rcvDate = rcvDate;
        }
    }

    /**
	 * Basic message envelope structure
	 */
    public static class MsgEnvelope extends MsgInfo {

        private String _sender, _subject, _rfcId;

        public MsgEnvelope() {
            super();
        }

        /**
		 * @return message sender - usually the display name and NOT the address
		 * (may be null/empty if sender unknown or not initialized)
		 */
        public String getSender() {
            return _sender;
        }

        /**
		 * Update message sender
		 * @param sender new sender value - Note: null/empty may mean sender unknown or not set
		 */
        public void setSender(String sender) {
            _sender = sender;
        }

        /**
		 * @return message subject (may be null/empty if subject unknown or not initialized)
		 */
        public String getSubject() {
            return _subject;
        }

        /**
		 * Update message subject
		 * @param subject new sender value - Note: null/empty may mean subject unknown or not set
		 */
        public void setSubject(String subject) {
            _subject = subject;
        }

        /**
		 * @return message RFC822 ID value (may be null/empty if unknown or not initialized)
		 */
        public String getRfcId() {
            return _rfcId;
        }

        /**
		 * Update message RFC822 ID value
		 * @param rfcId new sender value - Note: null/empty may mean unknown or not set
		 */
        public void setRfcId(String rfcId) {
            _rfcId = rfcId;
        }
    }

    /**
	 * Handler interface for updating specific message object fields from XML tag values
	 */
    public static interface MessagePropHandler {

        boolean updateEnvelope(final Node infoNode, final MsgEnvelope msgEnv);
    }

    /**
	 * Helper base class for message properties handlers
	 */
    public abstract static class AbstractMessagePropHandler implements MessagePropHandler {

        protected AbstractMessagePropHandler() {
            super();
        }

        /**
		 * Called by the default implementation once a non-empty string has been found
		 * @param nodeVal extract string value
		 * @param msgEnv message info object to be updated
		 * @return TRUE if successful
		 */
        protected abstract boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv);

        @Override
        public boolean updateEnvelope(final Node infoNode, final MsgEnvelope msgEnv) {
            final String nodeVal = HotmailProtocol.getNodeValue(infoNode);
            if ((nodeVal != null) && (nodeVal.length() > 0)) return updateEnvelope(nodeVal, msgEnv);
            return false;
        }
    }

    /**
	 * HREF property handler
	 */
    public static final class MessageHRefPropHandler extends AbstractMessagePropHandler {

        public MessageHRefPropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) {
                final int idStart = nodeVal.lastIndexOf('/');
                if (idStart <= 0) return false;
                msgEnv.setHRef(nodeVal.substring(idStart + 1));
            }
            return (msgEnv != null);
        }
    }

    /**
	 * sender property handler
	 */
    public static final class MessageSenderPropHandler extends AbstractMessagePropHandler {

        public MessageSenderPropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) msgEnv.setSender(nodeVal);
            return (msgEnv != null);
        }
    }

    /**
	 * subject property handler
	 */
    public static final class MessageSubjectPropHandler extends AbstractMessagePropHandler {

        public MessageSubjectPropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) msgEnv.setSubject(nodeVal);
            return (msgEnv != null);
        }
    }

    /**
	 * RFC ID property handler
	 */
    public static final class MessageRfcIdPropHandler extends AbstractMessagePropHandler {

        public MessageRfcIdPropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) msgEnv.setRfcId(nodeVal);
            return (msgEnv != null);
        }
    }

    /**
	 * Received date/time property handler
	 */
    public static final class MessageRcvDatePropHandler extends AbstractMessagePropHandler {

        public MessageRcvDatePropHandler() {
            super();
        }

        public static final int decodeDateTimeComponent(final String dtVal, final int startPos, final int numDigits, final char delim, final int minVal, final int maxVal) throws NumberFormatException {
            final int dtvLen = (null == dtVal) ? 0 : dtVal.length(), endPos = startPos + numDigits;
            if ((startPos < 0) || (numDigits <= 0) || (dtvLen < endPos)) throw new NumberFormatException("Bad/Illegal position parameters");
            if (delim != '\0') {
                if ((dtvLen < (endPos + 1)) || (dtVal.charAt(endPos) != delim)) throw new NumberFormatException("Bad/Illegal delimiter parameter");
            }
            final String numVal = dtVal.substring(startPos, endPos);
            final int cVal = Integer.parseInt(numVal);
            if ((cVal < minVal) || (cVal > maxVal)) throw new NumberFormatException("Retrieved value (" + cVal + " is out of range " + minVal + "-" + maxVal);
            return cVal;
        }

        /**
		 * Decodes the received date/time
		 * @param dtVal date/time value - format is: yyyy-mm-ddThh:mm:ss (may NOT be null/empty)
		 * @param rcvDate The {@link Calendar} object to be set according to decoded data
		 * @return true if successfully decoded
		 */
        public static final boolean decodeMessageRcvDate(final String dtVal, final Calendar rcvDate) {
            if ((null == dtVal) || (dtVal.length() <= 0) || (null == rcvDate)) return false;
            try {
                final int year = decodeDateTimeComponent(dtVal, 0, 4, '-', 1900, 9999), month = decodeDateTimeComponent(dtVal, 5, 2, '-', 1, 12), day = decodeDateTimeComponent(dtVal, 8, 2, 'T', 1, 31), hour = decodeDateTimeComponent(dtVal, 11, 2, DateUtil.DEFAULT_TMSEP, 0, 23), minute = decodeDateTimeComponent(dtVal, 14, 2, DateUtil.DEFAULT_TMSEP, 0, 59), second = decodeDateTimeComponent(dtVal, 17, 2, '\0', 0, 59);
                rcvDate.set(year, Calendar.JANUARY + (month - 1), day, hour, minute, second);
                rcvDate.setTimeZone(new SimpleTimeZone(0, "GMT+0000"));
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }

        /**
		 * Decodes the received date/time
		 * @param dtVal date/time value - format is: yyyy-mm-ddThh:mm:ss (may NOT be null/empty)
		 * @return decoded Calendar object
		 * @throws IllegalArgumentException if unable to decode
		 * @throws IllegalStateException if ERA not "AD" (which should be NEVER...)
		 */
        public static final Calendar decodeMessageRcvDate(final String dtVal) throws IllegalArgumentException {
            final Calendar rcvDate = new GregorianCalendar();
            rcvDate.set(Calendar.ERA, GregorianCalendar.AD);
            if (!decodeMessageRcvDate(dtVal, rcvDate)) throw new IllegalArgumentException("Bad/Illegal string value: " + dtVal);
            if (rcvDate.get(Calendar.ERA) != GregorianCalendar.AD) throw new IllegalStateException("ERA value mismatch: value=" + rcvDate.get(Calendar.ERA) + " expected=" + GregorianCalendar.AD);
            return rcvDate;
        }

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) {
                try {
                    msgEnv.setReceiveDate(decodeMessageRcvDate(nodeVal));
                } catch (IllegalArgumentException iae) {
                    return false;
                }
            }
            return (msgEnv != null);
        }
    }

    /**
	 * Base class for numerical properties
	 */
    public abstract static class AbstractNumMessagePropHandler extends AbstractMessagePropHandler {

        protected AbstractNumMessagePropHandler() {
            super();
        }

        /**
		 * Method to override for numerical properties
		 * @param nodeVal node value
		 * @param msgEnv message envelope object to be updated
		 * @return true if successful
		 */
        protected abstract boolean updateEnvelope(final int nodeVal, final MsgEnvelope msgEnv);

        @Override
        protected boolean updateEnvelope(final String nodeVal, final MsgEnvelope msgEnv) {
            if (msgEnv != null) {
                try {
                    return updateEnvelope(Integer.parseInt(nodeVal), msgEnv);
                } catch (NumberFormatException nfe) {
                    return false;
                }
            }
            return true;
        }
    }

    /**
	 * content size handler
	 */
    public static final class MessageContentLengthPropHandler extends AbstractNumMessagePropHandler {

        public MessageContentLengthPropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final int nodeVal, final MsgEnvelope msgEnv) {
            msgEnv.setSize(nodeVal);
            return true;
        }
    }

    /**
	 * entity type handler
	 */
    public static final class MessageEntityTypePropHandler extends AbstractNumMessagePropHandler {

        public MessageEntityTypePropHandler() {
            super();
        }

        @Override
        protected boolean updateEnvelope(final int nodeVal, final MsgEnvelope msgEnv) {
            return (0 == nodeVal);
        }
    }

    /**
	 * Handlers of the various message properties - key=XML tag, value=handler class
	 */
    private static final Map<String, MessagePropHandler> msgPropHandlersMap = new TreeMap<String, MessagePropHandler>(String.CASE_INSENSITIVE_ORDER);

    static {
        final Object[] msgProps = { "href", new MessageHRefPropHandler(), EntityTypePropName, new MessageEntityTypePropHandler(), ContentLengthPropName, new MessageContentLengthPropHandler(), SenderPropName, new MessageSenderPropHandler(), SubjectPropName, new MessageSubjectPropHandler(), RcvDatePropName, new MessageRcvDatePropHandler(), RfcIdPropName, new MessageRfcIdPropHandler() };
        for (int i = 0; i < msgProps.length; i += 2) msgPropHandlersMap.put((String) msgProps[i], (MessagePropHandler) msgProps[i + 1]);
    }

    /**
	 * Traverses the folder nodes looking for specific properties
	 * @param msgNodes message nodes to be traversed
	 * @param msgEnv message envelope object to be updated
	 * @return Updated {@link MsgEnvelope} object - null if error or no
	 * instance provided to begin with
	 */
    public static final MsgEnvelope updateMsgEnvelope(final NodeList msgNodes, final MsgEnvelope msgEnv) {
        if ((null == msgNodes) || (null == msgEnv)) return msgEnv;
        final int numNodes = msgNodes.getLength();
        for (int nIndex = 0; nIndex < numNodes; nIndex++) {
            final Node nd = msgNodes.item(nIndex);
            if (null == nd) continue;
            final String ndName = nd.getLocalName();
            final MessagePropHandler handler = ((null == ndName) || (ndName.length() <= 0)) ? null : msgPropHandlersMap.get(ndName);
            if (handler != null) {
                if (!handler.updateEnvelope(nd, msgEnv)) return null;
            } else if (null == updateMsgEnvelope(nd.getChildNodes(), msgEnv)) return null;
        }
        return msgEnv;
    }

    /**
	 * Extracts message envelope given the root DOM element of the XML response for the message
	 * @param nd DOM root element for the message
	 * @return The extract {@link MsgEnvelope} information - null if unable to extract a valid message object
	 */
    public static final MsgEnvelope extractMsgEnvelope(final Node nd) {
        final MsgEnvelope msgEnv = updateMsgEnvelope(nd.getChildNodes(), new MsgEnvelope());
        if ((null == msgEnv) || (null == msgEnv.getHRef()) || (msgEnv.getHRef().length() <= 0)) return null;
        return msgEnv;
    }

    /**
	 * Extracts the messages information from the given document nodes
	 * @param docNodes document nodes to be checked
	 * @param msgs The {@link Collection} of {@link MsgEnvelope} objects
	 * to be updated
	 * @return TRUE if successful
	 */
    public static final boolean extractMessagesInfo(final NodeList docNodes, final Collection<MsgEnvelope> msgs) {
        if ((null == docNodes) || (null == msgs)) return false;
        final int numNodes = docNodes.getLength();
        for (int nIndex = 0; nIndex < numNodes; nIndex++) {
            final Node nd = docNodes.item(nIndex);
            if (null == nd) continue;
            final String nodeName = nd.getLocalName();
            if ((nodeName != null) && (nodeName.length() > 0) && nodeName.equalsIgnoreCase("response")) {
                final MsgEnvelope msgEnv = extractMsgEnvelope(nd);
                if (msgEnv != null) msgs.add(msgEnv);
                continue;
            }
            if (nd.hasChildNodes()) {
                if (!extractMessagesInfo(nd.getChildNodes(), msgs)) return false;
            }
        }
        return true;
    }

    /**
	 * Extracts messages information from returned response document
	 * @param rspDoc response document
	 * @return extracted {@link Collection} of {@link MsgEnvelope} objects
	 * - may be null/empty if no messages found
	 * @throws IllegalStateException if parsing errors encountered
	 */
    public static final Collection<MsgEnvelope> extractMessagesInfo(final Document rspDoc) throws IllegalStateException {
        final Collection<MsgEnvelope> msgs = new LinkedList<MsgEnvelope>();
        if (!extractMessagesInfo((null == rspDoc) ? null : rspDoc.getChildNodes(), msgs)) throw new IllegalStateException("Cannot extract messages information");
        final int numMsgs = msgs.size();
        if (numMsgs <= 0) return null;
        return msgs;
    }
}
