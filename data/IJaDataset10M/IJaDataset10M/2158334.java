package com.em.validation.client.core.reflector;

import javax.validation.Validation;
import javax.validation.Validator;
import org.junit.Assert;
import org.junit.Test;
import com.em.validation.client.model.reflector.JustInterface01;
import com.em.validation.client.model.reflector.JustInterface02;
import com.em.validation.client.model.reflector.OnlyInterfaces;
import com.em.validation.client.model.reflector.ReflectiveBase;
import com.em.validation.client.model.reflector.ReflectiveExposed;
import com.em.validation.client.model.reflector.ReflectiveIntermediary;
import com.em.validation.client.model.reflector.ReflectiveMiddle;
import com.em.validation.client.model.reflector.ReflectiveSkip;
import com.em.validation.client.model.reflector.ReflectiveTop;
import com.em.validation.client.model.tests.GwtValidationBaseTestCase;
import com.em.validation.client.reflector.IReflector;
import com.em.validation.client.reflector.IReflectorFactory;

public class ReflectorTest extends GwtValidationBaseTestCase {

    private static class ReflectiveUneeded extends ReflectiveExposed {
    }

    @Test
    public void testDeepReflectionChain() {
        IReflectorFactory factory = this.getReflectorFactory();
        IReflector rExposed = factory.getReflector(ReflectiveExposed.class);
        IReflector rItermediary = rExposed.getParentReflector();
        IReflector rSkip = rItermediary.getParentReflector();
        IReflector rTop = rSkip.getParentReflector();
        IReflector rMid = rTop.getParentReflector();
        IReflector rBase = rMid.getParentReflector();
        Assert.assertNotNull(rExposed);
        Assert.assertNotNull(rItermediary);
        Assert.assertNotNull(rSkip);
        Assert.assertNotNull(rTop);
        Assert.assertNotNull(rMid);
        Assert.assertNotNull(rBase);
        Assert.assertTrue(rBase.getParentReflector() == null || rBase.getParentReflector().getTargetClass() == null);
        Assert.assertEquals(ReflectiveExposed.class, rExposed.getTargetClass());
        Assert.assertEquals(ReflectiveIntermediary.class, rItermediary.getTargetClass());
        Assert.assertEquals(ReflectiveSkip.class, rSkip.getTargetClass());
        Assert.assertEquals(ReflectiveTop.class, rTop.getTargetClass());
        Assert.assertEquals(ReflectiveMiddle.class, rMid.getTargetClass());
        Assert.assertEquals(ReflectiveBase.class, rBase.getTargetClass());
        ReflectiveExposed refExp = new ReflectiveExposed();
        refExp.setReflectiveBaseString("base");
        refExp.setReflectiveIntermediaryLayerString("inter");
        refExp.setReflectiveMiddleString("mid");
        Assert.assertEquals("base", rExposed.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("inter", rExposed.getValue("reflectiveIntermediaryLayerString", refExp));
        Assert.assertEquals("mid", rExposed.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals("base", rItermediary.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("inter", rItermediary.getValue("reflectiveIntermediaryLayerString", refExp));
        Assert.assertEquals("mid", rItermediary.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals("base", rSkip.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("mid", rSkip.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals("base", rTop.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("mid", rTop.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals(null, rMid.getValue("reflectiveIntermediaryLayerString", refExp));
        Assert.assertEquals("base", rMid.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("mid", rMid.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals(null, rBase.getValue("reflectiveIntermediaryLayerString", refExp));
        Assert.assertEquals(null, rBase.getValue("reflectiveMiddleString", refExp));
        Assert.assertEquals("base", rBase.getValue("reflectiveBaseString", refExp));
        Assert.assertEquals("reflectiveSide01AtIntermediaryLayerString", rExposed.getValue("reflectiveSide01AtIntermediaryLayerString", refExp));
        Assert.assertEquals("reflectiveSide02AtIntermediaryLayerString", rExposed.getValue("reflectiveSide02AtIntermediaryLayerString", refExp));
        Assert.assertEquals("reflectiveSideAtTopTierString", rExposed.getValue("reflectiveSideAtTopTierString", refExp));
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        Assert.assertEquals(6, validator.validate(new ReflectiveExposed()).size());
        Assert.assertEquals(6, validator.validate(new ReflectiveIntermediary()).size());
        Assert.assertEquals(3, validator.validate(new ReflectiveSkip()).size());
        Assert.assertEquals(3, validator.validate(new ReflectiveTop()).size());
        Assert.assertEquals(2, validator.validate(new ReflectiveMiddle()).size());
        Assert.assertEquals(1, validator.validate(new ReflectiveBase()).size());
        ReflectiveExposed rexAnon = new ReflectiveExposed() {
        };
        Assert.assertEquals(6, validator.validate(rexAnon).size());
        IReflector rAnon = factory.getReflector(rexAnon.getClass());
        rexAnon.setReflectiveBaseString("base");
        rexAnon.setReflectiveIntermediaryLayerString("inter");
        rexAnon.setReflectiveMiddleString("mid");
        Assert.assertEquals("base", rExposed.getValue("reflectiveBaseString", rexAnon));
        Assert.assertEquals("inter", rExposed.getValue("reflectiveIntermediaryLayerString", rexAnon));
        Assert.assertEquals("mid", rExposed.getValue("reflectiveMiddleString", rexAnon));
        Assert.assertEquals("reflectiveSide01AtIntermediaryLayerString", rAnon.getValue("reflectiveSide01AtIntermediaryLayerString", rexAnon));
        Assert.assertEquals("reflectiveSide02AtIntermediaryLayerString", rAnon.getValue("reflectiveSide02AtIntermediaryLayerString", rexAnon));
        Assert.assertEquals("reflectiveSideAtTopTierString", rAnon.getValue("reflectiveSideAtTopTierString", rexAnon));
        class ReflectiveLocal extends ReflectiveUneeded {
        }
        ;
        ReflectiveLocal local = new ReflectiveLocal();
        IReflector rLocal = factory.getReflector(local.getClass());
        local.setReflectiveBaseString("base");
        local.setReflectiveIntermediaryLayerString("inter");
        local.setReflectiveMiddleString("mid");
        Assert.assertEquals("base", rExposed.getValue("reflectiveBaseString", local));
        Assert.assertEquals("inter", rExposed.getValue("reflectiveIntermediaryLayerString", local));
        Assert.assertEquals("mid", rExposed.getValue("reflectiveMiddleString", local));
        Assert.assertEquals("reflectiveSide01AtIntermediaryLayerString", rLocal.getValue("reflectiveSide01AtIntermediaryLayerString", local));
        Assert.assertEquals("reflectiveSide02AtIntermediaryLayerString", rLocal.getValue("reflectiveSide02AtIntermediaryLayerString", local));
        Assert.assertEquals("reflectiveSideAtTopTierString", rLocal.getValue("reflectiveSideAtTopTierString", local));
        Assert.assertEquals(6, validator.validate(new ReflectiveLocal()).size());
    }

    @Test
    public void testAnonymousInterfaceImplementation() {
        IReflectorFactory factory = this.getReflectorFactory();
        JustInterface01 interface01_01 = new JustInterface01() {

            @Override
            public String getInterface01String() {
                return "interface01String";
            }

            @Override
            public String getJustBaseInterface() {
                return "baseInterfaceString";
            }
        };
        IReflector reflector = factory.getReflector(interface01_01.getClass());
        Assert.assertEquals("interface01String", reflector.getValue("interface01String", interface01_01));
    }

    private static class OnlyInterfacesRedirect extends OnlyInterfaces {
    }

    @Test
    public void testClassWithNoConstraintsButConstrainedInterfaces() {
        IReflectorFactory factory = this.getReflectorFactory();
        JustInterface01 iface01 = new OnlyInterfaces();
        JustInterface02 iface02 = new OnlyInterfaces();
        OnlyInterfaces iface03 = new OnlyInterfaces() {
        };
        OnlyInterfacesRedirect iface04 = new OnlyInterfacesRedirect();
        IReflector reflector01 = factory.getReflector(iface01.getClass());
        IReflector reflector02 = factory.getReflector(iface02.getClass());
        IReflector reflector03 = factory.getReflector(iface03.getClass());
        IReflector reflector04 = factory.getReflector(iface04.getClass());
        Assert.assertEquals("interface01String", reflector01.getValue("interface01String", iface01));
        Assert.assertEquals("interface02String", reflector01.getValue("interface02String", iface01));
        Assert.assertEquals("interface01String", reflector02.getValue("interface01String", iface02));
        Assert.assertEquals("interface02String", reflector02.getValue("interface02String", iface02));
        Assert.assertEquals("interface01String", reflector03.getValue("interface01String", iface03));
        Assert.assertEquals("interface02String", reflector03.getValue("interface02String", iface03));
        Assert.assertEquals("interface01String", reflector04.getValue("interface01String", iface04));
        Assert.assertEquals("interface02String", reflector04.getValue("interface02String", iface04));
    }
}
