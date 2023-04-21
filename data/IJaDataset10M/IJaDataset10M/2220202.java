package com.jmbaai.bombsight.app;

import gumbo.app.AppAnimator;
import gumbo.app.impl.AppAnimatorImpl;
import gumbo.config.ConfigManager;
import gumbo.core.deploy.JarClassLoader;
import gumbo.core.life.impl.DisposableImpl;
import gumbo.core.util.AssertUtils;
import gumbo.model.state.FrameClock;
import gumbo.model.state.impl.FrameClockImpl;
import com.jmbaai.bombsight.bomber.BomberControl;
import com.jmbaai.bombsight.bomber.BomberModel;
import com.jmbaai.bombsight.bomber.impl.BomberControlImpl;
import com.jmbaai.bombsight.bomber.impl.BomberModelImpl;
import com.jmbaai.bombsight.world.WorldModel;
import com.jmbaai.bombsight.world.impl.WorldModelImpl;

/**
 * Bomber bombsight simulation application. Configured via command line
 * arguments and property files (see ConfigManager).
 * @author jonb
 */
public class BomberApp extends DisposableImpl.IdentityEquality {

    /**
	 * Creates an instance. Assumes app properties have already been loaded.
	 * @param config Shared exposed config. Never null.
	 */
    public BomberApp(ConfigManager config) {
        AssertUtils.assertNonNullArg(config);
        FrameClock clock = new FrameClockImpl();
        WorldModel world = new WorldModelImpl(config);
        BomberModel bomber = new BomberModelImpl(config, world);
        BomberControlImpl bomberControl = new BomberControlImpl(config);
        AppControl control = new AppControl(config, bomberControl);
        AppAnimator animator;
        boolean isHeadless = false;
        if (isHeadless) {
            animator = new AppAnimatorImpl(new AppAnimatorImpl.Delegate(clock));
            animator.addAnimatable(world);
            animator.addAnimatable(bomber);
            control.lateBind(animator);
            bomberControl.lateBind(bomber);
        } else {
            BomberGui appGui = new BomberGui(config, APP_NAME, clock, control, world, bomber);
            animator = appGui.getAnimator();
            control.lateBind(animator);
            bomberControl.lateBind(appGui.getBomber());
        }
        control.tryAgain();
        animator.startAnimation();
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException ex) {
        }
    }

    public static final String APP_NAME = "BomberApp";

    public static void main(String[] args) {
        ConfigManager config = AppConfig.newInstance();
        config.reportProperties(System.out);
        config.debugWithProperties();
        new BomberApp(config);
    }

    /**
	 * Used as the main class entry in an executable jar, which contains the
	 * application, its jars, and its native libraries.
	 * @author jonb
	 * @see JarClassLoader
	 */
    public static class Launcher {

        public static void main(String[] args) {
            JarClassLoader jcl = new JarClassLoader();
            try {
                jcl.invokeMain("com.jmbaai.bombsight.app.BomberApp", args);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
