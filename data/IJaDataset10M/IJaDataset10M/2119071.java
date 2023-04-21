package DE.FhG.IGD.semoa.server;

import DE.FhG.IGD.util.*;
import java.io.*;
import java.util.*;

/**
 * Implemented by packets.
 *
 * @author Volker Roth
 * @version "$Id: Packet.java 625 2002-04-18 20:01:08Z upinsdor $"
 */
public interface Packet {

    public String getRecipient();

    public String getSender();

    /**
     * @return The data of this request as a byte array. For
     *   efficiency reasons, the byte array that is also used
     *   internally is returned. Callers must make sure that
     *   this array is not modified.
     */
    public byte[] getData();

    /**
     * @return The data converted into a string.
     */
    public String getDataAsString();

    public void write(OutputStream out) throws IOException;

    public void read(InputStream in) throws IOException;
}
