package org.archive.modules.fetcher;

import org.archive.modules.ProcessorTestBase;
import org.archive.modules.fetcher.FetchFTP;

/**
 * @author pjack
 *
 */
public class FetchFTPTest extends ProcessorTestBase {

    @Override
    protected Class getModuleClass() {
        return FetchFTP.class;
    }

    @Override
    protected Object makeModule() {
        return new FetchFTP();
    }
}
