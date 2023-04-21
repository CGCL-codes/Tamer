package at.suas.sepiaxweb.client.composite;

import at.suas.sepiaxweb.client.Entry;
import at.suas.sepiaxweb.client.ServiceFactory;
import at.suas.sepiaxweb.client.dialog.ErrorDialog;
import at.suas.sepiaxweb.client.dialog.StandardDialog;
import at.suas.sepiaxweb.client.service.RepoServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author u725599
 */
public class SbvrRepoSearchComposite extends Composite implements ClickListener {

    private HorizontalPanel entryReceiversPanel;

    RepoServiceAsync repoServ;

    Button searchButton;

    VerticalPanel resultsPanel;

    VerticalPanel detailsPanel;

    private TextBox queryTextbox;

    SbvrStatementManagerComposite manager;

    public SbvrRepoSearchComposite(SbvrStatementManagerComposite manager) {
        this.manager = manager;
        VerticalPanel mainPanel = new VerticalPanel();
        try {
            ServiceFactory factory = ServiceFactory.getInstance();
            repoServ = factory.createRepoService();
        } catch (ServiceFactory.ServiceFactoryException ex) {
        }
        queryTextbox = new TextBox();
        queryTextbox.setWidth("400px");
        searchButton = new Button("Search");
        searchButton.addClickListener(this);
        mainPanel.add(new HTML("<br>"));
        mainPanel.add(queryTextbox);
        mainPanel.add(searchButton);
        mainPanel.add(new HTML("<br>"));
        entryReceiversPanel = new HorizontalPanel();
        mainPanel.add(entryReceiversPanel);
        mainPanel.add(new HTML("<br>"));
        resultsPanel = new VerticalPanel();
        resultsPanel.setWidth("100%");
        resultsPanel.setStyleName("gwt-sbvr-detailPanel");
        mainPanel.add(resultsPanel);
        mainPanel.add(new HTML("<br>"));
        detailsPanel = new VerticalPanel();
        detailsPanel.setWidth("100%");
        detailsPanel.setStyleName("gwt-sbvr-detailPanel");
        detailsPanel.add(new Label("SBVR Details"));
        mainPanel.add(detailsPanel);
        mainPanel.add(new Label(""));
        initWidget(mainPanel);
    }

    public void setResultList(Entry[] searchResults) {
        System.out.println("Test setResultList");
        resultsPanel.clear();
        if (searchResults == null || searchResults.length == 0) {
            HTML noResults = new HTML("<font color=\"#FF0000\"> No results found! </font>");
            resultsPanel.add(noResults);
        } else {
            for (int i = 0; i < searchResults.length; i++) {
                EntryComposite e = new EntryComposite(detailsPanel, (Entry) searchResults[i]);
                e.addDeleteClickListener(this);
                resultsPanel.add(e);
                System.out.println("Adding EntryInterfaceComposite for: " + searchResults[i].getTitle());
            }
        }
    }

    public void onClick(Widget arg0) {
        if (arg0 instanceof Hyperlink) {
            Hyperlink h = (Hyperlink) arg0;
            if ("Delete".equals(h.getText())) {
                System.out.println(arg0.getParent().getParent().getClass().toString());
                resultsPanel.remove(arg0.getParent().getParent());
            }
        } else if (arg0 == searchButton) {
            detailsPanel.clear();
            if (repoServ == null) {
                new StandardDialog("Repo not available");
                return;
            }
            Entry[] searchResults;
            repoServ.searchData(queryTextbox.getText(), new AsyncCallback() {

                public void onFailure(Throwable arg0) {
                    new ErrorDialog("Error while searching for entries in repository", arg0).show();
                }

                public void onSuccess(Object arg0) {
                    Entry[] searchResults = (Entry[]) arg0;
                    setResultList(searchResults);
                }
            });
        } else {
            if (arg0 instanceof Button) {
            }
        }
    }
}
