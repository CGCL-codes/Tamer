package org.parallelj.common.jdt.checkers;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite for org.parallelj.common.jdt.checkers package
 * 
 * @author Atos WorldLine
 */
@RunWith(Suite.class)
@SuiteClasses(value = { FieldCheckerTestCase.class, JavaCodeCheckerTestCase.class, MethodCheckerTestCase.class })
public class CommonToolsTests {
}
