package org.jaimz.spark.laf;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;
import org.jaimz.spark.borders.MarginBorder;
import org.jaimz.spark.borders.RoundRectBorder;

public class SparkLookAndFeel extends BasicLookAndFeel {

    private static final long serialVersionUID = 1L;

    public String getName() {
        return "Spark";
    }

    public String getID() {
        return "org.jaimz.spark";
    }

    public String getDescription() {
        return "The Spark look and feel.";
    }

    public boolean isNativeLookAndFeel() {
        return false;
    }

    public boolean isSupportedLookAndFeel() {
        return true;
    }

    protected void initComponentDefaults(UIDefaults table) {
        Color arcadeDark = new Color(0.0f, 0.0f, 0.0f);
        Color arcadeLight = new Color(0.4f, 0.4f, 0.4f);
        Color buttonDisabledColor = new Color(0.8f, 0.8f, 0.8f);
        Color controlDark = new Color(0.0f, 0.0f, 0.4f);
        Color controlLight = new Color(0.0f, 0.0f, 0.8f);
        Color controlLitDark = new Color(0.0f, 0.0f, 0.6f);
        Color controlLitLight = new Color(0.0f, 0.0f, 1.0f);
        Color controlDepressedDark = new Color(0.0f, 0.0f, 0.8f);
        Color controlDepressedLight = new Color(1.0f, 1.0f, 1.0f);
        Color buttonFocus = new Color(0.0f, 0.5f, 1.0f);
        Color barDark = new Color(0.2f, 0.2f, 0.2f);
        Color barLight = new Color(0.6f, 0.6f, 0.6f);
        Color barBorder = new Color(0.8f, 0.8f, 0.8f);
        Color glossHiColor = new Color(1.0f, 1.0f, 1.0f, 0.65f);
        Color glossLoColor = new Color(1.0f, 1.0f, 1.0f, 0.3f);
        Color borderColour = new Color(0.5f, 0.5f, 0.5f);
        Color paletteDark = new Color(0f, 0f, 0f, .8f);
        Color paletteLight = new Color(.4f, .4f, .4f, .8f);
        Color paletteCtrlLight = new Color(.9f, .9f, .9f);
        Color paletteCtrlDark = new Color(.6f, .6f, .6f);
        Color paletteBorderColor = new Color(.6f, .6f, .6f);
        Color splitPaneDividerColor = new Color(.6f, .6f, .6f);
        Object[] defaults = { "Panel.border", new BorderUIResource(new MarginBorder(2, 2, 2, 2)), "Arcade.border", new BorderUIResource(new MarginBorder(4, 4, 4, 4)), "DashBoard.border", new RoundRectBorder(borderColour, new Insets(8, 8, 8, 8)), "Palette.border", new BorderUIResource(new RoundRectBorder(paletteBorderColor, new Insets(8, 8, 8, 8))), "Spark.defaultBorderColour", new ColorUIResource(borderColour), "Arcade.darkColor", new ColorUIResource(arcadeDark), "Arcade.lightColor", new ColorUIResource(arcadeLight), "Spark.glossInset", new Integer(1), "Spark.glossExtent", new Float(0.5), "Spark.control.disabled", new ColorUIResource(buttonDisabledColor), "Spark.control.dark", new ColorUIResource(controlDark), "Spark.control.light", new ColorUIResource(controlLight), "Spark.control.lit.dark", new ColorUIResource(controlLitDark), "Spark.control.lit.light", new ColorUIResource(controlLitLight), "Spark.control.depressed.dark", new ColorUIResource(controlDepressedDark), "Spark.control.depressed.light", new ColorUIResource(controlDepressedLight), "Spark.bar.light", new ColorUIResource(barLight), "Spark.bar.dark", new ColorUIResource(barDark), "Spark.bar.border", new ColorUIResource(barBorder), "Spark.palette.dark", new ColorUIResource(paletteDark), "Spark.palette.light", new ColorUIResource(paletteLight), "Spark.palette.ctrl.dark", new ColorUIResource(paletteCtrlDark), "Spark.palette.ctrl.light", new ColorUIResource(paletteCtrlLight), "Button.border", new BorderUIResource(new MarginBorder(4, 14, 4, 14)), "Button.focus", new ColorUIResource(buttonFocus), "Button.foreground", new ColorUIResource(Color.WHITE), "SplitPane.dividerSize", new Integer(7), "SplitPaneDivider.border", new BorderUIResource(new RoundRectBorder(splitPaneDividerColor, new Insets(1, 1, 1, 1))), "Gloss.hiColor", new ColorUIResource(glossHiColor), "Gloss.lowColor", new ColorUIResource(glossLoColor) };
        table.putDefaults(defaults);
    }

    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        final String sparkLafPkgName = "org.jaimz.spark.laf.";
        Object[] uiDefaults = { "ArcadeUI", sparkLafPkgName + "ArcadeUI", "ButtonUI", sparkLafPkgName + "SparkButtonUI", "BarUI", sparkLafPkgName + "BarUI", "DashBoardUI", sparkLafPkgName + "DashBoardUI", "PaletteUI", sparkLafPkgName + "PaletteUI" };
        table.putDefaults(uiDefaults);
    }
}
