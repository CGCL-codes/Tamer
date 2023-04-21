package net.zylk.kerozain.portal.service;

import com.liferay.portal.kernel.util.ClassLoaderProxy;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.MethodKey;

/**
 * @author zylk.net
 */
public class CorporationLocalServiceClp implements CorporationLocalService {

    public CorporationLocalServiceClp(ClassLoaderProxy classLoaderProxy) {
        _classLoaderProxy = classLoaderProxy;
        _addCorporationMethodKey0 = new MethodKey(_classLoaderProxy.getClassName(), "addCorporation", net.zylk.kerozain.portal.model.Corporation.class);
        _createCorporationMethodKey1 = new MethodKey(_classLoaderProxy.getClassName(), "createCorporation", long.class);
        _deleteCorporationMethodKey2 = new MethodKey(_classLoaderProxy.getClassName(), "deleteCorporation", long.class);
        _deleteCorporationMethodKey3 = new MethodKey(_classLoaderProxy.getClassName(), "deleteCorporation", net.zylk.kerozain.portal.model.Corporation.class);
        _dynamicQueryMethodKey4 = new MethodKey(_classLoaderProxy.getClassName(), "dynamicQuery", com.liferay.portal.kernel.dao.orm.DynamicQuery.class);
        _dynamicQueryMethodKey5 = new MethodKey(_classLoaderProxy.getClassName(), "dynamicQuery", com.liferay.portal.kernel.dao.orm.DynamicQuery.class, int.class, int.class);
        _dynamicQueryMethodKey6 = new MethodKey(_classLoaderProxy.getClassName(), "dynamicQuery", com.liferay.portal.kernel.dao.orm.DynamicQuery.class, int.class, int.class, com.liferay.portal.kernel.util.OrderByComparator.class);
        _dynamicQueryCountMethodKey7 = new MethodKey(_classLoaderProxy.getClassName(), "dynamicQueryCount", com.liferay.portal.kernel.dao.orm.DynamicQuery.class);
        _fetchCorporationMethodKey8 = new MethodKey(_classLoaderProxy.getClassName(), "fetchCorporation", long.class);
        _getCorporationMethodKey9 = new MethodKey(_classLoaderProxy.getClassName(), "getCorporation", long.class);
        _getPersistedModelMethodKey10 = new MethodKey(_classLoaderProxy.getClassName(), "getPersistedModel", java.io.Serializable.class);
        _getCorporationByUuidAndGroupIdMethodKey11 = new MethodKey(_classLoaderProxy.getClassName(), "getCorporationByUuidAndGroupId", java.lang.String.class, long.class);
        _getCorporationsMethodKey12 = new MethodKey(_classLoaderProxy.getClassName(), "getCorporations", int.class, int.class);
        _getCorporationsCountMethodKey13 = new MethodKey(_classLoaderProxy.getClassName(), "getCorporationsCount");
        _updateCorporationMethodKey14 = new MethodKey(_classLoaderProxy.getClassName(), "updateCorporation", net.zylk.kerozain.portal.model.Corporation.class);
        _updateCorporationMethodKey15 = new MethodKey(_classLoaderProxy.getClassName(), "updateCorporation", net.zylk.kerozain.portal.model.Corporation.class, boolean.class);
        _getBeanIdentifierMethodKey16 = new MethodKey(_classLoaderProxy.getClassName(), "getBeanIdentifier");
        _setBeanIdentifierMethodKey17 = new MethodKey(_classLoaderProxy.getClassName(), "setBeanIdentifier", java.lang.String.class);
    }

