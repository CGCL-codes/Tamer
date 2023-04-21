package pl.szpadel.android.gadu;

public class ConnectionState {

    public static final int CONNECTED = 0;

    public static final int CONNECTING = 1;

    public static final int DISCONNECTED = 2;

    public static final int REASON_NONE = 100;

    public static final int REASON_LOGIN_FAILED = 101;

    public static final int REASON_NETWORK_PROBLEM = 103;

    public static final int REASON_DISCONNECTED_BY_USER = 104;

    private int mState;

    private int mReason;

    public int getState() {
        return mState;
    }

    public int getReason() {
        return mReason;
    }

    protected ConnectionState(int state, int reason) {
        mState = state;
        mReason = reason;
    }

    public static ConnectionState Connected() {
        return new ConnectionState(CONNECTED, REASON_NONE);
    }

    public static ConnectionState Connecting() {
        return new ConnectionState(CONNECTING, REASON_NONE);
    }

    public static ConnectionState Disconnected(int reason) {
        return new ConnectionState(DISCONNECTED, reason);
    }

    public String toString() {
        return "ConnectionState [state=" + mState + ", reason=" + mReason + " ]";
    }
}
