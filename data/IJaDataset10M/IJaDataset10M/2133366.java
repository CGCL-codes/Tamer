package com.jaspersoft.jasperserver.api.metadata.common.service.impl;

import com.jaspersoft.jasperserver.api.common.domain.ExecutionContext;
import com.jaspersoft.jasperserver.api.common.service.impl.ImplementationClassObjectFactoryImpl;
import com.jaspersoft.jasperserver.api.metadata.common.domain.Resource;
import com.jaspersoft.jasperserver.api.metadata.common.service.ResourceFactory;

/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: ResourceFactoryImpl.java 2 2006-04-30 16:19:39Z sgwood $
 */
public class ResourceFactoryImpl extends ImplementationClassObjectFactoryImpl implements ResourceFactory {

    public Resource newResource(ExecutionContext context, Class _class) {
        return (Resource) newObject(_class);
    }
}
