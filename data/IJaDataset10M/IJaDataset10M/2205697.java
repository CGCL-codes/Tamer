package net.sf.dlcdb.webapp.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.dlcdb.dbobjects.DocumentUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class DLCappController implements Controller {

    private DocumentUser user;

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final Log logger = LogFactory.getLog(getClass());
        String now = (new java.util.Date()).toString();
        logger.info("returning hello view with " + now);
        Map modelObjects = new HashMap();
        modelObjects.put("now", now);
        modelObjects.put("user", getUser());
        return new ModelAndView("hello", "model", modelObjects);
    }

    public DocumentUser getUser() {
        return user;
    }

    public void setUser(DocumentUser user) {
        this.user = user;
    }
}
