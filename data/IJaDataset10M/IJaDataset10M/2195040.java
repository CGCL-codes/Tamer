package net.sf.jqql.packets.out._08;

import java.nio.ByteBuffer;
import net.sf.jqql.Util;
import net.sf.jqql.QQ;
import net.sf.jqql.beans.QQUser;
import net.sf.jqql.packets.PacketParseException;
import net.sf.jqql.packets._08BasicOutPacket;

/**
 * @author seraph
 *         <p/>
 *         硕大无比的登陆包 包头和包围不必说了把，和别的都是一样的 下面是加密包体（其实是部分加密）
 *         ***********************************************************
 *         1. 2字节的登陆令牌的长度0038（未加密）
 *         2. 56字节的登陆令牌的内容（未加密）
 *         ****************************以下为加密内容********************
 *         3. 2字节未知作用的0000
 *         4. 2字节用来描述未知数据1的长度
 *         5. 32字节的未知作用的数据
 *         6. 16字节的未知作用的数据
 *         7. 固定的19字节的00
 *         8. 固定的16字节未知数据
 *         9. 1字节填充数据，可近似看作是固定的
 *         10. 11字节的未知作用的数据
 *         11. 2字节的00
 *         12. 4字节的网络序 unit
 *         13. 4字节的网络序 unit
 *         14. 1字节的固定内容 00
 *         15. 4字节的推荐服务器地址， 没有则是00
 *         16. 固定的16字节内容
 *         17. 未知作用的数据2的长度 20
 *         18. 数据2的内容
 *         19. 固定的45字节的内容
 *         20. 2字节的00
 *         21. 4字节的网络序unit
 *         22. 4字节的网络序unit
 *         23. 4字节的推荐服务器地址，和上面的一定要完全一样
 *         24. 固定的24字节
 *         25. 固定的248字节填充
 */
public class _08LoginPacket extends _08BasicOutPacket {

    public _08LoginPacket(ByteBuffer buf, int length, QQUser user) throws PacketParseException {
        super(buf, length, user);
    }

    public _08LoginPacket(QQUser user) {
        super(QQ.QQ_CMD_LOGIN, true, user);
    }

    @Override
    public String getPacketName() {
        return "07 Login _08Packet";
    }

    @Override
    protected int getEncryptStart() {
        return QQ.QQ_LOGIN_TOKEN_LENGTH;
    }

    @Override
    protected int getDecryptStart() {
        return QQ.QQ_LOGIN_TOKEN_LENGTH;
    }

    @Override
    public byte[] getDecryptKey(byte[] body) {
        return user.getLoginKey();
    }

    @Override
    public byte[] getEncryptKey(byte[] body) {
        return user.getLoginKey();
    }

    /**
     * 就为了那20字节，也就是那用tea加密过的32个字节，解密后就是20个字节
     * 16字节的一次md5密码和四个神秘未知字节
     *
     * @return 组装好的20个字节
     * @author seraph
     */
    private byte[] getImportant16Bytes() {
        byte[] b = user.getPasswordMd5();
        byte[] b1 = new byte[20];
        ByteBuffer bb = ByteBuffer.allocate(QQ.QQ_MAX_PACKET_SIZE);
        bb.put(b);
        bb.putChar((char) 0);
        bb.putChar(user.getVerifySuffix());
        bb.position(0);
        bb.get(b1);
        bb.clear();
        return b1;
    }

    @Override
    protected void putBody(ByteBuffer buf) {
        buf.putChar((char) user.getLoginToken().length);
        buf.put(user.getLoginToken());
        buf.putInt(32);
        buf.put(crypter.encrypt(getImportant16Bytes(), user.getPasswordKey()));
        buf.put(crypter.encrypt("".getBytes(), user.getPasswordKey()));
        buf.put(QQ.QQ_LOGIN_FIX_4_35_);
        buf.put(user.getRandomByte());
        buf.put(user.getLoginMode());
        buf.put(QQ.QQ_LOGIN_FIX_7_10_);
        if (user.getServerIp() == null || Util.getIpStringFromBytes(user.getServerIp()).equalsIgnoreCase("0.0.0.0")) {
            buf.put(QQ.QQ_PRELOGIN_1_15);
        } else {
            buf.put(QQ.QQ_LOGIN_FIX_11);
            buf.put(user.getServerIp());
            log.debug("推荐的服务器地址是：" + Util.getIpStringFromBytes(user.getServerIp()));
        }
        buf.put(QQ.QQ_LOGIN_FIX_11_16_);
        buf.put((byte) user.getTempToken().length);
        buf.put(user.getTempToken());
        buf.put(QQ.QQ_LOGIN_FIX_16_332_);
    }
}
