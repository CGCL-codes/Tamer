package plasmid.topology;

import plasmid.Persistible;
import plasmid.Uuid;
import plasmid.Stats;

/**
 * Represents the static state of an instance of the P2PEng1 sync engine.
 * Used at topology server and at engine hosts.
 **/
public class P2PEng1 extends Engine implements Persistible {

    protected long sleepMs = Long.getLong("plasmid.P2PEng1.sleepMs", 2 * 60 * 1000L).longValue();

    public long getSleepMs() {
        return sleepMs;
    }

    public void setSleepMs(long sleepMs) {
        this.sleepMs = sleepMs;
    }

    /** The unique-id of our peer.**/
    protected long peerUuid = Uuid.UUID_NONE;

    public long getPeerUuid() {
        return peerUuid;
    }

    /** The RMI URL of the peer's plasmid.host root.
     * ### We should create a persistent class to represent a
     * remote host, and have a remote hosts table in our
     * "synchronicity.Host".
     **/
    protected String peerAddress;

    public String getPeerAddress() {
        return peerAddress;
    }

    /**
     *
     **/
    public P2PEng1(long myUuid, Replica repl) {
        super(myUuid, repl.getLReplicaUuid(), repl.getHostUuid());
        this.replUuid = repl.getUuid();
    }

    public P2PEng1(P2PEng1 orig) {
        super(orig.uuid, orig.lReplicaUuid, orig.hostUuid);
        this.replUuid = orig.replUuid;
        this.peerUuid = orig.peerUuid;
        this.peerAddress = orig.peerAddress;
    }

    public void setPeer(P2PEng1 peer, String peerAddress) {
        peerUuid = peer.getUuid();
        setPeerAddress(peerAddress);
    }

    public void setPeerAddress(String peerAddress) {
        this.peerAddress = peerAddress;
    }

    public String getEngineType() {
        return this.getClass().getName();
    }

    public Engine createClientInstance(plasmid.host.Host h) {
        return new plasmid.engine.P2PEng1Persistent((plasmid.host.Host) h, this);
    }

    public boolean updateFrom(Engine e) {
        boolean ret = false;
        P2PEng1 pe = (P2PEng1) e;
        if (!pe.getPeerAddress().equals(getPeerAddress())) {
            setPeerAddress(pe.getPeerAddress());
            ret = true;
        }
        if (pe.getSleepMs() != getSleepMs()) {
            setSleepMs(pe.getSleepMs());
            ret = true;
        }
        return ret;
    }

    /**
     * Placeholder - override if you keep Stats!
     **/
    public Stats getStats() {
        return null;
    }

    public String toString() {
        return "{ P2PEng1" + " peerAddress " + peerAddress + " peerUuid " + peerUuid + " sleepMs " + sleepMs + super.toString() + " }";
    }
}
