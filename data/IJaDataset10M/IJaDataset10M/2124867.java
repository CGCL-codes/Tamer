package sf2.service.mem;

import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import sf2.core.Event;
import sf2.service.AbstractService;
import sf2.service.ServiceDescription;
import sf2.service.ServiceState;
import sf2.view.event.ViewChangeDoneEvent;
import sf2.view.event.ViewChangeStartEvent;

public class OnMemoryService extends AbstractService {

    protected ServiceState createState() {
        return null;
    }

    public Set<InetAddress> getMembers() {
        return null;
    }

    protected boolean handleMemberChange(Set<InetAddress> newMembers, InetAddress preferredPrimary, BlockingQueue<Event> compQ) {
        return false;
    }

    protected void handleMembershipChangeDone(ViewChangeDoneEvent done) {
    }

    protected void handleMembershipChangeStart(ViewChangeStartEvent start) {
    }

    protected void handleServiceInstallDone(Set<InetAddress> members) {
    }

    protected void handleServiceInstallFailed(Set<InetAddress> candidates, Set<InetAddress> failed) {
    }

    protected void handleViewChangeDone(ViewChangeDoneEvent done) {
    }

    protected void handleViewChangeStart(ViewChangeStartEvent start) {
    }

    public void freeze() {
    }

    public boolean isPrimary() {
        return false;
    }

    public ServiceDescription describe() {
        ServiceDescription desc = new ServiceDescription(getServiceName(), getServiceKey());
        return desc;
    }
}
