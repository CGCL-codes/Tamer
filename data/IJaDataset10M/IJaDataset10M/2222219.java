package net.spatula.tally_ho.ui.article.update;

import net.spatula.tally_ho.service.ArticleService;
import net.spatula.tally_ho.service.beans.ArticleBean;
import net.spatula.tally_ho.ui.AuthenticatedWebPage;
import net.spatula.tally_ho.ui.article.common.ArticleFormPanel;
import net.spatula.tally_ho.ui.article.common.ArticleSession;
import net.spatula.tally_ho.ui.signin.Utils;
import wicket.PageParameters;
import wicket.markup.html.basic.Label;
import wicket.markup.html.panel.FeedbackPanel;
import wicket.util.string.StringValueConversionException;

public class ArticleUpdate extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    public ArticleUpdate(PageParameters params) {
        super(params);
        long id;
        try {
            id = params.getLong("id");
        } catch (StringValueConversionException e) {
            id = 0;
        }
        ArticleBean article = ArticleService.getInstance().getArticleById(id);
        ArticleSession session = (ArticleSession) getSession();
        session.setArticle(article);
        add(new ArticleFormPanel("articleForm", UpdateArticle.class));
        add(new Label("hello", "Welcome back, " + Utils.getUserBean().getUsername() + "!"));
    }
}
