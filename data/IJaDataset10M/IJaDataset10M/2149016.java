package edu.umn.cs5115.scheduler.search;

import edu.umn.cs5115.scheduler.SchedulerDocument;
import edu.umn.cs5115.scheduler.framework.WindowController;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

/**
 * Window controller for the search/search results windows.
 * @author grant
 */
public class SearchWindowController extends WindowController {

    /** Created the search interface */
    private SearchDialog searchInterface;

    /** Actual interface created by the SearchDialog class. */
    private Composite searchPanel;

    /** Created the search results interface */
    private SearchResultsDisplay searchResultsDisplay;

    /** Actual interface created by the SearchResultsDisplay. */
    private Composite resultsPanel;

    /** Creates a new instance of SearchWindowController */
    public SearchWindowController(SchedulerDocument document) {
        super(document);
        GridLayout layout = new GridLayout();
        layout.marginHeight = 0;
        layout.marginWidth = 0;
        Shell window = new Shell();
        window.setLayout(layout);
        setWindow(window);
        showSearchInterface();
        window.pack();
        window.addListener(SWT.Resize, new Listener() {

            public void handleEvent(Event event) {
                relayoutContent();
            }
        });
    }

    /** 
     * Window controller checks the return value of this method to get the
     * window's title.
     * @return The string "Search -- <document name>"
     */
    public String getWindowTitle() {
        return "Search — " + getDocument().getDisplayName();
    }

    public void showSearchResults(SearchResults results, boolean showBackButton) {
        if (searchPanel != null) {
            searchPanel.setVisible(false);
            ((GridData) searchPanel.getLayoutData()).exclude = true;
        }
        searchResultsDisplay = new SearchResultsDisplay(this, results, showBackButton);
        resultsPanel = searchResultsDisplay.createSearchResultsInterface();
        resultsPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        relayoutContent();
    }

    public void showSearchInterface() {
        if (resultsPanel != null) {
            resultsPanel.setVisible(false);
            ((GridData) resultsPanel.getLayoutData()).exclude = true;
        }
        if (searchPanel == null) {
            searchInterface = new SearchDialog(this);
            searchPanel = searchInterface.createPanel();
            searchPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        }
        searchPanel.setVisible(true);
        ((GridData) searchPanel.getLayoutData()).exclude = false;
        relayoutContent();
    }

    /**
     * Stretches panels to the size of the window.
     * Updates the layout of the window (.layout(true));
     */
    private void relayoutContent() {
        this.getWindow().layout(true);
    }
}
