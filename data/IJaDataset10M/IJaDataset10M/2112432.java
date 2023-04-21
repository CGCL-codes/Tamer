package net.sf.jqql.packets;

import java.nio.ByteBuffer;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.in.disk.ApplyReplyPacket;
import net.sf.jqql.packets.in.disk.AuthenticateReplyPacket;
import net.sf.jqql.packets.in.disk.BeginSessionPacket;
import net.sf.jqql.packets.in.disk.CreateReplyPacket;
import net.sf.jqql.packets.in.disk.DeleteReplyPacket;
import net.sf.jqql.packets.in.disk.DiskInPacketFragment;
import net.sf.jqql.packets.in.disk.DownloadReplyPacket;
import net.sf.jqql.packets.in.disk.FinalizeReplyPacket;
import net.sf.jqql.packets.in.disk.GetServerListReplyPacket;
import net.sf.jqql.packets.in.disk.GetShareListReplyPacket;
import net.sf.jqql.packets.in.disk.GetSharedDiskReplyPacket;
import net.sf.jqql.packets.in.disk.GetSizeReplyPacket;
import net.sf.jqql.packets.in.disk.ListMyDiskDirReplyPacket;
import net.sf.jqql.packets.in.disk.ListSharedDiskDirReplyPacket;
import net.sf.jqql.packets.in.disk.MoveReplyPacket;
import net.sf.jqql.packets.in.disk.PasswordOpReplyPacket;
import net.sf.jqql.packets.in.disk.PrepareReplyPacket;
import net.sf.jqql.packets.in.disk.RenameReplyPacket;
import net.sf.jqql.packets.in.disk.SetShareListReplyPacket;
import net.sf.jqql.packets.in.disk.UnknownDiskInPacket;
import net.sf.jqql.packets.in.disk.UploadFileReplyPacket;
import net.sf.jqql.packets.out.UnknownOutPacket;
import net.sf.jqql.packets.out.disk.ApplyPacket;
import net.sf.jqql.packets.out.disk.AuthenticatePacket;
import net.sf.jqql.packets.out.disk.CreatePacket;
import net.sf.jqql.packets.out.disk.DeletePacket;
import net.sf.jqql.packets.out.disk.FinalizePacket;
import net.sf.jqql.packets.out.disk.GetServerListPacket;
import net.sf.jqql.packets.out.disk.GetShareListPacket;
import net.sf.jqql.packets.out.disk.GetSharedDiskPacket;
import net.sf.jqql.packets.out.disk.GetSizePacket;
import net.sf.jqql.packets.out.disk.ListMyDiskDirPacket;
import net.sf.jqql.packets.out.disk.ListSharedDiskDirPacket;
import net.sf.jqql.packets.out.disk.MovePacket;
import net.sf.jqql.packets.out.disk.PasswordOpPacket;
import net.sf.jqql.packets.out.disk.PreparePacket;
import net.sf.jqql.packets.out.disk.RenamePacket;
import net.sf.jqql.packets.out.disk.SetShareListPacket;
import net.sf.jqql.packets.out.disk.UnknownDiskOutPacket;
import net.sf.jqql.packets.out.disk.UploadFilePacket;

/**
 * disk protocol family packet parser
 * disk协议族包解析器
 *
 * @author luma
 */
public class DiskFamilyParser implements IParser {

    private int offset, length;

    private char command, source;

    private int remaining;

    private PacketHistory history;

    public DiskFamilyParser() {
        history = new PacketHistory();
        remaining = 0;
    }

    public boolean accept(ByteBuffer buf) {
        if (remaining > 0) return buf.hasRemaining();
        offset = buf.position();
        int bufferLength = buf.limit() - offset;
        if (bufferLength < QQ.QQ_LENGTH_DISK_FAMILY_IN_HEADER) return false;
        length = buf.getInt(offset) + 4;
        if (length > QQ.QQ_MAX_PACKET_SIZE) {
            remaining = length;
            source = buf.getChar(offset + 4);
            command = buf.getChar(offset + 8);
            return true;
        }
        if (bufferLength < length) return false;
        return true;
    }

    public int getLength(ByteBuffer buf) {
        if (length > QQ.QQ_MAX_PACKET_SIZE) {
            return Math.min(remaining, buf.remaining());
        } else return length;
    }

