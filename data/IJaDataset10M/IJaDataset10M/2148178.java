package com.yfaces.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.component.UIComponentBase;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import com.yfaces.bean._YFJSBean;

public abstract class YComponent extends UIComponentBase implements ClientBehaviorHolder {

    protected Map<String, List<ClientBehavior>> clientBehaviors;

    enum PropertyKeys {

        value
    }

    /**
	 * Get the YUI dependencies for this component.
	 * 
	 * @return
	 */
    public abstract Collection<String> getYUIDependencies();

    protected abstract String getScript();

    protected _YFJSBean getDependencyBean() {
        FacesContext fCon = FacesContext.getCurrentInstance();
        _YFJSBean b = (_YFJSBean) fCon.getApplication().getELResolver().getValue(fCon.getELContext(), null, "_YFJSBean");
        return b;
    }

    protected String generateId() {
        return getClientId() + "yf_" + System.currentTimeMillis() + this.hashCode();
    }

    @Override
    public void addClientBehavior(String eventName, ClientBehavior behavior) {
        if (clientBehaviors == null) clientBehaviors = new HashMap<String, List<ClientBehavior>>();
        List<ClientBehavior> behaviors = clientBehaviors.get(eventName);
        if (behaviors == null) behaviors = new ArrayList<ClientBehavior>();
        behaviors.add(behavior);
    }

    @Override
    public Map<String, List<ClientBehavior>> getClientBehaviors() {
        return clientBehaviors;
    }

    @Override
    public abstract String getDefaultEventName();

    @Override
    public Collection<String> getEventNames() {
        return clientBehaviors.keySet();
    }

    @Override
    public String getFamily() {
        return YFaces.ComponentFamily;
    }

    public void setValue(Object value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public Object getValue() {
        return getStateHelper().get(PropertyKeys.value);
    }
}
