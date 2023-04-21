package com.hand.service.impl;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.hand.model.po.NewNotice;
import com.hand.service.INewNoticeService;
import com.hand.utils.MyUtils;

/**
 * 系统名：HCSMobileApp
 * 子系统名：最新公告service接口实现
 * 著作权：COPYRIGHT (C) 2011 HAND 
 *			INFORMATION SYSTEMS CORPORATION  ALL RIGHTS RESERVED.
 * @author nianchun.li
 * @createTime May 11, 2011
 */
public class NewNoticeService implements INewNoticeService {

    /**
	 * 获取最新公告信息
	 */
    public List<NewNotice> getNewNoticeInfo() {
        List<NewNotice> newNoticeList = new LinkedList<NewNotice>();
        NewNotice newNotice = new NewNotice();
        String content = "2011年劳动节节假日安排通知,4月30日至5月2日放假公休，共3天." + "节假日期间，各地区、各部门要妥善安排好值班和安全、保卫等工作，遇有重大突发事" + "件发生，要按规定及时报告并妥善处置，确保人民群众祥和平安度过节日假期。";
        String name = "赵旭";
        newNotice.setContent(content);
        newNotice.setName(name);
        newNotice.setTime(MyUtils.dateFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        newNoticeList.add(newNotice);
        newNotice = new NewNotice();
        content = "关于进步完善公司管理通知，自2011年5月3号起，各部门将本部门管理规范条例" + "有关实施细则和办法整理成上传文档，发送到上级部门，截止时间为2011年5月9号。" + "望认真落实。特此通知！";
        name = "李年春";
        newNotice.setContent(content);
        newNotice.setName(name);
        newNotice.setTime(MyUtils.dateFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        newNoticeList.add(newNotice);
        newNotice = new NewNotice();
        content = "关于组织篮球比赛通知，鉴于各成员反映，及宣传委研究确定于2011年5月12号下" + "午1点组织篮球比赛，要求各部门推荐一至三名篮球参赛者到宣传部，望各部门积极报" + "名，特此通知！";
        name = "李观音生";
        newNotice.setContent(content);
        newNotice.setName(name);
        newNotice.setTime(MyUtils.dateFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        newNoticeList.add(newNotice);
        newNotice = new NewNotice();
        content = "销售主管部门向浦东分公司下发有关手机最新价格的调整，希望各部门及时查看，并" + "制定销售措施，上发一份给所属上级部门.";
        name = "陈泽民";
        newNotice.setContent(content);
        newNotice.setName(name);
        newNotice.setTime(MyUtils.dateFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        newNoticeList.add(newNotice);
        newNotice = new NewNotice();
        content = "通知：各部门销售人员于明天下午(2011/5/5)二点整到会议室开电视电话会议.望准" + "时到达！ 人事部 曾国兴";
        name = "张小双";
        newNotice.setContent(content);
        newNotice.setName(name);
        newNotice.setTime(MyUtils.dateFormat("yyyy-MM-dd HH:mm:ss", new Date()));
        newNoticeList.add(newNotice);
        return newNoticeList;
    }
}
