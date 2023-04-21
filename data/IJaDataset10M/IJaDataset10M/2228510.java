package org.sulweb.infumon.common.session;

import org.sulweb.util.SparseArray;

/**
 *
 * @author lucio
 */
public class TE33xOperationStatusValuesList extends SparseArray<String> {

    /** Creates a new instance of TE33xOperationStatusValuesList */
    public TE33xOperationStatusValuesList() {
        super(5);
        set(0, "Ferma");
        set(1, "In infusione");
        set(2, "Spurgo");
        set(3, "KOR");
        set(4, "In pausa");
    }
}
