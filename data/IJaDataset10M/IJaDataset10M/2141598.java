package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class TimeSeriesRecordTypeImpl implements gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimeSeriesRecordType {

    private gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimePeriodType _timePeriod;

    private java.lang.String _classifier;

    private java.math.BigInteger _count;

    private java.math.BigInteger _denominator;

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimePeriodType getTimePeriod() {
        return _timePeriod;
    }

    public void setTimePeriod(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimePeriodType pTimePeriod) {
        _timePeriod = pTimePeriod;
    }

    public java.lang.String getClassifier() {
        return _classifier;
    }

    public void setClassifier(java.lang.String pClassifier) {
        _classifier = pClassifier;
    }

    public java.math.BigInteger getCount() {
        return _count;
    }

    public void setCount(java.math.BigInteger pCount) {
        _count = pCount;
    }

    public java.math.BigInteger getDenominator() {
        return _denominator;
    }

    public void setDenominator(java.math.BigInteger pDenominator) {
        _denominator = pDenominator;
    }
}
