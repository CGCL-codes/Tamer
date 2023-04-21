package com.centraview.report.valueobject;

import com.centraview.common.ListElement;
import com.centraview.common.ListElementMember;

/**
 * <p>Title:  ReportListElement </p>
 *
 * <p>Description: This class stores all columns for each row of Report. </p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */
public class ReportListElement extends ListElement {

    private int itsElementID;

    /**
     * ReportListElement
     *
     * @param ID int
     */
    public ReportListElement(int ID) {
        itsElementID = ID;
    }

    /**
     * addMember
     *
     * @param ID int
     * @param lem ListElementMember
     */
    public void addMember(int ID, ListElementMember lem) {
        put(new Integer(ID), lem);
    }

    /**
     * getMember
     *
     * @param memberName String
     * @return ListElementMember
     */
    public ListElementMember getMember(String memberName) {
        return null;
    }

    /**
     * setListAuth
     *
     * @param value char
     */
    public void setListAuth(char value) {
        super.auth = value;
    }

    /**
     * getListAuth
     *
     * @return char
     */
    public char getListAuth() {
        return auth;
    }

    /**
     * getElementID
     *
     * @return int
     */
    public int getElementID() {
        return itsElementID;
    }
}
