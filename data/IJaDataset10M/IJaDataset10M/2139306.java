package com.javaeedev.web.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import com.javaeedev.domain.Announcement;
import com.javaeedev.util.HttpUtil;
import com.javaeedev.web.AbstractBaseController;
import com.javaeedev.web.Page;

/**
 * Show announcement.
 * 
 * @author Xuefeng
 * 
 * @spring.bean name="/home/announcement.jspx"
 * @spring.property name="signon" value="false"
 */
public class AnnouncementController extends AbstractBaseController {

    @Override
    @SuppressWarnings("unchecked")
    protected ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String id = HttpUtil.getString(request, "id");
        List<Announcement> announcements = facade.queryAnnouncements(new Page(1, 5));
        Announcement announcement = facade.queryAnnouncement(id);
        Map map = new HashMap();
        map.put("announcement", announcement);
        map.put("announcements", announcements);
        return new ModelAndView("/home/announcement.htm", map);
    }
}
