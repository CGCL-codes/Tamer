package org.xactor.test.tm.mbean;

import org.jboss.system.ServiceMBean;
import org.xactor.test.tm.resource.Operation;

/**
 * @author adrian@jboss.org
 * @version $Revision: 37406 $
 */
public interface TMTestMBean extends ServiceMBean {

    void testOperations(String test, Operation[] ops) throws Exception;
}
