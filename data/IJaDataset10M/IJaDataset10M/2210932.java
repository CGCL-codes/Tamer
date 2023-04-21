package com.sshtools.j2ssh.transport;

import com.sshtools.j2ssh.util.State;
import java.io.IOException;

/**
 *
 *
 * @author $author$
 * @version $Revision: 1.24 $
 */
public class TransportProtocolState extends State {

    /**  */
    public static final int UNINITIALIZED = 1;

    /**  */
    public static final int NEGOTIATING_PROTOCOL = 2;

    /**  */
    public static final int PERFORMING_KEYEXCHANGE = 3;

    /**  */
    public static final int CONNECTED = 4;

    /**  */
    public static final int DISCONNECTED = 5;

    /**  */
    public IOException lastError;

    /**  */
    public String reason = "";

    /**
     * Creates a new TransportProtocolState object.
     */
    public TransportProtocolState() {
        super(UNINITIALIZED);
    }

    /**
     *
     *
     * @param lastError
     */
    protected void setLastError(IOException lastError) {
        this.lastError = lastError;
    }

    /**
     *
     *
     * @return
     */
    public boolean hasError() {
        return lastError != null;
    }

    /**
     *
     *
     * @return
     */
    public IOException getLastError() {
        return lastError;
    }

    /**
     *
     *
     * @param reason
     */
    protected void setDisconnectReason(String reason) {
        this.reason = reason;
    }

    /**
     *
     *
     * @return
     */
    public String getDisconnectReason() {
        return reason;
    }

    /**
     *
     *
     * @param state
     *
     * @return
     */
    public boolean isValidState(int state) {
        return ((state == UNINITIALIZED) || (state == NEGOTIATING_PROTOCOL) || (state == PERFORMING_KEYEXCHANGE) || (state == CONNECTED) || (state == DISCONNECTED));
    }
}
