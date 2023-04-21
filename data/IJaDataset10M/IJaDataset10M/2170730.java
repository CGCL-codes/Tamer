package net.googlecode.vasetskiy.task3;

import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Vasetskiy Vlad
 * Date: 29.03.11
 * Time: 13:49
 * To change this template use File | Settings | File Templates.
 */
public class Manager extends PersonInform {

    /**
     * Basic Constructor, creates a Manager.
     *
     * @param fName Manager's firstname.
     * @param lName  Manager's lastname.
     */
    public Manager(String fName, String lName) {
        super(fName, lName);
    }

    /**
     * Sets manager has rate per hour or not.
     *
     * @param pHour true if manager has rate per hour
     */
    public void setPerHour(boolean pHour) {
        perHour = pHour;
    }

    /**
     * Sets manager has bonus or not.
     *
     * @param bnus true if manager has bonus
     */
    public void setBonus(boolean bnus) {
        bonus = bnus;
    }
}
