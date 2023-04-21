package org.jcvi.common.core.symbol;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestByteGlyph.class, TestByteGlyphFactory.class, TestShortGlyph.class, TestShortGlyphFactory.class, TestEncodedByteGlyph.class, TestEncodedShortGlyph.class, TestDefaultShortGlyphCodec.class })
public class AllNumericGlyphUnitTests {
}
