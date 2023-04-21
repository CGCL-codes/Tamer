package de.sambalmueslie.geocache_planer.gui;

import java.awt.BorderLayout;
import javax.swing.JFrame;
import de.sambalmueslie.geocache_planer.common.controller.Controller;
import de.sambalmueslie.geocache_planer.common.model.MainModel;
import de.sambalmueslie.geocache_planer.gui.menu.MenuBar;
import de.sambalmueslie.geocache_planer.gui.panel.MainPanel;
import de.sambalmueslie.geocache_planer.gui.panel.MainToolbar;

/**
 * the view of the geocache planer.
 * 
 * @author Sambalmueslie
 * 
 * @date 05.05.2009
 * 
 */
public class GeocachePlanerGui extends JFrame {

    /** the default border size for the {@link BorderLayout}. */
    public static final int BORDER = 5;

    /**
	 * 
	 */
    private static final long serialVersionUID = 1559210764273023156L;

    /** the title of the window. */
    private static final String TITLE_TXT = "Geocache Planer V0.23 by Sambalmueslie";

    /**
	 * constructor.
	 * 
	 * @param c
	 *            the controller
	 * @param m
	 *            the model
	 */
    public GeocachePlanerGui(final Controller c, final MainModel m) {
        super(TITLE_TXT);
        controller = c;
        model = m;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(MAXIMIZED_BOTH);
        setSize(600, 400);
        setJMenuBar(getGeocachePlanerMenuBar());
        setLayout(new BorderLayout(BORDER, BORDER));
        add(getMainToolbar(), BorderLayout.NORTH);
        add(getMainPanel(), BorderLayout.CENTER);
    }

    /**
	 * geter for the menu bar.
	 * 
	 * @return the menu bar
	 */
    private MenuBar getGeocachePlanerMenuBar() {
        if (menuBar == null) {
            menuBar = new MenuBar(controller);
        }
        return menuBar;
    }

    /**
	 * getter for the main panel.
	 * 
	 * @return the main panel
	 */
    private MainPanel getMainPanel() {
        if (mainPanel == null) {
            mainPanel = new MainPanel(controller, model);
        }
        return mainPanel;
    }

    /**
	 * getter for the toolbar.
	 * 
	 * @return the toolbar
	 */
    private MainToolbar getMainToolbar() {
        if (toolbar == null) {
            toolbar = new MainToolbar(controller);
        }
        return toolbar;
    }

    /** the controller. */
    private Controller controller = null;

    /** the main window. */
    private MainPanel mainPanel = null;

    /** the menu bar. */
    private MenuBar menuBar = null;

    /** the model. */
    private MainModel model = null;

    /** the tool bar. */
    private MainToolbar toolbar = null;
}
