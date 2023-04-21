package org.springunit.framework.junit4;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springunit.framework.SpringUnitTest;

/**
 * SpringUnitTest enabled for use with JUnit4 annotations.<br/>
 * <br/>
 * The approach followed here follows the example given by Derek Young,
 * "Adapting Spring's JUnit 3.x base classes to JUnit 4",
 * Tuesday, April 3, 2007,
 * http://dmy999.com/article/21/adapting-a-springs-junit-3x-base-classes-to-junit-4.
 * <br/>
 * 
 * @author <a href="mailto:ted@velkoff.com">Ted Velkoff</a>
 *
 */
@RunWith(NameRunner.class)
public abstract class SpringUnit4Test extends SpringUnitTest {

    @Before
    public final void callSetUp() throws Exception {
        this.setName(NameRunner.getTestMethodName());
        super.setUp();
    }

    @After
    public final void callTearDown() throws Exception {
        super.tearDown();
    }
}
