package info.metlos.jdc.nmdc;

import info.metlos.jdc.common.ConnectionMode;
import info.metlos.jdc.common.ConnectionSpeed;
import info.metlos.jdc.common.IMessage;
import info.metlos.jdc.common.IMessageDecoder;
import info.metlos.jdc.common.IRemoteSide;
import info.metlos.jdc.fileshare.list.DirectoryEntry;
import info.metlos.jdc.fileshare.list.FileEntry;
import info.metlos.jdc.nmdc.helper.MessageFormatUtil;
import info.metlos.jdc.nmdc.helper.MessageLocation;
import info.metlos.jdc.nmdc.messages.AbstractNMDCMessage;
import info.metlos.jdc.nmdc.messages.ChatMessage;
import info.metlos.jdc.nmdc.messages.ConnectToMeMessage;
import info.metlos.jdc.nmdc.messages.ForceMoveMessage;
import info.metlos.jdc.nmdc.messages.HelloMessage;
import info.metlos.jdc.nmdc.messages.HubIsFullMessage;
import info.metlos.jdc.nmdc.messages.HubNameMessage;
import info.metlos.jdc.nmdc.messages.HubTopicMessage;
import info.metlos.jdc.nmdc.messages.LockMessage;
import info.metlos.jdc.nmdc.messages.MyInfoMessage;
import info.metlos.jdc.nmdc.messages.NickListMessage;
import info.metlos.jdc.nmdc.messages.OpListMessage;
import info.metlos.jdc.nmdc.messages.QuitMessage;
import info.metlos.jdc.nmdc.messages.RevConnectToMe;
import info.metlos.jdc.nmdc.messages.SearchMessage;
import info.metlos.jdc.nmdc.messages.SearchResultMessage;
import info.metlos.jdc.nmdc.messages.SupportsMessage;
import info.metlos.jdc.nmdc.messages.ValidateDenideMessage;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The decoder of NMDC protocol messages.
 * <p>
 * This class is NOT thread safe and results are undefined if used concurrently.
 * 
 * @author metlos
 * 
 * @version $Id: NMDCMessageDecoder.java 237 2008-09-28 17:03:21Z metlos $
 */
public class NMDCMessageDecoder implements IMessageDecoder {

    protected static final String SPACE = " ";

    protected static final String $ = "$";

    private static final Logger logger = LogManager.getLogger(NMDCMessageDecoder.class);

    protected static final byte separatorByte = (byte) '|';

    protected static final byte $byte = (byte) '$';

    protected static final byte spaceByte = (byte) ' ';

    protected static final byte chatByte = (byte) '<';

    protected static final byte colonByte = (byte) ':';

    protected static final byte greaterByte = (byte) '>';

    protected static final byte commaByte = (byte) ',';

    protected static final byte slashByte = (byte) '/';

    protected static final byte ascii5Byte = (byte) 5;

    protected static final byte rightParenthesisByte = (byte) ')';

    private IRemoteSide remote;

    private byte[] lastData;

    private boolean preserveMessageBytes;

    /**
	 * In order not to create a new MessageLocation instance in every call to
	 * {@link #findMessage(byte[], int)} (there are lots), this is the single
	 * instance it will ever return (each time initialized differently).
	 */
    private final MessageLocation theLocation = new MessageLocation();

    /**
	 * Default constructor. Creates a new instance of decoder set up to use
	 * US-ASCII charset.
	 * 
	 * @see #NMDCMessageDecoder(IRemoteSide, Charset)
	 */
    public NMDCMessageDecoder() {
        this(null, Charset.forName("US-ASCII"));
    }

    /**
	 * Creates a new decoder using a specified charset to decode the messages.
	 * With no remote side set.
	 * 
	 * @param charset
	 * 
	 * @see #NMDCMessageDecoder(IRemoteSide, Charset)
	 */
    public NMDCMessageDecoder(Charset charset) {
        this(null, charset);
    }

