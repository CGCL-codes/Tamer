package sf2.view.impl.twophase.msg;

import java.net.InetAddress;
import java.util.Set;
import sf2.core.Event;

public class TwoPhaseViewChange implements Event {

    protected byte[] key;

    protected Set<InetAddress> newMembers;

    public TwoPhaseViewChange(byte[] key, Set<InetAddress> newMembers) {
        this.key = key;
        this.newMembers = newMembers;
    }

    public byte[] getKey() {
        return key;
    }

    public Set<InetAddress> getNewMembers() {
        return newMembers;
    }
}
