package org.proteomecommons.MSExpedite.Graph;

import java.util.LinkedList;
import javax.swing.InputMap;
import javax.swing.JRootPane;

public abstract class AbstractGraphController implements IGraphController {

    protected IObjectDrawer objRenderer = null;

    protected Graph graph = null;

    protected JRootPane rootPane = null;

    private LinkedList<IKeyUserListener> keyUserListener = new LinkedList<IKeyUserListener>();

    protected LinkedList<IToggleButton> listeners = new LinkedList<IToggleButton>();

    protected boolean isOn = false;

    public AbstractGraphController() {
    }

    public AbstractGraphController(Graph graph, IObjectDrawer oDrawer) {
        set(oDrawer);
        set(graph);
    }

    public void setButtonsSelected(IToggleButton caller) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).setSelected(caller, isOn);
        }
    }

    public AbstractGraphController(Graph graph, IObjectDrawer oDrawer, JRootPane rootPane) {
        set(oDrawer);
        set(graph);
        set(rootPane);
    }

    public void set(JRootPane rootPane) {
        this.rootPane = rootPane;
    }

    public void add(IToggleButton list) {
        listeners.add(list);
    }

    public void fireKeyUserStateChanged(String state, InputMap im) {
        KeyUserEvent e = new KeyUserEvent(this, im, rootPane, state);
        for (int i = 0; i < keyUserListener.size(); i++) {
            keyUserListener.get(i).keyUserStateChanged(e);
        }
    }

    public void set(IObjectDrawer oDrawer) {
        this.objRenderer = oDrawer;
    }

    public void set(Graph graph) {
        this.graph = graph;
    }

    public void add(IKeyUserListener list) {
        keyUserListener.add(list);
    }

    public void remove(IToggleButton button) {
        listeners.remove(button);
    }

    public void remove(IKeyUserListener list) {
        this.keyUserListener.remove(list);
    }

    public boolean isOn() {
        return isOn;
    }

    public abstract void enable(boolean b);
}
