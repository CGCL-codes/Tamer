package com.hcs.protocol.utils;

import com.hcs.protocol.model.ClientMsg;
import com.hcs.utils.exc.CheckExcepiton;
import com.hcs.utils.exc.DecryptException;
import com.hcs.utils.exc.NotLoginExcepiton;
import com.hcs.utils.exc.NotMateProtocolExcepiton;
import com.hcs.utils.exc.SessionExpireExcepiton;

/**
 * 报文信息解码工具类
 * 
 * @author nianchun.li
 * @createTime 2011/5/4 16:48
 */
public class ExplainUtil {

    /**
	 * 对客户端上传的数据进行解码
	 * 
	 * @param bytes
	 *            上传字节信息
	 * @return 解码后封装客户消息数据结构类
	 */
    public static ClientMsg serverDecode(byte[] bytes) {
        ClientMsg clientMsg = new ClientMsg();
        int pos = 0;
        byte[] msgIdB = MyUtils.subByte(bytes, pos, 2);
        short msgId = TypeConvert.bytes2ShortConverse(msgIdB);
        clientMsg.setMsgId(msgId);
        MsgPrint.showMsg("消息ID=" + msgId);
        byte[] userTagB = MyUtils.subByte(bytes, pos += 2, 12);
        String userTag = TypeConvert.bytesConverseToStr(userTagB, "utf-8");
        clientMsg.setUserTag(userTag);
        MsgPrint.showMsg("userTag=" + userTag);
        byte[] md5CodeB = MyUtils.subByte(bytes, pos += 12, 3);
        clientMsg.setMd5Code(md5CodeB);
        MsgPrint.showMsg("md5Code=" + new String(md5CodeB));
        byte[] retainB = MyUtils.subByte(bytes, pos += 3, 6);
        MsgPrint.showMsg("保留信息=" + new String(retainB));
        byte[] paramB = MyUtils.subByte(bytes, pos += 6, (bytes.length - pos));
        clientMsg.setParam(paramB);
        return clientMsg;
    }

    /**
	 * 此方法用来解释服务器返回的数据
	 * 
	 * @param resultByte
	 *            服务器返回字节内容
	 * @return 返回封装后的消息对象
	 * @throws Exception
	 */
    public static byte[] clientDecode(byte[] resultByte) throws Exception {
        if (null == resultByte) {
            MsgPrint.showMsg("客户端返回结果为空！");
        }
        int pos = 0;
        byte[] identifierCodeB = MyUtils.subByte(resultByte, pos, 3);
        String identifierCode = TypeConvert.bytesConverseToStr(identifierCodeB, "utf-8");
        MsgPrint.showMsg("标识码=" + identifierCode);
        if ("001".equals(identifierCode)) {
            throw new CheckExcepiton();
        } else if ("002".equals(identifierCode)) {
            throw new DecryptException();
        } else if ("003".equals(identifierCode)) {
            throw new NotLoginExcepiton();
        } else if ("004".equals(identifierCode)) {
            throw new SessionExpireExcepiton();
        } else if ("005".equals(identifierCode)) {
            throw new NotMateProtocolExcepiton();
        } else if ("007".equals(identifierCode)) {
            throw new Exception();
        }
        byte[] messageIdB = MyUtils.subByte(resultByte, pos += 3, 2);
        short messageId = TypeConvert.bytes2ShortConverse(messageIdB);
        MsgPrint.showMsg("消息ID=" + messageId);
        byte[] userTagB = MyUtils.subByte(resultByte, pos += 2, 12);
        String userTag = TypeConvert.bytesConverseToStr(userTagB, "utf-8");
        ProtocolContanst.USER_TAG = userTag;
        System.out.println("userTag=" + ProtocolContanst.USER_TAG);
        byte[] validataCodeB = MyUtils.subByte(resultByte, pos += 12, 3);
        try {
            String validataCode = TypeConvert.bytesConverseToStr(validataCodeB, "utf-8");
            MsgPrint.showMsg("校验码=" + validataCode);
        } catch (RuntimeException e) {
            MsgPrint.showByteArray("validataCodeB", validataCodeB);
        }
        byte[] retainB = MyUtils.subByte(resultByte, pos += 3, 6);
        String retain = TypeConvert.bytesConverseToStr(retainB, "utf-8");
        MsgPrint.showMsg("保留=" + retain);
        byte[] returnByte = MyUtils.subByte(resultByte, pos += 6, resultByte.length - 26);
        return returnByte;
    }
}
