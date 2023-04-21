package net.sf.jqql.packets;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sf.jqql.Crypter;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.debug.DebugSwitch;
import net.sf.jqql.debug.IDebugObject;
import net.sf.jqql.debug.PacketDO;

/**
 * base class of all QQ packet object
 * QQ所有包对象的基类
 * 
 * @author notxx
 * @author luma
 */
public abstract class Packet {

    /**
     * logger.
     */
    protected static Log log = LogFactory.getLog(Packet.class);

    /**
         * crypter.
     * 解密者.
     */
    protected static final Crypter crypter = new Crypter();

    /** 
         * packet buffer, with back array, use to save unencrypted packet body, subcalss should use this buffer 
         * in putBody method. before use, run clear()
         * 
     * 包体缓冲区，有back array，用来存放未加密时的包体，子类应该在putBody方法中
     * 使用这个缓冲区。使用之前先执行clear() 
     */
    protected static final ByteBuffer bodyBuf = ByteBuffer.allocate(QQ.QQ_MAX_PACKET_SIZE);

    /** 调试模式开关 */
    protected static DebugSwitch ds = DebugSwitch.getInstance();

    /**
         * packet command, 0x03~0x)4.
     * 包命令, 0x03~0x04.
     */
    protected char command;

    /**
         * source sign, 0x01~0x02.
     * 源标志, 0x01~0x02.
     */
    protected char source;

    /**
         * packet sequence, 0x05~0x06.
     * 包序号, 0x05~0x06.
     */
    protected char sequence;

    /** 包头字节 */
    protected byte header;

    /**
     * QQUser
         * for creating multiple QQClient in a JVM, packet need to keep a QQuser reference to meake sure relevant 
         * field of packet user how to fill.
     * 为了支持一个JVM中创建多个QQClient，包中需要保持一个QQUser的引用以
     * 确定包的用户相关字段如何填写
     */
    protected QQUser user;

    /**
         * true means this packet is a duplicated packet, duplicated packet doesn't need to deal with, but cause LumaQQ often 
         * happen ack packet miss problem, so here add a field to show that coming message packet is duplicated. now this
         * field only available to massage, just of sorts a method, though not so pretty.
     * true表示这个包是一个重复包，重复包本来是不需要处理的，但是由于LumaQQ较常发生
     * 消息确认包丢失的问题，所以，这里加一个字段来表示到来的消息包是重复的。目前这个
     * 字段只对消息有效，姑且算个解决办法吧，虽然不是太好看
     */
    protected boolean duplicated;

    /** 明文包体 */
    protected byte[] bodyDecrypted;

    /**
         * create a specify parameters packet
     * 构造一个指定参数的包
     * 
     * @param header
         *              packet header
     *         包头
     * @param source
         *              packet source
     *         包源
     * @param command
         *              packet command
     *         包命令 
     * @param sequence
         *              packet sequence number
     *         包序号 
     * @param user    
         *              QQ user object
     *         QQ用户对象
     */
    public Packet(byte header, char source, char command, char sequence, QQUser user) {
        this.user = user;
        this.source = source;
        this.command = command;
        this.sequence = sequence;
        this.duplicated = false;
        this.header = header;
    }

    /**
     * 从buf中构造一个OutPacket，用于调试。这个buf里面可能包含了抓包软件抓来的数据
     * 
     * @param buf
     *             ByteBuffer
     * @throws PacketParseException
     *             解析出错
     */
    protected Packet(ByteBuffer buf, QQUser user) throws PacketParseException {
        this(buf, buf.limit() - buf.position(), user);
    }

