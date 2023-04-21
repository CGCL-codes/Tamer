package com.hcs.protocol.impl;

import com.hcs.protocol.utils.ExplainUtil;
import com.hcs.protocol.utils.HttpUtil;
import com.hcs.protocol.utils.ProtocolContanst;

/**
 * 系统名：HCSMobileApp 
 * 子系统名：心跳
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS CORPORATION ALL
 * 			RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 5, 2011
 */
public class HeartBeatProtocol {

    /**
	 * 为维持用户会话状态，客户端定时向服务器发送心跳
	 * @return 客户端心跳状态
	 * @throws Exception 
	 */
    public byte[] getHeartBeatProtocolParam() throws Exception {
        short msgId = ProtocolContanst.HEARTBEAT_MSG_ID;
        byte[] validateCode = new byte[3];
        byte[] retain = new byte[] { 0, 0, 0, 0, 0, 0 };
        byte[] msgBody = "000".getBytes(ProtocolContanst.CODE);
        validateCode = HttpUtil.getValidateCode(msgBody);
        byte[] msgHeader = HttpUtil.getHeaderByteInfo(msgId, ProtocolContanst.USER_TAG, validateCode, retain);
        byte[] resultByte = new byte[msgHeader.length + msgBody.length];
        System.arraycopy(msgHeader, 0, resultByte, 0, msgHeader.length);
        System.arraycopy(msgBody, 0, resultByte, msgHeader.length, msgBody.length);
        return resultByte;
    }

    /**
	 * 获取请求结果
	 * 
	 * @param result 返回结果
	 * @return 解码后返回结果
	 * @throws Exception 
	 */
    public int getResult(byte[] result) throws Exception {
        if (null == result || 0 > result.length) {
            return 0;
        }
        result = ExplainUtil.clientDecode(result);
        if (null != result) {
            return result[0];
        }
        return 0;
    }

    /**
	 * 获取String型的返回结果（用于测试）
	 * 
	 * @param result 返回结果
	 * @return 解码后返回结果
	 * @throws Exception 
	 */
    public String getResultString(byte[] result) throws Exception {
        if (null == result || 0 > result.length) {
            return "请求失败!";
        }
        result = ExplainUtil.clientDecode(result);
        if (null != result) {
            return "请求成功!" + result[0];
        }
        return "请求失败!";
    }
}
