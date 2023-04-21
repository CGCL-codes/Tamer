package net.sf.unruly.impl.event;

import net.sf.unruly.Session;
import net.sf.unruly.listener.SessionEventListener;
import net.sf.unruly.mapping.ClassRegistry;
import net.sf.unruly.mapping.RulesClassDescriptor;
import net.sf.unruly.mapping.RulesPropertyDescriptor;
import net.sf.unruly.mapping.impl.DerivedClassRegistry;
import net.sf.unruly.mapping.impl.MapClassRegistry;
import net.sf.unruly.util.PropertyChangeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Default implementation that registers property change listeners.
 * There should be one instance of this class per session.
 *
 * @author Lance Arlaus
 */
public class DefaultSessionEventListener implements SessionEventListener {

    private static final Log LOG = LogFactory.getLog(DefaultSessionEventListener.class);

    private final ClassRegistry<RulesClassDescriptor> rulesClassRegistry;

    private final ClassRegistry<PropertyChangeListener> listenerClassRegistry;

    private final Session session;

    public DefaultSessionEventListener(ClassRegistry<RulesClassDescriptor> rulesClassRegistry, Session session) {
        this.rulesClassRegistry = rulesClassRegistry;
        this.session = session;
        this.listenerClassRegistry = createPropertyChangeListenerRegistry();
    }

    public <T> void onCreateObject(Class<? extends T> mappedClass, T instance) {
        Assert.isTrue(this.session == session, "object created for different (wrong) session");
        PropertyChangeListener listener = listenerClassRegistry.getMappedValue(mappedClass);
        if (listener != null) {
            Assert.isTrue(PropertyChangeUtils.isPropertyChangeSource(instance), instance.getClass().getSimpleName() + " is not a change source");
            PropertyChangeUtils.addPropertyChangeListener(instance, listener);
        }
    }

    protected ClassRegistry<PropertyChangeListener> createPropertyChangeListenerRegistry() {
        LOG.info("creating all PropertyChangeListeners for Session");
        final MapClassRegistry<PropertyChangeListener> registry = new MapClassRegistry<PropertyChangeListener>();
        for (final Class mappedClass : rulesClassRegistry.getMappedClasses()) {
            final RulesClassDescriptor descriptor = rulesClassRegistry.getMappedValue(mappedClass);
            final PropertyChangeListener listener = createPropertyChangeListener(descriptor);
            if (listener != null) {
                registry.put(mappedClass, listener);
            }
        }
        return new DerivedClassRegistry<PropertyChangeListener>(registry);
    }

    protected PropertyChangeListener createPropertyChangeListener(final RulesClassDescriptor rulesClass) {
        final Set<String> mutatingProperties = getMutatingProperties(rulesClass);
        if (!mutatingProperties.isEmpty()) {
            PropertyChangeListener listener = new SessionPropertyChangeListener(session, mutatingProperties);
            if (rulesClass.isPersistent()) {
                listener = new PersistentPropertyChangeFilter(listener);
            }
            return listener;
        } else {
            return null;
        }
    }

    protected Set<String> getMutatingProperties(final RulesClassDescriptor rulesClass) {
        final Collection<? extends RulesPropertyDescriptor> properties = rulesClass.getProperties().values();
        final Set<String> mutatingProperties = new HashSet<String>();
        for (final RulesPropertyDescriptor property : properties) {
            switch(property.getWriteMode()) {
                case AUTO:
                case IMMEDIATE:
                    mutatingProperties.add(property.getPropertyDescriptor().getName());
                    break;
                case NEVER:
                    break;
                default:
                    throw new IllegalStateException("unrecognized property write mode" + property.getWriteMode());
            }
        }
        return mutatingProperties;
    }
}
