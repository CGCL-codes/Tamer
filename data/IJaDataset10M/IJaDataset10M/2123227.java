package net.ep.db4o.javassist;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import java.util.regex.Pattern;
import net.ep.db4o.activator.TransparentActivation;
import net.ep.db4o.javassist.testclasses.NoConstructor;
import net.ep.db4o.javassist.testclasses.NoPublicConstructor;
import net.ep.db4o.javassist.testclasses.SensorPanelCTA;
import org.testng.annotations.Test;
import com.db4o.internal.Config4Impl;
import com.db4o.internal.Platform4;
import com.db4o.internal.ReflectorConfigurationImpl;
import com.db4o.reflect.ReflectClass;
import com.db4o.reflect.Reflector;
import com.db4o.reflect.generic.GenericReflector;
import com.db4o.ta.Activatable;

public class JVSTReflectorTest {

    @Test
    public void testJVSTReflector() {
        testReflector(new JVSTReflector((ClassLoader) null));
    }

    private void testReflector(Reflector del) {
        Reflector refl = new GenericReflector(null, del);
        refl.configuration(new ReflectorConfigurationImpl(new Config4Impl()));
        ReflectClass cl = refl.forName(Activatable.class.getName());
        assertNotNull(cl);
        assertEquals(cl.getName(), Activatable.class.getName());
        cl = refl.forName(SensorPanelCTA.class.getName());
        assertEquals(Platform4.jdk().getClass().getName(), "com.db4o.internal.JDK_5");
        assertNotNull(cl);
        assertEquals(cl.getName(), SensorPanelCTA.class.getName());
        assertTrue(cl.getDelegate() instanceof JVSTClass, "cl is " + cl.getClass().getName());
        Object instance = cl.newInstance();
        assertNotNull(instance);
        Pattern pattern = Pattern.compile(SensorPanelCTA.class.getName() + "_TA\\$\\w*+");
        assertTrue(pattern.matcher(instance.getClass().getName()).matches());
        assertTrue(TransparentActivation.class.isAssignableFrom(instance.getClass()));
        assertTrue(cl.getDelegate() instanceof JVSTClass);
    }

    @Test
    public void testPublicContructors() {
        assertTrue(JVSTReflector.hasPublicConstructor(SensorPanelCTA.class));
        assertFalse(JVSTReflector.hasPublicConstructor(NoPublicConstructor.class));
        assertTrue(JVSTReflector.hasPublicConstructor(NoConstructor.class));
    }
}
