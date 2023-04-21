package org.jcvi.common.core.seq.read.trace.pyro.sff;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestSFFUtil_convertFlowgramValues.class, TestSFFUtil_paddedBytes.class, TestSFFFlowgram_computeValues.class, TestSFFUtil_numberOfIntensities.class, TestSffUtil_Linkers.class, TestSffNameUtil.class })
public class AllSFFUtilUnitTests {
}
