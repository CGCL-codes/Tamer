package org.lpny.gr.builder.factory;

import groovy.util.FactoryBuilderSupport;
import java.util.HashMap;
import java.util.Map;
import org.restlet.Directory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author keke
 * @version
 * @since
 * @revision $Revision: 33 $
 */
@Test(groups = { "unittest" })
public class DirectoryFactoryTest extends AbstractFactoryTest {

    private DirectoryFactory fixture;

    @BeforeTest(groups = { "unittest" })
    @Test(groups = { "unittest" })
    public void construct() {
        fixture = new DirectoryFactory();
    }

    public void testNewInstance() throws InstantiationException, IllegalAccessException {
        final FactoryBuilderSupport builder = createMockBuilder();
        final Map attributes = new HashMap();
        attributes.put("root", ".");
        final Directory directory = (Directory) fixture.newInstance(builder, "directory", null, attributes);
        assert directory != null;
    }

    @Test(groups = { "unittest" }, expectedExceptions = { IllegalArgumentException.class })
    public void testNewInstanceNullRoot() throws InstantiationException, IllegalAccessException {
        final FactoryBuilderSupport builder = createMockBuilder();
        final Map attributes = new HashMap();
        final Directory directory = (Directory) fixture.newInstance(builder, "directory", null, attributes);
        assert directory != null;
    }
}
