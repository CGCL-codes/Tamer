package net.sourceforge.ondex.xten.functions;

import java.util.Collection;
import java.util.List;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXGraph;
import net.sourceforge.ondex.core.ONDEXGraphMetaData;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.base.ONDEXViewImpl;
import net.sourceforge.ondex.core.util.ONDEXViewFunctions;
import net.sourceforge.ondex.core.util.SparseBitSet;

/**
 * THis class contains a library of methods to simplify construction of
 * view according to the extended set of conditions.
 * 
 * @author lysenkoa
 *
 */
public class ViewConstruction {

    /**
	 * Constructs the view containing all relations associated with particular 
	 * concept class, using the string ids to define the class
	 * @param graph - graph to operate on 
	 * @param types - string ids of teh concept classes
	 * determine which relation  to include in the view.
	 * 
	 * @return - resulting view
	 */
    public static ONDEXView<ONDEXRelation> getRelationsOfConceptClasses(ONDEXGraph graph, List<String> ccs) {
        ONDEXView<ONDEXRelation> result = new ONDEXViewImpl<ONDEXRelation>(graph, ONDEXRelation.class, new SparseBitSet());
        ONDEXGraphMetaData meta = graph.getMetaData();
        for (String cc : ccs) {
            ConceptClass ct = meta.getConceptClass(cc);
            if (ct != null) {
                result = ONDEXViewFunctions.or(result, graph.getRelationsOfConceptClass(ct));
            }
        }
        return result;
    }

    /**
	 * Return all of the concept of concept classes with provided ids in the graph
	 * @param graph - Ondex graph
	 * @param strIds - id of concept classes
	 * 
	 * @return view
	 */
    public static final ONDEXView<ONDEXConcept> getConceptsOfTypes(ONDEXGraph graph, Collection<String> strIds) {
        ONDEXView<ONDEXConcept> result = new ONDEXViewImpl<ONDEXConcept>(graph, ONDEXConcept.class, new SparseBitSet());
        ConceptClass[] ccs = ControledVocabularyHelper.convertConceptClasses(graph, strIds);
        for (ConceptClass cc : ccs) {
            result = ONDEXViewFunctions.or(result, graph.getConceptsOfConceptClass(cc));
        }
        return result;
    }

    /**
	 * Return all of the relations of relation types with provided ids in the graph
	 * @param graph - Ondex graph
	 * @param strIds - id of relation types
	 * 
	 * @return view
	 */
    public static final ONDEXView<ONDEXRelation> getRelationsOfTypes(ONDEXGraph graph, Collection<String> strIds) {
        ONDEXView<ONDEXRelation> result = new ONDEXViewImpl<ONDEXRelation>(graph, ONDEXRelation.class, new SparseBitSet());
        RelationType[] rts = ControledVocabularyHelper.convertRelationTypes(graph, strIds);
        for (RelationType rt : rts) {
            ONDEXViewFunctions.or(result, graph.getRelationsOfRelationType(rt));
        }
        return result;
    }
}
