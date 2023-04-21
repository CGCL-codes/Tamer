package net.f;

import net.f.me.xml.rpc.EnumType;
import net.f.query.BuyType;
import net.f.query.TimeUnit;
import net.f.util.Utils;

public class Price {

    private static final EnumType U_Enum = TimeUnit.instance();

    private static final EnumType B_Enum = BuyType.instance();

    protected Integer id;

    protected String lockType;

    protected Integer quantity;

    protected Integer timeUnit;

    protected Integer type;

    protected Double unitCost;

    public Price() {
    }

    public Price(Integer id, String lockType, Integer quantity, Integer timeUnit, Integer type, Double unitCost) {
        this.id = id;
        this.lockType = lockType;
        this.quantity = quantity;
        this.timeUnit = timeUnit;
        this.type = type;
        this.unitCost = unitCost;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLockType() {
        return lockType;
    }

    public void setLockType(String lockType) {
        this.lockType = lockType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getTimeUnit() {
        return timeUnit;
    }

    public String getTimeUnit_Adapt() {
        return U_Enum.name(timeUnit);
    }

    public void setTimeUnit_Adapt(String timeUnit) {
        this.timeUnit = U_Enum.ordinal(timeUnit);
    }

    public void setTimeUnit(Integer timeUnit) {
        this.timeUnit = timeUnit;
    }

    public Integer getType() {
        return type;
    }

    public String getType_Adapt() {
        return B_Enum.name(type);
    }

    public void setType_Adapt(String type) {
        this.type = U_Enum.ordinal(type);
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Double getUnitCost() {
        return unitCost;
    }

    public void setUnitCost(Double unitCost) {
        this.unitCost = unitCost;
    }

    public String getUnitCost_Adapt() {
        return Utils.toCostString(unitCost);
    }

    public void setUnitCost_Adapt(String unitCost) {
        this.unitCost = Utils.parseCost(unitCost);
    }
}
