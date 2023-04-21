package com.ivis.xprocess.ui.view.providers;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import com.ivis.xprocess.core.PossibleAnswer;
import com.ivis.xprocess.ui.util.FontAndColorManager;

public class AnswerTableLabelProvider implements ITableLabelProvider, ITableColorProvider, ITableFontProvider {

    public Image getColumnImage(Object element, int columnIndex) {
        return null;
    }

    public String getColumnText(Object element, int columnIndex) {
        if (element instanceof PossibleAnswer) {
            PossibleAnswer possibleAnswer = (PossibleAnswer) element;
            if (columnIndex == 0) {
                return "" + possibleAnswer.getNumber();
            }
            if (columnIndex == 1) {
                return possibleAnswer.getAnswerText();
            }
            if (columnIndex == 2) {
                return Boolean.toString(possibleAnswer.getPass());
            }
        }
        return "";
    }

    public void addListener(ILabelProviderListener listener) {
    }

    public void dispose() {
    }

    public boolean isLabelProperty(Object element, String property) {
        return false;
    }

    public void removeListener(ILabelProviderListener listener) {
    }

    public Color getForeground(Object element, int columnIndex) {
        return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
    }

    public Color getBackground(Object element, int columnIndex) {
        return FontAndColorManager.getInstance().getColor("255:255:255");
    }

    public Font getFont(Object element, int columnIndex) {
        return FontAndColorManager.getInstance().getEditorFont();
    }
}
