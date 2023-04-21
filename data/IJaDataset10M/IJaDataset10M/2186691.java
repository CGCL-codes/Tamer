package org.coode.cardinality.ui;

import org.coode.cardinality.model.CardinalityRow;
import org.coode.cardinality.model.CardinalityTableModel;
import org.coode.cardinality.prefs.CardiPrefs;
import org.coode.cardinality.ui.celleditor.OWLClassExpressionCellEditor;
import org.coode.cardinality.ui.celleditor.OWLLiteralCellEditor;
import org.coode.cardinality.ui.celleditor.PropCombo;
import org.protege.editor.owl.OWLEditorKit;
import org.protege.editor.owl.ui.UIHelper;
import org.protege.editor.owl.ui.renderer.OWLCellRenderer;
import org.protege.editor.owl.ui.table.BasicLinkedOWLObjectTable;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLObject;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLProperty;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

/**
 * Author: Nick Drummond<br>
 * nick.drummond@cs.manchester.ac.uk<br>
 * http://www.cs.man.ac.uk/~drummond<br><br>
 * <p/>
 * The University Of Manchester<br>
 * Bio Health Informatics Group<br>
 * Date: Aug 30, 2006<br><br>
 * <p/>
 */
public class CardinalityTable extends BasicLinkedOWLObjectTable {

    private DefaultTableCellRenderer defaultRen;

    private OWLCellRenderer entityRen;

    private ClosureCellRenderer closureRen;

    private TableCellEditor defaultEditor;

    private TableCellEditor objPropEditor;

    private TableCellEditor descrFillerEditor;

    private TableCellEditor dataFillerEditor;

    private TableCellEditor dataPropEditor;

    private OWLLiteralCellEditor dataLiteralEditor;

    private PropCombo objPropCombo;

    private PropCombo dataPropCombo;

    private FocusAdapter defaultCellEditorFocusListener;

    private JTextField defaultCellEditorField;

    public CardinalityTable(OWLEditorKit eKit) {
        super(new CardinalityTableModel(eKit.getModelManager()), eKit);
        resizeColumns();
        showVerticalLines = false;
        showHorizontalLines = true;
        setGridColor(getSelectionBackground());
        getColumnModel().setColumnMargin(0);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        getTableHeader().setReorderingAllowed(false);
        createRenderers(eKit);
        createEditors(eKit);
    }

    private void resizeColumns() {
        TableColumnModel tcm = getColumnModel();
        for (int i = getModel().getColumnCount() - 1; i >= 0; i--) {
            TableColumn tc = tcm.getColumn(i);
            if (i == CardinalityTableModel.COL_FILLER) {
                int w = CardiPrefs.getInstance().getInt(CardiPrefs.FILLER_COL_WIDTH, 330);
                tc.setPreferredWidth(w);
            } else if (i == CardinalityTableModel.COL_PROP) {
                int w = CardiPrefs.getInstance().getInt(CardiPrefs.PROP_COL_WIDTH, 130);
                tc.setPreferredWidth(w);
            } else {
                tc.setMinWidth(30);
                tc.setMaxWidth(30);
            }
        }
    }

