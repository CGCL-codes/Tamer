package org.tracfoundation.trac2001.primitive.file;

import org.tracfoundation.trac2001.TRAC2001;
import org.tracfoundation.trac2001.util.*;
import java.io.IOException;

/**
 * fpr - Position Read
 *
 * Read the current position of the file pointer.
 *
 * @author Edith Mooers, Trac Foundation http://tracfoundation.org
 * @version 1.0 (c) 2001
 */
public class FPR {

    /**
     * Read the current position of the file pointer.
     *
     * @param <CODE>TRAC2001</CODE> the trac process.
     */
    public static void action(TRAC2001 trac) {
        Primitive active = trac.getActivePrimitive();
        if (active.length() >= 1) {
            Channel ch = trac.getChannel(active.jGet());
            if (ch != null) {
                try {
                    active.addValue(ch.getFilePointer());
                } catch (Exception e) {
                    trac.zReturn(active.getArg(1));
                }
            }
        }
    }
}
