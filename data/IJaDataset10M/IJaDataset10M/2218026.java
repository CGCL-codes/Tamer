package uipp.web.articles;

import com.flexive.shared.EJBLookup;
import com.flexive.shared.content.FxContent;
import com.flexive.shared.exceptions.FxApplicationException;
import uipp.ejb.Articles;
import uipp.ejb.IContentWithImagesBase;
import uipp.web.contentBeans.EditContentBeanWithImages;

/**
 * Bean for Articles edit forms
 * 
 * @author Jindrich Basek (basekjin@fit.cvut.cz, CTU Prague, FIT) */
public class EditArticleBean extends EditContentBeanWithImages {

    /** Creates a new instance of EditArticleBean */
    public EditArticleBean() {
        super();
    }

    @Override
    protected String getSuccessDeleteMessageKey() {
        return "deleteArticle.message.articleDeleted";
    }

    /**
     * Removes existing content instance action
     * 
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    @Override
    protected void removeAction() throws FxApplicationException {
        EJBLookup.getEngine(Articles.class).removeContent(pk);
    }

    /**
     * Save existing content action
     * 
     * @param editPermissions can user edit permission
     * @return modified content instance
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    @Override
    protected FxContent saveExistingAction(boolean editPermissions) throws FxApplicationException {
        return EJBLookup.getEngine(Articles.class).saveExistingContent(getEditContent(), usersRead, usersWrite, editPermissions);
    }

    /**
     * Save new content action
     * 
     * @return newly created content instance
     * @throws FxApplicationException error loading or processing data from data storage 
     */
    @Override
    protected FxContent saveAction() throws FxApplicationException {
        return EJBLookup.getEngine(Articles.class).saveContent(getContent());
    }

    @Override
    protected IContentWithImagesBase getContentBean() {
        return EJBLookup.getEngine(Articles.class);
    }
}
