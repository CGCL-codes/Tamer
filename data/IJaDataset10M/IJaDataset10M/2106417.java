package com.emental.mindraider.ui.editors.color;

import java.awt.Color;
import com.emental.mindraider.MindRaiderConstants;

public class YellowAnnotationColorProfile implements AnnotationColorProfile {

    public String getLabel() {
        return "Yellow";
    }

    public String getUri() {
        return MindRaiderConstants.MR_RDF_URN + ":annotation:color-profiles:yellow";
    }

    public void fromXml() {
    }

    public void toXml() {
    }

    public Color getBackroundColor() {
        return new Color(0xfc, 0xff, 0xcc);
    }

    public Color getTextColor() {
        return Color.BLACK;
    }

    public Color getEnabledCaretColor() {
        return Color.RED;
    }

    public Color getDisabledCaretColor() {
        return Color.DARK_GRAY;
    }

    public Color getNormalLinkColor() {
        return new Color(0x00, 0x00, 0xff);
    }

    public Color getMindRaiderLinkColor() {
        return new Color(0x00, 0xff, 0x00);
    }

    public Color getTodoColor() {
        return Color.WHITE;
    }

    public Color getImportantTodoColor() {
        return Color.RED;
    }

    public Color getFinishedToDoColor() {
        return Color.DARK_GRAY;
    }

    public Color getSelectionColor() {
        return Color.RED;
    }

    public Color getSelectionTextColor() {
        return Color.WHITE;
    }
}
