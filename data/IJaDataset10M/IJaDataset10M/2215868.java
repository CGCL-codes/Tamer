package jhomenet.server.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jhomenet.ui.MainWindow;
import jhomenet.ui.window.IWindowListener;
import jhomenet.ui.window.WindowEventImpl;
import jhomenet.commons.ServerContext;
import jhomenet.server.console.LocalConsoleService;

/**
 * TODO: Class description.
 * <p>
 * Id: $Id: $
 *
 * @author Dave Irwin (jhomenet at gmail dot com)
 */
public class UiManager {

    /**
	 * 
	 */
    private static final UiManager instance = new UiManager();

    /**
	 * Server reference.
	 */
    private ServerContext serverContext;

    /**
	 * The main local GUI window.
	 */
    private static MainWindow mainGuiWindow;

    /**
	 * 
	 */
    private static MainGuiWindowListener localWindowListener;

    /**
	 * 
	 */
    private static LocalConsoleService localConsole;

    /**
	 * Listens for when the authentication window's exit button is pushed. When
	 * it is pushed, this will open the local console window which again
	 * prompts the user to authenticate.
	 */
    private static final ActionListener exitAuthButtonListener = new ActionListener() {

        /**
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
        public void actionPerformed(ActionEvent arg0) {
            if (localConsole != null) {
                localConsole.setDisplayConsole(true);
            }
        }
    };

    /**
	 * Get a new instance of the UI manager. This method is NOT thread safe.
	 * 
	 * @param serverContext
	 * @param config
	 * @return
	 */
    public static UiManager getInstance(ServerContext serverContext) {
        instance.serverContext = serverContext;
        return instance;
    }

    /**
	 * 
	 * @param serverContext
	 */
    private UiManager() {
        super();
        UiManager.localWindowListener = new MainGuiWindowListener();
    }

    /**
	 * @return the exitAuthButtonListener
	 */
    public static final ActionListener getExitAuthButtonListener() {
        return exitAuthButtonListener;
    }

    /**
	 * 
	 */
    public void createMainGuiWindow() {
        createMainGuiWindow(false);
    }

    /**
	 * 
	 * @param maximized
	 */
    public void createMainGuiWindow(boolean maximized) {
        createMainGuiWindow(maximized, null);
    }

    /**
	 * 
	 * @param windowListener
	 */
    public void createMainGuiWindow(IWindowListener windowListener) {
        createMainGuiWindow(false, windowListener);
    }

    /**
	 * 
	 * @param maximized
	 * @param windowListener
	 */
    public void createMainGuiWindow(boolean maximized, IWindowListener windowListener) {
        if (UiManager.mainGuiWindow == null) UiManager.mainGuiWindow = new MainWindow(serverContext);
        UiManager.mainGuiWindow.addWindowListener(localWindowListener);
        if (windowListener != null) UiManager.mainGuiWindow.addWindowListener(windowListener);
        if (maximized) UiManager.mainGuiWindow.setMaximized();
        UiManager.mainGuiWindow.setVisible(true);
    }

    /**
	 * @param localConsole the localConsole to set
	 */
    public final void setLocalConsole(LocalConsoleService localConsole) {
        UiManager.localConsole = localConsole;
    }

    /**
	 * 
	 */
    private class MainGuiWindowListener implements IWindowListener {

        /**
		 * @see jhomenet.ui.window.IWindowListener#windowClosing(jhomenet.ui.window.WindowEventImpl)
		 */
        public void windowClosing(WindowEventImpl event) {
            if (localConsole != null) {
                localConsole.setDisplayConsole(true);
            }
        }
    }
}
