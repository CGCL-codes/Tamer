package com.datagenic.fourthdimension;

public class FourthDimensionString extends FourthDimensionObject {

    /**
   @param value
   @roseuid 4226F0FD030D
    */
    public FourthDimensionString(String value) {
        super(value);
    }

    /**
   @return com.datagenic.fourthdimension.DataObject
   @roseuid 423F0D8B01E5
    */
    public DataObject getDataObject() {
        return this;
    }

    /**
   @param minIndex
   @param maxIndex
   @return com.datagenic.fourthdimension.DataObject
   @roseuid 423F116E0281
    */
    public DataObject getDataObject(long minIndex, long maxIndex) {
        return getDataObject();
    }

    /**
   Access method for the value property.
   @return   the current value of the value property
   @roseuid 426E4ACA0177
    */
    public String getStringValue() {
        return getValue().toString();
    }
}
