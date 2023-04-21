package unittests;

import static org.testng.Assert.assertEquals;
import java.util.HashSet;
import java.util.Set;
import net.sf.extcos.internal.JavaClassResourceType;
import net.sf.extcos.internal.PackageImpl;
import net.sf.extcos.internal.ResourceResolverImpl;
import net.sf.extcos.resource.Resource;
import net.sf.extcos.resource.ResourceResolver;
import net.sf.extcos.selector.Package;
import net.sf.extcos.spi.ResourceType;
import net.sf.extcos.util.PropertyInjector;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import common.TestBase;

public class ResourceResolverImplTest extends TestBase {

    private ResourceResolver resolver;

    @BeforeClass
    public void initResolver() throws NoSuchFieldException {
        resolver = new ResourceResolverImpl();
        PropertyInjector injector = new PropertyInjector();
        injector.setTarget(resolver);
        injector.inject("classLoader", Thread.currentThread().getContextClassLoader());
    }

    @Test
    public void testGetResources() {
        Set<ResourceType> resourceTypes = new HashSet<ResourceType>();
        resourceTypes.add(JavaClassResourceType.javaClasses());
        Package basePackage = new PackageImpl(getProperty("resources.package"));
        Set<Resource> resources = resolver.getResources(resourceTypes, basePackage);
        assertEquals(resources.size(), getIntProperty("resources.amount"));
    }
}
