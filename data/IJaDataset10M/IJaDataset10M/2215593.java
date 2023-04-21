package com.mobfee.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.mobfee.business.facade.IMobfeeFacade;
import com.mobfee.domain.GameArticle;
import com.mobfee.domain.GameArticleReply;
import com.mobfee.domain.GameInfo;
import com.mobfee.domain.UserProfile;

public class ChangeStateOfGameArticleReplyController implements Controller {

    private IMobfeeFacade facade;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long articleId = Long.parseLong(request.getParameter("articleId"));
        String action = request.getParameter("action").toString();
        long replyId = Long.parseLong(request.getParameter("replyId"));
        UserProfile user = (UserProfile) request.getSession().getAttribute(StringHelper.USER_SESSION);
        GameArticleReply gameArticleReply = facade.getGameArticleReply(replyId);
        if (gameArticleReply != null && user != null) {
            if (user.isMaintenanceAdmin()) {
                if (action.equals("resume")) gameArticleReply.setState(1);
                if (action.equals("delete")) gameArticleReply.setState(0);
                facade.addOrUpdateGameArticleReply(gameArticleReply);
                response.sendRedirect("viewGameArticle.do?articleId=" + articleId);
                return null;
            } else {
                return new ModelAndView("Error", "message", "��û��Ȩ��ִ�иò���");
            }
        } else {
            return new ModelAndView("Error", "message", "��Ҫɾ��Ĳ�����");
        }
    }

    public void setFacade(IMobfeeFacade facade) {
        this.facade = facade;
    }
}
