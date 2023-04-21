package com.intel.gui.controls2.configurable;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JSplitPane;
import com.intel.gpe.client2.defaults.IPreferences;
import com.intel.gpe.client2.defaults.preferences.INode;
import com.intel.gpe.client2.defaults.preferences.KeyImpl;

/**
 * @version $Id: ConfigurableSplitPane.java,v 1.7 2007/02/28 15:30:24 dizhigul Exp $
 * @author Denis Zhigula
 */
public class ConfigurableSplitPane extends JSplitPane implements IConfigurable {

    private List<IConfigurable> children = new ArrayList<IConfigurable>();

    private IConfigurable parent;

    private INode node;

    public ConfigurableSplitPane(IConfigurable parent, INode node) {
        super();
        this.parent = parent;
        this.node = node;
        this.parent.addChild(this);
    }

    public ConfigurableSplitPane(IConfigurable parent, INode node, int newOrientation, Component c1, Component c2) {
        super(newOrientation, c1, c2);
        this.parent = parent;
        this.node = node;
        this.parent.addChild(this);
    }

    public void load(IPreferences preferences) {
        String defaultValue = preferences.get(new KeyImpl(node, GUIKeys.splitPosition.getKey()));
        int split = 0;
        if (defaultValue != null) {
            split = new Integer(preferences.get(new KeyImpl(node, GUIKeys.splitPosition.getKey())));
        }
        setDividerLocation(split);
        for (IConfigurable child : children) {
            child.load(preferences);
        }
    }

    public void store(IPreferences preferences) {
        preferences.set(new KeyImpl(node, GUIKeys.splitPosition.getKey()), Integer.toString(getDividerLocation()));
        for (IConfigurable child : children) {
            child.store(preferences);
        }
    }

    public void addChild(IConfigurable child) {
        Iterator<IConfigurable> thisChildren = children.iterator();
        while (thisChildren.hasNext()) {
            IConfigurable thisChild = thisChildren.next();
            if (thisChild.getNode() == null) {
                return;
            }
            if (thisChild.getNode().equals(child.getNode())) {
                return;
            }
        }
        children.add(child);
    }

    public INode getNode() {
        return node;
    }
}
