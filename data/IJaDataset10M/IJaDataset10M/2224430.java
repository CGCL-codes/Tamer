package prajna.geo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;
import prajna.gui.ColorCellEditor;
import prajna.gui.FloatCellEditor;

/**
 * Provides a GUI utility to edit the display of geographic layers. This utiliy
 * allows a user to edit the draw and fill colors of a geographic layer,
 * whether a particular layer is pickable, and set the layer transparency. It
 * also allows a user to alter the order in which the geographic layers are
 * drawn. It uses a frame and an internal table to represent the various
 * characteristics of each geographic layer.
 * 
 * @author <a href="http://www.ganae.com/edswing">Edward Swing</a>
 */
public class GeoLayerEditor extends JFrame {

    private static final long serialVersionUID = 4082354474004078436L;

    private List<GeoLayer> layerList;

    private GeoLayerTableModel model = new GeoLayerTableModel();

    private JTable layerTable = new JTable(model);

    private HashSet<ChangeListener> listeners = new HashSet<ChangeListener>();

    /**
     * This table model represents the table for the geographic layer
     * properties. The table includes layer name, pickability, draw color, fill
     * color, and transparency.
     */
    private class GeoLayerTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -4784921373419007774L;

        List<GeoLayer> layerData = new ArrayList<GeoLayer>();

        /**
         * Get the column class for the geographic layer table
         * 
         * @param columnIndex the column index
         * @return the class for the column
         */
        @Override
        public Class<?> getColumnClass(int columnIndex) {
            Class<?> cls = null;
            if (columnIndex == 0) {
                cls = String.class;
            } else if (columnIndex == 1) {
                cls = Boolean.class;
            } else if (columnIndex == 2 || columnIndex == 3) {
                cls = Color.class;
            } else if (columnIndex == 4) {
                cls = Float.class;
            }
            return cls;
        }

        /**
         * Get the number of columns
         * 
         * @return 5
         */
        public int getColumnCount() {
            return 5;
        }

        /**
         * Get the name for the column
         * 
         * @param columnIndex the column index
         * @return the name for the column
         */
        @Override
        public String getColumnName(int columnIndex) {
            String name = "unknown";
            if (columnIndex == 0) {
                name = "Name";
            } else if (columnIndex == 1) {
                name = "Pickable";
            } else if (columnIndex == 2) {
                name = "Draw Color";
            } else if (columnIndex == 3) {
                name = "Fill Color";
            } else if (columnIndex == 4) {
                name = "Transparency";
            }
            return name;
        }

        /**
         * Get the number of rows. This corresponds to the number of layers in
         * the Geographic Layer editor.
         * 
         * @return the number of rows.
         */
        public int getRowCount() {
            return layerData.size();
        }

        /**
         * Get the value at a particular table cell
         * 
         * @param row the row number
         * @param col the column number
         * @return a value for the table cell
         */
        public Object getValueAt(int row, int col) {
            GeoLayer layer = layerData.get(layerData.size() - row - 1);
            if (col == 0) {
                return layer.getName();
            } else if (col == 1) {
                return new Boolean(layer.isPickable());
            } else if (col == 2) {
                Color draw = layer.getDrawColor();
                if (draw == null) {
                    draw = Color.RED;
                }
                return draw;
            } else if (col == 3) {
                Color fill = layer.getFillColor();
                if (fill == null) {
                    fill = Color.YELLOW;
                }
                return fill;
            } else if (col == 4) {
                Color fill = layer.getFillColor();
                float alpha = (fill == null) ? 0.0f : (float) layer.getFillColor().getAlpha() / 255;
                return new Float(alpha);
            }
            return null;
        }

        /**
         * Return whether a cell is editable. All cells except those in the
         * first column, which holds the name, are editable
         * 
         * @param row the row number
         * @param col the column number
         * @return true if the cell is editable.
         */
        @Override
        public boolean isCellEditable(int row, int col) {
            return (col != 0);
        }

        /**
         * Sets the data for the table model
         * 
         * @param layers the new data
         */
        public void setData(List<GeoLayer> layers) {
            layerData = layers;
        }

