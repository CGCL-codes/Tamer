package com.softaspects.jsf.renderer.table;

import com.softaspects.jsf.component.base.ComponentDefinitions;
import com.softaspects.jsf.renderer.base.RenderingUtils;
import com.softaspects.jsf.component.table.TableInterfaceManager;

public class CustomInterfaceManagerRenderer extends InterfaceManagerRenderer {

    protected String renderCellFunction(String aFunctionName, int aDefaultColor, int aAltColor, int aEditedColor, int aFocusedColor, int aSelectedColor, int aStep) throws java.io.IOException {
        StringBuffer sBuffer = new StringBuffer();
        sBuffer.append(initFunction(aFunctionName, "aSelected, aFocused, aEdited, aRowIndex, aColumnIndex"));
        sBuffer.append('{');
        sBuffer.append(RenderingUtils.indentString(1, "var colors = new Array();"));
        sBuffer.append(RenderingUtils.indentString(1, initColorsArray(TableInterfaceManager.PRIORITY_CELL_DEFAULT, aDefaultColor)));
        sBuffer.append(RenderingUtils.indentString(1, initColorsArray(TableInterfaceManager.PRIORITY_CELL_EDITED, aEditedColor)));
        if (aStep > 0 && aAltColor != ComponentDefinitions.COLOR_NOT_GIVEN) {
            sBuffer.append(NEW_LINE_CONSTANT);
            sBuffer.append("if (( ");
            sBuffer.append(getCompareToStep("aColumnIndex", aStep));
            sBuffer.append(" && ");
            sBuffer.append(getNotCompareToStep("aRowIndex", aStep));
            sBuffer.append(") || (");
            sBuffer.append(getNotCompareToStep("aColumnIndex", aStep));
            sBuffer.append(" && ");
            sBuffer.append(getCompareToStep("aRowIndex", aStep));
            sBuffer.append(")) {");
            sBuffer.append(initColorsArray(TableInterfaceManager.PRIORITY_CELL_DEFAULT, aAltColor));
            sBuffer.append(" }");
            sBuffer.append(NEW_LINE_CONSTANT);
        }
        sBuffer.append(RenderingUtils.indentString(1, initColorsArray(TableInterfaceManager.PRIORITY_CELL_FOCUSED, aFocusedColor)));
        sBuffer.append(RenderingUtils.indentString(1, initColorsArray(TableInterfaceManager.PRIORITY_CELL_SELECTED, aSelectedColor)));
        sBuffer.append(NEW_LINE_CONSTANT);
        sBuffer.append(RenderingUtils.indentString(1, initKeyVariable(TableInterfaceManager.PRIORITY_CELL_DEFAULT)));
        sBuffer.append(NEW_LINE_CONSTANT);
        sBuffer.append(RenderingUtils.indentString(1, ifValue("aEdited", TableInterfaceManager.PRIORITY_CELL_EDITED)));
        sBuffer.append(RenderingUtils.indentString(2, "else"));
        sBuffer.append(RenderingUtils.indentString(1, ifValue("aFocused", TableInterfaceManager.PRIORITY_CELL_FOCUSED)));
        sBuffer.append(RenderingUtils.indentString(2, "else"));
        sBuffer.append(RenderingUtils.indentString(1, ifValue("aSelected", TableInterfaceManager.PRIORITY_CELL_SELECTED)));
        sBuffer.append(NEW_LINE_CONSTANT);
        sBuffer.append(outWhile());
        sBuffer.append(outEndOfFunction());
        return sBuffer.toString();
    }
}
