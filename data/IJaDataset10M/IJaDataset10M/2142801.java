package net.sf.jqql.packets.in;

import java.nio.ByteBuffer;
import net.sf.jqql.Util;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicInPacket;

/**
 * <pre>
 * 认证问题操作的回复包
 * <p/>
 * ******** 格式1，得到认证问题的回复包 ********
 * 头部
 * ------- 加密开始，会话密钥 --------
 * 子命令，1字节，0x03
 * 未知2字节，0x0001
 * 回复码，1字节 （如果回复码为0x00，继续后面的部分）
 * 问题长度，1字节
 * 问题
 * ------- 加密结束 --------
 * 尾部
 * <p/>
 * ******** 格式2，回答问题的回复包 ********
 * 头部
 * ------- 加密开始，会话密钥 --------
 * 子命令，1字节，0x03
 * 未知2字节，0x0001
 * 回复码，1字节 （如果回复码为0x00，继续后面的部分）
 * 验证信息长度，2字节
 * 验证信息
 * ------- 加密结束 --------
 * 尾部
 * </pre>
 *
 * @author luma
 */
public class AuthQuestionOpReplyPacket extends _08BasicInPacket {

    public byte subCommand;

    public byte replyCode;

    public byte[] authInfo;

    public String question;

    public AuthQuestionOpReplyPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    @Override
    protected void parseBody(ByteBuffer buf) throws PacketParseException {
        subCommand = buf.get();
        buf.getChar();
        replyCode = buf.get();
        switch(subCommand) {
            case QQ.QQ_SUB_CMD_GET_QUESTION:
                if (replyCode == QQ.QQ_REPLY_OK) {
                    int len = buf.get() & 0xFF;
                    question = Util.getString(buf, len);
                }
                break;
            case QQ.QQ_SUB_CMD_ANSWER_QUESTION:
                if (replyCode == QQ.QQ_REPLY_OK) {
                    int len = buf.getChar();
                    authInfo = new byte[len];
                    buf.get(authInfo);
                }
                break;
        }
    }

    @Override
    public String getPacketName() {
        return "Auth Question Op Reply _08Packet";
    }
}
