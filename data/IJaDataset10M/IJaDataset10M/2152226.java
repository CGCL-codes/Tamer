package net.allblog.leech.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import net.allblog.leech.siteInfo.SiteInfo;
import net.allblog.leech.siteInfo.SiteInfoDAO;
import net.allblog.leech.siteInfo.SiteInfoDAOImpl;

public class SiteInfoAction extends HttpServlet {

    Logger logger = Logger.getLogger(SiteInfoAction.class);

    public void doGet(HttpServletRequest req, HttpServletResponse res) {
        try {
            req.setCharacterEncoding("UTF-8");
            res.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error("Servlet Encoding Exception", e1);
        }
        SiteInfoDAO sid = new SiteInfoDAOImpl();
        List<SiteInfo> list = (List<SiteInfo>) sid.getSiteInfo();
        req.setAttribute("list", list);
        try {
            req.getRequestDispatcher("/web/jsp/siteInfo.jsp").forward(req, res);
        } catch (ServletException e) {
            logger.error(e.getLocalizedMessage(), e);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) {
        try {
            req.setCharacterEncoding("UTF-8");
            res.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getLocalizedMessage(), e1);
        }
        String method = (String) req.getParameter("_method");
        if (method.equalsIgnoreCase("delete")) {
            this.doDelete(req, res);
            return;
        } else if (method.equalsIgnoreCase("put")) {
            this.doPut(req, res);
            return;
        }
        SiteInfo si = new SiteInfo(req);
        SiteInfoDAO sid = new SiteInfoDAOImpl();
        sid.addSiteInfo(si);
        this.doGet(req, res);
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse res) {
        try {
            req.setCharacterEncoding("UTF-8");
            res.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getLocalizedMessage(), e1);
        }
        SiteInfo si = new SiteInfo(req);
        SiteInfoDAO sid = new SiteInfoDAOImpl();
        sid.delSiteInfo(si);
        req.setAttribute("_method", "delete");
        this.doGet(req, res);
    }

    public void doPut(HttpServletRequest req, HttpServletResponse res) {
        try {
            req.setCharacterEncoding("UTF-8");
            res.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            logger.error(e1.getLocalizedMessage(), e1);
        }
        SiteInfo si = new SiteInfo(req);
        SiteInfoDAO sid = new SiteInfoDAOImpl();
        sid.modSiteInfo(si);
        req.setAttribute("_method", "put");
        this.doGet(req, res);
    }
}
