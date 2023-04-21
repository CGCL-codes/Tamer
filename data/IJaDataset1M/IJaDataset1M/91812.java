package org.fao.waicent.kids.giews.dao.bean;

public class Data_3Dimensions {

    private Integer datasetId;

    private String dimension1;

    private String dimension2;

    private String dimension3;

    private String dataValue;

    private String dataSymbol;

    private String Measurement_Unit_Label;

    private String Precision_Value;

    public Integer getDatasetId() {
        return datasetId;
    }

    public void setDatasetId(Integer datasetId) {
        this.datasetId = datasetId;
    }

    public String getDimension1() {
        return dimension1;
    }

    public void setDimension1(String dimension1) {
        this.dimension1 = dimension1;
    }

    public String getDimension2() {
        return dimension2;
    }

    public void setDimension2(String dimension2) {
        this.dimension2 = dimension2;
    }

    public String getDimension3() {
        return dimension3;
    }

    public void setDimension3(String dimension3) {
        this.dimension3 = dimension3;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getDataSymbol() {
        return dataSymbol;
    }

    public void setDataSymbol(String dataSymbol) {
        this.dataSymbol = dataSymbol;
    }

    public String getMeasurement_Unit_Label() {
        return this.Measurement_Unit_Label;
    }

    public void setMeasurement_Unit_Label(String Measurement_Unit_Label) {
        this.Measurement_Unit_Label = Measurement_Unit_Label;
    }

    public String getPrecisionValue() {
        return this.Precision_Value;
    }

    public void setPrecisionValue(String Precision_Value) {
        this.Precision_Value = Precision_Value;
    }
}
