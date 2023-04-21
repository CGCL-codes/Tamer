package br.com.linkcom.neo.util;

import org.hibernate.EntityMode;
import org.springframework.orm.hibernate3.HibernateTemplate;

public class HibernateUtils {

    public Object getId(HibernateTemplate hibernateTemplate, Object bean) {
        Class<? extends Object> class1 = bean.getClass();
        while (class1.getName().contains("$$")) {
            class1 = class1.getSuperclass();
        }
        return hibernateTemplate.getSessionFactory().getClassMetadata(class1).getIdentifier(bean, EntityMode.POJO);
    }
}
