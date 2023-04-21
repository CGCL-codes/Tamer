package br.com.visualmidia.ui.importAccount;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TableItem;
import br.com.visualmidia.business.Account;
import br.com.visualmidia.business.Expenditure;
import br.com.visualmidia.business.GDDate;
import br.com.visualmidia.business.Incoming;
import br.com.visualmidia.business.Money;
import br.com.visualmidia.business.Operation;
import br.com.visualmidia.core.MessageConstants;
import br.com.visualmidia.exception.TransactionDateException;
import br.com.visualmidia.persistence.DeleteExtractBankItem;
import br.com.visualmidia.persistence.GetAccountByName;
import br.com.visualmidia.persistence.GetBankAccountsByNumber;
import br.com.visualmidia.persistence.GetExpenditures;
import br.com.visualmidia.persistence.GetIncoming;
import br.com.visualmidia.persistence.GetOperation;
import br.com.visualmidia.persistence.add.AddExtractBankItem;
import br.com.visualmidia.system.GDRegistry;
import br.com.visualmidia.system.GDSystem;
import br.com.visualmidia.ui.MainScreen;
import br.com.visualmidia.ui.controlcenter.ControlCenter;
import br.com.visualmidia.ui.dialog.ChooseAccountImportExtractBankItemDialog;
import br.com.visualmidia.ui.validator.Validator;

/**
 * @author  Lucas
 */
public class ExtractLinkAccountBankControlCenter extends ControlCenter {

    private Label pageLabel;

    private GDRegistry gdRegistry;

    private ExtractImportedTable extractImportedTable;

    private Button importExtractButton;

    private GDSystem system;

    private int accountId;

    protected static final String DEFAULT_MESSAGE = "Para categorizar lan�amentos clique duas vezes em cima de uma transa��o.";

    public ExtractLinkAccountBankControlCenter(CTabFolder parent, int style, MainScreen screen) {
        super(parent, style);
        this.mainScreen = screen;
        this.gdRegistry = GDSystem.getGDRegistry();
        this.system = GDSystem.getInstance();
        this.accountId = 0;
    }

    protected String title() {
        return "Importa��o de Extrato de Contas";
    }

    @Override
    protected ControlCenter getMySelf() {
        return this;
    }

