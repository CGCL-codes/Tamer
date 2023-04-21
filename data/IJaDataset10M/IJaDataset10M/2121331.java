package com.schinzer.fin.basic;

import java.util.Vector;
import java.util.Date;

public class Repeat {

    private long id;

    private Account account;

    private Category category;

    private Date begin;

    private Date end;

    private float amount;

    private String description;

    private String counterpart;

    private int cycle;

    private char cycleUnit;

    private int cycleDay;

    private Vector<RepeatedTransaction> transactions;

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((account == null) ? 0 : account.hashCode());
        result = PRIME * result + Float.floatToIntBits(amount);
        result = PRIME * result + ((begin == null) ? 0 : begin.hashCode());
        result = PRIME * result + ((category == null) ? 0 : category.hashCode());
        result = PRIME * result + ((counterpart == null) ? 0 : counterpart.hashCode());
        result = PRIME * result + cycle;
        result = PRIME * result + cycleDay;
        result = PRIME * result + cycleUnit;
        result = PRIME * result + ((description == null) ? 0 : description.hashCode());
        result = PRIME * result + ((end == null) ? 0 : end.hashCode());
        result = PRIME * result + (int) (id ^ (id >>> 32));
        result = PRIME * result + ((transactions == null) ? 0 : transactions.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Repeat other = (Repeat) obj;
        if (account == null) {
            if (other.account != null) return false;
        } else if (!account.equals(other.account)) return false;
        if (Float.floatToIntBits(amount) != Float.floatToIntBits(other.amount)) return false;
        if (begin == null) {
            if (other.begin != null) return false;
        } else if (!begin.equals(other.begin)) return false;
        if (category == null) {
            if (other.category != null) return false;
        } else if (!category.equals(other.category)) return false;
        if (counterpart == null) {
            if (other.counterpart != null) return false;
        } else if (!counterpart.equals(other.counterpart)) return false;
        if (cycle != other.cycle) return false;
        if (cycleDay != other.cycleDay) return false;
        if (cycleUnit != other.cycleUnit) return false;
        if (description == null) {
            if (other.description != null) return false;
        } else if (!description.equals(other.description)) return false;
        if (end == null) {
            if (other.end != null) return false;
        } else if (!end.equals(other.end)) return false;
        if (id != other.id) return false;
        if (transactions == null) {
            if (other.transactions != null) return false;
        } else if (!transactions.equals(other.transactions)) return false;
        return true;
    }

    public boolean add(RepeatedTransaction rptTxn) {
        return transactions.add(rptTxn);
    }

    public void add(int index, RepeatedTransaction rptTxn) {
        transactions.add(index, rptTxn);
    }

    public void addElement(RepeatedTransaction rptTxn) {
        transactions.addElement(rptTxn);
    }

    public void clear() {
        transactions.clear();
    }

    public boolean contains(RepeatedTransaction rptTxn) {
        return transactions.contains(rptTxn);
    }

    public RepeatedTransaction elementAt(int index) {
        return transactions.elementAt(index);
    }

    public RepeatedTransaction firstElement() {
        return transactions.firstElement();
    }

    public RepeatedTransaction get(int index) {
        return transactions.get(index);
    }

    public int indexOf(RepeatedTransaction rptTxn) {
        return transactions.indexOf(rptTxn);
    }

    public int indexOf(RepeatedTransaction rptTxn, int index) {
        return transactions.indexOf(rptTxn, index);
    }

    public boolean isEmpty() {
        return transactions.isEmpty();
    }

    public RepeatedTransaction lastElement() {
        return transactions.lastElement();
    }

    public boolean remove(RepeatedTransaction rptTxn) {
        return transactions.remove(rptTxn);
    }

    public RepeatedTransaction remove(int index) {
        return transactions.remove(index);
    }

    public void removeAllElements() {
        transactions.removeAllElements();
    }

    public boolean removeElement(RepeatedTransaction rptTxn) {
        return transactions.removeElement(rptTxn);
    }

    public void removeElementAt(int index) {
        transactions.removeElementAt(index);
    }

    public RepeatedTransaction set(int index, RepeatedTransaction rptTxn) {
        return transactions.set(index, rptTxn);
    }

    public void setElementAt(RepeatedTransaction rptTxn, int index) {
        transactions.setElementAt(rptTxn, index);
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCounterpart() {
        return counterpart;
    }

    public void setCounterpart(String counterpart) {
        this.counterpart = counterpart;
    }

    public int getCycle() {
        return cycle;
    }

    public void setCycle(int cycle) {
        this.cycle = cycle;
    }

    public int getCycleDay() {
        return cycleDay;
    }

    public void setCycleDay(int cycleDay) {
        this.cycleDay = cycleDay;
    }

    public char getCycleUnit() {
        return cycleUnit;
    }

    public void setCycleUnit(char cycleUnit) {
        this.cycleUnit = cycleUnit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
