package org.candango.myfuses.core;

import java.net.URL;
import java.util.HashMap;

public abstract class AbstractCircuit implements Circuit {

    /**
	 * Circuit Action hash map
	 */
    private HashMap<String, CircuitAction> actionMap = new HashMap<String, CircuitAction>();

    private Application application;

    /**
	 * Circuit name
	 */
    private String name;

    /**
	 * Circuit path
	 */
    private String path;

    /**
	 * Circuit package
	 */
    private String pack;

    public CircuitAction[] getActions() {
        CircuitAction[] actions = new CircuitAction[actionMap.size()];
        return actionMap.values().toArray(actions);
    }

    public void addAction(CircuitAction action) {
        actionMap.put(action.getName(), action);
        action.setCircuit(this);
    }

    public CircuitAction getAction(String name) {
        return actionMap.get(name);
    }

    public void clearActions() {
        actionMap.clear();
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        if (getPackage() == null) {
            return null;
        }
        URL url = this.getClass().getClassLoader().getResource(getPackage().replace('.', '/'));
        if (url == null) {
            return null;
        }
        return url.getPath();
    }

    public String getPackage() {
        return pack;
    }

    public void setPackage(String name) {
        pack = name;
    }
}
