package com.javaeye.lonlysky.lforum.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.entity.IndexOnline;
import com.javaeye.lonlysky.lforum.entity.forum.ForumStatistics;
import com.javaeye.lonlysky.lforum.service.OnlineUserManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;

/**
 * 在线用户列表页
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class OnlineuserAction extends ForumBaseAction {

    private static final long serialVersionUID = 7376056274568146981L;

    /**
	 * 在线用户列表
	 */
    private List<IndexOnline> onlineuserlist = new ArrayList<IndexOnline>();

    /**
	 * 在线用户数
	 */
    private int onlineusernumber = 0;

    /**
	 * 当前页码
	 */
    private int pageid = 0;

    /**
	 * 总页数
	 */
    private int pagecount = 0;

    /**
	 * 分页页码链接
	 */
    private String pagenumbers = "";

    /**
	 * 在线用户总数
	 */
    private int totalonline;

    /**
	 * 在线注册用户数
	 */
    private int totalonlineuser;

    /**
	 * 在线游客数
	 */
    private int totalonlineguest;

    /**
	 * 在线隐身用户数
	 */
    private int totalonlineinvisibleuser;

    /**
	 * 最高在线用户数
	 */
    private String highestonlineusercount;

    /**
	 * 最高在线用户数发生时间
	 */
    private String highestonlineusertime;

    private int upp = 16;

    private int startrow = 0;

    private int endrow = 0;

    @Autowired
    private StatisticManager statisticManager;

    @Override
    public String execute() throws Exception {
        pagetitle = "在线列表";
        Map<String, Integer> countMap = new HashMap<String, Integer>();
        List<IndexOnline> allonlineuserlist = onlineUserManager.getOnlineUserList(countMap);
        totalonline = countMap.get(OnlineUserManager.TOTALUSER);
        totalonlineuser = countMap.get(OnlineUserManager.USER);
        totalonlineguest = countMap.get(OnlineUserManager.GUESTUSER);
        totalonlineinvisibleuser = countMap.get(OnlineUserManager.INVISIBLEUSER);
        onlineusernumber = onlineusercount;
        pagecount = onlineusernumber % upp == 0 ? onlineusernumber / upp : onlineusernumber / upp + 1;
        if (pagecount == 0) {
            pagecount = 1;
        }
        pageid = LForumRequest.getParamIntValue("page", 1);
        if (pageid <= 1) {
            pageid = 1;
            startrow = 0;
            endrow = upp - 1;
        } else {
            if (pageid > pagecount) {
                pageid = pagecount;
            }
            startrow = (pageid - 1) * upp;
            endrow = pageid * upp;
        }
        if (startrow >= onlineusernumber) startrow = onlineusernumber - 1;
        if (endrow >= onlineusernumber) endrow = onlineusernumber - 1;
        for (; startrow <= endrow; startrow++) {
            onlineuserlist.add(allonlineuserlist.get(startrow));
        }
        if (LForumRequest.getParamValue("search").equals("")) {
            pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "onlineuser.action", 8);
        } else {
            pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "onlineuser.action", 8);
        }
        totalonline = onlineusercount;
        ForumStatistics statistics = statisticManager.getStatistic();
        highestonlineusercount = statistics.getHighestonlineusercount().toString();
        highestonlineusertime = statistics.getHighestonlineusertime();
        return SUCCESS;
    }

    public List<IndexOnline> getOnlineuserlist() {
        return onlineuserlist;
    }

    public int getOnlineusernumber() {
        return onlineusernumber;
    }

    public int getPageid() {
        return pageid;
    }

    public int getPagecount() {
        return pagecount;
    }

    public String getPagenumbers() {
        return pagenumbers;
    }

    public int getTotalonline() {
        return totalonline;
    }

    public int getTotalonlineuser() {
        return totalonlineuser;
    }

    public int getTotalonlineguest() {
        return totalonlineguest;
    }

    public int getTotalonlineinvisibleuser() {
        return totalonlineinvisibleuser;
    }

    public String getHighestonlineusercount() {
        return highestonlineusercount;
    }

    public String getHighestonlineusertime() {
        return highestonlineusertime;
    }
}
