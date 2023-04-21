package net.sf.jqql.packets.out;

import java.nio.ByteBuffer;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicOutPacket;

/**
 * <pre>
 * Logout请求包，这个包不需要服务器的应答，格式为
 * 1. 头部
 * 2. password key
 * 3. 尾部
 * </pre>
 *
 * @author luma
 */
public class LogoutPacket extends _08BasicOutPacket {

    /**
     * 构造函数
     */
    public LogoutPacket(QQUser user) {
        super(QQ.QQ_CMD_LOGOUT, false, user);
        sendCount = 4;
    }

    /**
     * @param buf
     * @param length
     * @throws PacketParseException
     */
    public LogoutPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "Logout _08Packet";
    }

    @Override
    protected void putBody(ByteBuffer buf) {
        buf.put(user.getPasswordKey());
    }
}
