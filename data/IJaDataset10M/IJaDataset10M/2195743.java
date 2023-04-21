package com.schinzer.fin.view.base.forms;

import java.util.Set;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;
import com.schinzer.fin.model.base.Account;
import com.schinzer.fin.model.base.banks.Bank;
import com.schinzer.fin.model.base.banks.BankDeDE;

public class BankDeDEDetailsForm extends ValidatorForm {

    private static final long serialVersionUID = 9007516490968332391L;

    private static Log log = LogFactory.getLog(BankDeDEDetailsForm.class);

    private BankDeDE bank;

    public Set<Account> getAccounts() {
        return bank.getAccounts();
    }

    public Bank getBank() {
        return bank;
    }

    public String getBankCode() {
        return bank.getBankCode();
    }

    public String getBlz() {
        return bank.getBlz();
    }

    public String getCity() {
        return bank.getCity();
    }

    public String getId() {
        return new Long(bank.getId()).toString();
    }

    public String getName() {
        return bank.getName();
    }

    public String getNoAccounts() {
        return new Integer(bank.getNoAccounts()).toString();
    }

    @Override
    public void reset(ActionMapping mp, HttpServletRequest rq) {
        super.reset(mp, rq);
        resetAll();
    }

    @Override
    public void reset(ActionMapping mp, ServletRequest rq) {
        super.reset(mp, rq);
        resetAll();
    }

    private void resetAll() {
        bank = new BankDeDE();
    }

    public void setAccounts(Set<Account> accounts) {
        log.debug("setAccounts(" + accounts.toString() + ")");
        bank.setAccounts(accounts);
    }

    public void setBank(BankDeDE bank) {
        log.debug("setBank(" + bank.toString() + ")");
        this.bank = bank;
    }

    public void setBlz(String blz) {
        log.debug("setBlz(" + blz + ")");
        bank.setBlz(blz);
    }

    public void setCity(String city) {
        log.debug("setCity(" + city + ")");
        bank.setCity(city);
    }

    public void setId(long id) {
        log.debug("setId(long)");
        bank.setId(id);
    }

    public void setId(String idStr) {
        log.debug("setId(" + idStr + ")");
        bank.setId(Long.parseLong(idStr));
    }

    public void setName(String name) {
        bank.setName(name);
    }
}
