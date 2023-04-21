package com.centraview.common;

public class ProjectListElement extends ListElement {

    private int itsElementID;

    public ProjectListElement(int ID) {
        itsElementID = ID;
    }

    public void addMember(int ID, ListElementMember lem) {
        put(new Integer(ID), lem);
    }

    public ListElementMember getMember(String memberName) {
        return null;
    }

    public void setListAuth(char value) {
        super.auth = value;
    }

    public char getListAuth() {
        return auth;
    }

    public int getElementID() {
        return itsElementID;
    }
}
