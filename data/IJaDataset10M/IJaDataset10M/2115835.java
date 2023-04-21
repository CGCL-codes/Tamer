package org.orbeon.faces.example.cardemo;

import javax.faces.component.SelectItem;
import javax.faces.context.FacesContext;
import java.util.ResourceBundle;

public class CustomerBean extends Object {

    String title = null;

    SelectItem mr = null;

    SelectItem mrs = null;

    SelectItem ms = null;

    String firstName = null;

    String middleInitial = null;

    String lastName = null;

    String mailingAddress = null;

    String city = null;

    String state = null;

    int zip;

    String month = null;

    String year = null;

    public CustomerBean() {
        super();
    }

    public void setMr(SelectItem mR) {
        this.mr = mR;
    }

    public SelectItem getMr() {
        ResourceBundle rb = ResourceBundle.getBundle("org/orbeon/faces/example/cardemo/Resources", (FacesContext.getCurrentInstance().getLocale()));
        String mRTitle = (String) rb.getObject("mrLabel");
        return new SelectItem(mRTitle, mRTitle, mRTitle);
    }

    public void setMrs(SelectItem mRs) {
        this.mrs = mRs;
    }

    public SelectItem getMrs() {
        ResourceBundle rb = ResourceBundle.getBundle("org/orbeon/faces/example/cardemo/Resources", (FacesContext.getCurrentInstance().getLocale()));
        String mRsTitle = (String) rb.getObject("mrsLabel");
        return new SelectItem(mRsTitle, mRsTitle, mRsTitle);
    }

    public void setMs(SelectItem mS) {
        this.ms = mS;
    }

    public SelectItem getMs() {
        ResourceBundle rb = ResourceBundle.getBundle("org/orbeon/faces/example/cardemo/Resources", (FacesContext.getCurrentInstance().getLocale()));
        String mSTitle = (String) rb.getObject("msLabel");
        return new SelectItem(mSTitle, mSTitle, mSTitle);
    }

    public void setFirstName(String first) {
        firstName = first;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setMiddleInitial(String mI) {
        middleInitial = mI;
    }

    public String getMiddleInitial() {
        return middleInitial;
    }

    public void setLastName(String last) {
        lastName = last;
    }

    public String getLastName() {
        return lastName;
    }

    public void setMailingAddress(String mA) {
        mailingAddress = mA;
    }

    public String getMailingAddress() {
        return mailingAddress;
    }

    public void setCity(String cty) {
        city = cty;
    }

    public String getCity() {
        return city;
    }

    public void setState(String sT) {
        state = sT;
    }

    public String getState() {
        return state;
    }

    public void setZip(int zipCode) {
        zip = zipCode;
    }

    public int getZip() {
        return zip;
    }

    public void setMonth(String mth) {
        month = mth;
    }

    public String getMonth() {
        return month;
    }

    public void setYear(String yr) {
        year = yr;
    }

    public String getYear() {
        return year;
    }
}
