package org.aiotrade.charting.laf;

import java.awt.Color;
import java.awt.Font;

/**
 *
 * @author Caoyuan Deng
 */
public class CityLights extends LookFeel {

    public CityLights() {
        monthColors = new Color[] { Color.cyan.darker().darker().darker(), Color.cyan.darker().darker(), Color.yellow.darker().darker().darker(), Color.red.darker().darker().darker(), Color.red.darker().darker(), Color.yellow.darker().darker().darker(), Color.white.darker().darker().darker(), Color.white.darker().darker(), Color.yellow.darker().darker().darker(), Color.magenta.darker().darker().darker(), Color.magenta.darker().darker(), Color.yellow.darker().darker().darker() };
        planetColors = new Color[] { Color.magenta.darker(), Color.white, Color.blue, Color.red, Color.cyan, Color.yellow, Color.orange.darker().darker(), Color.green.darker().darker(), Color.gray.darker().darker(), Color.blue };
        chartColors = new Color[] { Color.YELLOW, Color.BLUE, Color.WHITE.darker(), Color.GREEN, Color.RED, Color.CYAN, Color.YELLOW.darker(), Color.PINK, Color.LIGHT_GRAY, Color.MAGENTA, Color.DARK_GRAY };
        axisFont = new Font("Dialog Input", Font.PLAIN, 9);
        systemBackgroundColor = new Color(212, 208, 200);
        nameColor = Color.WHITE;
        backgroundColor = Color.BLACK;
        axisBackgroundColor = systemBackgroundColor;
        stickChartColor = Color.BLUE;
        positiveColor = Color.GREEN;
        negativeColor = Color.RED;
        positiveBgColor = Color.GREEN;
        negativeBgColor = Color.RED;
        borderColor = Color.RED;
        referCursorColor = new Color(0.0f, 1.0f, 1.0f, 0.382f);
        mouseCursorColor = Color.WHITE.darker();
        mouseCursorTextColor = Color.BLACK;
        mouseCursorTextBgColor = Color.YELLOW;
        referCursorTextColor = Color.WHITE;
        referCursorTextBgColor = referCursorColor.darker();
        drawingMasterColor = Color.white;
        drawingColor = Color.WHITE;
        drawingColorTransparent = new Color(0.0f, 0.0f, 1.f, 0.382f);
        handleColor = Color.WHITE;
        astrologyColor = Color.YELLOW;
    }
}
