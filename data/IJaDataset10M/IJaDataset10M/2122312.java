package com.vce.util;

import java.io.Serializable;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 *
 * @author lruiz
 */
public class ObjectLookup {

    private HibernateTemplate hibernateTemplate;

    /** Creates a new instance of ObjectLookup */
    public ObjectLookup() {
    }

    public void setHibernateTemplate(HibernateTemplate template) {
        this.hibernateTemplate = template;
    }

    public Object find(Object id, Class clazz) {
        return hibernateTemplate.get(clazz, (Serializable) id);
    }
}
