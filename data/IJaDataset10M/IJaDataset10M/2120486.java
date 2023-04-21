package net.sf.rcpforms.widgetwrapper.wrapper;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import net.sf.rcpforms.widgets2.IRCPWidget2;
import net.sf.rcpforms.widgetwrapper.statehandling.WidgetStateManager;
import net.sf.rcpforms.widgetwrapper.wrapper.event.EventAdapterRegistry;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IEventAdapter;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IEventKey;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IEventListenerFactory;
import net.sf.rcpforms.widgetwrapper.wrapper.event.IUntypedEventAdapter;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.SWTEventListener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class RCPWidget2 extends RCPBeanControl2 implements IRCPWidget2 {

    @SuppressWarnings("all")
    private static final Logger log = Logger.getLogger(RCPWidget2.class.getName());

    /**
     * if you make a control invisible, the wrappers automatically exclude them from layout, thus
     * their space is reused. If this flag is set, the standard swt behavior is kept, thus the
     * invisible control still takes up the space it had before.
     */
    private boolean keepSpaceIfInvisible = false;

    /** renderer used for widget state rendering */
    private WidgetStateManager stateManager = null;

    private int controlState = EControlState.getFlags(EControlState.VISIBLE, EControlState.ENABLED);

    private ListenerList stateListenerList = new ListenerList();

    protected final StateListener stateListener;

    private EControlState lastFiredState = null;

    private Map<Object, Boolean> customStateMap = null;

    private ArrayList<ListenerEntry> listenerEntries;

    private ArrayList<UntypedListenerEntry> untypedListenerEntries;

    public RCPWidget2(final String labelText, final int style) {
        this(labelText, style, null);
    }

    public RCPWidget2(final String labelText, final int style, final Object uid) {
        super(labelText, style, uid);
        stateListener = new StateListener() {

            public void stateChanged(final StateChangeEvent event) {
                updateState();
                if (isContainer()) {
                    fireStateChange(event.getState(), lastFiredState, event.getNewState());
                    lastFiredState = event.getState();
                }
            }
        };
        listenerEntries = new ArrayList<ListenerEntry>();
        untypedListenerEntries = new ArrayList<UntypedListenerEntry>();
    }

    protected void addStateListener(final StateListener listener) {
        stateListenerList.add(listener);
    }

    protected void removeStateListener(final StateListener listener) {
        stateListenerList.remove(listener);
    }

    public void fireStateChange(final EControlState state, final Object oldValue, final Object newValue) {
        final StateChangeEvent event = new StateChangeEvent(this, state, oldValue, newValue);
        final Object[] listeners = stateListenerList.getListeners();
        for (int i = 0; i < listeners.length; ++i) {
            ((StateListener) listeners[i]).stateChanged(event);
        }
    }

    /**
     * true if this widget is composed of other widgets, by swt composition or compound widgets.
     * RCPCompound redefines this to true.
     * <p>
     * This is different from {@link RCPCompound#isExtensible()} which defines who can compose it,
     * subclassers or clients.
     */
    public boolean isContainer() {
        return false;
    }

    @Override
    protected void onDispose() {
        super.onDispose();
    }

    /**
     * modifies the given state of the widget and fires a state change event
     */
    public void setState(final EControlState state, final boolean value) {
        doSetState(state, value);
    }

    /**
     * if for a widget special state rendering is needed, a custom renderer can be set here; however
     * this is not recommended, since applicationwide state rendering should be consistent and to
     * customize rendering for a RCP Application this can be done by
     * {@link WidgetStateManager#registerWidgetStateManager(Class, WidgetStateManager) registering a specific State
     * Renderer}
     * 
     * @param renderer renderer to use or null if the default renderer registered for this widget
     *            class should be used
     */
    public void setStateRenderer(final WidgetStateManager renderer) {
        this.stateManager = renderer;
        updateState();
    }

    /**
     * @param state
     * @param value
     */
    protected void doSetState(final EControlState state, final boolean value) {
        final int oldState = controlState;
        final int temporaryState = EControlState.modifyState(state, value, this.controlState);
        this.controlState = getStateManager().reviewState(this, temporaryState);
        if (oldState != controlState) {
            updateState(false);
            fireStateChange(state, !value, value);
            fireMappedPropertyChange(PROP_STATE, state, !value, value);
        }
    }

    /**
     * @return true if this widget is enabled and the known parent chain is enabled
     */
    public boolean isChainEnabled() {
        return getState(EControlState.ENABLED);
    }

    /**
     * @return true if this widget is visible and the known parent chain is visible
     */
    public boolean isChainVisible() {
        return getState(EControlState.VISIBLE);
    }

    protected WidgetStateManager getStateManager() {
        return stateManager != null ? stateManager : WidgetStateManager.getInstance(getClass());
    }

    /**
     * updateState may be called in states where the control is not created yet; implementors must
     * take care of this !
     */
    protected void updateState() {
        this.updateState(true);
    }

    /**
     * updateState may be called in states where the control is not created yet; implementors must
     * take care of this !
     */
    protected void updateState(final boolean changed) {
        if (changed) this.controlState = getStateManager().reviewState(this, controlState);
        getStateManager().updateState(this, controlState);
    }

    public boolean hasState(final EControlState state) {
        return EControlState.isMember(state, this.controlState);
    }

    @Override
    public void createUI(final FormToolkit formToolkit) {
        super.createUI(formToolkit);
    }

    /**
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget#defineSelfListeners()
     */
    @Override
    protected void defineSelfListeners() {
        super.defineSelfListeners();
        attachActionListeners();
        final Widget swtWidget = getSWTWidget();
        swtWidget.addListener(SWT.Resize, new Listener() {

            public void handleEvent(final Event event) {
                handleResize(event);
            }
        });
    }

    /**
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPBeanControl2#dispose()
     */
    @Override
    public void dispose() {
        removeActionListeners();
        super.dispose();
    }

    /**
     * @param formToolkit
     */
    @Override
    protected void doCreateUI(final FormToolkit formToolkit) {
        super.doCreateUI(formToolkit);
        updateState();
    }

    /**
     * @param keepSpaceIfInvisible The keepSpaceIfInvisible to set.
     */
    public void setKeepSpaceIfInvisible(final boolean keepSpaceIfInvisible) {
        this.keepSpaceIfInvisible = keepSpaceIfInvisible;
        updateState();
    }

    /**
     * @return Returns the keepSpaceIfInvisible.
     */
    public boolean isKeepSpaceIfInvisible() {
        return keepSpaceIfInvisible;
    }

    protected void handleResize(final Event event) {
    }

    public void addListener(final EventListener listener, final IEventKey eventKey, final IEventKey... furtherKeys) {
        checkActionSupported(eventKey);
        for (final IEventKey key : furtherKeys) {
            checkActionSupported(key);
        }
        final IEventKey[] allKeys;
        if (furtherKeys.length > 0) {
            allKeys = new IEventKey[furtherKeys.length + 1];
            System.arraycopy(furtherKeys, 0, allKeys, 0, furtherKeys.length);
            allKeys[furtherKeys.length] = eventKey;
        } else {
            allKeys = new IEventKey[] { eventKey };
        }
        synchronized (listenerEntries) {
            for (final ListenerEntry entry : listenerEntries) {
                if (entry == listener) {
                    return;
                }
            }
            final ListenerEntry entry = new ListenerEntry();
            entry.listener = listener;
            entry.eventKeys = allKeys;
            listenerEntries.add(entry);
            if (hasSpawned()) {
                attachActionListeners_entry(getSWTWidget(), entry);
            }
        }
    }

    public boolean removeListener(final EventListener listener) {
        int index = -1;
        synchronized (listenerEntries) {
            for (int i = 0; i < listenerEntries.size(); i++) {
                if (listenerEntries.get(i) == listener) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                listenerEntries.remove(index);
                return true;
            }
        }
        return false;
    }

    public void addUntypedListener(final Object listener, final IEventKey eventKey, final IEventKey... furtherKeys) {
        checkActionSupported(eventKey);
        for (final IEventKey key : furtherKeys) {
            checkActionSupported(key);
        }
        final IEventKey[] allKeys;
        if (furtherKeys.length > 0) {
            allKeys = new IEventKey[furtherKeys.length + 1];
            System.arraycopy(furtherKeys, 0, allKeys, 0, furtherKeys.length);
            allKeys[furtherKeys.length] = eventKey;
        } else {
            allKeys = new IEventKey[] { eventKey };
        }
        synchronized (untypedListenerEntries) {
            for (final UntypedListenerEntry entry : untypedListenerEntries) {
                if (entry == listener) {
                    return;
                }
            }
            final UntypedListenerEntry entry = new UntypedListenerEntry();
            entry.listener = listener;
            entry.eventKeys = allKeys;
            entry.adapter = EventAdapterRegistry.lookupUntypedListenerAdapter(listener.getClass());
            untypedListenerEntries.add(entry);
            if (hasSpawned()) {
                attachActionListeners_entry(getSWTWidget(), entry);
            }
        }
    }

    public boolean removeUntypedListener(final Object listener) {
        int index = -1;
        synchronized (untypedListenerEntries) {
            for (int i = 0; i < untypedListenerEntries.size(); i++) {
                if (untypedListenerEntries.get(i) == listener) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                untypedListenerEntries.remove(index);
                return true;
            }
        }
        return false;
    }

    public Set<IEventKey> getActionSupportedKeys() {
        return EventAdapterRegistry.getEventSupportedKeys(getClass());
    }

    protected IEventAdapter lookupActionAdapter(final IEventKey eventKey) {
        return EventAdapterRegistry.lookupAdapter(getClass(), eventKey);
    }

    protected void checkActionSupported(final IEventKey eventKey) {
        if (!getActionSupportedKeys().contains(eventKey)) {
            throw new IllegalArgumentException("event-key '" + eventKey + "' is not supported by widget type " + getClass());
        }
    }

    protected void checkActionSupported(final IEventKey eventKey, final IEventAdapter eventAdapter, final IEventListenerFactory eventListenerFactory) {
        checkActionSupported(eventKey);
        final Class<?> requiredWidgetType = eventAdapter.getRequiredWidgetType();
        if (requiredWidgetType != null && !requiredWidgetType.isAssignableFrom(this.getClass())) {
            throw new IllegalArgumentException("widget " + getClass() + " not supported for event-adapter for event-key=" + eventKey);
        }
    }

    protected void attachActionListeners() {
        final Widget swtWidget = getSWTWidget();
        for (final ListenerEntry entry : listenerEntries) {
            attachActionListeners_entry(swtWidget, entry);
        }
        for (final UntypedListenerEntry entry : untypedListenerEntries) {
            attachActionListeners_entry(swtWidget, entry);
        }
    }

    protected void removeActionListeners() {
        final Widget swtWidget = getSWTWidget();
        for (final ListenerEntry entry : listenerEntries) {
            removeActionListeners_entry(swtWidget, entry);
        }
        for (final UntypedListenerEntry entry : untypedListenerEntries) {
            removeActionListeners_entry(swtWidget, entry);
        }
    }

    protected void attachActionListeners_entry(final Widget swtWidget, final ListenerEntry entry) {
        final IEventKey[] eventKeys = entry.eventKeys;
        final TypedListener typedListener = new TypedListener((SWTEventListener) entry.listener);
        for (int i = 0; i < eventKeys.length; i++) {
            swtWidget.addListener(eventKeys[i].getSwtEventType(), typedListener);
        }
    }

    protected void attachActionListeners_entry(final Widget swtWidget, final UntypedListenerEntry entry) {
        for (int i = 0; i < entry.eventKeys.length; i++) {
            entry.adapter.reAttachListener(this, entry.eventKeys[i], entry.listener);
        }
    }

    protected void removeActionListeners_entry(final Widget swtWidget, final ListenerEntry entry) {
        final IEventKey[] eventKeys = entry.eventKeys;
        final TypedListener typedListener = new TypedListener((SWTEventListener) entry.listener);
        for (int i = 0; i < eventKeys.length; i++) {
            swtWidget.removeListener(eventKeys[i].getSwtEventType(), typedListener);
        }
    }

    protected void removeActionListeners_entry(final Widget swtWidget, final UntypedListenerEntry entry) {
        for (int i = 0; i < entry.eventKeys.length; i++) {
            entry.adapter.removeListener(this, entry.eventKeys[i], entry.listener);
        }
    }

    protected static class ListenerEntry {

        public EventListener listener;

        public IEventKey[] eventKeys;
    }

    protected static class UntypedListenerEntry {

        public Object listener;

        public IEventKey[] eventKeys;

        public IUntypedEventAdapter adapter;
    }

    public final boolean getState(final EControlState state) {
        return EControlState.isMember(state, controlState);
    }

    /** convenience state getter */
    public final boolean isVisible() {
        return getState(EControlState.VISIBLE);
    }

    /** convenience state getter */
    public final boolean isEnabled() {
        return getState(EControlState.ENABLED);
    }

    /** convenience state getter */
    public final boolean isMandatory() {
        return getState(EControlState.MANDATORY);
    }

    /** convenience state setter */
    public final void setVisible(final boolean newState) {
        setState(EControlState.VISIBLE, newState);
    }

    /** convenience state setter */
    public final void setEnabled(final boolean newState) {
        setState(EControlState.ENABLED, newState);
    }

    /** convenience state setter */
    public final void setMandatory(final boolean newState) {
        setState(EControlState.MANDATORY, newState);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPWidget#getIsVisible()
     */
    public boolean getIsVisible() {
        return isVisible();
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPWidget#setIsVisible(boolean)
     */
    public void setIsVisible(final boolean visible) {
        setVisible(visible);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPWidget#getIsEnabled()
     */
    public boolean getIsEnabled() {
        return isEnabled();
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPWidget#setIsEnabled(boolean)
     */
    public void setIsEnabled(final boolean enabled) {
        setEnabled(enabled);
    }

    /**
     * @see net.sf.rcpforms.widgetwrapper.wrapper.RCPWidget#setLabel(java.lang.String)
     */
    @Override
    public void setLabel(final String label) {
        if (hasSpawned()) {
            final String oldLabel = getLabel();
            super.setLabel(label);
            firePropertyChange(PROP_LABEL, label, oldLabel);
        } else {
            addSpawnHook4Property(PROP_LABEL, label);
        }
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#setIsEditable(boolean)
     */
    public void setIsEditable(final boolean editable) {
        setState(EControlState.READONLY, !editable);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#getIsEditable()
     */
    public boolean getIsEditable() {
        return !getState(EControlState.READONLY);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#setIsMandatory(boolean)
     */
    public void setIsMandatory(final boolean mandatory) {
        setMandatory(mandatory);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#getIsMandatory()
     */
    public boolean getIsMandatory() {
        return isMandatory();
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#setIsRecommended(boolean)
     */
    public void setIsRecommended(final boolean recommended) {
        setState(EControlState.RECOMMENDED, recommended);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#getIsRecommended()
     */
    public boolean getIsRecommended() {
        return getState(EControlState.RECOMMENDED);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#getIsInfoState()
     */
    public boolean getIsInfoState() {
        return getState(EControlState.INFO);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#setIsInfoState(boolean)
     */
    public void setIsInfoState(final boolean newIsInfoState) {
        setState(EControlState.INFO, newIsInfoState);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#setCustomState(java.lang.Object, java.lang.Boolean)
     */
    public void setCustomState(final Object stateKey, final Boolean customState) {
        final Boolean oldState = getCustomState(stateKey);
        if (customStateMap == null) {
            customStateMap = new HashMap<Object, Boolean>();
        }
        customStateMap.put(stateKey, customState);
        changeSupport.fireMappedPropertyChange(PROP_CUSTOM_STATE, stateKey, customState, oldState);
    }

    /**
     * @see net.sf.rcpforms.widgets2.IRCPControl#getCustomState(java.lang.Object)
     */
    public Boolean getCustomState(final Object stateKey) {
        return customStateMap != null ? customStateMap.get(stateKey) : null;
    }
}
