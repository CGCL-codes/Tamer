package org.viewer.graph;

import java.util.Hashtable;
import java.util.Properties;

/**

 * A GraphElement is the basic ancestor of anything that can appear in graph be

 * it a Node, Edge or Cluster.  

 */
public abstract class GraphElement {

    public GraphElement() {
    }

    /**

   * add this element to a particular owner cluster

   * 

   * @param c

   *          the new owner

   */
    public void setOwner(Cluster c) {
        if (owner != null) {
            owner.remove(this);
        }
        owner = c;
    }

    /**

   * get the parent cluster or owner of this graph element

   */
    public Cluster getOwner() {
        return owner;
    }

    /**

   * draw the element if it's visible

   */
    public void draw() {
        if (visible) view.draw();
    }

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        if (visible && view != null) {
            visible = false;
            view.hide();
        }
    }

    public void show(org.viewer.view.GraphCanvas graphCanvas) {
        if (!visible && view != null) {
            visible = true;
            view.show(graphCanvas);
        }
    }

    /**

   * The following method may be used to associate data with a graph element.

   * Try not to use this too much or things will get slow and messy.

   * An example usage can be seen in 

   * @param key

   *          usually, the key should be a string

   * @param value reference to your user data

   */
    public void storeUserData(Object key, Object value) {
        if (userData == null) {
            userData = new Hashtable();
        }
        userData.put(key, value);
    }

    /**

   * The graphElement maintains a hashtable allowing the user to associate it

   * with miscellaneous data. Use this method to access it.

   * @see org.viewer.graphanalysis.plugin.BiconnectedComponents for an example usage

   * @param key

   * @return the data object

   */
    public Object getUserData(Object key) {
        if (userData != null) {
            return userData.get(key);
        }
        return null;
    }

    /**

   * delete the element, removing all references to it, forever!

   */
    public abstract void delete();

    /**

   * Any properties loaded or saved in files con be stored in a Properties

   * object. This method is called to extract the GraphElement's properties when

   * the file is created.

   * 

   * @return Custom properties associated with this GraphElement.

   */
    public abstract Properties getProperties();

    public abstract void setProperties(Properties p);

    protected Viewable view;

    protected Layable layout;

    protected boolean visible = false;

    protected Cluster owner;

    private Hashtable userData;
}
