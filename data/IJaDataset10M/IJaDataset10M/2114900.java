package org.nodevision.portal.bp.axis;

public interface NVBPSkeletonService extends javax.xml.rpc.Service {

    public java.lang.String getNVBPServiceAddress();

    public org.nodevision.portal.bp.axis.NVBPSkeleton getNVBPService() throws javax.xml.rpc.ServiceException;

    public org.nodevision.portal.bp.axis.NVBPSkeleton getNVBPService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
