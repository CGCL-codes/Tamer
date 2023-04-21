package org.devyant.magicbeans;

/**
 * BasicTestCase is a <b>cool</b> class.
 * 
 * @author ftavares
 * @version $Revision: 1.1 $ $Date: 2005/11/16 22:03:51 $ ($Author: ftavares $)
 * @since Jul 3, 2005 5:35:40 AM
 */
public abstract class BasicTestCase extends AbstractTestCase {

    /**
     * Creates a new <code>BasicTestCase</code> instance.
     * @param testName
     */
    public BasicTestCase(String testName) {
        super(testName);
    }

    /**
     * @see org.devyant.magicbeans.AbstractTestCase#getValue(java.lang.Object)
     */
    protected Object getValue(Object bean) {
        return bean;
    }
}
