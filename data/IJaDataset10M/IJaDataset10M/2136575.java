package org.avaje.ebean.server.deploy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.avaje.ebean.EbeanServer;
import org.avaje.ebean.InvalidValue;
import org.avaje.ebean.Query;
import org.avaje.ebean.Transaction;
import org.avaje.ebean.bean.ObjectGraphNode;
import org.avaje.ebean.collection.BeanCollection;
import org.avaje.ebean.collection.BeanMap;

/**
 * Helper specifically for dealing with Maps.
 */
public class BeanMapHelp implements BeanCollectionHelp {

    final BeanDescriptor targetDescriptor;

    public BeanMapHelp(BeanDescriptor targetDescriptor) {
        this.targetDescriptor = targetDescriptor;
    }

    @SuppressWarnings("unchecked")
    public void add(BeanCollection<?> collection, Object bean, String mapKey) {
        BeanProperty beanProperty = targetDescriptor.getBeanProperty(mapKey);
        Object keyValue = beanProperty.getValue(bean);
        Map<Object, Object> map = (Map<Object, Object>) collection;
        map.put(keyValue, bean);
    }

    @SuppressWarnings("unchecked")
    public BeanCollection<?> createEmpty() {
        return new BeanMap();
    }

    public BeanCollection<?> createReference(Object parentBean, String serverName, String propertyName, ObjectGraphNode profilePoint) {
        return new BeanMap<Object, Object>(serverName, parentBean, propertyName, profilePoint);
    }

    public ArrayList<InvalidValue> validate(BeanDescriptor target, Object manyValue) {
        ArrayList<InvalidValue> errs = null;
        Map<?, ?> m = (Map<?, ?>) manyValue;
        Iterator<?> it = m.values().iterator();
        while (it.hasNext()) {
            Object detailBean = (Object) it.next();
            InvalidValue invalid = target.validate(true, detailBean);
            if (invalid != null) {
                if (errs == null) {
                    errs = new ArrayList<InvalidValue>();
                }
                errs.add(invalid);
            }
        }
        return errs;
    }

    public void refresh(EbeanServer server, Query<?> query, Transaction t, BeanPropertyAssocMany many, Object parentBean) {
        BeanMap<?, ?> newBeanMap = (BeanMap<?, ?>) server.findMap(query, t);
        Map<?, ?> current = (Map<?, ?>) many.getValue(parentBean);
        if (many.isManyToMany()) {
            newBeanMap.setModifyListening(true);
        }
        if (current == null) {
            many.setValue(parentBean, newBeanMap);
        } else if (current instanceof BeanMap) {
            BeanMap<?, ?> currentBeanMap = (BeanMap<?, ?>) current;
            currentBeanMap.setActualMap(newBeanMap.getActualMap());
            if (many.isManyToMany()) {
                currentBeanMap.setModifyListening(true);
            }
        } else {
            many.setValue(parentBean, newBeanMap);
        }
    }
}
