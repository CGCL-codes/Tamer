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
import com.mobfee.domain.Topic;
import com.mobfee.domain.GameArticle;
import com.mobfee.domain.GameArticleReply;
import com.mobfee.domain.GameComment;
import com.mobfee.domain.GameInfo;
import com.mobfee.domain.Page;
import com.mobfee.domain.TopicReply;
import com.mobfee.domain.UserLevel;
import com.mobfee.domain.UserProfile;

public class ViewBBSArticleController implements Controller {

    private IMobfeeFacade facade;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        long topicId = Long.parseLong(request.getParameter("topicId"));
        Topic topic = facade.getTopic(topicId);
        topic.setViewTimes(topic.getViewTimes() + 1);
        facade.addOrUpdateTopic(topic);
        UserProfile author = facade.getUser(topic.getAuthorId());
        UserProfile user = (UserProfile) request.getSession().getAttribute(StringHelper.USER_SESSION);
        List<TopicReply> topicReplys = new ArrayList<TopicReply>();
        int currentPage = 1;
        try {
            currentPage = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
        }
        int totalRows = 0;
        if (totalRows < 1) totalRows = facade.getTopicReplyRows(topicId);
        Page page = new Page(totalRows, 2);
        page.setCurrentPage(currentPage);
        topicReplys = facade.getTopicReplys(topicId, page.getStartRow(), page.getPageSize());
        UserLevel userLevel = new UserLevel();
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("topic", topic);
        model.put("author", author);
        model.put("user", user);
        model.put("page", page);
        model.put("userLevel", userLevel);
        model.put("topicReplys", topicReplys);
        model.put("totalRows", totalRows);
        return new ModelAndView("BBSArticleDetail", "model", model);
    }

    public void setFacade(IMobfeeFacade facade) {
        this.facade = facade;
    }
}
