package com.avaje.ebean.server.deploy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.InvalidValue;
import com.avaje.ebean.Query;
import com.avaje.ebean.Transaction;
import com.avaje.ebean.bean.BeanCollection;
import com.avaje.ebean.bean.LazyLoadEbeanServer;
import com.avaje.ebean.bean.ObjectGraphNode;
import com.avaje.ebean.common.BeanSet;

/**
 * Helper specifically for dealing with Sets.
 */
public final class BeanSetHelp<T> implements BeanCollectionHelp<T> {

    private final BeanPropertyAssocMany<T> many;

    private final BeanDescriptor<T> targetDescriptor;

    private LazyLoadEbeanServer ebeanServer;

    /**
	 * When attached to a specific many property.
	 */
    public BeanSetHelp(BeanPropertyAssocMany<T> many) {
        this.many = many;
        this.targetDescriptor = many.getTargetDescriptor();
    }

    /**
	 * For a query that returns a set.
	 */
    public BeanSetHelp() {
        this.many = null;
        this.targetDescriptor = null;
    }

    public void setEbeanServer(LazyLoadEbeanServer ebeanServer) {
        this.ebeanServer = ebeanServer;
    }

    public void add(BeanCollection<?> collection, Object bean) {
        collection.internalAdd(bean);
    }

    public BeanCollection<T> createEmpty() {
        return new BeanSet<T>();
    }

    public BeanCollection<T> createReference(Object parentBean, String serverName, String propertyName, ObjectGraphNode profilePoint) {
        return new BeanSet<T>(ebeanServer, parentBean, propertyName, profilePoint);
    }

    public ArrayList<InvalidValue> validate(Object manyValue) {
        ArrayList<InvalidValue> errs = null;
        Set<?> set = (Set<?>) manyValue;
        Iterator<?> i = set.iterator();
        while (i.hasNext()) {
            Object detailBean = i.next();
            InvalidValue invalid = targetDescriptor.validate(true, detailBean);
            if (invalid != null) {
                if (errs == null) {
                    errs = new ArrayList<InvalidValue>();
                }
                errs.add(invalid);
            }
        }
        return errs;
    }

    public void refresh(EbeanServer server, Query<?> query, Transaction t, Object parentBean) {
        BeanSet<?> newBeanSet = (BeanSet<?>) server.findSet(query, t);
        Set<?> current = (Set<?>) many.getValue(parentBean);
        if (many.isManyToMany()) {
            newBeanSet.setModifyListening(true);
        }
        if (current == null) {
            many.setValue(parentBean, newBeanSet);
        } else if (current instanceof BeanSet<?>) {
            BeanSet<?> currentBeanSet = (BeanSet<?>) current;
            currentBeanSet.setActualSet(newBeanSet.getActualSet());
            if (many.isManyToMany()) {
                currentBeanSet.setModifyListening(true);
            }
        } else {
            many.setValue(parentBean, newBeanSet);
        }
    }
}
