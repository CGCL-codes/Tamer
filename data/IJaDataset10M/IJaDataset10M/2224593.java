package play.view;

import higraph.view.SubgraphView;
import java.awt.Component;
import play.model.EdgePLAY;
import play.model.EdgePayloadPLAY;
import play.model.NodePLAY;
import play.model.NodePayloadPLAY;
import play.model.SubGraphPLAY;
import play.model.WholeGraphPLAY;

/**
 * @author  Charles
 */
public class SubGraphViewPLAY extends SubgraphView<NodePayloadPLAY, EdgePayloadPLAY, WholeGraphPLAY, SubGraphPLAY, NodePLAY, EdgePLAY> {

    /**
	 * @uml.property  name="sg"
	 * @uml.associationEnd  
	 */
    private SubGraphPLAY sg;

    private int id;

    protected SubGraphViewPLAY(ViewFactoryPLAY viewFactory, SubGraphPLAY theGraph, Component display) {
        super(viewFactory, theGraph, display);
        this.sg = theGraph;
        this.id = sg.registerView(this);
    }

    public SubGraphPLAY getSG() {
        return this.sg;
    }
}
