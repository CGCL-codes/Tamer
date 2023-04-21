package jgnash.ui.reconcile;

import com.jgoodies.forms.builder.DefaultFormBuilder;
import com.jgoodies.forms.factories.*;
import com.jgoodies.forms.layout.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import jgnash.engine.Account;
import jgnash.engine.event.*;
import jgnash.text.CommodityFormat;
import jgnash.ui.UIApplication;
import jgnash.ui.components.*;
import jgnash.ui.util.*;

/** Account reconcile dialog.
 * <p>
 * $Id: ReconcileDialog.java 675 2008-06-17 01:36:01Z ccavanaugh $
 * 
 * @author Craig Cavanaugh
 */
public class ReconcileDialog extends javax.swing.JDialog implements WeakObserver, ActionListener, ListSelectionListener {

    private Account account;

    private Date openingDate;

    private BigDecimal openingBalance;

    private BigDecimal endingBalance;

    private JTable creditTable;

    private JTable debitTable;

    private AbstractReconcileTableModel creditModel;

    private AbstractReconcileTableModel debitModel;

    private JLabel creditTotalLabel;

    private JLabel debitTotalLabel;

    private JLabel differenceLabel;

    private JLabel endingBalanceLabel;

    private JButton cancelButton;

    private JButton finishButton;

    private JButton finishLaterButton;

    private JButton creditSelectAllButton;

    private JButton creditClearAllButton;

    private JButton debitSelectAllButton;

    private JButton debitClearAllButton;

    private JLabel openingBalanceLabel;

    private JLabel reconciledBalanceLabel;

    private NumberFormat numberFormat;

    private UIResource rb = (UIResource) UIResource.get();

