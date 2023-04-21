package org.bing.engine.console.service;

import java.util.List;
import org.bing.engine.core.domain.Container;
import org.bing.engine.core.domain.ContainerSetting;

public class ContainerSettingManager extends AbstractHibernateManager {

    @SuppressWarnings("unchecked")
    public List<ContainerSetting> findByContainer(Container container) {
        return template.find("select t from ContainerSetting t where t.container=?", container);
    }

    @SuppressWarnings("unchecked")
    public List<ContainerSetting> findAll() {
        return template.find("select t from ContainerSetting t");
    }

    public void save(ContainerSetting setting) {
        template.saveOrUpdate(setting);
    }

    public void delete(ContainerSetting setting) {
        template.delete(setting);
    }
}
