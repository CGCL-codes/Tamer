package org.goet.kits;

import org.goet.datamodel.*;
import java.awt.*;

public class FontChangeItem extends MacroHistoryItem {

    public FontChangeItem(Node labelNode, Font font) {
        NodePropertyValue npv_face = NodePropertyValue.getSingleNPV(labelNode, SCHEMA.hasFontFace);
        NodePropertyValue npv_size = NodePropertyValue.getSingleNPV(labelNode, SCHEMA.hasFontSize);
        String faceString = font.getName();
        if (font.isBold()) {
            faceString += " Bold";
        }
        if (font.isItalic()) {
            faceString += " Italic";
        }
        Value new_face = BasicTypes.STRING_TYPE.getObject(faceString);
        Value new_size = BasicTypes.INTEGER_TYPE.getObject(font.getSize());
        addItem(new TextEditHistoryItem(npv_face, new_face));
        addItem(new TextEditHistoryItem(npv_size, new_size));
    }
}
