package com.javaeye.order.dto;

import com.javaeye.common.dto.DTO;

public class PurchaseOrderDetail implements DTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = 8785936138754021678L;

    private int id;

    private int productId;

    private String productName;

    private String productRootCategory;

    private String productCategory;

    private String productType;

    private String productNo;

    private String customNo;

    private int number;

    private String unit;

    private double price;

    private Double otherAmount;

    private double amount;

    private int planNumber;

    private int inStoreNumber;

    private int deliveryNumber;

    private PurchaseOrder order;

    private String productSku;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public PurchaseOrder getOrder() {
        return order;
    }

    public void setOrder(PurchaseOrder order) {
        this.order = order;
    }

    public void setPlanNumber(int planNumber) {
        this.planNumber = planNumber;
    }

    public int getPlanNumber() {
        return planNumber;
    }

    public void setInStoreNumber(int inStoreNumber) {
        this.inStoreNumber = inStoreNumber;
    }

    public int getInStoreNumber() {
        return inStoreNumber;
    }

    public void setDeliveryNumber(int deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
    }

    public int getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductType() {
        return productType;
    }

    public void setCustomNo(String customNo) {
        this.customNo = customNo;
    }

    public String getCustomNo() {
        return customNo;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }

    /**
	 * @param productCategory the productCategory to set
	 */
    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    /**
	 * @return the productCategory
	 */
    public String getProductCategory() {
        return productCategory;
    }

    /**
	 * @param productNo the productNo to set
	 */
    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    /**
	 * @return the productNo
	 */
    public String getProductNo() {
        return productNo;
    }

    /**
	 * @param otherAmount the otherAmount to set
	 */
    public void setOtherAmount(Double otherAmount) {
        this.otherAmount = otherAmount;
    }

    /**
	 * @return the otherAmount
	 */
    public Double getOtherAmount() {
        return otherAmount;
    }

    public void setProductRootCategory(String productRootCategory) {
        this.productRootCategory = productRootCategory;
    }

    public String getProductRootCategory() {
        return productRootCategory;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductSku() {
        return productSku;
    }
}
