package com.eteks.sweethome3d;

import com.eteks.sweethome3d.model.Home;
import com.eteks.sweethome3d.model.HomeApplication;
import com.eteks.sweethome3d.plugin.PluginManager;
import com.eteks.sweethome3d.viewcontroller.ContentManager;
import com.eteks.sweethome3d.viewcontroller.Controller;
import com.eteks.sweethome3d.viewcontroller.HomeController;
import com.eteks.sweethome3d.viewcontroller.View;
import com.eteks.sweethome3d.viewcontroller.ViewFactory;

/**
 * Home frame pane controller.
 * @author Emmanuel Puybaret
 */
public class HomeFrameController implements Controller {

    private final Home home;

    private final HomeApplication application;

    private final ViewFactory viewFactory;

    private final ContentManager contentManager;

    private final PluginManager pluginManager;

    private View homeFrameView;

    private HomeController homeController;

    public HomeFrameController(Home home, HomeApplication application, ViewFactory viewFactory, ContentManager contentManager, PluginManager pluginManager) {
        this.home = home;
        this.application = application;
        this.viewFactory = viewFactory;
        this.contentManager = contentManager;
        this.pluginManager = pluginManager;
    }

    /**
   * Returns the view associated with this controller.
   */
    public View getView() {
        if (this.homeFrameView == null) {
            this.homeFrameView = new HomeFramePane(this.home, this.application, this.contentManager, this);
        }
        return this.homeFrameView;
    }

    /**
   * Returns the home controller managed by this controller.
   */
    public HomeController getHomeController() {
        if (this.homeController == null) {
            this.homeController = new HomeController(this.home, this.application, this.viewFactory, this.contentManager, this.pluginManager);
        }
        return this.homeController;
    }

    /**
   * Displays the view controlled by this controller.
   */
    public void displayView() {
        ((HomeFramePane) getView()).displayView();
    }
}
