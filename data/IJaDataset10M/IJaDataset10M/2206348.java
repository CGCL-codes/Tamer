package br.edu.ufcg.ccc.javalog.view;

import java.util.Deque;
import java.util.LinkedList;
import javax.swing.JFrame;

/** 
 * Window Manager for Java Swing.
 * @author Allyson Lima, Diego Pedro, Victor Freire
 * @version 22/11/09 
 */
public class WindowManager {

    /**
	 * This singleton's instance.
	 */
    private static WindowManager instance;

    /**
	 * The window stack.
	 */
    private Deque<JFrame> windows;

    /**
	 * Only the singleton can construct WindowManager objects.
	 */
    private WindowManager() {
        windows = new LinkedList<JFrame>();
    }

    /**
	 * Returns the unique instance of this singleton.
	 */
    public static WindowManager getInstance() {
        if (instance == null) instance = new WindowManager();
        return instance;
    }

    /**
	 * Enables the top window, restores user control over it. 
	 * @return true on sucess, false if there are no windows.
	 */
    private boolean enableTopWindow() {
        JFrame janelaTopo = windows.peekFirst();
        if (janelaTopo == null) return false;
        janelaTopo.setEnabled(true);
        janelaTopo.setVisible(true);
        janelaTopo.requestFocus();
        return true;
    }

    /**
	 * Opens a new window and adds it to the window stack.
	 * @param newWindow window to open
	 */
    public void openNewWindow(JFrame newWindow) {
        JFrame janelaTopo = windows.peekFirst();
        newWindow.setLocationRelativeTo(janelaTopo);
        if (janelaTopo != null) janelaTopo.setEnabled(false);
        windows.push(newWindow);
        enableTopWindow();
    }

    /**
	 * Closes the last opened window if there are still windows opened.
	 * @return false if there are no windows to close
	 */
    public boolean closeLastWindow() {
        windows.pollFirst();
        return enableTopWindow();
    }

    /**
	 * Return the window on top, the current window.
	 * @return current window
	 */
    public JFrame getTopWindow() {
        return windows.peekLast();
    }
}
