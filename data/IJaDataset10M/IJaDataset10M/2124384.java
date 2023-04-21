package com.hifi.plugin.ui.theme.metal.plaf.simplelist.impl;

import com.hifi.plugin.ui.components.smooth.list.ISmoothListCellRenderer;
import com.hifi.plugin.ui.components.smooth.list.ISmoothListPlaf;
import com.hifi.plugin.ui.components.smooth.list.editor.IListCellEditor;

public class SimpleSmoothListPlaf implements ISmoothListPlaf {

    private SimpleListCellRenderer renderer;

    private SimpleListCellEditor editor;

    public SimpleSmoothListPlaf() {
        renderer = new SimpleListCellRenderer();
        editor = new SimpleListCellEditor();
    }

    @Override
    public IListCellEditor getCellEditor() {
        return editor;
    }

    @Override
    public ISmoothListCellRenderer getCellRenderer() {
        return renderer;
    }
}
