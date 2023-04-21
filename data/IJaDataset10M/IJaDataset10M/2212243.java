package com.googlecode.proxymatic.apps.factory;

import com.googlecode.proxymatic.core.FaultTestCase;
import java.util.HashMap;
import java.util.Map;

public class FactoryBuilderUnitTest extends FaultTestCase {

    private FactoryBuilder factoryBuilder;

    private StaticImplementationLookup implementationLookup;

    private StaticInstanceProvider instanceProvider;

    public void setUp() {
        implementationLookup = new StaticImplementationLookup();
        implementationLookup.addMapping(Map.class, HashMap.class);
        instanceProvider = new StaticInstanceProvider();
        factoryBuilder = new ProxyFactoryBuilder(implementationLookup, instanceProvider);
    }

    public void testSimpleCase() {
        FactoryBuilder factoryBuilder = new ProxyFactoryBuilder();
        TestFactory testFactory = factoryBuilder.createFactory(TestFactory.class);
        assertEquals(new Integer(42), testFactory.newInteger(42));
    }

    public void testReplaceImplementation() {
        implementationLookup.addMapping(Map.class, HashMap.class);
        FactoryBuilder factoryBuilder = new ProxyFactoryBuilder(implementationLookup, InstanceProvider.NONE);
        TestFactoryWithNonConcreteReturn testFactory = factoryBuilder.createFactory(TestFactoryWithNonConcreteReturn.class);
        assertEquals(new HashMap(), testFactory.newHashMap());
    }

    public void testReplaceImplementationWithAnnotations() {
        implementationLookup.addMapping(Map.class, HashMap.class);
        FactoryBuilder factoryBuilder = new ProxyFactoryBuilder();
        TestFactoryWithNonConcreteReturnAndAnnotation testFactory = factoryBuilder.createFactory(TestFactoryWithNonConcreteReturnAndAnnotation.class);
        assertEquals(new HashMap(), testFactory.newHashMap());
    }

    public void testMissingParameters() {
        One instanceOne = new One();
        Two instanceTwo = new Two();
        Three instanceThree = new Three();
        instanceProvider.registerInstance(One.class, instanceOne);
        instanceProvider.registerInstance(Two.class, instanceTwo);
        instanceProvider.registerInstance(Three.class, instanceThree);
        FactoryBuilder factoryBuilder = new ProxyFactoryBuilder(ImplementationLookup.NONE, instanceProvider);
        TestFactoryWithMissingParameters testFactoryWithMissingParameters = factoryBuilder.createFactory(TestFactoryWithMissingParameters.class);
        One paramOne = new One();
        Two paramTwo = new Two();
        Three paramThree = new Three();
        check(testFactoryWithMissingParameters.newDataObject(), instanceOne, instanceTwo, instanceThree);
        check(testFactoryWithMissingParameters.newDataObject(paramOne), paramOne, instanceTwo, instanceThree);
        check(testFactoryWithMissingParameters.newDataObject(paramOne, paramTwo), paramOne, paramTwo, instanceThree);
        check(testFactoryWithMissingParameters.newDataObject(paramOne, paramThree), paramOne, instanceTwo, paramThree);
    }

    public void testFailsWithPrimitiveReturn() {
        checkFailure(TestFactoryWithPrimitiveReturn.class);
    }

    public void testFailsWithMisorderedParameters() {
        checkFailure(TestFactoryWithMisorderedParameters.class);
    }

    public void testFailsWithExtraParameters() {
        checkFailure(TestFactoryWithExtraParameters.class);
    }

    public void testFailsWithMissingAndExtraParameters() {
        instanceProvider.registerInstance(One.class, new One());
        instanceProvider.registerInstance(Two.class, new Two());
        checkFailure(TestFactoryWithMissingAndExtraParameters.class);
    }

    public void testFailsWithNonProvidedMissingParameters() {
        checkFailure(TestFactoryWithMissingParameters.class);
    }

    private void checkFailure(final Class factoryClass) {
        assertThrows(IllegalArgumentException.class, "Can't find handler for", new Block() {

            public void run() throws Throwable {
                factoryBuilder.createFactory(factoryClass);
            }
        });
    }

    private void check(DataObject object, One paramOne, Two instanceTwo, Three instanceThree) {
        assertSame(paramOne, object.getOne());
        assertSame(instanceTwo, object.getTwo());
        assertSame(instanceThree, object.getThree());
    }
}
