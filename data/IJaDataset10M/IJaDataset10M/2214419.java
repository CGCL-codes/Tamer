package longhall.core;

import java.util.*;

public class Configuration {

    private NameContext context;

    private Configuration lowerLayer;

    private String componentName;

    private String className;

    private String description;

    private String origin;

    private HashMap assignments = new HashMap();

    private ArrayList listeners = new ArrayList();

    public Configuration(NameContext context) {
        this(context, null);
    }

    public Configuration(NameContext context, Configuration lowerLayer) {
        this.context = context;
        this.lowerLayer = lowerLayer;
    }

    public NameContext getContext() {
        return this.context;
    }

    public void setClassName(String s) {
        this.className = s;
    }

    public String getClassName() {
        return this.className;
    }

    public void setComponentName(String newComponentName) {
        this.componentName = newComponentName;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public void setDescription(String newDescription) {
        this.description = newDescription;
    }

    public String getDescription() {
        return this.description;
    }

    public void setOrigin(String newOrigin) {
        this.origin = newOrigin;
    }

    public String getOrigin() {
        return this.origin;
    }

    public Configuration getLowerLayer() {
        return lowerLayer;
    }

    public synchronized Assigner getAssignment(String name) {
        return (Assigner) assembleAssignments().get(name);
    }

    public synchronized Iterator assignments() {
        return assembleAssignments().values().iterator();
    }

    protected HashMap assembleAssignments() {
        HashMap allAssignments = new HashMap();
        addAssignments(allAssignments);
        return allAssignments;
    }

    protected void addAssignments(HashMap allAssignments) {
        if (lowerLayer != null) {
            lowerLayer.addAssignments(allAssignments);
        }
        allAssignments.putAll(assignments);
    }

    public synchronized void putLiteral(String name, Object value) {
        doPut(name, new LiteralAssigner(name, value));
    }

    public synchronized void putComponent(String name, String componentName) {
        doPut(name, new ComponentAssigner(name, componentName));
    }

    public synchronized void copyFrom(String name, String componentName, String propertyName) {
        doPut(name, new CopyPropertyAssigner(name, componentName, propertyName));
    }

    public synchronized void putArray(String name, ArrayAssigner array) {
        doPut(name, array);
    }

    protected void doPut(String name, Assigner a) {
        a.setContext(context);
        assignments.put(name, a);
    }

    public synchronized void addListener(String eventName, String componentName) {
        listeners.add(new AddListener(getContext(), eventName, componentName));
    }

    public synchronized Iterator listeners() {
        ArrayList allListeners = new ArrayList();
        addListeners(allListeners);
        return allListeners.iterator();
    }

    protected void addListeners(ArrayList allListeners) {
        if (lowerLayer != null) {
            lowerLayer.addListeners(allListeners);
        }
        allListeners.addAll(listeners);
    }

    public synchronized void stackOn(Configuration that) {
        if (this.lowerLayer != null) {
            this.lowerLayer.stackOn(that);
        } else {
            this.lowerLayer = that;
        }
    }
}
