package net.opensesam.dao;

import net.opensesam.entity.ResourceGroup;
import org.hibernate.SessionFactory;

public class ResourceGroupDao extends AbstractDao<ResourceGroup> {

    public ResourceGroupDao(SessionFactory sessionFactory) {
        super(ResourceGroup.class, sessionFactory);
    }
}
