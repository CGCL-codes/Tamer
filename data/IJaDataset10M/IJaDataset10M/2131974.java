package com.mobfee.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.mobfee.business.facade.IMobfeeFacade;
import com.mobfee.domain.GameArticle;
import com.mobfee.domain.GameArticleReply;
import com.mobfee.domain.GameComment;
import com.mobfee.domain.GameInfo;
import com.mobfee.domain.Page;
import com.mobfee.domain.UserLevel;
import com.mobfee.domain.UserProfile;

public class ViewAllGameArticleReplysController implements Controller {

    private IMobfeeFacade facade;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content = "";
        try {
            content = request.getParameter("content");
        } catch (Exception e) {
        }
        if (content != null && content.length() > 1) {
            long articleId = Long.parseLong(request.getParameter("articleId"));
            String username = "�ο�";
            long userId = (long) 0;
            UserProfile user = (UserProfile) request.getSession().getAttribute(StringHelper.USER_SESSION);
            if (user == null) {
                String url = request.getRequestURL() + (request.getQueryString() == null ? "" : "?" + request.getQueryString());
                String pageUrl = url.substring(28);
                request.getSession().setAttribute(StringHelper.FORWARD_ACTION, pageUrl);
                return new ModelAndView("Error", "message", "��û��Ȩ��ִ�иò���");
            }
            if (user != null) {
                username = user.getUsername();
                userId = user.getUserId();
            }
            GameArticleReply gar = new GameArticleReply();
            SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String time = bartDateFormat.format(date).toString();
            gar.setReplyTime(time);
            gar.setContent(content);
            gar.setReferId(articleId);
            gar.setUserId(userId);
            gar.setUsername(username);
            gar.setState(1);
            try {
                facade.addOrUpdateGameArticleReply(gar);
                response.sendRedirect("viewGameArticle.do?articleId=" + articleId);
            } catch (Exception e) {
            }
        }
        long articleId = Long.parseLong(request.getParameter("articleId"));
        UserProfile user = (UserProfile) request.getSession().getAttribute(StringHelper.USER_SESSION);
        List<GameArticleReply> gameArticleReply = new ArrayList<GameArticleReply>();
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }
        int totalRows = 0;
        if (totalRows < 1) totalRows = facade.getGameArticleReplysRow(articleId);
        Page page = new Page(totalRows, 15);
        page.setCurrentPage(currentPage);
        gameArticleReply = facade.getGameArticleReplys(articleId, page.getStartRow(), page.getPageSize());
        GameArticle gameArticle = facade.getGameArticle(articleId);
        UserProfile userProfile = facade.getUser(gameArticle.getAuthorId());
        UserLevel userLevel = new UserLevel();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("user", user);
        model.put("userLevel", userLevel);
        model.put("gameArticleReply", gameArticleReply);
        model.put("gameArticle", gameArticle);
        model.put("totalRows", totalRows);
        model.put("page", page);
        return new ModelAndView("AllGameArticleReplys", "model", model);
    }

    public void setFacade(IMobfeeFacade facade) {
        this.facade = facade;
    }
}
