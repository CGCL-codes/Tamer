package org.tracfoundation.trac2001.primitive.file;

import org.tracfoundation.trac2001.TRAC2001;
import org.tracfoundation.trac2001.util.*;
import java.io.IOException;

/**
 * fc - Close an open file
 *
 * closes a file being used by the TRAC process.
 *
 * @author Edith Mooers, Trac Foundation http://tracfoundation.org
 * @version 1.0 (c) 2001
 */
public class FC {

    /**
     * Close the file on the jth channel and free up the channel
     *
     * @param <CODE>TRAC2001</CODE> the trac process.
     */
    public static void action(TRAC2001 trac) {
        Channel ch = trac.getChannel(trac.getActivePrimitive().jGet());
        if (ch != null) {
            try {
                ch.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
}
