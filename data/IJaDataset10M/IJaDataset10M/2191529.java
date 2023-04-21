package com.atech.graphics.components.jtreetable;

import java.awt.Component;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultTreeSelectionModel;

/**
 * * This file is part of ATech Tools library.
 * 
 * <one line to give the library's name and a brief idea of what it does.>
 * Copyright (C) 2007 Andy (Aleksander) Rozman (Atech-Software)
 * 
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * 
 * For additional information about this project please visit our project site
 * on http://atech-tools.sourceforge.net/ or contact us via this emails:
 * andyrozman@users.sourceforge.net or andy@atech-software.com
 * 
 * @author Andy
 */
public class JTreeTable extends JTable {

    private static final long serialVersionUID = 6274675444215143328L;

    /**
     * The tree.
     */
    protected TreeTableCellRenderer tree;

    private TreeTableModel treeTableModel;

    private boolean show_grid = false;

    /**
     * Instantiates a new j tree table.
     * 
     * @param treeTableModel the tree table model
     */
    public JTreeTable(TreeTableModel treeTableModel) {
        super();
        this.treeTableModel = treeTableModel;
        TreeTableCellRenderer tree2 = new TreeTableCellRenderer(treeTableModel, this);
        setTreeTableCellRenderer(tree2);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));
    }

    /** 
     * setShowGrid
     */
    public void setShowGrid(boolean show) {
        super.setShowGrid(show);
        this.show_grid = show;
    }

    /**
     * Gets the show grid.
     * 
     * @return the show grid
     */
    public boolean getShowGrid() {
        return this.show_grid;
    }

    /** 
     * getEditingRow
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? -1 : editingRow;
    }

    /**
     * Sets the tree table cell renderer.
     * 
     * @param renderer the new tree table cell renderer
     */
    public void setTreeTableCellRenderer(TreeTableCellRenderer renderer) {
        this.tree = renderer;
        super.setModel(new TreeTableModelAdapter(treeTableModel, tree));
        tree.setSelectionModel(new DefaultTreeSelectionModel() {

            private static final long serialVersionUID = 6706842894134298653L;

            {
                setSelectionModel(listSelectionModel);
            }
        });
        tree.setRowHeight(getRowHeight());
        if (this.show_grid) {
        }
        setDefaultRenderer(TreeTableModel.class, tree);
    }

    /**
     * Removes the icons.
     */
    public void removeIcons() {
    }

    /**
     * The Class TreeTableCellEditor.
     */
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {

        /** 
         * getTableCellEditorComponent
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int r, int c) {
            return tree;
        }
    }
}
