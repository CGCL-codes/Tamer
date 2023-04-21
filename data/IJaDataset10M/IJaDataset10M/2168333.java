package com.fh.auge.ui.views;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import com.fh.auge.core.security.StockGroup;
import com.fh.auge.ui.FontManager;

public class AbstractColumnLabelAdapter extends ColumnLabelProvider {

    public Color getForeground(Object element) {
        return super.getBackground(element);
    }

    public Font getFont(Object element) {
        if (element instanceof StockGroup) {
            return FontManager.getInstance().getGainFont();
        }
        return super.getFont(element);
    }
}

;
