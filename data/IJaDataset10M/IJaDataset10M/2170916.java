package uk.ac.ebi.intact.site.mb;

import uk.ac.ebi.intact.search.wsclient.SearchServiceClient;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * TODO comment this!
 *
 * @author Bruno Aranda (baranda@ebi.ac.uk)
 * @version $Id: SearchBean.java 8147 2007-04-17 14:13:37Z baranda $
 */
public class SearchBean implements Serializable {

    private static final String SEARCH_QUERY_URL = "uk.ac.ebi.intact.SEARCH_QUERY_URL";

    private String searchQuery;

    public SearchBean() {
    }

    public String doSearch() {
        FacesContext context = FacesContext.getCurrentInstance();
        String searchQueryUrl = context.getExternalContext().getInitParameter(SEARCH_QUERY_URL);
        try {
            context.responseComplete();
            context.getExternalContext().redirect(searchQueryUrl + searchQuery);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getSearchQuery() {
        return searchQuery;
    }

    public void setSearchQuery(String searchQuery) {
        this.searchQuery = searchQuery;
    }
}
