package cerebralnexus.general.network;

import cerebralnexus.general.drawom.DrawnObjectKey;

/**
 *
 * @author Brent Couvrette
 */
public class NetDOKAData extends NetworkData {

    private DrawnObjectKey[] keys;

    public NetDOKAData(int actionID, DrawnObjectKey[] keys, String sessionID, int userID) {
        super(NetworkData.TYPE_DOKADATA, actionID, sessionID, userID);
        this.keys = keys;
    }

    /**
     * Creates a new NetDOKAData with just the given array of DrawnObjectKeys.
     * @param keys The DrawnObjectKeys in the new NetDOKAData.
     */
    public NetDOKAData(DrawnObjectKey[] keys) {
        super(NetworkData.TYPE_DOKADATA);
        this.keys = keys;
    }

    protected NetDOKAData(int type, int actionID, DrawnObjectKey[] keys, String sessionID, int userID) {
        super(type, actionID, sessionID, userID);
        this.keys = keys;
    }

    /**
     * Creates a new NetDOKAData with just the given array of DrawnObjectKeys.
     * @param keys The DrawnObjectKeys in the new NetDOKAData.
     */
    protected NetDOKAData(int type, DrawnObjectKey[] keys) {
        super(type);
        this.keys = keys;
    }

    public DrawnObjectKey[] getKeys() {
        return keys;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NetDOKAData other = (NetDOKAData) obj;
        if (this.keys != other.keys && (this.keys == null || !this.keys.equals(other.keys))) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.keys != null ? this.keys.hashCode() : 0);
        return hash;
    }
}
