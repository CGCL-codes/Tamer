package net.sf.josas.ui.swing.control;

import net.sf.josas.business.BankService;
import net.sf.josas.model.AccountModel;
import net.sf.josas.om.Account;
import net.sf.josas.om.BankingAccount;
import net.sf.josas.persistence.entities.Bank;
import net.sf.josas.ui.common.DefaultCommonDialogController;
import net.sf.josas.ui.swing.view.AccountPane;

/**
 * Controller for entering or editing accounts.
 *
 * @author frederic
 *
 */
public class AccountController implements DefaultCommonDialogController {

    /** Model. */
    private AccountModel model;

    /** Main view. */
    private AccountPane mainView;

    /** Account. */
    private Account account;

    /** Association id. */
    private long associationId;

    /**
    * @param amodel
    *            country model
    */
    public AccountController(final AccountModel amodel) {
        model = amodel;
        mainView = new AccountPane(this, model);
    }

    /**
    * @return The main view.
    */
    public final AccountPane getView() {
        return mainView;
    }

    /**
    * @param mode
    *            creation or edition mode
    * @return true if fields are OK.
    */
    @Override
    public final boolean checkFields(final int mode) {
        boolean retVal = true;
        switch(mainView.getTypeField().getSelectedIndex()) {
            case AccountModel.BANK_ITEM_IDX:
                BankService bModel = new BankService();
                String bName = (String) mainView.getBankField().getSelectedItem();
                String cName = mainView.getNameField().getText();
                if (bName != null && cName != null && !cName.equals("")) {
                    Bank bank = bModel.getBankByName(bName);
                    account = new BankingAccount(bModel.getBank(bank.getId()), cName);
                    account.setAssociationId(associationId);
                    ((BankingAccount) account).setRib(mainView.getRibField().getText());
                    ((BankingAccount) account).setIban(mainView.getIbanField().getText());
                    model.save(account);
                }
                break;
            default:
                assert false;
        }
        return retVal;
    }

    /**
    * @param id value to set
    */
    public final void setAssociationId(final long id) {
        associationId = id;
    }
}
