package com.ynhenc.gis.ui.viewer_02.style_editor;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import com.ynhenc.gis.ui.resource.IconImage;

public class Renderer_08_IconImage extends Renderer_00_Object {

    @Override
    public Icon getIcon(Object value) {
        if (value instanceof IconImage) {
            return new Icon_08_IconImage((IconImage) value);
        } else {
            return null;
        }
    }
}
