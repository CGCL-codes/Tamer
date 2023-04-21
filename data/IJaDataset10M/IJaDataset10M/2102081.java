package org.xfeep.asura.core;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Map;
import org.xfeep.asura.core.event.ServiceEvent;
import org.xfeep.asura.core.logger.CoreLoggerManager;

public class ComponentInstance {

    protected ComponentInstanceStatus status = ComponentInstanceStatus.INIT;

    protected Component component;

    protected Object service;

    protected ComponentInstanceProperties properties;

    protected ComponentContext context;

    protected DynamicReference[] dynamicReferences;

    protected byte[] instanceLock = new byte[0];

    public DynamicReference[] getDynamicReferences() {
        return dynamicReferences;
    }

    public void setDynamicReferences(DynamicReference[] dynamicReferences) {
        this.dynamicReferences = dynamicReferences;
    }

    public ComponentInstance() {
    }

    public ComponentInstance(String configId, Component component) {
        super();
        this.component = component;
        this.context = new ComponentContext(component);
        ComponentInstanceProperties componentInstanceProperties = new ComponentInstanceProperties();
        componentInstanceProperties.initComponentProperties(configId, component);
        this.setProperties(componentInstanceProperties);
        ReferenceDefinition[] globalReferenceDefinitions = component.definition.references;
        int dynamicRefPos = component.definition.dynamicReferencePosition;
        if (component.definition.dynamicReferencePosition != globalReferenceDefinitions.length) {
            dynamicReferences = new DynamicReference[globalReferenceDefinitions.length - dynamicRefPos];
            for (int i = 0; i < dynamicReferences.length; i++) {
                ReferenceDefinition rd = globalReferenceDefinitions[i + dynamicRefPos];
                DynamicReference dynamicReference = null;
                if (rd.getMultiplicityType().isUnary()) {
                    dynamicReference = new DynamicOReference(rd, this);
                } else {
                    dynamicReference = new DynamicNReference(rd, this);
                }
                dynamicReference.setOpenOrder(i);
                dynamicReferences[i] = dynamicReference;
            }
        }
    }

    public void addProperty(String key, Object value) {
        properties.put(key, value);
    }

    public void removeProperty(String key) {
        properties.remove(key);
    }

    public final Object getCurrentService() {
        return service;
    }

    public Object getService() {
        return service;
    }

    public Object getService(Reference reference) {
        return service;
    }

    public void ungetService(Reference reference) {
    }

    public void setService(Object service) {
        this.service = service;
    }

    public Component getComponent() {
        return component;
    }

    public Class getComponentClass() {
        return component.definition.implement;
    }

    public ComponentInstanceStatus getStatus() {
        return status;
    }

    public void setStatus(ComponentInstanceStatus status) {
        this.status = status;
    }

    void log(String message, Throwable e) {
        if (e instanceof InvocationTargetException) {
            InvocationTargetException ie = (InvocationTargetException) e;
            e = ie.getTargetException();
        }
        CoreLoggerManager.getDefaultLogger().error(message, e);
    }

    public boolean enable() {
        synchronized (instanceLock) {
            if (dynamicReferences != null) {
                dynamicReferences[0].openContract();
            }
            if (status == ComponentInstanceStatus.DESTROYED) {
                status = ComponentInstanceStatus.WAIT_FOR_CHANCE;
            }
            if (status != ComponentInstanceStatus.ACTIVE) {
                return tryGetChance();
            }
            return false;
        }
    }

    public void disable() {
        synchronized (instanceLock) {
            tryLostChance();
            if (dynamicReferences != null) {
                dynamicReferences[0].closeContract();
            }
            status = ComponentInstanceStatus.DESTROYED;
        }
    }

    public boolean tryGetChance() {
        synchronized (instanceLock) {
            if (service != null) {
                return false;
            }
            if (dynamicReferences != null) {
                for (DynamicReference r : dynamicReferences) {
                    if (!r.isSatisfied()) {
                        return false;
                    }
                }
            }
            if (!init()) {
                destroy();
                tryLostChance();
                return false;
            }
            if (!activate()) {
                deactivate();
                destroy();
                tryLostChance();
                return false;
            }
            return true;
        }
    }

    public void tryLostChance() {
        synchronized (instanceLock) {
            if (service != null) {
                deactivate();
                destroy();
                service = null;
            }
        }
    }

