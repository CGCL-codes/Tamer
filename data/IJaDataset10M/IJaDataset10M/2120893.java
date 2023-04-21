package org.apache.commons.jrcs.diff;

/**
 * Thrown whenever a delta cannot be applied as a patch to a given text.
 *
 * @version $Revision: 1.2 $ $Date: 2003/05/06 14:59:12 $
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @see Delta
 * @see Diff
 */
public class PatchFailedException extends DiffException {

    public PatchFailedException() {
    }

    public PatchFailedException(String msg) {
        super(msg);
    }
}
