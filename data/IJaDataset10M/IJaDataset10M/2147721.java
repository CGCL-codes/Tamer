package royere.cwi.layout;

import royere.cwi.db.OidFactory;
import royere.cwi.structure.Element;
import royere.cwi.structure.Graph;

/**
 * Dummy element that CompoundLayout uses to denote the end
 * of a subgraph in its element traversal.
 *
 * @see CompoundLayout 
 * @author yugen
 */
public class EndOfContextMarker extends Element {

    /** The metanode that defines the context */
    Graph metanode = null;

    /**
   * Constructor.
   *
   * @param metanode the subgraph
   */
    public EndOfContextMarker(Graph metanode) {
        this.metanode = metanode;
        this.setOid(OidFactory.newOid());
    }

    public Graph getMetanode() {
        return metanode;
    }

    public String toString() {
        return "EndOfContextMarker(" + metanode + ")";
    }
}
