package com.laoer.bbscs.web.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServlet;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import com.laoer.bbscs.comm.*;

/**
 * @author laoer
 *
 */
public class SysListener extends HttpServlet implements ServletContextListener {

    private static final Log logger = LogFactory.getLog(SysListener.class);

    /**
	 *
	 */
    private static final long serialVersionUID = -7106672475983633319L;

    public void contextDestroyed(ServletContextEvent sce) {
    }

    public void contextInitialized(ServletContextEvent sce) {
        String rootpath = sce.getServletContext().getRealPath("/");
        if (rootpath != null) {
            rootpath = rootpath.replaceAll("\\\\", "/");
        } else {
            rootpath = "/";
        }
        if (!rootpath.endsWith("/")) {
            rootpath = rootpath + "/";
        }
        Constant.ROOTPATH = rootpath;
        logger.info("Application Run Path:" + rootpath);
        String urlrewrtie = sce.getServletContext().getInitParameter("urlrewrite");
        boolean burlrewrtie = false;
        if (urlrewrtie != null) {
            burlrewrtie = Boolean.parseBoolean(urlrewrtie);
        }
        Constant.USE_URL_REWRITE = burlrewrtie;
        logger.info("Use Urlrewrite:" + burlrewrtie);
        String cluster = sce.getServletContext().getInitParameter("cluster");
        boolean bcluster = false;
        if (cluster != null) {
            bcluster = Boolean.parseBoolean(cluster);
        }
        Constant.USE_CLUSTER = bcluster;
        logger.info("Use Cluster:" + bcluster);
        String servletmapping = sce.getServletContext().getInitParameter("servletmapping");
        if (servletmapping == null) {
            servletmapping = "*.bbscs";
        }
        Constant.SERVLET_MAPPING = servletmapping;
        logger.info("SERVLET MAPPING:" + servletmapping);
        String poststoragemodes = sce.getServletContext().getInitParameter("poststoragemode");
        if (poststoragemodes == null) {
            poststoragemodes = "0";
        }
        Constant.POST_STORAGE_MODE = NumberUtils.toInt(poststoragemodes, 0);
        logger.info("Post Storage Mode:" + poststoragemodes);
    }
}
