package org.zkoss.bind.impl;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Binder;
import org.zkoss.bind.sys.Binding;
import org.zkoss.xel.ExpressionX;
import org.zkoss.zk.ui.Component;

/**
 * Base implementation for implementing a {@link Binding}
 * @author henrichen
 * @since 6.0.0
 */
public class BindingImpl implements Binding, Serializable {

    private static final long serialVersionUID = 1463169907348730644L;

    private WeakReference<Component> _comp;

    private final Binder _binder;

    private final Map<String, Object> _args;

    protected BindingImpl(Binder binder, Component comp, Map<String, Object> args) {
        _comp = new WeakReference<Component>(comp);
        _binder = binder;
        _args = args;
    }

    public Component getComponent() {
        Object comp = _comp == null ? null : _comp.get();
        if (comp == null && _comp != null) {
            _comp = null;
        }
        return (Component) comp;
    }

    public Binder getBinder() {
        return _binder;
    }

    public Map<String, Object> getArgs() {
        return _args;
    }

    protected String getPureExpressionString(ExpressionX expr) {
        if (expr == null) {
            return null;
        }
        final String evalstr = expr.getExpressionString();
        return evalstr.substring(2, evalstr.length() - 1);
    }

    protected Object setAttribute(BindContext ctx, Object key, Object value) {
        Map<Object, Object> bindingBag = getBindingAttribute(ctx);
        if (bindingBag == null) {
            bindingBag = new HashMap<Object, Object>();
            ctx.setAttribute(this, bindingBag);
        }
        return bindingBag.put(key, value);
    }

    protected Object getAttribute(BindContext ctx, Object key) {
        Map<Object, Object> bindingBag = getBindingAttribute(ctx);
        return bindingBag != null ? bindingBag.get(key) : null;
    }

    protected boolean containsAttribute(BindContext ctx, Object key) {
        Map<Object, Object> bindingBag = getBindingAttribute(ctx);
        return bindingBag != null ? bindingBag.containsKey(key) : false;
    }

    @SuppressWarnings("unchecked")
    private Map<Object, Object> getBindingAttribute(BindContext ctx) {
        return (Map<Object, Object>) ctx.getAttribute(this);
    }
}
