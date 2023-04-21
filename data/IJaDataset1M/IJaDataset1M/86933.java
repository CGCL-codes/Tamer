package org.jmol.api;

import java.util.BitSet;

/**
 * listen to atom selections in a Jmol Viewer
 */
public interface JmolSelectionListener {

    /**
   * Called when the selected atoms change
   * @param selection bit set giving selection of atom indexes
   */
    public void selectionChanged(BitSet selection);
}
