package org.yes.cart.domain.dto.impl;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import org.yes.cart.domain.dto.SkuWarehouseDTO;
import java.math.BigDecimal;

/**
* User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
@Dto
public class SkuWarehouseDTOImpl implements SkuWarehouseDTO {

    private static final long serialVersionUID = 20100624L;

    @DtoField(value = "skuWarehouseId")
    private long skuWarehouseId;

    @DtoField(value = "sku", converter = "skuId2Sku", entityBeanKeys = "org.yes.cart.domain.entity.ProductSku")
    private long productSkuId;

    @DtoField(value = "sku.code", readOnly = true)
    private String skuCode;

    @DtoField(value = "sku.name", readOnly = true)
    private String skuName;

    @DtoField(value = "warehouse", converter = "warehouseId2Warehouse", entityBeanKeys = "org.yes.cart.domain.entity.Warehouse")
    private long warehouseId;

    @DtoField(value = "warehouse.code", readOnly = true)
    private String warehouseCode;

    @DtoField(value = "warehouse.name", readOnly = true)
    private String warehouseName;

    @DtoField(value = "quantity")
    private BigDecimal quantity;

    /** {@inheritDoc} */
    public long getSkuWarehouseId() {
        return skuWarehouseId;
    }

    /** {@inheritDoc} */
    public void setSkuWarehouseId(final long skuWarehouseId) {
        this.skuWarehouseId = skuWarehouseId;
    }

    /** {@inheritDoc} */
    public long getProductSkuId() {
        return productSkuId;
    }

    /** {@inheritDoc}*/
    public long getId() {
        return productSkuId;
    }

    /** {@inheritDoc} */
    public void setProductSkuId(final long productSkuId) {
        this.productSkuId = productSkuId;
    }

    /** {@inheritDoc} */
    public String getSkuCode() {
        return skuCode;
    }

    /** {@inheritDoc} */
    public void setSkuCode(final String skuCode) {
        this.skuCode = skuCode;
    }

    /** {@inheritDoc} */
    public String getSkuName() {
        return skuName;
    }

    /** {@inheritDoc} */
    public void setSkuName(final String skuName) {
        this.skuName = skuName;
    }

    /** {@inheritDoc} */
    public long getWarehouseId() {
        return warehouseId;
    }

    /** {@inheritDoc} */
    public void setWarehouseId(final long warehouseId) {
        this.warehouseId = warehouseId;
    }

    /** {@inheritDoc} */
    public String getWarehouseCode() {
        return warehouseCode;
    }

    /** {@inheritDoc} */
    public void setWarehouseCode(final String warehouseCode) {
        this.warehouseCode = warehouseCode;
    }

    /** {@inheritDoc} */
    public String getWarehouseName() {
        return warehouseName;
    }

    /** {@inheritDoc} */
    public void setWarehouseName(final String warehouseName) {
        this.warehouseName = warehouseName;
    }

    /** {@inheritDoc} */
    public BigDecimal getQuantity() {
        return quantity;
    }

    /** {@inheritDoc} */
    public void setQuantity(final BigDecimal quantity) {
        this.quantity = quantity;
    }
}
