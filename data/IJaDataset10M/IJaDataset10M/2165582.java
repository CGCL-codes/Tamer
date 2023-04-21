package org.posterita.beans;

public class ProductStatusBean extends UDIBean {

    private Boolean availability = Boolean.valueOf(true);

    private ProductImageInfo imageInfo;

    private Boolean hasAttachment;

    public String getProductClassification() {
        return productClassification;
    }

    public void setProductClassification(String productClassification) {
        this.productClassification = productClassification;
    }

    public Integer[] getCheckBox() {
        return checkBox;
    }

    public void setCheckBox(Integer[] checkBox) {
        this.checkBox = checkBox;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    private String colour, model, transmission, year;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getTransmission() {
        return transmission;
    }

    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    private String productName;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public ProductImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(ProductImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public Boolean getIsWebstoreFeatured() {
        return isWebstoreFeatured;
    }

    public void setIsWebstoreFeatured(Boolean isWebstoreFeatured) {
        this.isWebstoreFeatured = isWebstoreFeatured;
    }

    public String getProductNameComplete() {
        String retVal = "";
        if (productName != null) retVal = productName.replaceAll("_", " ");
        return retVal;
    }

    public String getKeyword1() {
        return keyword1;
    }

    public void setKeyword1(String keyword1) {
        this.keyword1 = keyword1;
    }

    public String getKeyword2() {
        return keyword2;
    }

    public void setKeyword2(String keyword2) {
        this.keyword2 = keyword2;
    }

    public String getKeyword3() {
        return keyword3;
    }

    public void setKeyword3(String keyword3) {
        this.keyword3 = keyword3;
    }

    public String getKeyword4() {
        return keyword4;
    }

    public void setKeyword4(String keyword4) {
        this.keyword4 = keyword4;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public Boolean getIsSelfService() {
        return isSelfService;
    }

    public void setIsSelfService(Boolean isSelfService) {
        this.isSelfService = isSelfService;
    }
}