    /**
	 * Creates a new decoder initialized with specified remote side and US-ASCII
	 * charset.
	 * 
	 * @param remote
	 * 
	 * @see #NMDCMessageDecoder(IRemoteSide, Charset)
	 */
    public NMDCMessageDecoder(IRemoteSide remote) {
        this(remote, Charset.forName("US-ASCII"));
    }

    /**
	 * Creates a new decoder initialized to use specified remote side and
	 * charset.
	 * 
	 * @param remote
	 *            the remote side to initialize the messages with
	 * @param charset
	 *            the charset to use when decoding textual data
	 */
    public NMDCMessageDecoder(IRemoteSide remote, Charset charset) {
        this.remote = remote;
        initTextDecoder(charset);
        theLocation.setCharBuffer(CharBuffer.allocate(1024));
    }

    public Charset getCharset() {
        return theLocation.getTextDecoder().charset();
    }

    /**
	 * Initializes the text decoder to use given charset;
	 * 
	 * @param charset
	 *            the charset to use
	 */
    private void initTextDecoder(Charset charset) {
        theLocation.setTextDecoder(charset.newDecoder());
        theLocation.getTextDecoder().onMalformedInput(CodingErrorAction.REPLACE);
        theLocation.getTextDecoder().onUnmappableCharacter(CodingErrorAction.REPLACE);
    }

    /**
	 * @return the remote side this decoder decodes messages of
	 */
    public IRemoteSide getRemote() {
        return remote;
    }

    /**
	 * The remote must be set before any messages are decoded.
	 * 
	 * @param remote
	 */
    public void setRemote(IRemoteSide remote) {
        this.remote = remote;
    }

    /**
	 * @return the preserveMessageBytes
	 */
    public boolean isPreserveMessageBytes() {
        return preserveMessageBytes;
    }

    /**
	 * Tells whether to preserve the original binary data of the message while
	 * decoding it (this only works with descendants of
	 * {@link AbstractNMDCMessage}).
	 * 
	 * @param preserveMessageBytes
	 *            the preserveMessageBytes to set
	 */
    public void setPreserveMessageBytes(boolean preserveMessageBytes) {
        this.preserveMessageBytes = preserveMessageBytes;
    }

    /**
	 * This is a helper method for situations where we receive a message with
	 * some charset but later on we find out that we should have decoded it
	 * using a different charset. This can happen in NMDC when we receive a
	 * search result message over UDP.
	 * 
	 * This method only works if the message was decoded while preserving its
	 * original bytes ({@link #setPreserveMessageBytes(boolean)}.
	 * 
	 * @param message
	 *            the message to redecode using given charset
	 * @param messageCharset
	 *            the new charset
	 * @return the message from the text decoded using the messageCharset
	 */
    public <T extends AbstractNMDCMessage> T reDecodeMessage(T message, Charset messageCharset) {
        CharsetDecoder origDecoder = theLocation.getTextDecoder();
        int origStart = theLocation.getArrayOffsetStart();
        int origEnd = theLocation.getArrayOffsetEnd();
        byte[] origBytes = theLocation.getArray();
        byte[] dataBytes = message.getBytes();
        if (dataBytes != null) {
            theLocation.init(0, dataBytes.length, dataBytes);
            initTextDecoder(messageCharset);
            @SuppressWarnings("unchecked") T newMessage = (T) decodeSingleMessage(theLocation);
            theLocation.setTextDecoder(origDecoder);
            theLocation.init(origStart, origEnd, origBytes);
            return newMessage;
        }
        return null;
    }

