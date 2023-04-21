package org.jcvi.tasker;

import org.jcvi.glk.Plate;

/**
 * 
 * @author jsitz
 */
public interface PlateResolver {

    /**
     * Resolve the given plate name to a real {@link Plate} object.
     *
     * @param plateName The name of the plate.
     * @return A <code>Plate</code> with the given name, or <code>null</code> if the name could
     * not be resolved.
     */
    Plate resolvePlateName(String plateName);
}
