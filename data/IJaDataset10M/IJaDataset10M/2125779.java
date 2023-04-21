package org.jcvi.glyph.encoder;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestRunLengthEncodedGlyphCodec.class, TestIllegalEncodedValueException.class, TestTigrQualitiesEncoder.class, TestTigrQualitiesEncoderCodec.class, TestTigrPeaksEncoder.class, TestTigrPeaksEncoderCodec.class, TestShortGlyphDeltaEncoderWhenEncoding.class, TestShortGlyphDeltaEncoderWhenDecoding.class })
public class AllEnocderUnitTests {
}
