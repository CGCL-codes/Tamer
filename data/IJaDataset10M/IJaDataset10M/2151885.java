package org.jazzteam.snipple.action.article;

import org.jazzteam.snipple.action.Action;
import org.jazzteam.snipple.data.ArticleDataService;

public class RemoveArticleAction extends Action {

    private ArticleDataService articleDataService;

    private Long id;

    public RemoveArticleAction(ArticleDataService articleDataService) {
        this.articleDataService = articleDataService;
    }

    public String index() {
        articleDataService.remove(id);
        return SUCCESS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
