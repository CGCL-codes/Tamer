package com.hcs.service.impl;

import java.util.List;
import com.hcs.service.IMAppHallService;
import com.hcs.service.model.MouthBulletin;
import com.hcs.service.model.NewNotice;
import com.hcs.service.utils.HttpUtil;
import com.hcs.service.utils.ProtocolContanst;

/**
 * 系统名：HCSMobileApp
 * 子系统名：手机应用大厅接口实现
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 10, 2011
 */
public class MAppHallService implements IMAppHallService {

    HttpUtil httpUtil = null;

    /**
	 * 获取工作简报（当天）
	 */
    public List<MouthBulletin> getBrief(short idCard, short type, int page, short count) {
        if (0 == idCard || 0 == type || 0 == page || 0 == count) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            BriefProtocol briefProtocol = new BriefProtocol();
            byte[] param = briefProtocol.getBriefParam(idCard, type, page, count);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return briefProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 最新公告信息
	 */
    public List<NewNotice> getNewNotices() {
        try {
            httpUtil = new HttpUtil();
            NewNoticeProtocol newNoticeProtocol = new NewNoticeProtocol();
            byte[] param = newNoticeProtocol.getNewNoticeProtocolParam();
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return newNoticeProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
