package net.sf.jgcs.jgroups;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.sf.jgcs.NotJoinedException;
import net.sf.jgcs.membership.Membership;
import net.sf.jgcs.membership.MembershipID;
import org.jgroups.JChannel;
import org.jgroups.View;
import org.jgroups.stack.IpAddress;

public class JGroupsMembership implements Membership {

    private List<SocketAddress> addresses;

    private List<SocketAddress> failed, joined, leaved, failedOnNextView;

    private SocketAddress myAddr;

    private JGroupsMembershipID membershipID;

    public JGroupsMembership(View view, JChannel ch) {
        addresses = new ArrayList<SocketAddress>();
        Iterator it = view.getMembers().iterator();
        while (it.hasNext()) {
            IpAddress jgroupsAddr = (IpAddress) it.next();
            addresses.add(new InetSocketAddress(jgroupsAddr.getIpAddress(), jgroupsAddr.getPort()));
        }
        membershipID = new JGroupsMembershipID(view.getVid().getCoordAddress(), view.getVid().getId());
        IpAddress myjgaddr = (IpAddress) ch.getLocalAddress();
        myAddr = new InetSocketAddress(myjgaddr.getIpAddress(), myjgaddr.getPort());
        failed = new ArrayList<SocketAddress>(addresses.size());
        joined = new ArrayList<SocketAddress>(addresses.size());
        leaved = new ArrayList<SocketAddress>(addresses.size());
        failedOnNextView = new ArrayList<SocketAddress>(addresses.size());
    }

    public MembershipID getMembershipID() {
        return membershipID;
    }

    SocketAddress getMyAddress() {
        return myAddr;
    }

    public String toString() {
        return "JGroups membership: " + addresses;
    }

    void setFailed(List<SocketAddress> failed) {
        this.failed = failed;
    }

    void setJoined(List<SocketAddress> joined) {
        this.joined = joined;
    }

    void setLeaved(List<SocketAddress> leaved) {
        this.leaved = leaved;
    }

    public boolean addToFailed(SocketAddress peer) {
        boolean contain = addresses.contains(peer);
        if (contain) failedOnNextView.add(peer);
        return contain;
    }

    public List<SocketAddress> getFailedToNextView() {
        return failedOnNextView;
    }

    public List<SocketAddress> getMembershipList() {
        return addresses;
    }

    public int getLocalRank() throws NotJoinedException {
        return getMemberRank(myAddr);
    }

    public int getCoordinatorRank() {
        return 0;
    }

    public int getMemberRank(SocketAddress peer) {
        return addresses.indexOf(peer);
    }

    public SocketAddress getMemberAddress(int rank) {
        return addresses.get(rank);
    }

    public List<SocketAddress> getJoinedMembers() {
        return joined;
    }

    public List<SocketAddress> getLeavedMembers() {
        return leaved;
    }

    public List<SocketAddress> getFailedMembers() {
        return failed;
    }

    public boolean containsMember(SocketAddress mbr) {
        return !(mbr == null || addresses == null) && addresses.contains(mbr);
    }

    public int size() {
        return addresses.size();
    }
}
