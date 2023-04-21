package org.koossery.adempiere.core.contract.criteria.pa;

import java.math.BigDecimal;
import org.koossery.adempiere.core.contract.criteria.KTADempiereBaseCriteria;

public class PA_MeasureCriteria extends KTADempiereBaseCriteria {

    private static final long serialVersionUID = 1L;

    private int c_ProjectType_ID;

    private String calculationClass;

    private String description;

    private BigDecimal manualActual;

    private String manualNote;

    private String measureDataType;

    private String measureType;

    private String name;

    private int pa_Benchmark_ID;

    private int pa_Hierarchy_ID;

    private int pa_Measure_ID;

    private int pa_MeasureCalc_ID;

    private int pa_Ratio_ID;

    private int r_RequestType_ID;

    private String isActive;

    public int getC_ProjectType_ID() {
        return c_ProjectType_ID;
    }

    public void setC_ProjectType_ID(int c_ProjectType_ID) {
        this.c_ProjectType_ID = c_ProjectType_ID;
    }

    public String getCalculationClass() {
        return calculationClass;
    }

    public void setCalculationClass(String calculationClass) {
        this.calculationClass = calculationClass;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getManualActual() {
        return manualActual;
    }

    public void setManualActual(BigDecimal manualActual) {
        this.manualActual = manualActual;
    }

    public String getManualNote() {
        return manualNote;
    }

    public void setManualNote(String manualNote) {
        this.manualNote = manualNote;
    }

    public String getMeasureDataType() {
        return measureDataType;
    }

    public void setMeasureDataType(String measureDataType) {
        this.measureDataType = measureDataType;
    }

    public String getMeasureType() {
        return measureType;
    }

    public void setMeasureType(String measureType) {
        this.measureType = measureType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPa_Benchmark_ID() {
        return pa_Benchmark_ID;
    }

    public void setPa_Benchmark_ID(int pa_Benchmark_ID) {
        this.pa_Benchmark_ID = pa_Benchmark_ID;
    }

    public int getPa_Hierarchy_ID() {
        return pa_Hierarchy_ID;
    }

    public void setPa_Hierarchy_ID(int pa_Hierarchy_ID) {
        this.pa_Hierarchy_ID = pa_Hierarchy_ID;
    }

    public int getPa_Measure_ID() {
        return pa_Measure_ID;
    }

    public void setPa_Measure_ID(int pa_Measure_ID) {
        this.pa_Measure_ID = pa_Measure_ID;
    }

    public int getPa_MeasureCalc_ID() {
        return pa_MeasureCalc_ID;
    }

    public void setPa_MeasureCalc_ID(int pa_MeasureCalc_ID) {
        this.pa_MeasureCalc_ID = pa_MeasureCalc_ID;
    }

    public int getPa_Ratio_ID() {
        return pa_Ratio_ID;
    }

    public void setPa_Ratio_ID(int pa_Ratio_ID) {
        this.pa_Ratio_ID = pa_Ratio_ID;
    }

    public int getR_RequestType_ID() {
        return r_RequestType_ID;
    }

    public void setR_RequestType_ID(int r_RequestType_ID) {
        this.r_RequestType_ID = r_RequestType_ID;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
