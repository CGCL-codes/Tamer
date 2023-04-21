package org.jcvi.common.core.seq;

import org.jcvi.common.core.seq.fastx.AllFastXTests;
import org.jcvi.common.core.seq.plate.AllPlateUnitTests;
import org.jcvi.common.core.seq.read.trace.AllTraceUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ AllFastXTests.class, AllTraceUnitTests.class, AllPlateUnitTests.class })
public class AllSeqUnitTests {
}
