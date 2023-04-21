package net.sf.adastra.test;

import org.jmock.cglib.MockObjectTestCase;
import org.jmock.core.constraint.IsCloseTo;
import static java.lang.Math.abs;

/**
 * I am a class that from which all tests should extend.
 * 
 * @author David Dunwoody (david.dunwoody@gmail.com)
 * @version $Id: AdAstraTestBase.java 22 2006-06-27 21:45:31Z ddunwoody $
 * 
 */
public abstract class AdAstraTestBase extends MockObjectTestCase {

    protected IsCloseTo isCloseTo(double operand) {
        return eq(operand, abs(operand / 10000f));
    }
}
