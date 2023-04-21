package com.sbsit.pricepro.model;

public class POSOrderListItem implements PriceRowData {

    private Long id;

    private PointOfSale pointOfSale;

    private Double quantity;

    private Double quantity2;

    private String description;

    private String orderHystory;

    private String product;

    private String producer;

    private String code;

    private Boolean vat;

    private Boolean pricesLimitation;

    private Double price;

    private String termOfUse;

    private String packing;

    private Double producerPrice;

    private Supplier supplier;

    public POSOrderListItem(PointOfSale pos, PriceRowData priceRow, Double q1, Double q2) {
        this.setPointOfSale(pos);
        this.setCode(priceRow.getCode());
        this.setPacking(priceRow.getPacking());
        this.setPrice(priceRow.getPrice());
        this.setPricesLimitation(priceRow.getPricesLimitation());
        this.setProducer(priceRow.getProducer());
        this.setProducerPrice(priceRow.getProducerPrice());
        this.setProduct(priceRow.getProduct());
        this.setSupplier(priceRow.getSupplier());
        this.setTermOfUse(priceRow.getTermOfUse());
        this.setVAT(priceRow.getVAT());
        this.setQuantity(q1);
        this.setQuantity2(q2);
    }

    public POSOrderListItem() {
    }

    public Double getSum() {
        return getQuantity() * getPrice();
    }

    public Double getSum2() {
        return getQuantity2() * getPrice();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setPointOfSale(PointOfSale pointOfSale) {
        this.pointOfSale = pointOfSale;
    }

    public PointOfSale getPointOfSale() {
        return pointOfSale;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity2(Double quantity2) {
        this.quantity2 = quantity2;
    }

    public Double getQuantity2() {
        return quantity2;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setOrderHystory(String orderHystory) {
        this.orderHystory = orderHystory;
    }

    public String getOrderHystory() {
        return orderHystory;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProduct() {
        return product;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducer() {
        return producer;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setVAT(Boolean vat) {
        this.vat = vat;
    }

    public Boolean getVAT() {
        return vat;
    }

    public void setPricesLimitation(Boolean pricesLimitation) {
        this.pricesLimitation = pricesLimitation;
    }

    public Boolean getPricesLimitation() {
        return pricesLimitation;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getPrice() {
        return price;
    }

    public void setTermOfUse(String termOfUse) {
        this.termOfUse = termOfUse;
    }

    public String getTermOfUse() {
        return termOfUse;
    }

    public void setPacking(String packing) {
        this.packing = packing;
    }

    public String getPacking() {
        return packing;
    }

    public void setProducerPrice(Double producerPrice) {
        this.producerPrice = producerPrice;
    }

    public Double getProducerPrice() {
        return producerPrice;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Supplier getSupplier() {
        return supplier;
    }
}
