package com.hcs.protocol.impl;

import java.util.LinkedList;
import java.util.List;
import com.hcs.protocol.model.MouthBulletin;
import com.hcs.protocol.utils.ExplainUtil;
import com.hcs.protocol.utils.HttpUtil;
import com.hcs.protocol.utils.MyUtils;
import com.hcs.protocol.utils.ProtocolContanst;
import com.hcs.protocol.utils.TypeConvert;

/**
 * 系统名：HCSMobileApp
 * 子系统名：获取工作简报（当天）
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 5, 2011
 */
public class BriefProtocol {

    private static short idCard;

    private static short type;

    /**
	 * 获取工作简报（当天）
	 * @param idCard 身份（1.公司经理;2.销售人员）
	 * @param type	类型（1.预购;2.拜访）
	 * @param page	页数
	 * @param count	数量
	 * @return 当天简报列表信息
	 */
    public byte[] getBriefParam(short idCard, short type, int page, short count) {
        short msgId = ProtocolContanst.BRIEF_MSG_ID;
        byte[] validateCode = new byte[3];
        byte[] retain = new byte[] { 0, 0, 0, 0, 0, 0 };
        byte[] msgBody = getMsgBody(idCard, type, page, count);
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
	 */
    public List<MouthBulletin> getResult(byte[] result) {
        if (null == result || 0 > result.length) {
            return null;
        }
        try {
            byte[] param = ExplainUtil.clientDecode(result);
            int pos = 0;
            byte[] typeB = MyUtils.subByte(param, pos, 1);
            byte[] totalB = MyUtils.subByte(param, pos += 1, 4);
            int total = (int) TypeConvert.bytes2intConverse(totalB);
            List<MouthBulletin> bulletinList = new LinkedList<MouthBulletin>();
            if (1 == idCard) {
                if (1 == type) {
                    for (int i = 0; i < total; i++) {
                        MouthBulletin bulletin = new MouthBulletin();
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        bulletin.setName(name);
                        byte[] lowB = MyUtils.subByte(param, pos += nameLength, 1);
                        bulletin.setField1(String.valueOf(lowB[0]));
                        byte[] middleB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField2(String.valueOf(middleB[0]));
                        byte[] heightB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField3(String.valueOf(heightB[0]));
                        byte[] handleB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField4(String.valueOf(handleB[0]));
                        bulletinList.add(bulletin);
                        pos -= 3;
                    }
                } else {
                    for (int i = 0; i < total; i++) {
                        MouthBulletin bulletin = new MouthBulletin();
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        bulletin.setName(name);
                        byte[] oldCallOnB = MyUtils.subByte(param, pos += nameLength, 1);
                        bulletin.setField1(String.valueOf(oldCallOnB[0]));
                        byte[] newCallOnB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField2(String.valueOf(newCallOnB[0]));
                        byte[] oldCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField3(String.valueOf(oldCustomerB[0]));
                        byte[] newCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField4(String.valueOf(newCustomerB[0]));
                        bulletinList.add(bulletin);
                        pos -= 3;
                    }
                }
            } else {
                if (1 == type) {
                    for (int i = 0; i < total; i++) {
                        MouthBulletin bulletin = new MouthBulletin();
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        bulletin.setName(name);
                        byte[] CallOnCountB = MyUtils.subByte(param, pos += nameLength, 1);
                        bulletin.setField1(String.valueOf(CallOnCountB[0]));
                        byte[] newCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField2(String.valueOf(newCustomerB[0]));
                        bulletinList.add(bulletin);
                        pos -= 3;
                    }
                } else {
                    for (int i = 0; i < total; i++) {
                        MouthBulletin bulletin = new MouthBulletin();
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        bulletin.setName(name);
                        byte[] oldCallOnB = MyUtils.subByte(param, pos += nameLength, 1);
                        bulletin.setField1(String.valueOf(oldCallOnB[0]));
                        byte[] thirtyDayCallOnCountB = MyUtils.subByte(param, pos += 1, 1);
                        bulletin.setField2(String.valueOf(thirtyDayCallOnCountB[0]));
                        bulletinList.add(bulletin);
                        pos -= 3;
                    }
                }
            }
            return bulletinList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取String型的返回结果（用于测试）
	 * 
	 * @param result 返回结果
	 * @return 解码后返回结果
	 */
    public String getResultString(byte[] result) {
        if (null == result || 0 > result.length) {
            return "fail!";
        }
        try {
            StringBuffer sb = new StringBuffer();
            byte[] param = ExplainUtil.clientDecode(result);
            int pos = 0;
            byte[] typeB = MyUtils.subByte(param, pos, 1);
            sb.append("类型=" + typeB[0] + "\n");
            byte[] totalB = MyUtils.subByte(param, pos += 1, 4);
            int total = (int) TypeConvert.bytes2intConverse(totalB);
            sb.append("总记录数=" + total + "\n");
            if (1 == idCard) {
                if (1 == type) {
                    for (int i = 0; i < total; i++) {
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        sb.append("销售人员名称长度=" + nameLength + "\n");
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        sb.append("销售人员名称=" + name + "\n");
                        byte[] lowB = MyUtils.subByte(param, pos += nameLength, 1);
                        sb.append("低=" + lowB[0] + "\n");
                        byte[] middleB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("中=" + middleB[0] + "\n");
                        byte[] heightB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("高=" + heightB[0] + "\n");
                        byte[] handleB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("受注残=" + handleB[0] + "\n");
                        pos -= 3;
                    }
                } else {
                    for (int i = 0; i < total; i++) {
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        sb.append("销售人员名称长度=" + nameLength + "\n");
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        sb.append("销售人员名称=" + name + "\n");
                        byte[] oldCallOnB = MyUtils.subByte(param, pos += nameLength, 1);
                        sb.append("老客户（拜访）=" + oldCallOnB[0] + "\n");
                        byte[] newCallOnB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("新客户（拜访）=" + newCallOnB[0] + "\n");
                        byte[] oldCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("老客户（新建）=" + oldCustomerB[0] + "\n");
                        byte[] newCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("新客户（新建）=" + newCustomerB[0] + "\n");
                        pos -= 3;
                    }
                }
            } else {
                if (1 == type) {
                    for (int i = 0; i < total; i++) {
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        sb.append("客户名称长度=" + nameLength + "\n");
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        sb.append("客户名称=" + name + "\n");
                        byte[] CallOnCountB = MyUtils.subByte(param, pos += nameLength, 1);
                        sb.append("拜访次数=" + CallOnCountB[0] + "\n");
                        byte[] newCustomerB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("新建客户数=" + newCustomerB[0] + "\n");
                        pos -= 3;
                    }
                } else {
                    for (int i = 0; i < total; i++) {
                        byte[] nameLengthB = MyUtils.subByte(param, pos += 4, 1);
                        int nameLength = nameLengthB[0];
                        sb.append("客户名称长度=" + nameLength + "\n");
                        byte[] nameB = MyUtils.subByte(param, pos += 1, nameLength);
                        String name = new String(nameB, ProtocolContanst.CODE);
                        sb.append("客户名称=" + name + "\n");
                        byte[] oldCallOnB = MyUtils.subByte(param, pos += nameLength, 1);
                        sb.append("购买可能性=" + oldCallOnB[0] + "\n");
                        byte[] thirtyDayCallOnCountB = MyUtils.subByte(param, pos += 1, 1);
                        sb.append("过去30天拜访数=" + thirtyDayCallOnCountB[0] + "\n");
                        pos -= 3;
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail!";
    }

    /**
	 * 获取报文体内容
	 * @param idCard 身份
	 * @param type 类型
	 * @param page 页数
	 * @param count 数量
	 * @return 报文体内容
	 */
    private byte[] getMsgBody(short idCard, short type, int page, short count) {
        List<byte[]> byteList = new LinkedList<byte[]>();
        try {
            this.idCard = idCard;
            this.type = type;
            byteList.add(new byte[] { (byte) idCard });
            byteList.add(new byte[] { (byte) type });
            byteList.add(TypeConvert.int2bytesCoverse(page));
            byteList.add(new byte[] { (byte) count });
            return MyUtils.byteListConvterToByteArray(byteList);
        } catch (Exception e) {
            return null;
        }
    }
}
