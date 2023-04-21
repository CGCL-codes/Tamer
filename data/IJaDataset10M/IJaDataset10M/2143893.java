package net.sf.rcpmoney.ui.views;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import net.sf.rcpmoney.application.Model;
import net.sf.rcpmoney.entity.Account;
import net.sf.rcpmoney.entity.CategoryType;
import net.sf.rcpmoney.entity.Credit;
import net.sf.rcpmoney.entity.Recipient;
import net.sf.rcpmoney.entity.SplitElement;
import net.sf.rcpmoney.repository.CreditRepository;
import net.sf.rcpmoney.ui.dialogs.SplitElementDialog;
import net.sf.rcpmoney.ui.provider.AccountContentProvider;
import net.sf.rcpmoney.ui.provider.AccountLabelProvider;
import net.sf.rcpmoney.ui.provider.RecipientContentProvider;
import net.sf.rcpmoney.ui.provider.RecipientLabelProvider;
import net.sf.rcpmoney.ui.provider.SplitElementContentProvider;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class CreditView extends ViewPart implements Observer {

    private Credit currentCredit = new Credit();

    public CreditView() {
        super();
        createCredit();
    }

    private void createCredit() {
        currentCredit = new Credit();
        currentCredit.setCategoriesList(new HashSet<SplitElement>());
    }

    public static final String ID = "net.sf.rcpmoney.ui.views.CreditView";

    private Composite top = null;

    private Label labelDate = null;

    private Text textDate = null;

    private Label labelAmount = null;

    private Text textAmount = null;

    private Label labelAccount = null;

    private Combo comboAccount = null;

    private Label labelRecipient = null;

    private Combo comboRecipient = null;

    private Table tableSplit = null;

    private Button buttonSave = null;

    private ComboViewer comboAccountViewer = null;

    private ComboViewer comboRecipientViewer = null;

    private TableViewer tableViewer = null;

    private Button buttonAddSplitElement = null;

    @Override
    public void createPartControl(Composite parent) {
        GridData gridData11 = new GridData();
        gridData11.horizontalSpan = 4;
        gridData11.verticalAlignment = GridData.CENTER;
        gridData11.horizontalAlignment = GridData.CENTER;
        GridData gridData2 = new GridData();
        gridData2.horizontalAlignment = GridData.FILL;
        gridData2.verticalAlignment = GridData.CENTER;
        GridData gridData1 = new GridData();
        gridData1.verticalAlignment = GridData.CENTER;
        gridData1.grabExcessHorizontalSpace = false;
        gridData1.horizontalSpan = 4;
        gridData1.horizontalAlignment = GridData.CENTER;
        GridData gridData = new GridData();
        gridData.horizontalSpan = 4;
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.grabExcessHorizontalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 4;
        top = new Composite(parent, SWT.NONE);
        top.setLayout(gridLayout);
        labelDate = new Label(top, SWT.NONE);
        labelDate.setText("Date");
        textDate = new Text(top, SWT.BORDER);
        labelAmount = new Label(top, SWT.NONE);
        labelAmount.setText("Montant");
        textAmount = new Text(top, SWT.BORDER);
        textAmount.setLayoutData(gridData2);
        labelRecipient = new Label(top, SWT.NONE);
        labelRecipient.setText("B�n�ficiaire");
        createComboRecipient();
        labelAccount = new Label(top, SWT.NONE);
        labelAccount.setText("Compte");
        createComboAccount();
        buttonAddSplitElement = new Button(top, SWT.NONE);
        buttonAddSplitElement.setText("Ajouter une cat�gorie");
        buttonAddSplitElement.setLayoutData(gridData11);
        final Shell shell = parent.getShell();
        buttonAddSplitElement.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
                System.out.println("widgetSelected()");
                SplitElementDialog dialog = new SplitElementDialog(shell, CategoryType.CREDIT);
                SplitElement tmp = new SplitElement();
                Set<SplitElement> set = currentCredit.getCategoriesSet();
                BigDecimal total = BigDecimal.ZERO;
                for (SplitElement splitElement : set) {
                    total = total.add(splitElement.getAmount());
                }
                tmp.setAmount(currentCredit.getAmount().subtract(total));
                dialog.setSplitElement(tmp);
                dialog.setBlockOnOpen(true);
                int result = dialog.open();
                if (result == Window.OK) {
                    System.out.println("OK");
                    System.out.println(dialog.getSplitElement());
                    currentCredit.addSplitElement(dialog.getSplitElement());
                    tableViewer.refresh();
                } else if (result == Window.CANCEL) {
                    System.out.println("Cancel");
                } else {
                    System.out.println("????");
                }
            }
        });
        tableSplit = new Table(top, SWT.NONE);
        tableSplit.setHeaderVisible(true);
        tableSplit.setLayoutData(gridData);
        tableSplit.setLinesVisible(true);
        tableViewer = new TableViewer(tableSplit);
        createColumns();
        getSite().setSelectionProvider(tableViewer);
        tableViewer.setContentProvider(new SplitElementContentProvider());
        tableViewer.setInput(currentCredit.getCategoriesSet());
        buttonSave = new Button(top, SWT.NONE);
        buttonSave.setText("Sauvegarder");
        buttonSave.setLayoutData(gridData1);
        buttonSave.addSelectionListener(new org.eclipse.swt.events.SelectionAdapter() {

            public void widgetSelected(SelectionEvent e) {
                updateCredit();
                System.out.println("" + currentCredit);
                Model currentModel = Model.getInstance();
                CreditRepository repository = currentModel.getCreditRepository();
                currentModel.begin();
                repository.save(currentCredit);
                currentModel.commit();
                clearUI();
            }
        });
        bindValues();
        Model.getInstance().addObserver(this);
    }

    private void clearUI() {
        currentCredit.setCategoriesList(new HashSet<SplitElement>());
        textDate.setText("");
        textAmount.setText("0");
        comboRecipient.deselectAll();
        comboAccount.deselectAll();
        tableViewer.setInput(currentCredit.getCategoriesSet());
    }

    private void createColumns() {
        String[] titles = { "Cat�gorie", "Description", "Montant" };
        int[] bounds = { 150, 60, 100 };
        TableViewerColumn col = createTableViewerColumn(titles[0], bounds[0]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                SplitElement split = (SplitElement) element;
                return split.getCategory().getFullName();
            }
        });
        col = createTableViewerColumn(titles[1], bounds[1]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                SplitElement split = (SplitElement) element;
                return split.getDescription();
            }
        });
        col = createTableViewerColumn(titles[2], bounds[2]);
        col.setLabelProvider(new ColumnLabelProvider() {

            @Override
            public String getText(Object element) {
                SplitElement split = (SplitElement) element;
                return split.getAmount().toString();
            }
        });
    }

    private TableViewerColumn createTableViewerColumn(String title, int bound) {
        final TableViewerColumn viewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
        final TableColumn column = viewerColumn.getColumn();
        column.setText(title);
        column.setWidth(bound);
        column.setResizable(true);
        column.setMoveable(true);
        return viewerColumn;
    }

    private void bindValues() {
        DataBindingContext bindingContext = new DataBindingContext();
        IObservableValue myModel = PojoProperties.value(Credit.class, "date").observe(currentCredit);
        IObservableValue myWidget = WidgetProperties.text(SWT.Modify).observe(textDate);
        bindingContext.bindValue(myWidget, myModel);
        myModel = PojoProperties.value(Credit.class, "amount").observe(currentCredit);
        myWidget = WidgetProperties.text(SWT.Modify).observe(textAmount);
        bindingContext.bindValue(myWidget, myModel);
    }

    @Override
    public void setFocus() {
    }

    /**
	 * This method initializes comboAccount
	 * 
	 */
    private void createComboAccount() {
        GridData gridData4 = new GridData();
        gridData4.widthHint = 70;
        comboAccount = new Combo(top, SWT.NONE);
        comboAccount.setLayoutData(gridData4);
        comboAccountViewer = new ComboViewer(comboAccount);
        comboAccountViewer.setContentProvider(new AccountContentProvider());
        comboAccountViewer.setLabelProvider(new AccountLabelProvider());
        comboAccountViewer.setInput(Model.getInstance().getAccountRepository());
    }

    /**
	 * This method initializes comboRecipient
	 * 
	 */
    private void createComboRecipient() {
        GridData gridData3 = new GridData();
        gridData3.horizontalAlignment = GridData.FILL;
        gridData3.verticalAlignment = GridData.CENTER;
        comboRecipient = new Combo(top, SWT.NONE);
        comboRecipient.setLayoutData(gridData3);
        comboRecipientViewer = new ComboViewer(comboRecipient);
        comboRecipientViewer.setContentProvider(new RecipientContentProvider());
        comboRecipientViewer.setLabelProvider(new RecipientLabelProvider());
        comboRecipientViewer.setInput(Model.getInstance().getRecipientRepository());
    }

    private void updateCredit() {
        IStructuredSelection accountSelection = (IStructuredSelection) comboAccountViewer.getSelection();
        if (!accountSelection.isEmpty()) {
            Account account = (Account) accountSelection.getFirstElement();
            currentCredit.setTo(account);
        }
        IStructuredSelection recipientSelection = (IStructuredSelection) comboRecipientViewer.getSelection();
        if (!recipientSelection.isEmpty()) {
            Recipient recipient = (Recipient) recipientSelection.getFirstElement();
            currentCredit.setFrom(recipient);
        }
    }

    @Override
    public void update(Observable arg0, Object arg1) {
        comboAccountViewer.refresh();
        comboRecipientViewer.refresh();
    }
}
