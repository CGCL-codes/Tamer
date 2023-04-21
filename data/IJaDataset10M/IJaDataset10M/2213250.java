package com.amazon.webservices.awsecommerceservice.x20090201;

public class Details implements java.io.Serializable {

    private java.lang.String merchantId;

    public java.lang.String getMerchantId() {
        return this.merchantId;
    }

    public void setMerchantId(java.lang.String merchantId) {
        this.merchantId = merchantId;
    }

    private java.lang.String owningMerchantId;

    public java.lang.String getOwningMerchantId() {
        return this.owningMerchantId;
    }

    public void setOwningMerchantId(java.lang.String owningMerchantId) {
        this.owningMerchantId = owningMerchantId;
    }

    private java.lang.String promotionId;

    public java.lang.String getPromotionId() {
        return this.promotionId;
    }

    public void setPromotionId(java.lang.String promotionId) {
        this.promotionId = promotionId;
    }

    private java.lang.String promotionCategory;

    public java.lang.String getPromotionCategory() {
        return this.promotionCategory;
    }

    public void setPromotionCategory(java.lang.String promotionCategory) {
        this.promotionCategory = promotionCategory;
    }

    private java.lang.String merchantPromotionId;

    public java.lang.String getMerchantPromotionId() {
        return this.merchantPromotionId;
    }

    public void setMerchantPromotionId(java.lang.String merchantPromotionId) {
        this.merchantPromotionId = merchantPromotionId;
    }

    private java.lang.String groupClaimCode;

    public java.lang.String getGroupClaimCode() {
        return this.groupClaimCode;
    }

    public void setGroupClaimCode(java.lang.String groupClaimCode) {
        this.groupClaimCode = groupClaimCode;
    }

    private java.lang.String couponCombinationType;

    public java.lang.String getCouponCombinationType() {
        return this.couponCombinationType;
    }

    public void setCouponCombinationType(java.lang.String couponCombinationType) {
        this.couponCombinationType = couponCombinationType;
    }

    private java.lang.String startDate;

    public java.lang.String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(java.lang.String startDate) {
        this.startDate = startDate;
    }

    private java.lang.String endDate;

    public java.lang.String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(java.lang.String endDate) {
        this.endDate = endDate;
    }

    private java.lang.String termsAndConditions;

    public java.lang.String getTermsAndConditions() {
        return this.termsAndConditions;
    }

    public void setTermsAndConditions(java.lang.String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.PromotionEligibilityRequirements eligibilityRequirements;

    public com.amazon.webservices.awsecommerceservice.x20090201.PromotionEligibilityRequirements getEligibilityRequirements() {
        return this.eligibilityRequirements;
    }

    public void setEligibilityRequirements(com.amazon.webservices.awsecommerceservice.x20090201.PromotionEligibilityRequirements eligibilityRequirements) {
        this.eligibilityRequirements = eligibilityRequirements;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.PromotionBenefits benefits;

    public com.amazon.webservices.awsecommerceservice.x20090201.PromotionBenefits getBenefits() {
        return this.benefits;
    }

    public void setBenefits(com.amazon.webservices.awsecommerceservice.x20090201.PromotionBenefits benefits) {
        this.benefits = benefits;
    }

    private com.amazon.webservices.awsecommerceservice.x20090201.PromotionItemApplicability itemApplicability;

    public com.amazon.webservices.awsecommerceservice.x20090201.PromotionItemApplicability getItemApplicability() {
        return this.itemApplicability;
    }

    public void setItemApplicability(com.amazon.webservices.awsecommerceservice.x20090201.PromotionItemApplicability itemApplicability) {
        this.itemApplicability = itemApplicability;
    }

    private java.lang.String merchandisingMessage;

    public java.lang.String getMerchandisingMessage() {
        return this.merchandisingMessage;
    }

    public void setMerchandisingMessage(java.lang.String merchandisingMessage) {
        this.merchandisingMessage = merchandisingMessage;
    }

    private java.lang.String iconMediaId;

    public java.lang.String getIconMediaId() {
        return this.iconMediaId;
    }

    public void setIconMediaId(java.lang.String iconMediaId) {
        this.iconMediaId = iconMediaId;
    }

    private java.lang.Boolean isIconMediaIdCustom;

    public java.lang.Boolean getIsIconMediaIdCustom() {
        return this.isIconMediaIdCustom;
    }

    public void setIsIconMediaIdCustom(java.lang.Boolean isIconMediaIdCustom) {
        this.isIconMediaIdCustom = isIconMediaIdCustom;
    }
}
