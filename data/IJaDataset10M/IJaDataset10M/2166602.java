package com.avaje.ebean.server.ldap;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Logger;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import com.avaje.ebean.bean.EntityBean;
import com.avaje.ebean.bean.EntityBeanIntercept;
import com.avaje.ebean.event.BeanPersistController;
import com.avaje.ebean.server.deploy.BeanDescriptor;
import com.avaje.ebean.server.deploy.BeanProperty;

public class LdapBeanBuilder<T> {

    private static final Logger logger = Logger.getLogger(LdapBeanBuilder.class.getName());

    private final BeanDescriptor<T> beanDescriptor;

    private final boolean vanillaMode;

    private Set<String> loadedProps;

    public LdapBeanBuilder(BeanDescriptor<T> beanDescriptor, boolean vanillaMode) {
        this.beanDescriptor = beanDescriptor;
        this.vanillaMode = vanillaMode;
    }

    @SuppressWarnings("unchecked")
    public T readAttributes(Attributes attributes) throws NamingException {
        Object bean = beanDescriptor.createBean(vanillaMode);
        NamingEnumeration<? extends Attribute> all = attributes.getAll();
        boolean setLoadedProps = false;
        if (loadedProps == null) {
            setLoadedProps = true;
            loadedProps = new LinkedHashSet<String>();
        }
        while (all.hasMoreElements()) {
            Attribute attr = all.nextElement();
            String attrName = attr.getID();
            BeanProperty prop = beanDescriptor.getBeanPropertyFromDbColumn(attrName);
            if (prop == null) {
                if ("objectclass".equalsIgnoreCase(attrName)) {
                } else {
                    logger.info("... hmm, no property to map to attribute[" + attrName + "] value[" + attr.get() + "]");
                }
            } else {
                prop.setAttributeValue(bean, attr);
                if (setLoadedProps) {
                    loadedProps.add(prop.getName());
                }
            }
        }
        if (bean instanceof EntityBean) {
            EntityBeanIntercept ebi = ((EntityBean) bean)._ebean_getIntercept();
            ebi.setLoadedProps(loadedProps);
            ebi.setLoaded();
        }
        BeanPersistController persistController = beanDescriptor.getPersistController();
        if (persistController != null) {
            persistController.postLoad(bean, loadedProps);
        }
        return (T) bean;
    }
}
