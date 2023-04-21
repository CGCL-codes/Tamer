package com.javaeye.lonlysky.lforum.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.struts2.config.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import com.javaeye.lonlysky.lforum.ForumBaseAction;
import com.javaeye.lonlysky.lforum.comm.LForumRequest;
import com.javaeye.lonlysky.lforum.comm.utils.ForumUtils;
import com.javaeye.lonlysky.lforum.comm.utils.Utils;
import com.javaeye.lonlysky.lforum.service.ScoresetManager;
import com.javaeye.lonlysky.lforum.service.SearchManager;
import com.javaeye.lonlysky.lforum.service.StatisticManager;
import com.javaeye.lonlysky.lforum.service.UserCreditManager;

/**
 * 搜索页面
 * 
 * @author 黄磊
 *
 */
@ParentPackage("default")
public class SearchAction extends ForumBaseAction {

    private static final long serialVersionUID = -2273066142411318954L;

    /**
	 * 版块列表
	 */
    private String forumlist;

    /**
	 * 搜索缓存Id
	 */
    private int searchid;

    /**
	 * 当前页码
	 */
    private int pageid;

    /**
	 * 主题数量
	 */
    private int topiccount;

    /**
	 * 日志数量
	 */
    private int blogcount;

    /**
	 * 分页数量
	 */
    private int pagecount;

    /**
	 * 分页页码链接
	 */
    private String pagenumbers;

    /**
	 * 搜索结果数量
	 */
    private int searchresultcount;

    /**
	 * 搜索出的主题列表
	 */
    private List<Object[]> topiclist;

    /**
	 * 当此值为true时,显示搜索结果提示
	 */
    private boolean searchpost;

    /**
	 * 搜索类型
	 */
    private String type = "post";

    /**
	 * 当前主题页码
	 */
    private int topicpageid;

    /**
	 * 主题分页总数
	 */
    private int topicpagecount;

    /**
	 * 主题分页页码链接
	 */
    private String topicpagenumbers;

    @Autowired
    private ScoresetManager scoresetManager;

    @Autowired
    private StatisticManager statisticManager;

    @Autowired
    private UserCreditManager userCreditManager;

    @Autowired
    private SearchManager searchManager;