    /**
         * create a outPacket from buf, use to debug. this buf maybe contain data which catched by catching packet software 
     * 从buf中构造一个OutPacket，用于调试。这个buf里面可能包含了抓包软件抓来的数据
     * 
     * @param buf
     *             ByteBuffer
     * @param length
         *                      parsing content length
     *             要解析的内容长度
     * @throws PacketParseException
         *                      if parse error
     *             如果解析出错
     */
    protected Packet(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        this.user = user;
        parseHeader(buf);
        if (!validateHeader()) throw new PacketParseException("包头有误，抛弃该包: " + toString());
        byte[] body = getBodyBytes(buf, length);
        bodyDecrypted = decryptBody(body, 0, body.length);
        if (bodyDecrypted == null) throw new PacketParseException("包内容解析出错，抛弃该包: " + toString());
        ByteBuffer tempBuf = ByteBuffer.wrap(bodyDecrypted);
        try {
            parseBody(tempBuf);
        } catch (BufferUnderflowException e) {
            throw new PacketParseException(e.getMessage());
        }
        parseTail(buf);
        if (ds.isDebug()) {
            byte[] debugContent = dump();
            IDebugObject obj = new PacketDO(getPacketName(), debugContent, this instanceof InPacket, getHeadLength(), debugContent.length - getTailLength());
            ds.deliverDebugObject(obj);
        }
    }

    /**
         * export all content of packet to a byte array, mostly use to debug
     * 导出包的全部内容到一个字节数组中，主要用于调试
     * 
     * @return
         *              byte array for packet
     *         包的字节数组
     */
    public byte[] dump() {
        if (bodyDecrypted == null) return new byte[0]; else {
            byte[] debugContent = new byte[getLength(bodyDecrypted.length)];
            ByteBuffer debugBuf = ByteBuffer.wrap(debugContent);
            putHead(debugBuf);
            debugBuf.put(bodyDecrypted);
            putTail(debugBuf);
            debugBuf = null;
            return debugContent;
        }
    }

    /**
         * create a packet object, don't fill any field, only use by subclass
     * 构造一个包对象，什么字段也不填，仅限于子类使用
     */
    protected Packet() {
    }

    /**
     * get total length of UDP form packet, rule out TCP form
     * 得到UDP形式包的总长度，不考虑TCP形式
     * 
     * @param bodyLength
     *          packet body length
     *         包体长度
     * @return
     *          packet length
     *         包长度
     */
    protected abstract int getLength(int bodyLength);

    /**
     * checkout header
     * 校验头部
     *
     * @return
     *          true means header usable
     *         true表示头部有效
     */
    protected abstract boolean validateHeader();

    /**
     * @return
     *          packet header length
     *         包头长度
     */
    protected abstract int getHeadLength();

    /**
     * @return
     *          packet tail length
     *         包尾长度
     */
    protected abstract int getTailLength();

    /**
         * change packet header to byte stream, and write into appointed ByteBuffer object.
     * 将包头部转化为字节流, 写入指定的ByteBuffer对象.
     * 
     * @param buf
         *                   ByteBuffer object whicn packet need to write into
     *                   写入的ByteBuffer对象.
     */
    protected abstract void putHead(ByteBuffer buf);

    /**
         * initialize packet body
     * 初始化包体
     * 
     * @param buf
     *             ByteBuffer
     */
    protected abstract void putBody(ByteBuffer buf);

    /**
     * get byte array of packet body
     * 得到包体的字节数组
     * 
     * @param buf
     *         ByteBuffer
     * @param length
     *          packet total length
     *         包总长度
     * @return
     *          byte array of packet body 
     *         包体字节数组
     */
    protected abstract byte[] getBodyBytes(ByteBuffer buf, int length);

    /**
     * @return
     *          sign this packet belong which protocol family
     *         标识这个包属于哪个协议族
     */
    public abstract int getFamily();

    /**
         * change packet tail to byte stream, and write into appointed ByteBuffer object.
     * 将包尾部转化为字节流, 写入指定的ByteBuffer对象.
     * 
     * @param buf
         *              ByteBuffer object whicn packet need to write into
     *         写入的ByteBuffer对象.
     */
    protected abstract void putTail(ByteBuffer buf);

