package junitx.util;

import java.util.Hashtable;
import java.util.Map;

/**
 * Utility class that manages resources and allows separate tests to share them.
 *
 * <h4>Example</h4>
 *
 * The AllTests class registers all external resourses by calling the
 * <tt>addResource</tt> method of the ResourceManager:<p>
 *
 * <pre>
 *    public class AllTests {
 *
 *       public static Test suite() {
 *          TestSuite suite = new TestSuite();
 *          suite.addTestSuite(MyClassTest.class);
 *
 *          return new TestSetup(suite) {
 *              public void setUp()
 *                    throws Exception {
 *                 ResourceManager.addResource("RESOURCE_ID", new Object());
 *              }
 *          };
 *       }
 *    }
 * </pre>
 *
 * The test class simply gets the resource from the ResourceManager during the
 * set up process:<p>
 *
 * <pre>
 *    public class MyClassTest extends TestCase {
 *
 *       private Object res;
 *
 *       public void setUp() {
 *          res = ResourceManager.getResource("RESOURCE_ID");
 *       }
 *
 *       public void testXYZ() {
 *          res.XYZ();
 *       }
 *    }
 * </pre>
 *
 * To share a resource whithin the same <tt>TestCase</tt>:
 * 
 * <pre>
 *    public class MyClassTest extends TestCase {
 * 
 *       private Object res;
 *
 *       public void setUp() {
 *          if (!ResourceManager.contains("RESOURCE_ID")) {
 *             ResourceManager.addResource("RESOURCE_ID", new Object());
 *          }
 *          res = ResourceManager.getResource("RESOURCE_ID");
 *       }
 *
 *       public void testXYZ() {
 *          res.XYZ();
 *       }
 *    }
 * </pre>
 *
 * @version $Revision: 1.4 $ $Date: 2003/03/23 01:25:24 $
 * @author <a href="mailto:vbossica@users.sourceforge.net">Vladimir R. Bossicard</a>
 */
public class ResourceManager {

    private static Map resources = new Hashtable();

    /**
     * Don't let anyone have access to this constructor.
     */
    private ResourceManager() {
    }

    /**
     * Associates the resource to the key in the manager. Neither the key nor
     * the value can be null.  The value can be retrieved by calling the
     * getResource method with a key that is equal to the original key.
     *
     * @param key the resource key.
     * @param value the resource.
     * @throws IllegalArgumentException if the key is already used.
     */
    public static void addResource(String key, Object value) throws IllegalArgumentException {
        if (resources.containsKey(key)) {
            throw new IllegalArgumentException("Resource with key '" + key + "' already exists");
        }
        resources.put(key, value);
    }

    /**
     * Returns the resource to which the specified key is mapped in this
     * ResourceManager or <tt>null</tt> if the resource was not found.
     *
     * @param key a key in the ResourceManager.
     * @return Object the resource to which the key is mapped in this hashtable.
     * @throws NullPointerException if the key is null.
     */
    public static Object getResource(String key) throws NullPointerException {
        if (key == null) {
            throw new NullPointerException("Invalid key <null>");
        }
        return resources.get(key);
    }

    /**
     * Returns <tt>true</tt> if the ResourceManager contains a resource with
     * the specified key.
     */
    public static boolean containsResource(String key) {
        return resources.containsKey(key);
    }

    /**
     * Removes the key (and its corresponding resource) from this
     * ResourceManager.
     *
     * @param key the key that needs to be removed.
     */
    public static void removeResource(String key) {
        resources.remove(key);
    }
}