    public ReconcileDialog(Account reconcileAccount, Date openingDate, BigDecimal openingBalance, BigDecimal endingBalance) {
        super(UIApplication.getFrame(), false);
        account = reconcileAccount;
        this.endingBalance = endingBalance;
        this.openingBalance = openingBalance;
        this.openingDate = openingDate;
        numberFormat = CommodityFormat.getShortNumberFormat(account.getCommodityNode());
        setTitle(rb.getString("Button.Reconcile") + " - " + account.getPathName());
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        layoutMainPanel();
        DialogUtils.addBoundsListener(this);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                JTableUtils.packTable(debitTable);
                JTableUtils.packTable(creditTable);
            }
        });
        engine.addAccountObserver(this);
        engine.addSystemObserver(this);
    }

    private void initComponents() {
        creditTotalLabel = new JLabel();
        debitTotalLabel = new JLabel();
        differenceLabel = new JLabel();
        openingBalanceLabel = new JLabel();
        endingBalanceLabel = new JLabel();
        reconciledBalanceLabel = new JLabel();
        cancelButton = new JButton(rb.getString("Button.Cancel"));
        finishLaterButton = new JButton(rb.getString("Button.FinishLater"));
        finishButton = new JButton(rb.getString("Button.Finish"));
        creditSelectAllButton = new JButton(rb.getString("Button.SelectAll"));
        creditClearAllButton = new JButton(rb.getString("Button.ClearAll"));
        debitSelectAllButton = new JButton(rb.getString("Button.SelectAll"));
        debitClearAllButton = new JButton(rb.getString("Button.ClearAll"));
        creditModel = new CreditModel(account, openingDate);
        creditTable = createTable(creditModel);
        debitModel = new DebitModel(account, openingDate);
        debitTable = createTable(debitModel);
        openingBalanceLabel.setText(numberFormat.format(openingBalance));
        endingBalanceLabel.setText(numberFormat.format(endingBalance));
        updateCreditStatus();
        updateDebitStatus();
        updateStatus();
        finishButton.addActionListener(this);
        finishLaterButton.addActionListener(this);
        cancelButton.addActionListener(this);
        creditSelectAllButton.addActionListener(this);
        creditClearAllButton.addActionListener(this);
        debitSelectAllButton.addActionListener(this);
        debitClearAllButton.addActionListener(this);
    }

    private JTable createTable(AbstractReconcileTableModel model) {
        JTable table = new JTable(model);
        table.setDefaultRenderer(String.class, new ColoredTableCellRenderer());
        table.setDefaultRenderer(Date.class, new DateTableCellRenderer());
        table.setDefaultRenderer(Boolean.class, new ColoredBooleanTableCellRenderer());
        table.setDefaultRenderer(BigDecimal.class, ColoredCommodityTableCellRenderer.getFullRenderer(account.getCommodityNode()));
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(this);
        return table;
    }

    private void layoutMainPanel() {
        initComponents();
        FormLayout layout = new FormLayout("min:grow(0.5), 7dlu, min:grow(0.5)", "fill:min:g, 7dlu, p, 14dlu, p");
        layout.addGroupedColumn(1);
        layout.addGroupedColumn(3);
        CellConstraints cc = new CellConstraints();
        FormLayout dLayout = new FormLayout("fill:min:g(1.0)", "fill:min:g, 4dlu, p");
        JPanel dPanel = new JPanel(dLayout);
        dPanel.add(buildTablePanel(rb.getString("Title.Debits"), debitTotalLabel, debitTable), cc.xy(1, 1));
        dPanel.add(ButtonBarFactory.buildLeftAlignedBar(debitSelectAllButton, debitClearAllButton), cc.xy(1, 3));
        FormLayout cLayout = new FormLayout("fill:min:g(1.0)", "fill:min:g, 4dlu, p");
        JPanel cPanel = new JPanel(cLayout);
        cPanel.add(buildTablePanel(rb.getString("Title.Credits"), creditTotalLabel, creditTable), cc.xy(1, 1));
        cPanel.add(ButtonBarFactory.buildLeftAlignedBar(creditSelectAllButton, creditClearAllButton), cc.xy(1, 3));
        JPanel p = new JPanel(layout);
        p.setBorder(Borders.DIALOG_BORDER);
        p.add(dPanel, cc.xywh(1, 1, 1, 3));
        p.add(cPanel, cc.xy(3, 1));
        p.add(buildStatPanel(), cc.xy(3, 3));
        p.add(ButtonBarFactory.buildRightAlignedBar(cancelButton, finishLaterButton, finishButton), cc.xywh(1, 5, 3, 1));
        getContentPane().add(p);
        pack();
    }

    private JPanel buildStatPanel() {
        FormLayout layout = new FormLayout("left:p, 8dlu, right:65dlu:g", "");
        DefaultFormBuilder builder = new DefaultFormBuilder(layout);
        builder.setDefaultDialogBorder();
        builder.setRowGroupingEnabled(true);
        builder.append(rb.getString("Label.OpeningBalance"), openingBalanceLabel);
        builder.append(rb.getString("Label.TargetBalance"), endingBalanceLabel);
        builder.append(rb.getString("Label.ReconciledBalance"), reconciledBalanceLabel);
        builder.appendSeparator();
        builder.append(rb.getString("Label.Difference"), differenceLabel);
        return builder.getPanel();
    }

    private JPanel buildTablePanel(String title, JLabel label, JTable table) {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new ShadowBorder());
        ComponentFactory cf = DefaultComponentFactory.getInstance();
        JPanel header = new GradientPanel();
        header.setBackground(UIManager.getColor("InternalFrame.activeTitleBackground"));
        header.add(cf.createTitle(title));
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footer.setBackground(SystemColor.inactiveCaptionBorder);
        footer.add(label);
        JScrollPane pane = new JScrollPane(table);
        pane.setBorder(new EmptyBorder(0, 0, 0, 0));
        pane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        p.add(header, BorderLayout.NORTH);
        p.add(pane, BorderLayout.CENTER);
        p.add(footer, BorderLayout.SOUTH);
        return p;
    }

    /** Closes the dialog */
    private void closeDialog() {
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }

    /** look to see if this dialog needs to be closed automatically
	 */
    public void update(WeakObservable o, jgnashEvent event) {
        switch(event.messageId) {
            case jgnashEvent.ACCOUNT_REMOVE:
                if (event.account != account) {
                    return;
                }
            case jgnashEvent.FILE_NEW_SUCCESS:
            case jgnashEvent.FILE_CLOSING:
            case jgnashEvent.FILE_LOAD_SUCCESS:
                closeDialog();
                return;
            default:
                return;
        }
    }

    /** Invoked when an action occurs.
	 */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == finishLaterButton || e.getSource() == finishButton) {
            closeDialog();
            creditModel.commitChanges();
            debitModel.commitChanges();
        } else if (e.getSource() == cancelButton) {
            closeDialog();
        } else if (e.getSource() == creditSelectAllButton) {
            creditModel.selectAll();
            updateCreditStatus();
            updateStatus();
        } else if (e.getSource() == creditClearAllButton) {
            creditModel.clearAll();
            updateCreditStatus();
            updateStatus();
        } else if (e.getSource() == debitSelectAllButton) {
            debitModel.selectAll();
            updateDebitStatus();
            updateStatus();
        } else if (e.getSource() == debitClearAllButton) {
            debitModel.clearAll();
            updateDebitStatus();
            updateStatus();
        }
    }

    /**
	 * Called whenever the value of the selection changes.
	 * @param e the event that characterizes the change.
	 */
    public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting()) return;
        if (e.getSource() == creditTable.getSelectionModel()) {
            if (creditTable.getSelectedRow() >= 0) {
                creditModel.toggleReconciledState(creditTable.getSelectedRow());
                creditTable.clearSelection();
                updateCreditStatus();
                updateStatus();
            }
        } else if (e.getSource() == debitTable.getSelectionModel()) {
            if (debitTable.getSelectedRow() >= 0) {
                debitModel.toggleReconciledState(debitTable.getSelectedRow());
                debitTable.clearSelection();
                updateDebitStatus();
                updateStatus();
            }
        }
    }

    private void updateCreditStatus() {
        creditTotalLabel.setText(numberFormat.format(creditModel.getReconciledTotal()));
    }

    private void updateDebitStatus() {
        debitTotalLabel.setText(numberFormat.format(debitModel.getReconciledTotal()));
    }

    private void updateStatus() {
        BigDecimal sum = creditModel.getReconciledTotal().add(debitModel.getReconciledTotal());
        BigDecimal reconciledBalance = sum.add(openingBalance);
        BigDecimal difference = endingBalance.subtract(reconciledBalance).abs();
        reconciledBalanceLabel.setText(numberFormat.format(reconciledBalance));
        differenceLabel.setText(numberFormat.format(difference));
        finishLaterButton.setEnabled(difference.signum() != 0);
        finishButton.setEnabled(difference.signum() == 0);
    }
}
