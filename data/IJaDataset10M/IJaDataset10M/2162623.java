package com.hcs.service.impl;

import java.util.List;
import com.hcs.service.IInfoManagerService;
import com.hcs.service.model.ColligateData;
import com.hcs.service.model.FavoritePhone;
import com.hcs.service.model.HistoryInfo;
import com.hcs.service.model.TCustomerWeb;
import com.hcs.service.model.TPurchaseinadvanceWeb;
import com.hcs.service.model.TVisitWeb;
import com.hcs.service.model.TWorkBulletinWeb;
import com.hcs.service.utils.HttpUtil;
import com.hcs.service.utils.ProtocolContanst;

/**
 * 系统名：HCSMobileApp
 * 子系统名：信息管理模块接口实现
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 10, 2011
 */
public class InfoManagerService implements IInfoManagerService {

    HttpUtil httpUtil = null;

    /**
	 * 历史信息查询
	 */
    public List<HistoryInfo> getHistoryInfoList(short idCard, short type, String time, int page, short count) {
        if (0 == idCard || 0 == type || null == time || "".equals(time) || 0 == page || 0 == count) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            HistoryInfoProtocol historyInfoProtocol = new HistoryInfoProtocol();
            byte[] param = historyInfoProtocol.getHistoryInfoProtocolParam(type, time, page, count);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return historyInfoProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取历史详细信息 说明：根据信息ID获取历史详细信息
	 */
    public HistoryInfo getHistoryDetail(int id, short type) {
        if (0 == id) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            HistoryDetailProtocol historyDetailProtocol = new HistoryDetailProtocol();
            byte[] param = historyDetailProtocol.getHistoryDetailProtocolParam(id, type);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return historyDetailProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 信息查询
	 */
    public ColligateData infoSearch(String name, String mobile, String time) {
        if (null == name || "".equals(name) || null == mobile || "".equals(mobile) || null == time || "".equals(time)) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            InfoListProtocol infoListProtocol = new InfoListProtocol();
            byte[] param = infoListProtocol.getInfoListProtocolParam(name, mobile, time);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return infoListProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 简报查询
	 */
    public List<TWorkBulletinWeb> getBriefList(String time, int page, short count) {
        if (null == time || "".equals(time) || 0 == page || 0 == count) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            BriefListProtocol briefListProtocol = new BriefListProtocol();
            byte[] param = briefListProtocol.getBriefListParam(time, page, count);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return briefListProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 根据类型和指定时间获取详细简报信息
	 */
    public TWorkBulletinWeb getBriefDetail(int briefId) {
        if (0 == briefId) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            BriefDetailProtocol briefDetailProtocol = new BriefDetailProtocol();
            byte[] param = briefDetailProtocol.getBriefDetailParam(briefId);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return briefDetailProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 新建客户信息
	 */
    public int addCustomer(TCustomerWeb customerWeb) {
        if (null == customerWeb) {
            return 0;
        }
        try {
            httpUtil = new HttpUtil();
            NewCustomerProtocol newCustomerProtocol = new NewCustomerProtocol();
            byte[] param = newCustomerProtocol.getNewCustomerProtocolParam(customerWeb);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return newCustomerProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 新建预购信息
	 */
    public int addTPurchaseinadvanceWeb(TPurchaseinadvanceWeb purchaseinadvanceWeb) {
        if (null == purchaseinadvanceWeb) {
            return 0;
        }
        try {
            httpUtil = new HttpUtil();
            NewOrderProtocol newOrderProtocol = new NewOrderProtocol();
            byte[] param = newOrderProtocol.getNewOrderProtocolParam(purchaseinadvanceWeb);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return newOrderProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 新建拜访信息
	 */
    public int addTVisitWeb(TVisitWeb visitWeb) {
        if (null == visitWeb) {
            return 0;
        }
        try {
            httpUtil = new HttpUtil();
            NewVisitedProtocol newVisitedProtocol = new NewVisitedProtocol();
            byte[] param = newVisitedProtocol.getNewVisitedProtocolParam(visitWeb);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return newVisitedProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 根据用户ＩＤ获取收藏电话列表
	 */
    public List<FavoritePhone> getPhoneList(int userId, int page, short count) {
        if (0 == userId || 0 == page || 0 == count) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            PhoneListProtocol phoneListProtocol = new PhoneListProtocol();
            byte[] param = phoneListProtocol.getPhoneListParam(page, count);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return phoneListProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 新增加电话记录
	 */
    public int addPhone(String userName, String mobile) {
        if (null == userName || "".equals(userName) || null == mobile || "".equals(mobile)) {
            return 0;
        }
        try {
            httpUtil = new HttpUtil();
            AddPhoneProtocol addPhoneProtocol = new AddPhoneProtocol();
            byte[] param = addPhoneProtocol.getAddPhoneParam(userName, mobile);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return addPhoneProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
	 * 获取预购指导信息列表
	 */
    public List<TPurchaseinadvanceWeb> getOrderDirect(int page, int count) {
        if (0 == page || 0 == count) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            OrderDirectProtocol orderDirectProtocol = new OrderDirectProtocol();
            byte[] param = orderDirectProtocol.getOrderDirectProtocolParam(page, count);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return orderDirectProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 获取预购指导详细信息
	 */
    public TPurchaseinadvanceWeb getDirectDetail(int directId) {
        if (0 == directId) {
            return null;
        }
        try {
            httpUtil = new HttpUtil();
            DirectDetailProtocol directDetailProtocol = new DirectDetailProtocol();
            byte[] param = directDetailProtocol.getDirectDetailParam(directId);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return directDetailProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * 回复指导信息
	 */
    public int reDirect(int directId, String directContent) {
        if (0 == directId || null == directContent || "".equals(directContent)) {
            return 0;
        }
        try {
            httpUtil = new HttpUtil();
            ReDirectProtocol reDirectProtocol = new ReDirectProtocol();
            byte[] param = reDirectProtocol.getReDirectParam(directId, directContent);
            param = httpUtil.sendPost(ProtocolContanst.URL + ProtocolContanst.USER_TAG, param);
            return reDirectProtocol.getResult(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