    private void createRenderers(OWLEditorKit eKit) {
        entityRen = new OWLCellRenderer(eKit) {

            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                JComponent c = (JComponent) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setOpaque(true);
                if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                } else {
                    c.setBackground(table.getBackground());
                }
                if (getRowHeight(row) < c.getPreferredSize().height + 2) {
                    setRowHeight(row, c.getPreferredSize().height + 2);
                }
                return c;
            }
        };
        entityRen.setHighlightKeywords(true);
        defaultRen = new DefaultTableCellRenderer();
        defaultRen.setVerticalAlignment(SwingConstants.TOP);
        defaultRen.setHorizontalAlignment(SwingConstants.RIGHT);
        closureRen = new ClosureCellRenderer(eKit.getModelManager());
        closureRen.setVerticalAlignment(SwingConstants.TOP);
        setRowHeight(Math.max(getRowHeight(), closureRen.getPreferredSize().height));
    }

    private void createEditors(OWLEditorKit eKit) {
        defaultCellEditorField = new JTextField();
        defaultCellEditorFocusListener = new FocusAdapter() {

            public void focusLost(FocusEvent e) {
                defaultEditor.cancelCellEditing();
            }
        };
        defaultCellEditorField.addFocusListener(defaultCellEditorFocusListener);
        defaultEditor = new DefaultCellEditor(defaultCellEditorField);
        defaultCellEditorField.setHorizontalAlignment(SwingConstants.RIGHT);
        objPropCombo = new PropCombo(eKit, PropCombo.OBJ_PROPS);
        objPropEditor = new DefaultCellEditor(objPropCombo) {

            public boolean isCellEditable(EventObject eventObject) {
                if (eventObject instanceof MouseEvent) {
                    return ((MouseEvent) eventObject).getClickCount() >= 2;
                } else {
                    return super.isCellEditable(eventObject);
                }
            }
        };
        dataPropCombo = new PropCombo(eKit, PropCombo.DATA_PROPS);
        dataPropEditor = new DefaultCellEditor(dataPropCombo) {

            public boolean isCellEditable(EventObject eventObject) {
                if (eventObject instanceof MouseEvent) {
                    return ((MouseEvent) eventObject).getClickCount() >= 2;
                } else {
                    return super.isCellEditable(eventObject);
                }
            }
        };
        descrFillerEditor = new OWLClassExpressionCellEditor(eKit);
        ((OWLClassExpressionCellEditor) descrFillerEditor).setExpandable(true);
        dataFillerEditor = new DefaultCellEditor(new UIHelper(eKit).getDatatypeSelector()) {

            public boolean isCellEditable(EventObject eventObject) {
                if (eventObject instanceof MouseEvent) {
                    return ((MouseEvent) eventObject).getClickCount() >= 2;
                } else {
                    return super.isCellEditable(eventObject);
                }
            }
        };
        dataLiteralEditor = new OWLLiteralCellEditor(new JTextField(), eKit.getModelManager());
    }

    public CardinalityTableModel getModel() {
        return (CardinalityTableModel) super.getModel();
    }

    public Set<CardinalityRow> getSelection() {
        Set<CardinalityRow> selection = new HashSet<CardinalityRow>();
        int minRow = getSelectionModel().getMinSelectionIndex();
        int maxRow = getSelectionModel().getMaxSelectionIndex();
        for (int i = minRow; i <= maxRow; i++) {
            if (getSelectionModel().isSelectedIndex(i)) {
                selection.add(getModel().getRow(i));
            }
        }
        return selection;
    }

    public void setSelection(CardinalityRow newRestr) {
        int rowIndex = getModel().getRow(newRestr);
        selectionModel.setSelectionInterval(rowIndex, rowIndex);
    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);
        if (c != null) {
            CardinalityRow r = getModel().getRow(row);
            c.setEnabled(!r.isReadOnly());
        }
        return c;
    }

    public TableCellRenderer getCellRenderer(int row, int col) {
        switch(col) {
            case CardinalityTableModel.COL_PROP:
            case CardinalityTableModel.COL_FILLER:
                entityRen.setInferred(getModel().getRow(row).isReadOnly());
                return entityRen;
            case CardinalityTableModel.COL_CLOSED:
                return closureRen;
            case CardinalityTableModel.COL_MIN:
            case CardinalityTableModel.COL_MAX:
            default:
                return defaultRen;
        }
    }

    public TableCellEditor getCellEditor(int row, int col) {
        switch(col) {
            case CardinalityTableModel.COL_PROP:
                OWLProperty p = (OWLProperty) getModel().getValueAt(row, CardinalityTableModel.COL_PROP);
                if (p instanceof OWLObjectProperty) {
                    return objPropEditor;
                } else {
                    return dataPropEditor;
                }
            case CardinalityTableModel.COL_FILLER:
                OWLProperty q = (OWLProperty) getModel().getValueAt(row, CardinalityTableModel.COL_PROP);
                if (q instanceof OWLObjectProperty) {
                    return descrFillerEditor;
                } else {
                    final OWLObject filler = getModel().getRow(row).getFiller();
                    if (filler instanceof OWLLiteral) {
                        return dataLiteralEditor;
                    } else {
                        return dataFillerEditor;
                    }
                }
            case CardinalityTableModel.COL_CLOSED:
                return super.getCellEditor(row, col);
            case CardinalityTableModel.COL_MIN:
            case CardinalityTableModel.COL_MAX:
            default:
                return defaultEditor;
        }
    }

    protected boolean isHeaderVisible() {
        return true;
    }

    public void dispose() {
        getModel().dispose();
        objPropCombo.dispose();
        dataPropCombo.dispose();
        defaultCellEditorField.removeFocusListener(defaultCellEditorFocusListener);
    }
}
