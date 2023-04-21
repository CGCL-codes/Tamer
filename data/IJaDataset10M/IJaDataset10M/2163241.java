package net.hypotenubel.jaicwain.gui.plaf;

import java.awt.*;
import javax.swing.*;
import org.apache.log4j.Logger;
import net.hypotenubel.jaicwain.App;
import net.hypotenubel.jaicwain.gui.docking.DockingPanelContainer;
import net.hypotenubel.jaicwain.gui.plaf.kunststoff.*;
import net.hypotenubel.jaicwain.gui.plaf.liquid.*;
import net.hypotenubel.jaicwain.gui.plaf.looks.*;
import net.hypotenubel.jaicwain.gui.plaf.metal.*;
import net.hypotenubel.jaicwain.gui.plaf.napkin.*;
import net.hypotenubel.jaicwain.gui.plaf.pgs.*;
import net.hypotenubel.jaicwain.gui.plaf.system.*;
import net.hypotenubel.jaicwain.gui.swing.MainFrame;

/**
 * Keeps track of the available look and feels and provides the
 * means necessary to install a new look and feel.
 * 
 * @author Christoph Daniel Schulze
 * @version $Id: LookAndFeelManager.java 144 2006-10-03 00:20:14Z captainnuss $
 */
public class LookAndFeelManager {

    /**
     * Look and feel constant specifying the Kunststoff look and feel.
     */
    public static final int LF_KUNSTSTOFF = 0;

    /**
     * Look and feel constant specifying the Liquid look and feel.
     */
    public static final int LF_LIQUID = 1;

    /**
     * Look and feel constant specifying the Metal look and feel.
     */
    public static final int LF_METAL = 2;

    /**
     * Look and feel constant specifying the Napkin look and feel.
     */
    public static final int LF_NAPKIN = 3;

    /**
     * Look and feel constant specifying the Pgs look and feel.
     */
    public static final int LF_PGS = 4;

    /**
     * Look and feel constant specifying the JGoodies Plastic look and feel.
     */
    public static final int LF_PLASTIC = 5;

    /**
     * Look and feel constant specifying the JGoodies Plastic 3D look and feel.
     */
    public static final int LF_PLASTIC3D = 6;

    /**
     * Look and feel constant specifying the JGoodies Plastic XP look and feel.
     */
    public static final int LF_PLASTICXP = 7;

    /**
     * Look and feel constant specifying the System look and feel.
     */
    public static final int LF_SYSTEM = 8;

    /**
     * Array of installed {@code JaicWainLF}s.
     */
    private JaicWainLF[] lfs = null;

    /**
     * Index of the currently installed look and feel.
     */
    private int currentLF = 0;

    /**
     * Creates a new instance.
     */
    public LookAndFeelManager() {
        Logger.getLogger(LookAndFeelManager.class).info("Starting L&F subsystem...");
        lfs = new JaicWainLF[] { new KunststoffLF(), new LiquidLF(), new MetalLF(), new NapkinLF(), new PgsLF(), new PlasticLF(), new Plastic3DLF(), new PlasticXPLF(), new SystemLF() };
        int index = App.options.getIntOption("lf", "currentlf", LF_PLASTICXP);
        if ((index < 0) || (index >= lfs.length)) {
            index = LF_PLASTICXP;
        }
        installLookAndFeel(lfs[index]);
    }

    /**
     * Installs a new look and feel and updates all open windows.
     * 
     * @param lf {@code LookAndFeelManager} containing the new look and
     *           feel.
     * @return {@code boolean} indicating whether the new look and feel has
     *         been successfully installed ({@code true}) or not
     *         ({@code false}).
     */
    public boolean installLookAndFeel(JaicWainLF lf) {
        if (lf == null) return false;
        boolean valid = false;
        int index = 0;
        for (; index < lfs.length; index++) {
            if (lfs[index] == lf) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return false;
        }
        if (!lf.installLookAndFeel()) {
            return false;
        }
        currentLF = index;
        UIDefaults defaults = UIManager.getDefaults();
        defaults.remove("SplitPane.border");
        defaults.remove("SplitPaneDivider.border");
        if (App.gui != null) {
            MainFrame[] frames = App.gui.getMainFrames();
            DockingPanelContainer[] containers;
            for (int i = 0; i < frames.length; i++) {
                SwingUtilities.updateComponentTreeUI(frames[i]);
                containers = frames[i].getWindowManager().getContainers();
                for (int j = 0; j < containers.length; j++) {
                    if (containers[j] instanceof Component) {
                        SwingUtilities.updateComponentTreeUI((Component) containers[j]);
                        if (containers[j] instanceof Window) ((Window) containers[j]).pack();
                    }
                }
            }
        }
        App.options.setIntOption("lf", "currentlf", currentLF);
        Logger.getLogger(LookAndFeelManager.class).debug("Installed Look and Feel: " + lf.getName());
        return true;
    }

    /**
     * Returns the installed look and feels.
     * 
     * @return Array of installed {@code LookAndFeelManager}s.
     */
    public JaicWainLF[] getLookAndFeels() {
        return lfs;
    }

    /**
     * Returns the current look and feel.
     * 
     * @return {@code LookAndFeelManager} containing the current look and
     *         feel.
     */
    public JaicWainLF getCurrentLookAndFeel() {
        return lfs[currentLF];
    }
}