    public net.zylk.kerozain.portal.model.Corporation addCorporation(net.zylk.kerozain.portal.model.Corporation corporation) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_addCorporationMethodKey0, ClpSerializer.translateInput(corporation));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public net.zylk.kerozain.portal.model.Corporation createCorporation(long corporationId) {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_createCorporationMethodKey1, corporationId);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public void deleteCorporation(long corporationId) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        MethodHandler methodHandler = new MethodHandler(_deleteCorporationMethodKey2, corporationId);
        try {
            _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.PortalException) {
                throw (com.liferay.portal.kernel.exception.PortalException) t;
            }
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
    }

    public void deleteCorporation(net.zylk.kerozain.portal.model.Corporation corporation) throws com.liferay.portal.kernel.exception.SystemException {
        MethodHandler methodHandler = new MethodHandler(_deleteCorporationMethodKey3, ClpSerializer.translateInput(corporation));
        try {
            _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
    }

    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_dynamicQueryMethodKey4, ClpSerializer.translateInput(dynamicQuery));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (java.util.List) ClpSerializer.translateOutput(returnObj);
    }

    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_dynamicQueryMethodKey5, ClpSerializer.translateInput(dynamicQuery), start, end);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (java.util.List) ClpSerializer.translateOutput(returnObj);
    }

    @SuppressWarnings("rawtypes")
    public java.util.List dynamicQuery(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery, int start, int end, com.liferay.portal.kernel.util.OrderByComparator orderByComparator) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_dynamicQueryMethodKey6, ClpSerializer.translateInput(dynamicQuery), start, end, ClpSerializer.translateInput(orderByComparator));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (java.util.List) ClpSerializer.translateOutput(returnObj);
    }

    public long dynamicQueryCount(com.liferay.portal.kernel.dao.orm.DynamicQuery dynamicQuery) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_dynamicQueryCountMethodKey7, ClpSerializer.translateInput(dynamicQuery));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return ((Long) returnObj).longValue();
    }

    public net.zylk.kerozain.portal.model.Corporation fetchCorporation(long corporationId) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_fetchCorporationMethodKey8, corporationId);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public net.zylk.kerozain.portal.model.Corporation getCorporation(long corporationId) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getCorporationMethodKey9, corporationId);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.PortalException) {
                throw (com.liferay.portal.kernel.exception.PortalException) t;
            }
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public com.liferay.portal.model.PersistedModel getPersistedModel(java.io.Serializable primaryKeyObj) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getPersistedModelMethodKey10, ClpSerializer.translateInput(primaryKeyObj));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.PortalException) {
                throw (com.liferay.portal.kernel.exception.PortalException) t;
            }
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (com.liferay.portal.model.PersistedModel) ClpSerializer.translateOutput(returnObj);
    }

    public net.zylk.kerozain.portal.model.Corporation getCorporationByUuidAndGroupId(java.lang.String uuid, long groupId) throws com.liferay.portal.kernel.exception.PortalException, com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getCorporationByUuidAndGroupIdMethodKey11, ClpSerializer.translateInput(uuid), groupId);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.PortalException) {
                throw (com.liferay.portal.kernel.exception.PortalException) t;
            }
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public java.util.List<net.zylk.kerozain.portal.model.Corporation> getCorporations(int start, int end) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getCorporationsMethodKey12, start, end);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (java.util.List<net.zylk.kerozain.portal.model.Corporation>) ClpSerializer.translateOutput(returnObj);
    }

    public int getCorporationsCount() throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getCorporationsCountMethodKey13);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return ((Integer) returnObj).intValue();
    }

    public net.zylk.kerozain.portal.model.Corporation updateCorporation(net.zylk.kerozain.portal.model.Corporation corporation) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_updateCorporationMethodKey14, ClpSerializer.translateInput(corporation));
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public net.zylk.kerozain.portal.model.Corporation updateCorporation(net.zylk.kerozain.portal.model.Corporation corporation, boolean merge) throws com.liferay.portal.kernel.exception.SystemException {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_updateCorporationMethodKey15, ClpSerializer.translateInput(corporation), merge);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof com.liferay.portal.kernel.exception.SystemException) {
                throw (com.liferay.portal.kernel.exception.SystemException) t;
            }
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (net.zylk.kerozain.portal.model.Corporation) ClpSerializer.translateOutput(returnObj);
    }

    public java.lang.String getBeanIdentifier() {
        Object returnObj = null;
        MethodHandler methodHandler = new MethodHandler(_getBeanIdentifierMethodKey16);
        try {
            returnObj = _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
        return (java.lang.String) ClpSerializer.translateOutput(returnObj);
    }

    public void setBeanIdentifier(java.lang.String beanIdentifier) {
        MethodHandler methodHandler = new MethodHandler(_setBeanIdentifierMethodKey17, ClpSerializer.translateInput(beanIdentifier));
        try {
            _classLoaderProxy.invoke(methodHandler);
        } catch (Throwable t) {
            if (t instanceof RuntimeException) {
                throw (RuntimeException) t;
            } else {
                throw new RuntimeException(t.getClass().getName() + " is not a valid exception");
            }
        }
    }

    public ClassLoaderProxy getClassLoaderProxy() {
        return _classLoaderProxy;
    }

    private ClassLoaderProxy _classLoaderProxy;

    private MethodKey _addCorporationMethodKey0;

    private MethodKey _createCorporationMethodKey1;

    private MethodKey _deleteCorporationMethodKey2;

    private MethodKey _deleteCorporationMethodKey3;

    private MethodKey _dynamicQueryMethodKey4;

    private MethodKey _dynamicQueryMethodKey5;

    private MethodKey _dynamicQueryMethodKey6;

    private MethodKey _dynamicQueryCountMethodKey7;

    private MethodKey _fetchCorporationMethodKey8;

    private MethodKey _getCorporationMethodKey9;

    private MethodKey _getPersistedModelMethodKey10;

    private MethodKey _getCorporationByUuidAndGroupIdMethodKey11;

    private MethodKey _getCorporationsMethodKey12;

    private MethodKey _getCorporationsCountMethodKey13;

    private MethodKey _updateCorporationMethodKey14;

    private MethodKey _updateCorporationMethodKey15;

    private MethodKey _getBeanIdentifierMethodKey16;

    private MethodKey _setBeanIdentifierMethodKey17;
}