    /**
	 * The returned list only contains IMessage instances inheriting from
	 * {@link AbstractNMDCMessage}.
	 * 
	 * @see info.metlos.jdc.common.IMessageDecoder#decode(byte[])
	 */
    public List<IMessage> decode(byte[] data) {
        MessageLocation ml;
        List<IMessage> ret = new LinkedList<IMessage>();
        int startIdx = 0;
        while ((ml = findMessage(data, startIdx)) != null) {
            ret.add(decodeSingleMessage(ml));
            if (ml.getArray() != data) {
                startIdx = MessageFormatUtil.indexOf($byte, data, 0);
            } else {
                if (ml.getArrayOffsetEnd() == data.length - 1) {
                    break;
                } else {
                    startIdx = ml.getArrayOffsetEnd() + 1;
                }
            }
        }
        return ret;
    }

    public void reset() {
        lastData = null;
    }

    /**
	 * Finds the index of the next message in the data. Takes the
	 * {@link #lastData} into account as well. If the {@link #lastData} is
	 * non-null, the startIndex parameter is ignored and a message is found as
	 * if the data array was prepended with the lastData and startIndex was 0.
	 * <p>
	 * In case of non-null {@link #lastData}, a new byte[] is constructed and
	 * returned otherwise the returned byte[] will be the data itself.
	 * 
	 * @param data
	 *            the data to search through
	 * @param startIndex
	 *            the index to start the search from
	 * @return the MessageLocation describing where the message is.
	 */
    protected MessageLocation findMessage(byte[] data, int startIndex) {
        byte[] messageBytes = null;
        int start = 0;
        int end = 0;
        if (lastData != null) {
            int separatorIdx = MessageFormatUtil.indexOf(separatorByte, data);
            if (separatorIdx == 0) {
                messageBytes = lastData;
                start = MessageFormatUtil.indexOf($byte, chatByte, lastData, 0, lastData.length);
                end = lastData.length;
            } else if (separatorIdx > 0) {
                messageBytes = new byte[lastData.length + separatorIdx];
                System.arraycopy(lastData, 0, messageBytes, 0, lastData.length);
                System.arraycopy(data, 0, messageBytes, lastData.length, separatorIdx);
                start = MessageFormatUtil.indexOf($byte, chatByte, messageBytes, 0, messageBytes.length);
                end = MessageFormatUtil.indexOf(separatorByte, messageBytes, start);
            } else {
                messageBytes = new byte[lastData.length + data.length];
                System.arraycopy(lastData, 0, messageBytes, 0, lastData.length);
                System.arraycopy(data, 0, messageBytes, lastData.length, data.length);
                lastData = messageBytes;
                return null;
            }
            lastData = null;
        } else {
            messageBytes = data;
            start = MessageFormatUtil.indexOf($byte, chatByte, messageBytes, startIndex, messageBytes.length);
            if (start < 0) {
                lastData = null;
                return null;
            } else {
                end = MessageFormatUtil.indexOf(separatorByte, data, start);
                if (end < 0) {
                    lastData = new byte[data.length - start];
                    System.arraycopy(data, start, lastData, 0, lastData.length);
                    return null;
                }
            }
        }
        theLocation.init(start, end, messageBytes);
        return theLocation;
    }

