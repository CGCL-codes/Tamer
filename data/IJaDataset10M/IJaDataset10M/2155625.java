package com.hand.test.servlet;

import java.util.LinkedList;
import java.util.List;
import com.hand.model.po.ClientMsg;
import com.hand.test.TestBase;
import com.hand.utils.ExplainUtil;
import com.hand.utils.HttpUtil;
import com.hand.utils.MsgPrint;
import com.hand.utils.MyUtils;
import com.hand.utils.ProtocolContanst;
import com.hand.utils.TypeConvert;

/**
 * 测试轮询反馈模块
 * 
 * @author zhongming.meng
 * @since 2011-06-14
 */
public class TestFeedBackRollingModel extends TestBase {

    public void testUpdatePollingData() {
        try {
            String userTag = "8442539375R8";
            byte[] paramBytes = getRequest(userTag);
            byte[] resultByte = HttpUtil.sendPost(ProtocolContanst.URL + userTag + "&isEncrypt=0", paramBytes);
            MsgPrint.showByteArray("resultByte", resultByte);
            try {
                ClientMsg clientMsg = ExplainUtil.clientDecode(resultByte);
                this.assertEquals(clientMsg.getParam()[0], 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
		 * 获取类型和指定时间（请求的字节数组内容）
		 * 
		 * @param userTag
		 *            用户标识
		 * @return 请求的字节数组内容
		 */
    private byte[] getRequest(String userTag) {
        short msgId = 1021;
        byte[] validateCode = new byte[3];
        byte[] retain = new byte[] { 0, 0, 0, 0, 0, 0 };
        String ordersID = "2,3,4";
        String bulltinsID = "121,122,123";
        byte[] msgBody = getMsgBody(ordersID, bulltinsID);
        validateCode = HttpUtil.getValidateCode(msgBody);
        byte[] msgHeader = HttpUtil.getHeaderByteInfo(msgId, userTag, validateCode, retain);
        byte[] resultByte = new byte[msgHeader.length + msgBody.length];
        System.arraycopy(msgHeader, 0, resultByte, 0, msgHeader.length);
        System.arraycopy(msgBody, 0, resultByte, msgHeader.length, msgBody.length);
        return resultByte;
    }

    private byte[] getMsgBody(String ordersID, String bulltinsID) {
        List<byte[]> byteList = new LinkedList<byte[]>();
        try {
            byte[] ordersIDB = ordersID.getBytes("utf-8");
            byte[] bulltinsIDB = bulltinsID.getBytes("utf-8");
            byteList.add(TypeConvert.int2bytesCoverse(ordersIDB.length));
            byteList.add(ordersIDB);
            byteList.add(TypeConvert.int2bytesCoverse(bulltinsIDB.length));
            byteList.add(bulltinsIDB);
            return MyUtils.byteListConvterToByteArray(byteList);
        } catch (Exception e) {
            return null;
        }
    }
}
