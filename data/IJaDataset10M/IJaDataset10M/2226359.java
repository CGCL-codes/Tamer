package net.sf.dozer.functional_tests;

import net.sf.dozer.util.mapping.DataObjectInstantiator;
import net.sf.dozer.util.mapping.DozerBeanMapper;
import net.sf.dozer.util.mapping.NoProxyDataObjectInstantiator;
import java.util.ArrayList;

/**
 * @author Dmitry Buzdin
 */
public class ClassloaderTest extends AbstractMapperTest {

    public void testClassloader() {
        ArrayList files = new ArrayList();
        files.add("classloader.xml");
        mapper = new DozerBeanMapper(files);
        assertNotNull(mapper);
    }

    protected DataObjectInstantiator getDataObjectInstantiator() {
        return NoProxyDataObjectInstantiator.INSTANCE;
    }
}
