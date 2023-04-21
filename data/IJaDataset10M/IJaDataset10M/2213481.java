package bugs;

import java.util.Map;
import java.util.Properties;
import javax.util.jcache.CacheAccessFactory;
import junit.framework.TestCase;
import org.fjank.jcache.CacheImpl;

public class Bug974609_2Test extends TestCase {

    /**
	 * Tests the diskSize. Sets the maxObjects to 1, inserts 3 elements, where
	 * all elements are one Mb in size. The first goes to memory, the second
	 * should be spooled to disk, while the third should not fit anywhere, and
	 * an {@link javax.util.jcache.CacheException} should be thrown. The
	 * diskCaching system has the ability to read the size of the objects, so
	 * there is no need to specify the exact size of each object. Since the
	 * diskCache has some sizeoverhead, each object is slightly smaller than 1Mb
	 * to avoid that the cache stops before at least one object is added..
	 * 
	 * @throws Exception if any exceptions occur.
	 */
    public final void testSetDiskSize() throws Exception {
        Properties props = new Properties();
        props.put("diskSize", "1");
        props.put("maxObjects", "2");
        CacheImpl cache = CacheImpl.getCache(false);
        cache.open(props);
        CacheAccessFactory factory = CacheAccessFactory.getInstance();
        Map map = factory.getMapAccess();
        map.put("one", new byte[1024 * 1024]);
        map.put("two", new byte[1024 * 900]);
        try {
            map.put("three", new byte[1024 * 900]);
            fail("Should throw CacheException, but did not.");
        } catch (IllegalArgumentException e) {
        }
    }
}
