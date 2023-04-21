package net.sourceforge.ondex.mapping.gdsequality;

import net.sourceforge.ondex.InvalidPluginArgumentException;
import net.sourceforge.ondex.annotations.Authors;
import net.sourceforge.ondex.annotations.Custodians;
import net.sourceforge.ondex.args.ArgumentDefinition;
import net.sourceforge.ondex.args.BooleanArgumentDefinition;
import net.sourceforge.ondex.args.StringArgumentDefinition;
import net.sourceforge.ondex.core.*;
import net.sourceforge.ondex.event.ONDEXEventHandler;
import net.sourceforge.ondex.event.type.*;
import net.sourceforge.ondex.exception.type.EmptyStringException;
import net.sourceforge.ondex.exception.type.NullValueException;
import net.sourceforge.ondex.mapping.ONDEXMapping;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import static net.sourceforge.ondex.mapping.ArgumentNames.*;
import static net.sourceforge.ondex.mapping.gdsequality.ArgumentNames.*;

/**
 * Creates relation where all specified GDS are equal
 *
 * @author hindlem
 */
@Authors(authors = { "Matthew Hindle" }, emails = { "matthew_hindle at users.sourceforge.net" })
@Custodians(custodians = { "Jochen Weile" }, emails = { "jweile at users.sourceforge.net" })
public class Mapping extends ONDEXMapping implements MetaData {

    private static Mapping instance;

    private EvidenceType eviType;

    private RelationType rtSet;

    /**
     * NB: Overrides previous instance logging
     */
    public Mapping() {
        instance = this;
    }

