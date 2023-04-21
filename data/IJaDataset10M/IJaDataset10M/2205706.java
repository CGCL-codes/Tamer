package com.hifi.plugin.library;

import org.java.plugin.Plugin;
import com.hifi.core.api.modules.ICoreModuleManager;
import com.hifi.core.api.ui.constants.UIModuleEnum;
import com.hifi.core.api.ui.modules.displaypane.IDisplayPane;
import com.hifi.core.api.ui.modules.frame.IMainFrame;
import com.hifi.core.api.ui.modules.mainpane.IMainPaneUI;
import com.hifi.core.api.ui.modules.sources.ISourceTreeUI;
import com.hifi.core.api.ui.modules.statusbar.IStatusBarUI;
import com.hifi.core.db.ConnectionPool;
import com.hifi.plugin.library.api.model.ILibraryModelManager;
import com.hifi.plugin.library.api.model.LibraryModelEnum;
import com.hifi.plugin.library.api.model.modules.library.ILibraryModel;
import com.hifi.plugin.library.api.ui.ILibraryUIManager;
import com.hifi.plugin.library.api.ui.LibraryUIEnum;
import com.hifi.plugin.library.api.ui.modules.ILibraryTree;
import com.hifi.plugin.library.controllers.FilterLibraryByDirController;
import com.hifi.plugin.library.controllers.LibraryEventController;
import com.hifi.plugin.library.model.LibraryModelManager;
import com.hifi.plugin.library.ui.LibraryUIManager;
import com.hifi.plugin.library.ui.constants.library.LibrarySourceEnum;
import com.hifi.plugin.songview.SongViewPlugin;
import com.hifi.plugin.songview.api.IViewPlugin;
import com.hifi.plugin.songview.api.IViewUI;
import com.hifi.plugin.ui.core.IUIModuleManager;
import com.hifi.plugin.ui.ext.IComponentPlugin;
import com.hifi.plugin.ui.ext.ITheme;

public class LibraryPlugin extends Plugin implements IComponentPlugin {

    public static final String PLUGIN_ID = "com.hifi.plugin.library";

    private LibraryUIManager libUI;

    private LibraryModelManager libModel;

    private ILibraryTree libraryUI;

    private IStatusBarUI statusbar;

    private IMainFrame mainframe;

    private ISourceTreeUI sourceUI;

    private IMainPaneUI mainpane;

    private IDisplayPane displaypane;

    private IViewUI view;

    public LibraryPlugin() {
    }

    @Override
    protected void doStart() throws Exception {
    }

    @Override
    public void init(ICoreModuleManager coreManager, IUIModuleManager uiManager, ITheme theme) {
        libUI = new LibraryUIManager(theme);
        libModel = new LibraryModelManager(ConnectionPool.get().getConnection());
        sourceUI = (ISourceTreeUI) uiManager.getModule(UIModuleEnum.SOURCE_TREE);
        mainframe = (IMainFrame) uiManager.getModule(UIModuleEnum.FRAME);
        mainpane = (IMainPaneUI) uiManager.getModule(UIModuleEnum.MAINPANE);
        displaypane = (IDisplayPane) uiManager.getModule(UIModuleEnum.DISPLAYPANE);
        statusbar = (IStatusBarUI) uiManager.getModule(UIModuleEnum.STATUSBAR);
        try {
            Plugin plugin = getManager().getPlugin(SongViewPlugin.PLUGIN_ID);
            IComponentPlugin componentPlugin = (IComponentPlugin) plugin;
            componentPlugin.init(coreManager, uiManager, theme);
            IViewPlugin songview = (IViewPlugin) plugin;
            ILibraryModel libraryModel = (ILibraryModel) libModel.getModule(LibraryModelEnum.LIBRARYMODEL);
            view = songview.createView(libraryModel.getLibraries().get(0));
            initUI();
            initControllers();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        libraryUI = (ILibraryTree) libUI.getModule(LibraryUIEnum.LIBRARY);
        libraryUI.init(mainframe.getFrame(), null);
        ILibraryModel libraryModel = (ILibraryModel) libModel.getModule(LibraryModelEnum.LIBRARYMODEL);
        libraryUI.addToTree(sourceUI, libraryModel.getLibraryTreeNodes());
        mainpane.addPane(LibrarySourceEnum.LIBRARY, view.getView());
        mainpane.addPane(LibrarySourceEnum.LIBRARY_DIRECTORY, view.getView());
    }

    private void initControllers() {
        libraryUI.addLibraryEventListener(new LibraryEventController(libraryUI, statusbar, displaypane));
        libraryUI.addLibraryDirectoryEventListener(new FilterLibraryByDirController(view));
    }

    public ILibraryUIManager getLibraryUI() {
        return libUI;
    }

    public ILibraryModelManager getLibraryModel() {
        return libModel;
    }

    @Override
    protected void doStop() throws Exception {
    }
}
