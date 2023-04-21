package com.iplayawriter.novelizer.view;

import com.iplayawriter.novelizer.model.IValues;
import com.iplayawriter.novelizer.view.info.IInfoView;
import com.iplayawriter.novelizer.view.info.InfoView;
import com.iplayawriter.novelizer.view.navigator.INavigatorView;
import com.iplayawriter.novelizer.view.navigator.NavigatorView;
import com.iplayawriter.novelizer.view.plot.IPlotView;
import com.iplayawriter.novelizer.view.plot.PlotView;
import com.iplayawriter.novelizer.view.scene.ISceneView;
import com.iplayawriter.novelizer.view.scene.SceneView;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

/**
 * A perspective implementation that shows a plot line of scenes as well the repository 
 * of novel elements other than scenes and sequences.
 * 
 * @author Erik
 */
class ScenePerspective implements IPerspective {

    /** The application frame */
    private final JFrame appFrame;

    /** The Navigator view */
    private NavigatorView navigatorView;

    /** The viewer for general novel information */
    private InfoView infoView;

    /** The view of the plot line */
    private PlotView plotView;

    /** The viewer for scene information */
    private SceneView sceneView;

    /** Factory for creating dialogs */
    private final DialogFactory dialogFactory;

    /**
     * Creates the perspective.
     * 
     * @param appFrame the application frame
     */
    ScenePerspective(JFrame appFrame) {
        this.appFrame = appFrame;
        dialogFactory = new DialogFactory(appFrame);
    }

    /**
     * Initializes the perspective's layout.
     */
    void init() {
        navigatorView = new NavigatorView();
        navigatorView.init();
        infoView = new InfoView();
        infoView.init();
        plotView = new PlotView();
        plotView.init();
        sceneView = new SceneView();
        sceneView.init();
        JSplitPane bottomSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, infoView, sceneView);
        bottomSplitPane.setBorder(null);
        bottomSplitPane.setDividerLocation(200);
        bottomSplitPane.setOneTouchExpandable(true);
        JSplitPane rightSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, plotView, bottomSplitPane);
        rightSplitPane.setDividerLocation(300);
        rightSplitPane.setBorder(null);
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, navigatorView, rightSplitPane);
        mainSplitPane.setDividerLocation(180);
        mainSplitPane.setOneTouchExpandable(true);
        appFrame.getContentPane().add(mainSplitPane);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasNavigatorView() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public INavigatorView getNavigatorView() {
        return navigatorView;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasInfoView() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public IInfoView getInfoView() {
        return infoView;
    }

    /** {@inheritDoc} */
    @Override
    public ISceneView getSceneView() {
        return sceneView;
    }

    /** {@inheritDoc} */
    @Override
    public IPlotView getPlotView() {
        return plotView;
    }

    /** {@inheritDoc} */
    @Override
    public IValues edit(String title, IValues values) {
        GenericDialog dialog = dialogFactory.getGenericDialog(title, values);
        dialog.setModal(true);
        dialog.setVisible(true);
        return dialog.getResults();
    }

    /** {@inheritDoc} */
    @Override
    public IValues add(String string, IValues values) {
        return edit(string, values);
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasSceneView() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean hasPlotView() {
        return true;
    }
}
