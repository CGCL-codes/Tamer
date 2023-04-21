package com.cosmos.acacia.crm.data.product;

import com.cosmos.acacia.crm.data.contacts.BusinessPartner;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;
import java.math.MathContext;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import com.cosmos.acacia.annotation.Property;
import com.cosmos.acacia.annotation.PropertyValidator;
import com.cosmos.acacia.annotation.ValidationType;
import com.cosmos.acacia.crm.data.DbResource;
import com.cosmos.acacia.crm.enums.MeasurementUnit;
import com.cosmos.util.CodeFormatter;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Transient;

/**
 *
 * @author miro
 */
@Entity
@Table(name = "simple_products")
@DiscriminatorValue(value = Product.DISCRIMINATOR_SIMPLE_PRODUCT)
@PrimaryKeyJoinColumn(name = "product_id", referencedColumnName = "product_id")
@NamedQueries({ @NamedQuery(name = "SimpleProduct.findByParentDataObjectAndDeleted", query = "select p from SimpleProduct p where p.dataObject.parentDataObjectId = :parentDataObjectId and p.dataObject.deleted = :deleted"), @NamedQuery(name = "SimpleProduct.findByParentDataObjectIsNullAndDeleted", query = "select p from SimpleProduct p where p.dataObject.parentDataObjectId is null and p.dataObject.deleted = :deleted"), @NamedQuery(name = "SimpleProduct.findByProductName", query = "select p from SimpleProduct p where p.productName like :productName and p.dataObject.deleted = false"), @NamedQuery(name = "SimpleProduct.findByProductCode", query = "select p from SimpleProduct p where p.productCode like :productCode and p.dataObject.deleted = false"), @NamedQuery(name = "SimpleProduct.findByCategories", query = "select p from SimpleProduct p where p.dataObject.deleted = false and p.category.id in (:categoryIds)"), @NamedQuery(name = "SimpleProduct.findNotIncludedProductsInPricelist", query = "select p from SimpleProduct p where p.dataObject.deleted = false and " + "not exists (select i from PricelistItem i where i.dataObject.parentDataObjectId = :pricelistId and i.product=p)") })
public class SimpleProduct extends Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final MathContext MATH_CONTEXT = MathContext.DECIMAL64;

    @JoinColumn(name = "category_id", nullable = false, referencedColumnName = "product_category_id")
    @ManyToOne
    @Property(title = "Category", propertyValidator = @PropertyValidator(required = true))
    private ProductCategory category;

    @Column(name = "is_purchased", nullable = false)
    @Property(title = "Is Purchased")
    private boolean purchased;

    @Column(name = "is_salable", nullable = false)
    @Property(title = "Is Salable")
    private boolean salable = true;

    @Column(name = "is_obsolete", nullable = false)
    @Property(title = "Is Obsolete")
    private boolean obsolete;

    @JoinColumn(name = "pattern_mask_format_id", referencedColumnName = "pattern_mask_format_id")
    @ManyToOne
    @Property(title = "Pattern Mask Format")
    private PatternMaskFormat patternMaskFormat;

    @JoinColumn(name = "product_color_id", nullable = true, referencedColumnName = "resource_id")
    @ManyToOne
    @Property(title = "Product Color")
    private DbResource productColor;

    @Column(name = "minimum_quantity", nullable = false)
    @Property(title = "Min. Quantity", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private BigDecimal minimumQuantity = BigDecimal.ONE;

    @Column(name = "maximum_quantity")
    @Property(title = "Max. Quantity", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private BigDecimal maximumQuantity;

    @Column(name = "default_quantity")
    @Property(title = "Default Quantity", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private BigDecimal defaultQuantity;

    @Column(name = "list_price", nullable = false)
    @Property(title = "List Price", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private BigDecimal listPrice;

    @JoinColumn(name = "transport_percent_id", referencedColumnName = "percent_value_id")
    @ManyToOne
    @Property(title = "Transport %", editable = false, percent = true, visible = false)
    private ProductPercentValue transportPercentValue;

    @JoinColumn(name = "discount_percent_id", referencedColumnName = "percent_value_id")
    @ManyToOne
    @Property(title = "Discount %", editable = false, hidden = true, percent = true, visible = false)
    private ProductPercentValue discountPercentValue;

    @JoinColumn(name = "profit_percent_id", referencedColumnName = "percent_value_id")
    @ManyToOne
    @Property(title = "Profit %", editable = false, hidden = true, percent = true, visible = false)
    private ProductPercentValue profitPercentValue;

    @JoinColumn(name = "customs_duty_percent_id", referencedColumnName = "percent_value_id")
    @ManyToOne
    @Property(title = "Customs duty %", editable = false, hidden = true, percent = true, visible = false)
    private ProductPercentValue customsDutyPercentValue;

    @JoinColumn(name = "excise_duty_percent_id", referencedColumnName = "percent_value_id")
    @ManyToOne
    @Property(title = "Excise duty %", editable = false, hidden = true, percent = true, visible = false)
    private ProductPercentValue exciseDutyPercentValue;

    @Column(name = "transport_value", precision = 19, scale = 4)
    @Property(title = "Transport Value")
    private BigDecimal transportValue;

    @Column(name = "quantity_per_package", nullable = false)
    @Property(title = "Qty per Package", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private int quantityPerPackage = 1;

    @Column(name = "price_per_quantity", nullable = false)
    @Property(title = "Price per Qty", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private int pricePerQuantity = 1;

    @JoinColumn(name = "dimension_unit_id", referencedColumnName = "resource_id")
    @ManyToOne
    @Property(title = "Dimension Unit")
    private DbResource dimensionUnit;

    @Column(name = "dimension_width")
    @Property(title = "Dimension Width", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 99999d))
    private BigDecimal dimensionWidth;

    @Column(name = "dimension_length")
    @Property(title = "Dimension Length", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 99999d))
    private BigDecimal dimensionLength;

    @Column(name = "dimension_height")
    @Property(title = "Dimension Height", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 99999d))
    private BigDecimal dimensionHeight;

    @Transient
    @Property(title = "Cubature", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d))
    private BigDecimal dimensionCubature;

    @JoinColumn(name = "weight_unit_id", referencedColumnName = "resource_id")
    @ManyToOne
    @Property(title = "Weight Unit")
    private DbResource weightUnit;

    @Column(name = "weight")
    @Property(title = "Weight", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 9999999999d))
    private BigDecimal weight;

    @Column(name = "delivery_time")
    @Property(title = "Delivery Time", propertyValidator = @PropertyValidator(validationType = ValidationType.NUMBER_RANGE, minValue = 0d, maxValue = 1000000000000d))
    private Integer deliveryTime;

    @Column(name = "description")
    @Property(title = "Description")
    private String description;

    @JoinColumn(name = "producer_id")
    @ManyToOne
    @Property(title = "Producer")
    private BusinessPartner producer;

    @Transient
    @Property(title = "Total Discount %", editable = false, hidden = true, percent = true, visible = false)
    private BigDecimal totalDiscountPercent;

    @Transient
    @Property(title = "Total Discount Value", editable = false, hidden = true, visible = false)
    private BigDecimal totalDiscountValue;

    @Transient
    @Property(title = "Total Profit %", editable = false, hidden = true, percent = true, visible = false)
    private BigDecimal totalProfitPercent;

    @Transient
    @Property(title = "Total Profit Value", editable = false, hidden = true, visible = false)
    private BigDecimal totalProfitValue;

    @Transient
    @Property(title = "Purchase Price", editable = false, hidden = true, visible = false)
    private BigDecimal purchasePrice;

    @Transient
    @Property(title = "Transport Price", editable = false, hidden = true, visible = false)
    private BigDecimal transportPrice;

    @Transient
    @Property(title = "Base Duty Price", editable = false, hidden = true, visible = false)
    private BigDecimal baseDutyPrice;

    @Transient
    @Property(title = "Customs Duty Percent", editable = false, hidden = true, visible = false, percent = true)
    private BigDecimal customsDutyPercent;

    @Transient
    @Property(title = "Customs Duty Value", editable = false, hidden = true, visible = false)
    private BigDecimal customsDutyValue;

    @Transient
    @Property(title = "Excise Duty Percent", editable = false, hidden = true, visible = false, percent = true)
    private BigDecimal exciseDutyPercent;

    @Transient
    @Property(title = "Excise Duty Value", editable = false, hidden = true, visible = false)
    private BigDecimal exciseDutyValue;

    @Transient
    @Property(title = "Cost Price", editable = false, hidden = true, visible = false)
    private BigDecimal costPrice;

    @Transient
    @Property(title = "Sales Price", editable = false)
    private BigDecimal salesPrice;

    public SimpleProduct() {
        super(DISCRIMINATOR_SIMPLE_PRODUCT);
    }

    public SimpleProduct(UUID productId) {
        super(DISCRIMINATOR_SIMPLE_PRODUCT, productId);
    }

    public ProductCategory getCategory() {
        return category;
    }

    public void setCategory(ProductCategory category) {
        ProductCategory oldValue = this.category;
        this.category = category;
        firePropertyChange("category", oldValue, category);
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        boolean oldValue = this.purchased;
        this.purchased = purchased;
        firePropertyChange("purchased", oldValue, purchased);
    }

    public boolean isSalable() {
        return salable;
    }

    public void setSalable(boolean salable) {
        boolean oldValue = this.salable;
        this.salable = salable;
        firePropertyChange("salable", oldValue, salable);
    }

    public boolean isObsolete() {
        return obsolete;
    }

    public void setObsolete(boolean obsolete) {
        boolean oldValue = this.obsolete;
        this.obsolete = obsolete;
        firePropertyChange("obsolete", oldValue, obsolete);
    }

    public PatternMaskFormat getPatternMaskFormat() {
        return patternMaskFormat;
    }

    public void setPatternMaskFormat(PatternMaskFormat patternMaskFormat) {
        PatternMaskFormat oldValue = this.patternMaskFormat;
        this.patternMaskFormat = patternMaskFormat;
        firePropertyChange("patternMaskFormat", oldValue, patternMaskFormat);
    }

    public DbResource getProductColor() {
        return productColor;
    }

    public void setProductColor(DbResource productColor) {
        DbResource oldValue = this.productColor;
        this.productColor = productColor;
        firePropertyChange("productColor", oldValue, productColor);
    }

    public BigDecimal getMinimumQuantity() {
        return minimumQuantity;
    }

    public void setMinimumQuantity(BigDecimal minimumQuantity) {
        BigDecimal oldValue = this.minimumQuantity;
        this.minimumQuantity = minimumQuantity;
        firePropertyChange("minimumQuantity", oldValue, minimumQuantity);
    }

    public BigDecimal getMaximumQuantity() {
        return maximumQuantity;
    }

    public void setMaximumQuantity(BigDecimal maximumQuantity) {
        BigDecimal oldValue = this.maximumQuantity;
        this.maximumQuantity = maximumQuantity;
        firePropertyChange("maximumQuantity", oldValue, maximumQuantity);
    }

    public BigDecimal getDefaultQuantity() {
        return defaultQuantity;
    }

    public void setDefaultQuantity(BigDecimal defaultQuantity) {
        BigDecimal oldValue = this.defaultQuantity;
        this.defaultQuantity = defaultQuantity;
        firePropertyChange("defaultQuantity", oldValue, defaultQuantity);
    }

    public BigDecimal getListPrice() {
        return listPrice;
    }

    public void setListPrice(BigDecimal listPrice) {
        BigDecimal oldValue = this.listPrice;
        this.listPrice = listPrice;
        firePropertyChange("listPrice", oldValue, listPrice);
    }

    public int getPricePerQuantity() {
        return pricePerQuantity;
    }

    public void setPricePerQuantity(int pricePerQuantity) {
        int oldValue = this.pricePerQuantity;
        this.pricePerQuantity = pricePerQuantity;
        firePropertyChange("pricePerQuantity", oldValue, pricePerQuantity);
    }

    public int getQuantityPerPackage() {
        return quantityPerPackage;
    }

    public void setQuantityPerPackage(int quantityPerPackage) {
        int oldValue = this.quantityPerPackage;
        this.quantityPerPackage = quantityPerPackage;
        firePropertyChange("quantityPerPackage", oldValue, quantityPerPackage);
    }

    public DbResource getDimensionUnit() {
        return dimensionUnit;
    }

    public void setDimensionUnit(DbResource dimensionUnit) {
        DbResource oldValue = this.dimensionUnit;
        this.dimensionUnit = dimensionUnit;
        firePropertyChange("dimensionUnit", oldValue, dimensionUnit);
    }

    public BigDecimal getDimensionWidth() {
        return dimensionWidth;
    }

    public void setDimensionWidth(BigDecimal dimensionWidth) {
        BigDecimal oldValue = this.dimensionWidth;
        this.dimensionWidth = dimensionWidth;
        firePropertyChange("dimensionWidth", oldValue, dimensionWidth);
    }

    public BigDecimal getDimensionLength() {
        return dimensionLength;
    }

    public void setDimensionLength(BigDecimal dimensionLength) {
        BigDecimal oldValue = this.dimensionLength;
        this.dimensionLength = dimensionLength;
        firePropertyChange("dimensionLength", oldValue, dimensionLength);
    }

    public BigDecimal getDimensionHeight() {
        return dimensionHeight;
    }

    public void setDimensionHeight(BigDecimal dimensionHeight) {
        BigDecimal oldValue = this.dimensionHeight;
        this.dimensionHeight = dimensionHeight;
        firePropertyChange("dimensionHeight", oldValue, dimensionHeight);
    }

    public BigDecimal getDimensionCubature() {
        if (dimensionWidth == null || dimensionLength == null || dimensionHeight == null) {
            return dimensionCubature = null;
        }
        return dimensionCubature = dimensionWidth.multiply(dimensionLength).multiply(dimensionHeight);
    }

    public void setDimensionCubature(BigDecimal cubature) {
    }

    public DbResource getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(DbResource weightUnit) {
        DbResource oldValue = this.weightUnit;
        this.weightUnit = weightUnit;
        firePropertyChange("weightUnit", oldValue, weightUnit);
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        BigDecimal oldValue = this.weight;
        this.weight = weight;
        firePropertyChange("weight", oldValue, weight);
    }

    public Integer getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Integer deliveryTime) {
        Integer oldValue = this.deliveryTime;
        this.deliveryTime = deliveryTime;
        firePropertyChange("deliveryTime", oldValue, deliveryTime);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange("description", oldValue, description);
    }

    /**
     * Synthetic property getter.
     * Format on every call.
     * If not setup {@link PatternMaskFormat} property - returns "";
     * If no product code entered returns "";
     * If format error occurs, prints the stack trace and returns "<FORMAT ERROR>";
     * @return - not null result
     */
    public String getCodeFormatted() {
        PatternMaskFormat f = getPatternMaskFormat();
        if (f == null) {
            if (getProductCode() != null) return getProductCode(); else return "";
        }
        if (f.getFormat() == null) return "";
        if (getProductCode() == null) return "";
        try {
            CodeFormatter formatter = new CodeFormatter(f.getFormat());
            String result = formatter.getDisplayValue(getProductCode());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "<FORMAT ERROR>";
        }
    }

    @PostLoad
    public void postLoad() {
        System.out.println("PostLoad()");
    }

    public static SimpleProduct newTestProduct(String productName, String productCode) {
        SimpleProduct product = new SimpleProduct();
        product.setProductName(productName);
        product.setProductCode(productCode);
        product.setMeasureUnit(MeasurementUnit.Piece.getDbResource());
        return product;
    }

    /**
     * Setter for producer
     * @param producer - BusinessPartner
     */
    public void setProducer(BusinessPartner producer) {
        BusinessPartner oldValue = this.producer;
        this.producer = producer;
        firePropertyChange("producer", oldValue, producer);
    }

    /**
     * Getter for producer
     * @return BusinessPartner
     */
    public BusinessPartner getProducer() {
        return producer;
    }

    @Override
    public String getInfo() {
        return getProductName();
    }

    public String getProductDisplay() {
        String codeFormatted = getCodeFormatted();
        String name = getProductName();
        if (codeFormatted == null) {
            codeFormatted = "";
        } else {
            codeFormatted += " ";
        }
        if (name == null) {
            name = "";
        }
        String categoryName = "";
        if (getCategory() != null) {
            categoryName = getCategory().getCategoryName();
        }
        return codeFormatted + " " + name + ", " + categoryName;
    }

    public ProductPercentValue getCustomsDutyPercentValue() {
        return customsDutyPercentValue;
    }

    public void setCustomsDutyPercentValue(ProductPercentValue customsDutyPercentValue) {
        ProductPercentValue oldValue = this.customsDutyPercentValue;
        this.customsDutyPercentValue = customsDutyPercentValue;
        firePropertyChange("customsDutyPercentValue", oldValue, customsDutyPercentValue);
    }

    public ProductPercentValue getDiscountPercentValue() {
        return discountPercentValue;
    }

    public void setDiscountPercentValue(ProductPercentValue discountPercentValue) {
        ProductPercentValue oldValue = this.discountPercentValue;
        this.discountPercentValue = discountPercentValue;
        firePropertyChange("discountPercentValue", oldValue, discountPercentValue);
    }

    public ProductPercentValue getExciseDutyPercentValue() {
        return exciseDutyPercentValue;
    }

    public void setExciseDutyPercentValue(ProductPercentValue exciseDutyPercentValue) {
        ProductPercentValue oldValue = this.exciseDutyPercentValue;
        this.exciseDutyPercentValue = exciseDutyPercentValue;
        firePropertyChange("exciseDutyPercentValue", oldValue, exciseDutyPercentValue);
    }

    public ProductPercentValue getProfitPercentValue() {
        return profitPercentValue;
    }

    public void setProfitPercentValue(ProductPercentValue profitPercentValue) {
        ProductPercentValue oldValue = this.profitPercentValue;
        this.profitPercentValue = profitPercentValue;
        firePropertyChange("profitPercentValue", oldValue, profitPercentValue);
    }

    public ProductPercentValue getTransportPercentValue() {
        return transportPercentValue;
    }

    public void setTransportPercentValue(ProductPercentValue transportPercentValue) {
        ProductPercentValue oldValue = this.transportPercentValue;
        this.transportPercentValue = transportPercentValue;
        firePropertyChange("transportPercentValue", oldValue, transportPercentValue);
    }

    public BigDecimal getTransportValue() {
        return transportValue;
    }

    public void setTransportValue(BigDecimal transportValue) {
        BigDecimal oldValue = this.transportValue;
        this.transportValue = transportValue;
        firePropertyChange("transportValue", oldValue, transportValue);
    }

    public BigDecimal getTotalDiscountPercent() {
        BigDecimal categoryDiscount;
        if (category != null) categoryDiscount = category.getDiscountPercent(); else categoryDiscount = null;
        BigDecimal productDiscount = null;
        if (discountPercentValue != null) productDiscount = discountPercentValue.getPercentValue(); else productDiscount = null;
        if (categoryDiscount != null && productDiscount != null) {
            BigDecimal sumDiscount = categoryDiscount.add(productDiscount, MATH_CONTEXT);
            BigDecimal multipliedDiscount = categoryDiscount.multiply(productDiscount, MATH_CONTEXT);
            return sumDiscount.subtract(multipliedDiscount, MATH_CONTEXT);
        }
        if (categoryDiscount != null) return categoryDiscount;
        if (productDiscount != null) return productDiscount;
        return null;
    }

    public void setTotalDiscountPercent(BigDecimal totalDiscount) {
    }

    public BigDecimal getTotalDiscountValue() {
        BigDecimal discount;
        if (listPrice == null || (discount = getTotalDiscountPercent()) == null) return null;
        return listPrice.multiply(discount, MATH_CONTEXT);
    }

    public void setTotalDiscountValue(BigDecimal totalDiscountValue) {
    }

    public BigDecimal getPurchasePrice() {
        if (listPrice == null) return null;
        BigDecimal discountValue;
        if ((discountValue = getTotalDiscountValue()) == null) return listPrice;
        return listPrice.subtract(discountValue, MATH_CONTEXT);
    }

    public void setPurchasePrice(BigDecimal purchasePrice) {
    }

    public BigDecimal getTransportPrice() {
        BigDecimal price = getPurchasePrice();
        BigDecimal transportPercent;
        if (transportPercentValue != null) transportPercent = transportPercentValue.getPercentValue(); else transportPercent = null;
        BigDecimal value;
        if ((value = getTransportPrice(transportValue, transportPercent, price)) != null) return value;
        if (category == null) return null;
        return getTransportPrice(category.getTransportValue(), category.getTransportPercent(), price);
    }

    private BigDecimal getTransportPrice(BigDecimal value, BigDecimal percent, BigDecimal price) {
        if (value != null) return value;
        if (percent == null || price == null) return null;
        return price.multiply(percent, MATH_CONTEXT);
    }

    public void setTransportPrice(BigDecimal transportPrice) {
    }

    public BigDecimal getBaseDutyPrice() {
        BigDecimal price;
        if ((price = getPurchasePrice()) == null) return null;
        BigDecimal transport;
        if ((transport = getTransportPrice()) == null) return price;
        return price.add(transport, MATH_CONTEXT);
    }

    public void setBaseDutyPrice(BigDecimal baseDutyPrice) {
    }

    public BigDecimal getCustomsDutyPercent() {
        if (customsDutyPercentValue != null) return customsDutyPercentValue.getPercentValue();
        BigDecimal percent;
        if (category != null && (percent = category.getCustomsDutyPercent()) != null) return percent;
        return null;
    }

    public void setCustomsDutyPercent(BigDecimal customsDutyPercent) {
    }

    public BigDecimal getCustomsDutyValue() {
        BigDecimal price;
        BigDecimal percent;
        if ((price = getBaseDutyPrice()) == null || (percent = getCustomsDutyPercent()) == null) return null;
        return price.multiply(percent, MATH_CONTEXT);
    }

    public void setCustomsDutyValue(BigDecimal customsDutyValue) {
    }

    public BigDecimal getExciseDutyPercent() {
        if (exciseDutyPercentValue != null) return exciseDutyPercentValue.getPercentValue();
        BigDecimal percent;
        if (category != null && (percent = category.getExciseDutyPercent()) != null) return percent;
        return null;
    }

    public void setExciseDutyPercent(BigDecimal exciseDutyPercent) {
    }

    public BigDecimal getExciseDutyValue() {
        BigDecimal price;
        BigDecimal percent;
        if ((price = getBaseDutyPrice()) == null || (percent = getExciseDutyPercent()) == null) return null;
        return price.multiply(percent, MATH_CONTEXT);
    }

    public void setExciseDutyValue(BigDecimal exciseDutyValue) {
    }

    public BigDecimal getCostPrice() {
        BigDecimal price;
        if ((price = getBaseDutyPrice()) == null) return null;
        BigDecimal duty;
        if ((duty = getCustomsDutyValue()) != null) price = price.add(duty, MATH_CONTEXT);
        if ((duty = getExciseDutyValue()) != null) price = price.add(duty, MATH_CONTEXT);
        return price;
    }

    public void setCostPrice(BigDecimal costPrice) {
    }

    public BigDecimal getTotalProfitPercent() {
        BigDecimal percent;
        if (profitPercentValue != null) percent = profitPercentValue.getPercentValue(); else percent = null;
        BigDecimal value;
        if (category != null && (value = category.getProfitPercent()) != null) {
            if (percent == null) return value;
            return percent.add(value, MATH_CONTEXT);
        }
        return percent;
    }

    public void setTotalProfitPercent(BigDecimal totalProfitPercent) {
    }

    public BigDecimal getTotalProfitValue() {
        BigDecimal price;
        if ((price = getCostPrice()) == null) return null;
        BigDecimal profit;
        if ((profit = getTotalProfitPercent()) == null) return null;
        return price.multiply(profit, MATH_CONTEXT).divide(BigDecimal.ONE.subtract(profit, MATH_CONTEXT), MATH_CONTEXT);
    }

    public void setTotalProfitValue(BigDecimal totalProfitValue) {
    }

    @Override
    public BigDecimal getSalesPrice() {
        BigDecimal price;
        if ((price = getCostPrice()) == null) return null;
        BigDecimal profit;
        if ((profit = getTotalProfitPercent()) == null) return price;
        return price.divide(BigDecimal.ONE.subtract(profit, MATH_CONTEXT), MATH_CONTEXT);
    }

    @Override
    public void setSalesPrice(BigDecimal salePrice) {
    }
}
