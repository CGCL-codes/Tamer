package org.opennms.netmgt.model.events;

import org.opennms.netmgt.model.AbstractEntityVisitor;
import org.opennms.netmgt.model.OnmsIpInterface;
import org.opennms.netmgt.model.OnmsMonitoredService;
import org.opennms.netmgt.model.OnmsNode;
import org.opennms.netmgt.xml.event.Event;

public class AddEventVisitor extends AbstractEntityVisitor {

    private static final String m_eventSource = "Provisiond";

    private final EventForwarder m_eventForwarder;

    public AddEventVisitor(EventForwarder eventForwarder) {
        m_eventForwarder = eventForwarder;
    }

    public void visitNode(OnmsNode node) {
        System.out.printf("Sending nodeAdded Event for %s\n", node);
        m_eventForwarder.sendNow(createNodeAddedEvent(node));
    }

    public void visitIpInterface(OnmsIpInterface iface) {
        System.out.printf("Sending nodeGainedInterface Event for %s\n", iface);
        m_eventForwarder.sendNow(createNodeGainedInterfaceEvent(iface));
    }

    public void visitMonitoredService(OnmsMonitoredService monSvc) {
        System.out.printf("Sending nodeGainedService Event for %s\n", monSvc);
        m_eventForwarder.sendNow(createNodeGainedServiceEvent(monSvc));
    }

    protected Event createNodeAddedEvent(OnmsNode node) {
        return EventUtils.createNodeAddedEvent(m_eventSource, node.getId(), node.getLabel(), node.getLabelSource());
    }

    protected Event createNodeGainedInterfaceEvent(OnmsIpInterface iface) {
        return EventUtils.createNodeGainedInterfaceEvent(m_eventSource, iface.getNode().getId(), iface.getInetAddress());
    }

    protected Event createNodeGainedServiceEvent(OnmsMonitoredService monSvc) {
        OnmsIpInterface iface = monSvc.getIpInterface();
        OnmsNode node = iface.getNode();
        return EventUtils.createNodeGainedServiceEvent(m_eventSource, monSvc.getNodeId(), iface.getInetAddress(), monSvc.getServiceType().getName(), node.getLabel(), node.getLabelSource(), node.getSysName(), node.getSysDescription());
    }
}
