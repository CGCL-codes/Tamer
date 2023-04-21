package com.chatserver.lookandfeel.jtattoo.acryl;

import java.awt.*;
import javax.swing.plaf.*;
import com.chatserver.lookandfeel.jtattoo.*;

public class AcrylDefaultTheme extends BaseTheme {

    public AcrylDefaultTheme() {
        super();
        setUp();
        loadProperties();
        setupColorArrs();
    }

    public String getPropertyFileName() {
        return "AcrylTheme.properties";
    }

    public static void setUp() {
        BaseTheme.setUp();
        menuOpaque = false;
        menuAlpha = 0.90f;
        backgroundColor = new ColorUIResource(244, 244, 244);
        selectionForegroundColor = white;
        selectionBackgroundColor = extraDarkGray;
        frameColor = new ColorUIResource(32, 32, 32);
        focusCellColor = focusColor;
        buttonBackgroundColor = extraLightGray;
        buttonColorLight = new ColorUIResource(244, 244, 244);
        buttonColorDark = new ColorUIResource(220, 220, 220);
        rolloverColor = new ColorUIResource(40, 106, 172);
        rolloverColorLight = new ColorUIResource(188, 252, 255);
        rolloverColorDark = new ColorUIResource(61, 134, 209);
        controlForegroundColor = black;
        controlBackgroundColor = backgroundColor;
        controlColorLight = new ColorUIResource(96, 98, 100);
        controlColorDark = new ColorUIResource(48, 49, 50);
        controlShadowColor = gray;
        controlDarkShadowColor = darkGray;
        windowTitleForegroundColor = white;
        windowTitleBackgroundColor = controlColorLight;
        windowTitleColorLight = controlColorLight;
        windowTitleColorDark = controlColorDark;
        windowBorderColor = new ColorUIResource(0, 0, 0);
        windowInactiveTitleBackgroundColor = new ColorUIResource(232, 232, 232);
        windowInactiveTitleColorLight = new ColorUIResource(236, 236, 236);
        windowInactiveTitleColorDark = new ColorUIResource(224, 224, 224);
        windowInactiveBorderColor = new ColorUIResource(212, 212, 212);
        menuBackgroundColor = backgroundColor;
        menuSelectionForegroundColor = white;
        menuSelectionBackgroundColor = extraDarkGray;
        menuColorLight = white;
        menuColorDark = backgroundColor;
        toolbarBackgroundColor = backgroundColor;
        toolbarColorLight = menuColorLight;
        toolbarColorDark = menuColorDark;
        desktopColor = backgroundColor;
    }

    public static void setupColorArrs() {
        BaseTheme.setupColorArrs();
        Color topHi = ColorHelper.brighter(controlColorLight, 20);
        Color topLo = ColorHelper.brighter(controlColorLight, 30);
        Color bottomHi = controlColorDark;
        Color bottomLo = controlColorLight;
        Color[] topColors = ColorHelper.createColorArr(topHi, topLo, 10);
        Color[] bottomColors = ColorHelper.createColorArr(bottomHi, bottomLo, 10);
        DEFAULT_COLORS = new Color[20];
        for (int i = 0; i < 10; i++) {
            DEFAULT_COLORS[i] = topColors[i];
            DEFAULT_COLORS[i + 10] = bottomColors[i];
        }
        ACTIVE_COLORS = DEFAULT_COLORS;
        topHi = ColorHelper.brighter(backgroundColor, 15);
        topLo = ColorHelper.darker(backgroundColor, 5);
        INACTIVE_COLORS = ColorHelper.createColorArr(topHi, topLo, 20);
        if (controlColorLight.equals(new ColorUIResource(96, 98, 100))) {
            ROLLOVER_COLORS = new Color[] { new Color(202, 207, 233), new Color(183, 195, 228), new Color(164, 182, 223), new Color(137, 169, 217), new Color(123, 169, 218), new Color(116, 167, 218), new Color(112, 166, 219), new Color(61, 134, 209), new Color(79, 150, 219), new Color(100, 165, 230), new Color(120, 180, 240), new Color(134, 193, 254), new Color(151, 211, 255), new Color(166, 226, 255), new Color(188, 252, 255) };
        } else {
            topHi = ColorHelper.brighter(rolloverColorLight, 20);
            topLo = ColorHelper.brighter(rolloverColorLight, 30);
            bottomHi = rolloverColorDark;
            bottomLo = rolloverColorLight;
            topColors = ColorHelper.createColorArr(topHi, topLo, 10);
            bottomColors = ColorHelper.createColorArr(bottomHi, bottomLo, 10);
            ROLLOVER_COLORS = new Color[20];
            for (int i = 0; i < 10; i++) {
                ROLLOVER_COLORS[i] = topColors[i];
                ROLLOVER_COLORS[i + 10] = bottomColors[i];
            }
        }
        SELECTED_COLORS = ColorHelper.createColorArr(new Color(200, 200, 200), new Color(240, 240, 240), 20);
        PRESSED_COLORS = SELECTED_COLORS;
        DISABLED_COLORS = ColorHelper.createColorArr(Color.white, new Color(230, 230, 230), 20);
        topHi = windowTitleColorLight;
        topLo = ColorHelper.brighter(windowTitleColorLight, 30);
        bottomHi = windowTitleColorDark;
        bottomLo = windowTitleColorLight;
        topColors = ColorHelper.createColorArr(topHi, topLo, 10);
        bottomColors = ColorHelper.createColorArr(bottomHi, bottomLo, 10);
        WINDOW_TITLE_COLORS = new Color[20];
        for (int i = 0; i < 10; i++) {
            WINDOW_TITLE_COLORS[i] = topColors[i];
            WINDOW_TITLE_COLORS[i + 10] = bottomColors[i];
        }
        WINDOW_INACTIVE_TITLE_COLORS = ColorHelper.createColorArr(windowInactiveTitleColorLight, windowInactiveTitleColorDark, 20);
        MENUBAR_COLORS = ColorHelper.createColorArr(menuColorLight, menuColorDark, 20);
        TOOLBAR_COLORS = ColorHelper.createColorArr(toolbarColorLight, toolbarColorDark, 20);
        topHi = ColorHelper.brighter(buttonColorLight, 20);
        topLo = ColorHelper.brighter(buttonColorLight, 80);
        bottomHi = buttonColorDark;
        bottomLo = buttonColorLight;
        topColors = ColorHelper.createColorArr(topHi, topLo, 10);
        bottomColors = ColorHelper.createColorArr(bottomHi, bottomLo, 10);
        BUTTON_COLORS = new Color[20];
        for (int i = 0; i < 10; i++) {
            BUTTON_COLORS[i] = topColors[i];
            BUTTON_COLORS[i + 10] = bottomColors[i];
        }
        TAB_COLORS = BUTTON_COLORS;
        COL_HEADER_COLORS = BUTTON_COLORS;
        TRACK_COLORS = ColorHelper.createColorArr(backgroundColor, Color.white, 16);
        THUMB_COLORS = DEFAULT_COLORS;
        SLIDER_COLORS = DEFAULT_COLORS;
        PROGRESSBAR_COLORS = DEFAULT_COLORS;
    }
}