    protected AbstractNMDCMessage decodeSingleMessage(MessageLocation location) {
        switch(location.byteAt(0)) {
            case chatByte:
                return decodeChatMessage(location);
            case $byte:
                String id = getMessageIdentifier(location);
                if (logger.isDebugEnabled()) {
                    logger.debug("Decoding: " + messageToString(location));
                }
                if (id == null) {
                    logger.warn("Invalid message received: " + messageToString(location));
                    return null;
                }
                if (id.equals("Lock")) {
                    return decodeLockMessage(location);
                } else if (id.equals("HubName")) {
                    return decodeHubNameMessage(location);
                } else if (id.equals("Hello")) {
                    return decodeHelloMessage(location);
                } else if (id.equals("HubTopic")) {
                    return decodeHubTopicMessage(location);
                } else if (id.equals("NickList")) {
                    return decodeNickListMessage(location);
                } else if (id.equals("Supports")) {
                    return decodeSupportsMessage(location);
                } else if (id.equals("MyINFO")) {
                    return decodeMyInfoMessage(location);
                } else if (id.equalsIgnoreCase("ForceMove")) {
                    return decodeForceMoveMessage(location);
                } else if (id.equals("ConnectToMe")) {
                    return decodeConnectToMeMessage(location);
                } else if (id.equals("Quit")) {
                    return decodeQuitMessage(location);
                } else if (id.equals("To:")) {
                    return decodeToMessage(location);
                } else if (id.equals("Search")) {
                    return decodeSearchMessage(location);
                } else if (id.equals("OpList")) {
                    return decodeOpListMessage(location);
                } else if (id.equals("HubIsFull")) {
                    return new HubIsFullMessage((NMDCHub) remote, lastData);
                } else if (id.equals("ValidateDenide")) {
                    return decodeValidateDenideMessage(location);
                } else if (id.equals("RevConnectToMe")) {
                    return decodeRevConnectToMeMessage(location);
                } else if (id.equals("SR")) {
                    return decodeSearchResultMessage(location);
                }
        }
        return null;
    }

    /**
	 * @return the IDENTIFIER part of the $IDENTIFIER at the beginning of the
	 *         message.
	 */
    protected String getMessageIdentifier(MessageLocation location) {
        int spaceIdx = location.indexOf(spaceByte);
        if (spaceIdx > 0) {
            return location.decodeRaw(1, spaceIdx);
        } else {
            logger.error("Strange message received: " + messageToString(location));
            return null;
        }
    }

    /**
	 * Used for debugging purposes
	 * 
	 * @param ml
	 * @return
	 */
    protected String messageToString(MessageLocation ml) {
        return ml.decodeRaw(0, ml.length());
    }

    protected byte[] getMessageBytes(MessageLocation ml) {
        if (preserveMessageBytes) {
            return ml.copy();
        }
        return null;
    }

    protected ChatMessage decodeChatMessage(MessageLocation ml) {
        int nickEndIdx = ml.indexOf(greaterByte);
        ChatMessage ret = new ChatMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setDateCreated(new Date());
        if (nickEndIdx > 0) {
            ret.setAuthorNick(ml.decodeText(1, nickEndIdx));
            if (nickEndIdx < ml.length() - 1) {
                ret.setMessage(ml.decodeText(nickEndIdx + 2));
            } else {
                ret.setMessage("");
            }
        } else {
            ret.setMessage(ml.decodeText(0));
            ret.setAuthorNick("");
        }
        return ret;
    }

    protected LockMessage decodeLockMessage(MessageLocation ml) {
        int lockStartIdx = ml.indexOf(spaceByte);
        int lockEndIdx = ml.indexOf(spaceByte, lockStartIdx + 1);
        if (lockEndIdx == -1) {
            lockEndIdx = ml.length();
        }
        String lock = ml.decodeRaw(lockStartIdx + 1, lockEndIdx);
        String pk = "";
        if (lockEndIdx < ml.getArrayOffsetEnd()) {
            int pkIdx = ml.indexOf("Pk=", lockEndIdx, ml.length());
            if (pkIdx >= -1) {
                pk = ml.decodeRaw(pkIdx + 3);
            }
        }
        LockMessage ret = new LockMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setLock(lock);
        ret.setPk(pk);
        return ret;
    }