    /**
         * encrypt packet body
     * 加密包体
     * 
     * @param b 
         *              unencrypt byte array
     *         未加密的字节数组
     * @param offset
         *              offset of packet body begin
     *         包体开始的偏移
     * @param length
         *              packet body length
     *         包体长度
     * @return
         *              encrypted packet body
     *         加密的包体
     */
    protected abstract byte[] encryptBody(byte[] b, int offset, int length);

    /**
         * decrypt packet body
     * 解密包体
     * 
     * @param body
     *                  byte array of packet body 
     *             包体字节数组
     * @param offset
     *                  offset of packet body begin
     *             包体开始偏移
     * @param length
     *                  packet length
     *             包体长度
     * @return 解密的包体字节数组  // byte array of decrypted packet body 
     */
    protected abstract byte[] decryptBody(byte[] body, int offset, int length);

    /**
     * @return
     *          start position of cryptograph, relative to the first byte of packet body.
     *          if this packet is unknown packet, return -1, this method only available to some protocol family. 
     *         密文的起始位置，这个位置是相对于包体的第一个字节来说的，如果这个包是未知包，
     *      返回-1，这个方法只对某些协议族有意义
     */
    protected abstract int getCryptographStart();

    /**
         * parse packet body, from begin position of buf
     * 解析包体，从buf的开头位置解析起
     * 
     * @param buf
     *             ByteBuffer
     * @throws PacketParseException
         *                      if parse error
     *             如果解析出错
     */
    protected abstract void parseBody(ByteBuffer buf) throws PacketParseException;

    /**
         * parse packet header from current position of buf
     * 从buf的当前位置解析包头
     * 
     * @param buf
     *         ByteBuffer
     * @throws PacketParseException
         *              if parse error
     *         如果解析出错
     */
    protected abstract void parseHeader(ByteBuffer buf) throws PacketParseException;

    /**
         * parse packet tail from current position of buf 
     * 从buf的当前未知解析包尾
     * 
     * @param buf
     *         ByteBuffer
     * @throws PacketParseException
         *              if parse error
     *         如果解析出错
     */
    protected abstract void parseTail(ByteBuffer buf) throws PacketParseException;

    public boolean equals(Object obj) {
        if (obj instanceof Packet) {
            Packet packet = (Packet) obj;
            return header == packet.header && command == packet.command && sequence == packet.sequence;
        } else return super.equals(obj);
    }

    /**
         * make up sequence number and command as hash code. for avoid different packet have the same command, header join in
     * 把序列号和命令拼起来作为哈希码. 为了避免不同header的包有相同的命令，Header也参与进来
     */
    public int hashCode() {
        return hash(sequence, command);
    }

    /**
         * get hash value
     * 得到hash值
     * 
     * @param header
     * @param sequence
     * @param command
     * @return
     */
    public static int hash(char sequence, char command) {
        return (sequence << 16) | command;
    }

    /**
     * @return Returns the command.
     */
    public char getCommand() {
        return command;
    }

    /**
     * @return Returns the sequence.
     */
    public char getSequence() {
        return sequence;
    }

    /**
     * @param sequence The sequence to set.
     */
    public void setSequence(char sequence) {
        this.sequence = sequence;
    }

    /**
     * @return
     *          descriptive name of packet
     *         包的描述性名称
     */
    public String getPacketName() {
        return "Unknown Packet";
    }

    /**
     * @return Returns the source.
     */
    public char getSource() {
        return source;
    }

    /**
     * @return Returns the duplicated.
     */
    public boolean isDuplicated() {
        return duplicated;
    }

    /**
     * @param duplicated The duplicated to set.
     */
    public void setDuplicated(boolean duplicated) {
        this.duplicated = duplicated;
    }

    /**
     * @return Returns the header.
     */
    public byte getHeader() {
        return header;
    }

    /**
     * @param header The header to set.
     */
    public void setHeader(byte header) {
        this.header = header;
    }
}
