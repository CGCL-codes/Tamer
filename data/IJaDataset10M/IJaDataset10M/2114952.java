package org.t2framework.lucy.config.stax;

import org.t2framework.commons.meta.BeanDesc;
import org.t2framework.lucy.Lucy;
import org.t2framework.lucy.aop.config.AspectConfig;
import org.t2framework.lucy.config.stax.LucyConfigTagHandlerRule;
import org.t2framework.lucy.config.stax.StAXParser;
import org.t2framework.lucy.ut.LucyTestCase;

public class StAXParserAspectTest extends LucyTestCase {

    public void testParse_Aspect() throws Exception {
        Lucy lucy = getLucy();
        StAXParser parser = new StAXParser(new LucyConfigTagHandlerRule());
        parser.parse(lucy, "org/t2framework/lucy/config/stax/test4.xml");
        BeanTest4Target test4Target = lucy.get(BeanTest4Target.class);
        assertNotNull(test4Target);
        assertEquals("yone098", test4Target.getStr());
    }

    public void testParse_ComponentsWithAspect() throws Exception {
        Lucy lucy = getLucy();
        StAXParser parser = new StAXParser(new LucyConfigTagHandlerRule());
        parser.parse(lucy, "org/t2framework/lucy/config/stax/componentsWithAspect.xml");
        BeanTest6Target get = lucy.get(BeanTest6Target.class);
        assertNotNull(get);
        BeanDesc<BeanTest6Target> desc = lucy.getBeanDesc(BeanTest6Target.class);
        AspectConfig acd = (AspectConfig) desc.findConfig(AspectConfig.class);
        assertNotNull(acd);
    }
}
