package shoppingCartFrame;

import java.util.Enumeration;
import java.util.*;
import javax.swing.JPanel;

public class PanelRegistry extends Observable {

    protected static PanelRegistry registry;

    protected Hashtable panels;

    protected int nextUnique;

    protected PanelRegistry() {
        panels = new Hashtable();
        nextUnique = 0;
    }

    public static synchronized PanelRegistry instance() {
        if (registry == null) {
            registry = new PanelRegistry();
        }
        return registry;
    }

    public synchronized void addPanel(String name, JPanel newPanel) {
        panels.put(name, newPanel);
        setChanged();
        notifyObservers(new PanelRegistryEvent(PanelRegistryEvent.ADD_PANEL, name, newPanel));
    }

    public synchronized void addPanel(String name, JPanel newPanel, boolean uniqueName) {
        if (!uniqueName && (panels.get(name) != null)) {
            name = name + "<" + nextUnique + ">";
            nextUnique++;
        }
        panels.put(name, newPanel);
        setChanged();
        notifyObservers(new PanelRegistryEvent(PanelRegistryEvent.ADD_PANEL, name, newPanel));
    }

    public synchronized void removePanel(JPanel panel) {
        Enumeration e = panels.keys();
        while (e.hasMoreElements()) {
            Object key = e.nextElement();
            if (panels.get(key) == panel) {
                panels.remove(key);
                setChanged();
                notifyObservers(new PanelRegistryEvent(PanelRegistryEvent.REMOVE_PANEL, (String) key, panel));
                return;
            }
        }
    }

    public synchronized void removePanel(String name) {
        JPanel panel = (JPanel) panels.get(name);
        if (panel == null) return;
        panels.remove(name);
        setChanged();
        notifyObservers(new PanelRegistryEvent(PanelRegistryEvent.REMOVE_PANEL, name, panel));
    }

    public JPanel findPanel(String name) {
        return (JPanel) panels.get(name);
    }

    public Enumeration getFrames() {
        return panels.elements();
    }
}
