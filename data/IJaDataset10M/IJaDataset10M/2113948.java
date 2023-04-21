package ch.epfl.lsr.adhoc.simulator.daemon.protocol;

/**
 * This abstract class represents the protocol between the main simulator
 * and the deamon server running on a remote machine
 * 
 * @version $Revision: 1.6 $ $Date: 2004/06/19 21:48:56 $
 * @author Author: Boris Danev and Aurelien Frossard
 */
public abstract class DaemonProtocolMsg implements java.io.Serializable {

    public static final String codeRevision = "$Revision: 1.6 $ $Date: 2004/06/19 21:48:56 $ Author: Boris Danev and Aurelien Frossard";

    /** Which session this message concerns */
    protected int m_sessionID;

    /** Default constructor */
    public DaemonProtocolMsg(int p_sessionID) {
        m_sessionID = p_sessionID;
    }

    /** Returns the session attached to this message */
    public int getMesgSessionID() {
        return m_sessionID;
    }
}
