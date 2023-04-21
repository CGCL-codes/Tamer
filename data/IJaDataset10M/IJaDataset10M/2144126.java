package net.sf.jqql.packets.in._05;

import java.nio.ByteBuffer;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._05InPacket;

/**
 * <pre>
 * 请求开始传送的回复包
 * 1. 头部
 * 2. 未知的8字节，和请求包一致
 * 3. session id, 4字节
 * -------- 加密开始 --------
 * 4. 未知1字节，请求发送时是0x04，请求接收时是0x00
 * 5. 未知1字节，请求发送时是0x4C，请求接收时是0x00
 * 6. 未知2字节，全0
 * 7. 未知2字节，0xC350
 * 8. 未知2字节，0xEA60
 * 9. 未知4字节
 * -------- 加密结束 --------
 * 10. 尾部
 * </pre>
 *
 * @author luma
 */
public class RequestBeginReplyPacket extends _05InPacket {

    public int sessionId;

    /**
     * @param buf
     * @param length
     * @param user
     * @throws PacketParseException
     */
    public RequestBeginReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "Request Begin Reply _08Packet";
    }

    @Override
    protected int getEncryptStart() {
        return 12;
    }

    @Override
    protected int getDecryptStart() {
        return 12;
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        buf.getLong();
        sessionId = buf.getInt();
    }
}
