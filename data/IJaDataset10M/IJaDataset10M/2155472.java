package bz.ziro.kanbe.logic;

import java.io.StringWriter;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import com.google.appengine.api.datastore.Key;
import bz.ziro.kanbe.bean.FileData;
import bz.ziro.kanbe.bean.ListData;
import bz.ziro.kanbe.bean.PageData;
import bz.ziro.kanbe.bean.ParamData;
import bz.ziro.kanbe.bean.SiteData;
import bz.ziro.kanbe.bean.TextData;
import bz.ziro.kanbe.dao.PageDao;
import bz.ziro.kanbe.dao.SiteDao;
import bz.ziro.kanbe.model.Page;
import bz.ziro.kanbe.model.Site;
import bz.ziro.kanbe.model.TemplateBrowser;

/**
 * 表示ページ作成ロジック
 * @author Administrator
 */
public class HtmlLogic {

    private static final Logger logger = Logger.getLogger(HtmlLogic.class.getName());

    /**
     * サイトのデータを作成する
     * @param site
     * @param page
     * @return
     */
    private static VelocityContext createContext(Site site, Page page, ParamData param) {
        VelocityContext context = new VelocityContext();
        context.put("site", new SiteData(site));
        PageData pageData = new PageData(page, param);
        context.put("page", pageData);
        TextData textData = new TextData();
        context.put("text", textData);
        FileData fileData = new FileData();
        context.put("file", fileData);
        context.put("list", new ListData());
        return context;
    }

    /**
     * サイト作成ロジック
     * @param pageNum 
     * @param keyBuf 指定したページID
     * @param testFlag テスト表示有無
     * @param request リクエスト
     * @param writer HTML(out)
     * @return フォワード先
     */
    public static String createHtml(String siteKey, ParamData param, HttpServletRequest request, StringWriter writer) {
        TemplateLogic.init();
        FirstLogic.insertFirstSiteData();
        Site site = null;
        if (siteKey == null) {
            site = SiteDao.findPublish();
        } else {
            site = SiteDao.find(Long.valueOf(siteKey));
        }
        if (site == null) {
            return "privateSite.jsp";
        }
        Long pageKey = param.getId();
        Page page = null;
        if (pageKey == null) {
            page = PageDao.findRootPage(site);
        } else {
            page = PageDao.find(site.getKey().getId(), pageKey);
        }
        VelocityContext context = HtmlLogic.createContext(site, page, param);
        Key siteTemplateKey = page.getSiteTemplateKey();
        Key pageTemplateKey = page.getPageTemplateKey();
        TemplateBrowser siteBrow = BrowserLogic.findAgentBrowser(siteTemplateKey, request);
        if (siteBrow == null) {
            logger.warning("サイトブラウザ指定がないよー");
            return "errorSite.jsp";
        }
        String templateData = siteBrow.getTemplateData();
        int templateIdx = templateData.indexOf("${pageTemplate}");
        if (templateIdx != -1) {
            TemplateBrowser pageBrow = BrowserLogic.findAgentBrowser(pageTemplateKey, request);
            if (pageBrow == null) {
                logger.warning("ページブラウザ指定がないよー");
                return "errorSite.jsp";
            }
            String mae = templateData.substring(0, templateIdx - 1);
            String usiro = templateData.substring(templateIdx + 15);
            templateData = mae + pageBrow.getTemplateData() + usiro;
        }
        try {
            Velocity.evaluate(context, writer, "kanbeSite", templateData);
        } catch (Exception ex) {
            logger.severe(ex.toString());
        }
        String forward = "index.jsp";
        if (siteKey != null) {
            forward = "test.jsp";
        }
        return forward;
    }

    /**
	 * ドメインを取得
	 * @param request
	 * @return
	 */
    public static String getDomain(HttpServletRequest request) {
        StringBuffer urlBuf = request.getRequestURL();
        String uri = request.getRequestURI();
        String url = urlBuf.toString();
        int idx = url.indexOf(uri);
        url = url.substring(0, idx);
        return url;
    }
}