    @SuppressWarnings("unchecked")
    @Override
    public String execute() throws Exception {
        pagetitle = "搜索";
        searchid = LForumRequest.getParamIntValue("searchid", -1);
        searchresultcount = 0;
        if (usergroupinfo.getAllowsearch() == 0) {
            reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有搜索的权限");
            return SUCCESS;
        }
        if (usergroupinfo.getAllowsearch() == 2 && LForumRequest.getParamIntValue("keywordtype", 0) == 1) {
            reqcfg.addErrLine("您当前的身份 \"" + usergroupinfo.getGrouptitle() + "\" 没有全文搜索的权限");
            return SUCCESS;
        }
        searchpost = false;
        if (LForumRequest.isPost() || !LForumRequest.getParamValue("posterid").equals("")) {
            searchpost = true;
            if (useradminid != 1 && LForumRequest.getParamIntValue("keywordtype", 0) == 1 && usergroupinfo.getDisableperiodctrl() != 1) {
                String visittime = scoresetManager.betweenTime(config.getSearchbanperiods());
                if (!visittime.equals("")) {
                    reqcfg.addErrLine("在此时间段( " + visittime + " )内用户不可以进行全文搜索");
                    return SUCCESS;
                }
            }
            if (useradminid != 1) {
                if (!statisticManager.checkSearchCount(config.getMaxspm())) {
                    reqcfg.addErrLine("抱歉,系统在一分钟内搜索的次数超过了系统安全设置的上限,请稍候再试");
                    return SUCCESS;
                }
                int interval = Utils.strDateDiffSeconds(lastsearchtime, config.getSearchctrl());
                if (interval <= 0) {
                    reqcfg.addErrLine("系统规定搜索间隔为" + config.getSearchctrl() + "秒, 您还需要等待 " + (interval * -1) + " 秒");
                    return SUCCESS;
                }
                if (userCreditManager.updateUserCreditsBySearch(userid) == -1) {
                    reqcfg.addErrLine("您的积分不足, 不能执行搜索操作");
                    return SUCCESS;
                }
            }
            int posterid = LForumRequest.getParamIntValue("posterid", -1);
            int searchtime = LForumRequest.getParamIntValue("searchtime", 0);
            String[] forumidlist = LForumRequest.getParamValues("searchforumid");
            String searchforumid = "";
            if (forumidlist != null) {
                for (String str : forumidlist) {
                    System.out.println("板块ID：" + str);
                    searchforumid = searchforumid + str + ",";
                }
                if (searchforumid.substring(searchforumid.length() - 1).equals(",")) {
                    searchforumid = searchforumid.substring(0, searchforumid.length() - 1);
                }
                System.out.println("板块IDLIST：" + searchforumid);
            }
            if (LForumRequest.getParamValue("keyword").equals("") && LForumRequest.getParamValue("poster").equals("") && LForumRequest.getParamValue("posterid").equals("")) {
                reqcfg.addErrLine("关键字和用户名不能同时为空");
                return SUCCESS;
            }
            if (posterid > 0) {
                if (!userManager.exists(posterid)) {
                    reqcfg.addErrLine("指定的用户ID不存在");
                    return SUCCESS;
                }
            } else if (!LForumRequest.getParamValue("poster").equals("")) {
                posterid = userManager.getUserId(LForumRequest.getParamValue("poster"));
                if (posterid == -1) {
                    reqcfg.addErrLine("搜索用户名不存在");
                    return SUCCESS;
                }
            }
            if (!searchforumid.trim().equals("")) {
                for (String strid : forumidlist) {
                    if (!Utils.isInt(strid)) {
                        reqcfg.addErrLine("非法的搜索版块ID");
                        return SUCCESS;
                    }
                }
            }
            type = LForumRequest.getParamValue("type").toLowerCase();
            int keywordtype = LForumRequest.getParamIntValue("keywordtype", 0);
            if (type == "author") keywordtype = 8;
            if (LForumRequest.getParamValue("keyword").equals("") && posterid > 0 && type.equals("")) {
                type = "author";
                keywordtype = 8;
            }
            if (type != "") {
                if (!Utils.inArray(type, "post,digest,author")) {
                    reqcfg.addErrLine("非法的参数信息");
                    return SUCCESS;
                }
            }
            searchid = searchManager.search(userid, usergroupid, LForumRequest.getParamValue("keyword").trim(), posterid, type, searchforumid, keywordtype, searchtime, LForumRequest.getParamIntValue("searchtimetype", 0), LForumRequest.getParamIntValue("resultorder", 0), LForumRequest.getParamIntValue("resultordertype", 0));
            if (keywordtype == 8) {
                type = "author";
            } else {
                type = "";
            }
            if (searchid > 0) {
                reqcfg.setUrl("search.action?type=" + type + "&searchid=" + searchid);
                reqcfg.setMetaRefresh().setShowBackLink(false).addMsgLine("搜索完毕, 稍后将转到搜索结果页面");
            } else {
                reqcfg.addMsgLine("抱歉, 没有搜索到符合要求的记录");
            }
            return SUCCESS;
        } else {
            searchid = LForumRequest.getParamIntValue("searchid", -1);
            if (searchid > 0) {
                pageid = LForumRequest.getParamIntValue("page", 1);
                type = LForumRequest.getParamValue("type").toLowerCase();
                if (!type.equals("")) {
                    if (!Utils.inArray(type, "topic,author")) {
                        reqcfg.addErrLine("非法的参数信息");
                        return SUCCESS;
                    }
                }
                Map<Integer, Integer> map = new HashMap<Integer, Integer>();
                if (type.equals("author")) {
                    topicpageid = LForumRequest.getParamIntValue("topicpage", 1);
                    topiclist = searchManager.getSearchCacheList(searchid, 16, topicpageid, map, "");
                    topiccount = map.get(0);
                    topicpageid = calculateCurrentPage(topiccount, topicpageid, map);
                    topicpagecount = map.get(0);
                    topicpagenumbers = ForumUtils.getPageNumbers(topicpageid, topicpagecount, "search.action?show=topic&type=" + type + "&searchid=" + searchid, 8, "topicpage", "#1");
                    return SUCCESS;
                } else {
                    topiclist = searchManager.getSearchCacheList(searchid, 16, pageid, map, type);
                    topiccount = map.get(0);
                }
                if (topiccount == 0) {
                    reqcfg.addErrLine("不存在的searchid");
                    return SUCCESS;
                }
                calculateCurrentPage();
                pagenumbers = ForumUtils.getPageNumbers(pageid, pagecount, "search.action?type=" + type + "&searchid=" + searchid, 8);
                return SUCCESS;
            } else {
                forumlist = cachesManager.getForumListBoxOptionsCache();
            }
        }
        return SUCCESS;
    }

    private void calculateCurrentPage() {
        pagecount = topiccount % 16 == 0 ? topiccount / 16 : topiccount / 16 + 1;
        if (pagecount == 0) {
            pagecount = 1;
        }
        if (pageid < 1) {
            pageid = 1;
        }
        if (pageid > pagecount) {
            pageid = pagecount;
        }
    }

    private int calculateCurrentPage(int listcount, int pageid, Map<Integer, Integer> pagecount) {
        pagecount.put(0, listcount % 16 == 0 ? listcount / 16 : listcount / 16 + 1);
        if (pagecount.get(0) == 0) {
            pagecount.put(0, 1);
        }
        if (pageid < 1) {
            pageid = 1;
        }
        if (pageid > pagecount.get(0)) {
            pageid = pagecount.get(0);
        }
        return pageid;
    }

    public String getForumlist() {
        return forumlist;
    }

    public int getSearchid() {
        return searchid;
    }

    public int getPageid() {
        return pageid;
    }

    public int getTopiccount() {
        return topiccount;
    }

    public int getBlogcount() {
        return blogcount;
    }

    public int getPagecount() {
        return pagecount;
    }

    public String getPagenumbers() {
        return pagenumbers;
    }

    public int getSearchresultcount() {
        return searchresultcount;
    }

    public List<Object[]> getTopiclist() {
        return topiclist;
    }

    public boolean isSearchpost() {
        return searchpost;
    }

    public String getType() {
        return type;
    }

    public int getTopicpageid() {
        return topicpageid;
    }

    public int getTopicpagecount() {
        return topicpagecount;
    }

    public String getTopicpagenumbers() {
        return topicpagenumbers;
    }
}
