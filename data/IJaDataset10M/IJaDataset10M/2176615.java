package org.leo.oglexplorer.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.axis2.AxisFault;
import org.leo.oglexplorer.model.SearchListener;
import org.leo.oglexplorer.model.SearchProvider;
import org.leo.oglexplorer.model.bookmarks.Bookmarks;
import org.leo.oglexplorer.model.engine.SearchEngine;
import org.leo.oglexplorer.model.engine.impl.BingSearchEngine;
import org.leo.oglexplorer.model.engine.impl.FileSystemSearchEngine;
import org.leo.oglexplorer.model.engine.impl.FlickrSearchEngine;
import org.leo.oglexplorer.model.engine.impl.GoogleSearchEngine;
import org.leo.oglexplorer.model.engine.impl.ITunesSearchEngine;
import org.leo.oglexplorer.model.engine.impl.MySpaceSearchEngine;
import org.leo.oglexplorer.model.engine.impl.TwitterSearchEngine;
import org.leo.oglexplorer.model.engine.impl.WikipediaSearchEngine;
import org.leo.oglexplorer.model.engine.impl.WordpressSearchEngine;
import org.leo.oglexplorer.resources.Resources;
import org.leo.oglexplorer.ui.bookmarks.BookmarksFrame;
import org.leo.oglexplorer.ui.task.BusyComponent;

/**
 * MainPanel $Id: MainPanel.java 177 2011-08-29 07:00:07Z leolewis $
 * 
 * <pre>
 * Contain the components for selecting search engines and types and displaying results
 * </pre>
 * 
 * @author Leo Lewis
 */
public class MainPanel extends JPanel implements SearchListener {

    /**  */
    private static final long serialVersionUID = -4202881373426629368L;

    /** Search button */
    private JButton _searchButton;

    /** Search Text Field */
    private JTextField _searchTextField;

    /** Search Provider */
    private SearchProvider _searchProvider;

    /** Description panel */
    private DescriptionPanel _displayEditorPane;

    /** Component that display the busy status */
    private BusyComponent _busyComponent;

    /** Button that open the config dialog */
    private JButton _openConfigDialogButton;

    /** OGL panel */
    private OGLPanel _oglPanel;

    /**
	 * Panel to which additional components, specific to the current search
	 * engine will be added
	 */
    private JPanel _additionalComponentPanel;

    /** Tree that display the search engines and search contents available */
    private SearchEngineTree _engineTree;

    /** Bookmarks */
    private Bookmarks _bookmarks;

    /** AutoComplete component */
    private AutoCompleteComponent _autoCompleteComponent;

    /** Did you mean component */
    private DidYouMeanComponent _didYouMeanComponent;

    /**
	 * Constructor
	 */
    public MainPanel() {
        super(new BorderLayout());
        init();
    }

