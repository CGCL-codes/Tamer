package org.archive.modules.extractor;

import org.archive.modules.ProcessorTestBase;
import org.archive.modules.extractor.AggressiveExtractorHTML;

/**
 * Unit test for {@link AggressiveExtractorHTML}.
 *
 * @author pjack
 */
public class AggressiveExtractorHTMLTest extends ProcessorTestBase {

    @Override
    protected Class getModuleClass() {
        return AggressiveExtractorHTML.class;
    }

    @Override
    protected Object makeModule() {
        return new AggressiveExtractorHTML();
    }
}
