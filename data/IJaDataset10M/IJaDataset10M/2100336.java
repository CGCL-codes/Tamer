package org.xfeep.asura.core;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import org.xfeep.asura.core.event.ServiceEvent;
import org.xfeep.asura.core.logger.CoreLoggerManager;
import org.xfeep.asura.core.reflect.TypeItem;

public class NReference extends Reference {

    protected LinkedHashSet<ComponentInstance> targets;

    public NReference(ReferenceDefinition definition, Component component) {
        super(definition, component);
    }

    @Override
    public boolean isSatisfied() {
        if (!definition.multiplicityType.isRequired()) {
            return true;
        }
        if (targets != null) {
            return !targets.isEmpty();
        }
        return false;
    }

    @Override
    public boolean bind(ComponentInstance instance) {
        if (targets == null && !definition.multiplicityType.isRequired()) {
            return true;
        }
        TypeItem bind = definition.bind;
        List<Object> services = new ArrayList<Object>();
        for (ComponentInstance target : targets) {
            try {
                Object service = target.getService(this);
                if (service == null) {
                    continue;
                }
                services.add(service);
            } catch (Throwable e) {
                CoreLoggerManager.getDefaultLogger().warn("error when bind service reference : " + bind.getName() + "@" + source.definition.getImplement().getName(), e);
            }
        }
        try {
            if (bind.getType().isArray()) {
                Object array = Array.newInstance(definition.serviceClass, services.size());
                for (int i = 0; i < services.size(); i++) {
                    Array.set(array, i, services.get(i));
                }
                bind.setValue(instance.service, array);
            } else {
                bind.setValue(instance.service, services);
            }
            return true;
        } catch (Throwable e) {
            CoreLoggerManager.getDefaultLogger().error("error when bind service reference : " + bind.getName() + "@" + source.definition.getImplement().getName(), e);
        }
        return false;
    }

    public boolean unbind(ComponentInstance instance, ComponentInstance target) {
        TypeItem bind = definition.bind;
        try {
            bind.setValue(instance.service, null);
            target.ungetService(this);
            return true;
        } catch (Throwable e) {
            CoreLoggerManager.getDefaultLogger().error("error when unbind service reference : " + bind.getName() + "@" + source.definition.getImplement().getName(), e);
        }
        return false;
    }

    @Override
    public boolean unbind(ComponentInstance instance) {
        for (ComponentInstance target : targets) {
            unbind(instance, target);
        }
        return true;
    }

    @Override
    public void onServiceChanged(ServiceEvent event) {
        synchronized (source.instanceMainLock) {
            ComponentInstance eventSource = event.getSource();
            if (event.getType() == ServiceEvent.ACTIVE && definition.getMatcher().match(eventSource.getProperties())) {
                if (!source.isStatisfied()) {
                    if (targets == null) {
                        targets = new LinkedHashSet<ComponentInstance>();
                    }
                    if (!targets.contains(eventSource)) {
                        targets.add(eventSource);
                    }
                    source.tryStatisfy();
                } else {
                    if (targets == null) {
                        targets = new LinkedHashSet<ComponentInstance>();
                    }
                    if (!targets.contains(eventSource)) {
                        targets.add(eventSource);
                        for (ComponentInstance ci : source.getInstances(null)) {
                            bind(ci);
                        }
                    }
                }
            } else if (targets != null && definition.contractType != ContractType.CARELESS) {
                if (targets.contains(eventSource)) {
                    targets.remove(eventSource);
                    if (definition.multiplicityType.isRequired() && targets.isEmpty()) {
                        source.tryUnstatisfy();
                        targets = null;
                    } else {
                        for (ComponentInstance ci : source.getInstances(null)) {
                            bind(ci);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void closeContract(boolean cascade) {
        targets = null;
        super.closeContract(cascade);
    }

    @Override
    public void reset() {
        targets = null;
    }
}
