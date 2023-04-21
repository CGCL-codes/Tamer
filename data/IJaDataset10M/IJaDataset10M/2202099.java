package com.potix.zk.ui;

import java.util.Map;
import java.util.HashMap;
import com.potix.zk.ui.ext.PostCreate;
import com.potix.zk.ui.ext.DynamicPropertied;

/**
 * The implemetation of a macro component upon HTML.
 *
 * <p>Generally, a macro component is created automatically by ZK loader.
 * If a developer wants to create it manually, it has to instantiate from
 * the correct class, and then invoke {@link #postCreate}.
 *
 * @author <a href="mailto:tomyeh@potix.com">tomyeh@potix.com</a>
 */
public class HtmlMacroComponent extends HtmlBasedComponent implements PostCreate, IdSpace, DynamicPropertied {

    private Map _attrs = new HashMap(7);

    public HtmlMacroComponent() {
        _attrs.put("includer", this);
    }

    public void postCreate() {
        getDesktop().getExecution().createComponents(getMillieu().getMacroURI(this), this, _attrs);
    }

    public Object clone() {
        final HtmlMacroComponent clone = (HtmlMacroComponent) super.clone();
        clone._attrs = new HashMap(clone._attrs);
        return clone;
    }

    public boolean hasDynamicProperty(String name) {
        return _attrs.containsKey(name);
    }

    public Object getDynamicProperty(String name) {
        return _attrs.get(name);
    }

    public void setDynamicProperty(String name, Object value) throws WrongValueException {
        _attrs.put(name, value);
    }
}