        /**
         * Set the value at a particular cell
         * 
         * @param val the new value
         * @param row the row number
         * @param col the column number
         */
        @Override
        public void setValueAt(Object val, int row, int col) {
            GeoLayer layer = layerData.get(layerData.size() - row - 1);
            if (val instanceof Boolean && col == 1) {
                layer.setPickable(((Boolean) val).booleanValue());
            } else if (val instanceof Color && col == 2) {
                layer.setDrawColor((Color) val);
                fireStateChanged();
            } else if (val instanceof Color && col == 3) {
                Color color = (Color) val;
                Color fillCol = layer.getFillColor();
                fillCol = new Color(color.getRed(), color.getGreen(), color.getBlue(), fillCol.getAlpha());
                layer.setFillColor(fillCol);
                fireStateChanged();
            } else if (val instanceof Float && col == 4) {
                Color fillCol = layer.getFillColor();
                int alpha = Math.round(((Float) val).floatValue() * 255);
                fillCol = new Color(fillCol.getRed(), fillCol.getGreen(), fillCol.getBlue(), alpha);
                layer.setFillColor(fillCol);
                fireStateChanged();
            }
        }
    }

    /**
     * Instantiates a new geographic layer editor.
     */
    public GeoLayerEditor() {
        setLayout(new BorderLayout());
        add(layerTable);
        layerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JPanel butnPanel = new JPanel(new GridLayout(1, 0));
        JButton upButn = new JButton("Up");
        JButton downButn = new JButton("Down");
        JButton closeButn = new JButton("Close");
        add(butnPanel, BorderLayout.SOUTH);
        add(layerTable.getTableHeader(), BorderLayout.NORTH);
        ActionListener moveListener = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                String butn = evt.getActionCommand();
                moveLayer(butn.equals("Up"));
            }
        };
        upButn.addActionListener(moveListener);
        downButn.addActionListener(moveListener);
        closeButn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setVisible(false);
            }
        });
        butnPanel.add(upButn);
        butnPanel.add(downButn);
        butnPanel.add(closeButn);
        layerTable.setDefaultRenderer(Color.class, new ColorCellEditor());
        layerTable.setDefaultEditor(Color.class, new ColorCellEditor());
        layerTable.setDefaultRenderer(Float.class, new FloatCellEditor());
        layerTable.setDefaultEditor(Float.class, new FloatCellEditor());
        pack();
    }

    /**
     * Adds a change listener. These ChangeListeners will be triggered whenever
     * one of the table cells is changed, or when the ordering of the layers is
     * changed.
     * 
     * @param listener the change listener
     */
    public void addChangeListener(ChangeListener listener) {
        listeners.add(listener);
    }

    /**
     * Fire change events to all listeners
     */
    private void fireStateChanged() {
        ChangeEvent evt = new ChangeEvent(this);
        for (ChangeListener listener : listeners) {
            listener.stateChanged(evt);
        }
    }

    /**
     * Gets the list of layers, in display order
     * 
     * @return the layers
     */
    public List<GeoLayer> getLayers() {
        return layerList;
    }

    /**
     * Move the relative position of one of the layers. This call will move the
     * currently selected layer either up or down one level, based upon the
     * argument.
     * 
     * @param moveUp whether to move the layer up (true) or down (false).
     */
    private void moveLayer(boolean moveUp) {
        int layerInx = layerTable.getSelectedRow();
        if (layerInx != -1) {
            int layerPick = layerList.size() - layerInx - 1;
            if (moveUp && layerPick != layerList.size() - 1) {
                GeoLayer selLayer = layerList.remove(layerPick);
                layerList.add(layerPick + 1, selLayer);
                model.setData(layerList);
                layerTable.getSelectionModel().setLeadSelectionIndex(layerInx - 1);
                layerTable.repaint();
            } else if (!moveUp && layerPick != 0) {
                GeoLayer selLayer = layerList.remove(layerPick);
                layerList.add(layerPick - 1, selLayer);
                model.setData(layerList);
                layerTable.getSelectionModel().setLeadSelectionIndex(layerInx + 1);
                layerTable.repaint();
            }
        }
        fireStateChanged();
    }

    /**
     * Removes the change listener from this editor
     * 
     * @param listener the listener
     */
    public void removeChangeListener(ChangeListener listener) {
        listeners.remove(listener);
    }

    /**
     * Sets the list of layers.
     * 
     * @param layers the new layers
     */
    public void setLayers(List<GeoLayer> layers) {
        layerList = layers;
        model.setData(layerList);
        pack();
    }
}
