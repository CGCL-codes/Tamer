package com.nodeshop.action.shop;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import com.nodeshop.bean.Pager;
import com.nodeshop.bean.SystemConfig;
import com.nodeshop.entity.Footer;
import com.nodeshop.entity.FriendLink;
import com.nodeshop.entity.Member;
import com.nodeshop.entity.Navigation;
import com.nodeshop.service.FooterService;
import com.nodeshop.service.FriendLinkService;
import com.nodeshop.service.MemberService;
import com.nodeshop.service.NavigationService;
import com.nodeshop.util.SystemConfigUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

/**
 * 前台Action类 - 前台基类
  
 * 版权所有 2008-2010 长沙鼎诚软件有限公司，并保留所有权利。
  
 
  
 
 
 * KEY: nodeshop08EDCB64FEA84DDFDCD64F97F83B43F1
 
 */
public class BaseShopAction extends ActionSupport {

    private static final long serialVersionUID = 6718838811223344556L;

    public static final String VIEW = "view";

    public static final String LIST = "list";

    public static final String STATUS = "status";

    public static final String WARN = "warn";

    public static final String SUCCESS = "success";

    public static final String ERROR = "error";

    public static final String MESSAGE = "message";

    public static final String CONTENT = "content";

    protected String id;

    protected String[] ids;

    protected Pager pager;

    protected String logInfo;

    protected String redirectionUrl;

    @Resource
    protected MemberService memberService;

    @Resource
    protected NavigationService navigationService;

    @Resource
    protected FriendLinkService friendLinkService;

    @Resource
    protected FooterService footerService;

    public String input() {
        return null;
    }

    public SystemConfig getSystemConfig() {
        return SystemConfigUtil.getSystemConfig();
    }

    public String getPriceCurrencyFormat() {
        return SystemConfigUtil.getPriceCurrencyFormat();
    }

    public String getPriceUnitCurrencyFormat() {
        return SystemConfigUtil.getPriceUnitCurrencyFormat();
    }

    public String getOrderCurrencyFormat() {
        return SystemConfigUtil.getOrderCurrencyFormat();
    }

    public String getOrderUnitCurrencyFormat() {
        return SystemConfigUtil.getOrderUnitCurrencyFormat();
    }

    public Member getLoginMember() {
        String id = (String) getSession(Member.LOGIN_MEMBER_ID_SESSION_NAME);
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        Member loginMember = memberService.load(id);
        return loginMember;
    }

    public Object getAttribute(String name) {
        return ServletActionContext.getRequest().getAttribute(name);
    }

    public void setAttribute(String name, Object value) {
        ServletActionContext.getRequest().setAttribute(name, value);
    }

    public String getParameter(String name) {
        return getRequest().getParameter(name);
    }

    public String[] getParameterValues(String name) {
        return getRequest().getParameterValues(name);
    }

    public Object getSession(String name) {
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        return session.get(name);
    }

    public Map<String, Object> getSession() {
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        return session;
    }

    public void setSession(String name, Object value) {
        ActionContext actionContext = ActionContext.getContext();
        Map<String, Object> session = actionContext.getSession();
        session.put(name, value);
    }

    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public ServletContext getApplication() {
        return ServletActionContext.getServletContext();
    }

    public String ajax(String content, String type) {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType(type + ";charset=UTF-8");
            response.setHeader("Pragma", "No-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);
            response.getWriter().write(content);
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String ajaxText(String text) {
        return ajax(text, "text/plain");
    }

    public String ajaxHtml(String html) {
        return ajax(html, "text/html");
    }

    public String ajaxXml(String xml) {
        return ajax(xml, "text/xml");
    }

    public String ajaxJson(String jsonString) {
        return ajax(jsonString, "text/html");
    }

    public String ajaxJson(Map<String, String> jsonMap) {
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        return ajax(jsonObject.toString(), "text/html");
    }

    public String ajaxJsonWarnMessage(String message) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, WARN);
        jsonMap.put(MESSAGE, message);
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        return ajax(jsonObject.toString(), "text/html");
    }

    public String ajaxJsonSuccessMessage(String message) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, SUCCESS);
        jsonMap.put(MESSAGE, message);
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        return ajax(jsonObject.toString(), "text/html");
    }

    public String ajaxJsonErrorMessage(String message) {
        Map<String, String> jsonMap = new HashMap<String, String>();
        jsonMap.put(STATUS, ERROR);
        jsonMap.put(MESSAGE, message);
        JSONObject jsonObject = JSONObject.fromObject(jsonMap);
        return ajax(jsonObject.toString(), "text/html");
    }

    public void setResponseNoCache() {
        getResponse().setHeader("progma", "no-cache");
        getResponse().setHeader("Cache-Control", "no-cache");
        getResponse().setHeader("Cache-Control", "no-store");
        getResponse().setDateHeader("Expires", 0);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public Pager getPager() {
        return pager;
    }

    public void setPager(Pager pager) {
        this.pager = pager;
    }

    public String getLogInfo() {
        return logInfo;
    }

    public void setLogInfo(String logInfo) {
        this.logInfo = logInfo;
    }

    public String getRedirectionUrl() {
        return redirectionUrl;
    }

    public void setRedirectionUrl(String redirectionUrl) {
        this.redirectionUrl = redirectionUrl;
    }

    public List<Navigation> getTopNavigationList() {
        return navigationService.getTopNavigationList();
    }

    public List<Navigation> getMiddleNavigationList() {
        return navigationService.getMiddleNavigationList();
    }

    public List<Navigation> getBottomNavigationList() {
        return navigationService.getBottomNavigationList();
    }

    public List<FriendLink> getFriendLinkList() {
        return friendLinkService.getAll();
    }

    public List<FriendLink> getPictureFriendLinkList() {
        return friendLinkService.getPictureFriendLinkList();
    }

    public List<FriendLink> getTextFriendLinkList() {
        return friendLinkService.getTextFriendLinkList();
    }

    public Footer getFooter() {
        return footerService.getFooter();
    }
}
