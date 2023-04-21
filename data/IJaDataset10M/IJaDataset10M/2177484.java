package com.hcs.service.impl;

import java.util.LinkedList;
import java.util.List;
import com.hcs.service.utils.ExplainUtil;
import com.hcs.service.utils.HttpUtil;
import com.hcs.service.utils.MyUtils;
import com.hcs.service.utils.ProtocolContanst;

/**
 * 系统名：HCSMobileApp 
 * 子系统名：修改密码 
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS
 * 			CORPORATION ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 5, 2011
 */
public class UpdatePwdProtocol {

    /**
	 * 修改密码
	 * 
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param confirmPwd
	 *            确认密码
	 * @return 添加电话号码请求参数
	 */
    public byte[] getUpdatePwdParam(String oldPwd, String newPwd, String confirmPwd) {
        short msgId = ProtocolContanst.RE_PWD_MSG_ID;
        byte[] validateCode = new byte[3];
        byte[] retain = new byte[] { 0, 0, 0, 0, 0, 0 };
        byte[] msgBody = getMsgBody(oldPwd, newPwd, confirmPwd);
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
	 * @return 修改是否成功（0.失败 1.成功）
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

    /**
	 * 获取报文体内容
	 * 
	 * @param oldPwd
	 *            旧密码
	 * @param newPwd
	 *            新密码
	 * @param confirmNewPwd
	 *            确认新密码
	 * @return 报文体内容
	 */
    private byte[] getMsgBody(String oldPwd, String newPwd, String confirmNewPwd) {
        List<byte[]> byteList = new LinkedList<byte[]>();
        try {
            byte[] accountB = oldPwd.getBytes("utf-8");
            byte[] passwrodB = newPwd.getBytes("utf-8");
            byte[] confirmNewPwdB = confirmNewPwd.getBytes("utf-8");
            byteList.add(new byte[] { (byte) accountB.length });
            byteList.add(accountB);
            byteList.add(new byte[] { (byte) passwrodB.length });
            byteList.add(passwrodB);
            byteList.add(new byte[] { (byte) confirmNewPwdB.length });
            byteList.add(confirmNewPwdB);
            return MyUtils.byteListConvterToByteArray(byteList);
        } catch (Exception e) {
            return null;
        }
    }
}
