package ch.epfl.lsr.adhoc.simulator.daemon.protocol;

import java.io.Serializable;

/**
 * Upon reception of this message, the daemon starts waiting for the XML file
 * to receive
 *  
 * @version $Revision: 1.6 $ $Date: 2004/06/19 21:48:55 $
 * @author Author: Boris Danev and Aurelien Frossard
 */
public class SentNodeMsg extends DaemonProtocolMsg implements Serializable {

    public static final String codeRevision = "$Revision: 1.6 $ $Date: 2004/06/19 21:48:55 $ Author: Boris Danev and Aurelien Frossard";

    /** Character representation of a text file */
    private char[] m_file;

    /** Indicates the number of nodes that will follow this node */
    private int m_totalNext = 0;

    /** @param p_sessionID */
    public SentNodeMsg(int p_sessionID) {
        super(p_sessionID);
    }

    /** Sets the number of nodes that will follow this node */
    public void setTotalNbOfNodes(int p_totalNext) {
        m_totalNext = p_totalNext;
    }

    /** Gets the total number of following nodes */
    public int getTotalNbOfNodes() {
        return m_totalNext;
    }

    /** Sets a text file inside the object*/
    public void setFile(char[] p_file) {
        m_file = p_file;
    }

    /** Get the text file in form of characters */
    public char[] getFile() {
        return m_file;
    }

    /** Gives the string representation of the object */
    public String toString() {
        return "SentNodeMsg [sessionID = " + m_sessionID + "]";
    }
}
