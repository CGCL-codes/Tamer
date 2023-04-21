package com.kwoksys.action.kb;

import com.kwoksys.action.base.BaseAction;
import com.kwoksys.action.common.template.*;
import com.kwoksys.biz.ServiceProvider;
import com.kwoksys.biz.kb.KbService;
import com.kwoksys.biz.kb.KbUtils;
import com.kwoksys.biz.kb.dto.Article;
import com.kwoksys.biz.system.dto.Category;
import com.kwoksys.framework.system.AppPaths;
import com.kwoksys.framework.system.RequestContext;
import com.kwoksys.framework.util.HttpUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Action class for deleting Article.
 */
public class ArticleDeleteAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        RequestContext requestContext = new RequestContext(request);
        Integer articleId = HttpUtils.getParameter(request, "articleId");
        KbService kbService = ServiceProvider.getKbService();
        Article article = kbService.getArticle(articleId);
        Category category = kbService.getCategory(article.getCategoryId());
        StandardTemplate standardTemplate = new StandardTemplate(requestContext);
        standardTemplate.setTemplatePath(mapping, SUCCESS);
        ObjectDeleteTemplate delete = new ObjectDeleteTemplate();
        standardTemplate.addTemplate(delete);
        delete.setFormAction(AppPaths.ROOT + AppPaths.KB_ARTICLE_DELETE_2 + "?articleId=" + articleId);
        delete.setFormCancelAction(AppPaths.KB_ARTICLE_DETAIL + "?articleId=" + articleId);
        delete.setConfirmationMsgKey("kb.articleDelete.confirm");
        delete.setSubmitButtonKey("kb.articleDelete.submitButton");
        ArticleSpecTemplate articleTemplate = new ArticleSpecTemplate(article);
        standardTemplate.addTemplate(articleTemplate);
        HeaderTemplate header = standardTemplate.getHeaderTemplate();
        header.setNavCmds(KbUtils.generatePath(category));
        return standardTemplate.findForward(STANDARD_TEMPLATE);
    }
}
