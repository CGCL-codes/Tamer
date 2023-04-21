package net.sf.hibernate4gwt.core.beanlib.merge;

import java.io.Serializable;
import net.sf.beanlib.hibernate.UnEnhancer;
import net.sf.beanlib.hibernate3.Hibernate3JavaBeanReplicator;
import net.sf.beanlib.spi.BeanTransformerSpi;
import net.sf.beanlib.spi.replicator.BeanReplicatorSpi;
import net.sf.hibernate4gwt.annotations.AnnotationsHelper;
import net.sf.hibernate4gwt.core.IPersistenceUtil;
import net.sf.hibernate4gwt.core.beanlib.IClassMapper;
import net.sf.hibernate4gwt.core.store.IProxyStore;
import net.sf.hibernate4gwt.exception.NotHibernateObjectException;
import net.sf.hibernate4gwt.exception.TransientHibernateObjectException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Bean replicator with different from and to classes for merge operation
 * @author bruno.marchesson
 *
 */
public class MergeClassBeanReplicator extends Hibernate3JavaBeanReplicator {

    /**
	 * Log channel
	 */
    private static Log _log = LogFactory.getLog(MergeClassBeanReplicator.class);

    /**
	 * The class mapper (can be null)
	 */
    private IClassMapper _classMapper;

    /**
	 * The persistent util class
	 */
    private IPersistenceUtil _persistenceUtil;

    /**
	 * The current proxy store
	 */
    private IProxyStore _proxyStore;

    public static final Factory factory = new Factory();

    /**
     * Factory for {@link MergeClassBeanReplicator}
     * 
     * @author bruno.marchesson
     */
    public static class Factory implements BeanReplicatorSpi.Factory {

        private Factory() {
        }

        public MergeClassBeanReplicator newBeanReplicatable(BeanTransformerSpi beanTransformer) {
            return new MergeClassBeanReplicator(beanTransformer);
        }
    }

    public static MergeClassBeanReplicator newBeanReplicatable(BeanTransformerSpi beanTransformer) {
        return factory.newBeanReplicatable(beanTransformer);
    }

    protected MergeClassBeanReplicator(BeanTransformerSpi beanTransformer) {
        super(beanTransformer);
    }

    /**
	 * @return the Class Mapper
	 */
    public IClassMapper getClassMapper() {
        return _classMapper;
    }

    /**
	 * @param mapper the classMapper to set
	 */
    public void setClassMapper(IClassMapper mapper) {
        _classMapper = mapper;
    }

    /**
	 * @return the _persistenceUtil
	 */
    public IPersistenceUtil getPersistenceUtil() {
        return _persistenceUtil;
    }

    /**
	 * @param util the persistence Util to set
	 */
    public void setPersistenceUtil(IPersistenceUtil util) {
        _persistenceUtil = util;
    }

    /**
	 * @return the proxy store
	 */
    public IProxyStore getProxyStore() {
        return _proxyStore;
    }

    /**
	 * @param store the proxy Store to set
	 */
    public void setProxyStore(IProxyStore store) {
        _proxyStore = store;
    }

    @Override
    protected Object replicate(Object from) {
        BeanlibThreadLocal.setProxyInformations(null);
        return super.replicate(from);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected <T extends Object> T createToInstance(Object from, java.lang.Class<T> toClass) throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException {
        if (_classMapper != null) {
            Class sourceClass = _classMapper.getSourceClass(UnEnhancer.unenhanceClass(from.getClass()));
            if (sourceClass != null) {
                if (_log.isDebugEnabled()) {
                    _log.debug("Creating mapped class " + sourceClass);
                }
                toClass = sourceClass;
            } else {
                if (_log.isDebugEnabled()) {
                    _log.debug("Creating merged class " + toClass);
                }
            }
        }
        if (toClass.isInterface()) {
            toClass = (Class<T>) from.getClass();
        }
        T result = null;
        if ((AnnotationsHelper.hasServerOnlyOrReadOnlyAnnotations(from.getClass())) || (AnnotationsHelper.hasServerOnlyOrReadOnlyAnnotations(toClass))) {
            try {
                Serializable id = _persistenceUtil.getId(from, toClass);
                result = (T) _persistenceUtil.load(id, toClass);
            } catch (NotHibernateObjectException e) {
                _log.warn("Not an hibernate class (" + toClass + ") : annotated values will not be restored.");
            } catch (TransientHibernateObjectException e) {
                _log.warn("Transient value of class " + toClass + " : annotated values will not be restored.");
            }
        }
        if (result != null) {
            return result;
        } else {
            result = super.createToInstance(from, toClass);
            if ((_classMapper != null) && (_classMapper.getSourceClass(result.getClass()) != null)) {
                return newInstanceAsPrivileged(toClass);
            }
            return result;
        }
    }
}
