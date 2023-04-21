package de.dermoba.srcp.devices;

import de.dermoba.srcp.client.SRCPSession;
import de.dermoba.srcp.common.exception.SRCPException;
import de.dermoba.srcp.common.exception.SRCPIOException;

public class SESSION {

    private SRCPSession session;

    private int bus = 0;

    public SESSION(SRCPSession pSession) {
        session = pSession;
    }

    /** SRCP syntax: GET <bus> SESSION */
    public String get(int pSessionID) throws SRCPException {
        return session.getCommandChannel().send("GET " + bus + " SESSION " + pSessionID);
    }

    /** SRCP syntax: TERM <bus> SESSION */
    public String term() throws SRCPException {
        String result = "";
        try {
            result = session.getCommandChannel().send("TERM " + bus + " SESSION");
        } catch (SRCPIOException e) {
        }
        return result;
    }

    /** SRCP syntax: TERM <bus> SESSION [<sessionid>] */
    public String term(int pSessionID) throws SRCPException {
        return session.getCommandChannel().send("TERM " + bus + " SESSION " + pSessionID);
    }
}
