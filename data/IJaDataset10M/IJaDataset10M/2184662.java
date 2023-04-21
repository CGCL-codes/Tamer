package org.libreplan.business.materials.entities;

import java.math.BigDecimal;
import org.hibernate.validator.NotNull;

/**
 * @author Óscar González Fernández <ogonzalez@igalia.com>
 *
 */
public class MaterialInfo {

    private Material material;

    private BigDecimal units = BigDecimal.ZERO;

    private BigDecimal unitPrice = BigDecimal.ZERO;

    @NotNull(message = "material not specified")
    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    @NotNull(message = "units not specified")
    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units != null ? units : BigDecimal.ZERO;
    }

    public void setUnitsWithoutNullCheck(BigDecimal units) {
        this.units = units;
    }

    @NotNull(message = "unit price not specified")
    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice != null ? unitPrice : BigDecimal.ZERO;
    }

    public void setUnitPriceWithoutNullCheck(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public MaterialInfo copy() {
        MaterialInfo result = new MaterialInfo();
        result.setMaterial(getMaterial());
        result.setUnits(getUnits());
        result.setUnitPrice(getUnitPrice());
        return result;
    }

    public BigDecimal getTotalPrice() {
        return getUnits().multiply(getUnitPrice());
    }
}
