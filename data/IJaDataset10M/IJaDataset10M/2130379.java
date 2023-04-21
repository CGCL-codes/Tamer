package org.homeunix.thecave.buddi.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import org.homeunix.thecave.buddi.Const;
import org.homeunix.thecave.buddi.i18n.BuddiKeys;
import org.homeunix.thecave.buddi.i18n.keys.BudgetCategoryTypes;
import org.homeunix.thecave.buddi.model.BudgetCategory;
import org.homeunix.thecave.buddi.model.Document;
import org.homeunix.thecave.buddi.model.impl.FilteredLists;
import org.homeunix.thecave.buddi.model.impl.ModelFactory;
import org.homeunix.thecave.buddi.model.swing.BudgetDateSpinnerModel;
import org.homeunix.thecave.buddi.model.swing.MyBudgetTreeTableModel;
import org.homeunix.thecave.buddi.plugin.api.util.TextFormatter;
import org.homeunix.thecave.buddi.util.InternalFormatter;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTableAmountCellEditor;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTableAmountCellRenderer;
import org.homeunix.thecave.buddi.view.swing.MyBudgetTreeNameCellRenderer;
import org.homeunix.thecave.buddi.view.swing.TranslatorListCellRenderer;
import org.homeunix.thecave.moss.swing.MossDecimalField;
import org.homeunix.thecave.moss.swing.MossPanel;
import org.homeunix.thecave.moss.util.OperatingSystemUtil;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

public class MyBudgetPanel extends MossPanel implements ActionListener {

    public static final long serialVersionUID = 0;

    private final JXTreeTable tree;

    private final JLabel balanceLabel;

    private final BudgetDateSpinnerModel dateSpinnerModel;

    private final JSpinner dateSpinner;

    private final JComboBox periodTypeComboBox;

    private final MyBudgetTreeTableModel treeTableModel;

    private final MainFrame parent;

    public MyBudgetPanel(MainFrame parent) {
        super(true);
        this.parent = parent;
        this.treeTableModel = new MyBudgetTreeTableModel((Document) parent.getDocument());
        tree = new JXTreeTable(treeTableModel);
        balanceLabel = new JLabel();
        dateSpinnerModel = new BudgetDateSpinnerModel(treeTableModel);
        dateSpinner = new JSpinner(dateSpinnerModel);
        periodTypeComboBox = new JComboBox(BudgetCategoryTypes.values());
        open();
    }

