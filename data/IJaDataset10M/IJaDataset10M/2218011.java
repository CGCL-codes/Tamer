package com.softaspects.jsf.component.base;

import com.softaspects.jsf.component.base.event.ComponentEvent;
import com.softaspects.jsf.component.base.event.PresentationEvent;
import com.softaspects.jsf.component.base.listener.ComponentEventListener;
import com.softaspects.jsf.renderer.base.RendererUtils;
import com.softaspects.jsf.support.FacesUtil;
import com.softaspects.jsf.support.util.Validation;
import org.apache.commons.logging.LogFactory;
import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Base Component
 */
public abstract class WGFComponentBase extends UIComponentBase implements PropertiesHolder, Serializable {

    protected static final String SELECTION_MODEL_WAS_CHANGED_FROM_REQUEST = "component_selection_model_was_changed_from_request";

    protected static final String DATA_MODEL_WAS_CHANGED_FROM_REQUEST = "component_data_model_was_changed_from_request";

    private Map<String, Object> properties = new HashMap<String, Object>();

    private Object[] _state = null;

    private List<ComponentEvent> queueEvent = new ArrayList<ComponentEvent>();

    private List<ComponentEventListener> listeners = new ArrayList<ComponentEventListener>();

    private HashMap<Integer, String> fPresentationEvents = new HashMap<Integer, String>();

    private static org.apache.commons.logging.Log log = LogFactory.getLog(WGFComponentBase.class);

    protected boolean handleNavigationActions(FacesContext context) {
        return false;
    }

    public boolean isImmediate() {
        return (java.lang.Boolean) getProperty("immediate", Boolean.FALSE);
    }

    protected boolean handleNavigation = true;

