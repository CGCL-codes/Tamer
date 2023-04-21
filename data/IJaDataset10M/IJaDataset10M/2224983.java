package org.promotego.admin;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import org.promotego.dao.interfaces.UserDao;
import org.promotego.runmode.RunMode;
import org.promotego.runmode.RunModeChooser;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.context.ServletContextAware;

/**
 * @author alf
 *
 */
public class SystemStatus implements ServletContextAware {

    private UserDao m_userDao;

    private RunModeChooser m_runModeChooser;

    private ServletContext m_servletContext;

    public Map<String, Object> getStatusMap() {
        Map<String, Object> retval = new HashMap<String, Object>();
        retval.put("User Count", getUserCount());
        retval.put("Run Mode", getRunMode());
        retval.put("Server Info", m_servletContext.getServerInfo());
        return retval;
    }

    private int getUserCount() {
        return m_userDao.getCount();
    }

    private RunMode getRunMode() {
        return m_runModeChooser.getRunMode();
    }

    @Required
    public void setUserDao(UserDao userDao) {
        m_userDao = userDao;
    }

    @Required
    public void setRunModeChooser(RunModeChooser runModeChooser) {
        m_runModeChooser = runModeChooser;
    }

    @Required
    public void setServletContext(ServletContext servletContext) {
        m_servletContext = servletContext;
    }
}
