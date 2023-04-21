package com.ynhenc.gis.ui.viewer_02.style_editor;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import com.ynhenc.gis.model.map.GisProject;
import com.ynhenc.gis.model.shape.Layer;
import com.ynhenc.gis.ui.viewer_01.map_viewer.MapViewer;
import com.ynhenc.gis.ui.viewer_03.dbf_viewer.JDbfGridTable;

public class ColListener_12_LayerEnabler extends ColListener {

    @Override
    public Object getValueAt(int row, int col, GisProject gisProject) {
        Layer layer = this.getLayer(row, gisProject);
        if (layer != null) {
            return layer.isLayerEnabled();
        } else {
            return false;
        }
    }

    @Override
    public void setValueAt(Object value, int row, int col, GisProject gisProject, MapViewer mapViewer, JTable jTable) {
        Layer layer = gisProject.getMapData_Base().getLayer(row);
        if (layer != null) {
            Boolean selected = (Boolean) value;
            layer.setLayerEnabled(selected);
            if (mapViewer.isShowing()) {
                this.repaintMapViewerAndStyleEditor(mapViewer);
            } else {
                mapViewer.createMapImageAgain();
            }
        }
    }

    @Override
    public TableCellRenderer getTableCellRenderer() {
        return null;
    }

    @Override
    public TableCellEditor getTableCellEditor() {
        return null;
    }

    public ColListener_12_LayerEnabler(String name, int width) {
        super(name, width, true);
    }
}
