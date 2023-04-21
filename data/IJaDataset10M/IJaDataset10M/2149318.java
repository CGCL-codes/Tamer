package net.zylk.kerozain.portal.service;

import com.liferay.portal.kernel.util.ClassLoaderProxy;

/**
 * @author zylk.net
 */
public class AuditActionServiceClp implements AuditActionService {

    public AuditActionServiceClp(ClassLoaderProxy classLoaderProxy) {
        _classLoaderProxy = classLoaderProxy;
    }

    public ClassLoaderProxy getClassLoaderProxy() {
        return _classLoaderProxy;
    }

    private ClassLoaderProxy _classLoaderProxy;
}
