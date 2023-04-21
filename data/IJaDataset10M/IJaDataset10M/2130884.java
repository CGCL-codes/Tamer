package com.newsbeef.site;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;
import com.newsbeef.dao.FeedInfoDao;

public class FeedSubscribeController implements Controller {

    private FeedInfoDao feedInfoDao;

    public void setFeedInfoDao(FeedInfoDao feedDao) {
        this.feedInfoDao = feedDao;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String url = request.getParameter("url");
        feedInfoDao.addFeed("http://rss.slashdot.org/Slashdot/slashdot");
        return new ModelAndView("feeds", "url", url);
    }
}
