package FeedReader;

import RssIO.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * @author sahaqiel
 *
 * previously used jdom.jar and rome-1.0.jar as libraries.
 */
public class FeedReader implements PortlIfc {

    private JFrame frame;

    private JTable feedsJTable;

    private ArticleItemModel feedsModel = new ArticleItemModel();

    private FeedChooserPanel fcPanel;

    private JTextPane detailsJTextArea;

    private int indexEngine = 0;

    private String lastDir;

    private FeedManager mngr;

    public FeedReader(String[] args) {
    }

    public void createJFrame() {
        frame = new JFrame("Feed Reader");
        JMenuBar bar = new JMenuBar();
        JMenu[] menuArray = getJMenus();
        for (JMenu menu : menuArray) {
            bar.add(menu);
        }
        JMenuItem exitJMenuItem = new JMenuItem("Exit");
        menuArray[0].add(exitJMenuItem);
        exitJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                exitJMenuItemActionPerformed();
            }
        });
        frame.setJMenuBar(bar);
        frame.add(getJComponent());
        frame.setSize(getPreferredSize());
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void exitJMenuItemActionPerformed() {
        stop();
        System.exit(0);
    }

    @Override
    public String[][] getAppletInfo() {
        return new String[0][0];
    }

    @Override
    public JMenu[] getJMenus() {
        return new JMenu[] { getFileJMenu(), getFeedJMenu() };
    }

    private JMenu getFileJMenu() {
        JMenu fileJMenu = new JMenu("File");
        JMenuItem newUrlJMenuItem = new JMenuItem("New URL");
        fileJMenu.add(newUrlJMenuItem);
        newUrlJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newUrlJMenuItemActionPerformed();
            }
        });
        JMenuItem openFileJMenuItem = new JMenuItem("Open File");
        fileJMenu.add(openFileJMenuItem);
        openFileJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                openFileJMenuItemActionPerformed();
            }
        });
        fileJMenu.addSeparator();
        JMenuItem printPreviewJMenuItem = new JMenuItem("Print Preview");
        fileJMenu.add(printPreviewJMenuItem);
        printPreviewJMenuItem.setEnabled(false);
        JMenuItem printJMenuItem = new JMenuItem("Print Article");
        fileJMenu.add(printJMenuItem);
        printJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                printArticleActionPerformed();
            }
        });
        fileJMenu.addSeparator();
        return fileJMenu;
    }

    private void newUrlJMenuItemActionPerformed() {
        String urlPath = JOptionPane.showInputDialog(frame, "Please Input URL", "http://feeds.washingtonpost.com/rss/business");
        if (urlPath != null) {
            mngr = FeedManager.getInstance();
            mngr.addFeed(urlPath);
            fetchFeedAtUrl(urlPath);
        }
    }

    private void openFileJMenuItemActionPerformed() {
        JFileChooser chooser = null;
        if (lastDir != null) {
            chooser = new JFileChooser(lastDir);
        } else {
            chooser = new JFileChooser("/tmp/");
        }
        chooser.setFileFilter(new FeedFileFilter());
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                lastDir = chooser.getCurrentDirectory().getAbsolutePath();
                openFile(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }

    protected void openFile(String filePath) {
        if (filePath != null) {
            FeedObjectIfc feedObj = new FeedFetcher().getFeedObject(filePath);
            feedsModel.clearArticles();
            if (detailsJTextArea != null) {
                detailsJTextArea.setText("");
                if (feedObj.getArticleCount() > 0) {
                    detailsJTextArea.setText(feedObj.getArticleAt(0).getDescription());
                }
            }
            for (int i = 1; i < feedObj.getArticleCount(); i++) {
                feedsModel.addArticle(feedObj.getArticleAt(i));
            }
        }
    }

    private void printArticleActionPerformed() {
        if ((detailsJTextArea != null) && (detailsJTextArea.getText().length() > 0)) {
            JTextField headerJTextField = new JTextField("");
            JTextField footerJTextField = new JTextField("");
            JTextArea textArea = new JTextArea(detailsJTextArea.getText());
            PageDialogTask dialogTask = null;
            try {
                dialogTask = new PageDialogTask(headerJTextField, footerJTextField, textArea);
                dialogTask.doInBackground();
            } catch (Exception ex) {
                String title = ex.getClass().getSimpleName();
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage(), title, JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
            try {
                if (dialogTask != null && dialogTask.getJob() != null) {
                    PrintingTask task = new PrintingTask(dialogTask.getJob());
                    task.doInBackground();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private JMenu getFeedJMenu() {
        JMenu feedJMenu = new JMenu("Feeds");
        JMenuItem fetchOnStartupJMenuItem = new JCheckBoxMenuItem("Fetch Stored Feeds on Startup");
        feedJMenu.add(fetchOnStartupJMenuItem);
        fetchOnStartupJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fetchOnStartupJMenuItemActionPerformed();
            }
        });
        feedJMenu.addSeparator();
        JMenuItem addFeedJMenuItem = new JMenuItem("Add Feed");
        feedJMenu.add(addFeedJMenuItem);
        addFeedJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                newUrlJMenuItemActionPerformed();
            }
        });
        JMenuItem removeFeedJMenuItem = new JMenuItem("Remove Feed");
        feedJMenu.add(removeFeedJMenuItem);
        return feedJMenu;
    }

    private void fetchOnStartupJMenuItemActionPerformed() {
    }

    private void siteJMenuItemActionPerformed(ActionEvent event) {
        String cmd = event.getActionCommand();
        mngr = FeedManager.getInstance();
        FeedObjectIfc selected = mngr.getFeed(cmd);
        if (selected != null) {
            if (feedsModel != null) {
                feedsJTable.setVisible(false);
                feedsModel.clearArticles();
                for (int i = 0; i < selected.getArticleCount(); i++) {
                    feedsModel.addArticle(selected.getArticleAt(i));
                }
                feedsJTable.setVisible(true);
            }
        }
    }

    private JMenu getArticleJMenu() {
        JMenu articleJMenu = new JMenu("Article");
        JMenuItem openArticleJMenuItem = new JMenuItem("Open");
        articleJMenu.add(openArticleJMenuItem);
        openArticleJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                openArticleJMenuItemActionPerformed();
            }
        });
        JMenuItem markImportantJMenuItem = new JMenuItem("Mark Important");
        articleJMenu.add(markImportantJMenuItem);
        markImportantJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                markImportantJMenuItemActionPerformed();
            }
        });
        JMenuItem removeArticleJMenuItem = new JMenuItem("Remove");
        articleJMenu.add(removeArticleJMenuItem);
        removeArticleJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                removeArticleJMenuItemActionPerformed();
            }
        });
        return articleJMenu;
    }

    private void openArticleJMenuItemActionPerformed() {
    }

    private void markImportantJMenuItemActionPerformed() {
    }

    private void removeArticleJMenuItemActionPerformed() {
    }

    private void addFeedJMenuItemActionPerformed() {
    }

    @Override
    public JComponent getJComponent() {
        JPanel allPanel = new JPanel(new BorderLayout());
        JSplitPane panel = new JSplitPane(SwingConstants.VERTICAL);
        allPanel.add(panel, BorderLayout.CENTER);
        fcPanel = new FeedChooserPanel();
        panel.setLeftComponent(fcPanel.getJComponent());
        fcPanel.addActionListener(FeedChooserPanel.BUTTONS.Fetch, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                fetchJButtonActionPerformed();
            }
        });
        JSplitPane centerJSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        panel.setRightComponent(centerJSplitPane);
        centerJSplitPane.setDividerLocation(0.5);
        feedsJTable = new JTable(feedsModel);
        centerJSplitPane.setTopComponent(new JScrollPane(feedsJTable));
        feedsJTable.setColumnSelectionAllowed(false);
        feedsJTable.setRowSelectionAllowed(true);
        feedsJTable.setFillsViewportHeight(true);
        feedsJTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        feedsJTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent event) {
                feedsJTableValueChanged(event);
            }
        });
        detailsJTextArea = new JTextPane();
        centerJSplitPane.setBottomComponent(new JScrollPane(detailsJTextArea));
        detailsJTextArea.setEditable(false);
        return allPanel;
    }

    private void fetchJButtonActionPerformed() {
        if (fcPanel != null) {
            String selected = (String) fcPanel.getSeleted();
            fetchFeedAtUrl(selected);
        }
    }

    private void feedsJTableValueChanged(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) {
            return;
        }
        if (feedsJTable.getSelectedRow() == -1) {
            return;
        }
        ArticleItemModel model = (ArticleItemModel) feedsJTable.getModel();
        ArticleItemIfc item = model.getItemAt(feedsJTable.getSelectedRow());
        StringBuilder buf = new StringBuilder();
        buf.append(item.getDescription());
        if (detailsJTextArea != null) {
            detailsJTextArea.setContentType("text/html");
            buf.append("</html>");
            detailsJTextArea.setText("<html>" + buf.toString());
        }
    }

    private void fetchFeedAtUrl(final String urlStr) {
        if (urlStr != null) {
            mngr = FeedManager.getInstance();
            FeedObjectIfc feedObj = mngr.fetchFeed(urlStr);
            if (feedObj != null) {
                feedsModel.clearArticles();
                ArticleItemIfc firstArticle = null;
                for (int i = 0; i < feedObj.getArticleCount(); i++) {
                    feedsModel.addArticle(feedObj.getArticleAt(i));
                    if (i == 0) {
                        firstArticle = feedObj.getArticleAt(i);
                    }
                }
                if (firstArticle != null) {
                    detailsJTextArea.setText(firstArticle.getDescription());
                }
            }
        }
    }

    @Override
    public String[][] getParameterInfo() {
        return new String[0][0];
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1024, 768);
    }

    @Override
    public double getVersion() {
        return 0.2;
    }

    @Override
    public void start() {
        mngr = FeedManager.getInstance();
        mngr.open();
        mngr.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent event) {
                feedManagerPropertyChanged(event);
            }
        });
    }

    private void feedManagerPropertyChanged(PropertyChangeEvent event) {
        if (event.getPropertyName().equals(FeedManager.FEED_ADDED_PROPERTY)) {
        } else if (event.getPropertyName().equals(FeedManager.FEED_FETCHED_PROPERTY)) {
            if (feedsModel != null) {
                String feedUrl = (String) event.getNewValue();
                FeedObjectIfc feedObj = mngr.getFeed(feedUrl);
                if (feedObj != null) {
                    feedsModel.clearArticles();
                    for (int i = 0; i < feedObj.getArticleCount(); i++) {
                        feedsModel.addArticle(feedObj.getArticleAt(i));
                    }
                }
            }
        }
    }

    @Override
    public void stop() {
        mngr = FeedManager.getInstance();
        mngr.close();
    }

    public static void main(final String[] args) {
        final FeedReader fr = new FeedReader(args);
        fr.start();
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                fr.createJFrame();
            }
        });
    }

    private void out(String m) {
        System.out.println(getClass().getName() + " " + m);
    }
}