    @Override
    protected void createMessageLabel() {
        screenMessageLabel = new Label(mainPanel, SWT.NULL);
        screenMessageLabel.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
                gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
                gc.fillGradientRectangle(0, 0, mainPanel.getClientArea().width, 30, true);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
                gc.setLineWidth(1);
                gc.drawLine(0, 30, mainPanel.getClientArea().width, 30);
                gc.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
                if (screenMessageLabel.getText().equals("")) {
                    gc.drawText(DEFAULT_MESSAGE, 30, 10, SWT.DRAW_TRANSPARENT);
                    gc.drawImage(new Image(null, "img/exclamation.png"), 9, 9);
                } else {
                    gc.drawText(screenMessageLabel.getText(), 32, 10, SWT.DRAW_TRANSPARENT);
                    gc.drawImage(new Image(null, "img/exclamation.png"), 9, 9);
                }
                gc.dispose();
            }
        });
        FormData data = new FormData();
        data.top = new FormAttachment(0, 0);
        data.left = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.height = 31;
        screenMessageLabel.setLayoutData(data);
    }

    @Override
    protected void createMainPanel() {
        mainPanel = new Composite(shell, SWT.NONE);
        mainPanel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        mainPanel.setLayout(new FormLayout());
        FormData data = new FormData();
        data.left = new FormAttachment(0, 0);
        data.top = new FormAttachment(0, 0);
        data.right = new FormAttachment(100, 0);
        data.bottom = new FormAttachment(100, 0);
        mainPanel.setLayoutData(data);
    }

    @Override
    protected void configureButtons() {
    }

    @Override
    protected void createFields() {
        createPageLabel();
        createImportExtractButton();
        createDeleteButton();
        createExtractImportedTable();
    }

    private void createImportExtractButton() {
        importExtractButton = new Button(mainPanel, SWT.NONE);
        importExtractButton.setText("Importar extrato");
        importExtractButton.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event arg0) {
                createFileDialog();
                extractImportedTable.updateTable();
            }
        });
        FormData data = new FormData();
        data.left = new FormAttachment(0, 11);
        data.bottom = new FormAttachment(100, -5);
        importExtractButton.setLayoutData(data);
    }

    private void createDeleteButton() {
        Button deleteBankItemButton = new Button(mainPanel, SWT.NONE);
        deleteBankItemButton.setText("Excluir itens");
        deleteBankItemButton.addListener(SWT.MouseUp, new Listener() {

            public void handleEvent(Event arg0) {
                if (extractImportedTable.getSelectedItens().length == 0) {
                    setErrorMessage("Selecione um item para excluir");
                }
                deleteItens(extractImportedTable.getSelectedItens());
                extractImportedTable.updateTable();
            }

            private void deleteItens(TableItem[] selectedItens) {
                try {
                    for (TableItem item : selectedItens) {
                        String id = item.getText(0).trim() + " = " + item.getText(2).trim() + " = " + item.getText(5).replace(",", ".").trim();
                        system.execute(new DeleteExtractBankItem(id));
                    }
                } catch (Exception e) {
                    MessageBox box = new MessageBox(parent.getShell(), IMessageProvider.INFORMATION);
                    box.setText("Data Inv�lida");
                    box.setMessage(MessageConstants.TRANSACTION_DATE_EXCEPTION);
                    box.open();
                }
            }
        });
        FormData data = new FormData();
        data.left = new FormAttachment(importExtractButton, 11);
        data.bottom = new FormAttachment(100, -5);
        deleteBankItemButton.setLayoutData(data);
    }

    protected void createFileDialog() {
        FileDialog fileDialog = new FileDialog(new Shell(), SWT.OPEN);
        fileDialog.setFilterNames(new String[] { "Arquivo(*.ofc)" });
        fileDialog.setFilterExtensions(new String[] { "*.ofc" });
        String destZipFile = fileDialog.open();
        if (destZipFile != null) {
            File file = new File(destZipFile);
            parserFileOFC(file);
        }
    }

    public void parserFileOFC(File inFile) {
        String description = null;
        String value = "";
        String bankOperationNumber = null;
        GDDate date = null;
        String inputLine;
        try {
            boolean isCancel = false;
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inFile)));
            while ((inputLine = br.readLine()) != null) {
                if (isCancel) {
                    break;
                }
                String lineTest = inputLine.trim();
                if (lineTest.contains("MEMO")) {
                    description = lineTest.split("<MEMO>")[1];
                } else if (lineTest.contains("TRNAMT")) {
                    value = lineTest.split("<TRNAMT>")[1];
                } else if (lineTest.contains("DTPOSTED")) {
                    int year = Integer.parseInt(lineTest.split("<DTPOSTED>")[1].substring(0, 4));
                    int month = Integer.parseInt(lineTest.split("<DTPOSTED>")[1].substring(4, 6));
                    int day = Integer.parseInt(lineTest.split("<DTPOSTED>")[1].substring(6, 8));
                    date = new GDDate(day, month, year);
                } else if (lineTest.contains("FITID")) {
                    if (lineTest.split("<FITID>").length == 0) {
                        bankOperationNumber += 0 + " = " + date.getFormatedDate().trim() + " = " + value.replace(",", ".").trim();
                        description = "Documento " + 0 + " - " + description;
                    } else {
                        String idOperation = lineTest.split("<FITID>")[1];
                        description = "Documento " + idOperation + " - " + description;
                        bankOperationNumber = idOperation.trim();
                        bankOperationNumber += " = " + date.getFormatedDate().trim() + " = " + value.replace(",", ".").trim();
                    }
                } else if (lineTest.contains("ACCTID")) {
                    String accountNumber = lineTest.split("<ACCTID>")[1];
                    try {
                        List<Account> accounts = (List<Account>) system.query(new GetBankAccountsByNumber(accountNumber));
                        if (accounts.size() > 1) {
                            ChooseAccountImportExtractBankItemDialog dialog = new ChooseAccountImportExtractBankItemDialog(mainScreen.getShell(), accounts);
                            dialog.open();
                            if (dialog.getReturnCode() == TitleAreaDialog.OK) {
                                Account account = (Account) system.query(new GetAccountByName(dialog.getAccountName()));
                                accountId = Integer.parseInt(account.getId());
                            } else {
                                isCancel = true;
                            }
                        } else if (accounts.size() == 1) {
                            Account account = accounts.get(0);
                            accountId = Integer.parseInt(account.getId());
                        } else {
                            MessageBox box = new MessageBox(getDisplay().getShells()[0], IMessageProvider.WARNING);
                            box.setText("Alerta Gerente Digital!");
                            box.setMessage("A conta " + accountNumber + " n�o est� cadastrada no Gerente Digital.");
                            box.open();
                            break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (lineTest.contains("/STMTTRN")) {
                    if (accountId != 0) {
                        Operation operation = getRelatedOperation(bankOperationNumber, description, value, date);
                        if (operation != null) {
                            if (!operation.isOperationLinked()) {
                                system.execute(new AddExtractBankItem(bankOperationNumber, description, value, date, operation, accountId));
                            }
                        } else {
                            Incoming incoming = getRelatedIncoming(bankOperationNumber, description, value, date);
                            if (incoming != null) {
                                system.execute(new AddExtractBankItem(bankOperationNumber, description, value, date, incoming, accountId));
                            } else {
                                Expenditure expenditure = getRelatedExpenditure(bankOperationNumber, description, value, date);
                                if (expenditure != null) {
                                    system.execute(new AddExtractBankItem(bankOperationNumber, description, value, date, expenditure, accountId));
                                } else {
                                    system.execute(new AddExtractBankItem(bankOperationNumber, description, value, date, accountId));
                                }
                            }
                        }
                    } else {
                        setErrorMessage("Extrato escolhido n�o cont�m o padr�o utilizado pelo Gerente Digital.");
                    }
                }
            }
            br.close();
        } catch (TransactionDateException e) {
            MessageBox box = new MessageBox(parent.getShell(), IMessageProvider.INFORMATION);
            box.setText("Data Inv�lida");
            box.setMessage(MessageConstants.TRANSACTION_DATE_EXCEPTION);
            box.open();
        } catch (FileNotFoundException ex) {
            System.out.println("File Not Found Exception: InputFileDeclared");
        } catch (IOException ex) {
            System.out.println("IOException: InputFileDeclared");
        }
    }

    @SuppressWarnings("unchecked")
    private Expenditure getRelatedExpenditure(String bankOperationNumber, String description, String value, GDDate date) {
        try {
            Map<String, Expenditure> expenditures = (Map<String, Expenditure>) system.query(new GetExpenditures());
            for (Expenditure expenditure : expenditures.values()) {
                GDDate incomingDate = new GDDate(expenditure.getNextPaymentDate());
                incomingDate.addDays(-3);
                GDDate endDate = new GDDate(expenditure.getNextPaymentDate());
                endDate.addDays(3);
                if ((date.afterDay(incomingDate) && date.beforeDay(endDate)) && new Money("-" + expenditure.getValue()).equals(new Money(value))) {
                    return expenditure;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Incoming getRelatedIncoming(String bankOperationNumber, String description, String value, GDDate date) {
        try {
            Map<String, Incoming> incomings = (Map<String, Incoming>) system.query(new GetIncoming());
            for (Incoming incoming : incomings.values()) {
                GDDate incomingDate = new GDDate(incoming.getNextPaymentDate());
                incomingDate.addDays(-3);
                GDDate endDate = new GDDate(incoming.getNextPaymentDate());
                endDate.addDays(3);
                if ((date.afterDay(incomingDate) && date.beforeDay(endDate)) && new Money(incoming.getValue()).equals(new Money(value))) {
                    return incoming;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private Operation getRelatedOperation(String bankOperationNumber, String description, String value, GDDate date) {
        try {
            Map<String, Operation> operationMap = (Map<String, Operation>) system.query(new GetOperation());
            for (Operation operation : operationMap.values()) {
                if (operation.getAccount().isBankAccount()) {
                    if ((new GDDate(operation.getDateTime()).equals(date) && new Money(operation.getValue()).equals(new Money(value))) || operation.getBankOperationNumber().equals(bankOperationNumber)) {
                        return operation;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void createPageLabel() {
        pageLabel = new Label(mainPanel, SWT.NONE);
        pageLabel.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
        pageLabel.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_FOREGROUND));
        Font font = gdRegistry.getFont(GDRegistry.FONT_ARIAL_14_BOLD);
        pageLabel.setFont(font);
        pageLabel.setText(title());
        FormData data = new FormData();
        data.left = new FormAttachment(0, 11);
        data.top = new FormAttachment(0, 40);
        pageLabel.setLayoutData(data);
    }

    private void createExtractImportedTable() {
        extractImportedTable = new ExtractImportedTable(mainPanel, SWT.NONE);
        FormData data = new FormData();
        data.left = new FormAttachment(0, 11);
        data.top = new FormAttachment(pageLabel, 10);
        data.right = new FormAttachment(100, -11);
        data.bottom = new FormAttachment(importExtractButton, -5);
        extractImportedTable.setLayoutData(data);
    }

    public void setErrorMessage(String errorMessage) {
        screenMessageLabel.setVisible(false);
        screenMessageLabel.setText(errorMessage);
        screenMessageLabel.setVisible(true);
    }

    @Override
    protected void addRegister() {
    }

    @Override
    public void reloadScreen() {
    }

    @Override
    public void loadScreen(String token) {
    }

    @Override
    public void setFocusOnTabItem(String tabItemText) {
    }

    @Override
    protected Listener createValidationListener() {
        return null;
    }

    @Override
    protected Collection<Validator> validators() {
        return null;
    }

    @Override
    public Label getScreenMessageLabel() {
        return null;
    }

    @Override
    public MainScreen getMainScreen() {
        return null;
    }

    @Override
    public TabFolder getTabFolder() {
        return null;
    }

    public void update() {
        Thread updateThread = new Thread() {

            public void run() {
                getDisplay().syncExec(new Runnable() {

                    public void run() {
                        extractImportedTable.updateTable();
                    }
                });
            }
        };
        updateThread.start();
    }
}
