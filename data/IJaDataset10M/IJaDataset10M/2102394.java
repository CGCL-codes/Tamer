package com.feyaSoft.home.web.feedback;

import java.io.PrintWriter;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.feyaSoft.home.dao.base.BaseSearchResult;
import com.feyaSoft.home.hibernate.feedback.Feedback;

/**
 * @author Fenqiang Zhuang
 * @Oct 10, 2007
 * 
 * This file is used to .....
 */
public class FeedbackUtil {

    /**
	 * Set Feedback parameter - from user input
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    public static Feedback setFeedback(HttpServletRequest request, Feedback feedback) throws Exception {
        if (request.getParameter("subject") != null) {
            String subject = (String) request.getParameter("subject").trim();
            feedback.setSubject(subject);
        }
        if (request.getParameter("description") != null) {
            String description = (String) request.getParameter("description").trim();
            feedback.setDescription(description);
        }
        if (request.getParameter("author") != null) {
            String author = (String) request.getParameter("author").trim();
            feedback.setAuthor(author);
        }
        if (request.getParameter("email") != null) {
            String email = (String) request.getParameter("email").trim();
            feedback.setEmail(email);
        }
        if (request.getParameter("parentId") != null) {
            String parentId = (String) request.getParameter("parentId").trim();
            feedback.setParentId(new Long(parentId));
        }
        return feedback;
    }

    /**
	 * create JSon response out - for list of health page
	 * 
	 * @param request
	 * @param response
	 * @param tsts
	 * @throws Exception
	 */
    public static void jsonResponse(BaseSearchResult searchResult, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FeedbackJson tsJson = new FeedbackJson();
        List users = (List) searchResult.getResults();
        tsJson.setResults(users);
        tsJson.setTotalCount(searchResult.getTotalCount().intValue());
        boolean scriptTag = false;
        String cb = request.getParameter("callback");
        if (cb != null) {
            scriptTag = true;
            response.setContentType("text/javascript");
        } else {
            response.setContentType("application/x-json");
        }
        PrintWriter out = response.getWriter();
        if (scriptTag) {
            out.write(cb + "(");
        }
        response.getWriter().print(tsJson.toJSONObject());
        if (scriptTag) {
            out.write(");");
        }
    }
}
