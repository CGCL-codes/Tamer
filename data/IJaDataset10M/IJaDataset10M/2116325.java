package savenews.backend.bo;

import savenews.backend.dao.ArticleDAO;
import savenews.backend.exceptions.FileAlreadyExistsException;
import savenews.backend.to.Article;
import savenews.backend.to.Origin;

/**
 * Article business object: Manages articles operations
 * @author Eduardo Ferreira
 */
public class ArticleBO {

    /** Singleton instance */
    private static ArticleBO instance = new ArticleBO();

    ArticleDAO articleDAO;

    /**
	 * Private constructor to avoid instantiation
	 */
    private ArticleBO() {
        articleDAO = ArticleDAO.getInstance();
    }

    /**
	 * @return Singleton instance
	 */
    public static ArticleBO getInstance() {
        return instance;
    }

    /**
	 * Send an article to processing
	 */
    public void export(Article article, boolean overwriteFile) throws FileAlreadyExistsException {
        Origin origin = OriginsBO.getInstance().findOrigin(article.getOriginDescription());
        article.setOrigin(origin);
        articleDAO.export(article, overwriteFile);
    }
}
