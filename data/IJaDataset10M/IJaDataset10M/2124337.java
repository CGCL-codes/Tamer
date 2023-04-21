package uipp.web.articles;

import com.flexive.shared.EJBLookup;
import com.flexive.shared.FxContext;
import com.flexive.shared.exceptions.FxApplicationException;
import uipp.ejb.Articles;
import uipp.web.contentBeans.ContentBean;

/**
 * Bean for form with Articles table
 * 
 * @author Jindrich Basek (basekjin@fit.cvut.cz, CTU Prague, FIT) */
public class ArticlesBean extends ContentBean {

    public static final String CATEGORIES_NAME = "ArticlesCategory";

    public ArticlesBean() {
        super();
    }

    @Override
    protected void loadTotalRows() throws FxApplicationException {
        totalRows = EJBLookup.getEngine(Articles.class).getContentCount(selectedCategories);
    }

    @Override
    protected void loadData() throws FxApplicationException {
        data = EJBLookup.getEngine(Articles.class).getContent(FxContext.getUserTicket(), firstRow, rowsPerPage, sortField, sortAscending, selectedCategories);
    }

    @Override
    protected String loadCategoriesName() {
        return CATEGORIES_NAME;
    }
}
