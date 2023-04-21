package net.scarabocio.controllers;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.scarabocio.blo.JdbcMethods;
import net.scarabocio.blo.PostBo;
import net.scarabocio.blo.TypoImporter;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.parancoe.util.MemoryAppender;
import org.parancoe.web.util.FlashHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/admin/*.html")
public class AdminController {

    private static final Logger logger = Logger.getLogger(AdminController.class);

    @Resource
    private PostBo postBo;

    @Resource
    TypoImporter typoImporter;

    @Resource
    JdbcMethods jdbcMethods;

    @RequestMapping
    public ModelAndView index(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("admin/index", null);
    }

    @RequestMapping
    public ModelAndView logs(HttpServletRequest req, HttpServletResponse res) {
        if ("true".equals(req.getParameter("clean"))) {
            MemoryAppender.clean();
        }
        if ("error".equals(req.getParameter("test"))) logger.error("sample error message");
        if ("warn".equals(req.getParameter("test"))) logger.warn("sample warn message");
        String log = MemoryAppender.getFullLog();
        log = colourLog(log);
        Map params = new HashMap();
        params.put("log", log);
        return new ModelAndView("admin/logs", params);
    }

    @RequestMapping
    public ModelAndView conf(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("admin/conf", null);
    }

    @RequestMapping
    public ModelAndView spring(HttpServletRequest req, HttpServletResponse res) {
        return new ModelAndView("admin/spring", null);
    }

    @RequestMapping
    public ModelAndView regenerateLuceneIndexes(HttpServletRequest req, HttpServletResponse res) throws Exception {
        postBo.regenerateLuceneIndexes();
        logger.info("Regenerated the Lucene indexes");
        FlashHelper.setRedirectNotice(req, "RegeneratedLuceneIndex");
        return new ModelAndView("redirect:/admin/index.html");
    }

    @RequestMapping
    public String importFromTypo(HttpServletRequest req) throws SQLException, ClassNotFoundException, UnsupportedEncodingException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/jug_blog", "root", "root");
        typoImporter.importUsers(conn);
        typoImporter.importCategories(conn);
        typoImporter.importBlogPosts(conn);
        jdbcMethods.closeResource(conn);
        FlashHelper.setRedirectNotice(req, "TypoImported");
        return "redirect:/admin/index.html";
    }

    private String colourLog(String log) {
        String lines[];
        if (log == null) lines = new String[] { "" }; else lines = log.split("[\\n\\r]");
        for (int i = 0; i < lines.length; i++) {
            if (lines[i].indexOf("[ERROR]") != -1) lines[i] = "<span class=\"log_error\">" + lines[i] + "</span>";
            if (lines[i].indexOf("[WARN]") != -1) lines[i] = "<span class=\"log_warn\">" + lines[i] + "</span>";
            if (StringUtils.isNotBlank(lines[i])) lines[i] += "<br/>";
        }
        return StringUtils.join(lines);
    }
}
