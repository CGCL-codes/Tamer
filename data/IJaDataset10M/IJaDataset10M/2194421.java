package ch.netcetera.wikisearch.swing;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import ch.netcetera.wikisearch.Constants;
import ch.netcetera.wikisearch.search.SearcherFactory;

/**
 * Swing search Component. Searches are triggered in here. 
 * Note that this uses the SwingWorker class to perform background-searches.
 */
public class SearchComponent extends JPanel implements ActionListener {

    private JTextField searchInput = null;

    private JLabel errorLabel = null;

    private ResultContainer resultContainer = null;

    private String searchString = null;

    private String[] searcherIDs = null;

    /**
	 * Creates new Search component.
	 * 
	 * @param aResultListener the one that want's to know about the results
	 * @param searcherID the searcher to use
	 */
    public SearchComponent(ResultContainer aResultContainer) {
        super(new GridLayout(2, 2));
        this.resultContainer = aResultContainer;
        this.searcherIDs = SearcherFactory.getSearcherIDs();
        createGUI();
    }

    private void createGUI() {
        Constants.basicComponentInit(this, "Search Input");
        searchInput = new JTextField();
        searchInput.setFocusAccelerator(Constants.SEARCH_HOTKEY);
        errorLabel = new JLabel();
        add(new JLabel("Search for: "));
        add(searchInput);
        add(new JLabel("Hit [Alt-" + Constants.SEARCH_HOTKEY + "] for Quicksearch."));
        add(errorLabel);
        searchInput.addActionListener(this);
        Dimension searchInputDimension = searchInput.getPreferredSize();
        searchInputDimension.setSize(100, searchInputDimension.getHeight());
        searchInput.setPreferredSize(searchInputDimension);
    }

    /**
	 * React to user's search command. This will set-up a background-
	 * thread for searching.
	 */
    public void actionPerformed(ActionEvent evt) {
        searchString = searchInput.getText();
        Logger logger = Logger.getLogger("ch.netcetera.wikisearch.swing");
        logger.fine("SearchComponent.actionPerformed: searchString=" + searchString);
        if (null == searchString || "".equals(searchString)) {
            errorLabel.setText("Please enter keyword.");
        } else {
            for (int i = 0; i < searcherIDs.length; i++) {
                String searcherID = searcherIDs[i];
                SearchWorker worker = new SearchWorker(SearcherFactory.newSearcher(searcherID), searchString, resultContainer.getResultListener(searcherID));
                worker.start();
            }
        }
    }
}
