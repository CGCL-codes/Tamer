package org.rascalli.mbe;

/**
 * <p>
 * Generates a sequence of UUIDs starting with 0.
 * </p>
 * 
 * <p>
 * <b>Company:&nbsp;</b> SAT, Research Studios Austria
 * </p>
 * 
 * <p>
 * <b>Copyright:&nbsp;</b> (c) 2007
 * </p>
 * 
 * <p>
 * <b>last modified:</b><br/> $Author: $<br/> $Date: $<br/> $Revision: $
 * </p>
 * 
 * @author Christian Schollum
 */
public final class SequentialUUIDGenerator implements UUIDGenerator {

    private int nextUUID = 0;

    public String generateUUID() {
        synchronized (this) {
            return "" + (nextUUID++);
        }
    }
}
