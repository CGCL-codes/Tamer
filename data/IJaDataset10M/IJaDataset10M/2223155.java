package org.lcelb.accounts.manager.ui.extensions.wizards.pages;

import java.util.Date;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.lcelb.accounts.manager.common.ICommonConstants;
import org.lcelb.accounts.manager.common.helper.MiscConverter;
import org.lcelb.accounts.manager.data.AbstractOwner;
import org.lcelb.accounts.manager.data.Account;
import org.lcelb.accounts.manager.data.helper.DataHelper;
import org.lcelb.accounts.manager.ui.extensions.message.Messages;
import org.lcelb.accounts.manager.ui.wizards.pages.AbstractDefaultPage;

/**
 * @author fournier
 * 
 * 11 janv. 07
 */
public class TransferPage extends AbstractDefaultPage {

    private AbstractOwner _owner;

    private Account _selectedSourceAccount;

    private Account _selectedTargetAccount;

    private String _transferDayValue;

    private String _transferMonthValue;

    private String _transferYearValue;

    private String _amount;

    private String _label;

    /**
   * Constructor.
   * 
   * @param pageName_p
   * @param owner_p
   */
    public TransferPage(String pageName_p, AbstractOwner owner_p) {
        super(pageName_p);
        _owner = owner_p;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.wizards.pages.AbstractDefaultPage#createPageArea(org.eclipse.swt.widgets.Composite)
   */
    @Override
    protected void createPageArea(Composite parent_p) {
        Group accountSelectionArea = createGroup(parent_p, Messages.TransferPage_AccountSelectionGroup_Title, Messages.TransferPage_AccountSelectionGroup_Tooltip, 1);
        createAccountSelectionArea(accountSelectionArea);
        Group transferDateArea = createGroup(parent_p, Messages.TransferPage_DateGroup_Title, Messages.TransferPage_DateGroup_Tooltip, 3);
        createTransferDate(transferDateArea);
        Group transferAmountAndLabelArea = createGroup(parent_p, Messages.TransferPage_AmountAndLabelGroup_Title, Messages.TransferPage_DateGroup_Tooltip, 2);
        createTransferAmountAndLabelArea(transferAmountAndLabelArea);
    }

    /**
   * Create amount and label area.
   * 
   * @param transferAmountAndLabelArea_p void
   */
    private void createTransferAmountAndLabelArea(Group transferAmountAndLabelArea_p) {
        createLabeledTextFor(transferAmountAndLabelArea_p, Messages.TransferPage_Amount_Title, ICommonConstants.EMPTY_STRING, new Listener() {

            public void handleEvent(Event event_p) {
                _amount = ((Text) event_p.widget).getText();
                updateButtons();
            }
        });
        createLabeledTextFor(transferAmountAndLabelArea_p, Messages.TransferPage_Label_Title, ICommonConstants.EMPTY_STRING, new Listener() {

            public void handleEvent(Event event_p) {
                _label = ((Text) event_p.widget).getText();
                updateButtons();
            }
        });
    }

    /**
   * Create date area
   * 
   * @param parent_p void
   */
    private void createTransferDate(Composite parent_p) {
        Date currentDate = new Date();
        _transferDayValue = String.valueOf(MiscConverter.getDay(currentDate));
        createText(parent_p, _transferDayValue, Messages.TransferPage_DateDay_Title, 2, new ModifyListener() {

            public void modifyText(ModifyEvent event_p) {
                _transferDayValue = ((Text) event_p.widget).getText();
                updateButtons();
            }
        });
        _transferMonthValue = getMonth(MiscConverter.getMonth(currentDate));
        createText(parent_p, _transferMonthValue, Messages.TransferPage_DateMonth_Title, 2, new ModifyListener() {

            public void modifyText(ModifyEvent event_p) {
                _transferMonthValue = ((Text) event_p.widget).getText();
                updateButtons();
            }
        });
        _transferYearValue = String.valueOf(MiscConverter.getYear(currentDate));
        createText(parent_p, _transferYearValue, Messages.TransferPage_DateYear_Title, 4, new ModifyListener() {

            public void modifyText(ModifyEvent event_p) {
                _transferYearValue = ((Text) event_p.widget).getText();
                updateButtons();
            }
        });
    }

    /**
   * Create account selection area.
   * 
   * @param parent_p void
   */
    private void createAccountSelectionArea(Composite parent_p) {
        Account[] accounts = DataHelper.getAllAccounts(_owner);
        Listener selectedSourceAccountListener = new Listener() {

            public void handleEvent(Event event_p) {
                Combo selectedCombo = (Combo) event_p.widget;
                _selectedSourceAccount = (Account) selectedCombo.getData(selectedCombo.getText());
                updateButtons();
            }
        };
        Combo source = createAccountCombo(parent_p, Messages.TransferPage_SelectedSourceAccount, selectedSourceAccountListener, accounts, false);
        String selectedAccountNumber = source.getText();
        _selectedSourceAccount = (Account) source.getData(selectedAccountNumber);
        Listener selectedTargetAccountListener = new Listener() {

            public void handleEvent(Event event_p) {
                Combo selectedCombo = (Combo) event_p.widget;
                _selectedTargetAccount = (Account) selectedCombo.getData(selectedCombo.getText());
            }
        };
        createAccountCombo(parent_p, Messages.TransferPage_SelectedTargetAccount, selectedTargetAccountListener, accounts, true);
    }

    /**
   * Create a combo to select an account.
   * 
   * @param parent_p
   * @param accounts_p void
   */
    private Combo createAccountCombo(Composite parent_p, String label_p, Listener accountSelectionListener_p, Account[] accounts_p, boolean withEmptyItem_p) {
        Label accountComboLabel = new Label(parent_p, SWT.NULL);
        accountComboLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
        accountComboLabel.setText(label_p);
        Combo accountCombo = new Combo(parent_p, SWT.READ_ONLY | SWT.DROP_DOWN);
        accountCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        accountCombo.addListener(SWT.Selection, accountSelectionListener_p);
        if (withEmptyItem_p) {
            accountCombo.add(ICommonConstants.EMPTY_STRING);
        }
        for (int i = 0; i < accounts_p.length; i++) {
            String accountNumber = accounts_p[i].getNumber();
            accountCombo.add(accountNumber);
            accountCombo.setData(accountNumber, accounts_p[i]);
        }
        accountCombo.select(0);
        return accountCombo;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.wizards.pages.AbstractDefaultPage#getCompleteStatus()
   */
    @Override
    protected boolean getCompleteStatus() {
        boolean result = false;
        result = (null != _selectedSourceAccount);
        if (result) {
            result &= (null != _transferDayValue) && !ICommonConstants.EMPTY_STRING.equals(_transferDayValue);
            result &= (null != _transferMonthValue) && !ICommonConstants.EMPTY_STRING.equals(_transferMonthValue);
            result &= (null != _transferYearValue) && !ICommonConstants.EMPTY_STRING.equals(_transferYearValue);
        }
        if (result) {
            result &= (null != getDate());
        }
        result &= (null != _amount);
        if (result) {
            result &= (-1 != getAmount());
        }
        if (result) {
            result &= (null != _label) && !ICommonConstants.EMPTY_STRING.equals(_label);
        }
        return result;
    }

    /**
   * Return the transfer date.
   * 
   * @return Date.
   */
    public Date getDate() {
        StringBuffer dateBuffer = new StringBuffer(_transferDayValue);
        dateBuffer.append(ICommonConstants.DATE_SEPARATOR_IN_PATTERN);
        dateBuffer.append(_transferMonthValue);
        dateBuffer.append(ICommonConstants.DATE_SEPARATOR_IN_PATTERN);
        dateBuffer.append(_transferYearValue);
        return MiscConverter.convertDate(dateBuffer.toString());
    }

    /**
   * Return UI month string in [1,12].
   * 
   * @param month_p
   * @return String
   */
    private String getMonth(int month_p) {
        return String.valueOf(month_p + 1);
    }

    /**
   * @see org.lcelb.accounts.manager.ui.wizards.pages.AbstractDefaultPage#getPageDescription()
   */
    @Override
    protected String getPageDescription() {
        return Messages.TransferPage_Description;
    }

    /**
   * @see org.lcelb.accounts.manager.ui.wizards.pages.AbstractDefaultPage#getPageTitle()
   */
    @Override
    protected String getPageTitle() {
        return Messages.TransferPage_Title;
    }

    /**
   * Return the account used as source of the transfer.
   * 
   * @return the selectedSourceAccount
   */
    public Account getSelectedSourceAccount() {
        return _selectedSourceAccount;
    }

    /**
   * Return the account used as target of the transfer.
   * 
   * @return the selectedTargetAccount
   */
    public Account getSelectedTargetAccount() {
        return _selectedTargetAccount;
    }

    /**
   * Return the transfer amount.
   * 
   * @return the amount
   */
    public double getAmount() {
        double result = -1;
        Number newAmount = null;
        String amountText = MiscConverter.convertAmountToLocaleFormat(_amount);
        newAmount = MiscConverter.convertAmount(amountText);
        if (null != newAmount) {
            result = newAmount.doubleValue();
        }
        return result;
    }

    /**
   * Return the transfer label.
   * 
   * @return the label
   */
    public String getLabel() {
        return _label;
    }
}
