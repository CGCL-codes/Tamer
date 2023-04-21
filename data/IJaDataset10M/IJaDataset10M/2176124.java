package org.openjf.container;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public class BoardContainer {

    private static final Logger logger = Logger.getLogger(BoardContainer.class);

    private boolean autoAdd;

    private Map aliases = new HashMap();

    /**
     * Explicitly added component classes
     */
    private Set components = new HashSet();

    /**
     * Instances keyed by component class
     */
    private Map instances = new HashMap();

    /**
     * Sorted components
     */
    private List sorted = new ArrayList();

    /**
     * Started component classes
     */
    private Set started = new HashSet();

    public BoardContainer() {
    }

    public void addComponentClass(Class clazz) {
        if (!components.contains(clazz)) {
            if (clazz.isInterface()) {
                String implName = clazz.getName() + "Impl";
                try {
                    clazz = Class.forName(implName);
                } catch (ClassNotFoundException e) {
                    throw new BoardContainerException(implName + " implementation class not found for interface " + clazz.getName());
                }
            }
            components.add(clazz);
        }
    }

    public boolean hasStoppedComponents() {
        return started.size() != components.size();
    }

    public Set getComponentClasses() {
        return new HashSet(components);
    }

    private Constructor getComponentConstructor(Class clazz) {
        Constructor constructors[] = clazz.getConstructors();
        if (constructors.length > 1) {
            throw new BoardContainerException("Component classes cannot have more than one constructor");
        }
        return constructors[0];
    }

    private Class[] getDependencies(Class clazz) {
        Constructor constructor = getComponentConstructor(clazz);
        Class[] result = constructor.getParameterTypes();
        for (int i = 0; i < result.length; i++) {
            Class rclazz = result[i];
            if (rclazz.isInterface()) {
                String implName = rclazz.getName() + "Impl";
                try {
                    result[i] = Class.forName(implName);
                } catch (ClassNotFoundException e) {
                    throw new BoardContainerException(implName + " implementation class not found for interface " + rclazz.getName());
                }
            }
        }
        return result;
    }

    private void sortDependencies() {
        Set toSort = new HashSet(components);
        Set alreadySorted = new HashSet();
        List sortedList = new ArrayList();
        Set toAdd = new HashSet();
        Set toRemove = new HashSet();
        while (!toSort.isEmpty()) {
            toRemove.clear();
            Iterator it = toSort.iterator();
            while (it.hasNext()) {
                Class clazz = (Class) it.next();
                Class dependencies[] = getDependencies(clazz);
                boolean allFound = dependencies.length == 0;
                if (!allFound) {
                    allFound = true;
                    for (int i = 0; i < dependencies.length; i++) {
                        if (!components.contains(dependencies[i]) && !alreadySorted.contains(dependencies[i])) {
                            if (autoAdd) {
                                toAdd.add(dependencies[i]);
                            } else {
                                throw new BoardContainerException("Dependency " + dependencies[i] + " not found for component " + clazz);
                            }
                        }
                        if (!alreadySorted.contains(dependencies[i])) {
                            allFound = false;
                            break;
                        }
                    }
                }
                if (allFound) {
                    sortedList.add(clazz);
                    alreadySorted.add(clazz);
                    toRemove.add(clazz);
                }
            }
            if (toRemove.isEmpty() && toAdd.isEmpty()) {
                StringBuffer sb = new StringBuffer();
                it = toSort.iterator();
                while (it.hasNext()) {
                    sb.append(((Class) it.next()).getName());
                    sb.append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                throw new BoardContainerException("Circular dependency detected: " + sb);
            }
            toSort.removeAll(toRemove);
            toSort.addAll(toAdd);
            toAdd.clear();
        }
        this.sorted.clear();
        this.sorted.addAll(sortedList);
    }

    public void instantiate() {
        createAliases();
        sortDependencies();
        Map tempInstances = new HashMap(instances);
        for (int i = 0; i < sorted.size(); i++) {
            Class clazz = (Class) sorted.get(i);
            if (instances.containsKey(clazz)) {
                continue;
            }
            Class deps[] = getDependencies(clazz);
            Object args[] = new Object[deps.length];
            for (int j = 0; j < deps.length; j++) {
                args[j] = tempInstances.get(deps[j]);
            }
            Constructor constr = getComponentConstructor(clazz);
            try {
                logger.info("Instantiating " + clazz.getName());
                tempInstances.put(clazz, constr.newInstance(args));
            } catch (IllegalArgumentException e) {
                throw new BoardContainerException(e);
            } catch (InstantiationException e) {
                throw new BoardContainerException(e);
            } catch (IllegalAccessException e) {
                throw new BoardContainerException(e);
            } catch (InvocationTargetException e) {
                throw new BoardContainerException(e);
            }
        }
        instances.clear();
        instances.putAll(tempInstances);
    }

    private void createAliases() {
        aliases.clear();
        Set usedMoreThanOnce = new HashSet();
        Iterator it = components.iterator();
        while (it.hasNext()) {
            Class clazz = (Class) it.next();
            Class superclass = clazz.getSuperclass();
            while (superclass != null) {
                if (aliases.containsKey(superclass)) {
                    aliases.remove(superclass);
                    usedMoreThanOnce.add(superclass);
                } else {
                    if (!usedMoreThanOnce.contains(superclass)) {
                        aliases.put(superclass, clazz);
                    }
                }
                superclass = superclass.getSuperclass();
            }
            Class interfaces[] = clazz.getInterfaces();
            for (int i = 0; i < interfaces.length; i++) {
                Class intf = interfaces[i];
                if (aliases.containsKey(intf)) {
                    aliases.remove(intf);
                    usedMoreThanOnce.add(intf);
                } else {
                    if (!usedMoreThanOnce.contains(intf)) {
                        aliases.put(intf, clazz);
                    }
                }
            }
        }
    }

    public void start() {
        sortDependencies();
        createAliases();
        instantiate();
        Set tempStarted = new HashSet();
        for (int i = 0; i < sorted.size(); i++) {
            Object obj = instances.get(sorted.get(i));
            if (obj instanceof ComponentLifecycle) {
                ComponentLifecycle lc = (ComponentLifecycle) obj;
                try {
                    Class clazz = obj.getClass();
                    if (!started.contains(clazz)) {
                        logger.info("Starting " + clazz.getName());
                        lc.start();
                        tempStarted.add(clazz);
                    }
                } catch (Exception e) {
                    stop(tempStarted);
                    throw new BoardContainerException("Error starting components", e);
                }
            }
        }
        started.addAll(tempStarted);
    }

    private void stop(Set whichOnes) {
        Exception firstException = null;
        for (int i = sorted.size() - 1; i >= 0; i--) {
            Object obj = instances.get(sorted.get(i));
            if (obj instanceof ComponentLifecycle) {
                ComponentLifecycle lc = (ComponentLifecycle) obj;
                try {
                    Class clazz = obj.getClass();
                    if (whichOnes.contains(clazz)) {
                        whichOnes.remove(clazz);
                        logger.info("Stopping " + clazz.getName());
                        lc.stop();
                    }
                } catch (Exception e) {
                    firstException = e;
                }
            }
        }
        if (firstException != null) {
            throw new BoardContainerException("Error stopping components", firstException);
        }
    }

    public void stop() {
        stop(started);
    }

    public Object getComponent(Class clazz) {
        Object res = getComponentNE(clazz);
        if (res == null) {
            throw new BoardContainerException("Could not find component " + clazz.getName());
        }
        return res;
    }

    public Object getComponentNE(Class clazz) {
        Object res = instances.get(clazz);
        if (res == null) {
            Object alias = aliases.get(clazz);
            if (alias != null) {
                res = instances.get(alias);
            }
        }
        return res;
    }

    public boolean getAutoAdd() {
        return autoAdd;
    }

    public void setAutoAdd(boolean autoAdd) {
        this.autoAdd = autoAdd;
    }
}
