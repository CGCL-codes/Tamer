package org.jcvi.common.core.symbol.pos;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestPeaks.class, TestPeaksUtil.class, TestTigrPeaksEncoder.class, TestTigrPeaksEncoderCodec.class })
public class AllPeaksUnitTests {
}
