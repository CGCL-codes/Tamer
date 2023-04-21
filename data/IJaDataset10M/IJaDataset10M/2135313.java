package jfun.yan.xml.nuts;

import java.beans.IntrospectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import jfun.yan.Component;
import jfun.yan.Components;
import jfun.yan.Creator;
import jfun.yan.FilteredPropertiesInjector;
import jfun.yan.Monad;
import jfun.yan.ParameterBinder;
import jfun.yan.PropertyBinder;
import jfun.yan.function.Signature;
import jfun.yan.util.NameFilteredMemberPredicate;
import jfun.yan.xml.Constants;
import jfun.yan.xml.NutsUtils;
import jfun.yan.xml.nut.ComponentNut;
import jfun.yan.xml.nut.Nut;

/**
 * Super class for any Nut that supports &lt;arg&gt;,
 * &lt;args&gt;,&lt;prop&gt; and &lt;props&gt; sub-elements.
 * <p>
 * @author Ben Yu
 * Nov 9, 2005 11:38:43 PM
 */
public abstract class ArgumentsAndPropertiesNut extends ComponentNut {

    private Class[] param_types;

    private Object[] args;

    private Args sub_args;

    private final java.util.List arglist = new ArrayList();

    private final HashMap table = new HashMap();

    private int max_arg_num = -1;

    private String param_autowire_str;

    private ParameterBinder param_autowire_mode;

    private void withMaxNum(int max) {
        if (max > max_arg_num) {
            this.max_arg_num = max;
        }
    }

    /**
   * Get the maximum number of the arguments.
   * @return the value of the maximal 1-based ordinal position of
   * arguments.
   * -1 is returned if no "args" or "arg" is specified.
   */
    public int getMaxArgsCount() {
        return max_arg_num;
    }

    public Object[] getArgs() {
        return args;
    }

    public List getArgList() {
        return arglist;
    }

    public void setArgs(Object[] args) {
        this.args = args;
        withMaxNum(args.length);
    }

    public Class[] getParameterTypes() {
        return param_types;
    }

    public void setParams(Class[] param_types) {
        this.param_types = param_types;
    }

    public void addArgs(Args a) {
        checkDuplicate("args", this.args);
        final int sz = arglist.size();
        for (int i = 0; i < sz; i++) {
            final Arg x = (Arg) arglist.get(i);
            if (x.getInd() < a.getArgs().length) {
                throw x.raise("overlapping index: " + x.getInd());
            }
        }
        this.args = a.getArgs();
        this.sub_args = a;
        withMaxNum(this.args.length);
    }

    public void addArg(Arg a) {
        if (args != null && a.getInd() < args.length) {
            throw a.raise("overlapping index: " + a.getInd());
        }
        if (table.containsKey(a)) {
            throw a.raise("duplicate index: " + a.getInd());
        }
        table.put(a, a);
        arglist.add(a);
        withMaxNum(a.getInd() + 1);
    }

    private final Component toComponent(Class type, Object obj) {
        if (obj instanceof Creator) {
            return Components.adapt((Creator) obj);
        } else return NutsUtils.asComponent(convert(type, obj));
    }

    /**
   * Apply the specified parameters if any.
   * @param component the component to apply parameters.
   * @return the result.
   */
    protected Component applyArguments(Component component) {
        if (args != null) {
            Nut n = this;
            if (sub_args != null) n = sub_args;
            checkIndex(n, args.length - 1);
        }
        final int sz = arglist.size();
        final HashMap indmap = new HashMap();
        for (int i = 0; i < sz; i++) {
            final Arg a = (Arg) arglist.get(i);
            checkIndex(a, a.getInd());
            indmap.put(new Integer(a.getInd()), a);
        }
        if (args != null || !arglist.isEmpty()) {
            component = component.bindArguments(new ParameterBinder() {

                public Creator bind(Signature src, int ind, Class type) {
                    if (args != null && ind < args.length) {
                        return toComponent(type, args[ind]);
                    }
                    final Arg a = (Arg) indmap.get(new Integer(ind));
                    if (a != null) {
                        Component val = a.getVal(type);
                        if (val == null) {
                            val = Components.value(null);
                        }
                        final Component def = a.getDefault(type);
                        if (def != null) {
                            return Monad.mplus(val, def);
                        } else return val;
                    }
                    return Components.useArgument(src, ind, type);
                }
            });
        }
        return component;
    }

