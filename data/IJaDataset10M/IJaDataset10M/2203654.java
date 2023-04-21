package org.wct;

import java.util.*;

/** 
 * This class is a superclass of all container components of WCT
 * (components that can have other components inside)
 * @author  juliano
 * @version 0.1
 */
public abstract class Container extends Component {

    /**
    The list of components 
  **/
    private Component[] complist;

    /**
    The number of components in this container  
  **/
    private int ncomps;

    /**
      LayoutManager used to lay out components in this container
    **/
    private LayoutManager lm;

    /** Utility field used by bound properties. */
    private java.beans.PropertyChangeSupport propertyChangeSupport = new java.beans.PropertyChangeSupport(this);

    /** Creates new Container */
    public Container() {
        super();
        complist = new Component[16];
        lm = new BorderLayout();
    }

    /**
    Adds a component to this container, at the specified index and with the associated 
    constraints object.
  @param comp The component to add
  @param cons The constraints object
  @param index the position inside the container where the component should be placed.
  **/
    public void add(Component comp, Object cons, int index) {
        if (index > ncomps || (index < 0 && index != -1)) throw new IllegalArgumentException("The index parameter(" + index + ") is not within container bounds!");
        if (comp instanceof Container) {
            for (Container c = this; c != null; c = c.parent) {
                if (c == comp) throw new IllegalArgumentException("Adding a container parent to the container, or adding the container to itself");
            }
        }
        if (ncomps == complist.length) {
            Component temp[] = new Component[2 * ncomps];
            System.arraycopy(complist, 0, temp, 0, complist.length);
            complist = temp;
        }
        if (index != -1 && index != ncomps) System.arraycopy(complist, index, complist, index + 1, ncomps - index);
        if (index == -1) index = ncomps;
        complist[index] = comp;
        ncomps++;
        lm.addLayoutComponent(comp, cons);
        if (comp.parent != null) comp.parent.remove(comp);
        comp.parent = this;
    }

    /**
    Adds a component with the specified constraints to the end of the component list
  **/
    public void add(Component c, Object constraints) {
        add(c, constraints, -1);
    }

    /**
    * Adds a component without any constraints to the end of the component list
    */
    public void add(Component c) {
        add(c, null, -1);
    }

    /**
    Removes the specified component from this container.Do nothing if the component is not inside this container.
  **/
    public void remove(Component c) {
        for (int i = 0; i < ncomps; i++) {
            if (complist[i] == c) remove(i);
        }
    }

    /**
    Removes the component at the specified index
  **/
    public void remove(int index) {
        if (index >= ncomps) throw new IllegalArgumentException("The supplied index(" + index + ") isn't within the container bounds");
        Component c = complist[index];
        c.parent = null;
        if (index < ncomps - 1) System.arraycopy(complist, index + 1, complist, index, ncomps - 1 - index);
        ncomps--;
        lm.removeLayoutComponent(c);
    }

    /**
   Return the number of components inside this container
   **/
    public int getComponentCount() {
        return ncomps;
    }

    /**
   Return the nth component of this container.The indexing is zero-based.
   **/
    public Component getComponent(int index) {
        if (index >= ncomps) throw new IllegalArgumentException("The index parameter(" + index + ") is out of the bounds of the container");
        return complist[index];
    }

    /** Add a PropertyChangeListener to the listener list.
   * @param l The listener to add.
 */
    public void addPropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.addPropertyChangeListener(l);
    }

    /** Removes a PropertyChangeListener from the listener list.
   * @param l The listener to remove.
 */
    public void removePropertyChangeListener(java.beans.PropertyChangeListener l) {
        propertyChangeSupport.removePropertyChangeListener(l);
    }

    /** Returns the current layout manager
   * @return Value of property layoutManager.
 */
    public LayoutManager getLayoutManager() {
        return lm;
    }

    /** Sets the current layout manager
   * @param layoutManager New value of property layoutManager.
 */
    public void setLayoutManager(LayoutManager layoutManager) {
        LayoutManager oldLayoutManager = this.lm;
        this.lm = layoutManager;
        propertyChangeSupport.firePropertyChange("layoutManager", oldLayoutManager, lm);
    }
}
