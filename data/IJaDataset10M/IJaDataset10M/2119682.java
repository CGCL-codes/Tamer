package org.columba.core.gui.search;

import java.awt.BorderLayout;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import org.columba.api.gui.frame.IDock;
import org.columba.api.gui.frame.IFrameMediator;
import org.columba.core.gui.search.api.IResultPanel;
import org.columba.core.gui.search.api.ISearchPanel;
import org.columba.core.main.MainInterface;
import org.columba.core.resourceloader.IconKeys;
import org.columba.core.resourceloader.ImageLoader;
import org.columba.core.search.api.ISearchCriteria;
import org.columba.core.search.api.ISearchManager;
import org.columba.core.search.api.ISearchProvider;

public class SearchPanel extends JPanel implements ISearchPanel {

    private static final Logger LOG = Logger.getLogger("org.columba.core.search.gui.SearchPanel");

    private IFrameMediator frameMediator;

    private StackedBox box;

    private IconTextField textField;

    private ImageIcon icon = ImageLoader.getSmallIcon(IconKeys.EDIT_FIND);

    private JButton button;

    private Hashtable<String, ResultBox> map = new Hashtable<String, ResultBox>();

    private SearchBar searchBar;

    public SearchPanel(IFrameMediator frameMediator) {
        super();
        this.frameMediator = frameMediator;
        searchBar = new SearchBar(this, true);
        setLayout(new BorderLayout());
        JPanel top = new JPanel();
        top.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        top.setLayout(new BorderLayout());
        top.add(searchBar, BorderLayout.CENTER);
        add(top, BorderLayout.NORTH);
        JPanel center = new JPanel();
        center.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
        center.setLayout(new BorderLayout());
        box = new StackedBox();
        box.setBackground(UIManager.getColor("TextField.background"));
        JScrollPane pane = new JScrollPane(box);
        center.add(pane, BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
    }

    public void searchInCriteria(String searchTerm, String providerName, String criteriaName) {
        showSearchDockingView();
        ISearchManager manager = MainInterface.searchManager;
        manager.clearSearch(searchTerm);
        manager.reset();
        createStackedBox(searchTerm, providerName, criteriaName);
        manager.executeSearch(searchTerm, providerName, criteriaName, 0, 5);
    }

    public void searchInProvider(String searchTerm, String providerName) {
        showSearchDockingView();
        ISearchManager manager = MainInterface.searchManager;
        manager.clearSearch(searchTerm);
        manager.reset();
        createStackedBox(searchTerm, providerName, null);
        manager.executeSearch(searchTerm, providerName, 0, 5);
    }

    public void searchAll(String searchTerm) {
        showSearchDockingView();
        ISearchManager manager = MainInterface.searchManager;
        manager.clearSearch(searchTerm);
        manager.reset();
        createStackedBox(searchTerm, null, null);
        manager.executeSearch(searchTerm, 0, 5);
    }

    private void createStackedBox(String searchTerm, String providerName, String criteriaName) {
        if (searchTerm == null) throw new IllegalArgumentException("searchTerm == null");
        box.removeAll();
        boolean providerAll = (providerName == null) ? true : false;
        boolean providerSearch = (providerName != null) ? true : false;
        boolean criteriaSearch = (criteriaName != null && providerName != null) ? true : false;
        ISearchManager manager = MainInterface.searchManager;
        if (criteriaSearch) {
            ISearchProvider p = manager.getProvider(providerName);
            ISearchCriteria c = p.getCriteria(criteriaName, searchTerm);
            createResultPanel(p, c);
        } else if (providerSearch) {
            ISearchProvider p = manager.getProvider(providerName);
            Iterator<ISearchCriteria> it2 = p.getAllCriteria(searchTerm).iterator();
            while (it2.hasNext()) {
                ISearchCriteria c = it2.next();
                createResultPanel(p, c);
            }
        } else if (providerAll) {
            Iterator<ISearchProvider> it = manager.getAllProviders().iterator();
            while (it.hasNext()) {
                ISearchProvider p = it.next();
                if (p == null) continue;
                Iterator<ISearchCriteria> it2 = p.getAllCriteria(searchTerm).iterator();
                while (it2.hasNext()) {
                    ISearchCriteria c = it2.next();
                    createResultPanel(p, c);
                }
            }
        }
        validate();
        repaint();
    }

    private void createResultPanel(ISearchProvider p, ISearchCriteria c) {
        IResultPanel resultPanel = p.getResultPanel(c.getTechnicalName());
        if (resultPanel == null) resultPanel = new GenericResultPanel(p.getTechnicalName(), c.getTechnicalName());
        MainInterface.searchManager.addResultListener(resultPanel);
        ResultBox resultBox = new ResultBox(c, resultPanel);
        MainInterface.searchManager.addResultListener(resultBox);
        box.add(resultBox);
    }

    private void showSearchDockingView() {
        if (frameMediator instanceof IDock) {
            ((IDock) frameMediator).showDockable(IDock.DOCKING_VIEW_SEARCH);
        }
    }

    /**
	 * @see org.columba.core.gui.search.api.ISearchPanel#getView()
	 */
    public JComponent getView() {
        return this;
    }
}
