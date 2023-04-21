package org.archive.hcc.util.jmx;

import javax.management.MBeanException;
import javax.management.ReflectionException;
import javax.management.openmbean.OpenMBeanOperationInfo;

public interface MBeanOperation {

    public boolean matches(String name, String[] signature);

    public MBeanInvocation createInvocation(Object[] parameters, String[] signature) throws MBeanException, ReflectionException;

    public boolean validate(String operationName, Object[] params, String[] signature);

    public OpenMBeanOperationInfo getInfo();
}
