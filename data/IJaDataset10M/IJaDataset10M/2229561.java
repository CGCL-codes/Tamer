package org.jcvi.vics.web.gwt.search.client.panel.website;

import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import org.jcvi.vics.web.gwt.common.client.Constants;
import org.jcvi.vics.web.gwt.common.client.service.log.Logger;
import org.jcvi.vics.web.gwt.googlesearch.client.GoogleSearchHandler;
import org.jcvi.vics.web.gwt.search.client.model.SearchResultsData;
import org.jcvi.vics.web.gwt.search.client.panel.SearchResultsPanel;
import org.jcvi.vics.web.gwt.search.client.panel.iconpanel.SearchIconPanel;
import org.jcvi.vics.web.gwt.search.client.panel.iconpanel.SearchIconPanelFactory;
import org.jcvi.vics.web.gwt.search.client.panel.iconpanel.SearchResultsIconPanelFactory;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Aug 29, 2007
 * Time: 10:54:25 AM
 */
public class WebsiteSearchResultsPanelBasedOnGoogle extends SearchResultsPanel {

    private static Logger _logger = Logger.getLogger("org.jcvi.vics.web.gwt.search.client.panel.WebsiteSearchResultsPanelBasedOnGoogle");

    public WebsiteSearchResultsPanelBasedOnGoogle(String title, String searchId, String category) {
        super(title, searchId, category);
    }

    public void populatePanel(SearchResultsData searchResult) {
        createResultsPanel(searchResult);
    }

    private void createResultsPanel(SearchResultsData searchResult) {
        HorizontalPanel topPanel = new HorizontalPanel();
        topPanel.setStyleName("SiteSearchInputPanel");
        add(topPanel);
        SearchIconPanelFactory iconFactory = new SearchResultsIconPanelFactory();
        SearchIconPanel iconPanel = iconFactory.createSearchIconPanel(getCategory(), true);
        topPanel.add(iconPanel);
        HorizontalPanel googleInputPanel = new HorizontalPanel();
        googleInputPanel.setWidth("100%");
        topPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        topPanel.add(googleInputPanel);
        VerticalPanel googleResultsPanel = new VerticalPanel();
        googleResultsPanel.setStyleName("SiteSearchResultPanel");
        add(googleResultsPanel);
        GoogleSearchHandler googleSearch = new GoogleSearchHandler(googleResultsPanel);
        googleSearch.setInputPanel(googleInputPanel);
        googleSearch.addSearchableDomains("V.I.C.S.", Constants.VICSWEB_DOMAIN);
        googleSearch.setResultSetSize(GoogleSearchHandler.LARGERESULTSET);
        googleSearch.executeSearch(searchResult.getSearchId(), searchResult.getSearchString(), GoogleSearchHandler.WEBSEARCH);
    }
}
