package br.com.visualmidia.business;

import java.io.Serializable;

/**
 * @author   Lucas
 */
public class ExtractBankItem implements Serializable {

    private static final long serialVersionUID = -5054816686708032237L;

    /**
	 * @uml.property  name="value"
	 */
    private String value;

    /**
	 * @uml.property  name="description"
	 */
    private String description;

    private GDDate date;

    /**
	 * @uml.property  name="id"
	 */
    private String id;

    /**
	 * @uml.property  name="isCredit"
	 */
    private boolean isCredit;

    private Operation relatedOperation;

    private Incoming relatedIncoming;

    private Expenditure relatedExpenditure;

    /**
	 * @uml.property  name="accountNumber"
	 */
    private String accountNumber;

    private Account account;

    public ExtractBankItem(String id, String description, String value, GDDate date) {
        this.id = id;
        this.date = date;
        this.description = description;
        if (value.contains("-")) {
            this.value = value.split("-")[1].replace(".", ",");
            this.isCredit = false;
        } else {
            this.value = value.replace(".", ",");
            this.isCredit = true;
        }
    }

    /**
	 * @return
	 * @uml.property  name="description"
	 */
    public String getDescription() {
        return description;
    }

    /**
	 * @param description
	 * @uml.property  name="description"
	 */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
	 * @return
	 * @uml.property  name="value"
	 */
    public String getValue() {
        return value;
    }

    /**
	 * @param value
	 * @uml.property  name="value"
	 */
    public void setValue(String value) {
        if (value.contains("-")) {
            this.value = value.split("-")[1].replace(".", ",");
            this.isCredit = false;
        } else {
            this.value = value.replace(".", ",");
            this.isCredit = true;
        }
    }

    /**
	 * @return
	 * @uml.property  name="date"
	 */
    public GDDate getDate() {
        return date;
    }

    /**
	 * @param date
	 * @uml.property  name="date"
	 */
    public void setDate(GDDate date) {
        this.date = date;
    }

    /**
	 * @return
	 * @uml.property  name="id"
	 */
    public String getId() {
        return id;
    }

    /**
	 * @param id
	 * @uml.property  name="id"
	 */
    public void setId(String id) {
        this.id = id;
    }

    /**
	 * @return
	 * @uml.property  name="isCredit"
	 */
    public boolean isCredit() {
        return isCredit;
    }

    /**
	 * @param isCredit
	 * @uml.property  name="isCredit"
	 */
    public void setCredit(boolean isCredit) {
        this.isCredit = isCredit;
    }

    /**
	 * @return
	 * @uml.property  name="relatedOperation"
	 */
    public Operation getRelatedOperation() {
        return relatedOperation;
    }

    /**
	 * @param relatedOperation
	 * @uml.property  name="relatedOperation"
	 */
    public void setRelatedOperation(Operation relatedOperation) {
        this.relatedOperation = relatedOperation;
    }

    /**
	 * @param relatedExpenditure
	 * @uml.property  name="relatedExpenditure"
	 */
    public void setRelatedExpenditure(Expenditure relatedExpenditure) {
        this.relatedExpenditure = relatedExpenditure;
    }

    /**
	 * @param relatedIncoming
	 * @uml.property  name="relatedIncoming"
	 */
    public void setRelatedIncoming(Incoming relatedIncoming) {
        this.relatedIncoming = relatedIncoming;
    }

    /**
	 * @return
	 * @uml.property  name="relatedExpenditure"
	 */
    public Expenditure getRelatedExpenditure() {
        return relatedExpenditure;
    }

    /**
	 * @return
	 * @uml.property  name="relatedIncoming"
	 */
    public Incoming getRelatedIncoming() {
        return relatedIncoming;
    }

    /**
	 * @param accountNumber
	 * @uml.property  name="accountNumber"
	 */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
	 * @return
	 * @uml.property  name="accountNumber"
	 */
    public String getAccountNumber() {
        if (accountNumber == null) {
            return "";
        }
        return accountNumber;
    }

    /**
	 * @param account
	 * @uml.property  name="account"
	 */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
	 * @return
	 * @uml.property  name="account"
	 */
    public Account getAccount() {
        return account;
    }

    public boolean isLinked() {
        return relatedOperation != null || relatedExpenditure != null || relatedIncoming != null;
    }
}
