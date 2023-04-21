package vilaug.peirce;

import jung.ext.icon.IconVertexType;
import jung.ext.icon.IconVertex;
import jung.ext.icon.IconElementChecker;
import jung.ext.utils.BasicUtils;
import java.util.Set;
import java.util.Iterator;

public class DefaultPeirceElementChecker extends IconElementChecker implements PeirceElementChecker {

    private static final String ID = DefaultPeirceElementChecker.class.getName();

    /**
	 * The function <code>checkRelation</code> returns true iff 
	 * <code>getHeart</code> is from the intericon type.
   * 
   * TODO: check cycles
	 */
    public boolean checkRelation(PeirceRelation relation) {
        if (relation.getHeart().getType() != IconVertexType.INTERICON) return false;
        if (relation.getInterpretant().getType() == IconVertexType.INTERICON) return false;
        if (relation.getReferent().getType() == IconVertexType.INTERICON) return false;
        if (relation.getRepresentamen().getType() == IconVertexType.INTERICON) return false;
        if (!checkEdge(relation.getRepresentamen(), relation.getReferent())) return false;
        if (!checkEdge(relation.getInterpretant(), relation.getReferent())) return false;
        return true;
    }

    public boolean containsRelation(PeirceRelation relation) {
        Set potentialHearts = BasicUtils.intersection(relation.getInterpretant().getSuccessors(), relation.getRepresentamen().getSuccessors());
        Set actualHearts = BasicUtils.intersection(potentialHearts, relation.getReferent().getPredecessors());
        return !actualHearts.isEmpty();
    }

    public boolean uniqueRelation(PeirceRelation relation) {
        Set potentialHearts = BasicUtils.intersection(relation.getInterpretant().getSuccessors(), relation.getRepresentamen().getSuccessors());
        Set actualHearts = BasicUtils.intersection(potentialHearts, relation.getReferent().getPredecessors());
        if (actualHearts.isEmpty()) return true;
        for (Iterator i = actualHearts.iterator(); i.hasNext(); ) {
            IconVertex vertex = (IconVertex) i.next();
            if (vertex.getTooltip().equals(relation.getHeart().getTooltip())) return false;
        }
        return true;
    }

    /**
	 * Only normal icons may receive a property of visibility.
	 */
    public boolean checkVisibleness(IconVertex vertex) {
        if (vertex.getType() == IconVertexType.INTERICON) return false;
        return true;
    }

    public String getName() {
        return BasicUtils.afterDot(ID);
    }
}
