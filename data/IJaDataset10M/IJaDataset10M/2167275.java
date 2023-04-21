package net.spatula.tally_ho.ui.article.common;

import net.spatula.tally_ho.service.beans.ArticleBean;
import net.spatula.tally_ho.ui.AuthenticatedWebPage;
import net.spatula.tally_ho.ui.article.read.ArticleDisplayPanel;
import net.spatula.tally_ho.ui.signin.Utils;
import wicket.markup.html.basic.Label;
import wicket.model.PropertyModel;

/**
 * @author spatula
 * 
 */
public class PreviewBeforeSubmit extends AuthenticatedWebPage {

    private static final long serialVersionUID = 1L;

    private ArticleBean article = new ArticleBean();

    public PreviewBeforeSubmit(Class responseClass) {
        super();
        article = ((ArticleSession) getSession()).getArticle();
        article.setCreator(Utils.getUserBean());
        add(new Label("section", new PropertyModel(article.getSection(), "name")));
        add(new Label("headline", new PropertyModel(article, "headline")));
        add(new PreviewBeforeSubmitForm("articleForm", responseClass));
        add(new ArticleDisplayPanel("article", article, null));
    }
}
