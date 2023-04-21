package org.opennms.netmgt.importer;

import org.opennms.netmgt.config.modelimport.Asset;
import org.opennms.netmgt.config.modelimport.Category;
import org.opennms.netmgt.config.modelimport.Interface;
import org.opennms.netmgt.config.modelimport.MonitoredService;
import org.opennms.netmgt.config.modelimport.Node;
import org.opennms.netmgt.importer.operations.ImportOperationsManager;
import org.opennms.netmgt.importer.operations.SaveOrUpdateOperation;
import org.opennms.netmgt.importer.specification.AbstractImportVisitor;

public class ImportAccountant extends AbstractImportVisitor {

    private final ImportOperationsManager m_opsMgr;

    private SaveOrUpdateOperation m_currentOp;

    public ImportAccountant(ImportOperationsManager opsMgr) {
        m_opsMgr = opsMgr;
    }

    public void visitNode(Node node) {
        m_currentOp = m_opsMgr.foundNode(node.getForeignId(), node.getNodeLabel(), node.getBuilding(), node.getCity());
    }

    public void completeNode(Node node) {
        m_currentOp = null;
    }

    public void visitInterface(Interface iface) {
        m_currentOp.foundInterface(iface.getIpAddr(), iface.getDescr(), iface.getSnmpPrimary(), iface.getManaged(), iface.getStatus());
    }

    public void visitMonitoredService(MonitoredService svc) {
        m_currentOp.foundMonitoredService(svc.getServiceName());
    }

    public void visitCategory(Category category) {
        m_currentOp.foundCategory(category.getName());
    }

    public void visitAsset(Asset asset) {
        m_currentOp.foundAsset(asset.getName(), asset.getValue());
    }
}
