package org.gtugs.web;

import org.gtugs.domain.Chapter;
import org.gtugs.service.ChapterManager;
import org.gtugs.service.security.AppEngineUserService;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jasonacooper@google.com (Jason Cooper)
 */
public class AdminController implements Controller {

    private ChapterManager chapterManager;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Map<String, Object> model = new HashMap<String, Object>();
        AppEngineUserService userService = new AppEngineUserService();
        model.put("userService", userService);
        List<List<Chapter>> chapters = chapterManager.getChaptersGroupedByCountry();
        model.put("chapters", chapters);
        return new ModelAndView("admin", "model", model);
    }

    public void setChapterManager(ChapterManager chapterManager) {
        this.chapterManager = chapterManager;
    }
}
