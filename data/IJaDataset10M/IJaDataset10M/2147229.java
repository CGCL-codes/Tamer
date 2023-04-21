package quickfix;

public class PostgreSQLStoreFactory implements MessageStoreFactory {

    private long cppPointer;

    private SessionSettings settings = null;

    public PostgreSQLStoreFactory(SessionSettings settings) {
        this.settings = settings;
        create();
    }

    protected void finalize() {
        destroy();
    }

    private native void create();

    private native void destroy();

    public native MessageStore create(SessionID sessionID);
}
