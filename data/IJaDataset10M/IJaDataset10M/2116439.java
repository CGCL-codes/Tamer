package org.springmodules.cache.provider.jcs;

import java.lang.reflect.Method;
import junit.framework.TestCase;
import org.easymock.classextension.MockClassControl;

/**
 * <p>
 * Unit Test for <code>{@link JcsProfileValidator}</code>.
 * </p>
 * 
 * @author Alex Ruiz
 * 
 * @version $Revision: 458 $ $Date: 2006-02-23 23:52:57 -0500 (Thu, 23 Feb 2006) $
 */
public final class JcsProfileValidatorTests extends TestCase {

    /**
   * Primary object (instance of the class to test).
   */
    private JcsProfileValidator cacheProfileValidator;

    /**
   * Controls the behavior of <code>{@link #cacheProfileValidator}</code>.
   */
    private MockClassControl cacheProfileValidatorControl;

    /**
   * Constructor.
   * 
   * @param name
   *          the name of the Test Case.
   */
    public JcsProfileValidatorTests(String name) {
        super(name);
    }

    /**
   * Sets up the test fixture.
   */
    private void setUpCacheProfileValidator() {
        this.cacheProfileValidator = new JcsProfileValidator();
    }

    /**
   * Sets up
   * <ul>
   * <li><code>{@link #cacheProfileValidator}</code></li>
   * <li><code>{@link #cacheProfileValidatorControl}</code></li>
   * </ul>
   * 
   * @param methodsToMock
   *          the methods of <code>cacheProfileValidator</code> to mock.
   */
    private void setUpCacheProfileValidatorAsMockObject(Method[] methodsToMock) {
        Class classToMock = JcsProfileValidator.class;
        this.cacheProfileValidatorControl = MockClassControl.createControl(classToMock, null, null, methodsToMock);
        this.cacheProfileValidator = (JcsProfileValidator) this.cacheProfileValidatorControl.getMock();
    }

    /**
   * Verifies that the method
   * <code>{@link JcsProfileValidator#validateCacheName(String)}</code>
   * considers an empty String as an invalid cache name.
   */
    public void testValidateCacheNameWithEmptyString() {
        this.setUpCacheProfileValidator();
        try {
            this.cacheProfileValidator.validateCacheName("");
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException exception) {
        }
    }

    /**
   * Verifies that the method
   * <code>{@link JcsProfileValidator#validateCacheName(String)}</code>
   * considers a String that is not empty as a valid cache name.
   */
    public void testValidateCacheNameWithNotEmptyString() {
        this.setUpCacheProfileValidator();
        String cacheName = "CacheName";
        this.cacheProfileValidator.validateCacheName(cacheName);
    }

    /**
   * Verifies that the method
   * <code>{@link JcsProfileValidator#validateCacheName(String)}</code>
   * considers a String equal to <code>null</code> as an invalid cache name.
   */
    public void testValidateCacheNameWithStringEqualToNull() {
        this.setUpCacheProfileValidator();
        try {
            this.cacheProfileValidator.validateCacheName(null);
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException exception) {
        }
    }

    /**
   * Verifies that the method
   * <code>{@link JcsProfileValidator#validateCacheProfile(JcsProfile)}</code>
   * validates the name of the cache set in the specified cache profile.
   */
    public void testValidateCacheProfile() throws Exception {
        Class classToMock = JcsProfileValidator.class;
        Method validateCacheNameMethod = classToMock.getDeclaredMethod("validateCacheName", new Class[] { String.class });
        Method[] methodsToMock = new Method[] { validateCacheNameMethod };
        this.setUpCacheProfileValidatorAsMockObject(methodsToMock);
        String cacheName = "CacheName";
        JcsProfile cacheProfile = new JcsProfile();
        cacheProfile.setCacheName(cacheName);
        this.cacheProfileValidator.validateCacheName(cacheName);
        this.cacheProfileValidatorControl.replay();
        this.cacheProfileValidator.validateCacheProfile(cacheProfile);
        this.cacheProfileValidatorControl.verify();
    }

    /**
   * Verifies that the method
   * <code>{@link JcsProfileValidator#validateCacheProfile(Object)}</code>
   * throws an <code>IllegalArgumentException</code> if the specified argument
   * is not an instance of <code>{@link JcsProfile}</code>.
   */
    public void testValidateCacheProfileObjectWithObjectNotInstanceOfJcsCacheProfile() {
        this.setUpCacheProfileValidator();
        try {
            this.cacheProfileValidator.validateCacheProfile(new Object());
            fail("An IllegalArgumentException should have been thrown");
        } catch (IllegalArgumentException exception) {
        }
    }
}
