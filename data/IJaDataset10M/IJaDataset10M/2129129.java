package org.grill.clovercash;

import java.io.File;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.grill.clovercash.accounts.Account;
import org.grill.clovercash.accounts.Split;
import org.grill.clovercash.accounts.Transaction;
import org.grill.clovercash.accounts.Account.DeltaEvent;
import org.grill.clovercash.accounts.Account.IDeltaListener;
import org.grill.clovercash.storage.CsvStatement;
import org.grill.clovercash.storage.Reconciliation;
import org.grill.clovercash.storage.StatementEntry;
import org.grill.clovercash.ui.SplitRowView;
import org.joda.time.LocalDate;

public class ReconcileView extends ViewPart implements SplitRowOwner {

    public static final String ID = "org.grill.clovercash.reconcileview";

    private boolean initialized;

    private Reconciliation reconciliation;

    @Override
    public void init(IViewSite site, IMemento memento) throws PartInitException {
        super.init(site);
        if (memento != null) {
            String accountName = memento.getString("accountname");
            Account findAccount = CloverCashUIPlugin.getDefault().getRootAccount().findAccount(accountName);
            if (findAccount != null) reconciliation = findAccount.getReconciliation();
        }
    }

    @Override
    public void saveState(IMemento memento) {
        System.out.println("Saving my state!");
        memento.putString("accountname", reconciliation.getAccount().getDisplayName());
    }

    public ReconcileView() {
    }

    private Composite parent;

    @Override
    public void createPartControl(Composite tmp) {
        parent = new Composite(tmp, SWT.NONE);
        GridLayout layout = new GridLayout(1, false);
        parent.setLayout(layout);
        addActionBars();
        System.out.println("Creating reconcile view control");
        CloverCashUIPlugin.getDefault().getRootAccount().addListener(new IDeltaListener() {

            public void remove(DeltaEvent event) {
                remove(event);
            }

            public void add(DeltaEvent event) {
                reconciliation.checkSplitsAreStillValid();
                updateControls();
                populateRows();
            }
        });
        if (reconciliation != null) initialize(reconciliation);
    }

