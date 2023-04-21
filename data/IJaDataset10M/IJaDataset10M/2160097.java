package edu.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import edu.domains.User;

public class GoEdit extends ActionSupport {

    private int manageType;

    private Map<String, String> types;

    public String execute() throws Exception {
        User user = (User) ActionContext.getContext().getSession().get("user");
        if (null == user) {
            ActionContext.getContext().getSession().put("error", "请先登录");
            return INPUT;
        }
        if ("00000000".equals(user.getPermission())) {
            ActionContext.getContext().getSession().put("error", "您没有权利管理该板块");
            return INPUT;
        }
        if (manageType < 0 || manageType > 6) {
            return ERROR;
        }
        Map<String, String> temp = new HashMap<String, String>();
        if (manageType == 0) {
            temp.put("xxjj", "学校简介");
            temp.put("ldgh", "领导关怀");
            temp.put("gzzd", "规章制度");
            temp.put("bxcg", "办学成果");
        } else if (manageType == 1) {
            temp.put("zhxw", "综合新闻");
        } else if (manageType == 2) {
            temp.put("kyda", "科研档案");
            temp.put("jxsj", "教学设计");
            temp.put("stzx", "试题中心");
            temp.put("jxdjt", "教学大家谈");
            temp.put("jsfc", "教师风采");
        } else if (manageType == 3) {
            temp.put("pkhz", "贫困互助");
            temp.put("jjzx", "家教在线");
            temp.put("wdsh", "我的书画");
            temp.put("wdwz", "我的文章");
            temp.put("zwbs", "征文比赛");
        } else if (manageType == 4) {
            temp.put("xytp", "校园图片");
            temp.put("spwj", "视频文件");
            temp.put("hdtp", "活动图片");
        } else if (manageType == 5) {
            temp.put("jxrj", "教学软件");
            temp.put("jxsck", "教学素材库");
            temp.put("cyrj", "常用软件");
        } else if (manageType == 6) {
            temp.put("bw", "博文");
            temp.put("xzwj", "校长文集");
        }
        types = temp;
        return SUCCESS;
    }

    public int getManageType() {
        return manageType;
    }

    public void setManageType(int manageType) {
        this.manageType = manageType;
    }

    public Map<String, String> getTypes() {
        return types;
    }

    public void setTypes(Map<String, String> types) {
        this.types = types;
    }
}
