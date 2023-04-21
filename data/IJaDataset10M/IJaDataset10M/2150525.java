package org.t2framework.lucy.register;

import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.config.meta.AutoRegisterConfig;
import org.t2framework.lucy.register.test.BeanTest1Target;
import org.t2framework.lucy.register.test.BeanTest3Target;
import org.t2framework.lucy.register.test.BeanTest4Target;
import org.t2framework.lucy.ut.LucyTestCase;

/**
 * 
 * @author shot
 */
public class AutoRegisterImplTest extends LucyTestCase {

    public void testAutoRegister() throws Exception {
        final Lucy lucy = getLucy();
        AutoRegisterImpl register = new AutoRegisterImpl();
        register.setPackageName("org.t2framework.lucy.register.test");
        register.setClassPattern("BeanTest\\dTarget");
        register.setInstance("singleton");
        register.setIgnorePattern("BeanTest3Target");
        register.setReferenceClass(BeanTest4Target.class);
        register.register(lucy);
        BeanTest1Target t1 = lucy.get(BeanTest1Target.class);
        assertNotNull(t1);
        BeanDesc<BeanTest1Target> bd = lucy.getBeanDesc(BeanTest1Target.class);
        assertNotNull(bd);
        assertTrue(bd.isSingleton());
        assertNotNull(bd.findConfig(AutoRegisterConfig.class));
        BeanTest3Target t3 = lucy.get(BeanTest3Target.class);
        assertNull(t3);
    }
}
