package gov.cdc.ncphi.phgrid.services.amds.common.datatypes;

public interface FullRecordType {

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimePeriodType getTimePeriod();

    public void setTimePeriod(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.TimePeriodType pTimePeriod);

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialAreaCompositeType getSpatialArea();

    public void setSpatialArea(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialAreaCompositeType pSpatialArea);

    public java.lang.String getSyndrome();

    public void setSyndrome(java.lang.String pSyndrome);

    public java.lang.String getClassifier();

    public void setClassifier(java.lang.String pClassifier);

    public java.math.BigInteger getCount();

    public void setCount(java.math.BigInteger pCount);

    public java.math.BigInteger getDenominator();

    public void setDenominator(java.math.BigInteger pDenominator);
}
