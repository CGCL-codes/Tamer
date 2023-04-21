package org.jcvi.vics.web.gwt.search.client.panel.accession;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import org.jcvi.vics.model.common.SortArgument;
import org.jcvi.vics.model.tasks.search.SearchTask;
import org.jcvi.vics.web.gwt.common.client.security.ClientSecurityUtils;
import org.jcvi.vics.web.gwt.common.client.ui.link.NotLoggedInLink;
import org.jcvi.vics.web.gwt.common.client.util.HtmlUtils;
import org.jcvi.vics.web.gwt.search.client.model.AccessionResult;
import org.jcvi.vics.web.gwt.search.client.panel.CategorySearchDataBuilder;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: smurphy
 * Date: Aug 29, 2007
 * Time: 10:54:25 AM
 */
public class AccessionSearchDataBuilder extends CategorySearchDataBuilder {

    private static final String DATA_PANEL_TITLE = "Matching Accession";

    private HorizontalPanel dataPanel;

    public AccessionSearchDataBuilder(String searchId, String searchQuery) {
        super(searchId, searchQuery);
    }

    protected String getPanelSearchCategory() {
        return SearchTask.TOPIC_ACCESSION;
    }

    public Panel createDataPanel() {
        dataPanel = new HorizontalPanel();
        return dataPanel;
    }

    public void populateDataPanel() {
        retrieveAccessionResult();
    }

    protected String createDataPanelTitle() {
        return DATA_PANEL_TITLE;
    }

    private void retrieveAccessionResult() {
        _searchService.getPagedCategoryResults(searchId, getPanelSearchCategory(), 0, 1, new SortArgument[0], new AsyncCallback() {

            public void onFailure(Throwable caught) {
                HTML errorHTML = HtmlUtils.getHtml("Failed to retrieve accession search result", "error");
                dataPanel.add(errorHTML);
            }

            public void onSuccess(Object result) {
                List resultList = (List) result;
                if (resultList != null && resultList.size() > 0) {
                    AccessionResult accResult = (AccessionResult) resultList.get(0);
                    String accessiontText = "Your search matched " + accResult.getAccessionType() + " Accession ";
                    HTML accessionHTMLText = HtmlUtils.getHtml(accessiontText, "text");
                    Widget accessionLink;
                    if (accResult.getAccessionType().equals("Project") || accResult.getAccessionType().equals("Publication") || ClientSecurityUtils.isAuthenticated()) {
                        accessionLink = getAccessionLink(accResult.getDescription(), accResult.getAccession());
                    } else {
                        accessionLink = new NotLoggedInLink(accResult.getDescription());
                    }
                    dataPanel.add(accessionHTMLText);
                    dataPanel.add(HtmlUtils.getHtml("&nbsp;", "text"));
                    dataPanel.add(accessionLink);
                }
            }
        });
    }
}
