package com.road.site;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.lucene.search.TopDocCollector;
import org.springframework.web.servlet.ModelAndView;
import org.testng.log4testng.Logger;
import com.road.cache.Cache;
import com.road.cache.CacheExeption;
import com.road.cache.CacheFactory;
import com.road.index.IndexFactory;
import com.road.search.SearchException;

public class TalkingController extends BaseController implements LoginRequired, GeoRequired {

    private static Logger LOGGER = Logger.getLogger(TalkingController.class);

    private String getWord(HttpServletRequest request) {
        return request.getParameter("w");
    }

    @Override
    protected ModelAndView handleRequestSecured(HttpServletRequest request, HttpServletResponse response, UserContext user, GeoContext geo) throws SiteException {
        assert user != null;
        assert geo != null;
        String content = getContent(request);
        String talkto = getTalkto(request);
        String topicid = getTopicid(request);
        String road = user.getLocation();
        Map model = new HashMap();
        try {
            String talkid = IndexFactory.getInstance().getTalkIndex().nextTopicId();
            if (topicid == null || topicid.length() == 0) {
                topicid = talkid;
            }
            Talk newTalk = new Talk(topicid, talkid, content, new Date(), user.getName(), talkto, road, road);
            IndexFactory.getInstance().getTalkIndex().index(new Talk[] { newTalk });
        } catch (SearchException se) {
            throw new SiteException("fail to build index", se);
        }
        try {
            response.sendRedirect("/t?tid=" + topicid);
        } catch (IOException e) {
            LOGGER.error("can't forward the page into thread", e);
            throw new SiteException("can't find the page - " + "/t?tid=" + topicid, e);
        }
        return new ModelAndView("index", model);
    }

    private String getTopicid(HttpServletRequest request) {
        return request.getParameter("tid");
    }

    static Pattern p = Pattern.compile("to\\[(.*?)\\]:(.*)");

    private String getTalkto(HttpServletRequest request) {
        String content = request.getParameter("w");
        if (content.toLowerCase().startsWith("to[")) {
            Matcher m = p.matcher(content);
            if (m.matches()) {
                return m.group(1);
            }
        }
        return null;
    }

    private String getContent(HttpServletRequest request) {
        String content = request.getParameter("w");
        if (content.toLowerCase().startsWith("to[")) {
            Matcher m = p.matcher(content.toLowerCase());
            if (m.matches()) {
                return m.group(2);
            }
        }
        return content;
    }
}
