package org.jazzteam.studenthelper.model;

public class EmployeeAccount extends Employee {

    protected String cardNumber = null;

    protected String password = null;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