    public InPacket parseIncoming(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        if (this.length > QQ.QQ_MAX_PACKET_SIZE) {
            int actLen = length;
            if (remaining == this.length) {
                buf.position(offset + QQ.QQ_LENGTH_DISK_FAMILY_IN_HEADER);
                actLen -= QQ.QQ_LENGTH_DISK_FAMILY_IN_HEADER;
            }
            DiskInPacketFragment fragment = null;
            try {
                fragment = new DiskInPacketFragment(buf, actLen, user);
                fragment.command = command;
                fragment.source = source;
                fragment.replyCode = QQ.QQ_REPLY_OK;
                remaining -= length;
            } catch (PacketParseException e) {
                fragment = null;
            }
            return fragment;
        } else {
            try {
                switch(buf.getChar(offset + 8)) {
                    case QQ.QQ_DISK_CMD_BEGIN_SESSION:
                        return new BeginSessionPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_AUTHENTICATE:
                        return new AuthenticateReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_GET_SERVER_LIST:
                        return new GetServerListReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_GET_SHARED_DISK:
                        return new GetSharedDiskReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_LIST_SHARED_DISK_DIR:
                        return new ListSharedDiskDirReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_LIST_MY_DISK_DIR:
                        return new ListMyDiskDirReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_PASSWORD_OP:
                        return new PasswordOpReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_DOWNLOAD:
                        return new DownloadReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_RENAME:
                        return new RenameReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_DELETE:
                        return new DeleteReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_CREATE:
                        return new CreateReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_MOVE:
                        return new MoveReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_UPLOAD:
                        return new UploadFileReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_FINALIZE:
                        return new FinalizeReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_PREPARE:
                        return new PrepareReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_GET_SIZE:
                        return new GetSizeReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_GET_SHARE_LIST:
                        return new GetShareListReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_SET_SHARE_LIST:
                        return new SetShareListReplyPacket(buf, length, user);
                    case QQ.QQ_DISK_CMD_APPLY:
                        return new ApplyReplyPacket(buf, length, user);
                    default:
                        return new UnknownDiskInPacket(buf, length, user);
                }
            } catch (PacketParseException e) {
                buf.position(offset);
                return new UnknownDiskInPacket(buf, length, user);
            }
        }
    }

    public OutPacket parseOutcoming(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        try {
            switch(buf.getChar(offset + 8)) {
                case QQ.QQ_DISK_CMD_AUTHENTICATE:
                    return new AuthenticatePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_GET_SERVER_LIST:
                    return new GetServerListPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_GET_SHARED_DISK:
                    return new GetSharedDiskPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_LIST_SHARED_DISK_DIR:
                    return new ListSharedDiskDirPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_LIST_MY_DISK_DIR:
                    return new ListMyDiskDirPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_PASSWORD_OP:
                    return new PasswordOpPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_RENAME:
                    return new RenamePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_DELETE:
                    return new DeletePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_CREATE:
                    return new CreatePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_MOVE:
                    return new MovePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_UPLOAD:
                    return new UploadFilePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_FINALIZE:
                    return new FinalizePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_PREPARE:
                    return new PreparePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_GET_SIZE:
                    return new GetSizePacket(buf, length, user);
                case QQ.QQ_DISK_CMD_GET_SHARE_LIST:
                    return new GetShareListPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_SET_SHARE_LIST:
                    return new SetShareListPacket(buf, length, user);
                case QQ.QQ_DISK_CMD_APPLY:
                    return new ApplyPacket(buf, length, user);
                default:
                    return new UnknownDiskOutPacket(buf, length, user);
            }
        } catch (PacketParseException e) {
            buf.position(offset);
            return new UnknownOutPacket(buf, length, user);
        }
    }

    public boolean isDuplicatedNeedReply(InPacket in) {
        return false;
    }

    public int relocate(ByteBuffer buf) {
        if (length > QQ.QQ_MAX_PACKET_SIZE) {
            int skip = Math.min(remaining, buf.remaining());
            remaining -= skip;
            return offset + skip;
        }
        int offset = buf.position();
        if (buf.remaining() < 4) return offset;
        int len = buf.getInt(offset) + 4;
        if (len == 0 || offset + len > buf.limit()) return offset; else return offset + len;
    }

    public PacketHistory getHistory() {
        return history;
    }

    public boolean isDuplicate(InPacket in) {
        if (in instanceof DiskInPacketFragment || in instanceof UploadFileReplyPacket) return false; else return history.check(in, true);
    }
}
