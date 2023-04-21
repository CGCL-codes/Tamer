package org.one.stone.soup.xapp;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.metal.MetalTheme;
import org.one.stone.soup.grfx.ColorHelper;
import org.one.stone.soup.grfx.FontHelper;
import org.one.stone.soup.xml.XmlElement;

/**
 * @author nikcross
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class XappTheme extends MetalTheme {

    private XmlElement xTheme;

    public XappTheme(XmlElement theme) {
        xTheme = theme;
    }

    private ColorUIResource getColor(XmlElement source) {
        return new ColorUIResource(ColorHelper.htmlToColor(source.getValue()));
    }

    private FontUIResource getFont(XmlElement source) {
        return new FontUIResource(FontHelper.getFont(source.getAttributeValueByName("face"), source.getAttributeValueByName("style"), source.getAttributeValueByName("size")));
    }

    public String getName() {
        return xTheme.getAttributeValueByName("name");
    }

    protected ColorUIResource getPrimary1() {
        return getColor(xTheme.getElementByName("primaryColor1"));
    }

    protected ColorUIResource getPrimary2() {
        return getColor(xTheme.getElementByName("primaryColor2"));
    }

    protected ColorUIResource getPrimary3() {
        return getColor(xTheme.getElementByName("primaryColor3"));
    }

    protected ColorUIResource getSecondary1() {
        return getColor(xTheme.getElementByName("secondaryColor1"));
    }

    protected ColorUIResource getSecondary2() {
        return getColor(xTheme.getElementByName("secondaryColor2"));
    }

    protected ColorUIResource getSecondary3() {
        return getColor(xTheme.getElementByName("secondaryColor3"));
    }

    public FontUIResource getControlTextFont() {
        return getFont(xTheme.getElementByName("controlTextFont"));
    }

    public FontUIResource getMenuTextFont() {
        return getFont(xTheme.getElementByName("menuTextFont"));
    }

    public FontUIResource getSubTextFont() {
        return getFont(xTheme.getElementByName("subTextFont"));
    }

    public FontUIResource getSystemTextFont() {
        return getFont(xTheme.getElementByName("systemTextFont"));
    }

    public FontUIResource getUserTextFont() {
        return getFont(xTheme.getElementByName("userTextFont"));
    }

    public FontUIResource getWindowTitleFont() {
        return getFont(xTheme.getElementByName("windowTitleFont"));
    }

    public ColorUIResource getAcceleratorForeground() {
        return getColor(xTheme.getElementByName("accelerator").getElementByName("foreground"));
    }

    public ColorUIResource getAcceleratorSelectedForeground() {
        return getColor(xTheme.getElementByName("accelerator").getElementByName("selectedForeground"));
    }

    protected ColorUIResource getBlack() {
        return getColor(xTheme.getElementByName("black"));
    }

    public ColorUIResource getControl() {
        return getColor(xTheme.getElementByName("control").getElementByName("main"));
    }

    public ColorUIResource getControlDarkShadow() {
        return getColor(xTheme.getElementByName("control").getElementByName("darkShadow"));
    }

    public ColorUIResource getControlDisabled() {
        return getColor(xTheme.getElementByName("control").getElementByName("disabled"));
    }

    public ColorUIResource getControlHighlight() {
        return getColor(xTheme.getElementByName("control").getElementByName("highlight"));
    }

    public ColorUIResource getControlInfo() {
        return getColor(xTheme.getElementByName("control").getElementByName("info"));
    }

    public ColorUIResource getControlShadow() {
        return getColor(xTheme.getElementByName("control").getElementByName("shadow"));
    }

    public ColorUIResource getControlTextColor() {
        return getColor(xTheme.getElementByName("control").getElementByName("text"));
    }

    public ColorUIResource getDesktopColor() {
        return getColor(xTheme.getElementByName("desktop"));
    }

    public ColorUIResource getFocusColor() {
        return getColor(xTheme.getElementByName("focus"));
    }

    public ColorUIResource getHighlightedTextColor() {
        return getColor(xTheme.getElementByName("highlightedText"));
    }

    public ColorUIResource getInactiveControlTextColor() {
        return getColor(xTheme.getElementByName("inactiveControl"));
    }

    public ColorUIResource getInactiveSystemTextColor() {
        return getColor(xTheme.getElementByName("inactiveSystemText"));
    }

    public ColorUIResource getMenuBackground() {
        return getColor(xTheme.getElementByName("menu").getElementByName("background"));
    }

    public ColorUIResource getMenuDisabledForeground() {
        return getColor(xTheme.getElementByName("menu").getElementByName("disabledForeground"));
    }

    public ColorUIResource getMenuForeground() {
        return getColor(xTheme.getElementByName("menu").getElementByName("foreground"));
    }

    public ColorUIResource getMenuSelectedBackground() {
        return getColor(xTheme.getElementByName("menu").getElementByName("selectedBackground"));
    }

    public ColorUIResource getMenuSelectedForeground() {
        return getColor(xTheme.getElementByName("menu").getElementByName("selectedForeground"));
    }

    public ColorUIResource getPrimaryControl() {
        return getColor(xTheme.getElementByName("primaryControl").getElementByName("main"));
    }

    public ColorUIResource getPrimaryControlDarkShadow() {
        return getColor(xTheme.getElementByName("primaryControl").getElementByName("darkShadow"));
    }

    public ColorUIResource getPrimaryControlHighlight() {
        return getColor(xTheme.getElementByName("primaryControl").getElementByName("highlight"));
    }

    public ColorUIResource getPrimaryControlInfo() {
        return getColor(xTheme.getElementByName("primaryControl").getElementByName("info"));
    }

    public ColorUIResource getPrimaryControlShadow() {
        return getColor(xTheme.getElementByName("primaryControl").getElementByName("shadow"));
    }

    public ColorUIResource getSeparatorBackground() {
        return getColor(xTheme.getElementByName("separator").getElementByName("background"));
    }

    public ColorUIResource getSeparatorForeground() {
        return getColor(xTheme.getElementByName("separator").getElementByName("foreground"));
    }

    public ColorUIResource getSystemTextColor() {
        return getColor(xTheme.getElementByName("systemText"));
    }

    public ColorUIResource getTextHighlightColor() {
        return getColor(xTheme.getElementByName("textHighlight"));
    }

    public ColorUIResource getUserTextColor() {
        return getColor(xTheme.getElementByName("userText"));
    }

    protected ColorUIResource getWhite() {
        return getColor(xTheme.getElementByName("white"));
    }

    public ColorUIResource getWindowBackground() {
        return getColor(xTheme.getElementByName("windowBackground"));
    }

    public ColorUIResource getWindowTitleBackground() {
        return getColor(xTheme.getElementByName("windowTitle").getElementByName("background"));
    }

    public ColorUIResource getWindowTitleForeground() {
        return getColor(xTheme.getElementByName("windowTitle").getElementByName("foreground"));
    }

    public ColorUIResource getWindowTitleInactiveBackground() {
        return getColor(xTheme.getElementByName("windowTitle").getElementByName("inactiveBackground"));
    }

    public ColorUIResource getWindowTitleInactiveForeground() {
        return getColor(xTheme.getElementByName("windowTitle").getElementByName("inactiveForeground"));
    }
}
