package net.bervini.rasael.mathexplorer.swing;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.beans.*;
import java.io.Serializable;
import net.bervini.rasael.mathexplorer.Tree.MathNode;
import net.bervini.rasael.mathexplorer.Tree.TreeIcon;
import net.bervini.rasael.mathexplorer.Tree.TreeRendererEngine;

/**
 *
 * @author Rasael
 */
public class MathTreeViewComponent extends RasaelImagePanel implements Serializable, MouseMotionListener {

    /**
     *
     */
    public static final String PROP_SAMPLE_PROPERTY = "sampleProperty";

    private String sampleProperty;

    private PropertyChangeSupport propertySupport;

    private TreeIcon tree;

    /**
     *
     */
    public MathTreeViewComponent() {
        propertySupport = new PropertyChangeSupport(this);
    }

    /**
     *
     * @return
     */
    public String getSampleProperty() {
        return sampleProperty;
    }

    /**
     *
     * @param value
     */
    public void setSampleProperty(String value) {
        String oldValue = sampleProperty;
        sampleProperty = value;
        propertySupport.firePropertyChange(PROP_SAMPLE_PROPERTY, oldValue, sampleProperty);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * @return the tree
     */
    public TreeIcon getTree() {
        return tree;
    }

    /**
     * @param tree the tree to set
     */
    public void setTree(TreeIcon tree) {
        this.tree = tree;
        updateTree();
    }

    /**
     *
     * @param tree
     */
    public void setFormula(MathNode tree) {
        setTree(TreeRendererEngine.drawTree(tree));
    }

    private void updateTree() {
        setImage(TreeRendererEngine.createTreeImage(tree));
    }

    public void mouseDragged(MouseEvent e) {
    }

    public void mouseMoved(MouseEvent e) {
        TreeIcon icon = TreeRendererEngine.findNodeAtPosition(getTree(), e.getX(), e.getY());
        if (icon == null) {
            return;
        }
        updateTree();
    }
}
