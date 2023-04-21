package com.sbsit.pricepro.model;

import java.util.Date;

public class PriceListInfo
{
	//fields:
	
	private Date dateOfUpdating;
	private Integer rowCount;
	private Integer colProductName;
	private Integer colProducerName;
	private Integer colTermOfUse;
	private Integer colPrice;
	private Integer colCode;
	private Integer colProducerPrice;
	private Integer colToColumn;
	private Integer colPacking;
	private Integer colVAT;
	private Boolean vatOnly;
	private String vatString;
	private Boolean priceWithVAT;
	private Boolean vatInColumn;
	private Boolean vatInSheet;
	private Integer colVatProduct;
	private Integer colVatProducer;
	private Integer colVatTermOfUse;
	private Integer colVatPrice;
	private Integer colVatCode;
	private Integer colVatProducerPrice;
	private Integer colVatPacking;
	private Double discount;
	private Integer firstRow;
	private Integer lastRow;
	private Integer colPriceLimitationSign;
	private String priceLimitationSignString;
	
	//constructors:
	
	public PriceListInfo(){}
	
	//methods:
	
	public void setDateOfUpdating(Date dateOfUpdating) {
		this.dateOfUpdating = dateOfUpdating;
	}
	public Date getDateOfUpdating() {
		return dateOfUpdating;
	}
	
	public void setRowCount(Integer rowCount) {
		this.rowCount = rowCount;
	}
	public Integer getRowCount() {
		return rowCount;
	}
	
	public void setColProductName(Integer colProductName) {
		this.colProductName = colProductName;
	}
	public Integer getColProductName() {
		return colProductName;
	}
	
	public void setColProducerName(Integer colProducerName) {
		this.colProducerName = colProducerName;
	}
	public Integer getColProducerName() {
		return colProducerName;
	}
	
	public void setColTermOfUse(Integer colTermOfUse) {
		this.colTermOfUse = colTermOfUse;
	}
	public Integer getColTermOfUse() {
		return colTermOfUse;
	}
	
	public void setColPrice(Integer colPrice) {
		this.colPrice = colPrice;
	}
	public Integer getColPrice() {
		return colPrice;
	}
	public void setColCode(Integer colCode) {
		this.colCode = colCode;
	}
	public Integer getColCode() {
		return colCode;
	}
	public void setColProducerPrice(Integer colProducerPrice) {
		this.colProducerPrice = colProducerPrice;
	}
	public Integer getColProducerPrice() {
		return colProducerPrice;
	}
	public void setColToColumn(Integer colToColumn) {
		this.colToColumn = colToColumn;
	}
	public Integer getColToColumn() {
		return colToColumn;
	}
	public void setColPacking(Integer colPacking) {
		this.colPacking = colPacking;
	}
	public Integer getColPacking() {
		return colPacking;
	}
	public void setColVAT(Integer colVAT) {
		this.colVAT = colVAT;
	}
	public Integer getColVAT() {
		return colVAT;
	}
	public void setVatOnly(Boolean vatOnly) {
		this.vatOnly = vatOnly;
	}
	public Boolean getVatOnly() {
		return vatOnly;
	}
	public void setVatString(String vatString) {
		this.vatString = vatString;
	}
	public String getVatString() {
		return vatString;
	}
	public void setPriceWithVAT(Boolean priceWithVAT) {
		this.priceWithVAT = priceWithVAT;
	}
	public Boolean getPriceWithVAT() {
		return priceWithVAT;
	}
	public void setVatInColumn(Boolean vatInColumn) {
		this.vatInColumn = vatInColumn;
	}
	public Boolean getVatInColumn() {
		return vatInColumn;
	}
	public void setVatInSheet(Boolean vatInSheet) {
		this.vatInSheet = vatInSheet;
	}
	public Boolean getVatInSheet() {
		return vatInSheet;
	}
	public void setColVatProduct(Integer colVatProduct) {
		this.colVatProduct = colVatProduct;
	}
	public Integer getColVatProduct() {
		return colVatProduct;
	}
	public void setColVatProducer(Integer colVatProducer) {
		this.colVatProducer = colVatProducer;
	}
	public Integer getColVatProducer() {
		return colVatProducer;
	}
	public void setColVatTermOfUse(Integer colVatTermOfUse) {
		this.colVatTermOfUse = colVatTermOfUse;
	}
	public Integer getColVatTermOfUse() {
		return colVatTermOfUse;
	}
	public void setColVatPrice(Integer colVatPrice) {
		this.colVatPrice = colVatPrice;
	}
	public Integer getColVatPrice() {
		return colVatPrice;
	}
	public void setColVatCode(Integer colVatCode) {
		this.colVatCode = colVatCode;
	}
	public Integer getColVatCode() {
		return colVatCode;
	}
	public void setColVatProducerPrice(Integer colVatProducerPrice) {
		this.colVatProducerPrice = colVatProducerPrice;
	}
	public Integer getColVatProducerPrice() {
		return colVatProducerPrice;
	}
	public void setColVatPacking(Integer colVatPacking) {
		this.colVatPacking = colVatPacking;
	}
	public Integer getColVatPacking() {
		return colVatPacking;
	}
	public void setDiscount(Double discount) {
		this.discount = discount;
	}
	public Double getDiscount() {
		return discount;
	}
	public void setFirstRow(Integer firstRow) {
		this.firstRow = firstRow;
	}
	public Integer getFirstRow() {
		return firstRow;
	}
	public void setLastRow(Integer lastRow) {
		this.lastRow = lastRow;
	}
	public Integer getLastRow() {
		return lastRow;
	}
	public void setColPriceLimitationSign(Integer colPriceLimitationSign) {
		this.colPriceLimitationSign = colPriceLimitationSign;
	}
	public Integer getColPriceLimitationSign() {
		return colPriceLimitationSign;
	}
	public void setPriceLimitationSignString(String priceLimitationSignString) {
		this.priceLimitationSignString = priceLimitationSignString;
	}
	public String getPriceLimitationSignString() {
		return priceLimitationSignString;
	}


}
