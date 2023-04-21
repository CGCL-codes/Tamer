package net.sf.JUnit_Penetration.ObjectRandomizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import net.sf.JUnit_Penetration.ObjectRandomizer.api.IRandomizeable;
import net.sf.JUnit_Penetration.RuntimeInformatory.StackTraceInformation;
import org.junit.Test;

/**
 * Project	JUnit-Penetration
 * Package	net.sf.JUnit_Penetration.ObjectRandomizer
 * File		IntRandomizerTest.java
 * History	Mar 29, 2009 - initial version
 * 
 * @author	bb
 * @date	Mar 29, 2009
 *
 * Test the class IntParameter.
 */
public class IntRandomizerTest {

    /**
	 * attributes
	 */
    private StackTraceInformation info = new StackTraceInformation();

    /**
	 * Test method for {@link net.sf.JUnit_Penetration.ObjectRandomizer.IntRandomizer#IntParameter()}.
	 */
    @Test
    public void testIntRandomizer() {
        info.printCurrentInfo();
        IRandomizeable<Integer> intRand = new IntRandomizer();
        assertNotNull("object should not be null.", intRand);
    }

    /**
	 * Test method for {@link net.sf.JUnit_Penetration.ObjectRandomizer.IntRandomizer#nextParameter()}.
	 */
    @Test
    public void testNextObject() {
        info.printCurrentInfo();
        IRandomizeable<Integer> intRand = new IntRandomizer();
        int n = intRand.nextObject();
        System.out.println(intRand.nextObject());
    }

    /**
	 * Test method for {@link net.sf.JUnit_Penetration.ObjectRandomizer.IntRandomizer#getParameterType()}.
	 */
    @Test
    public void testGetObjectType() {
        info.printCurrentInfo();
        IRandomizeable intRand = new IntRandomizer();
        assertEquals("objects should be equal", int.class, intRand.getObjectType());
    }
}
