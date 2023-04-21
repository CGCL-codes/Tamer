package benchmarkeditor.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import javax.swing.AbstractAction;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;
import benchmarkeditor.core.BenchmarkElement;
import benchmarkeditor.core.SearchDescription;

/**
 * This class represents the display of the search results after a search operation
 * @author CKATZ
 *
 */
public class SearchResultsView extends JPanel {

    /**
	 * If <code>false</code>, then refined search results will
	 * replace the tab of the refined search; otherwise, every search
	 * result is displayed in a new tab, including refinement of
	 * previous searches.
	 */
    public static final boolean ADD_NEW_TAB_ON_REFINE = true;

    private JTabbedPaneWithCloseIcons _tabbedSearches;

    private ArrayList<SearchDescription> _searchResults;

    private JPanel _resultPane;

    private JLabel _searchTime;

    private JTextField _searchTarget;

    private BenchmarkElementList _searchList;

    private JButton _refreshButton;

    private JButton _refineButton;

    private int _searchCount = 0;

    /**
	 * Extension of {@link JTabbedPaneWithCloseIcons} that adds a Close
	 * button in the tab area "gutter", switches among search results
	 * when tabs are selected, and closes the search results view when
	 * the last tab is closed.
	 *
	 * @author GWILLIAM
	 */
    private class SearchResultsTabbedPane extends JTabbedPaneWithCloseIcons {

        /**
		 * Closes the search results view tabbed pane
		 */
        public void close() {
            if (_searchResults != null) {
                _searchResults.clear();
                _searchResults = null;
            }
            setLayout(((SearchResultsTabbedPane.CloseButtonLayout) getLayout()).layout);
            MainWindow.getTreeViewPanel().closeSearchResults();
        }

        private CloseButton _closeButton;

        @Override
        public void closeTab(int index) {
            if (index < _searchResults.size()) {
                _searchResults.remove(index);
            }
            if (_searchResults.isEmpty()) {
                close();
            } else {
                super.closeTab(index);
                setSelectedIndex(Math.min(index, getTabCount() - 1));
            }
        }

        @Override
        public void setSelectedIndex(int index) {
            ((Container) getComponentAt(index)).removeAll();
            ((Container) getComponentAt(index)).add(_resultPane, BorderLayout.CENTER);
            refreshResultPane(_searchResults.get(index));
            super.setSelectedIndex(index);
            repaint();
            _searchList.requestFocusInWindow();
        }

        @Override
        public void updateUI() {
            if (_closeButton == null) {
                _closeButton = new CloseButton();
                add(_closeButton);
            }
            Insets oldInsets = UIManager.getInsets("TabbedPane.tabAreaInsets");
            Insets tabInsets = (Insets) oldInsets.clone();
            Insets insets = getInsets();
            tabInsets.right += _closeButton.getPreferredSize().width + insets.right + insets.left;
            UIManager.getDefaults().putDefaults(new Object[] { "TabbedPane.tabAreaInsets", tabInsets });
            super.updateUI();
            setLayout(new CloseButtonLayout(getLayout()));
            UIManager.getDefaults().putDefaults(new Object[] { "TabbedPane.tabAreaInsets", oldInsets });
        }

        class CloseButtonLayout implements LayoutManager {

            private TabbedPaneLayout layout;

            public CloseButtonLayout(LayoutManager layout) {
                this.layout = (TabbedPaneLayout) layout;
            }

            @Override
            public void addLayoutComponent(String name, Component comp) {
                layout.addLayoutComponent(name, comp);
            }

            @Override
            public void layoutContainer(Container parent) {
                layout.layoutContainer(parent);
                if (_closeButton != null) {
                    Insets insets = getInsets();
                    Dimension size = _closeButton.getPreferredSize();
                    _closeButton.setBounds(parent.getWidth() - size.width - insets.right - 1, insets.top, size.width, size.height);
                }
            }

            @Override
            public Dimension minimumLayoutSize(Container parent) {
                return layout.minimumLayoutSize(parent);
            }

            @Override
            public Dimension preferredLayoutSize(Container parent) {
                return layout.preferredLayoutSize(parent);
            }

