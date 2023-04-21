package org.vosao.velocity.impl;

import java.util.HashMap;
import java.util.Map;
import org.vosao.business.Business;
import org.vosao.dao.Dao;
import org.vosao.entity.PluginEntity;
import org.vosao.global.SystemService;
import org.vosao.velocity.FormVelocityService;
import org.vosao.velocity.VelocityPluginService;

/**
 * 
 * @author Alexander Oleynik
 *
 */
public class VelocityPluginServiceImpl implements VelocityPluginService {

    private FormVelocityService form;

    private Business business;

    public VelocityPluginServiceImpl(Business aBusiness) {
        business = aBusiness;
        form = new FormVelocityServiceImpl(getBusiness());
    }

    @Override
    public Map<String, Object> getPlugins() {
        Map<String, Object> services = new HashMap<String, Object>();
        services.put("form", form);
        for (PluginEntity plugin : getDao().getPluginDao().selectEnabled()) {
            try {
                Object velocityPlugin = getBusiness().getPluginBusiness().getVelocityPlugin(plugin);
                if (velocityPlugin != null) {
                    services.put(plugin.getName(), velocityPlugin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return services;
    }

    public Business getBusiness() {
        return business;
    }

    public Dao getDao() {
        return getBusiness().getDao();
    }

    public SystemService getSystemService() {
        return getBusiness().getSystemService();
    }
}
