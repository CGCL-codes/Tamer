package com.ravana.transfer;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Manjuka Soysa
 */
public class Transfer {

    private String id;

    private Integer orderId;

    private String person;

    private String branchId;

    private String branchName;

    private Integer transferType;

    private String locationId;

    private String locationName;

    private Date creationTime;

    private Date statusTime;

    private Boolean salesTax;

    private BigDecimal salesTaxPercent;

    private List<TransferItem> items;

    private BigDecimal totalCost;

    private BigDecimal totalTax;

    private PropertyChangeSupport support;

    private Integer status;

    public Transfer() {
        support = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    public Integer getTransferId() {
        return orderId;
    }

    public void setTransferId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Date getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(Date statusTime) {
        this.statusTime = statusTime;
    }

    public Boolean getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(Boolean salesTax) {
        this.salesTax = salesTax;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<TransferItem> getItems() {
        return items;
    }

    public void setItems(List<TransferItem> items) {
        this.items = items;
    }

    public BigDecimal getSalesTaxPercent() {
        return salesTaxPercent;
    }

    public void setSalesTaxPercent(BigDecimal salesTaxPercent) {
        this.salesTaxPercent = salesTaxPercent;
    }

    public void addItem(TransferItem oi) {
        items.add(oi);
        renumber();
        calculateCost();
        support.firePropertyChange("newitem", oi, null);
    }

    void calculateCost() {
        if (items == null) return;
        BigDecimal oldCost = totalCost;
        totalCost = BigDecimal.ZERO;
        totalTax = BigDecimal.ZERO;
        for (TransferItem oi : items) {
            BigDecimal cost = oi.getSubTotal();
            if (cost != null) totalCost = totalCost.add(cost);
            BigDecimal tax = oi.getSubTotalTax();
            if (tax != null) totalTax = totalTax.add(tax);
        }
        support.firePropertyChange("cost", oldCost, totalCost);
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public BigDecimal getTotalTax() {
        return totalTax;
    }

    public void setTotalTax(BigDecimal totalTax) {
        this.totalTax = totalTax;
    }

    void removeItem(int selected) {
        if (id != null) {
            throw new RuntimeException("Can not remove lines from a saved Transfer");
        }
        items.remove(selected);
        renumber();
        calculateCost();
        support.firePropertyChange("removeitem", null, selected);
    }

    private void renumber() {
        int line = 1;
        for (TransferItem it : items) {
            it.setLineNumber(line++);
        }
    }

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Integer getTransferType() {
        return transferType;
    }

    public void setTransferType(Integer transferType) {
        this.transferType = transferType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }
}
