package com.finavi.model;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
public class Loan implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7832011507623970023L;

    @Id
    @GeneratedValue
    private long id;

    @OneToOne(mappedBy = "loan")
    private Bank bank;

    private double minAmount;

    private double maxAmount;

    private int minYears;

    private int maxYears;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LoanFee loanFee;

    private double accountManagementFee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "loan_Conditions", joinColumns = @JoinColumn(name = "loan_id"), inverseJoinColumns = @JoinColumn(name = "condition_id"))
    private Set<LoanConditions> conditions;

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private LivingWages livingWagesRates;

    public Loan() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(double minAmount) {
        this.minAmount = minAmount;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public int getMinYears() {
        return minYears;
    }

    public void setMinYears(int minYears) {
        this.minYears = minYears;
    }

    public int getMaxYears() {
        return maxYears;
    }

    public void setMaxYears(int maxYears) {
        this.maxYears = maxYears;
    }

    public LoanFee getLoanFee() {
        return loanFee;
    }

    public void setLoanFee(LoanFee loanFee) {
        this.loanFee = loanFee;
    }

    public double getAccountManagementFee() {
        return accountManagementFee;
    }

    public void setAccountManagementFee(double accountManagementFee) {
        this.accountManagementFee = accountManagementFee;
    }

    public Set<LoanConditions> getConditions() {
        return conditions;
    }

    public void setConditions(Set<LoanConditions> conditions) {
        this.conditions = conditions;
    }

    public void setLivingWagesRates(LivingWages livingWagesRates) {
        this.livingWagesRates = livingWagesRates;
    }

    public LivingWages getLivingWagesRates() {
        return livingWagesRates;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public Bank getBank() {
        return bank;
    }
}
