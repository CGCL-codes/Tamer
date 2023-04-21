package org.akrogen.tkui.dom.xhtml.internal.layouts.tables;

import org.akrogen.tkui.core.ui.layouts.IMigLayout;
import org.akrogen.tkui.dom.xhtml.layouts.tables.HTMLTableLayoutImpl;
import org.akrogen.tkui.dom.xhtml.layouts.tables.IHTMLTRLayout;

public class MigHTMLTableLayoutImpl extends HTMLTableLayoutImpl implements IMigLayout {

    private Object layoutConstraints;

    private Object columnConstraints;

    private Object rowConstraints;

    private boolean debugEnabled;

    private boolean fillx;

    public MigHTMLTableLayoutImpl() {
    }

    protected IHTMLTRLayout createTR(int rowIndex) {
        return new MigHTMLTRLayoutImpl(this, rowIndex);
    }

    public Object getLayoutConstraints() {
        StringBuffer layoutConstraints = new StringBuffer();
        layoutConstraints.append("gap -6");
        if (debugEnabled) layoutConstraints.append(",debug ");
        return layoutConstraints.toString();
    }

    public void setLayoutConstraints(Object layoutConstraints) {
        this.layoutConstraints = layoutConstraints;
    }

    public Object getColumnConstraints() {
        return columnConstraints;
    }

    public void setColumnConstraints(Object columnConstraints) {
        this.columnConstraints = columnConstraints;
    }

    public Object getRowConstraints() {
        return rowConstraints;
    }

    public void setRowConstraints(Object rowConstraints) {
        this.rowConstraints = rowConstraints;
    }

    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public boolean isFillx() {
        return fillx;
    }

    public void setFillx(boolean fillx) {
        this.fillx = fillx;
    }
}
