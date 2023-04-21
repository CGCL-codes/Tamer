package com.gorillalogic.faces.components.glui.trinidad;

import com.gorillalogic.faces.components.glui.GLUIComponentBase;
import com.gorillalogic.faces.components.glui.GLUISelectOne;
import com.gorillalogic.faces.components.glui.GLUISelectOne.Item;
import com.gorillalogic.faces.util.FacesUtils;
import org.apache.myfaces.trinidad.component.UIXValue;
import org.apache.myfaces.trinidad.component.UIXSelectOne;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectOneListbox;
import org.apache.myfaces.trinidad.component.core.input.CoreSelectItem;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.event.FacesListener;
import javax.faces.event.ValueChangeListener;
import javax.faces.render.Renderer;

public class TriniGLUISelectOne extends GLUISelectOne {

    public Object getValue() {
        return _wrapped.getValue();
    }

    public void setValue(Object value) {
        _wrapped.setValue(value);
    }

    public void setValueBinding(String key, ValueBinding vb) {
        _wrapped.setValueBinding(key, vb);
    }

    public Object getLocalValue() {
        return _wrapped.getLocalValue();
    }

    public Object getSubmittedValue() {
        return _wrapped.getSubmittedValue();
    }

    public void addValueChangeListener(ValueChangeListener listener) {
        _wrapped.addValueChangeListener(listener);
    }

    public void setImmediate(boolean immediate) {
        _wrapped.setImmediate(immediate);
    }

    public boolean isImmediate() {
        return _wrapped.isImmediate();
    }

    public void addItem(Item i) {
        if (i == null) {
            return;
        }
        CoreSelectItem item = new CoreSelectItem();
        if (i.label != null) {
            item.setLabel(i.label);
        }
        if (i.value != null) {
            item.setValue(i.value);
        }
        if (i.binding != null) {
            item.setValue(i.binding.getValue(FacesUtils.getFc()));
        }
        if (i.id != null) {
            item.setId(i.id);
        }
        _wrapped.getChildren().add(item);
    }

    public TriniGLUISelectOne() {
        _wrapped = new UISelectOneExtension(this);
    }

    protected UIComponent getComponent() {
        return _wrapped;
    }

    private UISelectOneExtension _wrapped;

    private class UISelectOneExtension extends CoreSelectOneListbox implements GLUIComponentExtension {

        GLUIComponentBase _wrapper = null;

        UISelectOneExtension(GLUISelectOne wrapper) {
            _wrapper = wrapper;
        }

        public GLUIComponentBase getWrapper() {
            return _wrapper;
        }

        public void addFacesListenerX(FacesListener arg0) {
            addFacesListener(arg0);
        }

        public FacesListener[] getFacesListenersX(Class arg0) {
            return getFacesListeners(arg0);
        }

        public void removeFacesListenerX(FacesListener arg0) {
            removeFacesListener(arg0);
        }

        public FacesContext getFacesContextX() {
            return getFacesContext();
        }

        public Renderer getRendererX(FacesContext arg0) {
            return getRenderer(arg0);
        }
    }
}
