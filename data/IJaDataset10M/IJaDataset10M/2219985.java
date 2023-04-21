package com.jmbaai.bombsight.app;

import gov.nasa.worldwind.layers.Earth.MSVirtualEarthLayer;
import gumbo.app.GuiAnimator;
import gumbo.app.awt.AwtGuiManager;
import gumbo.app.awt.impl.AwtGuiManagerImpl;
import gumbo.app.awt.util.MainWindow;
import gumbo.app.impl.GuiAnimatorImpl;
import gumbo.core.life.impl.DisposableImpl;
import gumbo.core.util.AssertUtils;
import gumbo.model.state.FrameClock;
import javax.swing.JDialog;
import com.jmbaai.bombsight.bomber.BomberGraphic;
import com.jmbaai.bombsight.bomber.BomberView;
import com.jmbaai.bombsight.bomber.impl.BomberGraphicImpl;
import com.jmbaai.bombsight.graphic.layer.AtmosphereLayer;
import com.jmbaai.bombsight.graphic.layer.CompassHudLayer;
import com.jmbaai.bombsight.sight.norden.NordenSightView;
import com.jmbaai.bombsight.world.WorldGraphic;
import com.jmbaai.bombsight.world.impl.WorldGraphicImpl;

/**
 * Delegate for BomberApp that implements the optional support for a GUI, with
 * 3D views and interactive control of the bomber and bombsight simulation.
 * @author jonb
 */
public class XXXBomberGui extends DisposableImpl.IdentityEquality {

    /**
	 * Creates a new instance.
	 */
    public XXXBomberGui(BomberApp app, FrameClock frameClock) {
        AssertUtils.assertNonNullArg(app);
        AssertUtils.assertNonNullArg(frameClock);
        AwtGuiManager guiManager = new AwtGuiManagerImpl(app.getName());
        _animator = new GuiAnimatorImpl(new GuiAnimatorImpl.Delegate(frameClock, guiManager));
        WorldGraphic world = new WorldGraphicImpl(app.getConfig(), app.getWorld());
        world.getGlobalLayers().add(new AtmosphereLayer());
        world.getGlobalLayers().add(new MSVirtualEarthLayer());
        BomberGraphic bomber = new BomberGraphicImpl(app.getConfig(), world, app.getBomber());
        MainWindow rootWindow = guiManager.getRootWindow();
        BomberView pilotView = new BomberView(world, bomber, bomber.getPilotViewSpec());
        _animator.addAnimatable(pilotView);
        pilotView.getViewLayers().add(new CompassHudLayer(pilotView));
        rootWindow.setMainArea(pilotView.getCanvas());
        BomberView sightView = new NordenSightView(world, bomber, bomber.getSightViewSpec());
        _animator.addAnimatable(sightView);
        JDialog dialog = new JDialog();
        dialog.getContentPane().add(sightView.getCanvas());
        dialog.pack();
        dialog.setVisible(true);
        AppStatusBar statusBar = new AppStatusBar(world, bomber.getPhysics().getWind(), bomber.getPhysics(), bomber.getBombType());
        rootWindow.setStatusBar(statusBar);
        _animator.addAnimatable(statusBar);
        AppControl appControl = new AppControl(app);
        AppKeyboardInput appInput = new AppKeyboardInput();
        appInput.setAppControl(appControl);
        appInput.setBomberControl(bomber.getControl());
        appInput.addSource(rootWindow.getWindowGraphic());
        if (pilotView != null) appInput.addSource(pilotView.getCanvas());
        if (sightView != null) appInput.addSource(sightView.getCanvas());
        rootWindow.showWindow(true);
        rootWindow.getMainArea().requestFocusInWindow();
    }

    /**
	 * Gets the GUI animator.
	 * @return Shared exposed animator. Never null.
	 */
    public GuiAnimator getAnimator() {
        return _animator;
    }

    private GuiAnimator _animator;
}