    protected boolean handleNavigation(String action) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (action != null) {
            context.getApplication().getNavigationHandler().handleNavigation(context, null, action);
            return true;
        }
        return false;
    }

    public void queueEvent(FacesEvent e) {
        if (isImmediate()) {
            e.setPhaseId(PhaseId.APPLY_REQUEST_VALUES);
        } else {
            e.setPhaseId(PhaseId.INVOKE_APPLICATION);
        }
        super.queueEvent(e);
    }

    public void broadcast(FacesEvent event) throws AbortProcessingException {
        boolean wasRedirected = false;
        if (isImmediate()) {
            this.processValidators(getFacesContext());
            this.processUpdates(getFacesContext());
            super.broadcast(event);
            if (this.handleNavigation) {
                wasRedirected = this.handleNavigationActions(getFacesContext());
            }
            if (!wasRedirected) getFacesContext().renderResponse();
        } else {
            super.broadcast(event);
            if (this.handleNavigation) {
                wasRedirected = this.handleNavigationActions(getFacesContext());
            }
        }
    }

    public void broadcastAllEvents() {
        for (ComponentEvent aQueueEvent : queueEvent) {
            broadcast(aQueueEvent);
        }
        queueEvent.clear();
    }

    public void queueEvent(ComponentEvent event) {
        queueEvent.add(event);
    }

    public void addListener(ComponentEventListener listener) {
        if (!listeners.contains(listener)) listeners.add(listener);
    }

    public void removeAllListeners() {
        listeners.clear();
    }

    public void setPresentationEvent(int aType, String aValue) {
        Validation.validateValue(aValue, "Component : Event");
        fPresentationEvents.put(aType, aValue);
    }

    /**
     * {@inheritDoc}
     */
    public void setPresentationEvent(PresentationEvent aPresentationEvent) {
        setPresentationEvent(aPresentationEvent.getType(), aPresentationEvent.getValue());
    }

    /**
     * {@inheritDoc}
     */
    public boolean isPresentationEventFound(int aType) {
        return fPresentationEvents.containsKey(new Integer(aType));
    }

    /**
     * {@inheritDoc}
     */
    public String getPresentationEventValue(int aType) {
        return fPresentationEvents.get(new Integer(aType));
    }

    public void removePresentationEvent(int aType) {
        fPresentationEvents.remove(new Integer(aType));
    }

    class IteratorPresentationEvents implements Iterator {

        private Iterator<Integer> keys;

        IteratorPresentationEvents(Iterator<Integer> aKeys) {
            this.keys = aKeys;
        }

        public boolean hasNext() {
            return keys.hasNext();
        }

        public void remove() {
            keys.remove();
        }

        public Object next() {
            Integer key = keys.next();
            String value = fPresentationEvents.get(key);
            return new PresentationEvent(key, value);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Iterator getPresentationEvents() {
        return new IteratorPresentationEvents(fPresentationEvents.keySet().iterator());
    }

    protected String getInterfaceManagerProperty() {
        return null;
    }

    protected Class getInterfaceManagerClass() {
        return null;
    }

    protected String getActionFromRequest() {
        return RendererUtils.getActionFromRequest(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap(), getJSId(FacesContext.getCurrentInstance()));
    }

    protected String getActionDataFromRequest() {
        return RendererUtils.getActionDataFromRequest(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap(), getJSId(FacesContext.getCurrentInstance()));
    }

    public String getJSId(FacesContext context) {
        Object varName = getProperty("varName");
        String var = varName instanceof String ? (String) varName : getClientId(context);
        return FacesUtil.getNamespacedId(var.replaceAll(":", "").replaceAll("-", ""), context);
    }

    public void setProperty(String property, Object value) {
        properties.put(property, value);
        ValueExpression ve = getValueExpression(property);
        if (ve != null) try {
            ve.setValue(FacesContext.getCurrentInstance().getELContext(), value);
        } catch (Exception e) {
        }
    }

    public boolean isPropertySet(String property) {
        return (properties.containsKey(property) && properties.get(property) != null) || (getValueExpression(property) != null);
    }

    public List<String> getModifiedPropertiesList() {
        List<String> result = new ArrayList<String>();
        result.addAll(properties.keySet());
        return result;
    }

    public Object getProperty(String property) {
        return getProperty(property, null);
    }

    public boolean getProperty(String property, boolean defaultValue) {
        return (Boolean) getProperty(property, (Boolean) defaultValue);
    }

    public Object getPropertyEx(String property) {
        return getPropertyEx(property, null);
    }

    public Object getPropertyOnly(String property, Object defaultValue) {
        ValueExpression _ve = getValueExpression(property);
        if (_ve != null) {
            return _ve.getValue(getFacesContext().getELContext());
        }
        return defaultValue;
    }

    public Object getPropertyEx(String property, Object defaultValue) {
        ValueExpression _ve = getValueExpression(property);
        if (_ve != null) {
            Object value = _ve.getValue(getFacesContext().getELContext());
            if (value != null) setProperty(property, value);
            return value;
        }
        return getProperty(property, defaultValue);
    }

    public Object getProperty(String property, Object defaultValue) {
        if (properties.containsKey(property) && !property.equals("customFileName")) {
            return properties.get(property);
        }
        ValueExpression _ve = getValueExpression(property);
        if (_ve != null) {
            Object value = _ve.getValue(getFacesContext().getELContext());
            if (value != null) setProperty(property, value);
            return value;
        }
        if (getInterfaceManagerProperty() != null) {
            Object im;
            if (isPropertySet(getInterfaceManagerProperty())) {
                im = getProperty(getInterfaceManagerProperty());
            } else {
                im = getChildComponent(this, getInterfaceManagerClass());
            }
            if (im == null) {
                try {
                    im = getInterfaceManagerClass().newInstance();
                } catch (Exception e) {
                }
            }
            if (im != null) {
                try {
                    Method method = im.getClass().getMethod(new StringBuffer("get").append(property.substring(0, 1).toUpperCase()).append(property.substring(1)).toString());
                    return method.invoke(im);
                } catch (Exception e) {
                }
            }
        }
        if ((isPropertySet("model") && (getProperty("model") instanceof PropertiesHolder))) {
            Object modelProperty = ((PropertiesHolder) getProperty("model")).getProperty(property, defaultValue);
            if (modelProperty != null) {
                setProperty(property, modelProperty);
                return modelProperty;
            }
        }
        setProperty(property, defaultValue);
        return defaultValue;
    }

    public List<UIComponent> getChildComponents(UIComponent componentBase, Class childComponentClass) {
        List<UIComponent> result = new ArrayList<UIComponent>();
        if (componentBase == null) return result;
        if (componentBase.getChildren() != null) {
            for (UIComponent component : componentBase.getChildren()) {
                if (childComponentClass.isAssignableFrom(component.getClass())) {
                    result.add(component);
                }
                result.addAll(getChildComponents(component, childComponentClass));
            }
        }
        return result;
    }

    public UIComponent getChildComponent(UIComponent componentBase, Class childComponentClass) {
        List<UIComponent> result = getChildComponents(componentBase, childComponentClass);
        return (result != null) && (result.size() > 0) ? result.get(0) : null;
    }

    public void restoreState(FacesContext _context, Object _state) {
        this._state = (Object[]) _state;
        super.restoreState(_context, this._state[0]);
        properties = (Map<String, Object>) this._state[1];
    }

    public void clearProperties() {
        Map<String, Object> newProperties = new HashMap<String, Object>();
        for (String key : properties.keySet()) {
            if (getValueExpression(key) == null) newProperties.put(key, properties.get(key));
        }
        properties = newProperties;
    }

    public Object saveState(FacesContext _context) {
        if (_state == null) {
            _state = new Object[2];
        }
        _state[0] = super.saveState(_context);
        _state[1] = properties;
        return _state;
    }

    public String getComponentTypeName() {
        throw new IllegalStateException();
    }

    public static String getSelectionWasChangedFromRequestFlagName(WGFComponentBase component) {
        return component.getId() + SELECTION_MODEL_WAS_CHANGED_FROM_REQUEST;
    }

    public static String getDateWasChangedFromRequestFlagName(WGFComponentBase component) {
        return component.getId() + DATA_MODEL_WAS_CHANGED_FROM_REQUEST;
    }

    protected String getAdditionalActionDataFromRequest() {
        return RendererUtils.getAdditionalActionDataFromRequest(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap(), this.getId());
    }

    public static boolean isSelectionModelWasChangedFromRequest(WGFComponentBase component, HttpServletRequest request) {
        return request.getAttribute(getSelectionWasChangedFromRequestFlagName(component)) != null;
    }

    public static boolean isSelectionModelWasChangedFromRequest(WGFComponentBase component, Map paramMap) {
        return paramMap.get(getSelectionWasChangedFromRequestFlagName(component)).equals(Boolean.TRUE);
    }

    public static void setSelectionModelWasChangedFromRequest(WGFComponentBase component, HttpServletRequest request) {
        request.setAttribute(getSelectionWasChangedFromRequestFlagName(component), Boolean.TRUE);
    }

    public static void setSelectionModelWasChangedFromRequest(WGFComponentBase component, Map paramMap) {
        paramMap.put(getSelectionWasChangedFromRequestFlagName(component), Boolean.TRUE);
    }

    public static boolean isDataModelWasChangedFromRequest(WGFComponentBase component, HttpServletRequest request) {
        return request.getAttribute(getSelectionWasChangedFromRequestFlagName(component)) != null;
    }

    public static void setDataModelWasChangedFromRequest(WGFComponentBase component, HttpServletRequest request) {
        request.setAttribute(getSelectionWasChangedFromRequestFlagName(component), Boolean.TRUE);
    }

    public static boolean isDataModelWasChangedFromRequest(WGFComponentBase component, Map paramMap) {
        return paramMap.get(getSelectionWasChangedFromRequestFlagName(component)).equals(Boolean.TRUE);
    }

    public static void setDataModelWasChangedFromRequest(WGFComponentBase component, Map paramMap) {
        paramMap.put(getSelectionWasChangedFromRequestFlagName(component), Boolean.TRUE);
    }

    public static boolean isTreeStateWasChangedFromRequest(WGFComponentBase component, HttpServletRequest request) {
        return request.getAttribute(getSelectionWasChangedFromRequestFlagName(component)) != null;
    }

    protected void checkListener(FacesListener listener) {
        if ((listener != null) && (getFacesListeners(listener.getClass()).length == 0)) {
            addFacesListener(listener);
        }
    }
}
