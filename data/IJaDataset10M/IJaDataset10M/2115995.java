package com.ipolyglot.webapp.dwr;

import java.util.List;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import com.ipolyglot.model.Lesson;
import com.ipolyglot.service.LessonManager;

public class LessonListDWRUtil {

    public String getLessonListHTML(String wordLanguageId, String translationLanguageId, HttpServletRequest request) {
        ServletContext servletContext = request.getSession().getServletContext();
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        LessonManager lessonManager = (LessonManager) ctx.getBean("lessonManager");
        List<Lesson> lessonList = lessonManager.getLessons(null, true, wordLanguageId, translationLanguageId, null);
        String ret = "";
        for (Lesson lesson : lessonList) {
            ret += "<a href='lesson" + lesson.getId() + "' class='lessonLanguagesMenu'>" + lesson.getName() + "</a><br/>";
        }
        return ret;
    }
}
