package net.sf.jqql.packets.in;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.sf.jqql.beans.QQFriend;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicInPacket;

/**
 * <pre>
 * 请求好友列表的应答包，格式为
 * 1. 头部
 * 2. 下一次好友列表开始位置，这个位置是你所有好友排序后的位置，如果为0xFFFF，那就是你的好友已经全部得到了
 *    每次都固定的返回50个好友，所以如果不足50个了，那么这个值一定是0xFFFF了
 * 3. 好友QQ号，4字节
 * 4. 头像，2字节
 * 5. 年龄，1字节
 * 6. 性别，1字节
 * 7. 昵称长度，1字节
 * 8. 昵称，不定字节，由8指定
 * 9. 用户标志字节，4字节
 * 10. 重复3-9的结构
 * 11.尾部
 * </pre>
 *
 * @author luma
 * @see net.sf.jqql.beans.QQFriend
 */
public class GetFriendListReplyPacket extends _08BasicInPacket {

    public char position;

    public List<QQFriend> friends;

    /**
     * 构造函数
     *
     * @param buf    缓冲区
     * @param length 包长度
     * @throws PacketParseException 解析错误
     */
    public GetFriendListReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "Get Friend List Reply _08Packet";
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        position = buf.getChar();
        friends = new ArrayList<QQFriend>();
        while (buf.hasRemaining()) {
            QQFriend friend = new QQFriend();
            friend.readBean(buf);
            friends.add(friend);
        }
    }
}
