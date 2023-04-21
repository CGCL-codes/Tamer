package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class SpatialSeriesQueryRequestTypeImpl implements gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialSeriesQueryRequestType {

    private gov.cdc.ncphi.phgrid.services.amds.common.datatypes.DateType _date;

    private gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeSetType _syndromeSet;

    private gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType _spatialGroupingSet;

    private java.lang.String _spatialAggregation;

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.DateType getDate() {
        return _date;
    }

    public void setDate(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.DateType pDate) {
        _date = pDate;
    }

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeSetType getSyndromeSet() {
        return _syndromeSet;
    }

    public void setSyndromeSet(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SyndromeSetType pSyndromeSet) {
        _syndromeSet = pSyndromeSet;
    }

    public gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType getSpatialGroupingSet() {
        return _spatialGroupingSet;
    }

    public void setSpatialGroupingSet(gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingSetType pSpatialGroupingSet) {
        _spatialGroupingSet = pSpatialGroupingSet;
    }

    public java.lang.String getSpatialAggregation() {
        return _spatialAggregation;
    }

    public void setSpatialAggregation(java.lang.String pSpatialAggregation) {
        _spatialAggregation = pSpatialAggregation;
    }
}
