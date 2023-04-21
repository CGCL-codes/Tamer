package edu.gsbme.gyoza2d.GraphGenerator;

import java.util.Observable;
import org.w3c.dom.Element;
import edu.gsbme.MMLParser2.Factory.ParserFactory;
import edu.gsbme.gyoza2d.GraphGenerator.GraphGenerator.LayoutRouting;
import edu.gsbme.gyoza2d.GraphGenerator.Layout.ILayoutAlgorithm;

/**
 * 
 * A view state contains an object that can be visualised by a graph, it contains the layout route and algorithm information.
 * 
 * Graph2D Configuraiton Object. UI may observe this object. The function NotifyChange() must be called by an algorithm that alters
 * this object to notify other UI to update their ui.
 * 
 * @author David
 *
 */
public class ViewStateConfig extends Observable {

    private LayoutStructure structure;

    private ParserFactory factory;

    private Element defaultElement;

    private Element prevElement;

    private Element currentElement;

    private LayoutRouting currentLayoutRouting;

    private ILayoutAlgorithm currentLayoutAlgorithm;

    public String subgraph;

    public int level;

    public boolean setEditable;

    double zoom = 1;

    public void setLayoutStructure(LayoutStructure structure) {
        this.structure = structure;
    }

    public LayoutStructure getLayoutStructure() {
        return structure;
    }

    public void setFactory(ParserFactory factory) {
        this.factory = factory;
    }

    public ParserFactory getFactory() {
        return factory;
    }

    public void setDefaultElement(Element element) {
        this.defaultElement = element;
    }

    public Element getDefaultElement() {
        return defaultElement;
    }

    public void setPrevElement(Element element) {
        this.prevElement = element;
    }

    public Element getPrevElement() {
        return prevElement;
    }

    public void setCurrentElement(Element element) {
        this.currentElement = element;
    }

    public Element getCurrentElement() {
        return currentElement;
    }

    public void setCurrentLayoutRouting(LayoutRouting route) {
        this.currentLayoutRouting = route;
    }

    public LayoutRouting getCurrentLayoutRounting() {
        return currentLayoutRouting;
    }

    public void setCurrentLayoutAlgorithm(ILayoutAlgorithm layout) {
        this.currentLayoutAlgorithm = layout;
    }

    public ILayoutAlgorithm getCurrentLayoutAlgorithm() {
        return currentLayoutAlgorithm;
    }

    public void NotifyChange() {
        setChanged();
        notifyObservers();
    }

    public double getZoomLevel() {
        return zoom;
    }

    public void setZoomLevel(double zoom) {
        this.zoom = zoom;
    }

    public LayoutStructure getStructure() {
        return structure;
    }

    public ViewStateConfig clone() {
        ViewStateConfig clone = new ViewStateConfig();
        clone.factory = this.getFactory();
        clone.structure = this.getLayoutStructure();
        clone.currentElement = this.getCurrentElement();
        clone.currentLayoutAlgorithm = this.getCurrentLayoutAlgorithm();
        clone.currentLayoutRouting = this.getCurrentLayoutRounting();
        return clone;
    }

    public void dispose() {
        structure = null;
        factory = null;
        defaultElement = null;
        prevElement = null;
        currentElement = null;
        currentLayoutAlgorithm = null;
        currentLayoutRouting = null;
    }
}
