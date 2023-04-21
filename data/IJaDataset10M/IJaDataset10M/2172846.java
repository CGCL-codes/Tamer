package plasmid.vHist;

import plasmid.topology.Replica;
import plasmid.replica.ActiveReplicaPersistent;

/** The extrinisic state used by the core VHist objects.
  * The versionHistory package makes use of the GoF "flyweight" 
  * pattern
  * which means we keep our version info compact by building
  * the implied context (like which volume you're in, which replica 
  * is active
  * , etc) in a separate transient class. 
  * <p>
  * Most of the VHhistContext  fields
  * are public visible to allow
  * use with minimal overhead (flyweight can be runtime expensive).
  * Get&Set methods would be cleaner, but significantly slower.
  **/
public class VHistContext implements plasmid.Persistible {

    /**
     * name of current object. public for speed.
     **/
    public String curItemName;

    /**
     * The currently active replica (i.e., the one that's changing).
     * public for speed.
     **/
    public ActiveReplicaPersistent activeReplica;

    public VHistContext(ActiveReplicaPersistent ar) {
        activeReplica = ar;
    }

    /**
     * Return the current timestap...
     * Not sure why I broke this out.
     * Probably need either more info about a change, or less.
     **/
    long getTimeStamp() {
        return System.currentTimeMillis();
    }

    long nextGen() {
        return activeReplica.nextGen();
    }
}
