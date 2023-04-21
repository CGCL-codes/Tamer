package org.tigr.microarray.mev.cgh.CGHAlgorithms.NumberOfAlterations.CloneAlterations;

import org.tigr.microarray.mev.cluster.gui.IData;

/**
 *
 * @author  Adam Margolin
 * @author Raktim Sinha
 */
public class CloneAmplifications2Copy extends CloneAlterations {

    /** Creates a new instance of MostDeleted2Copy */
    public CloneAmplifications2Copy() {
        nodeName = "CloneAmplifications2Copy";
    }

    protected boolean isAltered(int copyNumber) {
        if (copyNumber > 1 && copyNumber != IData.BAD_CLONE && copyNumber != IData.NO_COPY_CHANGE) {
            return true;
        } else {
            return false;
        }
    }
}
