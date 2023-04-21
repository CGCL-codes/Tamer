package com.xenoage.zong.gui.controller.frames;

import com.xenoage.zong.gui.controller.menues.MainMenuBarController;
import com.xenoage.zong.gui.controller.panels.ScorePanelController;
import com.xenoage.zong.gui.view.frames.ViewerMainFrame;
import com.xenoage.zong.viewer.ViewerApplication;

/**
 * The controller for the main frame of the Zong! Viewer.
 * 
 * Use this class instead of using {@link ViewerMainFrame} directly.
 *
 * @author Andreas Wenger
 */
public class ViewerMainFrameController extends MainFrameController {

    MainMenuBarController mainMenuBarController;

    /**
   * Creates a controller for the main frame of the Zong! Viewer.
   * @param app                   the main class of the application
   * @param scorePanelController  the controller of the score panel
   */
    public ViewerMainFrameController(ViewerApplication app, ScorePanelController scorePanelController) {
        super(app);
        initMainFrame(new ViewerMainFrame(this), scorePanelController);
        mainMenuBarController = new MainMenuBarController();
        frame.setJMenuBar(mainMenuBarController.getJMenuBar());
    }

    /**
   * Gets the main frame.
   */
    @Override
    public ViewerMainFrame getMainFrame() {
        return (ViewerMainFrame) frame;
    }

    /**
   * Gets the main menubar controller.
   */
    public MainMenuBarController getMainMenuBarController() {
        return mainMenuBarController;
    }

    @Override
    public void updateMenu() {
        mainMenuBarController.updateJMenuBar();
        super.updateMenu();
    }
}