    public boolean init() {
        synchronized (instanceLock) {
            ComponentDefinition definition = component.definition;
            try {
                service = definition.implement.newInstance();
                try {
                    if (definition.init != null) {
                        definition.init.setAccessible(true);
                        definition.init.invoke(service);
                    }
                    return true;
                } catch (Throwable e) {
                    log("can not invoke initialization method " + component.definition.implement.getName() + "." + definition.init.getName() + "(...)", e);
                }
            } catch (Throwable e) {
                log("can not create instance of " + component.definition.implement.getName(), e);
            }
            return false;
        }
    }

    public boolean bindReferences() {
        if (dynamicReferences == null) {
            return true;
        }
        int i = 0;
        for (i = 0; i < dynamicReferences.length; i++) {
            DynamicReference r = dynamicReferences[i];
            if (!r.bind() && r.getDefinition().getMultiplicityType().isRequired()) {
                break;
            }
        }
        if (i < dynamicReferences.length) {
            while (i > -1) {
                dynamicReferences[i].unbind();
                i--;
            }
            return false;
        }
        return true;
    }

    public boolean unbindReferences() {
        if (dynamicReferences == null) {
            return true;
        }
        boolean rt = true;
        for (int i = 0; i < dynamicReferences.length; i++) {
            if (!dynamicReferences[i].unbind()) {
                rt = false;
            }
        }
        return rt;
    }

    public boolean activate() {
        synchronized (instanceLock) {
            if (status == ComponentInstanceStatus.ACTIVE) {
                return false;
            }
            try {
                if (component.bindReferences(this) && this.bindReferences()) {
                    ComponentDefinition definition = component.definition;
                    if (definition.activate != null) {
                        definition.activate.setAccessible(true);
                        if (definition.activate.getParameterTypes().length > 0) {
                            definition.activate.invoke(service, context);
                        } else {
                            definition.activate.invoke(service);
                        }
                    }
                    this.status = ComponentInstanceStatus.ACTIVE;
                    for (Class serviceClass : component.definition.interfaces) {
                        component.serviceSpace.sendEvent(new ServiceEvent(this, serviceClass, ServiceEvent.ACTIVE));
                    }
                    return true;
                }
            } catch (Throwable e) {
                log("can not activate instance of " + component.definition.implement.getName(), e);
            }
            return false;
        }
    }

    public boolean deactivate() {
        synchronized (instanceLock) {
            if (status != ComponentInstanceStatus.ACTIVE) {
                return false;
            }
            try {
                status = ComponentInstanceStatus.ACTIVE_TO_INACTIVE;
                for (Class serviceClass : component.definition.interfaces) {
                    component.serviceSpace.sendEvent(new ServiceEvent(this, serviceClass, ServiceEvent.INACTIVE));
                }
                ComponentDefinition definition = component.definition;
                if (definition.deactivate != null) {
                    definition.deactivate.setAccessible(true);
                    definition.deactivate.invoke(service);
                }
                this.unbindReferences();
                component.unbindReferences(this);
                this.status = ComponentInstanceStatus.INACTIVE;
                return true;
            } catch (Throwable e) {
                log("can not deactivate instance of " + component.definition.implement.getName(), e);
            }
            return false;
        }
    }

    public boolean updateConfig(Object config) {
        throw new UnsupportedOperationException("updateConfig");
    }

    public boolean isActive() {
        synchronized (instanceLock) {
            return status == ComponentInstanceStatus.ACTIVE;
        }
    }

    public boolean destroy() {
        synchronized (instanceLock) {
            if (status == ComponentInstanceStatus.DESTROYED) {
                return false;
            }
            try {
                ComponentDefinition definition = component.definition;
                if (definition.destroy != null) {
                    definition.destroy.setAccessible(true);
                    definition.destroy.invoke(service);
                }
                this.status = ComponentInstanceStatus.DESTROYED;
                return true;
            } catch (Throwable e) {
                log("can not destory instance of " + component.definition.implement.getName(), e);
            }
            return false;
        }
    }

    public ComponentInstanceProperties getProperties() {
        return properties;
    }

    public void setProperties(ComponentInstanceProperties properties) {
        if (this.properties != null) {
            throw new IllegalStateException("properties have already initialized");
        }
        this.properties = properties;
        this.context.setProperties(Collections.unmodifiableMap(properties));
    }

    public void dispose() {
        disable();
    }

    /**
	 * this method will be removed in 1.0M3
	 * use setProperties(ComponentInstanceProperties properties) instead
	 * @param config
	 */
    @Deprecated
    public void setProperties(Map<String, Object> config) {
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(this.getClass().getSimpleName());
        sb.append("[").append(component != null ? component.definition.implement.getSimpleName() : "").append("]");
        return sb.toString();
    }
}
