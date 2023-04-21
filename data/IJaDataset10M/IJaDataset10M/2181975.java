package org.openconcerto.sql.view.list;

import org.openconcerto.sql.Configuration;
import org.openconcerto.sql.model.SQLField;
import org.openconcerto.sql.model.SQLRow;
import org.openconcerto.sql.model.Where;
import org.openconcerto.sql.request.ComboSQLRequest;
import org.openconcerto.sql.sqlobject.ITextWithCompletion;
import org.openconcerto.sql.sqlobject.SelectionListener;
import org.openconcerto.ui.TextAreaRenderer;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.Types;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.swing.SwingUtilities;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.table.TableCellEditor;

public class AutoCompletionManager implements SelectionListener {

    private SQLTableElement fromTableElement;

    private RowValuesTable table;

    private TextTableCellEditorWithCompletion editor;

    private int lastSelectedComboId = 1;

    protected boolean optOk, foreign;

    protected boolean disableCompletion;

    private SQLField fillFrom;

    private RowValuesTableModel tableModel;

    ITextWithCompletion t;

    SQLTextComboTableCellEditor textComboCellEdit;

    public AutoCompletionManager(SQLTableElement fromTableElement, SQLField fillFrom, RowValuesTable table, RowValuesTableModel tableModel) {
        this(fromTableElement, fillFrom, table, tableModel, ITextWithCompletion.MODE_CONTAINS, false);
    }

    public AutoCompletionManager(SQLTableElement fromTableElement, SQLField fillFrom, RowValuesTable table, RowValuesTableModel tableModel, int modeCompletion, boolean expandWithShowAs, boolean foreign) {
        this.foreign = foreign;
        List<String> l = new Vector<String>();
        if (expandWithShowAs) {
            List<SQLField> lSQLFields = Configuration.getInstance().getShowAs().getFieldExpand(fillFrom.getTable());
            for (int i = 0; i < lSQLFields.size(); i++) {
                l.add(lSQLFields.get(i).getName());
            }
        } else {
            l.add(fillFrom.getName());
        }
        ComboSQLRequest req = new ComboSQLRequest(fillFrom.getTable(), l);
        init(fromTableElement, fillFrom, table, tableModel, modeCompletion, req, foreign);
    }

    public AutoCompletionManager(SQLTableElement fromTableElement, SQLField fillFrom, RowValuesTable table, RowValuesTableModel tableModel, int modeCompletion, boolean expandWithShowAs) {
        this(fromTableElement, fillFrom, table, tableModel, modeCompletion, expandWithShowAs, false);
    }

    public AutoCompletionManager(SQLTableElement fromTableElement, SQLField fillFrom, RowValuesTable table, RowValuesTableModel tableModel, int modeCompletion, ComboSQLRequest req) {
        init(fromTableElement, fillFrom, table, tableModel, modeCompletion, req, false);
    }

    public void init(SQLTableElement fromTableElement, SQLField fillFrom, RowValuesTable table, RowValuesTableModel tableModel, int modeCompletion, ComboSQLRequest req, boolean foreign) {
        this.tableModel = tableModel;
        this.fromTableElement = fromTableElement;
        this.fillFrom = fillFrom;
        this.table = table;
        if (foreign) {
            TableCellEditor cellEdit = this.fromTableElement.getTableCellEditor(null);
            if (cellEdit instanceof SQLTextComboTableCellEditor) {
                this.textComboCellEdit = (SQLTextComboTableCellEditor) cellEdit;
                textComboCellEdit.addSelectionListener(new PropertyChangeListener() {

                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        int i = textComboCellEdit.getComboSelectedId();
                        if (AutoCompletionManager.this.lastSelectedComboId != i) {
                            idSelected(i, null);
                        }
                        AutoCompletionManager.this.lastSelectedComboId = i;
                        System.err.println("editing stopped");
                    }
                });
                textComboCellEdit.addCellEditorListener(new CellEditorListener() {

                    @Override
                    public void editingCanceled(ChangeEvent e) {
                        AutoCompletionManager.this.lastSelectedComboId = textComboCellEdit.getComboSelectedId();
                    }

                    @Override
                    public void editingStopped(ChangeEvent e) {
                        AutoCompletionManager.this.lastSelectedComboId = textComboCellEdit.getComboSelectedId();
                    }
                });
            }
        } else {
            this.t = new ITextWithCompletion(req, true);
            this.t.setModeCompletion(modeCompletion);
            this.editor = new TextTableCellEditorWithCompletion(table, this.t);
            if (this.fillFrom.getType().getType() == Types.VARCHAR) {
                this.t.setLimitedSize(this.fillFrom.getType().getSize());
                this.editor.setLimitedSize(this.fillFrom.getType().getSize());
            }
            this.fromTableElement.setEditor(this.editor);
            this.fromTableElement.setRenderer(new TextAreaRenderer());
            this.t.addSelectionListener(this);
        }
    }

    private HashMap<String, String> fillBy = new HashMap<String, String>();

    private int lastId = -1;

    private int lastEditingRow = -1;

    public void fill(String string, String string2) {
        this.fillBy.put(string, string2);
    }

    public void idSelected(final int id, Object source) {
        final int rowE = this.table.getEditingRow();
        if (rowE < 0) {
            this.lastEditingRow = rowE;
            return;
        }
        if (this.lastEditingRow == rowE && this.lastId == id) {
            return;
        }
        this.lastId = id;
        this.lastEditingRow = rowE;
        if (id > 1) {
            if (this.table.getCellEditor() != null && !this.foreign) {
                this.table.getCellEditor().stopCellEditing();
            }
            new Thread(new Runnable() {

                public void run() {
                    final SQLRow rowV = AutoCompletionManager.this.fillFrom.getTable().getRow(id);
                    final Set<String> keys = AutoCompletionManager.this.fillBy.keySet();
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            for (Iterator<String> iter = keys.iterator(); iter.hasNext(); ) {
                                String from = iter.next();
                                String to = AutoCompletionManager.this.fillBy.get(from);
                                Object fromV = getValueFrom(rowV, from);
                                int column = AutoCompletionManager.this.tableModel.getColumnForField(to);
                                if (column >= 0) {
                                    if (!AutoCompletionManager.this.table.getRowValuesTableModel().getValueAt(rowE, column).equals(fromV)) {
                                        AutoCompletionManager.this.table.getRowValuesTableModel().setValueAt(fromV, rowE, column);
                                        if (AutoCompletionManager.this.table.getEditingColumn() == column && AutoCompletionManager.this.table.getEditingRow() == rowE) {
                                            AutoCompletionManager.this.table.editingCanceled(null);
                                            AutoCompletionManager.this.table.setColumnSelectionInterval(column, column);
                                            AutoCompletionManager.this.table.setRowSelectionInterval(rowE, rowE);
                                            if (AutoCompletionManager.this.table.editCellAt(rowE, column)) {
                                                if (AutoCompletionManager.this.table.getEditorComponent() != null) {
                                                    AutoCompletionManager.this.table.getEditorComponent().requestFocusInWindow();
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    AutoCompletionManager.this.table.getRowValuesTableModel().putValue(fromV, rowE, to);
                                }
                            }
                            AutoCompletionManager.this.table.resizeAndRepaint();
                        }
                    });
                }
            }).start();
        }
    }

    protected Object getValueFrom(SQLRow row, String field) {
        return row.getObject(field);
    }

    public void setWhere(Where w) {
        if (this.t != null) {
            this.t.setWhere(w);
        } else {
            this.textComboCellEdit.setWhere(w);
        }
    }

    public void setFillWithField(String s) {
        this.t.setFillWithField(s);
    }
}
