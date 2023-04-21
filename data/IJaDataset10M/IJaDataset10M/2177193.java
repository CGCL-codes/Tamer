package org.springmodules.cache.interceptor;

import java.lang.reflect.Method;
import java.util.Collection;
import junit.framework.TestCase;

/**
 * <p>
 * Unit Test for <code>{@link AbstractMetadataCacheAttributeSource}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 458 $ $Date: 2006-02-23 23:52:57 -0500 (Thu, 23 Feb 2006) $
 */
public class MetadataCacheAttributeSourceTests extends TestCase {

    /**
   * Primary object (instance of the class to test).
   */
    private AbstractMetadataCacheAttributeSource attributeSource;

    /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
    public MetadataCacheAttributeSourceTests(String name) {
        super(name);
    }

    /**
   * Sets up the test fixture.
   */
    protected void setUp() throws Exception {
        super.setUp();
        this.attributeSource = new AbstractMetadataCacheAttributeSource() {

            protected Collection findAllAttributes(Method argMethod) {
                return null;
            }
        };
    }

    /**
   * Verifies that the method
   * <code>{@link AbstractMetadataCacheAttributeSource#getAttributeEntryKey(Method, Class)}</code>
   * creates a key by concatenating the name of the given class and the hash
   * code of the given method.
   */
    public void testGetAttributeEntryKey() throws Exception {
        Class targetClass = String.class;
        Method method = targetClass.getMethod("charAt", new Class[] { int.class });
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(targetClass);
        stringBuffer.append(System.identityHashCode(method));
        String expectedKey = stringBuffer.toString();
        Object actualKey = this.attributeSource.getAttributeEntryKey(method, targetClass);
        assertEquals("<Key>", expectedKey, actualKey);
    }
}
