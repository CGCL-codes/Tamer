package com.javaeye.plan.dto;

import java.util.ArrayList;
import java.util.List;
import com.javaeye.common.dto.DTO;

public class MonthPlanDetail implements DTO {

    /**
	 * 
	 */
    private static final long serialVersionUID = -4641886075237991919L;

    private int id;

    private String monthNo;

    private int productId;

    private String productName;

    private String productRootCategory;

    private String productCategory;

    private String productType;

    private String productNo;

    private String customNo;

    private String unit;

    private int number;

    private Integer acceptNumber;

    private String batchNo;

    private Integer status;

    private String productSku;

    private List<PlanDetailOrderInfo> orderInfos = new ArrayList<PlanDetailOrderInfo>();

    public void addOrderInfo(PlanDetailOrderInfo orderInfo) {
        orderInfos.add(orderInfo);
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

    public String getProductRootCategory() {
        return productRootCategory;
    }

    public void setProductRootCategory(String productRootCategory) {
        this.productRootCategory = productRootCategory;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductNo() {
        return productNo;
    }

    public void setProductNo(String productNo) {
        this.productNo = productNo;
    }

    public String getCustomNo() {
        return customNo;
    }

    public void setCustomNo(String customNo) {
        this.customNo = customNo;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getAcceptNumber() {
        return acceptNumber;
    }

    public void setAcceptNumber(Integer acceptNumber) {
        this.acceptNumber = acceptNumber;
    }

    public List<PlanDetailOrderInfo> getOrderInfos() {
        return orderInfos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMonthNo() {
        return monthNo;
    }

    public void setMonthNo(String monthNo) {
        this.monthNo = monthNo;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setOrderInfos(List<PlanDetailOrderInfo> orderInfos) {
        this.orderInfos = orderInfos;
    }

    public void setProductSku(String productSku) {
        this.productSku = productSku;
    }

    public String getProductSku() {
        return productSku;
    }
}
