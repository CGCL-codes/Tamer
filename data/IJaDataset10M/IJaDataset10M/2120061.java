package com.sitechasia.webx.core.cache;

import java.io.Serializable;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import com.sitechasia.webx.core.cache.synchronize.DomainObjectEvent;
import com.sitechasia.webx.core.cache.synchronize.Synchronizer;

/**
 * Hibernate拦截器，拦截Hibernate的事件并通知Cache层进行相应的处理。<br>
 * 
 * @author MarkDong
 * @version 1.0   
 * @since 1.5  
 */
public class CacheInterceptor extends EmptyInterceptor {

    private static final long serialVersionUID = -7055863190653997255L;

    ICacheProvider webxCacheProvider = null;

    Synchronizer webxSynchronizer = null;

    /**
	 * 通过Hibernate删除实例时会触发此方法
	 */
    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (webxCacheProvider == null) return;
        Class<?> entityClass = entity.getClass();
        webxCacheProvider.onChange(entityClass, entity);
        sendEvent(entity, state, propertyNames);
    }

    /**
	 * 通过Hibernate更新实例时会触发此方法
	 */
    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        if (webxCacheProvider == null) return false;
        if (webxCacheProvider.isSimple()) {
            sendEvent(entity, currentState, propertyNames);
        }
        Class<?> entityClass = entity.getClass();
        if (previousState == null) {
            webxCacheProvider.onChange(entityClass);
        } else {
            for (int i = 0; i < previousState.length; i++) {
                String propertyName = propertyNames[i];
                if (currentState[i] == null && previousState[i] == null) continue;
                if (currentState[i] == null || previousState[i] == null) {
                    webxCacheProvider.onChange(entityClass, propertyName, currentState[i]);
                    webxCacheProvider.onChange(entityClass, propertyName, previousState[i]);
                    sendEvent(entity, propertyName, currentState[i]);
                    sendEvent(entity, propertyName, previousState[i]);
                }
                if (!currentState[i].equals(previousState[i])) {
                    webxCacheProvider.onChange(entityClass, propertyName, currentState[i]);
                    webxCacheProvider.onChange(entityClass, propertyName, previousState[i]);
                    sendEvent(entity, propertyName, currentState[i]);
                    sendEvent(entity, propertyName, previousState[i]);
                }
            }
        }
        return false;
    }

    /**
	 * 通过Hibernate插入新记录时会触发此方法
	 */
    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (webxCacheProvider == null) return false;
        Class<?> entityClass = entity.getClass();
        webxCacheProvider.onChange(entityClass, entity);
        sendEvent(entity, state, propertyNames);
        return false;
    }

    public void setWebxCacheProvider(ICacheProvider webxCacheProvider) {
        this.webxCacheProvider = webxCacheProvider;
    }

    public void setWebxSynchronizer(Synchronizer synchronizer) {
        this.webxSynchronizer = synchronizer;
    }

    /**
	 * 发送一组消息通知各服务器实体的一组属性值发生变化 
	 */
    private void sendEvent(Object entity, Object[] states, String[] propertyNames) {
        if (webxCacheProvider == null || webxSynchronizer == null) return;
        if (webxCacheProvider.isSimple()) {
            DomainObjectEvent event = new DomainObjectEvent(entity.getClass());
            webxSynchronizer.send(event);
        } else {
            for (int i = 0; i < states.length; i++) {
                if (states[i] == null || PrimitiveTypes.isPrimitiveType(states[i])) {
                    DomainObjectEvent event = new DomainObjectEvent(entity.getClass(), propertyNames[i], states[i]);
                    webxSynchronizer.send(event);
                }
            }
        }
    }

    /**
	 * 发送单一消息通知实体的某个属性值发生变化 
	 */
    private void sendEvent(Object entity, String property, Object value) {
        if (webxCacheProvider == null || webxSynchronizer == null) return;
        if (webxCacheProvider.isSimple()) return;
        if (value == null || PrimitiveTypes.isPrimitiveType(value)) {
            DomainObjectEvent event = new DomainObjectEvent(entity.getClass(), property, value);
            webxSynchronizer.send(event);
        }
    }
}
