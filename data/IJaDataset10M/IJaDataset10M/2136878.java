package com.hand.servlet.sub;

import java.util.Date;
import org.springframework.web.context.WebApplicationContext;
import com.hand.model.TPurchaseinadvanceWeb;
import com.hand.model.po.ClientMsg;
import com.hand.service.IPurchasingService;
import com.hand.utils.MsgPrint;
import com.hand.utils.MyUtils;
import com.hand.utils.ProtocolContanst;
import com.hand.utils.TypeConvert;
import com.hand.utils.exc.CheckExcepiton;

/**
 * 系统名：HCSMobileApp 
 * 子系统名：回复指导信息
 * 著作权：COPYRIGHT (C) 2011 HAND INFORMATION SYSTEMS CORPORATION ALL
 * 			RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 5, 2011
 */
public class ReDirectModel {

    /**
	 * 回复指导信息
	 * 
	 * @param wac
	 *            提供注入service服务接口
	 * @param clientMsg
	 *            客户端上传内容
	 * @return 回复结果
	 */
    public byte[] getReDirect(WebApplicationContext wac, ClientMsg clientMsg) {
        if (null == clientMsg.getParam()) {
            return null;
        }
        int result = 0;
        try {
            int pos = 0;
            byte[] param = clientMsg.getParam();
            byte[] directIdB = MyUtils.subByte(param, pos, 4);
            int directId = (int) TypeConvert.bytes2intConverse(directIdB);
            MsgPrint.showMsg("预购指导ＩＤ=" + directId);
            byte[] directContentLengthB = MyUtils.subByte(param, pos += 4, 2);
            int directContentLength = TypeConvert.bytes2ShortConverse(directContentLengthB);
            MsgPrint.showMsg("指导内容长度=" + directContentLength);
            byte[] directContentB = MyUtils.subByte(param, pos += 2, directContentLength);
            String directContent = new String(directContentB, ProtocolContanst.CODE);
            MsgPrint.showMsg("指导内容=" + directContent);
            if (0 == directId || null == directContent || "".equals(directContent)) {
                result = 2;
                throw new CheckExcepiton();
            }
            IPurchasingService purchasingService = (IPurchasingService) wac.getBean("purchasingService");
            TPurchaseinadvanceWeb piaWeb = purchasingService.getTPurchaseinadvanceWebById(directId);
            if (null == piaWeb) {
                return new byte[] { (byte) 0 };
            }
            piaWeb.setEvaluationCommentC(directContent);
            piaWeb.setFeedbackDateC(new Date());
            piaWeb.setSendFlag((short) 3);
            if (purchasingService.updateTPurchasein(piaWeb) == 0) {
                result = 0;
            } else {
                result = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[] { (byte) result };
    }
}
