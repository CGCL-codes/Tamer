package org.opennms.netmgt.poller.mock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opennms.netmgt.poller.ServiceMonitor;

/**
 * @author brozow
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public abstract class MockContainer extends MockElement {

    private Map m_members = new HashMap();

    protected MockContainer(MockContainer parent) {
        super(parent);
    }

    public void addAnticipator(final PollAnticipator trigger) {
        MockVisitor triggerAdder = new MockVisitorAdapter() {

            public void visitService(MockService service) {
                service.addAnticipator(trigger);
            }
        };
        visit(triggerAdder);
    }

    /**
     * @param element
     * @return
     */
    protected MockElement addMember(MockElement element) {
        m_members.put(element.getKey(), element);
        element.setParent(this);
        return element;
    }

    /**
     * @param key
     * @return
     */
    protected MockElement getMember(Object key) {
        return (MockElement) m_members.get(key);
    }

    /**
     * @return
     */
    protected List getMembers() {
        return new ArrayList(m_members.values());
    }

    public int getPollCount() {
        class PollCounter extends MockVisitorAdapter {

            int pollCount = 0;

            int getPollCount() {
                return pollCount;
            }

            public void visitService(MockService service) {
                pollCount += service.getPollCount();
            }
        }
        ;
        PollCounter pollCounter = new PollCounter();
        visit(pollCounter);
        return pollCounter.getPollCount();
    }

    public int getPollStatus() {
        Iterator it = m_members.values().iterator();
        while (it.hasNext()) {
            MockElement element = (MockElement) it.next();
            if (element.getPollStatus() == ServiceMonitor.SERVICE_AVAILABLE) return ServiceMonitor.SERVICE_AVAILABLE;
        }
        return ServiceMonitor.SERVICE_UNAVAILABLE;
    }

    public void removeAnticipator(final PollAnticipator trigger) {
        MockVisitor triggerRemover = new MockVisitorAdapter() {

            public void visitService(MockService service) {
                service.removeAnticipator(trigger);
            }
        };
        visit(triggerRemover);
    }

    protected void removeMember(MockElement element) {
        m_members.remove(element.getKey());
        element.setParent(null);
    }

    public void resetPollCount() {
        class PollCountReset extends MockVisitorAdapter {

            public void visitService(MockService service) {
                service.resetPollCount();
            }
        }
        ;
        PollCountReset pollCounter = new PollCountReset();
        visit(pollCounter);
    }

    public void visit(MockVisitor v) {
        super.visit(v);
        v.visitContainer(this);
    }

    /**
     * @param v
     */
    protected void visitMembers(MockVisitor v) {
        Iterator it = m_members.values().iterator();
        while (it.hasNext()) {
            MockElement element = (MockElement) it.next();
            element.visit(v);
        }
    }
}
