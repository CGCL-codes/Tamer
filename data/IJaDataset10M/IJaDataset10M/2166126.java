package net.mogray.infinitypfm.ui.view.views;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import net.mogray.infinitypfm.client.InfinityPfm;
import net.mogray.infinitypfm.core.conf.MM;
import net.mogray.infinitypfm.core.data.Budget;
import net.mogray.infinitypfm.core.data.BudgetDetail;
import net.mogray.infinitypfm.core.data.DataFormatUtil;
import net.mogray.infinitypfm.core.data.MonthlyBalance;
import net.mogray.infinitypfm.core.data.Transaction;
import net.mogray.infinitypfm.ui.view.dialogs.MessageDialog;
import net.mogray.infinitypfm.ui.view.toolbars.BudgetToolbar;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

/**
 * @author wayne
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BudgetView extends BaseView {

    private Composite sh;

    private Label lblBudget = null;

    private Label lblOverUnder = null;

    private Label lblOverUnderAmt = null;

    private Label lblLiabTotal = null;

    private Label lblLiabTotalAmt = null;

    private Label lblExpenseTotal = null;

    private Label lblExpenseTotalAmt = null;

    private Label lblIncomeTotal = null;

    private Label lblIncomeTotalAmt = null;

    private Label lblTotalBalance = null;

    private Label lblTotalBalanceAmt = null;

    private Label lblMonth = null;

    private Label lblEstimate = null;

    private Button cmdEstimate = null;

    private Text txtEstimate = null;

    private BudgetToolbar tbMain = null;

    private Table tblTrans = null;

    private TableEditor editor = null;

    private Combo cmbMonth = null;

    private Budget budget = null;

    private int Yr = 0;

    private double dEstimate = 0.00;

    private static final int EDITABLECOLUMN = 1;

    private static final String DEFAULTMONTHAMOUNT = "0.0          ";

    /**
	 * @param arg0
	 * @param arg1
	 */
    public BudgetView(Composite arg0, int arg1) {
        super(arg0, arg1);
        sh = arg0;
        budget = InfinityPfm.qzMain.getTrMain().getSelectedBudget();
        LoadUI();
        LoadLayout();
    }

    protected void LoadUI() {
        this.setLayout(new FormLayout());
        tbMain = new BudgetToolbar(this);
        lblBudget = new Label(this, SWT.NONE);
        lblBudget.setText(MM.PHRASES.getPhrase("100") + ":  " + budget.getBudgetName());
        lblOverUnder = new Label(this, SWT.NONE);
        lblOverUnder.setText(MM.PHRASES.getPhrase("95") + ":");
        lblOverUnderAmt = new Label(this, SWT.NONE);
        lblOverUnderAmt.setText(DEFAULTMONTHAMOUNT);
        lblLiabTotal = new Label(this, SWT.NONE);
        lblLiabTotal.setText(MM.PHRASES.getPhrase("22") + " " + MM.PHRASES.getPhrase("99") + ":");
        lblLiabTotalAmt = new Label(this, SWT.NONE);
        lblLiabTotalAmt.setText(DEFAULTMONTHAMOUNT);
        lblExpenseTotal = new Label(this, SWT.NONE);
        lblExpenseTotal.setText(MM.PHRASES.getPhrase("94") + " " + MM.PHRASES.getPhrase("99") + ":");
        lblExpenseTotalAmt = new Label(this, SWT.NONE);
        lblExpenseTotalAmt.setText(DEFAULTMONTHAMOUNT);
        lblIncomeTotal = new Label(this, SWT.NONE);
        lblIncomeTotal.setText(MM.PHRASES.getPhrase("118") + " " + MM.PHRASES.getPhrase("99") + ":");
        lblIncomeTotalAmt = new Label(this, SWT.NONE);
        lblIncomeTotalAmt.setText(DEFAULTMONTHAMOUNT);
        lblTotalBalance = new Label(this, SWT.NONE);
        lblTotalBalance.setText(MM.PHRASES.getPhrase("119") + ":");
        lblTotalBalanceAmt = new Label(this, SWT.NONE);
        lblTotalBalanceAmt.setText(DEFAULTMONTHAMOUNT);
        lblMonth = new Label(this, SWT.NONE);
        lblMonth.setText(MM.PHRASES.getPhrase("99"));
        lblEstimate = new Label(this, SWT.NONE);
        lblEstimate.setText(MM.PHRASES.getPhrase("130"));
        cmdEstimate = new Button(this, SWT.PUSH);
        cmdEstimate.setText("...");
        cmdEstimate.setToolTipText(MM.PHRASES.getPhrase("131"));
        txtEstimate = new Text(this, SWT.BORDER);
        txtEstimate.setText("0000000.00");
        txtEstimate.setEnabled(false);
        tblTrans = new Table(this, SWT.MULTI | SWT.HIDE_SELECTION);
        tblTrans.setLinesVisible(true);
        LoadColumns();
        DataFormatUtil dateUtil = new DataFormatUtil();
        dateUtil.setDate(new Date());
        Yr = dateUtil.getYear();
        LoadBudgetData(dateUtil.getMonth());
        LoadMonthCombo(dateUtil.getMonth() - 1);
        editor = new TableEditor(tblTrans);
        editor.horizontalAlignment = SWT.LEFT;
        editor.grabHorizontal = true;
        editor.minimumWidth = 50;
        tblTrans.addSelectionListener(tblTrans_OnClick);
        cmdEstimate.addSelectionListener(cmdEstimate_OnClick);
        tblTrans.addListener(SWT.MeasureItem, paintListener);
    }

    protected void LoadLayout() {
        FormData lblbudgetdata = new FormData();
        lblbudgetdata.top = new FormAttachment(0, 20);
        lblbudgetdata.left = new FormAttachment(0, 20);
        lblBudget.setLayoutData(lblbudgetdata);
        FormData lblmonthdata = new FormData();
        lblmonthdata.top = new FormAttachment(lblBudget, 10);
        lblmonthdata.left = new FormAttachment(0, 20);
        lblMonth.setLayoutData(lblmonthdata);
        FormData cmbmonthdata = new FormData();
        cmbmonthdata.top = new FormAttachment(lblBudget, 30);
        cmbmonthdata.left = new FormAttachment(0, 20);
        cmbMonth.setLayoutData(cmbmonthdata);
        FormData lblestimatedata = new FormData();
        lblestimatedata.top = new FormAttachment(lblBudget, 10);
        lblestimatedata.left = new FormAttachment(cmbMonth, 25);
        lblEstimate.setLayoutData(lblestimatedata);
        FormData txtestimatedata = new FormData();
        txtestimatedata.top = new FormAttachment(lblBudget, 30);
        txtestimatedata.left = new FormAttachment(cmbMonth, 25);
        txtEstimate.setLayoutData(txtestimatedata);
        FormData cmdestimatedata = new FormData();
        cmdestimatedata.top = new FormAttachment(lblBudget, 28);
        cmdestimatedata.left = new FormAttachment(txtEstimate, 0);
        cmdEstimate.setLayoutData(cmdestimatedata);
        FormData lblincometotaldata = new FormData();
        lblincometotaldata.top = new FormAttachment(0, 25);
        lblincometotaldata.left = new FormAttachment(60, 0);
        lblIncomeTotal.setLayoutData(lblincometotaldata);
        FormData lblincometotalamtdata = new FormData();
        lblincometotalamtdata.top = new FormAttachment(0, 25);
        lblincometotalamtdata.left = new FormAttachment(lblIncomeTotal, 20);
        lblincometotalamtdata.right = new FormAttachment(100, -20);
        lblIncomeTotalAmt.setLayoutData(lblincometotalamtdata);
        FormData lblliabtotaldata = new FormData();
        lblliabtotaldata.top = new FormAttachment(lblIncomeTotal, 0);
        lblliabtotaldata.left = new FormAttachment(60, 0);
        lblLiabTotal.setLayoutData(lblliabtotaldata);
        FormData lblliabtotalamtdata = new FormData();
        lblliabtotalamtdata.top = new FormAttachment(lblIncomeTotal, 0);
        lblliabtotalamtdata.left = new FormAttachment(lblIncomeTotal, 20);
        lblliabtotalamtdata.right = new FormAttachment(100, -20);
        lblLiabTotalAmt.setLayoutData(lblliabtotalamtdata);
        FormData lblexpensetotaldata = new FormData();
        lblexpensetotaldata.top = new FormAttachment(lblLiabTotalAmt, 0);
        lblexpensetotaldata.left = new FormAttachment(60, 0);
        lblExpenseTotal.setLayoutData(lblexpensetotaldata);
        FormData lblexpensetotalamtdata = new FormData();
        lblexpensetotalamtdata.top = new FormAttachment(lblLiabTotalAmt, 0);
        lblexpensetotalamtdata.left = new FormAttachment(lblIncomeTotal, 20);
        lblexpensetotalamtdata.right = new FormAttachment(100, -20);
        lblExpenseTotalAmt.setLayoutData(lblexpensetotalamtdata);
        FormData lbltotalbalancedata = new FormData();
        lbltotalbalancedata.top = new FormAttachment(lblExpenseTotal, 0);
        lbltotalbalancedata.left = new FormAttachment(60, 0);
        lblTotalBalance.setLayoutData(lbltotalbalancedata);
        FormData lbltotalbalanceamtdata = new FormData();
        lbltotalbalanceamtdata.top = new FormAttachment(lblExpenseTotal, 0);
        lbltotalbalanceamtdata.left = new FormAttachment(lblIncomeTotal, 20);
        lbltotalbalanceamtdata.right = new FormAttachment(100, -20);
        lblTotalBalanceAmt.setLayoutData(lbltotalbalanceamtdata);
        FormData tbltransdata = new FormData();
        tbltransdata.top = new FormAttachment(lblTotalBalance, 10);
        tbltransdata.left = new FormAttachment(0, 20);
        tbltransdata.right = new FormAttachment(100, -20);
        tbltransdata.bottom = new FormAttachment(100, -20);
        tblTrans.setLayoutData(tbltransdata);
        FormData lbloverunderdata = new FormData();
        lbloverunderdata.top = new FormAttachment(tblTrans, 3);
        lbloverunderdata.left = new FormAttachment(35, 0);
        lblOverUnder.setLayoutData(lbloverunderdata);
        FormData lbloverunderamtdata = new FormData();
        lbloverunderamtdata.top = new FormAttachment(tblTrans, 3);
        lbloverunderamtdata.left = new FormAttachment(lblOverUnder, 10);
        lbloverunderamtdata.right = new FormAttachment(100, -20);
        lblOverUnderAmt.setLayoutData(lbloverunderamtdata);
        FormData tbmaindata = new FormData();
        tbmaindata.right = new FormAttachment(100, 0);
        tbMain.setLayoutDat(tbmaindata);
    }

    private void LoadBudgetData(int month) {
        try {
            Calendar now = Calendar.getInstance();
            Calendar passedMonth = Calendar.getInstance();
            DataFormatUtil format = new DataFormatUtil();
            BudgetDetail detail = new BudgetDetail();
            MonthlyBalance monthlyBalance = null;
            Transaction transaction = new Transaction();
            detail.setBudgetId(budget.getBudgetId());
            detail.setMth(month);
            detail.setYr(Yr);
            float totalBalance = 0;
            String yearBudgetBalance = null;
            String yearBalance = null;
            int currentMonth = 1;
            final Display display = InfinityPfm.shMain.getDisplay();
            String sOverUnder = (String) MM.sqlMap.queryForObject("getOverUnderForMonth", detail);
            if (sOverUnder == null) {
                lblOverUnderAmt.setText(DEFAULTMONTHAMOUNT);
            } else {
                lblOverUnderAmt.setText(format.getAmountFormatted(new Float(sOverUnder).floatValue()));
            }
            String sIncomeTotal = (String) MM.sqlMap.queryForObject("getIncomeTotalForMonth", detail);
            if (sIncomeTotal == null) {
                lblIncomeTotalAmt.setText(DEFAULTMONTHAMOUNT);
            } else {
                totalBalance = new Float(sIncomeTotal).floatValue() * -1;
                lblIncomeTotalAmt.setText(format.getAmountFormatted(totalBalance));
            }
            String sLiabTotal = (String) MM.sqlMap.queryForObject("getLiabilityTotalForMonth", detail);
            if (sLiabTotal == null) {
                lblLiabTotalAmt.setText(DEFAULTMONTHAMOUNT);
            } else {
                totalBalance = totalBalance - new Float(sLiabTotal).floatValue();
                lblLiabTotalAmt.setText(format.getAmountFormatted(new Float(sLiabTotal).floatValue()));
            }
            String sExpenseTotal = (String) MM.sqlMap.queryForObject("getExpenseTotalForMonth", detail);
            if (sExpenseTotal == null) {
                lblExpenseTotalAmt.setText(DEFAULTMONTHAMOUNT);
            } else {
                totalBalance = totalBalance - new Float(sExpenseTotal).floatValue();
                lblExpenseTotalAmt.setText(format.getAmountFormatted(new Float(sExpenseTotal).floatValue()));
            }
            lblTotalBalanceAmt.setText(format.getAmountFormatted(totalBalance));
            java.util.List detailList = null;
            detailList = MM.sqlMap.queryForList("getBudgetDetailForMonth", detail);
            if (detailList != null) {
                format.setDate(Yr, month);
                currentMonth = now.get(Calendar.MONTH) + 1;
                for (int i = 0; i < detailList.size(); i++) {
                    detail = (BudgetDetail) detailList.get(i);
                    TableItem ti = null;
                    try {
                        ti = tblTrans.getItem(i);
                    } catch (IllegalArgumentException iae) {
                        ti = new TableItem(tblTrans, SWT.NONE);
                    }
                    ti.setText(0, detail.getActName());
                    ti.setText(1, format.getAmountFormatted(new Float(detail.getAmount()).floatValue()));
                    transaction.setActId(detail.getActId());
                    transaction.setTranDate(format.getDate());
                    monthlyBalance = (MonthlyBalance) MM.sqlMap.queryForObject("getMonthlyBalance", transaction);
                    if (month == currentMonth) {
                        ti.setText(3, format.getAmountFormatted(detail.getAmount() / now.get(Calendar.WEEK_OF_MONTH)));
                    } else {
                        passedMonth.set(now.get(Calendar.YEAR), month, 1);
                        ti.setText(3, format.getAmountFormatted(detail.getAmount() / passedMonth.getActualMaximum(Calendar.WEEK_OF_MONTH)));
                    }
                    if (monthlyBalance != null) {
                        ti.setText(2, format.getAmountFormatted(new Float(monthlyBalance.getActBalance()).floatValue()));
                        if (month == currentMonth) {
                            ti.setText(4, format.getAmountFormatted(monthlyBalance.getActBalance() / now.get(Calendar.WEEK_OF_MONTH)));
                        } else {
                            ti.setText(4, format.getAmountFormatted(monthlyBalance.getActBalance() / passedMonth.getActualMaximum(Calendar.WEEK_OF_MONTH)));
                        }
                        ti.setText(5, format.getAmountFormatted(new Float(detail.getAmount() - monthlyBalance.getActBalance()).floatValue()));
                        detail.setActBalance(monthlyBalance.getActBalance());
                    } else {
                        ti.setText(2, format.getAmountFormatted(new Float(0).floatValue()));
                        ti.setText(4, format.getAmountFormatted(new Float(0).floatValue()));
                        ti.setText(5, format.getAmountFormatted(new Float(detail.getAmount()).floatValue()));
                    }
                    yearBalance = (String) MM.sqlMap.queryForObject("getYearlyBalance", transaction);
                    yearBudgetBalance = (String) MM.sqlMap.queryForObject("getYearBudgetBalance", detail);
                    if (yearBalance == null) {
                        yearBalance = "0";
                    }
                    if (yearBudgetBalance == null) {
                        yearBudgetBalance = "0";
                    }
                    sOverUnder = format.getAmountFormatted(new Float(yearBudgetBalance).floatValue() - new Float(yearBalance).floatValue());
                    if (sOverUnder != null) {
                        ti.setText(6, sOverUnder);
                    } else {
                        ti.setText(6, format.getAmountFormatted(new Float(0).floatValue()));
                    }
                    if (i % 2D == 0) {
                        ti.setBackground(0, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(1, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(2, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(3, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(4, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(5, display.getSystemColor(MM.ROW_BACKGROUND));
                        ti.setBackground(6, display.getSystemColor(MM.ROW_BACKGROUND));
                    }
                    ti.setData(detail);
                }
            }
        } catch (SQLException se) {
            MessageDialog show = new MessageDialog(MM.DIALOG_INFO, MM.APPTITLE, se.getMessage());
            show.Open();
        }
    }

    private void LoadMonthCombo(int month) {
        cmbMonth = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        cmbMonth.add(MM.PHRASES.getPhrase("106"));
        cmbMonth.add(MM.PHRASES.getPhrase("107"));
        cmbMonth.add(MM.PHRASES.getPhrase("108"));
        cmbMonth.add(MM.PHRASES.getPhrase("109"));
        cmbMonth.add(MM.PHRASES.getPhrase("110"));
        cmbMonth.add(MM.PHRASES.getPhrase("111"));
        cmbMonth.add(MM.PHRASES.getPhrase("112"));
        cmbMonth.add(MM.PHRASES.getPhrase("113"));
        cmbMonth.add(MM.PHRASES.getPhrase("114"));
        cmbMonth.add(MM.PHRASES.getPhrase("115"));
        cmbMonth.add(MM.PHRASES.getPhrase("116"));
        cmbMonth.add(MM.PHRASES.getPhrase("117"));
        cmbMonth.select(month);
        cmbMonth.addSelectionListener(cmbMonth_OnClick);
    }

    private void LoadColumns() {
        TableColumn tc1 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc2 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc3 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc4 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc5 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc6 = new TableColumn(tblTrans, SWT.LEFT);
        TableColumn tc7 = new TableColumn(tblTrans, SWT.LEFT);
        tc1.setText(MM.PHRASES.getPhrase("93"));
        tc2.setText(MM.PHRASES.getPhrase("100"));
        tc3.setText(MM.PHRASES.getPhrase("125"));
        tc4.setText(MM.PHRASES.getPhrase("138"));
        tc5.setText(MM.PHRASES.getPhrase("137"));
        tc6.setText(MM.PHRASES.getPhrase("95"));
        tc7.setText(MM.PHRASES.getPhrase("129"));
        tc1.setWidth(150);
        tc2.setWidth(100);
        tc3.setWidth(100);
        tc4.setWidth(120);
        tc5.setWidth(120);
        tc6.setWidth(120);
        tc7.setWidth(120);
        tc1.setResizable(true);
        tc2.setResizable(true);
        tc3.setResizable(true);
        tc4.setResizable(true);
        tc5.setResizable(true);
        tc6.setResizable(true);
        tc7.setResizable(true);
        tblTrans.setHeaderVisible(true);
    }

    public void QZDispose() {
        super.QZDispose();
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
        LoadUI();
        LoadLayout();
    }

    public int getYear() {
        return Yr;
    }

    public int getMonth() {
        return cmbMonth.getSelectionIndex() + 1;
    }

    public void setBudgetEstimate() {
        BudgetDetail detail = new BudgetDetail();
        detail.setBudgetId(budget.getBudgetId());
        detail.setMth(this.getMonth());
        detail.setYr(Yr);
        String budgetBalance = null;
        if (dEstimate > 0.0) {
            try {
                budgetBalance = (String) MM.sqlMap.queryForObject("getBudgetTotalForMonth", detail);
            } catch (SQLException se) {
                InfinityPfm.LogMessage(se.getMessage());
            }
            if (!txtEstimate.isEnabled()) {
                txtEstimate.setText(Double.toString(dEstimate - Double.parseDouble(budgetBalance)));
            }
        }
    }

    SelectionAdapter cmbMonth_OnClick = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            Control oldEditor = editor.getEditor();
            if (oldEditor != null) oldEditor.dispose();
            LoadBudgetData(cmbMonth.getSelectionIndex() + 1);
            setBudgetEstimate();
        }
    };

    SelectionAdapter tblTrans_OnClick = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            Control oldEditor = editor.getEditor();
            if (oldEditor != null) oldEditor.dispose();
            TableItem item = (TableItem) event.item;
            if (item == null) return;
            Text newEditor = new Text(tblTrans, SWT.BORDER);
            newEditor.setText(item.getText(1));
            newEditor.setSelection(0, 10);
            newEditor.addFocusListener(new FocusAdapter() {

                public void focusLost(FocusEvent event) {
                    Text combo = (Text) editor.getEditor();
                    String sAmt = combo.getText();
                    BudgetDetail detail = null;
                    DataFormatUtil formater = new DataFormatUtil();
                    if (sAmt != null && sAmt.length() > 0) {
                        editor.getItem().setText(EDITABLECOLUMN, sAmt);
                        detail = (BudgetDetail) editor.getItem().getData();
                        editor.getItem().setText(EDITABLECOLUMN + 4, formater.getAmountFormatted(Float.parseFloat(sAmt.replaceAll(",", "")) - detail.getActBalance()));
                        detail.setAmount(new Float(sAmt.replaceAll(",", "")).floatValue());
                        String budgetBalance = null;
                        try {
                            MM.sqlMap.update("updateBudgetDetail", detail);
                        } catch (SQLException se) {
                            InfinityPfm.LogMessage(se.getMessage());
                        }
                        setBudgetEstimate();
                    }
                }
            });
            if (newEditor != null) {
                newEditor.setFocus();
                editor.setEditor(newEditor, item, EDITABLECOLUMN);
            }
        }
    };

    SelectionAdapter cmdEstimate_OnClick = new SelectionAdapter() {

        public void widgetSelected(SelectionEvent event) {
            txtEstimate.setEnabled(!txtEstimate.isEnabled());
            if (!txtEstimate.isEnabled()) {
                dEstimate = Double.parseDouble(txtEstimate.getText());
                setBudgetEstimate();
            } else {
                txtEstimate.setFocus();
                txtEstimate.setSelection(0, 10);
            }
        }
    };

    Listener paintListener = new Listener() {

        public void handleEvent(Event event) {
            switch(event.type) {
                case SWT.MeasureItem:
                    {
                        event.height = 20;
                        break;
                    }
            }
        }
    };
}
