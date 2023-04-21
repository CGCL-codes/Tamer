package org.epo.jpxi.rebuilders;

import org.epo.jpxi.generic.*;
import org.epo.jpxi.shared.*;

/**
 * Jpxi Null Rebuilder, used when no rebuilder is needed.
 * Creation date: (03/09/01 15:22:09)
 */
public class JpxiNullRebuilder implements JpxiDocRebuilder {

    /**
 * JpxiNullRebuilder Standard constructor.
 */
    public JpxiNullRebuilder() {
        super();
    }

    public JpxiNullRebuilder(JpxiDocSkeleton theDocSkeleton, JpxiOffRangeList theJpxiOffRangeList) {
        super();
    }

    /**
 * This method returns the given buffer record.
 * Creation date: (03/09/01 15:22:58)
 * @param theBufferRecord JpxiBufferRecord
 * @return JpxiBufferRecord
 * @exception org.epo.jpxi.shared.JpxiException
 */
    public JpxiBufferRecord rebuilder(JpxiBufferRecord theBufferRecord) throws org.epo.jpxi.shared.JpxiException {
        return theBufferRecord;
    }
}
