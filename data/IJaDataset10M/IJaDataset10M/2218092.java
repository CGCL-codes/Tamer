package com.pjsofts.eurobudget.beans;

import java.io.Serializable;
import java.util.*;

/**
 * Bean Standard.
 * Main object which contains all information for an EuroBudget sesssion.
 * It is the object which is saved into file through serialization.
 * Should minimize all changes to this class as it may breaks its persistence 
 * (serial uid of the class will change and old files could'nt be load again)
 *
 * Note on xml encoding/decoding:seems to fail on ArrayList;SortedSet and some others !
 * @author  Standard
 */
public class FileData implements Serializable {

    private static final long serialVersionUID = 4623447909160012554L;

    public static final String FILE_STAMP = "EuroBudget";

    /** may be usefull later on (not static to be saved with other fields)*/
    public final int FILE_VERSION = 1;

    /** @serial */
    private Date lastModifyDate;

    /** @serial */
    private String lastModifyUser;

    /** @serial */
    private Date lastAccessDate;

    /** @serial */
    private String lastAccessUser;

    /** @serial */
    private Date creationDate;

    /** @serial */
    private String creationUser;

    /** @serial hint to help user remember his password */
    private String passwordHint;

    /** Doer of this account 
     * @serial
     */
    private Entity owner;

    private Vector accounts;

    /** All entities 
     * @serial 
     */
    private Vector entities;

    /** All banks 
     * @serial 
     */
    private Vector banks;

    /** All categories SortedMap(key:Category) of List//SortedSet of Categories 
     * @serial 
     */
    private SortedMap costCategories;

    /** All categories SortedMap(key:Category) of SortedSet//ArrayList of Categories 
     * @serial 
     */
    private SortedMap revenueCategories;

    /** group of ctg, Items: CategoryGroup , no more Set to allow better serialization in xml
     * @serial 
     */
    private CategoryGroup[] categoryGroups;

    /** @serial */
    private AccountGroup[] accountGroups;

    /** @serial */
    private TypeOfGood[] goodTypes;

    /** List of Goods 
     * @serial 
     */
    private List goods;

    /**
     * List of Impots 
     * Impots (not report, but what has been set and predictions of what we will pay) 
     * Depend of the country also.
     * country decide what kind of class for entry, choices, baremes and results
     * all may have global interface independant of country (display result)
     * 1 or 2 per user and per year, may do more to simulate many situation:
     * Key: user+name+year
     * Sort on date again
     * Need to separate: 
     * entry data (amount), choices of user (frais r�el ou 10%, etc ...), Bareme of the year, Result (mensuel,year,..).
     */
    private transient List impots;

    /** List of PeriodicTxn */
    private transient List periodicTxns;

    /** List of ReportSaved  Saved reports */
    private transient List savedReports;

    /** Creates a new instance of FileData */
    public FileData() {
    }

    /** Getter for property owner.
     * @return Value of property owner.
     */
    public com.pjsofts.eurobudget.beans.Entity getOwner() {
        return owner;
    }

    /** Setter for property owner.
     * @param owner New value of property owner.
     */
    public void setOwner(com.pjsofts.eurobudget.beans.Entity owner) {
        this.owner = owner;
    }

    /** Getter for property accounts.
     * @return Value of property accounts.
     */
    public Vector getAccounts() {
        return this.accounts;
    }

    /** Setter for property accounts.
     * @param accounts New value of property accounts.
     */
    public void setAccounts(Vector accounts) {
        this.accounts = accounts;
    }

    /** Getter for property entities.
     * @return Value of property entities.
     */
    public Vector getEntities() {
        return this.entities;
    }

    /** Setter for property entities.
     * @param entities New value of property entities.
     */
    public void setEntities(Vector entities) {
        this.entities = entities;
    }

    /** Getter for property banks.
     * @return Value of property banks.
     */
    public Vector getBanks() {
        return this.banks;
    }

    /** Setter for property banks.
     * @param banks New value of property banks.
     */
    public void setBanks(Vector banks) {
        this.banks = banks;
    }

    /** Getter for property costCategories.
     * @return Value of property costCategories.
     */
    public SortedMap getCostCategories() {
        return costCategories;
    }

    /** Setter for property costCategories.
     * @param costCategories New value of property costCategories.
     */
    public void setCostCategories(SortedMap costCategories) {
        this.costCategories = costCategories;
    }

    /** Getter for property revenueCategories.
     * @return Value of property revenueCategories.
     */
    public SortedMap getRevenueCategories() {
        return revenueCategories;
    }

