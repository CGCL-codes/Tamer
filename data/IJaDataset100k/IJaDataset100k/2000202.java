package com.centraview.report.valueobject;

import java.io.Serializable;

/**
 * <p>Title:  TheItem </p>
 *
 * <p>Description: This is ItemValue Object which represent the
 * table/field data for select boxes on View/New/Edit report screens
 * or selected tables and fields on these screens.</p>
 *
 * @author Kalmychkov Alexi, Serdioukov Eduard
 * @version 1.0
 * @date 01/05/04
 */
public class TheItem implements Serializable {

    private int parentId;

    private int id;

    private String fullName;

    /**
     * TheItem
     *
     * @param id int
     * @param name String
     * @param fullName String
     */
    public TheItem(int id, String fullName) {
        this.id = id;
        if (fullName != null) this.fullName = fullName; else this.fullName = "";
    }

    /**
     * TheItem
     */
    public TheItem() {
        this.fullName = "";
    }

    /**
     * getId
     *
     * @return int
     */
    public int getId() {
        return id;
    }

    /**
     * setId
     *
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * getFullName
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * setFullName
     *
     * @param fullName String
     */
    public void setFullName(String fullName) {
        if (fullName != null) this.fullName = fullName; else this.fullName = "";
    }

    /**
     * @return Returns the parentId.
     */
    public int getParentId() {
        return this.parentId;
    }

    /**
     * @param parentId The parentId to set.
     */
    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
