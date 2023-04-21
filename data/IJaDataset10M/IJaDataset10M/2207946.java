package uchicago.src.sim.gui;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of the GraphLayout interface. This implements
 * basic list operations, but no layout algorithm. Sub-classes are expected
 * to implement updateLayout() to do the actual layout of the nodes in the
 * nodeList. In addition subclasses may override the empty ActionListener
 * implementation provided here.
 *
 * @author Nick Collier
 * @version $Revision: 1.6 $ $Date: 2004/11/03 19:50:59 $
 */
public abstract class AbstractGraphLayout implements GraphLayout {

    protected ArrayList nodeList;

    protected int width, height;

    protected boolean update = true;

    public AbstractGraphLayout(int width, int height) {
        nodeList = new ArrayList();
        this.width = width;
        this.height = height;
    }

    public AbstractGraphLayout(List nodes, int width, int height) {
        this.width = width;
        this.height = height;
        nodeList = new ArrayList(nodes);
    }

    /**
   * Gets the height of the area on which to layout the graph.
   */
    public int getHeight() {
        return height;
    }

    /**
   * Gets the width of the area on which to layout the graph.
   */
    public int getWidth() {
        return width;
    }

    /**
   * Sets the list of nodes to be laid out by this GraphLayout.
   */
    public void setList(List listOfNodes) {
        nodeList.clear();
        nodeList.addAll(listOfNodes);
    }

    /**
   * Appends a list of nodes to the current list of nodes to be laid
   * out by this GraphLayout.
   */
    public void appendToList(List listOfNodes) {
        nodeList.addAll(listOfNodes);
    }

    /**
   * Appends the specified nodes to the list of nodes to be laid out by
   * this GraphLayout.
   */
    public void appendToList(DrawableNonGridNode node) {
        nodeList.add(node);
    }

    /**
   * Gets the list of nodes.
   */
    public ArrayList getNodeList() {
        return nodeList;
    }

    public void setUpdate(boolean doUpdate) {
        update = doUpdate;
    }

    public boolean getUpdate() {
        return update;
    }

    /**
   * Empty implementation of ActionListener interface. Sub-classes can
   * override this to provide their own implementations.
   */
    public void actionPerformed(ActionEvent evt) {
    }

    /**
   * Updates the layout of this graph by setting the x, y coordinate of
   * each DrawableNonGridNode in the current list of nodes.
   */
    public abstract void updateLayout();
}
