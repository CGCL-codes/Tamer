package com.hand.servlet.sub;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.springframework.web.context.WebApplicationContext;
import com.hand.model.TPurchaseinadvanceWeb;
import com.hand.model.TVisitWeb;
import com.hand.model.po.ClientMsg;
import com.hand.service.IPurchasingService;
import com.hand.service.IVisitWebService;
import com.hand.utils.MsgPrint;
import com.hand.utils.MyUtils;
import com.hand.utils.ProtocolContanst;
import com.hand.utils.TypeConvert;
import com.hand.utils.cache.UserBaseInfo;
import com.hand.utils.cache.UserInfo;
import com.hand.utils.exc.CheckExcepiton;

/**
 * 系统名：HCSMobileApp 
 * 子系统名：历史信息查询
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS CORPORATION ALL
 * 			RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 5, 2011
 */
public class HistoryInfoModel {

    /**
	 * 历史信息查询
	 * 
	 * @param wac
	 *            提供注入service服务接口
	 * @param clientMsg
	 *            客户端上传内容
	 * @return 历史信息列表
	 */
    public byte[] getHistoryInfo(WebApplicationContext wac, ClientMsg clientMsg) {
        if (null == clientMsg.getParam()) {
            return null;
        }
        try {
            int pos = 0;
            byte[] param = clientMsg.getParam();
            byte[] typeB = MyUtils.subByte(param, pos, 1);
            MsgPrint.showMsg("类型=" + typeB[0]);
            byte[] timeB = MyUtils.subByte(param, pos += 1, 10);
            String time = new String(timeB, ProtocolContanst.CODE);
            MsgPrint.showMsg("时间=" + time);
            byte[] pageB = MyUtils.subByte(param, pos += 10, 4);
            int page = (int) TypeConvert.bytes2intConverse(pageB);
            MsgPrint.showMsg("页数=" + page);
            byte[] countB = MyUtils.subByte(param, pos += 4, 1);
            MsgPrint.showMsg("数量=" + countB[0]);
            if (0 == countB[0]) {
                throw new CheckExcepiton();
            }
            IPurchasingService purchasingService = (IPurchasingService) wac.getBean("purchasingService");
            IVisitWebService visitWebService = (IVisitWebService) wac.getBean("visitWebService");
            Map<String, UserInfo> map = UserBaseInfo.getInstant().getUserInfoMap();
            UserInfo userInfo = map.get(clientMsg.getUserTag());
            List<byte[]> byteList = new LinkedList<byte[]>();
            if (2 == typeB[0]) {
                int total = visitWebService.getTVisitWebTotal(userInfo.getUserIdHi(), time);
                if (0 != total) {
                    List<TVisitWeb> visitWebList = visitWebService.getTVisitWebs(userInfo.getUserIdHi(), time, page, countB[0]);
                    byte[] totalB = TypeConvert.int2bytesCoverse(total);
                    byteList.add(totalB);
                    byteList.add(new byte[] { (byte) visitWebList.size() });
                    Iterator iter = visitWebList.iterator();
                    while (iter.hasNext()) {
                        TVisitWeb visitWeb = (TVisitWeb) iter.next();
                        String salesman = visitWeb.getAccountName();
                        int type = 2;
                        time = MyUtils.dateFormat("yyyy-MM-dd", visitWeb.getCreateDate());
                        byte[] salesmanB = salesman.getBytes(ProtocolContanst.CODE);
                        byte[] time1B = time.getBytes(ProtocolContanst.CODE);
                        byteList.add(new byte[] { (byte) salesmanB.length });
                        byteList.add(salesmanB);
                        byteList.add(new byte[] { (byte) type });
                        byteList.add(time1B);
                    }
                } else {
                    return TypeConvert.int2bytesCoverse(0);
                }
            } else {
                int total = purchasingService.getTPurchasingTotal(userInfo.getUserIdHi(), time);
                if (0 != total) {
                    List<TPurchaseinadvanceWeb> piaWebList = purchasingService.getTPurchasings(userInfo.getUserIdHi(), time, page, countB[0]);
                    byte[] totalB = TypeConvert.int2bytesCoverse(total);
                    byteList.add(totalB);
                    byteList.add(new byte[] { (byte) piaWebList.size() });
                    Iterator iter = piaWebList.iterator();
                    while (iter.hasNext()) {
                        TPurchaseinadvanceWeb piaWeb = (TPurchaseinadvanceWeb) iter.next();
                        String salesman = piaWeb.getAccountName();
                        int type = 1;
                        time = MyUtils.dateFormat("yyyy-MM-dd", piaWeb.getCreateDate());
                        byte[] salesmanB = salesman.getBytes(ProtocolContanst.CODE);
                        byte[] time1B = time.getBytes(ProtocolContanst.CODE);
                        byteList.add(new byte[] { (byte) salesmanB.length });
                        byteList.add(salesmanB);
                        byteList.add(new byte[] { (byte) type });
                        byteList.add(time1B);
                    }
                } else {
                    return TypeConvert.int2bytesCoverse(0);
                }
            }
            return MyUtils.byteListConvterToByteArray(byteList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
