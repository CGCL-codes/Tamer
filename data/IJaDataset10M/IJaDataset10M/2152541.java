package cn.vlabs.duckling.vwb.services.config;

import cn.vlabs.duckling.vwb.services.TestService;

/**
 * Introduction Here.
 * @date Feb 3, 2010
 * @author Yong Ke(keyong@cnic.cn)
 */
public class ConfigTest extends TestService {

    public void testGet() {
        Config cfg = (Config) manager.getFactory().getBean("configService");
        assertEquals(cfg.get("i18n.bundle_name", ""), "CoreResources");
        assertEquals(cfg.getInt("c3p0.initialPoolSize", -1), 10);
    }

    public void testReplace() {
        Config cfg = (Config) manager.getFactory().getBean("configService");
        assertEquals(cfg.get("duckling.umt", ""), cfg.get("duckling.umt.site", "") + "/votree/VOTreeServlet");
    }

    public void testSet() {
        Config cfg = (Config) manager.getFactory().getBean("configService");
        cfg.set("i18n.bundle_name", "CoreResources");
        assertEquals("CoreResources", cfg.get("i18n.bundle_name"));
    }
}
