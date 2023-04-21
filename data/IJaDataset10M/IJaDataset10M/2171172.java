package visugraph.gui;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import visugraph.gui.elem.DefaultElementPool;
import visugraph.gui.elem.ElementPool;
import visugraph.gui.elem.GuiElement;
import visugraph.gui.elem.GuiElementListener;

/**
 * Cette classe permet d'afficher une JTable dont les opérations de rendu et d'édition sont déléguées
 * à des GuiElement.
 */
public class GuiElementTable extends JTable {

    private static final long serialVersionUID = 0x42;

    private ElementPool pool;

    public GuiElementTable(TableModel model) {
        this(model, DefaultElementPool.INSTANCE);
    }

    public GuiElementTable(TableModel model, ElementPool pool) {
        super(model);
        this.pool = pool;
        this.setAutoCreateRowSorter(true);
        this.registerRenderers();
    }

    private void registerRenderers() {
        for (int i = 0; i < this.getModel().getColumnCount(); i++) {
            Class<?> columnType = this.getModel().getColumnClass(i);
            BridgeCell bridge = new BridgeCell(columnType);
            this.setDefaultRenderer(columnType, bridge);
            this.setDefaultEditor(columnType, bridge);
        }
    }

    public void tableChanged(TableModelEvent e) {
        if (this.pool != null && e.getFirstRow() == TableModelEvent.HEADER_ROW) {
            this.registerRenderers();
        }
        super.tableChanged(e);
    }

    /**
	 * Classe qui joue le pont entre un GuiElement et TableCellRenderer.
	 */
    @SuppressWarnings("unchecked")
    private class BridgeCell extends AbstractCellEditor implements TableCellRenderer, TableCellEditor, GuiElementListener {

        private static final long serialVersionUID = 0x42;

        private GuiElement element;

        private JComponent editor;

        private JComponent renderer;

        public BridgeCell(Class<?> clType) {
            super();
            this.element = pool.getGuiElement(clType);
            this.renderer = this.element.createRenderer();
            this.renderer.setOpaque(true);
            this.editor = this.element.createEditor();
            this.editor.setOpaque(true);
            this.element.listenEditor(this.editor, this);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            this.element.setRendererValue(this.renderer, value);
            this.renderer.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
            this.renderer.setBorder(hasFocus ? BorderFactory.createLineBorder(table.getForeground(), 1) : BorderFactory.createEmptyBorder());
            return this.renderer;
        }

        public Object getCellEditorValue() {
            return this.element.getEditorValue(this.editor);
        }

        public boolean isCellEditable(EventObject anEvent) {
            if (anEvent instanceof MouseEvent) {
                return ((MouseEvent) anEvent).getClickCount() >= this.element.getClickCountToEdit();
            } else {
                return true;
            }
        }

        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.element.setEditorValue(this.editor, value);
            this.editor.setBackground(table.getSelectionBackground());
            return this.editor;
        }

        public boolean stopCellEditing() {
            try {
                int modRowIdx = convertRowIndexToModel(getEditingRow());
                int modColumnIdx = convertColumnIndexToModel(getEditingColumn());
                getModel().setValueAt(this.element.getEditorValue(this.editor), modRowIdx, modColumnIdx);
                return super.stopCellEditing();
            } catch (IllegalArgumentException e) {
                ErrorDialog.showError("Impossible de modifier la valeur de cette cellule", e);
                return false;
            }
        }

        public void editorChanged(GuiElement<?> element, JComponent editor) {
            if (isEditing()) {
                this.stopCellEditing();
            }
        }
    }
}