    @Override
    public void setFocus() {
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void initialize(Reconciliation rec) {
        this.reconciliation = rec;
        initialized = true;
        System.out.println("Inintializeinng");
        buildView();
    }

    private Label currentLabel;

    private Label finalLabel;

    private Label diffLabel;

    private Button finishButton;

    private ScrolledComposite unReconciledScrollable;

    private Composite rowComposite;

    private Composite bottomComposite;

    private void buildView() {
        setPartName("Reconciling " + reconciliation.getAccount().getName());
        buildSplitRowView();
        buildTopView();
        bottomComposite = parent;
        buildUnreconciled();
        buildStatement();
        populateRows();
        updateControls();
        parent.getParent().layout(true, true);
    }

    private SplitRowView splitRow;

    private void buildSplitRowView() {
        splitRow = new SplitRowView(parent, this, reconciliation.getAccount(), new LocalDate());
        GridData gridData = new GridData(GridData.CENTER);
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.widthHint = 600;
        gridData.horizontalSpan = 2;
        splitRow.setData(gridData);
        splitRow.layout();
        splitRow.disableWidgets();
    }

    private void buildStatement() {
        statementTable = new Table(bottomComposite, SWT.V_SCROLL | SWT.FULL_SELECTION);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.heightHint = 100;
        statementTable.setLayoutData(gridData);
        statementTable.setHeaderVisible(true);
        statementTable.addListener(SWT.DefaultSelection, new Listener() {

            public void handleEvent(Event arg0) {
                int selectionIndex = statementTable.getSelectionIndex();
                if (selectionIndex == -1) return;
                TableItem item = statementTable.getItem(selectionIndex);
                StatementEntry entry = (StatementEntry) item.getData();
                splitRow.fillAndActivate(entry);
            }
        });
        TableColumn col1 = new TableColumn(statementTable, SWT.LEFT);
        col1.setText("Date");
        col1.setWidth(100);
        TableColumn col2 = new TableColumn(statementTable, SWT.LEFT);
        col2.setText("Payee");
        col2.setWidth(150);
        TableColumn col3 = new TableColumn(statementTable, SWT.RIGHT);
        col3.setText("Amount");
        col3.setWidth(75);
        TableColumn col4 = new TableColumn(statementTable, SWT.RIGHT);
        col4.setText("Balance");
        col4.setWidth(75);
    }

    private Table statementTable;

    private void addActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        IToolBarManager toolBarManager = bars.getToolBarManager();
        toolBarManager.add(new Action("Load Statement...", PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJ_ELEMENT)) {

            @Override
            public void run() {
                loadStatement();
            }
        });
    }

    private void loadStatement() {
        FileDialog dialog = new FileDialog(getViewSite().getShell(), SWT.OPEN);
        dialog.setFilterExtensions(new String[] { "Comma-Separated Value", "*.csv" });
        String path = dialog.open();
        if (path == null) return;
        if (new File(path).exists()) {
            try {
                CsvStatement csvStatement = new CsvStatement(path);
                csvStatement.load();
                if (csvStatement.getEntries().isEmpty()) MessageDialog.openError(getSite().getShell(), "No entries loaded", "No entries could be loaded from this file."); else {
                    reconciliation.setStatementEntries(csvStatement.getEntries());
                    renderStatement();
                    reconciliation.getAccount().changed(null);
                }
            } catch (Exception e) {
                MessageDialog.openError(getSite().getShell(), "Unable to load", "Error loading statement: " + e.toString());
                e.printStackTrace();
            }
        }
    }

    private void renderStatement() {
        if (reconciliation.getStatementEntries().isEmpty()) {
            disableStatementTable("No Statement Entries");
            return;
        }
        ArrayList<StatementEntry> matchingEntries = reconciliation.getMatchingStatementEntries();
        if (matchingEntries.isEmpty()) {
            disableStatementTable("Cannot Determine Entries");
            return;
        }
        statementTable.removeAll();
        statementTable.setEnabled(true);
        BigDecimal total = reconciliation.getAccount().getReconciledTotal(reconciliation.isRecurse());
        for (int x = 0; x < matchingEntries.size(); x++) {
            StatementEntry entry = matchingEntries.get(x);
            TableItem i = new TableItem(statementTable, SWT.NONE);
            i.setText(0, entry.date.toString());
            String desc = entry.payeeName;
            if (desc.length() == 0) desc = entry.memo;
            i.setText(1, desc);
            i.setText(2, NumberFormat.getCurrencyInstance().format(entry.value));
            total = total.add(entry.value);
            i.setText(3, NumberFormat.getCurrencyInstance().format(total));
            i.setData(entry);
        }
    }

    private void disableStatementTable(String text) {
        System.out.println("Disabling table: " + text);
        statementTable.removeAll();
        new TableItem(statementTable, SWT.NONE).setText(1, text);
    }

    private void buildUnreconciled() {
        unReconciledScrollable = new ScrolledComposite(bottomComposite, SWT.V_SCROLL);
        unReconciledScrollable.setLayoutData(new GridData(GridData.FILL_BOTH));
        rowComposite = new Composite(unReconciledScrollable, SWT.NONE);
        GridLayout rowLayout = new GridLayout(5, false);
        rowComposite.setLayout(rowLayout);
        unReconciledScrollable.setExpandVertical(true);
        unReconciledScrollable.setContent(rowComposite);
    }

    private void buildTopView() {
        Group topGroup = new Group(parent, SWT.BORDER);
        GridData gridData = new GridData(GridData.CENTER);
        gridData.horizontalAlignment = GridData.CENTER;
        gridData.horizontalSpan = 2;
        topGroup.setData(gridData);
        GridLayout topLayout = new GridLayout(5, false);
        topLayout.horizontalSpacing = 10;
        topGroup.setLayout(topLayout);
        Button editRecButton = new Button(topGroup, SWT.PUSH);
        editRecButton.setText("Edit...");
        editRecButton.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event arg0) {
                editReconciliation();
            }
        });
        currentLabel = new Label(topGroup, SWT.LEFT);
        finalLabel = new Label(topGroup, SWT.LEFT);
        diffLabel = new Label(topGroup, SWT.LEFT);
        finishButton = new Button(topGroup, SWT.PUSH);
        finishButton.setText("Finish");
        finishButton.setEnabled(true);
        finishButton.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event arg0) {
                if (finish()) {
                    getViewSite().getPage().hideView(ReconcileView.this);
                    initialized = false;
                }
            }
        });
    }

    protected void editReconciliation() {
        ReconciliationEditDialog dlg = new ReconciliationEditDialog(getSite().getShell());
        dlg.setAccount(reconciliation.getAccount());
        dlg.setDate(reconciliation.getDate());
        dlg.setEndAmount(reconciliation.getEndAmount());
        dlg.setRecurse(reconciliation.isRecurse());
        dlg.setShowAbort();
        int result = dlg.open();
        if (result == IDialogConstants.OK_ID) {
            reconciliation.setDate(dlg.getDate());
            reconciliation.setEndAmount(dlg.getEndAmount());
            reconciliation.setRecurse(dlg.isRecurse());
            if (reconciliation.isRecurse()) {
                for (Split s : reconciliation.getSplits().toArray(new Split[0])) {
                    if (s.owner != reconciliation.getAccount()) reconciliation.getSplits().remove(s);
                }
            }
            populateRows();
            updateControls();
        } else if (result == IDialogConstants.ABORT_ID) {
            initialized = false;
            reconciliation.getAccount().setReconciliation(null);
            getViewSite().getPage().hideView(ReconcileView.this);
            reconciliation.getAccount().changed(null);
        }
    }

    private void populateRows() {
        if (!initialized) return;
        for (Control c : rowComposite.getChildren()) c.dispose();
        for (final Split s : reconciliation.getUnreconciledSplits()) {
            final Label dateLabel = new Label(rowComposite, SWT.LEFT);
            dateLabel.setText(s.trans.date.toString("MM-dd-YYYY"));
            final Label descLabel = new Label(rowComposite, SWT.LEFT);
            descLabel.setText(s.trans.description);
            final Label fromLabel = new Label(rowComposite, SWT.LEFT);
            fromLabel.setText(s.owner.getDisplayName());
            final Label toLabel = new Label(rowComposite, SWT.LEFT);
            toLabel.setText(s.trans.getOther(s).owner.getDisplayName());
            final Label amountLabel = new Label(rowComposite, SWT.RIGHT);
            amountLabel.setText(NumberFormat.getCurrencyInstance().format(s.value));
            boolean in = reconciliation.getSplits().contains(s);
            Color c = in ? Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY) : null;
            dateLabel.setForeground(c);
            descLabel.setForeground(c);
            fromLabel.setForeground(c);
            toLabel.setForeground(c);
            amountLabel.setForeground(c);
            Listener l = new Listener() {

                public void handleEvent(Event arg0) {
                    boolean in = toggleRow(s);
                    Color c = in ? Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY) : null;
                    dateLabel.setForeground(c);
                    descLabel.setForeground(c);
                    fromLabel.setForeground(c);
                    toLabel.setForeground(c);
                    amountLabel.setForeground(c);
                }
            };
            dateLabel.addListener(SWT.MouseUp, l);
            descLabel.addListener(SWT.MouseUp, l);
            fromLabel.addListener(SWT.MouseUp, l);
            toLabel.addListener(SWT.MouseUp, l);
            amountLabel.addListener(SWT.MouseUp, l);
        }
        rowComposite.pack();
        unReconciledScrollable.setMinSize(rowComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        unReconciledScrollable.getParent().layout(true, true);
        renderStatement();
    }

    protected boolean toggleRow(Split s) {
        ArrayList<Split> splits = reconciliation.getSplits();
        boolean currentlyIn = splits.contains(s);
        if (currentlyIn) {
            splits.remove(s);
        } else {
            splits.add(s);
        }
        reconciliation.getAccount().changed(null);
        updateControls();
        return !currentlyIn;
    }

    protected boolean finish() {
        if (!reconciliation.canFinish()) {
            MessageDialog.openError(getSite().getShell(), "???", "You shouldn't be allowed to finish... doesn't add up!");
            return false;
        }
        initialized = false;
        reconciliation.getAccount().setReconciliation(null);
        getViewSite().getPage().hideView(ReconcileView.this);
        for (Split s : reconciliation.getSplits()) {
            s.reconciled = true;
            s.owner.changed(s.trans);
            s.trans.getOther(s).owner.changed(s.trans);
        }
        reconciliation.getAccount().changed(null);
        return true;
    }

    private void updateControls() {
        if (!initialized) return;
        NumberFormat format = NumberFormat.getCurrencyInstance();
        currentLabel.setText("Current: " + format.format(reconciliation.getCurrentAmount()));
        finalLabel.setText("Final: " + format.format(reconciliation.getEndAmount()));
        diffLabel.setText("Diff: " + format.format(reconciliation.getDifference()));
        finishButton.setEnabled(reconciliation.canFinish());
        parent.layout(true, true);
    }

    public void cancelRowChange() {
        splitRow.fillData();
        splitRow.disableWidgets();
    }

    public boolean saveRow() {
        if (!splitRow.validate()) return false;
        System.out.println("Saving row:");
        Account owner = splitRow.getAccount();
        Account other = splitRow.getOtherAccount();
        BigDecimal value = splitRow.getValue();
        LocalDate date = splitRow.getDate();
        String desc = splitRow.getDescription();
        Transaction tx = new Transaction(owner, other, desc, value, date);
        for (Split s : tx.splits) if (reconciliation.isInHierarchy(s.owner)) reconciliation.getSplits().add(s);
        owner.changed(tx);
        other.changed(tx);
        populateRows();
        ArrayList<StatementEntry> matchingStatementEntries = reconciliation.getMatchingStatementEntries();
        if (matchingStatementEntries.isEmpty()) {
            splitRow.fillData();
            splitRow.setDescActive();
        } else {
            splitRow.fillAndActivate(matchingStatementEntries.get(0));
        }
        return false;
    }

    public boolean shouldActivate(SplitRowView view) {
        return true;
    }

    public boolean showBothAccounts() {
        return true;
    }
}