    private void checkIndex(Nut nut, int ind) {
        if (param_types == null) return;
        if (ind >= param_types.length) {
            throw nut.raise("argument index out of bounds: " + ind);
        }
    }

    private String[] prop_names;

    private String prop_autowire_str;

    private final ArrayList prop_elements = new ArrayList();

    private PropertyBinder prop_autowire_mode;

    private boolean optional_properties = false;

    private boolean validate_property_names = true;

    /**
   * Is the a property explicitly specified?
   * @param key the property key.
   */
    public boolean containsExplicitProperty(String key) {
        if (prop_names != null) {
            for (int i = 0; i < prop_names.length; i++) {
                final String name = prop_names[i];
                if (name != null && name.equals(key)) {
                    return true;
                }
            }
        }
        final int sz = prop_elements.size();
        for (int i = 0; i < sz; i++) {
            final Prop prop = (Prop) prop_elements.get(i);
            if (prop.getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidate_property_names() {
        return validate_property_names;
    }

    public void setValidate_property_names(boolean check_property_names) {
        this.validate_property_names = check_property_names;
    }

    private static final String PROP_TAG_NAME = "prop";

    public void setProps(Map props) {
        int i = 0;
        for (Iterator it = props.keySet().iterator(); it.hasNext(); i++) {
            final Object key = it.next();
            final Object val = props.get(key);
            final Prop prop = new Prop();
            prop.initNutEnvironment(this.getNutEnvironment());
            prop.initTagLocation(this.getTagLocation());
            prop.initSequenceNumber(i);
            prop.initTagName(PROP_TAG_NAME);
            prop.setKey(key.toString());
            prop.setVal(val);
            addProp(prop);
        }
    }

    public void setAutowire(String mode) {
        this.prop_autowire_mode = getPropertyWiring(mode);
        this.prop_autowire_str = mode;
        this.param_autowire_mode = getParameterWiring(mode);
        this.param_autowire_str = mode;
    }

    /**
   * Get the specified autowire mode.
   */
    public PropertyBinder getAutoWire() {
        return prop_autowire_mode;
    }

    public void setProperty_names(String[] names) {
        prop_names = names;
        for (int i = 0; i < prop_names.length; i++) {
            prop_names[i] = prop_names[i].replace('-', '_');
        }
    }

    public String[] getPropertyNames() {
        return prop_names;
    }

    public void addProp(Prop prop) {
        prop_elements.add(prop);
    }

    public void setOptional_properties(boolean flag) {
        this.optional_properties = flag;
    }

    /**
   * Create a bean component and filter out the undesired properties.
   * @param c the Component to inject properties.
   * @param isAll whether we want all properties.
   * @return the bean Component.
   * @throws IntrospectionException when introspection failed.
   */
    protected Component defineBean(Component c, boolean isAll) throws IntrospectionException {
        if (isAll) {
            return c.bean();
        } else if (prop_names != null) {
            if (validate_property_names) {
                return c.bean(prop_names);
            } else {
                final Set propkeys = jfun.yan.util.Utils.toSet(this.prop_names, "property name");
                return Components.makeBean(c, FilteredPropertiesInjector.instance(c.getType(), new NameFilteredMemberPredicate(propkeys)));
            }
        } else if (getPropertyAutowireMode() != null) {
            return c.bean();
        } else {
            return c;
        }
    }

    /**
   * When no property names is specified, no property values are set,
   * do we by default create a bean component?
   */
    protected boolean isBeanByDefault() {
        return false;
    }

    /**
   * Create bean component is any property name is specified or any property
   * value is specified.
   * @param component the component to apply properties.
   * @return the result.
   */
    protected Component applyProperties(Component component) {
        final boolean isAll = isAllProperties();
        final HashMap valnames = checkPropertyNames(isAll);
        if (valnames == null && (prop_names == null || prop_names.length == 0) && !isBeanByDefault()) {
            return component;
        }
        try {
            component = defineBean(component, isAll);
        } catch (IntrospectionException e) {
            throw raise(e);
        }
        if (!prop_elements.isEmpty()) {
            component = component.bindProperties(new PropertyBinder() {

                public Creator bind(Class component_type, Object key, Class type) {
                    final Prop prop = (Prop) valnames.get(key);
                    if (prop != null) {
                        Component result = prop.getVal(type);
                        if (result == null) {
                            result = Components.value(null);
                        }
                        final Component def = prop.getDefault(type);
                        if (def != null) {
                            result = Monad.mplus(result, def);
                        } else if (prop.isOptional()) {
                            result = result.optional();
                        }
                        return result;
                    } else return Components.useProperty(component_type, key, type);
                }
            });
        }
        if (optional_properties) {
            component = component.optionalProperties();
        }
        final PropertyBinder autowiring = getPropertyAutowireMode();
        if (autowiring != null) {
            component = component.bindProperties(autowiring);
        }
        return component;
    }

    /**
   * In case the component implements any marker interface or has any specific
   * method signature that is used to automatically set property, call them
   * and set the corresponding properties.
   * @param c the component.
   * @return the Component object that informs the "aware"s.
   */
    protected Component informAwares(Component c) {
        return c;
    }

    /**
   * Get the auto wire mode for properties.
   * If not specified, use the global default setting.
   */
    public PropertyBinder getPropertyAutowireMode() {
        if (prop_autowire_str == null) {
            return getPropertyWiring(null);
        } else return prop_autowire_mode;
    }

    /**
   * Get the auto wire mode for parameters.
   * If not specified, use the global default setting.
   */
    public ParameterBinder getParameterAutowireMode() {
        if (param_autowire_str == null) {
            return getParameterWiring(null);
        } else return param_autowire_mode;
    }

    /**
   * Is wildcard used?
   */
    public boolean isAllProperties() {
        return (prop_names != null && prop_names.length == 1 && Constants.WILDCARD.equals(prop_names[0]));
    }

    /**
   * Check the validity of property names and property values.
   * populate property names if manual-wiring and property values are specified.
   * @param isAll whether wildcard is used for property-names.
   * @return null is returned if no property value is specified.
   * the key-value map is returned for the specified property values.
   */
    private HashMap checkPropertyNames(final boolean isAll) {
        HashSet prop_name_set = null;
        if (!isAll && prop_names != null) {
            prop_name_set = new HashSet(prop_names.length);
            for (int i = 0; i < prop_names.length; i++) {
                final String name = prop_names[i];
                if (prop_name_set.contains(name)) {
                    throw raise("duplicate property name: " + name);
                }
                prop_name_set.add(name);
            }
        }
        final int sz = prop_elements.size();
        if (sz == 0) return null;
        final HashMap valnames = new HashMap(sz);
        for (int i = 0; i < sz; i++) {
            final Prop prop = (Prop) prop_elements.get(i);
            final String key = prop.getKey();
            if (valnames.containsKey(key)) {
                throw prop.raise("duplicate property key: " + key);
            }
            if (this.validate_property_names && !isAll && prop_name_set != null) {
                if (!prop_name_set.contains(key)) {
                    throw prop.raise("unused property key: " + key);
                }
            }
            valnames.put(key, prop);
        }
        if (getPropertyAutowireMode() == null) {
            if (prop_names == null) {
                prop_names = new String[valnames.size()];
                valnames.keySet().toArray(prop_names);
            }
        }
        return valnames;
    }

    /**
   * Apply arguments and then properties to the component.
   * @param component the component to apply arguments and properties.
   * @return the result.
   */
    protected Component decorateComponent(Component component) {
        return applyProperties(applyArguments(informAwares(component)));
    }
}
