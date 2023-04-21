package gov.cdc.ncphi.phgrid.amds.stubs;

public interface AMDSPortType extends java.rmi.Remote {

    public gov.cdc.ncphi.phgrid.amds.stubs.MetaDataQueryResponse metaDataQuery(gov.cdc.ncphi.phgrid.amds.stubs.MetaDataQueryRequest parameters) throws java.rmi.RemoteException;

    public gov.cdc.ncphi.phgrid.amds.stubs.AmdsQueryResponse amdsQuery(gov.cdc.ncphi.phgrid.amds.stubs.AmdsQueryRequest parameters) throws java.rmi.RemoteException;

    public gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataResponse getServiceSecurityMetadata(gov.nih.nci.cagrid.introduce.security.stubs.GetServiceSecurityMetadataRequest parameters) throws java.rmi.RemoteException;
}
