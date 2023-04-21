package gnumler.gui;

import gme.kernel.mapping.Mappable;
import org.jgraph.graph.CellView;
import org.jgraph.graph.GraphModel;

/**
 * Every element to be represented in the graph (which is a DefaultGraphCell)
 * must implement this interface.
 * @author silvia
 *
 */
public interface GUIElement {

    /**
	 * 
	 * @return the view to be associated with the given GUIElement
	 */
    public CellView getView(GraphModel model);

    public Mappable getRepresentedElement();
}