            @Override
            public void removeLayoutComponent(Component comp) {
                layout.removeLayoutComponent(comp);
            }
        }

        class CloseButton extends JButton implements UIResource {

            public CloseButton() {
                super(new AbstractAction("Close") {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        close();
                    }
                });
                setToolTipText("Click to clear all search results and close this view");
            }
        }
    }

    private class RefreshAction extends AbstractAction {

        public RefreshAction() {
            super("Refresh Search");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = _tabbedSearches.getSelectedIndex();
            SearchDescription search = _searchResults.get(index);
            search.execute_search();
            refreshResultPane(search);
        }
    }

    private class RefineAction extends AbstractAction {

        public RefineAction() {
            super("Refine Search");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int index = _tabbedSearches.getSelectedIndex();
            SearchDescription search = _searchResults.get(index);
            AdvancedSearchWindow.createAndShowAdvancedSearchPane(search.clone());
        }
    }

    /**
	 * Constructor for the search results view class
	 * @param results The results of the search conducted
	 */
    public SearchResultsView() {
        this.setLayout(new BorderLayout());
        _tabbedSearches = new SearchResultsTabbedPane();
        JLabel searchTime = new JLabel("Search Time:");
        _searchTime = new JLabel();
        JLabel searchTarget = new JLabel("Search Target:");
        _searchTarget = new JTextField();
        _searchTarget.setEditable(false);
        JLabel searchListLabel = new JLabel("Search Results:");
        _searchList = new BenchmarkElementList();
        JScrollPane searchList = new JScrollPane(_searchList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        RefreshAction refreshAction = new RefreshAction();
        _refreshButton = new JButton(refreshAction);
        RefineAction refineAction = new RefineAction();
        _refineButton = new JButton(refineAction);
        _resultPane = new JPanel();
        GroupLayout g = new GroupLayout(_resultPane);
        _resultPane.setLayout(g);
        g.setAutoCreateContainerGaps(true);
        g.setAutoCreateGaps(true);
        g.setHorizontalGroup(g.createParallelGroup().addGroup(g.createSequentialGroup().addComponent(searchTarget).addComponent(_searchTarget)).addGroup(g.createSequentialGroup().addComponent(searchTime).addComponent(_searchTime, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(ComponentPlacement.UNRELATED).addGap(0, 10000, 10000).addComponent(_refreshButton)).addGroup(g.createParallelGroup().addGroup(g.createSequentialGroup().addComponent(searchListLabel).addPreferredGap(ComponentPlacement.UNRELATED).addGap(0, 10000, 10000).addComponent(_refineButton)).addComponent(searchList)));
        g.setVerticalGroup(g.createSequentialGroup().addGroup(g.createBaselineGroup(true, false).addComponent(searchTarget).addComponent(_searchTarget)).addGroup(g.createBaselineGroup(true, false).addComponent(searchTime).addComponent(_searchTime).addComponent(_refreshButton)).addGroup(g.createSequentialGroup().addGroup(g.createBaselineGroup(true, false).addComponent(searchListLabel).addComponent(_refineButton)).addComponent(searchList)));
        _searchResults = new ArrayList<SearchDescription>();
        this.add(_tabbedSearches);
    }

    public void addSearch(SearchDescription sd) {
        int index = ADD_NEW_TAB_ON_REFINE ? -1 : _searchResults.indexOf(sd);
        if (index == -1) {
            index = _searchResults.size();
            _searchResults.add(sd);
            _tabbedSearches.addTab("Search " + (++_searchCount), new JPanel(new BorderLayout()));
        }
        _tabbedSearches.setSelectedIndex(_tabbedSearches.getTabCount() - 1);
    }

    private void refreshResultPane(SearchDescription search) {
        _searchTarget.setText(search.getSearchTarget());
        _searchTarget.setCaretPosition(0);
        _searchTime.setText(SimpleDateFormat.getDateTimeInstance().format(search.getLastSearchTime().getTime()));
        Collection<BenchmarkElement> results = search.getLastResult();
        _searchList.setElements(results);
    }
}
