package net.sf.jqql.packets.in;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.beans.UserProperty;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicInPacket;

/**
 * <pre>
 * 用户属性回复包
 * 1. 头部
 * 2. 子命令，1字节
 * 当2部分为0x01时：
 * 3. 下一个包的起始位置，2字节
 * 4. 6部分的长度，1字节
 * 5. QQ号，4字节
 * 6. 用户属性字节，已知位如下
 * 	  bit30 -> 是否有个性签名
 * 7. 如果有更多好友，重复5-6部分
 * Note: 当2部分为其他值时，尚未仔细解析过后面的格式，非0x01值一般出现在TM中
 * 8. 尾部
 * </pre>
 *
 * @author luma
 */
public class UserPropertyOpReplyPacket extends _08BasicInPacket {

    public byte subCommand;

    public boolean finished;

    public char startPosition;

    public List<UserProperty> properties;

    public UserPropertyOpReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "User Property Op Reply _08Packet";
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        subCommand = buf.get();
        switch(subCommand) {
            case QQ.QQ_SUB_CMD_GET_USER_PROPERTY:
                startPosition = buf.getChar();
                finished = startPosition == QQ.QQ_POSITION_USER_PROPERTY_END;
                int pLen = buf.get() & 0xFF;
                properties = new ArrayList<UserProperty>();
                while (buf.hasRemaining()) {
                    UserProperty p = new UserProperty(pLen);
                    p.readBean(buf);
                    properties.add(p);
                }
                break;
        }
    }
}
