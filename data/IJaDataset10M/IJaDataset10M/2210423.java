package be.potame.cavadium;

import java.io.Serializable;
import java.math.BigDecimal;

public class FactorMeasureMapper implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long factorMeasureId;

    private BigDecimal sizeX;

    private BigDecimal sizeY;

    private BigDecimal oaX;

    private BigDecimal oaY;

    private BigDecimal ssd;

    private BigDecimal depth;

    private BigDecimal value;

    private String factorCode;

    private String beamName;

    private String machineCode;

    private String modifierCode;

    public Long getFactorMeasureId() {
        return factorMeasureId;
    }

    public void setFactorMeasureId(Long factorMeasureId) {
        this.factorMeasureId = factorMeasureId;
    }

    public BigDecimal getSizeX() {
        return sizeX;
    }

    public void setSizeX(BigDecimal sizeX) {
        this.sizeX = sizeX;
    }

    public BigDecimal getSizeY() {
        return sizeY;
    }

    public void setSizeY(BigDecimal sizeY) {
        this.sizeY = sizeY;
    }

    public BigDecimal getOaX() {
        return oaX;
    }

    public void setOaX(BigDecimal oaX) {
        this.oaX = oaX;
    }

    public BigDecimal getOaY() {
        return oaY;
    }

    public void setOaY(BigDecimal oaY) {
        this.oaY = oaY;
    }

    public BigDecimal getSsd() {
        return ssd;
    }

    public void setSsd(BigDecimal ssd) {
        this.ssd = ssd;
    }

    public BigDecimal getDepth() {
        return depth;
    }

    public void setDepth(BigDecimal depth) {
        this.depth = depth;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getFactorCode() {
        return factorCode;
    }

    public void setFactorCode(String factorCode) {
        this.factorCode = factorCode;
    }

    public String getBeamName() {
        return beamName;
    }

    public void setBeamName(String beamName) {
        this.beamName = beamName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getModifierCode() {
        return modifierCode;
    }

    public void setModifierCode(String modifierCode) {
        this.modifierCode = modifierCode;
    }
}
