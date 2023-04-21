package tudresden.ocl20.pivot.ocl22java.test.aspectj.constraintkinds;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * <p>
 * Provides a jUnit Test Suite containing test cases to test the correct
 * execution of AspectJ files generated for the different kinds of OCL
 * constraints (body, init, def, derive, pre, post, inv).
 * </p>
 * 
 * @author Claas Wilke
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ TestBody.class, TestDef.class, TestPost.class, TestPre.class })
public class AllConstraintKindTests {
}
