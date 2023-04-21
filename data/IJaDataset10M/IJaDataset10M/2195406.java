package pl.voidsystems.yajf.servlet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import pl.voidsystems.yajf.components.*;

/**
 * Class managing actions, component registration and component synchronization between requests.
 * 
 * @author rlitman
 */
public class ComponentRegistry {

    /**
     * Container that keeps IActionComponent->IActionHandler pairs
     */
    protected class ActionEntry {

        protected IActionComponent comp;

        protected IActionHandler handler;
    }

    /**
     * List of registered action components and action handlers
     * 
     * @uml.property name="registered_actions"
     * @uml.associationEnd multiplicity="(0 -1)"
     *                     inverse="this$0:pl.voidsystems.yajf.servlet.ComponentRegistry$ActionEntry"
     */
    protected ArrayList<ActionEntry> registered_actions = new ArrayList<ActionEntry>();

    /**
     * Values returned by action handlers
     * 
     * @uml.property name="action_results"
     * @uml.associationEnd qualifier="full_action_name:java.lang.String java.lang.String"
     */
    protected HashMap<String, String> action_results = new HashMap<String, String>();

    /**
     * List of all registered components from current request
     * 
     * @uml.property name="curr_components"
     * @uml.associationEnd multiplicity="(0 -1)" elementType="java.util.HashMap"
     *                     qualifier="getNameWithPath:java.lang.String pl.voidsystems.yajf.components.IComponent"
     */
    protected HashMap<String, IComponent> curr_components = new HashMap<String, IComponent>();

    /**
     * List of all registered components from previous request
     * 
     * @uml.property name="prev_components"
     * @uml.associationEnd qualifier="getNameWithPath:java.lang.String pl.voidsystems.yajf.components.IComponent"
     */
    protected HashMap<String, IComponent> prev_components = new HashMap<String, IComponent>();

    /**
     * All components' properties that have been changed by action handlers
     * 
     * @uml.property name="updated_props"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected PropertyKeeper updated_props = new PropertyKeeper();

    /**
     * Temporary container for tracking property changes
     * 
     * @uml.property name="temp_props"
     * @uml.associationEnd multiplicity="(1 1)"
     */
    protected PropertyKeeper temp_props = new PropertyKeeper();

    /**
     * List of components that want to have their "run() method called before registration and painting
     */
    protected List<IRunnableComponent> runnableComponents = new ArrayList<IRunnableComponent>();

    private static Log log = LogFactory.getLog(ComponentRegistry.class);

    /**
     * Not to be used directly. Use getReference() instead.
     */
    protected ComponentRegistry() {
    }

    /**
     * This method should be run at the start of the request. Runs all registered actions and prepares property
     * synchronization
     */
    @SuppressWarnings("unchecked")
    public void newRequestStarted() {
        this.prev_components = (HashMap<String, IComponent>) this.curr_components.clone();
        this.curr_components.clear();
        this.action_results.clear();
        this.runnableComponents.clear();
        preparePropertiesSync();
        processActions();
        filterProperties();
        this.registered_actions.clear();
    }

    /**
     * Creates a list of properties that have been changed by action handlers
     */
    protected void filterProperties() {
        log.info("Filtering properties...");
        for (IComponent comp : this.prev_components.values()) {
            Method[] methods = comp.getClass().getMethods();
            for (Method getter : methods) {
                ASynchronized sync = getter.getAnnotation(ASynchronized.class);
                if (sync != null && !getter.isAnnotationPresent(ANotSynchronized.class)) {
                    String comp_name = comp.getNameWithPath();
                    String prop_name = getter.getName();
                    Object new_value = callGetter(comp, getter);
                    Object old_value = this.temp_props.getProp(comp_name, prop_name);
                    if ((old_value != new_value) && (old_value == null || !old_value.equals(new_value))) {
                        this.updated_props.setProp(comp_name, prop_name, new_value);
                        log.info("    updated : " + comp_name + "/" + prop_name);
                        log.info("  old value : " + old_value);
                        log.info("  new value : " + new_value);
                    }
                }
            }
        }
    }

    /**
     * Remembers all component properties, so their changes can be noticed after running action handlers
     */
    protected void preparePropertiesSync() {
        log.info("Preparing properties synchronization...");
        this.temp_props.clear();
        for (IComponent comp : this.prev_components.values()) {
            boolean is_posted = comp instanceof IPostable && ((IPostable) comp).isPosted();
            Method[] methods = comp.getClass().getMethods();
            for (Method getter : methods) {
                ASynchronized sync = getter.getAnnotation(ASynchronized.class);
                if (sync != null && !getter.isAnnotationPresent(ANotSynchronized.class)) {
                    if (sync.clearType() == ClearPropertyType.CLEAR_ALWAYS || (sync.clearType() == ClearPropertyType.CLEAR_ON_POST && is_posted)) {
                        Object value = guessDefaultPropertyValue(getter.getReturnType());
                        callSetter(comp, sync.setter(), getter.getReturnType(), value);
                        this.updated_props.removeProp(comp.getNameWithPath(), getter.getName());
                    }
                    if (sync.clearType() == ClearPropertyType.NULL_ALWAYS || (sync.clearType() == ClearPropertyType.NULL_ON_POST && is_posted)) {
                        callSetter(comp, sync.setter(), getter.getReturnType(), null);
                        this.updated_props.removeProp(comp.getNameWithPath(), getter.getName());
                    }
                    Object value = callGetter(comp, getter);
                    this.temp_props.setProp(comp.getNameWithPath(), getter.getName(), value);
                }
            }
        }
    }

