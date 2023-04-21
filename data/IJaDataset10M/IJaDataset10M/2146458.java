package net.sourceforge.ondex.statistics.gdsattributecount;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.args.StringArgumentDefinition;
import net.sourceforge.ondex.config.ValidatorRegistry;
import net.sourceforge.ondex.core.AttributeName;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.ONDEXRelation;
import net.sourceforge.ondex.core.ONDEXView;
import net.sourceforge.ondex.core.base.ONDEXViewImpl;
import net.sourceforge.ondex.core.util.DefaultBitSet;
import net.sourceforge.ondex.core.util.ONDEXViewFunctions;
import net.sourceforge.ondex.event.ONDEXEventHandler;
import net.sourceforge.ondex.event.type.AttributeNameMissingEvent;
import net.sourceforge.ondex.event.type.ConceptClassMissingEvent;
import net.sourceforge.ondex.statistics.AbstractONDEXStatistics;
import net.sourceforge.ondex.validator.AbstractONDEXValidator;

/**
 * Count instances of Attribute name values e.g. TAXID lists value then count '3702	{instances}, 2456	{instances}, ...'
 * @author hindlem
 *
 */
public class Statistics extends AbstractONDEXStatistics implements ArgumentNames {

    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        return new ArgumentDefinition<?>[] { new StringArgumentDefinition(ATT_NAME_ARG, ATT_NAME_ARG_DESC, true, null, true), new StringArgumentDefinition(CCS_ARG, CCS_ARG_DESC, false, null, true) };
    }

    @Override
    public String getName() {
        return "Counts value instances for GDS";
    }

    @Override
    public String getVersion() {
        return "30-June-08";
    }

    @Override
    public boolean requiresIndexedGraph() {
        return false;
    }

    @Override
    public void start() {
        Set<ConceptClass> ccs = new HashSet<ConceptClass>();
        Object[] ccNames = sa.getObjectValueArray(CCS_ARG);
        if (ccNames != null && ccNames.length > 0) {
            for (Object ccName : ccNames) {
                ConceptClass cc = graph.getMetaData().getConceptClass(ccName.toString());
                if (cc == null) ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new ConceptClassMissingEvent(ccName.toString(), getCurrentMethodName())); else ccs.add(cc);
            }
        }
        ONDEXView<ONDEXConcept> conceptBase = graph.getConcepts();
        if (ccs.size() > 0) {
            conceptBase = new ONDEXViewImpl<ONDEXConcept>(graph, ONDEXConcept.class, new DefaultBitSet(0));
            for (ConceptClass cc : ccs) {
                conceptBase = ONDEXViewFunctions.or(conceptBase, graph.getConceptsOfConceptClass(cc));
            }
        }
        for (Object attName : sa.getObjectValueList(ATT_NAME_ARG)) {
            AttributeName att = graph.getMetaData().getAttributeName((String) attName);
            if (att == null) {
                fireEventOccurred(new AttributeNameMissingEvent((String) attName + " not found", getCurrentMethodName()));
            } else {
                HashMap<Object, Integer> objectsToValues = new HashMap<Object, Integer>();
                ONDEXView<ONDEXConcept> concepts = graph.getConceptsOfAttributeName(att);
                concepts = ONDEXViewFunctions.and(concepts, conceptBase);
                while (concepts.hasNext()) {
                    ONDEXConcept abstractConcept = (ONDEXConcept) concepts.next();
                    System.out.println(abstractConcept.getOfType().getId());
                    String value = abstractConcept.getConceptGDS(att).getValue().toString();
                    Integer count = objectsToValues.get(value);
                    if (count == null) {
                        count = 1;
                    } else {
                        count++;
                    }
                    objectsToValues.put(value, count);
                }
                concepts.close();
                printValueMap(objectsToValues);
                objectsToValues = new HashMap<Object, Integer>();
                ONDEXView<ONDEXRelation> ONDEXRelations = graph.getRelationsOfAttributeName(att);
                while (ONDEXRelations.hasNext()) {
                    ONDEXRelation relation = ONDEXRelations.next();
                    Object value = relation.getRelationGDS(att).getValue();
                    Integer count = objectsToValues.get(value);
                    if (count == null) {
                        count = 1;
                    } else {
                        count++;
                    }
                    objectsToValues.put(value, count);
                }
                ONDEXRelations.close();
                System.out.println("\nRelation GDS Instances of " + att.getId() + "+\n");
                printValueMap(objectsToValues);
            }
        }
    }

    /**
	 * prints to sys out the values in this map
	 * @param objectsToValues values to print
	 */
    private void printValueMap(HashMap<Object, Integer> objectsToValues) {
        Iterator<Object> values = objectsToValues.keySet().iterator();
        while (values.hasNext()) {
            String string = values.next().toString();
            Integer count = objectsToValues.get(string);
            AbstractONDEXValidator validator = ValidatorRegistry.validators.get("scientificspeciesname");
            String scientificName = (String) validator.validate(string);
            if (scientificName != null) {
                string = scientificName + " \t " + string;
            }
            System.out.println(string + "\t" + count);
        }
    }

    /**
	 * Convenience method for outputing the current method name in a dynamic way
	 * @return the calling method name
	 */
    public static String getCurrentMethodName() {
        Exception e = new Exception();
        StackTraceElement trace = e.fillInStackTrace().getStackTrace()[1];
        String name = trace.getMethodName();
        String className = trace.getClassName();
        int line = trace.getLineNumber();
        return "[CLASS:" + className + " - METHOD:" + name + " LINE:" + line + "]";
    }

    @Override
    public String[] requiresValidators() {
        return new String[] { "scientificspeciesname" };
    }
}
