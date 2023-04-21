package com.jtri.facade;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import org.apache.commons.beanutils.PropertyUtils;
import org.hibernate.Query;
import org.hibernate.Session;
import com.jtri.base.PageStructure;
import com.jtri.base.Row;

public class NamedQueryRules extends DefaultRules {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1932651083612354860L;

    protected void associateWithParent(HibernateFacade facade, Session sess, PageStructure pg, Object parent, Object dto, Class persistClass) throws Exception {
    }

    protected Collection loadSubCollection(Session sess, PageStructure parentStruc, PageStructure detStruc, Object bean) throws Exception {
        if (detStruc.getNamedQuery() != null) {
            Query q = sess.getNamedQuery(detStruc.getNamedQuery());
            if (detStruc.getMasterFields() == null) detStruc.setMasterFields(q.getNamedParameters());
            String[] params = q.getNamedParameters();
            for (int i = 0; i < detStruc.getMasterFields().length; i++) {
                Object value = null;
                try {
                    value = PropertyUtils.getProperty(bean, detStruc.getMasterFields()[i]);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("named query " + detStruc.getNamedQuery() + " execution error: parameter " + params[i] + " could not be set with property " + detStruc.getMasterFields()[i] + " of object's type '" + bean.getClass() + "'");
                }
                q.setParameter(params[i], value);
            }
            return q.list();
        } else {
            return super.loadSubCollection(sess, parentStruc, detStruc, bean);
        }
    }

    protected void removeFromParent(Session sess, PageStructure pg, Object parent, Object dto, Class persistClass) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    }

    protected void resolveRelations(Session sess, PageStructure pg, Object parent, Row row, Map mvo) throws Exception {
        if (pg.getNamedQuery() != null && parent != null && pg.getDetailFields() != null) {
            for (int i = 0; i < pg.getDetailFields().length; i++) {
                for (Iterator it2 = mvo.keySet().iterator(); it2.hasNext(); ) {
                    String name = (String) it2.next();
                    if (name.startsWith(pg.getDetailFields()[i] + ".")) {
                        it2.remove();
                    }
                }
                if (pg.getMasterFields() == null) {
                    pg.setMasterFields(sess.getNamedQuery(pg.getNamedQuery()).getNamedParameters());
                }
                Object parentValue = PropertyUtils.getProperty(parent, pg.getMasterFields()[i]);
                mvo.put(pg.getDetailFields()[i], parentValue);
            }
        } else super.resolveRelations(sess, pg, parent, row, mvo);
    }
}