    /**
     * Returns a most used default value for a given type or <code>null</code> if type is not accepted
     * 
     * @param type
     * @return a most used default value for a given type or <code>null</code> if type is not accepted
     */
    private Object guessDefaultPropertyValue(Class<?> type) {
        if (type == String.class) {
            return "";
        }
        if (type == int.class || type == Integer.class) {
            return new Integer(0);
        }
        if (type == boolean.class || type == Boolean.class) {
            return Boolean.FALSE;
        }
        if (type == float.class || type == Float.class) {
            return new Float(0);
        }
        if (type == double.class || type == Double.class) {
            return new Double(0);
        }
        return null;
    }

    /**
     * Calls property getter, ignoring all exceptions
     * 
     * @param comp
     *            a component
     * @param getter
     *            a component property getter
     * @return value returned by getter or null if any problems were encountered
     */
    protected Object callGetter(IComponent comp, Method getter) {
        Object tmp[] = {};
        Object value = null;
        try {
            value = getter.invoke(comp, tmp);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * Runs all registered actions from posted components
     */
    protected void processActions() {
        log.info("Processing actions...");
        for (Iterator iter = this.registered_actions.iterator(); iter.hasNext(); ) {
            ActionEntry entry = (ActionEntry) iter.next();
            if (entry.comp.isPosted()) {
                String result = entry.handler.processAction(entry.comp);
                if (result != null) {
                    String entry_name = entry.handler.getClass().getSimpleName() + "." + entry.comp.getAction();
                    this.action_results.put(entry_name, result);
                    log.info("Action " + entry_name + " returned " + result);
                }
            }
        }
    }

    /**
     * Main component property synchronization. Copies all property values changed in action handlers to components in
     * current request
     */
    public void synchronize() {
        for (Iterator iter = this.updated_props.keySet().iterator(); iter.hasNext(); ) {
            String comp_name = (String) iter.next();
            if (!this.curr_components.containsKey(comp_name)) {
                iter.remove();
            }
        }
        for (Iterator iter = this.curr_components.values().iterator(); iter.hasNext(); ) {
            IComponent element = (IComponent) iter.next();
            synchronizeComponent(element);
        }
    }

    /**
     * Synchronizes component properties with values set by action handlers and/or remembered by the
     * <code>storeProperty</code>.
     * 
     * @param comp
     *            A component to be synchronized
     */
    public void synchronizeComponent(IComponent comp) {
        IComponent other = this.prev_components.get(comp.getNameWithPath());
        if (other == null) {
            return;
        }
        Method[] methods = comp.getClass().getMethods();
        for (Method getter : methods) {
            ASynchronized sync = getter.getAnnotation(ASynchronized.class);
            if (sync != null && !getter.isAnnotationPresent(ANotSynchronized.class)) {
                String comp_name = comp.getNameWithPath();
                String prop_name = getter.getName();
                if (!this.updated_props.hasProp(comp_name, prop_name)) {
                    continue;
                }
                Object value = this.updated_props.getProp(comp_name, prop_name);
                log.info("Syncing " + comp_name + '/' + prop_name);
                callSetter(comp, sync.setter(), getter.getReturnType(), value);
            }
        }
    }

    /**
     * Calls the property setter ignoring any exceptions
     * 
     * @param comp
     *            a component
     * @param setter_name
     *            a setter method name
     * @param type
     *            type of the setter parameter
     * @param value
     *            a value to be passed to the setter
     */
    protected void callSetter(IComponent comp, String setter_name, Class<?> type, Object value) {
        Class[] arg_types = { type };
        try {
            Method setter;
            setter = comp.getClass().getMethod(setter_name, arg_types);
            callSetter(comp, setter, value);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Calls the property setter ignoring any exceptions
     * 
     * @param comp
     *            a component
     * @param setter
     *            a setter method
     * @param value
     *            a value to be passed to the setter
     */
    protected void callSetter(IComponent comp, Method setter, Object value) {
        Object[] args = { value };
        try {
            setter.invoke(comp, args);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Use this method to obtain session-unique reference to a ComponentRegistry object
     * 
     * @return Returns the reference to session-unique singleton
     */
    public static ComponentRegistry getReference() {
        HttpSession sess = ServletDispatcher.getRequest().getSession();
        ComponentRegistry result = (ComponentRegistry) sess.getAttribute("ComponentRegistry");
        if (result == null) {
            result = new ComponentRegistry();
            sess.setAttribute("ComponentRegistry", result);
        }
        return result;
    }

    /**
     * Registers the action component -> action handler pair. The action handler will be called upon next request if
     * given component is posted
     * 
     * @param comp
     *            a component, that generates actions
     * @param handler
     *            an action handler object
     */
    public void registerAction(IActionComponent comp, IActionHandler handler) {
        for (ActionEntry entry : this.registered_actions) {
            if (entry.comp == comp && entry.handler == handler) {
                log.error("Duplicate action registration :" + comp.getNameWithPath() + " handler:" + handler.getClass().getSimpleName());
                ServletDispatcher.addMessage("Duplicate action registration. Check server log for details.");
                return;
            }
        }
        ActionEntry entry = new ActionEntry();
        entry.comp = comp;
        entry.handler = handler;
        this.registered_actions.add(entry);
    }

    /**
     * Registers component, so it can be accessed in action handlers in next request
     * 
     * @param comp
     *            a component to be registered
     */
    public void registerComponent(IComponent comp) {
        if (comp == null) {
            throw new NullPointerException("Trying to register null component");
        }
        String name = comp.getName();
        if (name == null) {
            throw new NullPointerException("Trying to register a component with null name");
        }
        if (name.length() > 0) {
            String nameWithPath = comp.getNameWithPath();
            IComponent prev = this.curr_components.put(nameWithPath, comp);
            if (prev != null && comp.getClass().isAnnotationPresent(Anonymous.class)) {
                log.warn("Duplicate component registration: " + nameWithPath);
            }
        }
    }

    /**
     * Checks if action with given name was processed by an action handler
     * 
     * @param handler
     *            Handler object
     * @param action_name
     *            Short action name (as defined by an IActionComponent)
     * @return <code>true</code> if action with given name was handled.
     */
    public boolean wasActionFired(IActionHandler handler, String action_name) {
        String full_action_name = handler.getClass().getSimpleName() + "." + action_name;
        log.info("Checking action " + full_action_name);
        return this.wasActionFired(full_action_name);
    }

    /**
     * Checks if action with given name was processed by an action handler
     * 
     * @param full_action_name
     *            Action name in "class_name.action_name" format
     * @return <code>true</code> if action with given name was handled.
     */
    public boolean wasActionFired(String full_action_name) {
        return this.action_results.containsKey(full_action_name);
    }

    /**
     * Returns value passed from an action handler that processed an action with given name. Can be <code>null</code>
     * when value was null or action handler returned null. Use <code>wasActionFired</code> to differentiate these
     * situations.
     * 
     * @param handler
     *            Handler object
     * @param action_name
     *            Short action name (as defined by an IActionComponent)
     * @return value passed from an action handler or null if no action with given name was handled
     */
    public String getActionResult(IActionHandler handler, String action_name) {
        return getActionResult(handler.getClass().getSimpleName() + "." + action_name);
    }

    /**
     * Returns value passed from an action handler that processed an action with given name. Can be <code>null</code>
     * when value was null or action handler returned null. Use <code>wasActionFired</code> to differentiate these
     * situations.
     * 
     * @param full_action_name
     *            Action name in "class_name.action_name" format
     * @return value passed from an action handler or null if no action with given name was handled
     */
    public String getActionResult(String full_action_name) {
        return this.action_results.get(full_action_name);
    }

    /**
     * Stores a component's property with given name so it can be synchronized in the next request.
     * 
     * @param component
     * @param getter_name
     */
    public void storeProperty(IComponent component, String getter_name) {
        this.updated_props.setProp(component.getNameWithPath(), getter_name, callGetter(component, getter_name));
    }

    /**
     * Calls property getter ignoring all exceptions
     * 
     * @param component
     * @param getter_name
     * @return Value returned by getter or null
     */
    private Object callGetter(IComponent component, String getter_name) {
        Class[] empty_params = {};
        try {
            Method getter = component.getClass().getMethod(getter_name, empty_params);
            return callGetter(component, getter);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Adds component to a collection. All components from that collection will have method "run()" called before
     * component registration process.
     * 
     * @param component
     *            A component to be registered. Must not be null.
     */
    public void registerRunnable(IRunnableComponent component) {
        this.runnableComponents.add(component);
    }

    /**
     * Runs "run()" methods on all components that called have been passed to registerRunnable earlier.
     */
    public void runRegistered() {
        try {
            for (IRunnableComponent component : this.runnableComponents) {
                component.run();
            }
        } catch (ComponentStructureException e) {
            e.printStackTrace();
        }
    }
}