    public List<BudgetCategory> getSelectedBudgetCategories() {
        List<BudgetCategory> budgetCategories = new LinkedList<BudgetCategory>();
        for (Integer i : tree.getSelectedRows()) {
            budgetCategories.add((BudgetCategory) tree.getModel().getValueAt(i, -1));
        }
        return budgetCategories;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(periodTypeComboBox)) {
            treeTableModel.setSelectedBudgetPeriodType(ModelFactory.getBudgetCategoryType(periodTypeComboBox.getSelectedItem().toString()));
            dateSpinnerModel.setValue(treeTableModel.getSelectedBudgetPeriodType().getStartOfBudgetPeriod(dateSpinnerModel.getDate()));
            updateContent();
            fireStructureChanged();
        }
    }

    public void init() {
        super.init();
        tree.setRootVisible(false);
        tree.setShowsRootHandles(true);
        tree.setAutoResizeMode(JXTreeTable.AUTO_RESIZE_ALL_COLUMNS);
        tree.setClosedIcon(null);
        tree.setOpenIcon(null);
        tree.setLeafIcon(null);
        tree.setTreeCellRenderer(new MyBudgetTreeNameCellRenderer());
        for (int i = 1; i < treeTableModel.getColumnCount(); i++) {
            MossDecimalField editor = new MossDecimalField(0, true, 2);
            final KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new KeyEventDispatcher() {

                public boolean dispatchKeyEvent(KeyEvent e) {
                    if (MyBudgetPanel.this.parent.isActive() && MyBudgetPanel.this.parent.isMyBudgetTabSelected() && MyBudgetPanel.this.tree.getSelectedRow() != -1 && MyBudgetPanel.this.tree.getSelectedColumn() != -1 && (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_LEFT)) {
                        manager.redispatchEvent(tree, e);
                        return true;
                    }
                    if ((OperatingSystemUtil.isMac() && e.isMetaDown()) || (!OperatingSystemUtil.isMac() && e.isControlDown())) {
                        manager.redispatchEvent(parent, e);
                    }
                    return false;
                }
            });
            tree.getColumn(i).setCellRenderer(new MyBudgetTableAmountCellRenderer());
            tree.getColumn(i).setCellEditor(new MyBudgetTableAmountCellEditor(editor));
        }
        tree.addHighlighter(HighlighterFactory.createAlternateStriping(Const.COLOR_EVEN_ROW, Const.COLOR_ODD_ROW));
        tree.addTreeExpansionListener(new TreeExpansionListener() {

            public void treeCollapsed(TreeExpansionEvent event) {
                Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
                if (o instanceof BudgetCategory) {
                    BudgetCategory bc = (BudgetCategory) o;
                    bc.setExpanded(false);
                }
            }

            public void treeExpanded(TreeExpansionEvent event) {
                Object o = event.getPath().getPath()[event.getPath().getPath().length - 1];
                if (o instanceof BudgetCategory) {
                    BudgetCategory bc = (BudgetCategory) o;
                    bc.setExpanded(true);
                }
            }
        });
        JScrollPane listScroller = new JScrollPane(tree);
        dateSpinner.setPreferredSize(InternalFormatter.getComponentSize(dateSpinner, 120));
        dateSpinner.addChangeListener(new ChangeListener() {

            public void stateChanged(ChangeEvent arg0) {
                if (dateSpinner.getValue() instanceof Date) treeTableModel.setSelectedDate((Date) dateSpinner.getValue());
                updateContent();
            }
        });
        periodTypeComboBox.setSelectedItem(ModelFactory.getBudgetCategoryType(BudgetCategoryTypes.BUDGET_CATEGORY_TYPE_MONTH));
        periodTypeComboBox.addActionListener(this);
        periodTypeComboBox.setRenderer(new TranslatorListCellRenderer());
        JPanel balanceLabelPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        balanceLabelPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.BUDGET_NET_INCOME)));
        balanceLabelPanel.add(periodTypeComboBox);
        balanceLabelPanel.add(balanceLabel);
        JPanel listScrollerPanel = new JPanel(new BorderLayout());
        listScrollerPanel.add(listScroller, BorderLayout.CENTER);
        listScrollerPanel.add(balanceLabelPanel, BorderLayout.SOUTH);
        JPanel spinnerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        spinnerPanel.add(new JLabel(TextFormatter.getTranslation(BuddiKeys.CURRENT_BUDGET_PERIOD)));
        spinnerPanel.add(dateSpinner);
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(spinnerPanel, BorderLayout.EAST);
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(listScrollerPanel, BorderLayout.CENTER);
        if (OperatingSystemUtil.isMac()) {
            listScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        }
        tree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent arg0) {
                parent.updateContent();
            }
        });
        updateButtons();
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
    }

    public void updateContent() {
        super.updateContent();
        for (int i = 1; i < treeTableModel.getColumnCount(); i++) tree.getColumn(i).setHeaderValue(treeTableModel.getColumnName(i));
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, treeTableModel.getSelectedBudgetPeriodType().getDateFormat()));
        long budgetedNetIncome = 0;
        for (BudgetCategory bc : new FilteredLists.BudgetCategoryListFilteredByPeriodType((Document) parent.getDocument(), treeTableModel.getSelectedBudgetPeriodType())) {
            budgetedNetIncome += (bc.getAmount(treeTableModel.getSelectedDate()) * (bc.isIncome() ? 1 : -1));
        }
        balanceLabel.setText(TextFormatter.getHtmlWrapper(TextFormatter.getFormattedCurrency(budgetedNetIncome)));
        for (BudgetCategory bc : ((Document) parent.getDocument()).getBudgetCategories()) {
            TreePath path = new TreePath(new Object[] { treeTableModel.getRoot(), bc });
            if (bc.isExpanded()) tree.expandPath(path); else tree.collapsePath(path);
        }
    }

    public void fireStructureChanged() {
        treeTableModel.fireStructureChanged();
    }
}
