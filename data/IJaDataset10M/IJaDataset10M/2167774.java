package org.jmol.adapter.readers.quantum;

import org.jmol.adapter.smarter.*;

/**
 * Reader for Gaussian Wfn files -- not implemented yet
 *
 **/
public class GaussianWfnReader extends AtomSetCollectionReader {

    @Override
    protected void initializeReader() {
        continuing = false;
    }
}
