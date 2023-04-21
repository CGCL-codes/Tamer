package net.sourceforge.ondex.filter.relationtypeset;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import net.sourceforge.ondex.core.AbstractConcept;
import net.sourceforge.ondex.core.AbstractONDEXGraph;
import net.sourceforge.ondex.core.AbstractRelation;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.util.SparseBitSet;
import net.sourceforge.ondex.event.type.GeneralOutputEvent;
import net.sourceforge.ondex.event.type.WrongParameterEvent;
import net.sourceforge.ondex.filter.AbstractONDEXFilter;
import net.sourceforge.ondex.tools.ONDEXGraphCloner;
import net.sourceforge.ondex.workflow.args.ArgumentDefinition;
import net.sourceforge.ondex.workflow.args.BooleanArgumentDefinition;
import net.sourceforge.ondex.workflow.args.StringArgumentDefinition;
import net.sourceforge.ondex.workflow.args.StringMappingPairArgumentDefinition;

/**
 * Removes specified relation type set from the graph.
 * 
 * @version 01.02.2008
 * @author taubertj
 * 
 */
public class Filter extends AbstractONDEXFilter implements ArgumentNames {

    private ONDEXView<AbstractConcept> concepts = null;

    private ONDEXView<AbstractRelation> relations = null;

    private ONDEXView<AbstractConcept> inverseConcepts = null;

    private ONDEXView<AbstractRelation> inverseRelations = null;

    /**
	 * Constructor
	 */
    public Filter() {
    }

    @Override
    public void copyResultsToNewGraph(AbstractONDEXGraph exportGraph) {
        ONDEXGraphCloner graphCloner = new ONDEXGraphCloner(graph, exportGraph);
        ONDEXView<AbstractConcept> visibleConcepts = concepts.clone();
        while (visibleConcepts.hasNext()) {
            graphCloner.cloneConcept(visibleConcepts.next());
        }
        visibleConcepts.close();
        ONDEXView<AbstractRelation> visibleRelations = relations.clone();
        while (visibleRelations.hasNext()) {
            graphCloner.cloneRelation(visibleRelations.next());
        }
        visibleRelations.close();
    }

    @Override
    public ONDEXView<AbstractConcept> getVisibleConcepts() {
        return concepts.clone();
    }

    public ONDEXView<AbstractConcept> getInVisibleConcepts() {
        return inverseConcepts.clone();
    }

    @Override
    public ONDEXView<AbstractRelation> getVisibleRelations() {
        return relations.clone();
    }

    public ONDEXView<AbstractRelation> getInVisibleRelations() {
        return inverseRelations.clone();
    }

    /**
	 * Returns the name of this filter.
	 * 
	 * @return name
	 */
    public String getName() {
        return "RelationType Filter";
    }

    /**
	 * Returns the version of this filter.
	 * 
	 * @return version
	 */
    public String getVersion() {
        return "21.04.2008";
    }

    /**
	 * ArgumentDefinitions for RelationType, ConceptClass and CV
	 * restrictions.
	 * 
	 * @return three argument definition
	 */
    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        StringArgumentDefinition rtset_arg = new StringArgumentDefinition(TARGETRTSET_ARG, TARGETRTSET_ARG_DESC, true, null, true);
        StringMappingPairArgumentDefinition ccRestriction = new StringMappingPairArgumentDefinition(CONCEPTCLASS_RESTRICTION_ARG, CONCEPTCLASS_RESTRICTION_ARG_DESC, false, null, true);
        StringMappingPairArgumentDefinition cvRestriction = new StringMappingPairArgumentDefinition(CV_RESTRICTION_ARG, CV_RESTRICTION_ARG_DESC, false, null, true);
        BooleanArgumentDefinition removeTernariesArg = new BooleanArgumentDefinition(REMOVE_TERNARY_ARG, REMOVE_TERNARY_ARG_DESC, false, Boolean.TRUE);
        return new ArgumentDefinition<?>[] { rtset_arg, ccRestriction, cvRestriction, removeTernariesArg };
    }

    /**
	 * Filters the graph and constructs the lists for visible concepts and
	 * relations.
	 * 
	 */
    public void start() {
        Map<CV, HashSet<CV>> cvMapping = getAllowedCVs(graph);
        Map<ConceptClass, HashSet<ConceptClass>> ccMapping = getAllowedCCs(graph);
        boolean removeTernaries = (Boolean) args.getUniqueValue(REMOVE_TERNARY_ARG);
        HashSet<RelationType> filterOnRtSet = new HashSet<RelationType>();
        Object[] rtsets = super.args.getObjectValueArray(TARGETRTSET_ARG);
        if (rtsets != null && rtsets.length > 0) {
            for (Object rtset : rtsets) {
                String id = ((String) rtset).trim();
                RelationType relationTypeSet = graph.getONDEXGraphData().getRelationType(id);
                if (relationTypeSet != null) {
                    filterOnRtSet.add(relationTypeSet);
                    fireEventOccurred(new GeneralOutputEvent("Added RelationType " + relationTypeSet.getId(), "[Filter - setONDEXGraph]"));
                } else {
                    fireEventOccurred(new WrongParameterEvent(id + " is not a valid RelationType.", "[Filter - setONDEXGraph]"));
                }
            }
        } else {
            fireEventOccurred(new WrongParameterEvent("No target RelationType(s) given.", "[Filter - setONDEXGraph]"));
        }
        concepts = graph.getConcepts();
        inverseConcepts = new ONDEXView<AbstractConcept>(graph, AbstractConcept.class, new SparseBitSet());
        relations = graph.getRelations();
        Iterator<RelationType> it = filterOnRtSet.iterator();
        while (it.hasNext()) {
            RelationType rtset = it.next();
            if (cvMapping.size() == 0 && ccMapping.size() == 0 && removeTernaries) {
                inverseRelations = graph.getRelationsOfRelationType(rtset);
                relations = ONDEXView.andNot(relations, inverseRelations);
            } else {
                HashSet<Integer> filter = new HashSet<Integer>();
                ONDEXView<AbstractRelation> itr = graph.getRelationsOfRelationType(rtset);
                while (itr.hasNext()) {
                    AbstractRelation r = itr.next();
                    AbstractConcept fromConcept = r.getFromConcept();
                    AbstractConcept toConcept = r.getToConcept();
                    CV fromCV = fromConcept.getElementOf();
                    CV toCV = toConcept.getElementOf();
                    if (cvMapping.size() > 0 && !cvMapping.get(fromCV).contains(toCV)) {
                        continue;
                    }
                    ConceptClass fromCC = fromConcept.getOfType();
                    ConceptClass toCC = toConcept.getOfType();
                    if (ccMapping.size() > 0 && !ccMapping.get(fromCC).contains(toCC)) {
                        continue;
                    }
                    if (!removeTernaries && r.getQualifier() != null) {
                        continue;
                    }
                    filter.add(r.getId());
                }
                itr.close();
                inverseRelations = new ONDEXView<AbstractRelation>(graph, AbstractRelation.class, new SparseBitSet(filter));
                relations = ONDEXView.andNot(relations, inverseRelations);
            }
        }
    }

    /**
	 * An indexed graph is not required.
	 * 
	 * @return false
	 */
    public boolean requiresIndexedGraph() {
        return false;
    }

    public String[] requiresValidators() {
        return new String[0];
    }
}
