package org.makagiga.plugins;

import javax.swing.JDialog;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.metal.DefaultMetalTheme;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.plaf.metal.MetalTheme;
import javax.swing.plaf.metal.OceanTheme;
import org.makagiga.commons.Config;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.commons.Tuple;
import org.makagiga.commons.UI;
import org.makagiga.commons.icons.IconLoader;
import org.makagiga.commons.swing.MFrame;

/**
 * @since 3.0
 */
public class LookAndFeelPlugin extends Plugin<LookAndFeel> {

    private IconLoader iconLoader;

    private String lafClassName;

    static final String DEFAULT = "DEFAULT";

    /**
	 * The "Metal" LAF with modified "Ocean" theme.
	 */
    static final String METAL = "METAL";

    /**
	 * The "Nimbus" LAF.
	 */
    static final String NIMBUS = "NIMBUS";

    /**
	 * The LAF returned by the {@link UI#getPreferredLookAndFeelClassName()}.
	 */
    static final String SYSTEM = "SYSTEM";

    /**
	 * @since 4.0
	 */
    public static void applyLookAndFeel() {
        Tuple.Two<String, Boolean> abstractLAFConfig = readLAFConfig();
        if (abstractLAFConfig.get1().equals(DEFAULT)) {
            Config config = Config.getDefault();
            writeLAFConfig(config, abstractLAFConfig.get1(), abstractLAFConfig.get2());
        } else {
            JDialog.setDefaultLookAndFeelDecorated(false);
            MFrame.setDefaultLookAndFeelDecorated(false);
            Tuple.Two<String, Boolean> realLAFConfig = abstractLAFConfig;
            String className = realLAFConfig.get1();
            try {
                if (TK.isEmpty(className) || className.equals(METAL) || className.equals("JAVA")) {
                    setMetalLookAndFeel(new OceanTheme());
                } else if (className.equals(NIMBUS)) {
                    try {
                        UI.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                    } catch (Exception exception) {
                        MLogger.exception(exception);
                        setMetalLookAndFeel(new OceanTheme());
                    }
                } else if (className.equals(SYSTEM)) {
                    className = UI.getPreferredLookAndFeelClassName();
                    if (className.contains("MetalLookAndFeel")) setMetalLookAndFeel(new OceanTheme()); else UI.setLookAndFeel(className);
                } else if (className.contains("MetalLookAndFeel")) {
                    setMetalLookAndFeel(new DefaultMetalTheme());
                } else {
                    if (realLAFConfig.get2()) {
                        LookAndFeelPlugin plugin = findPluginForClassName(className);
                        if (plugin != null) UI.setLookAndFeel(plugin.create());
                    } else {
                        UI.setLookAndFeel(className);
                    }
                }
            } catch (Exception exception) {
                MLogger.exception(exception);
                MLogger.error("core", "Could not apply LAF: abstractLAFConfig=\"%s\", realLAFConfig=\"%s\"", abstractLAFConfig, realLAFConfig);
                setMetalLookAndFeel(new OceanTheme());
            }
        }
    }

    @Override
    public LookAndFeel create() throws Exception {
        return TK.newInstance(lafClassName);
    }

    public IconLoader getIconLoader() {
        return iconLoader;
    }

    public void setIconLoader(final IconLoader value) {
        iconLoader = value;
    }

    public String getIconThemeName() {
        return (iconLoader == null) ? null : iconLoader.getName();
    }

    public UIManager.LookAndFeelInfo getLookAndFeelInfo() {
        if (TK.isEmpty(lafClassName)) return null;
        return new UIManager.LookAndFeelInfo(getName(), lafClassName);
    }

    @Override
    public void onInit() throws PluginException {
        if (lafClassName == null) lafClassName = config.read("x.lafClassName", null);
    }

    /**
	 * @since 4.0
	 */
    protected void setLookAndFeelClassName(final String value) {
        lafClassName = value;
    }

    private static boolean setMetalLookAndFeel(final MetalTheme theme) {
        try {
            MetalLookAndFeel metal = new MetalLookAndFeel();
            MetalLookAndFeel.setCurrentTheme(theme);
            UI.setLookAndFeel(metal);
            return true;
        } catch (Exception exception) {
            MLogger.exception(exception);
            return false;
        }
    }

    static LookAndFeelPlugin findPluginForClassName(final String className) {
        for (PluginInfo i : PluginType.LOOK_AND_FEEL.get()) {
            LookAndFeelPlugin plugin = (LookAndFeelPlugin) i.getPlugin();
            UIManager.LookAndFeelInfo lafInfo = plugin.getLookAndFeelInfo();
            if ((lafInfo != null) && lafInfo.getClassName().equals(className)) return plugin;
        }
        return null;
    }

    static Tuple.Two<String, Boolean> readLAFConfig() {
        String defaultLAF = UI.getPreferredLookAndFeelClassName();
        if ("javax.swing.plaf.metal.MetalLookAndFeel".equals(defaultLAF)) defaultLAF = METAL;
        Config config = Config.getDefault();
        String className = config.read(Config.getPlatformKey("UI.lookAndFeel"), defaultLAF);
        boolean plugin = config.read(Config.getPlatformKey("UI.lookAndFeelPlugin"), false);
        return Tuple.of(className, plugin);
    }

    static void writeLAFConfig(final Config config, final String className, final boolean plugin) {
        config.write(Config.getPlatformKey("UI.lookAndFeel"), className);
        config.write(Config.getPlatformKey("UI.lookAndFeelPlugin"), plugin);
    }
}
