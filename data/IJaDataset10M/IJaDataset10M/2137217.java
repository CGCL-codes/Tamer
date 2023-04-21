package simplefix;

public interface Session {

    FixVersion getFixVersion();

    String getSenderCompID();

    String getTargetCompID();

    /**
     * This function is to send messages for the application. This is one of the
     * core out points for your FIX application. If, for example, your
     * application is a buy-side OMS, this is where you will send your new order
     * requests. If you were a sell side, you would send your execution reports
     * here. *
     * 
     * @param message
     *            FIX message
     * @param sessionId
     *            FIX session ID
     */
    void sendAppMessage(Message message);
}