    /** Setter for property revenueCategories.
     * @param revenueCategories New value of property revenueCategories.
     */
    public void setRevenueCategories(SortedMap revenueCategories) {
        this.revenueCategories = revenueCategories;
    }

    /** Getter for property categoryGroups.
     * @return Value of property categoryGroups.
     */
    public com.pjsofts.eurobudget.beans.CategoryGroup[] getCategoryGroups() {
        return categoryGroups;
    }

    /** Setter for property categoryGroups.
     * @param categoryGroups New value of property categoryGroups.
     */
    public void setCategoryGroups(com.pjsofts.eurobudget.beans.CategoryGroup[] categoryGroups) {
        this.categoryGroups = categoryGroups;
    }

    /** Getter for property goodTypes.
     * @return Value of property goodTypes.
     */
    public com.pjsofts.eurobudget.beans.TypeOfGood[] getGoodTypes() {
        return this.goodTypes;
    }

    /** Setter for property goodTypes.
     * @param goodTypes New value of property goodTypes.
     */
    public void setGoodTypes(com.pjsofts.eurobudget.beans.TypeOfGood[] goodTypes) {
        this.goodTypes = goodTypes;
    }

    /** Getter for property goods.
     * @return Value of property goods.
     */
    public List getGoods() {
        return goods;
    }

    /** Setter for property goods.
     * @param goods New value of property goods.
     */
    public void setGoods(List goods) {
        this.goods = goods;
    }

    /** Getter for property lastAccessDate.
     * @return Value of property lastAccessDate.
     */
    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    /** Setter for property lastAccessDate.
     * @param lastAccessDate New value of property lastAccessDate.
     */
    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    /** Getter for property lastAccessUser.
     * @return Value of property lastAccessUser.
     */
    public java.lang.String getLastAccessUser() {
        return lastAccessUser;
    }

    /** Setter for property lastAccessUser.
     * @param lastAccessUser New value of property lastAccessUser.
     */
    public void setLastAccessUser(java.lang.String lastAccessUser) {
        this.lastAccessUser = lastAccessUser;
    }

    /** Getter for property lastModifyDate.
     * @return Value of property lastModifyDate.
     */
    public Date getLastModifyDate() {
        return lastModifyDate;
    }

    /** Setter for property lastModifyDate.
     * @param lastModifyDate New value of property lastModifyDate.
     */
    public void setLastModifyDate(Date lastModifyDate) {
        this.lastModifyDate = lastModifyDate;
    }

    /** Getter for property lastModifyUser.
     * @return Value of property lastModifyUser.
     */
    public java.lang.String getLastModifyUser() {
        return lastModifyUser;
    }

    /** Setter for property lastModifyUser.
     * @param lastModifyUser New value of property lastModifyUser.
     */
    public void setLastModifyUser(java.lang.String lastModifyUser) {
        this.lastModifyUser = lastModifyUser;
    }

    /** Getter for property creationDate.
     * @return Value of property creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /** Setter for property creationDate.
     * @param creationDate New value of property creationDate.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /** Getter for property creationUser.
     * @return Value of property creationUser.
     */
    public java.lang.String getCreationUser() {
        return creationUser;
    }

    /** Setter for property creationUser.
     * @param creationUser New value of property creationUser.
     */
    public void setCreationUser(java.lang.String creationUser) {
        this.creationUser = creationUser;
    }

    /** Getter for property periodicTxns.
     * @return Value of property periodicTxns.
     */
    public List getPeriodicTxns() {
        return periodicTxns;
    }

    /** Setter for property periodicTxns.
     * @param periodicTxns New value of property periodicTxns.
     */
    public void setPeriodicTxns(List periodicTxns) {
        this.periodicTxns = periodicTxns;
    }

    /** Getter for property impots.
     * @return Value of property impots.
     */
    public List getImpots() {
        return impots;
    }

    /** Setter for property impots.
     * @param impots New value of property impots.
     */
    public void setImpots(List impots) {
        this.impots = impots;
    }

    /** Getter for property savedReports.
     * @return Value of property savedReports.
     */
    public List getSavedReports() {
        return savedReports;
    }

    /** Setter for property savedReports.
     * @param savedReports New value of property savedReports.
     */
    public void setSavedReports(List savedReports) {
        this.savedReports = savedReports;
    }

    /** Getter for property passwordHint.
     * @return Value of property passwordHint.
     *
     */
    public java.lang.String getPasswordHint() {
        return passwordHint;
    }

    /** Setter for property passwordHint.
     * @param passwordHint New value of property passwordHint.
     *
     */
    public void setPasswordHint(java.lang.String passwordHint) {
        this.passwordHint = passwordHint;
    }
}