    protected HubNameMessage decodeHubNameMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(spaceByte);
        String name = ml.decodeText(startIdx + 1);
        HubNameMessage ret = new HubNameMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setName(name);
        return ret;
    }

    protected HelloMessage decodeHelloMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(spaceByte);
        String nick = ml.decodeText(startIdx + 1);
        HelloMessage ret = new HelloMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setNickname(nick);
        return ret;
    }

    protected HubTopicMessage decodeHubTopicMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(spaceByte);
        String topic = ml.decodeText(startIdx + 1);
        HubTopicMessage ret = new HubTopicMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setTopic(topic);
        return ret;
    }

    protected NickListMessage decodeNickListMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(spaceByte);
        int maxIdx = ml.length();
        int nextIdx = ml.indexOf("$$", 0, maxIdx);
        NickListMessage ret = new NickListMessage((NMDCHub) remote, getMessageBytes(ml));
        while (nextIdx != -1) {
            ret.getNickNames().add(ml.decodeText(startIdx + 1, nextIdx));
            startIdx = nextIdx + 1;
            nextIdx = ml.indexOf("$$", startIdx + 1, maxIdx);
        }
        return ret;
    }

    protected SupportsMessage decodeSupportsMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(spaceByte);
        int nextIdx = ml.indexOf(spaceByte, startIdx + 1);
        SupportsMessage ret = new SupportsMessage((NMDCHub) remote, getMessageBytes(ml));
        while (nextIdx != -1) {
            ret.getTags().add(ml.decodeRaw(startIdx + 1, nextIdx));
            startIdx = nextIdx;
            nextIdx = ml.indexOf(spaceByte, startIdx + 1);
        }
        ret.getTags().add(ml.decodeRaw(startIdx + 1));
        return ret;
    }

    protected MyInfoMessage decodeMyInfoMessage(MessageLocation ml) {
        MyInfoMessage ret = new MyInfoMessage((NMDCHub) remote, getMessageBytes(ml));
        try {
            int startIdx = 13;
            int endIdx = ml.indexOf(spaceByte, startIdx);
            ret.setNickname(ml.decodeText(startIdx, endIdx));
            startIdx = endIdx + 1;
            endIdx = ml.indexOf(chatByte, startIdx);
            if (endIdx == -1) {
                endIdx = ml.indexOf($byte, startIdx);
                ret.setDescription(ml.decodeText(startIdx, endIdx));
                startIdx = endIdx + 1;
            } else {
                int descriptionStart = startIdx;
                ret.setDescription(ml.decodeText(startIdx, endIdx));
                startIdx = endIdx + 1;
                endIdx = ml.indexOf(spaceByte, startIdx);
                try {
                    DescriptionTag tag = new DescriptionTag();
                    ret.setTag(tag);
                    tag.setClientId(ml.decodeRaw(startIdx, endIdx));
                    startIdx = endIdx + 3;
                    endIdx = ml.indexOf(commaByte, startIdx);
                    tag.setVersion(ml.decodeRaw(startIdx, endIdx));
                    startIdx = endIdx + 3;
                    endIdx = startIdx + 1;
                    tag.setMode(ConnectionMode.fromIdentifier(ml.decodeRaw(startIdx, endIdx)));
                    startIdx = endIdx + 3;
                    endIdx = ml.indexOf(slashByte, startIdx);
                    if (endIdx == -1) {
                        endIdx = ml.indexOf(commaByte, startIdx);
                    }
                    tag.setNormalHubs(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
                    if (ml.byteAt(endIdx) == slashByte) {
                        startIdx = endIdx + 1;
                        endIdx = ml.indexOf(slashByte, startIdx);
                        if (endIdx == -1) {
                            endIdx = ml.indexOf(commaByte, startIdx);
                        }
                        tag.setVipHubs(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
                        if (ml.byteAt(endIdx) == '/') {
                            startIdx = endIdx + 1;
                            endIdx = ml.indexOf(commaByte, startIdx);
                            tag.setOperatorHubs(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
                        }
                    }
                    startIdx = endIdx + 3;
                    endIdx = ml.indexOf(greaterByte, startIdx);
                    tag.setOpenSlots(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
                    startIdx = endIdx + 2;
                } catch (Exception e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Misformatted description tag: " + messageToString(ml), e);
                    }
                    ret.setTag(null);
                    endIdx = ml.lastIndexOf($byte, 0, ml.length() - 1);
                    endIdx = ml.lastIndexOf($byte, 0, endIdx - 1);
                    endIdx = ml.lastIndexOf($byte, 0, endIdx - 1);
                    endIdx = ml.lastIndexOf($byte, 0, endIdx - 1);
                    endIdx = ml.lastIndexOf($byte, 0, endIdx - 1);
                    ret.setDescription(ml.decodeText(descriptionStart, endIdx));
                    startIdx = endIdx + 1;
                }
            }
            startIdx += 2;
            endIdx = ml.indexOf($byte, startIdx);
            endIdx--;
            if (startIdx < endIdx) {
                ret.setSpeed(ConnectionSpeed.fromString(ml.decodeRaw(startIdx, endIdx)));
                startIdx = endIdx;
                char statusChar = (char) (ml.byteAt(startIdx) & 0xFF);
                try {
                    ret.setStatus(MyStatus.fromChar(statusChar));
                } catch (IllegalArgumentException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("Invalid status detected. The status char was: " + statusChar + " (" + (int) statusChar + " ASCII)");
                    }
                    ret.setStatus(MyStatus.normal);
                }
                startIdx += 2;
            } else {
                ret.setSpeed(ConnectionSpeed.Unknown);
                ret.setStatus(MyStatus.normal);
                startIdx += 1;
            }
            endIdx = ml.indexOf($byte, startIdx);
            ret.setEmail(ml.decodeRaw(startIdx, endIdx));
            startIdx = endIdx + 1;
            endIdx = ml.indexOf($byte, startIdx);
            if (endIdx > startIdx) {
                ret.setShareSize(Long.parseLong(ml.decodeRaw(startIdx, endIdx)));
            }
        } catch (Exception e) {
            logger.error("Failed to fully decode MyINFO. Message was: " + messageToString(ml), e);
            if (ret.getNickname() == null) {
                return null;
            }
        }
        return ret;
    }

    protected ForceMoveMessage decodeForceMoveMessage(MessageLocation ml) {
        ForceMoveMessage ret = new ForceMoveMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setAddress(ml.decodeRaw(11));
        return ret;
    }

    protected ConnectToMeMessage decodeConnectToMeMessage(MessageLocation ml) {
        ConnectToMeMessage ret = new ConnectToMeMessage((NMDCHub) remote, getMessageBytes(ml));
        int startIdx = 13;
        int endIdx = ml.indexOf(spaceByte, startIdx);
        String firstNick = ml.decodeText(startIdx, endIdx);
        startIdx = endIdx + 1;
        int nextSpace = ml.indexOf(spaceByte, startIdx);
        int colonIdx = ml.indexOf(colonByte, startIdx);
        if (nextSpace == -1 || nextSpace > colonIdx) {
            ret.setRemoteNickname(firstNick);
        } else {
            ret.setMyNickname(firstNick);
            ret.setRemoteNickname(ml.decodeText(startIdx, nextSpace));
            startIdx = nextSpace + 1;
        }
        endIdx = colonIdx;
        String ip = ml.decodeRaw(startIdx, endIdx);
        startIdx = endIdx + 1;
        int port;
        try {
            port = Integer.parseInt(ml.decodeRaw(startIdx));
        } catch (NumberFormatException e) {
            logger.error("Failed to decode remote port. Message was: " + messageToString(ml));
            return null;
        }
        ret.setMyAddress(new InetSocketAddress(ip, port));
        return ret;
    }

    protected QuitMessage decodeQuitMessage(MessageLocation ml) {
        try {
            QuitMessage ret = new QuitMessage((NMDCHub) remote, getMessageBytes(ml));
            ret.setNickname(ml.decodeText(ml.indexOf(spaceByte) + 1));
            return ret;
        } catch (IndexOutOfBoundsException e) {
            logger.error("Failed to decode Quit message: " + messageToString(ml));
            return null;
        }
    }

    protected ChatMessage decodeToMessage(MessageLocation ml) {
        int startIdx = ml.indexOf(chatByte) + 1;
        int endIdx = ml.indexOf(greaterByte, startIdx);
        ChatMessage ret = new ChatMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setDateCreated(new Date());
        ret.setPrivateMessage(true);
        ret.setAuthorNick(ml.decodeText(startIdx, endIdx));
        startIdx = endIdx + 1;
        ret.setMessage(ml.decodeText(startIdx));
        return ret;
    }

    protected SearchMessage decodeSearchMessage(MessageLocation ml) {
        int startIdx = 8;
        int endIdx = ml.indexOf(colonByte, startIdx);
        String ipOrHub = ml.decodeRaw(startIdx, endIdx);
        startIdx = endIdx + 1;
        endIdx = ml.indexOf(spaceByte, startIdx);
        String portOrNick = ml.decodeText(startIdx, endIdx);
        startIdx = endIdx + 1;
        boolean sizeRestricted = ml.byteAt(startIdx) == (byte) 'T';
        startIdx += 2;
        boolean isMaxSize = ml.byteAt(startIdx) == (byte) 'T';
        startIdx += 2;
        endIdx = ml.indexOf((byte) '?', startIdx);
        long size = 0;
        try {
            size = Long.parseLong(ml.decodeRaw(startIdx, endIdx));
        } catch (NumberFormatException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not read 'size' part of search message: " + messageToString(ml));
            }
        }
        startIdx = endIdx + 2;
        NMDCSearchRequest.DataType dataType = NMDCSearchRequest.DataType.any;
        try {
            char type = (char) (ml.byteAt(startIdx) & 0xFF);
            switch(type) {
                case '1':
                    dataType = NMDCSearchRequest.DataType.any;
                    break;
                case '2':
                    dataType = NMDCSearchRequest.DataType.audio;
                    break;
                case '3':
                    dataType = NMDCSearchRequest.DataType.compressed;
                    break;
                case '4':
                    dataType = NMDCSearchRequest.DataType.documents;
                    break;
                case '5':
                    dataType = NMDCSearchRequest.DataType.executables;
                    break;
                case '6':
                    dataType = NMDCSearchRequest.DataType.pictures;
                    break;
                case '7':
                    dataType = NMDCSearchRequest.DataType.video;
                    break;
                case '8':
                    dataType = NMDCSearchRequest.DataType.folders;
            }
        } catch (NumberFormatException e) {
            dataType = NMDCSearchRequest.DataType.any;
            if (logger.isDebugEnabled()) {
                logger.debug("Could not determine 'datatype' in search message: " + messageToString(ml));
            }
        }
        startIdx += 1;
        String pattern = ml.decodeText(startIdx);
        SearchMessage ret = new SearchMessage((NMDCHub) remote, getMessageBytes(ml));
        if (!"Hub".equals(ipOrHub)) {
            try {
                ret.setIp(InetAddress.getByName(ipOrHub));
            } catch (UnknownHostException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not determine ip in the search message:" + messageToString(ml));
                }
            }
            try {
                ret.setPort(Integer.parseInt(portOrNick));
            } catch (NumberFormatException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not set 'port' in a seach message: " + messageToString(ml));
                }
            }
        } else {
            ret.setNickname(portOrNick);
        }
        NMDCSearchRequest req = new NMDCSearchRequest();
        ret.setRequest(req);
        if (sizeRestricted && size > 0) {
            if (isMaxSize) {
                req.setMaxFileSize(size);
            } else {
                req.setMinFileSize(size);
            }
        }
        req.setDataType(dataType);
        req.setPattern(MessageFormatUtil.unescape(pattern.replace($, SPACE)));
        return ret;
    }

    protected OpListMessage decodeOpListMessage(MessageLocation ml) {
        OpListMessage ret = new OpListMessage((NMDCHub) remote, getMessageBytes(ml));
        int startIdx = 7;
        int endIdx = ml.indexOf($byte, startIdx);
        if (endIdx >= 0) {
            startIdx++;
            while (endIdx >= 0) {
                ret.getOps().add(ml.decodeText(startIdx, endIdx));
                startIdx = endIdx + 2;
                endIdx = ml.indexOf($byte, startIdx);
            }
        }
        return ret;
    }

    protected ValidateDenideMessage decodeValidateDenideMessage(MessageLocation ml) {
        ValidateDenideMessage ret = new ValidateDenideMessage((NMDCHub) remote, getMessageBytes(ml));
        ret.setDeniedNick(ml.decodeText(16));
        return ret;
    }

    protected RevConnectToMe decodeRevConnectToMeMessage(MessageLocation ml) {
        RevConnectToMe ret = new RevConnectToMe((NMDCHub) remote, getMessageBytes(ml));
        int startIdx = 16;
        int endIdx = ml.indexOf(spaceByte, startIdx);
        ret.setMyNick(ml.decodeText(startIdx, endIdx));
        ret.setTargetNick(ml.decodeText(endIdx + 1));
        return ret;
    }

    protected SearchResultMessage decodeSearchResultMessage(MessageLocation ml) {
        SearchResultMessage ret = new SearchResultMessage(remote, getMessageBytes(ml));
        int startIdx = 4;
        int endIdx = ml.indexOf(spaceByte, startIdx);
        ret.setSourceNick(ml.decodeText(startIdx, endIdx));
        startIdx = endIdx + 1;
        endIdx = ml.indexOf(spaceByte, startIdx);
        int spaceIdx = ml.indexOf(spaceByte, startIdx);
        int ascii5Idx = ml.indexOf(ascii5Byte, startIdx);
        if (spaceIdx < ascii5Idx) {
            ret.setResult(DirectoryEntry.fromString(ml.decodeText(startIdx, spaceIdx), "\\"));
        } else {
            FileEntry result = FileEntry.fromString(ml.decodeText(startIdx, ascii5Idx), "\\");
            try {
                result.setFileSize(Long.parseLong(ml.decodeRaw(ascii5Idx + 1, spaceIdx)));
            } catch (NumberFormatException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Could not read file size in search result message: " + messageToString(ml));
                }
            }
            ret.setResult(result);
        }
        startIdx = spaceIdx + 1;
        endIdx = ml.indexOf(slashByte, startIdx);
        try {
            ret.setFreeSlots(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
        } catch (NumberFormatException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not read free slots in search result message: " + messageToString(ml));
            }
        }
        startIdx = endIdx + 1;
        endIdx = ml.indexOf(ascii5Byte, startIdx);
        try {
            ret.setTotalSlots(Integer.parseInt(ml.decodeRaw(startIdx, endIdx)));
        } catch (NumberFormatException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not read total slots in search result message: " + messageToString(ml));
            }
        }
        startIdx = endIdx + 1;
        endIdx = ml.indexOf(spaceByte, startIdx);
        ret.setHubName(ml.decodeText(startIdx, endIdx));
        startIdx = endIdx + 2;
        int colonIdx = ml.indexOf(colonByte, startIdx);
        int rightParenthesisIdx = ml.indexOf(rightParenthesisByte, startIdx);
        int hubNameEndIdx = 0;
        int port = 411;
        if (colonIdx >= 0) {
            hubNameEndIdx = colonIdx;
            try {
                port = Integer.parseInt(ml.decodeRaw(colonIdx + 1, rightParenthesisIdx));
            } catch (NumberFormatException e) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Failed to decode hub port in search result message: " + messageToString(ml));
                }
            }
        } else {
            hubNameEndIdx = rightParenthesisIdx;
        }
        try {
            ret.setHubAddress(new InetSocketAddress(InetAddress.getByName(ml.decodeRaw(startIdx, hubNameEndIdx)), port));
        } catch (UnknownHostException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Hub IP address decoding failed in search result message: " + messageToString(ml));
            }
        }
        return ret;
    }
}
