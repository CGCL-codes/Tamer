package org.archive.processors.extractor;

import org.archive.processors.ProcessorTestBase;

/**
 * Unit test for {@link ExtractorImpliedURI}.
 *
 * @author pjack
 */
public class ExtractorImpliedURITest extends ProcessorTestBase {

    @Override
    protected Class getModuleClass() {
        return ExtractorImpliedURI.class;
    }

    @Override
    protected Object makeModule() {
        return new ExtractorImpliedURI();
    }
}
