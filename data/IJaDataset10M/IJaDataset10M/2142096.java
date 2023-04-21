package se.sics.mspsim.util;

import java.io.PrintStream;
import java.util.ArrayList;

public class ComponentRegistry {

    private ArrayList<ComponentEntry> components = new ArrayList<ComponentEntry>();

    private boolean running = false;

    public void registerComponent(String name, Object component) {
        synchronized (this) {
            components.add(new ComponentEntry(name, component));
        }
        if (component instanceof ActiveComponent) {
            ((ActiveComponent) component).init(name, this);
            if (running) {
                ((ActiveComponent) component).start();
            }
        } else if (component instanceof ServiceComponent) {
            ((ServiceComponent) component).init(name, this);
        }
    }

    public synchronized Object getComponent(String name) {
        for (int i = 0, n = components.size(); i < n; i++) {
            if (name.equals(components.get(i).name)) {
                return components.get(i).component;
            }
        }
        return null;
    }

    public synchronized Object[] getAllComponents(String name) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0, n = components.size(); i < n; i++) {
            ComponentEntry entry = components.get(i);
            if (name.equals(entry.name)) {
                list.add(entry.component);
            }
        }
        return list.toArray();
    }

    @SuppressWarnings("unchecked")
    public synchronized Object getComponent(Class name) {
        for (int i = 0, n = components.size(); i < n; i++) {
            if (name.isAssignableFrom(components.get(i).component.getClass())) {
                return components.get(i).component;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public synchronized Object[] getAllComponents(Class name) {
        ArrayList<Object> list = new ArrayList<Object>();
        for (int i = 0, n = components.size(); i < n; i++) {
            Object component = components.get(i).component;
            if (name.isAssignableFrom(component.getClass())) {
                list.add(component);
            }
        }
        return list.toArray((Object[]) java.lang.reflect.Array.newInstance(name, list.size()));
    }

    public void start() {
        ComponentEntry[] plugs;
        synchronized (this) {
            running = true;
            plugs = components.toArray(new ComponentEntry[components.size()]);
        }
        for (int i = 0; i < plugs.length; i++) {
            if (plugs[i].component instanceof ActiveComponent) {
                ((ActiveComponent) plugs[i].component).start();
            }
        }
    }

    private static class ComponentEntry {

        public final String name;

        public final Object component;

        private ComponentEntry(String name, Object component) {
            this.name = name;
            this.component = component;
        }
    }

    public void printRegistry(PrintStream out) {
        ComponentEntry[] plugs = components.toArray(new ComponentEntry[components.size()]);
        out.printf("%-22s %s\n", "Component Name", "Component Class");
        out.println("----------------------------------------------");
        for (int i = 0; i < plugs.length; i++) {
            out.printf("%-22s %s\n", plugs[i].name, plugs[i].component.getClass().getName());
        }
    }
}
