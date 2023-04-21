package com.ynhenc.gis.ui.viewer_02.style_editor;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import com.ynhenc.gis.model.map.GisProject;
import com.ynhenc.gis.model.map.MapDataBase;
import com.ynhenc.gis.model.shape.Layer;
import com.ynhenc.gis.ui.viewer_01.map_viewer.MapViewer;

public class ColListener_13_LayerType extends ColListener {

    @Override
    public Object getValueAt(int row, int col, GisProject gisProject) {
        Layer layer = this.getLayer(row, gisProject);
        if (layer != null) {
            return "" + layer.getLyrType();
        } else {
            return "";
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col, GisProject gisProject, MapViewer mapViewer, JTable jTable) {
    }

    @Override
    public TableCellRenderer getTableCellRenderer() {
        if (renderer == null) {
            renderer = new JLabelRenderer();
        }
        return this.renderer;
    }

    @Override
    public TableCellEditor getTableCellEditor() {
        return null;
    }

    public ColListener_13_LayerType(String name, int width) {
        super(name, width, false);
    }

    private TableCellRenderer renderer;
}
