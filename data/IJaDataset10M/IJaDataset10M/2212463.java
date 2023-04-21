package org.jcvi.trace.sanger.phd;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author dkatzel
 *
 *
 */
public class TestIndexPhdFileDataStore extends AbstractTestPhdDataStore {

    @Override
    protected PhdDataStore createPhdDataStore(File phdfile) throws FileNotFoundException {
        return new IndexedPhdFileDataStore(phdfile);
    }
}
