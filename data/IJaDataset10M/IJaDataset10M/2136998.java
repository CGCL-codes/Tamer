package org.opennms.netmgt.provision.service;

import org.opennms.netmgt.provision.persist.AbstractRequisitionVisitor;
import org.opennms.netmgt.provision.persist.OnmsAssetRequisition;
import org.opennms.netmgt.provision.persist.OnmsIpInterfaceRequisition;
import org.opennms.netmgt.provision.persist.OnmsMonitoredServiceRequisition;
import org.opennms.netmgt.provision.persist.OnmsNodeCategoryRequisition;
import org.opennms.netmgt.provision.persist.OnmsNodeRequisition;
import org.opennms.netmgt.provision.service.operations.ImportOperationsManager;
import org.opennms.netmgt.provision.service.operations.SaveOrUpdateOperation;

public class RequisitionAccountant extends AbstractRequisitionVisitor {

    private final ImportOperationsManager m_opsMgr;

    private SaveOrUpdateOperation m_currentOp;

    public RequisitionAccountant(ImportOperationsManager opsMgr) {
        m_opsMgr = opsMgr;
    }

    @Override
    public void visitNode(OnmsNodeRequisition nodeReq) {
        m_currentOp = m_opsMgr.foundNode(nodeReq.getForeignId(), nodeReq.getNodeLabel(), nodeReq.getBuilding(), nodeReq.getCity());
    }

    @Override
    public void completeNode(OnmsNodeRequisition nodeReq) {
        m_currentOp = null;
    }

    @Override
    public void visitInterface(OnmsIpInterfaceRequisition ifaceReq) {
        m_currentOp.foundInterface(ifaceReq.getIpAddr().trim(), ifaceReq.getDescr(), ifaceReq.getSnmpPrimary(), ifaceReq.getManaged(), ifaceReq.getStatus());
    }

    @Override
    public void visitMonitoredService(OnmsMonitoredServiceRequisition svcReq) {
        m_currentOp.foundMonitoredService(svcReq.getServiceName());
    }

    @Override
    public void visitNodeCategory(OnmsNodeCategoryRequisition catReq) {
        m_currentOp.foundCategory(catReq.getName());
    }

    @Override
    public void visitAsset(OnmsAssetRequisition assetReq) {
        m_currentOp.foundAsset(assetReq.getName(), assetReq.getValue());
    }
}