    @Override
    public void start() throws NullValueException, EmptyStringException, InvalidPluginArgumentException {
        Boolean withinCV = (Boolean) ma.getUniqueValue(WITHIN_CV_ARG);
        Boolean ignoreCase = (Boolean) ma.getUniqueValue(IGNORE_CASE_ARG);
        String replacePatternString = (String) ma.getUniqueValue(REPLACE_PATTERN_ARG);
        ConceptClass conceptClass = null;
        String conceptClassName = (String) ma.getUniqueValue(CONCEPT_CLASS_ARG);
        if (conceptClassName != null) {
            conceptClass = graph.getMetaData().getConceptClass(conceptClassName);
            if (conceptClass == null) {
                ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new ConceptClassMissingEvent("Name:" + conceptClassName, Mapping.getCurrentMethodName()));
            }
        }
        Pattern replacePattern = null;
        if (replacePatternString != null) {
            replacePattern = Pattern.compile(replacePatternString);
        }
        if (withinCV == null) withinCV = false;
        rtSet = graph.getMetaData().getRelationType((String) ma.getUniqueValue(RELATION_ARG));
        if (rtSet == null) {
            RelationType rt = graph.getMetaData().getRelationType(RELATION_ARG);
            if (rt == null) {
                ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new RelationTypeMissingEvent(RELATION_ARG, Mapping.getCurrentMethodName()));
            }
            rtSet = graph.getMetaData().getFactory().createRelationType(RELATION_ARG, rt);
        }
        eviType = graph.getMetaData().getEvidenceType(EVI_GDSEQUAL);
        if (eviType == null) {
            ONDEXEventHandler.getEventHandlerForSID(graph.getSID()).fireEventOccurred(new EvidenceTypeMissingEvent(EVI_GDSEQUAL, Mapping.getCurrentMethodName()));
            eviType = graph.getMetaData().getFactory().createEvidenceType(EVI_GDSEQUAL, "A specified GDS value was equal", getName());
        }
        HashSet<AttributeName> attNames = new HashSet<AttributeName>();
        Object[] gdssArgs = ma.getObjectValueArray(GDS_ARG);
        for (Object gdsArg : gdssArgs) {
            AttributeName att = graph.getMetaData().getAttributeName((String) gdsArg);
            if (att == null) {
                fireEventOccurred(new AttributeNameMissingEvent((String) gdsArg, Mapping.getCurrentMethodName()));
            } else {
                attNames.add(att);
            }
        }
        Set<ONDEXConcept> concepts = null;
        for (AttributeName att : attNames) {
            Set<ONDEXConcept> attConcepts = graph.getConceptsOfAttributeName(att);
            if (concepts == null) {
                concepts = attConcepts;
            } else {
                concepts.addAll(attConcepts);
            }
        }
        if (conceptClass != null) {
            Set<ONDEXConcept> conceptsOfCC = graph.getConceptsOfConceptClass(conceptClass);
            concepts.retainAll(conceptsOfCC);
        }
        int found = 0;
        if (concepts != null && concepts.size() > 0) {
            for (ONDEXConcept concept : concepts) {
                for (ONDEXConcept cloneConcept : concepts) {
                    if (cloneConcept.getId() != concept.getId()) {
                        if (!evaluateMapping(graph, concept, cloneConcept, withinCV)) {
                            continue;
                        }
                        boolean matches = true;
                        for (AttributeName att : attNames) {
                            GDS gds = concept.getGDS(att);
                            GDS cloneGds = cloneConcept.getGDS(att);
                            Object value1 = gds.getValue();
                            Object value2 = cloneGds.getValue();
                            if (replacePattern != null && value1 instanceof String && value2 instanceof String) {
                                value1 = replacePattern.matcher((String) value1).replaceAll("");
                                value2 = replacePattern.matcher((String) value2).replaceAll("");
                                if (ignoreCase != null && ignoreCase && !((String) value1).equalsIgnoreCase((String) value2)) {
                                    matches = false;
                                    break;
                                }
                            } else if (!value1.equals(value2)) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            ONDEXRelation relation = graph.getRelation(concept, cloneConcept, rtSet);
                            if (relation == null) {
                                relation = graph.getFactory().createRelation(concept, cloneConcept, rtSet, eviType);
                                found++;
                            } else {
                                relation.addEvidenceType(eviType);
                            }
                        }
                    }
                }
            }
        } else {
            fireEventOccurred(new GeneralOutputEvent("No concepts meet conditions", Mapping.getCurrentMethodName()));
        }
        fireEventOccurred(new GeneralOutputEvent("Created " + found + " new relations of type " + rtSet.getId(), Mapping.getCurrentMethodName()));
    }

    public ArgumentDefinition<?>[] getArgumentDefinitions() {
        HashSet<ArgumentDefinition<?>> extendedDefinition = new HashSet<ArgumentDefinition<?>>();
        extendedDefinition.add(new StringArgumentDefinition(RELATION_ARG, RELATION_DESC, true, null, false));
        extendedDefinition.add(new StringArgumentDefinition(GDS_ARG, GDS_DESC, true, null, true));
        extendedDefinition.add(new StringArgumentDefinition(CONCEPT_CLASS_ARG, CONCEPT_CLASS_ARG_DESC, false, null, false));
        extendedDefinition.add(new StringArgumentDefinition(GDS_EQUALS_ARG, GDS_EQUALS_ARG_DESC, false, null, true));
        extendedDefinition.add(new StringArgumentDefinition(REPLACE_PATTERN_ARG, REPLACE_PATTERN_ARG_DESC, false, null, false));
        extendedDefinition.add(new BooleanArgumentDefinition(WITHIN_CV_ARG, WITHIN_CV_ARG_DESC, false, false));
        extendedDefinition.add(new BooleanArgumentDefinition(IGNORE_CASE_ARG, IGNORE_CASE_ARG_DESC, false, false));
        return extendedDefinition.toArray(new ArgumentDefinition<?>[extendedDefinition.size()]);
    }

    @Override
    public String getName() {
        return "GDS equality mapping";
    }

    @Override
    public String getVersion() {
        return "03.05.2008";
    }

    @Override
    public String getId() {
        return "gdsequality";
    }

    @Override
    public boolean requiresIndexedGraph() {
        return false;
    }

    /**
     * Convenience method for outputing the current method name in a dynamic way
     *
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

    /**
     * @param et
     */
    public static void propagateEventOccurred(EventType et) {
        if (instance != null) instance.fireEventOccurred(et);
    }

    public String[] requiresValidators() {
        return new String[0];
    }
}
