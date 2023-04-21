package org.moviereport.ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import org.apache.commons.lang.ObjectUtils;
import org.moviereport.core.model.MovieCollection;
import org.moviereport.core.model.MovieDescription;
import org.moviereport.ui.bf.MovieReportCoreAccess;
import org.moviereport.ui.contentview.ContentPanel;
import org.moviereport.ui.navigationtree.NavigationPanel;
import org.moviereport.ui.navigationtree.NavigationTreeModel;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
import org.pushingpixels.substance.api.SubstanceConstants.Side;

public class MainFrame extends JFrame {

    private static final long serialVersionUID = -4276711249095128631L;

    private static final String DIALOG_TITLE = "Movie Report - The movie Manager";

    private JSplitPane splitPane = new JSplitPane();

    private ContentPanel contentPanel = new ContentPanel();

    private NavigationPanel navigationPanel = new NavigationPanel();

    public MainFrame() {
    }

    public void init() {
        setTitle(DIALOG_TITLE);
        Dimension dimension = new Dimension(900, 600);
        setSize(dimension);
        setPreferredSize(dimension);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                Main.exit();
            }
        });
        setJMenuBar(getMovieReportMenuBar());
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(200);
        splitPane.setRightComponent(contentPanel);
        navigationPanel.init();
        navigationPanel.addPropertyChangeListener(NavigationPanel.PROPERTY_SELECTION_CHANGED, new NavigationSelectionChangedListener());
        splitPane.setLeftComponent(navigationPanel);
        contentPane.add(splitPane, BorderLayout.CENTER);
    }

    private JMenuBar getMovieReportMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu();
        fileMenu.setText("File");
        menuBar.add(fileMenu);
        JMenuItem exitItem = new JMenuItem();
        exitItem.setText("Exit");
        exitItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Main.exit();
            }
        });
        fileMenu.add(exitItem);
        return menuBar;
    }

    private class NavigationSelectionChangedListener implements PropertyChangeListener {

        public void propertyChange(PropertyChangeEvent evt) {
            Object selection = evt.getNewValue();
            if (ObjectUtils.equals(NavigationTreeModel.ALL_MOVIES, selection)) {
                contentPanel.setMovieDescriptions(MovieReportCoreAccess.getInstance().getAllMovieDescriptions());
            } else if (ObjectUtils.equals(NavigationTreeModel.ALL_MOVIE_COLLECTIONS, selection)) {
                contentPanel.setMovieDescriptions(MovieReportCoreAccess.getInstance().getAllMovieDescriptions());
            } else if (selection instanceof MovieCollection) {
                MovieCollection movieCollection = (MovieCollection) selection;
                contentPanel.setMovieDescriptions(movieCollection.getAllMovieDescriptionsRecursiv());
            } else if (selection instanceof MovieDescription) {
                MovieDescription movieDescription = (MovieDescription) selection;
                contentPanel.setMovieDescription(movieDescription);
            } else {
                contentPanel.resetPanel();
            }
        }
    }

    private static JToolBar getToolbar(int size, boolean hasStrings) {
        JToolBar toolBar = new JToolBar();
        toolBar.setSize(300, 100);
        JButton buttonCut = new JButton(hasStrings ? "cut" : null, getIcon(size + "/edit-cut"));
        buttonCut.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, Boolean.TRUE);
        toolBar.add(buttonCut);
        JButton buttonCopy = new JButton(hasStrings ? "copy" : null, getIcon(size + "/edit-copy"));
        buttonCopy.putClientProperty(SubstanceLookAndFeel.BUTTON_NO_MIN_SIZE_PROPERTY, Boolean.TRUE);
        buttonCopy.setEnabled(false);
        toolBar.add(buttonCopy);
        JButton buttonPaste = new JButton(getIcon(size + "/edit-paste"));
        toolBar.add(buttonPaste);
        JButton buttonSelectAll = new JButton(getIcon(size + "/edit-select-all"));
        toolBar.add(buttonSelectAll);
        JButton buttonDelete = new JButton(getIcon(size + "/edit-delete"));
        toolBar.add(buttonDelete);
        toolBar.addSeparator();
        JToolBar innerToolbar = new JToolBar(JToolBar.HORIZONTAL);
        innerToolbar.setFloatable(false);
        JToggleButton buttonFormatCenter = new JToggleButton(getIcon(size + "/format-justify-center"));
        buttonFormatCenter.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(5.0f));
        innerToolbar.add(buttonFormatCenter);
        JToggleButton buttonFormatLeft = new JToggleButton(getIcon(size + "/format-justify-left"));
        innerToolbar.add(buttonFormatLeft);
        JToggleButton buttonFormatRight = new JToggleButton(getIcon(size + "/format-justify-right"));
        innerToolbar.add(buttonFormatRight);
        JToggleButton buttonFormatFill = new JToggleButton(getIcon(size + "/format-justify-fill"));
        buttonFormatFill.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(0.0f));
        innerToolbar.add(buttonFormatFill);
        toolBar.add(innerToolbar);
        toolBar.addSeparator();
        if (size > 20) {
            JToolBar innerToolbar2 = new JToolBar(JToolBar.HORIZONTAL);
            innerToolbar2.setFloatable(false);
            JPanel innerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            innerToolbar2.add(innerPanel, BorderLayout.CENTER);
            final JToggleButton buttonStyleBold = new JToggleButton(getIcon(size + "/format-text-bold"));
            Set<Side> rightSide = EnumSet.of(Side.RIGHT);
            buttonStyleBold.putClientProperty(SubstanceLookAndFeel.BUTTON_OPEN_SIDE_PROPERTY, rightSide);
            buttonStyleBold.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(3.0f));
            final JToggleButton buttonStyleItalic = new JToggleButton(getIcon(size + "/format-text-italic"));
            buttonStyleItalic.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(0.0f));
            buttonStyleItalic.putClientProperty(SubstanceLookAndFeel.BUTTON_OPEN_SIDE_PROPERTY, rightSide);
            final JToggleButton buttonStyleUnderline = new JToggleButton(getIcon(size + "/format-text-underline"));
            buttonStyleUnderline.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(0.0f));
            buttonStyleUnderline.putClientProperty(SubstanceLookAndFeel.BUTTON_OPEN_SIDE_PROPERTY, rightSide);
            final JToggleButton buttonStyleStrikethrough = new JToggleButton(getIcon(size + "/format-text-strikethrough"));
            buttonStyleStrikethrough.putClientProperty(SubstanceLookAndFeel.BUTTON_SIDE_PROPERTY, EnumSet.of(Side.LEFT));
            buttonStyleStrikethrough.putClientProperty(SubstanceLookAndFeel.CORNER_RADIUS, Float.valueOf(3.0f));
            buttonStyleBold.setSelected(true);
            innerPanel.add(buttonStyleBold);
            innerPanel.add(buttonStyleItalic);
            innerPanel.add(buttonStyleUnderline);
            innerPanel.add(buttonStyleStrikethrough);
            toolBar.add(innerToolbar2);
        }
        toolBar.add(Box.createGlue());
        JButton buttonExit = new JButton(getIcon(size + "/process-stop"));
        buttonExit.setToolTipText("Closes the test application");
        buttonExit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        toolBar.add(buttonExit);
        return toolBar;
    }

    public static Icon getIcon(String iconName) {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("test/check/icons/" + iconName + ".gif");
        if (url != null) return new ImageIcon(url);
        url = cl.getResource("test/check/icons/" + iconName + ".png");
        if (url != null) return new ImageIcon(url);
        return null;
    }
}
