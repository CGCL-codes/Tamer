package net.sf.jqql.packets.in.disk;

import static net.sf.jqql.events.QQEvent.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import net.sf.jqql.QQ;
import net.sf.jqql.annotation.DocumentalPacket;
import net.sf.jqql.annotation.LinkedEvent;
import net.sf.jqql.annotation.PacketName;
import net.sf.jqql.annotation.RelatedPacket;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.DiskInPacket;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets.out.disk.GetShareListPacket;

/**
 * <pre>
 * get share list reply packet
 * 1. head
 * 2. friend count, 4 byte
 * 3. friend's QQ number, 4 byte
 * 4. unknown 4 byte
 * 5. If set sharing to more friends, repeat part 3-4
 *
 * 得到共享列表的回复包
 * 1. 头部
 * 2. 好友数目，4字节
 * 3. 好友QQ号，4字节
 * 4. 未知4字节
 * 5. 如果设置了对更多好友共享，重复3-4部分
 * </pre>
 *
 * @author luma
 */
@DocumentalPacket
@PacketName("get share netwok disk owner list reply packet")
@RelatedPacket({ GetShareListPacket.class })
@LinkedEvent({ QQ_DISK_GET_SHARE_LIST_SUCCESS })
public class GetShareListReplyPacket extends DiskInPacket {

    public List<Integer> friends;

    public GetShareListReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    public String getPacketName() {
        return "Get Share List Reply Packet";
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        if (replyCode == QQ.QQ_REPLY_OK) {
            int count = buf.getInt();
            friends = new ArrayList<Integer>();
            while (count-- > 0) {
                friends.add(buf.getInt());
                buf.getInt();
            }
        }
    }
}
