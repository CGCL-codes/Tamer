package oracle.toplink.essentials.internal.sequencing;

import java.util.Vector;
import java.util.Hashtable;

class PreallocationHandler implements SequencingLogInOut {

    protected Hashtable preallocatedSequences;

    public PreallocationHandler() {
        super();
    }

    /**
    * PROTECTED:
    * return a vector from the global sequences based on
    * seqName.  If there is not one, put it there.
    */
    protected Vector getPreallocatedSequences(String seqName) {
        Vector sequencesForName;
        synchronized (preallocatedSequences) {
            sequencesForName = (Vector) preallocatedSequences.get(seqName);
            if (sequencesForName == null) {
                sequencesForName = new Vector();
                preallocatedSequences.put(seqName, sequencesForName);
            }
        }
        return sequencesForName;
    }

    public void onConnect() {
        initializePreallocated();
    }

    public void onDisconnect() {
        preallocatedSequences = null;
    }

    public boolean isConnected() {
        return preallocatedSequences != null;
    }

    public void initializePreallocated() {
        preallocatedSequences = new Hashtable(20);
    }

    public void initializePreallocated(String seqName) {
        preallocatedSequences.remove(seqName);
    }

    public Vector getPreallocated(String seqName) {
        return getPreallocatedSequences(seqName);
    }

    public void setPreallocated(String seqName, Vector sequences) {
        getPreallocatedSequences(seqName).addAll(sequences);
    }
}
