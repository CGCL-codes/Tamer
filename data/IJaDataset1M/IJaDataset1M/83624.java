package toxTree.apps.toxForest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import toxTree.core.IDecisionCategory;
import toxTree.core.IDecisionCategoryEditor;
import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionResult;
import toxTree.exceptions.DecisionResultException;
import toxTree.tree.DecisionResultsList;
import toxTree.ui.tree.categories.CategoriesRenderer;
import toxtree.data.ActionList;
import toxtree.ui.DataModulePanel;
import toxtree.ui.tree.DecisionMethodsTableModel;
import toxtree.ui.tree.actions.EditDecisionMethodAction;

/**
 * Bottom panel of {@link ToxForestApp} (decision trees table).
 * @author Nina Jeliazkova nina@acad.bg
 * <b>Modified</b> Dec 16, 2006
 */
public class ToxForestDataModulePanel extends DataModulePanel {

    JTable table;

    EditDecisionMethodAction editAction;

    /**
	 * 
	 */
    private static final long serialVersionUID = -514823907951014642L;

    public ToxForestDataModulePanel(ToxForestDataModule dataModule) {
        super(dataModule);
        editAction = null;
    }

    public ToxForestDataModulePanel(ToxForestDataModule dataModule, boolean arg0) {
        super(dataModule, arg0);
        editAction = null;
    }

    public ToxForestDataModulePanel(ToxForestDataModule dataModule, LayoutManager arg0) {
        super(dataModule, arg0);
        editAction = null;
    }

    public ToxForestDataModulePanel(ToxForestDataModule dataModule, LayoutManager arg0, boolean arg1) {
        super(dataModule, arg0, arg1);
        editAction = null;
    }

    @Override
    protected void addWidgets(ActionList actions) {
        setLayout(new BorderLayout());
        TableModel model = new DecisionMethodsTableModel(((ToxForestDataModule) dataModule).getMethods());
        table = new JTable(model, createcolumns(model)) {

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                TableCellRenderer r = super.getCellRenderer(row, column);
                Object tip = ((DecisionMethodsTableModel) getModel()).getTooltip(column);
                if ((tip == null) || "".equals(tip)) tip = getValueAt(row, column);
                if (tip != null) setToolTipText(tip.toString()); else setToolTipText("");
                return r;
            }
        };
        ToolTipManager.sharedInstance().unregisterComponent(table);
        ToolTipManager.sharedInstance().unregisterComponent(table.getTableHeader());
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setPreferredScrollableViewportSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(true);
        table.setOpaque(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(32);
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() >= 1) {
                    Point p = e.getPoint();
                    DecisionResultsList methods = (DecisionResultsList) ((ToxForestDataModule) dataModule).getMethods();
                    int column = table.columnAtPoint(p);
                    int row = table.rowAtPoint(p);
                    switch(column) {
                        case 5:
                            {
                                try {
                                    ((ToxForestData) dataModule.getDataContainer()).classify(methods.getResult(row));
                                } catch (DecisionResultException x) {
                                    methods.getResult(row).setError(x.getMessage());
                                }
                                break;
                            }
                        case 3:
                            {
                                Object o = table.getValueAt(row, column);
                                if (o instanceof IDecisionCategory) {
                                    viewCategory(((IDecisionCategory) o));
                                }
                                break;
                            }
                        case 4:
                            {
                                viewVerboseResult(methods.getResult(row));
                                break;
                            }
                        case 2:
                            {
                                ((ToxForestDataModule) dataModule).setRules(methods.getMethod(row));
                                methods.getMethod(row).setEditable(true);
                                if (editAction == null) editAction = new EditDecisionMethodAction(dataModule);
                                editAction.actionPerformed(new ActionEvent(this, 0, ""));
                                break;
                            }
                        case 1:
                            {
                                removeMethod(methods, row);
                                break;
                            }
                    }
                }
            }
        });
        JScrollPane p = new JScrollPane(table);
        p.setAutoscrolls(true);
        p.setPreferredSize(new Dimension(400, 100));
        p.setMinimumSize(new Dimension(200, 100));
        p.setOpaque(true);
        add(p, BorderLayout.CENTER);
        JButton button = new JButton(((ToxForestDataModule) dataModule).getEstimateAction());
        add(button, BorderLayout.NORTH);
    }

    protected void removeMethod(DecisionResultsList methods, int row) {
        try {
            if (JOptionPane.showConfirmDialog(this, "Remove \"" + methods.getMethod(row).toString() + "\" ?", "Please confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) methods.remove(row);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }

    protected TableColumnModel createcolumns(TableModel model) {
        ColorTableCellRenderer r = new ColorTableCellRenderer();
        TableColumnModel columns = new DefaultTableColumnModel();
        for (int i = 0; i < model.getColumnCount(); i++) {
            TableColumn c = new TableColumn(i);
            c.setHeaderValue(model.getColumnName(i));
            if (i == 3) c.setCellRenderer(new CategoryTableCellRenderer()); else c.setCellRenderer(r);
            if (i == 0) c.setMaxWidth(32); else if (i == 1) c.setMaxWidth(48); else c.setMinWidth(64);
            columns.addColumn(c);
        }
        return columns;
    }

    protected void viewVerboseResult(IDecisionResult result) {
        try {
            JOptionPane.showMessageDialog(this, result.explain(true).toString());
        } catch (Exception x) {
        }
    }

    protected void viewCategory(IDecisionCategory category) {
        IDecisionCategoryEditor editor = category.getEditor();
        editor.setEditable(false);
        editor.edit(this, category);
    }

    public void update(Observable arg0, Object arg1) {
        setName(arg0.toString());
    }
}

class ColorTableCellRenderer extends DefaultTableCellRenderer {

    protected Color oddColor = new Color(240, 240, 240);

    public ColorTableCellRenderer() {
        super();
        setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) c.setBackground(table.getSelectionBackground()); else if ((row % 2) == 0) {
            c.setBackground(Color.white);
        } else c.setBackground(oddColor);
        return c;
    }
}

class CategoryTableCellRenderer extends ColorTableCellRenderer {

    protected Color oddColor = new Color(240, 240, 240);

    protected CategoriesRenderer crenderer;

    public CategoryTableCellRenderer() {
        super();
        crenderer = new CategoriesRenderer();
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if ((value != null) && (value instanceof IDecisionCategory)) try {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            IDecisionMethod method = (IDecisionMethod) table.getValueAt(row, 2);
            crenderer.setCategories(method.getCategories());
            c.setBackground(crenderer.getShowColor(((IDecisionCategory) value).getID() - 1));
            return c;
        } catch (Exception x) {
            x.printStackTrace();
        }
        return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    }
}
