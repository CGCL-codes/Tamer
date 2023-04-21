package gui;

import java.awt.Toolkit;
import javax.swing.JFrame;

/**
 * This class creates main window.
 * 
 * @author Bartek Kowalik
 *
 */
public class CSFrame extends JFrame {

    private static final long serialVersionUID = 8828008603294532253L;

    /**
	 * Default construction. It does:
	 * <ul>
	 * <li>sets title,</li>
	 * <li>sets size (400x400),</li>
	 * <li>sets location not related to platform,</li>
	 * <li>adds default menu bar.</li>
	 * </ul>
	 */
    public CSFrame() {
        setTitle("COUNTER STATS");
        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        setLocationByPlatform(false);
        setAtCenter();
        addDefaultMenuBar();
        addDefaultPanel();
    }

    /**
	 * Adds default menu bar based on:
	 * @see CSMenuBar
	 */
    private void addDefaultMenuBar() {
        CSMenuBar menuBar = new CSMenuBar();
        this.setJMenuBar(menuBar);
    }

    /**
	 * Adds default panel based on:
	 * @see CSMainPanel
	 */
    private void addDefaultPanel() {
        CSMainPanel panel = new CSMainPanel();
        this.add(panel);
    }

    /**
	 * Sets program window in a center of a screen.
	 */
    private void setAtCenter() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        int screenHeight = toolkit.getScreenSize().height;
        int screenWidth = toolkit.getScreenSize().width;
        int x = (screenWidth - DEFAULT_WIDTH) / 2;
        int y = (screenHeight - DEFAULT_HEIGHT) / 2;
        this.setLocation(x, y);
    }

    /**
	 * Default height of main window
	 */
    private static final int DEFAULT_HEIGHT = 400;

    /**
	 * Default width of main window
	 */
    private static final int DEFAULT_WIDTH = 400;
}
