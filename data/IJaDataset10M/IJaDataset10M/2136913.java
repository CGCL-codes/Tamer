package net.sourceforge.configured.rules.standard.typeresolver;

import java.util.Arrays;
import java.util.HashSet;
import org.junit.Test;
import net.sourceforge.configured.rules.standard.typeresolver.convention.StandardPackageNamingConvention;
import net.sourceforge.configured.rules.standard.typeresolver.impl.TestInterface1Impl;
import junit.framework.Assert;

public class NamingConventionTypeResolverTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testStandardNamingConvention() throws Exception {
        NamingConventionBasedConcreteTypeResolver resolver = new NamingConventionBasedConcreteTypeResolver();
        resolver.setIntantiatedTypes(new HashSet<Class<?>>(Arrays.asList(TestInterface1.class, TestInterface2.class)));
        Class<? extends TestInterface1> type = resolver.getImplementation(new ServiceProviderInfo(TestInterface1.class));
        Assert.assertTrue(resolver.canResolve(new ServiceProviderInfo(TestInterface1.class)));
        Assert.assertEquals(TestInterface1Impl.class, type);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonStandardNamingConvention() throws Exception {
        NamingConventionBasedConcreteTypeResolver resolver = new NamingConventionBasedConcreteTypeResolver();
        StandardPackageNamingConvention packageNamingConvention = new StandardPackageNamingConvention();
        packageNamingConvention.setSuffix("");
        resolver.setIntantiatedTypes(new HashSet<Class<?>>(Arrays.asList(TestInterface1.class, TestInterface2.class)));
        resolver.setPackageNamingConvention(packageNamingConvention);
        Class<? extends TestInterface2> type = resolver.getImplementation(new ServiceProviderInfo(TestInterface2.class));
        Assert.assertTrue(resolver.canResolve(new ServiceProviderInfo(TestInterface2.class)));
        Assert.assertEquals(TestInterface2Impl.class, type);
    }
}