    /**
	 * Init the component
	 */
    @SuppressWarnings("serial")
    protected void init() {
        _searchProvider = new SearchProvider();
        for (SearchEngine searchEngine : availableSearchEngines()) {
            _searchProvider.addSearchEngine(searchEngine);
        }
        _searchProvider.addListener(this);
        JPanel main = new JPanel(new BorderLayout());
        JPanel northPanel = new JPanel();
        _additionalComponentPanel = new JPanel(new BorderLayout());
        _additionalComponentPanel.setSize(150, northPanel.getHeight());
        _searchTextField = new JTextField(20) {

            @Override
            protected void paintComponent(Graphics g) {
                ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        _searchTextField.setFont(new Font("Serif", Font.PLAIN, 20));
        northPanel.add(_searchTextField);
        _autoCompleteComponent = new AutoCompleteComponent(_searchProvider, _searchTextField);
        _searchButton = new JButton(Resources.getImageIcon("search.gif"));
        _searchButton.setToolTipText(Resources.getLabel("search.tooltip"));
        _searchButton.setEnabled(false);
        northPanel.add(_searchButton);
        _busyComponent = new BusyComponent(_searchButton, _searchProvider, _searchTextField);
        northPanel.add(_busyComponent);
        _openConfigDialogButton = new JButton(Resources.getImageIcon("settings.png"));
        _openConfigDialogButton.setToolTipText(Resources.getLabel("preferences.tooltip"));
        _openConfigDialogButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new ConfigDialog(SwingUtilities.getWindowAncestor(MainPanel.this), _searchProvider, _autoCompleteComponent.getAutoComplete(), _bookmarks).setVisible(true);
            }
        });
        northPanel.add(_openConfigDialogButton);
        JButton openBookmarksButton = new JButton(Resources.getImageIcon("bookmarks.png"));
        openBookmarksButton.setToolTipText(Resources.getLabel("bookmarks.label"));
        openBookmarksButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                BookmarksFrame.openBookmarksFrame(_bookmarks);
            }
        });
        northPanel.add(openBookmarksButton);
        northPanel.add(_additionalComponentPanel);
        JButton openAboutDialogButton = new JButton(Resources.getImageIcon("about.png"));
        openAboutDialogButton.setToolTipText(Resources.getLabel("about.tooltip"));
        openAboutDialogButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                new LicenseDialog().setVisible(true);
            }
        });
        northPanel.add(openAboutDialogButton);
        main.add(northPanel, BorderLayout.NORTH);
        _oglPanel = new OGLPanel(_searchProvider);
        main.add(_oglPanel, BorderLayout.CENTER);
        _bookmarks = new Bookmarks();
        _displayEditorPane = new DescriptionPanel(_searchProvider, _bookmarks);
        main.add(_displayEditorPane, BorderLayout.SOUTH);
        add(main, BorderLayout.CENTER);
        _engineTree = new SearchEngineTree(_searchProvider);
        add(_engineTree, BorderLayout.WEST);
        _didYouMeanComponent = new DidYouMeanComponent(_searchTextField, _searchProvider);
        add(main, BorderLayout.CENTER);
    }

    /**
	 * Available search engines. Override to specify custom search engine
	 * selection.
	 * 
	 * @return available search engines
	 */
    protected List<SearchEngine> availableSearchEngines() {
        List<SearchEngine> list = new ArrayList<SearchEngine>();
        try {
            list.add(new BingSearchEngine());
        } catch (AxisFault e) {
            e.printStackTrace();
        }
        list.add(new GoogleSearchEngine());
        list.add(new WikipediaSearchEngine());
        list.add(new TwitterSearchEngine());
        try {
            list.add(new FileSystemSearchEngine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        list.add(new ITunesSearchEngine());
        list.add(new WordpressSearchEngine());
        try {
            list.add(new FlickrSearchEngine());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        list.add(new MySpaceSearchEngine());
        return list;
    }

    /**
	 * To call after the ParentFrame is shown
	 */
    public void afterShow() {
        _searchTextField.requestFocus();
    }

    /**
	 * Dispose the panel
	 */
    public void dispose() {
        _displayEditorPane.dispose();
        _oglPanel.dispose();
        _busyComponent.dispose();
        _searchProvider.dispose();
        _engineTree.dispose();
        _autoCompleteComponent.dispose();
        _bookmarks.dispose();
        _didYouMeanComponent.dispose();
        _displayEditorPane = null;
        _oglPanel = null;
        _busyComponent = null;
        _searchProvider = null;
        _engineTree = null;
        _autoCompleteComponent = null;
        _bookmarks = null;
        _didYouMeanComponent = null;
    }

    /**
	 * Return the value of the field progressComponent
	 * 
	 * @return the value of progressComponent
	 */
    public BusyComponent getBusyComponent() {
        return _busyComponent;
    }

    /**
	 * @see org.leo.oglexplorer.model.SearchListener#startSearch(java.lang.String)
	 */
    @Override
    public void startSearch(String words) {
        _searchTextField.setText(words);
    }

    /**
	 * @see org.leo.oglexplorer.model.SearchListener#setBusyMode(boolean)
	 */
    @Override
    public void setBusyMode(boolean busy) {
        _openConfigDialogButton.setEnabled(!busy);
    }

    /**
	 * @see org.leo.oglexplorer.model.SearchListener#searchEngineChanged(org.leo.oglexplorer.model.engine.SearchEngine)
	 */
    @Override
    public void searchEngineChanged(SearchEngine newEngine) {
        if (newEngine == null) {
            _searchButton.setEnabled(false);
        } else {
            _searchButton.setEnabled(true);
            _additionalComponentPanel.removeAll();
            JComponent engineAdditionalComponent = newEngine.additionalSearchComponent(this);
            if (engineAdditionalComponent != null) {
                _additionalComponentPanel.add(engineAdditionalComponent, BorderLayout.CENTER);
            }
            _additionalComponentPanel.revalidate();
            _additionalComponentPanel.repaint();
        }
    }
}
