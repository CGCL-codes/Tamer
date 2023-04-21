package com.kwoksys.action.kb;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.FooterTemplate;
import com.kwoksys.action.common.template.HeaderTemplate;
import com.kwoksys.action.common.template.RootTemplate;
import com.kwoksys.action.common.template.StandardTemplate;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.admin.AdminUtils;
import com.kwoksys.biz.admin.dto.AccessUser;
import com.kwoksys.biz.files.FileService;
import com.kwoksys.biz.files.FileUtils;
import com.kwoksys.biz.files.dao.FileQueries;
import com.kwoksys.biz.files.dto.File;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.biz.system.dto.Link;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.configs.ConfigManager;
import com.kwoksys.framework.connection.database.QueryBits;
import com.kwoksys.framework.system.Access;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import com.kwoksys.framework.util.WidgetUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Action class for showing Article detail.
 */
public class ArticleDetailAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        AccessUser user = requestContext.getUser();
        Integer articleId = HttpUtils.getParameter(request, "articleId");
        String articleWikiNamespace = HttpUtils.getParameterString(request, "title");
        KbService kbService = ServiceProvider.getKbService();
        FileService fileService = ServiceProvider.getFileService();
        Article article = (articleId != 0) ? kbService.getArticle(articleId) : kbService.getArticle(articleWikiNamespace);
        kbService.updateArticleViewCount(article);
        Category category = kbService.getCategory(article.getCategoryId());
        QueryBits query = new QueryBits();
        query.setOrderByColumn(FileQueries.getOrderByColumn(File.NAME));
        List<File> files = kbService.getArticleFiles(query, article.getId());
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        if (!files.isEmpty()) {
            boolean canDownloadFile = Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_DOWNLOAD);
            List formattedFiles = new ArrayList();
            for (File file : files) {
                Map map = new HashMap();
                map.put("file", file);
                map.put("fileName", WidgetUtils.getFileIconLink(canDownloadFile, file.getLogicalName(), AppPaths.ROOT + AppPaths.KB_ARTICLE_FILE_DOWNLOAD + "?articleId=" + articleId + "&fileId=" + file.getId()));
                map.put("filesize", FileUtils.formatFileSize(requestContext, file.getSize()));
                formattedFiles.add(map);
            }
            standardTemplate.setAttribute("files", formattedFiles);
        }
        standardTemplate.setAttribute("article", article);
        standardTemplate.setAttribute("articleText", KbUtils.formatContent(article));
        standardTemplate.setAttribute("articleCreator", AdminUtils.getSystemUsername(requestContext, article.getCreator()));
        standardTemplate.setAttribute("canDeleteFile", Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_DELETE));
        standardTemplate.setAttribute("deleteFilePath", AppPaths.ROOT + AppPaths.KB_ARTICLE_FILE_DELETE + "?articleId=" + articleId + "&fileId=");
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_EDIT)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.KB_ARTICLE_EDIT + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articleEdit");
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_PRINT)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.KB_ARTICLE_PRINT + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articlePrint");
            link.setImgSrc(AppPaths.ROOT + AppPaths.PRINT_ICON);
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_FILE_ADD)) {
            Link link = new Link();
            link.setTitleKey("files.fileAttach");
            if (fileService.isDirectoryExist(ConfigManager.file.getKbFileRepositoryLocation())) {
                link.setPath(AppPaths.ROOT + AppPaths.KB_ARTICLE_FILE_ADD + "?articleId=" + articleId);
                link.setImgSrc(AppPaths.ROOT + AppPaths.FILE_ADD_ICON);
            } else {
                link.setImgAlt("files.warning.invalidPath");
                link.setImgSrc(AppPaths.ROOT + AppPaths.WARNING_ICON);
            }
            header.addHeaderCmds(link);
        }
        if (Access.hasPermission(user, AppPaths.KB_ARTICLE_DELETE)) {
            Link link = new Link();
            link.setPath(AppPaths.ROOT + AppPaths.KB_ARTICLE_DELETE + "?articleId=" + article.getId());
            link.setTitleKey("kb.cmd.articleDelete");
            header.addHeaderCmds(link);
        }
        header.setNavCmds(KbUtils.generatePath(category));
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
