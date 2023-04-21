package org.mbari.aosn.realm;

import javax.management.MBeanException;
import javax.management.RuntimeOperationsException;
import org.apache.commons.modeler.BaseModelMBean;
import org.apache.commons.modeler.ManagedBean;

/**
 * @author godin
 *
 */
public class JsonUserDatabaseMBean extends BaseModelMBean {

    public JsonUserDatabaseMBean() throws MBeanException, RuntimeOperationsException {
        super();
    }

    /**
     * The <code>ManagedBean</code> information describing this MBean.
     */
    protected ManagedBean managed = registry.findManagedBean("JsonUserDatabase");
}
