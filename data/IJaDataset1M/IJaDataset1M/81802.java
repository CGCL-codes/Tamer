package com.objetdirect.gwt.umlapi.client.artifacts;

import com.objetdirect.gwt.umlapi.client.helpers.OptionsManager;

/**
 * Just an interface to access easily to static constants used in the artifacts.
 * 
 * @author Rapha�l Brugier (raphael-dot-brugier.at.gmail'dot'com)
 */
public interface CommonConstants {

    static int REFLEXIVE_PATH_X_GAP = OptionsManager.get("ReflexivePathXGap");

    static int REFLEXIVE_PATH_Y_GAP = OptionsManager.get("ReflexivePathYGap");

    static int TEXT_TOP_PADDING = OptionsManager.get("TextTopPadding");

    static int TEXT_BOTTOM_PADDING = OptionsManager.get("TextBottomPadding");

    static int TEXT_LEFT_PADDING = OptionsManager.get("TextLeftPadding");

    static int TEXT_RIGHT_PADDING = OptionsManager.get("TextRightPadding");

    static int RECTANGLE_LEFT_PADDING = OptionsManager.get("RectangleLeftPadding");

    static int RECTANGLE_RIGHT_PADDING = OptionsManager.get("RectangleRightPadding");

    static int RECTANGLE_TOP_PADDING = OptionsManager.get("RectangleTopPadding");

    static int RECTANGLE_BOTTOM_PADDING = OptionsManager.get("RectangleBottomPadding");

    static int NOTE_CORNER_HEIGHT = OptionsManager.get("NoteCornerHeight");

    static int NOTE_CORNER_WIDTH = OptionsManager.get("NoteCornerWidth");
}
