package net.sourceforge.ondex.ovtk2.reusable_functions;

import java.util.BitSet;
import java.util.Set;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.util.BitSetFunctions;
import net.sourceforge.ondex.ovtk2.graph.ONDEXJUNGGraph;
import net.sourceforge.ondex.ovtk2.ui.OVTK2Viewer;

/**
 * 
 * @author lysenkoa
 *
 */
public class Sorting {

    /**
	 * 
	 * @param osg - JUNG graph
	 * @param or - set of relations to filter
	 * @param aog - abstract ondex graph
	 * @return a filtered set containing only visible relations
	 */
    public static Set<ONDEXRelation> getVisibleRelations(ONDEXJUNGGraph osg, Set<ONDEXRelation> or, ONDEXGraph aog) {
        return BitSetFunctions.andNot(or, getVisibleRelations(osg, aog));
    }

    /**
	 * 
	 * @param osg - JUNG graph
	 * @param oc - set of concepts to filter
	 * @param aog - abstract ondex graph
	 * @return a filtered set containing only visible concepts
	 */
    public static Set<ONDEXConcept> getVisibleConcepts(ONDEXJUNGGraph osg, Set<ONDEXConcept> oc, ONDEXGraph aog) {
        return BitSetFunctions.and(oc, getVisbleConcepts(osg, aog));
    }

    /**
	 * 
	 * @param osg - JUNG graph
	 * @param aog - abstract ondex graph
	 * @return a filtered set containing only visible relations
	 */
    public static Set<ONDEXRelation> getVisibleRelations(ONDEXJUNGGraph osg, ONDEXGraph aog) {
        BitSet bits = new BitSet(osg.getEdges().size());
        for (ONDEXRelation edge : osg.getEdges()) {
            if (osg.isVisible(edge)) {
                bits.set(edge.getId());
            }
        }
        return BitSetFunctions.create(aog, ONDEXRelation.class, bits);
    }

    /**
	 * 
	 * @param osg - JUNG graph
	 * @param aog - abstract ondex graph
	 * @return a filtered set containing only visible concepts
	 */
    public static Set<ONDEXConcept> getVisbleConcepts(ONDEXJUNGGraph osg, ONDEXGraph aog) {
        BitSet bits = new BitSet();
        for (ONDEXConcept node : osg.getVertices()) {
            if (osg.isVisible(node)) {
                bits.set(node.getId());
            }
        }
        return BitSetFunctions.create(aog, ONDEXConcept.class, bits);
    }

    /**
	 * Gets an Set of picked concepts in the viewer
	 * @param viewer - viewer
	 * @return view
	 */
    public static Set<ONDEXConcept> getPickedNodes(OVTK2Viewer viewer) {
        Set<ONDEXConcept> nodes = viewer.getVisualizationViewer().getPickedVertexState().getPicked();
        BitSet set = new BitSet(nodes.size());
        for (ONDEXConcept n : nodes) {
            set.set(n.getId());
        }
        return BitSetFunctions.create(viewer.getONDEXJUNGGraph(), ONDEXConcept.class, set);
    }

    /**
	 * Gets an Set of picked relations in the viewer
	 * @param viewer - viewer
	 * @return view
	 */
    public static Set<ONDEXRelation> getPickedEdges(OVTK2Viewer viewer) {
        Set<ONDEXRelation> edges = viewer.getVisualizationViewer().getPickedEdgeState().getPicked();
        BitSet set = new BitSet(edges.size());
        for (ONDEXRelation e : edges) {
            set.set(e.getId());
        }
        return BitSetFunctions.create(viewer.getONDEXJUNGGraph(), ONDEXRelation.class, set);
    }
}
