package org.jcvi.common.core.util;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestMathUtilMaxOf.class, TestMathUtilMinOf.class, MathUtilAvgOf.class, TestMathUtilMedianOf.class })
public class MathUtilSuite {
}
