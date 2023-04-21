package com.certesystems.swingforms.gridset.combobox;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import org.apache.cayenne.Persistent;
import org.jdesktop.swingx.JXBusyLabel;
import com.certesystems.swingforms.Context;
import com.certesystems.swingforms.components.ActionButton;
import com.certesystems.swingforms.grid.Grid;
import com.certesystems.swingforms.gridset.GridSetComboBoxList;
import com.certesystems.swingforms.gridset.builders.GridSetBuilder;
import com.certesystems.swingforms.gridset.filters.FilterTableModel;
import com.certesystems.swingforms.tools.IconFactory;
import com.certesystems.swingforms.tools.SwingHelper;

public class GridSetComboBoxPanel extends JPanel implements Runnable, TableCellRenderer {

    private static final long serialVersionUID = 416404675692230649L;

    private GridSetBuilder gridSetBuilder;

    private GridSetComboBox gsCombo;

    private boolean nullEnabled;

    private Grid selectedItem;

    private FilterTableModel filterTableModel;

    private int tableX;

    private int tableY;

    private boolean enabled;

    private List<ActionListener> actionListeners = new ArrayList<ActionListener>();

    public boolean isEnabled() {
        return enabled;
    }

    public GridSetComboBoxPanel(GridSetBuilder gsb, boolean nullEnabled) {
        super(new BorderLayout());
        setOpaque(false);
        this.gridSetBuilder = gsb;
        this.nullEnabled = nullEnabled;
    }

    public synchronized Grid getSelectedItem() {
        if (gsCombo != null) {
            return gsCombo.getSelectedItem();
        } else {
            return selectedItem;
        }
    }

    public synchronized void setSelectedItem(Object o) {
        if (o instanceof Grid) {
            selectedItem = (Grid) o;
        } else if (o == null) {
            selectedItem = null;
        } else {
            try {
                selectedItem = gridSetBuilder.createGrid((Persistent) o);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        if (gsCombo != null) {
            gsCombo.setSelectedItem(selectedItem);
        }
    }

    public synchronized void setEnabled(boolean value) {
        this.enabled = value;
        removeAll();
        if (value) {
            if (filterTableModel == null && gridSetBuilder.getEntity().isInsertAllowed()) {
                ActionButton ab = new ActionButton(this, new ActionInsertComboBox());
                add(ab, BorderLayout.EAST);
            }
            new Thread(this).start();
        } else {
            JLabel label = new JLabel(getSelectedItem() == null ? "" : getSelectedItem().getDescription());
            label.setIcon(IconFactory.getInstance().getIcon(getSelectedItem() == null ? null : getSelectedItem().getIcon()));
            label.setBorder(BorderFactory.createEmptyBorder(2, 6, 2, 6));
            add(label, BorderLayout.CENTER);
            gsCombo = null;
        }
        Context.getContext().getDecorator().decorateGridSetComboBoxPanel(this);
        this.setVisible(false);
        this.setVisible(true);
        Container parent = getParent();
        if (parent != null) {
            parent.repaint();
        }
    }

    public GridSetBuilder getGridSetBuilder() {
        return gridSetBuilder;
    }

    public void setGridSetBuilder(GridSetBuilder gridSetBuilder) {
        this.gridSetBuilder = gridSetBuilder;
    }

    public void updateGrid(Grid grid) {
        if (gsCombo != null) {
            setEnabled(false);
            setSelectedItem(grid);
            setEnabled(true);
        }
    }

    private JXBusyLabel progress;

    public synchronized void run() {
        try {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    progress = SwingHelper.createBusyLabel();
                    add(progress, BorderLayout.WEST);
                    progress.setBorder(new EmptyBorder(0, 2, 0, 2));
                    progress.setBusy(true);
                }
            });
            final GridSetComboBoxList gscbl = gridSetBuilder.createGridSetComboBoxList();
            gscbl.refresh();
            Thread.sleep(200);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    Grid grid = getSelectedItem();
                    gsCombo = new GridSetComboBox(gscbl, nullEnabled);
                    for (ActionListener al : actionListeners) {
                        gsCombo.addActionListener(al);
                    }
                    gsCombo.setSelectedItem(grid);
                    progress.setBusy(false);
                    remove(progress);
                    add(gsCombo, BorderLayout.CENTER);
                    gsCombo.setVisible(false);
                    gsCombo.setVisible(true);
                    if (filterTableModel != null) {
                        filterTableModel.fireTableCellUpdated(tableX, tableY);
                    }
                }
            });
            Container parent = getParent();
            if (parent != null) {
                parent.repaint();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
        return null;
    }

    public FilterTableModel getFilterTableModel() {
        return filterTableModel;
    }

    public void setFilterTableModel(FilterTableModel filterTableModel) {
        this.filterTableModel = filterTableModel;
        setEnabled(true);
    }

    public int getTableX() {
        return tableX;
    }

    public void setTableX(int tableX) {
        this.tableX = tableX;
    }

    public int getTableY() {
        return tableY;
    }

    public void setTableY(int tableY) {
        this.tableY = tableY;
    }

    public void addActionListener(ActionListener al) {
        actionListeners.add(al);
    }

    public void removeActionListener(ActionListener al) {
        actionListeners.remove(al);
    }
}
