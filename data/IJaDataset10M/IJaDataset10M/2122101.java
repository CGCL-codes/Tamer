package graphics;

import java.awt.event.*;
import java.util.Map;
import javax.swing.*;
import org.jvnet.substance.SubstanceLookAndFeel;
import org.jvnet.substance.api.*;
import org.jvnet.substance.colorscheme.*;
import org.jvnet.substance.painter.border.ClassicBorderPainter;
import org.jvnet.substance.painter.decoration.ArcDecorationPainter;
import org.jvnet.substance.painter.decoration.DecorationAreaType;
import org.jvnet.substance.painter.gradient.GlassGradientPainter;
import org.jvnet.substance.painter.highlight.GlassHighlightPainter;
import org.jvnet.substance.shaper.ClassicButtonShaper;
import org.jvnet.substance.skin.SkinInfo;

/**
 * Factory that creates menus for the test applications.
 * @author Kirill Grouchnikov
 */
public class SampleMenuFactory {

    /**
	 * Returns menu for setting skins.
	 * @return Menu for setting skins.
	 */
    public static JMenu getSkinMenu() {
        JMenu jmSkin = new JMenu("Skins");
        Map<String, SkinInfo> skinMap = SubstanceLookAndFeel.getAllSkins();
        for (final Map.Entry<String, SkinInfo> entry : skinMap.entrySet()) {
            JMenuItem jmiSkin = new JMenuItem(entry.getValue().getDisplayName());
            jmiSkin.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    SubstanceLookAndFeel.setSkin(entry.getValue().getClassName());
                }
            });
            jmSkin.add(jmiSkin);
        }
        jmSkin.addSeparator();
        final CustomSkin customSkin = new CustomSkin();
        JMenuItem jmiSkin = new JMenuItem(customSkin.getDisplayName());
        jmiSkin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SubstanceLookAndFeel.setSkin(customSkin);
            }
        });
        jmSkin.add(jmiSkin);
        return jmSkin;
    }

    protected static class CustomSkin extends SubstanceSkin {

        @Override
        public String getDisplayName() {
            return "Custom";
        }

        public CustomSkin() {
            SubstanceColorScheme activeScheme = new OrangeColorScheme().shade(0.2).invert();
            SubstanceColorScheme defaultScheme = new MetallicColorScheme();
            SubstanceColorScheme disabledScheme = new LightGrayColorScheme();
            SubstanceColorSchemeBundle defaultSchemeBundle = new SubstanceColorSchemeBundle(activeScheme, defaultScheme, disabledScheme);
            defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.6f, ComponentState.ROLLOVER_UNSELECTED);
            defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.SELECTED);
            defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.95f, ComponentState.ROLLOVER_SELECTED);
            defaultSchemeBundle.registerHighlightColorScheme(activeScheme, 0.8f, ComponentState.ARMED, ComponentState.ROLLOVER_ARMED);
            this.registerDecorationAreaSchemeBundle(defaultSchemeBundle, DecorationAreaType.NONE);
            SubstanceColorSchemeBundle headerSchemeBundle = new SubstanceColorSchemeBundle(activeScheme.saturate(0.3), activeScheme, disabledScheme);
            this.registerDecorationAreaSchemeBundle(headerSchemeBundle, headerSchemeBundle.getActiveColorScheme(), DecorationAreaType.PRIMARY_TITLE_PANE, DecorationAreaType.SECONDARY_TITLE_PANE, DecorationAreaType.HEADER);
            this.borderPainter = new ClassicBorderPainter();
            this.gradientPainter = new GlassGradientPainter();
            this.buttonShaper = new ClassicButtonShaper();
            this.decorationPainter = new ArcDecorationPainter();
            this.highlightPainter = new GlassHighlightPainter();
            this.selectedTabFadeStart = 1.0f;
            this.selectedTabFadeEnd = 1.0f;
        }
    }

    public static JMenu getLookAndFeelMenu(JFrame frame) {
        JMenu lafMenu = new JMenu("Look & feel");
        JMenu substanceMenus = new JMenu("Substance family");
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Business", "org.jvnet.substance.skin.SubstanceBusinessLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Business Black Steel", "org.jvnet.substance.skin.SubstanceBusinessBlackSteelLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Business Blue Steel", "org.jvnet.substance.skin.SubstanceBusinessBlueSteelLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Creme", "org.jvnet.substance.skin.SubstanceCremeLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Creme Coffee", "org.jvnet.substance.skin.SubstanceCremeCoffeeLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Moderate", "org.jvnet.substance.skin.SubstanceModerateLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Nebula", "org.jvnet.substance.skin.SubstanceNebulaLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Nebula Brick Wall", "org.jvnet.substance.skin.SubstanceNebulaBrickWallLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Office Silver 2007", "org.jvnet.substance.skin.SubstanceOfficeSilver2007LookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Sahara", "org.jvnet.substance.skin.SubstanceSaharaLookAndFeel"));
        substanceMenus.addSeparator();
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Field of Wheat", "org.jvnet.substance.skin.SubstanceFieldOfWheatLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Finding Nemo", "org.jvnet.substance.skin.SubstanceFindingNemoLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Green Magic", "org.jvnet.substance.skin.SubstanceGreenMagicLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Mango", "org.jvnet.substance.skin.SubstanceMangoLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Office Blue 2007", "org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel"));
        substanceMenus.addSeparator();
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Challenger Deep", "org.jvnet.substance.skin.SubstanceChallengerDeepLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Emerald Dusk", "org.jvnet.substance.skin.SubstanceEmeraldDuskLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Magma", "org.jvnet.substance.skin.SubstanceMagmaLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Raven", "org.jvnet.substance.skin.SubstanceRavenLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Raven Graphite", "org.jvnet.substance.skin.SubstanceRavenGraphiteLookAndFeel"));
        substanceMenus.add(SubstanceLafChanger.getMenuItem(frame, "Raven Graphite Glass", "org.jvnet.substance.skin.SubstanceRavenGraphiteGlassLookAndFeel"));
        lafMenu.add(substanceMenus);
        lafMenu.addSeparator();
        JMenu customLafMenus = new JMenu("Custom LAFs");
        lafMenu.add(customLafMenus);
        JMenu jgoodiesMenu = new JMenu("JGoodies family");
        customLafMenus.add(jgoodiesMenu);
        jgoodiesMenu.add(SubstanceLafChanger.getMenuItem(frame, "JGoodies Plastic", "com.jgoodies.looks.plastic.PlasticLookAndFeel"));
        jgoodiesMenu.add(SubstanceLafChanger.getMenuItem(frame, "JGoodies PlasticXP", "com.jgoodies.looks.plastic.PlasticXPLookAndFeel"));
        jgoodiesMenu.add(SubstanceLafChanger.getMenuItem(frame, "JGoodies Plastic3D", "com.jgoodies.looks.plastic.Plastic3DLookAndFeel"));
        jgoodiesMenu.add(SubstanceLafChanger.getMenuItem(frame, "JGoodies Windows", "com.jgoodies.looks.windows.WindowsLookAndFeel"));
        JMenu jtattooMenu = new JMenu("JTattoo family");
        customLafMenus.add(jtattooMenu);
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Acryl", "com.jtattoo.plaf.acryl.AcrylLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Aero", "com.jtattoo.plaf.aero.AeroLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Aluminium", "com.jtattoo.plaf.aluminium.AluminiumLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Bernstein", "com.jtattoo.plaf.bernstein.BernsteinLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Fast", "com.jtattoo.plaf.fast.FastLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo HiFi", "com.jtattoo.plaf.hifi.HiFiLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Luna", "com.jtattoo.plaf.luna.LunaLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo McWin", "com.jtattoo.plaf.mcwin.McWinLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Mint", "com.jtattoo.plaf.mint.MintLookAndFeel"));
        jtattooMenu.add(SubstanceLafChanger.getMenuItem(frame, "JTattoo Smart", "com.jtattoo.plaf.smart.SmartLookAndFeel"));
        JMenu syntheticaMenu = new JMenu("Synthetica family");
        customLafMenus.add(syntheticaMenu);
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica base", "de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica BlackMoon", "de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica BlackStar", "de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica BlueIce", "de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica BlueMoon", "de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica BlueSteel", "de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica GreenDream", "de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica MauveMetallic", "de.javasoft.plaf.synthetica.SyntheticaMauveMetallicLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica OrangeMetallic", "de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica SkyMetallic", "de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica SilverMoon", "de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel"));
        syntheticaMenu.add(SubstanceLafChanger.getMenuItem(frame, "Synthetica WhiteVision", "de.javasoft.plaf.synthetica.SyntheticaWhiteVisionLookAndFeel"));
        JMenu officeMenu = new JMenu("Office family");
        customLafMenus.add(officeMenu);
        officeMenu.add(SubstanceLafChanger.getMenuItem(frame, "Office 2003", "org.fife.plaf.Office2003.Office2003LookAndFeel"));
        officeMenu.add(SubstanceLafChanger.getMenuItem(frame, "Office XP", "org.fife.plaf.OfficeXP.OfficeXPLookAndFeel"));
        officeMenu.add(SubstanceLafChanger.getMenuItem(frame, "Visual Studio 2005", "org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "A03", "a03.swing.plaf.A03LookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Alloy", "com.incors.plaf.alloy.AlloyLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "EaSynth", "com.easynth.lookandfeel.EaSynthLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "FH", "com.shfarr.ui.plaf.fh.FhLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Hippo", "se.diod.hippo.plaf.HippoLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "InfoNode", "net.infonode.gui.laf.InfoNodeLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Kuntstoff", "com.incors.plaf.kunststoff.KunststoffLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Liquid", "com.birosoft.liquid.LiquidLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Lipstik", "com.lipstikLF.LipstikLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Metouia", "net.sourceforge.mlf.metouia.MetouiaLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Napkin", "net.sourceforge.napkinlaf.NapkinLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Nimbus", "org.jdesktop.swingx.plaf.nimbus.NimbusLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "NimROD", "com.nilo.plaf.nimrod.NimRODLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Oyoaha", "com.oyoaha.swing.plaf.oyoaha.OyoahaLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Pagosoft", "com.pagosoft.plaf.PgsLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Quaqua", "ch.randelshofer.quaqua.QuaquaLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Simple", "com.memoire.slaf.SlafLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Skin", "com.l2fprod.gui.plaf.skin.SkinLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Smooth Metal", "smooth.metal.SmoothLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Squareness", "net.beeger.squareness.SquarenessLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Tiny", "de.muntjak.tinylookandfeel.TinyLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Tonic", "com.digitprop.tonic.TonicLookAndFeel"));
        customLafMenus.add(SubstanceLafChanger.getMenuItem(frame, "Trendy", "com.Trendy.swing.plaf.TrendyLookAndFeel"));
        return lafMenu;
    }

    protected static class SkinChanger implements ActionListener {

        protected ColorSchemeTransform transform;

        protected String name;

        public SkinChanger(ColorSchemeTransform transform, String name) {
            super();
            this.transform = transform;
            this.name = name;
        }

        public void actionPerformed(ActionEvent e) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    SubstanceSkin newSkin = SubstanceLookAndFeel.getCurrentSkin(null).transform(transform, name);
                    SubstanceLookAndFeel.setSkin(newSkin);
                }
            });
        }
    }
}
