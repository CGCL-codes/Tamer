package com.volantis.mcs.interaction.impl;

import com.volantis.mcs.interaction.BaseProxy;
import com.volantis.mcs.interaction.BeanProxy;
import com.volantis.mcs.interaction.InteractionModel;
import com.volantis.mcs.interaction.ListProxy;
import com.volantis.mcs.interaction.OpaqueProxy;
import com.volantis.mcs.interaction.ParentProxy;
import com.volantis.mcs.interaction.Proxy;
import com.volantis.mcs.interaction.ReadWriteState;
import com.volantis.mcs.interaction.diagnostic.DiagnosticEvent;
import com.volantis.mcs.interaction.diagnostic.DiagnosticListener;
import com.volantis.mcs.interaction.diagnostic.ProxyDiagnostic;
import com.volantis.mcs.interaction.event.InteractionEvent;
import com.volantis.mcs.interaction.event.InteractionEventListener;
import com.volantis.mcs.interaction.event.ProxyModelChangedEvent;
import com.volantis.mcs.interaction.event.ReadOnlyStateChangedEvent;
import com.volantis.mcs.interaction.impl.operation.SetModelObjectOperation;
import com.volantis.mcs.interaction.impl.validation.ProxyValidator;
import com.volantis.mcs.interaction.operation.Operation;
import com.volantis.mcs.model.ModelFactory;
import com.volantis.mcs.model.copy.Copyable;
import com.volantis.mcs.model.descriptor.BeanClassDescriptor;
import com.volantis.mcs.model.descriptor.PropertyDescriptor;
import com.volantis.mcs.model.descriptor.TypeDescriptor;
import com.volantis.mcs.model.path.Path;
import com.volantis.mcs.model.path.PathBuilder;
import com.volantis.mcs.model.path.Step;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.synergetics.log.LogDispatcher;
import com.volantis.synergetics.localization.ExceptionLocalizer;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public abstract class AbstractProxy implements InternalProxy {

    /**
     * Used for logging
     */
    private static final LogDispatcher logger = LocalizationFactory.createLogger(AbstractProxy.class);

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(AbstractProxy.class);

    /**
     * The current read/write state for this proxy. Defaults to an inherited
     * value.
     */
    private ReadWriteState readWriteState = ReadWriteState.INHERIT;

    private static int depth;

    private static final ModelFactory modelFactory = ModelFactory.getDefaultInstance();

    protected final InteractionModel interactionModel;

    protected InternalParentProxy parent;

    protected String description;

    private List interactionListeners;

    private List diagnosticListeners;

    private Object modelObject;

    private int modificationCount;

    private List diagnostics;

    private boolean hadDiagnostics;

    public AbstractProxy(InteractionModel interactionModel) {
        this.interactionModel = interactionModel;
        this.interactionListeners = new ArrayList();
        this.diagnosticListeners = new ArrayList();
    }

    protected void describe(String description) {
        this.description = description;
    }

    protected boolean equals(Object o1, Object o2) {
        return o1 == null ? o2 == null : o1.equals(o2);
    }

    public void fireEvent(InteractionEvent event) {
        if (event.getProxy() == this) {
            dispatchEvent(event, false);
        } else {
            dispatchEvent(event, true);
        }
    }

    private void dispatchEvent(InteractionEvent event, boolean deep) {
        List listeners = this.interactionListeners;
        for (int i = 0; i < listeners.size(); i++) {
            ListenerWrapper wrapper = (ListenerWrapper) listeners.get(i);
            if (!deep || wrapper.getDeep()) {
                InteractionEventListener listener = wrapper.getListener();
                event.dispatch(listener);
            }
        }
        if (parent != null) {
            parent.fireEvent(event);
        }
    }

    public Object getModelObject() {
        return getModelObject(false);
    }

    public Object getModelObject(boolean create) {
        if (modelObject == null) {
            if (parent == null) {
                if (create) {
                    modelObject = createModelObject();
                }
            } else {
                modelObject = parent.getEmbeddedModelObject(this, create);
            }
        }
        return modelObject;
    }

    protected abstract Object createModelObject();

    public void detach() {
        parent = null;
    }

    public void attach(ParentProxy parent) {
        this.parent = (InternalParentProxy) parent;
    }

    public List getDiagnostics() {
        return diagnostics;
    }

    public void addDiagnosticListener(DiagnosticListener listener) {
        int index = findListener(listener);
        if (index != -1) {
            throw new IllegalStateException("Cannot add listener multiple times");
        }
        diagnosticListeners = addListener(diagnosticListeners, listener);
    }

    public void removeDiagnosticListener(DiagnosticListener listener) {
        int index = findListener(listener);
        if (index == -1) {
            if (logger.isDebugEnabled()) {
                logger.debug("Attempt to remove listener that has not been added");
            }
        } else {
            diagnosticListeners = removeListener(diagnosticListeners, index);
        }
    }

    public void validate() {
        ProxyValidator proxyValidator = new ProxyValidator();
        proxyValidator.validate(this);
    }

    public Object copyModelObject() {
        if (modelObject == null) {
            return null;
        } else if (modelObject instanceof Copyable) {
            return ((Copyable) modelObject).copy();
        } else {
            return copyModelObjectImpl();
        }
    }

    /**
     * Copy a non null, non {@link Copyable} model object.
     *
     * @return The model object.
     */
    protected abstract Object copyModelObjectImpl();

    public String getPathAsString() {
        Path path = getPathFromRoot();
        return path.getAsString();
    }

    public ParentProxy getParentProxy() {
        return parent;
    }

    public Proxy getProxy(Path path) {
        return traversePath(path, false, null);
    }

    public Proxy getEnclosingProxy(Path path) {
        return traversePath(path, true, null);
    }

    private Proxy traversePath(Path path, boolean enclosing, final List proxies) {
        int count = path.getStepCount();
        AbstractProxy proxy = this;
        for (int s = 0; s < count && proxy != null; s += 1) {
            Step step = path.getStep(s);
            proxy = (AbstractProxy) proxy.traverse(step, enclosing, proxies);
            if (proxy instanceof BaseProxy) {
                Proxy concreteProxy = ((BaseProxy) proxy).getConcreteProxy();
                if (concreteProxy != null) {
                    proxy = (AbstractProxy) concreteProxy;
                }
            }
            if (proxies != null && proxy != null) {
                proxies.add(proxy);
            }
        }
        return proxy;
    }

    public List getProxies(Path path) {
        List proxies = new ArrayList();
        proxies.add(this);
        traversePath(path, true, proxies);
        return proxies;
    }

    public Path getPathFromRoot() {
        PathBuilder builder = modelFactory.createPathBuilder();
        if (parent != null) {
            parent.buildPath(builder, this);
        }
        return builder.getPath();
    }

    public ReadWriteState getReadWriteState() {
        return readWriteState;
    }

    public void setReadWriteState(ReadWriteState newState) {
        boolean oldReadOnly = isReadOnly();
        readWriteState = newState;
        boolean newReadOnly = isReadOnly();
        if (newReadOnly != oldReadOnly) {
            EventPropagatingProxyVisitor visitor = new EventPropagatingProxyVisitor(newReadOnly);
            accept(visitor);
        }
    }

    public boolean isReadOnly() {
        boolean readOnly;
        if (readWriteState == ReadWriteState.READ_ONLY) {
            readOnly = true;
        } else if (readWriteState == ReadWriteState.READ_WRITE) {
            readOnly = false;
        } else {
            Proxy parent = getParentProxy();
            if (parent != null) {
                readOnly = parent.isReadOnly();
            } else {
                readOnly = false;
            }
        }
        return readOnly;
    }

    private int findListener(DiagnosticListener listener) {
        for (int i = 0; i < diagnosticListeners.size(); i++) {
            DiagnosticListener d = (DiagnosticListener) diagnosticListeners.get(i);
            if (d == listener) {
                return i;
            }
        }
        return -1;
    }

    public final Object setModelObject(Object newModelObject) {
        return setModelObject(newModelObject, false);
    }

    public Object setModelObject(Object newModelObject, boolean force) {
        return setModelObject(newModelObject, force, true);
    }

    public Object setModelObject(Object newModelObject, boolean force, boolean originator) {
        assertWritable();
        depth += 1;
        try {
            if (!force && equals(modelObject, newModelObject)) {
                return modelObject;
            }
            ensureModelObjectCompatability(newModelObject);
            Object oldModelObject = modelObject;
            if (parent != null) {
                Object oldEmbeddedModelObject = parent.setEmbeddedModelObject(this, newModelObject);
            }
            modelObject = newModelObject;
            modifiedProxy();
            updateModelObject(newModelObject, force, false);
            fireEvent(new ProxyModelChangedEvent(this, oldModelObject, newModelObject, originator));
            return oldModelObject;
        } finally {
            depth -= 1;
        }
    }

    /**                                              
     * Changes the model object associated with this proxy and all its
     * descendants.
     *
     * @param modelObject The new model object.
     * @param force If true, the model will be explicitly set even if it is
     *              the same as the current model object.
     * @param originator Indicate this is the originator of the change.
     */
    protected abstract void updateModelObject(Object modelObject, boolean force, boolean originator);

    /**
     * Check to see whether the model object is compatible with this proxy.
     *
     * <p>If the model object cannot be assigned to this proxy then it will
     * throw an {@link IllegalArgumentException}.</p>
     *
     * @param modelObject The model object to assign.
     */
    protected void ensureModelObjectCompatability(Object modelObject) {
        TypeDescriptor descriptor = getTypeDescriptor();
        Class typeClass = descriptor.getTypeClass();
        if (modelObject != null && !typeClass.isInstance(modelObject)) {
            throw new IllegalArgumentException("Incompatible model object " + modelObject + " not instance of " + typeClass);
        }
    }

    public Operation prepareSetModelObjectOperation(Object newModelObject) {
        return new SetModelObjectOperation(this, newModelObject);
    }

    public void addListener(InteractionEventListener listener, boolean deep) {
        int index = findListener(listener);
        if (index != -1) {
            throw new IllegalStateException("Cannot add listener multiple times");
        }
        final EventListener wrapper = new ListenerWrapper(listener, deep);
        interactionListeners = addListener(interactionListeners, wrapper);
    }

    private List addListener(final List listeners, final EventListener listener) {
        List newList = new ArrayList(listeners);
        newList.add(listener);
        return newList;
    }

    private List removeListener(final List listeners, final int listenerIndex) {
        List newList = new ArrayList(listeners);
        newList.remove(listenerIndex);
        return newList;
    }

    private int findListener(InteractionEventListener listener) {
        for (int i = 0; i < interactionListeners.size(); i++) {
            ListenerWrapper wrapper = (ListenerWrapper) interactionListeners.get(i);
            if (wrapper.getListener() == listener) {
                return i;
            }
        }
        return -1;
    }

    public void removeListener(InteractionEventListener listener) {
        int index = findListener(listener);
        if (index != -1) {
            interactionListeners = removeListener(interactionListeners, index);
        }
    }

    public int getModificationCount() {
        return modificationCount;
    }

    protected void modifiedProxy() {
        modificationCount += 1;
    }

    public void prepareForValidation() {
        hadDiagnostics = (diagnostics != null);
        diagnostics = null;
    }

    public void addDiagnostic(ProxyDiagnostic diagnostic) {
        if (diagnostics == null) {
            diagnostics = new ArrayList();
        }
        diagnostics.add(diagnostic);
        if (parent != null) {
            parent.addDiagnostic(diagnostic);
        }
    }

    public void finishValidation() {
        if (hadDiagnostics || diagnostics != null) {
            DiagnosticEvent event = new DiagnosticEvent(this, true);
            List listeners = diagnosticListeners;
            for (int i = 0; i < listeners.size(); i++) {
                DiagnosticListener listener = (DiagnosticListener) listeners.get(i);
                listener.diagnosticsChanged(event);
            }
        }
    }

    /**
     * Called before modification of the proxy. If the proxy is not writeable
     * then an IllegalStateException will be thrown.
     */
    protected void assertWritable() {
        if (isReadOnly()) {
            throw new IllegalStateException("Can not modify proxy, as it is " + "read-only.");
        }
    }

    class ListenerWrapper implements EventListener {

        private final InteractionEventListener listener;

        private final boolean deep;

        public ListenerWrapper(InteractionEventListener listener, boolean deep) {
            this.listener = listener;
            this.deep = deep;
        }

        public InteractionEventListener getListener() {
            return listener;
        }

        public boolean getDeep() {
            return deep;
        }
    }

    /**
     * Visitor to propagate a change in read-only state down through children
     * of a proxy.
     */
    private class EventPropagatingProxyVisitor implements ProxyVisitor {

        /**
         * The new read-only state to be propagated through the event.
         */
        private boolean newState;

        private boolean originator = true;

        /**
         * Construct a new event propagating proxy visitor with a specified
         * new read-only state.
         *
         * @param newState The new read-only state
         */
        public EventPropagatingProxyVisitor(boolean newState) {
            this.newState = newState;
        }

        public void visit(BeanProxy proxy) {
            fireEvent((InternalProxy) proxy);
            BeanClassDescriptor descriptor = proxy.getBeanClassDescriptor();
            List properties = descriptor.getPropertyDescriptors();
            for (int i = 0; i < properties.size(); i++) {
                PropertyDescriptor property = (PropertyDescriptor) properties.get(i);
                Proxy child = proxy.getPropertyProxy(property.getIdentifier());
                propagate(child);
            }
        }

        public void visit(ListProxy proxy) {
            fireEvent((InternalProxy) proxy);
            for (int i = 0; i < proxy.size(); i++) {
                propagate(proxy.getItemProxy(i));
            }
        }

        public void visit(OpaqueProxy proxy) {
            fireEvent((InternalProxy) proxy);
        }

        public void visit(BaseProxy proxy) {
            fireEvent((InternalProxy) proxy);
            propagate(proxy.getConcreteProxy());
        }

        /**
         * Propagate the event down to a child proxy if it is supposed to
         * inherit read/write state.
         *
         * @param child The child to pass the event down to.
         */
        private void propagate(Proxy child) {
            if (child != null && child.getReadWriteState() == ReadWriteState.INHERIT) {
                ((InternalProxy) child).accept(this);
            }
        }

        /**
         * Fire a change event for a specified proxy.
         *
         * @param proxy The proxy to be the source of the event
         */
        private void fireEvent(InternalProxy proxy) {
            ReadOnlyStateChangedEvent event = new ReadOnlyStateChangedEvent(proxy, newState, originator);
            proxy.fireEvent(event);
            originator = false;
        }
    }
}
