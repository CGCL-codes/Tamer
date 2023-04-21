package gov.cdc.ncphi.phgrid.services.amds.common.datatypes.impl;

public class SpatialGroupingResultTypeImpl implements gov.cdc.ncphi.phgrid.services.amds.common.datatypes.SpatialGroupingResultType {

    private java.lang.String _name;

    private java.util.List _timeSpatialRecord = new java.util.ArrayList();

    public java.lang.String getName() {
        return _name;
    }

    public void setName(java.lang.String pName) {
        _name = pName;
    }

    public java.util.List getTimeSpatialRecord() {
        return _timeSpatialRecord;
    }
}
