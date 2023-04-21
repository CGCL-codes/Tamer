package net.sourceforge.ondex.parser.aracyc2.parser.transformer;

import java.util.HashSet;
import java.util.Iterator;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.core.RelationType;
import net.sourceforge.ondex.event.type.ConceptClassMissingEvent;
import net.sourceforge.ondex.event.type.RelationTypeMissingEvent;
import net.sourceforge.ondex.parser.aracyc2.Parser;
import net.sourceforge.ondex.parser.aracyc2.parser.MetaData;
import net.sourceforge.ondex.parser.aracyc2.sink.AbstractNode;
import net.sourceforge.ondex.parser.aracyc2.sink.Enzyme;
import net.sourceforge.ondex.parser.aracyc2.sink.Protein;

/**
 * Transforms a net.sourceforge.ondex.parser.aracyc2.sink.Enzyme to a Concept.
 * @author peschr
 */
public class EnzymeTransformer extends AbstractTransformer {

    ConceptClass ccEnzyme = null;

    ConceptClass ccProtcmplx = null;

    RelationType rtIsA = null;

    RelationType rtInBy = null;

    RelationType rtAcBy = null;

    RelationType rtPartOfCatClass = null;

    RelationType rtCatBy = null;

    RelationType cofactoredBy = null;

    public EnzymeTransformer(Parser parser) {
        super(parser);
        try {
            graph = getParser().getGraph();
            rtIsA = graph.getMetaData().getRelationType(MetaData.RT_IS_A);
            if (rtIsA == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_IS_A, Parser.getCurrentMethodName()));
            }
            rtInBy = graph.getMetaData().getRelationType(MetaData.RT_INHIBITED_BY);
            if (rtInBy == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_INHIBITED_BY, Parser.getCurrentMethodName()));
            }
            rtAcBy = graph.getMetaData().getRelationType(MetaData.RT_ACTIVATED_BY);
            if (rtAcBy == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_ACTIVATED_BY, Parser.getCurrentMethodName()));
            }
            ccEnzyme = graph.getMetaData().getConceptClass(MetaData.CC_Enzyme);
            if (ccEnzyme == null) {
                Parser.propagateEventOccurred(new ConceptClassMissingEvent(MetaData.CC_Enzyme, Parser.getCurrentMethodName()));
            }
            rtPartOfCatClass = graph.getMetaData().getRelationType(MetaData.RT_PART_OF_CATALYSING_CLASS);
            if (rtPartOfCatClass == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_PART_OF_CATALYSING_CLASS, Parser.getCurrentMethodName()));
            }
            rtCatBy = graph.getMetaData().getRelationType(MetaData.RT_CATALYSED_BY);
            if (rtCatBy == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_CATALYSED_BY, Parser.getCurrentMethodName()));
            }
            cofactoredBy = graph.getMetaData().getRelationType(MetaData.RT_COFACTORS_BY);
            if (cofactoredBy == null) {
                Parser.propagateEventOccurred(new RelationTypeMissingEvent(MetaData.RT_COFACTORS_BY, Parser.getCurrentMethodName()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void nodeToConcept(AbstractNode node) {
        Enzyme enzyme = (Enzyme) node;
        ONDEXConcept concept = graph.getFactory().createConcept(enzyme.getUniqueId(), cvAraC, ccEnzyme, etIMPD);
        enzyme.setConcept(concept);
    }

    @Override
    public void pointerToRelation(AbstractNode node) {
        Enzyme enzyme = (Enzyme) node;
        HashSet<ONDEXConcept> nonRedundant = getNonRedundant(enzyme.getConcept(), enzyme.getCatBy().getConcept());
        Iterator<Protein> proteins = enzyme.getIs_a().iterator();
        while (proteins.hasNext()) {
            Protein protein = proteins.next();
            super.copyContext(protein.getConcept(), nonRedundant);
            super.copyContext(graph.getFactory().createRelation(node.getConcept(), protein.getConcept(), rtIsA, etIMPD), nonRedundant);
        }
        Iterator<AbstractNode> inhibitors = enzyme.getInhibitor().iterator();
        while (inhibitors.hasNext()) {
            AbstractNode inhibitor = inhibitors.next();
            super.copyContext(inhibitor.getConcept(), nonRedundant);
            super.copyContext(graph.getFactory().createRelation(node.getConcept(), inhibitor.getConcept(), this.rtInBy, etIMPD), nonRedundant);
        }
        Iterator<AbstractNode> activators = enzyme.getActivator().iterator();
        while (activators.hasNext()) {
            AbstractNode activator = activators.next();
            super.copyContext(activator.getConcept(), nonRedundant);
            super.copyContext(graph.getFactory().createRelation(node.getConcept(), activator.getConcept(), this.rtAcBy, etIMPD), nonRedundant);
        }
        Iterator<AbstractNode> cofactors = enzyme.getActivator().iterator();
        while (cofactors.hasNext()) {
            AbstractNode cofactor = cofactors.next();
            super.copyContext(cofactor.getConcept(), nonRedundant);
            super.copyContext(graph.getFactory().createRelation(node.getConcept(), cofactor.getConcept(), this.cofactoredBy, etIMPD), nonRedundant);
        }
        if (enzyme.getCatBy() != null) {
            if (enzyme.getEcNumber() != null) {
                super.copyContext(enzyme.getEcNumber().getConcept(), nonRedundant);
                super.copyContext(graph.getFactory().createRelation(enzyme.getConcept(), enzyme.getEcNumber().getConcept(), this.rtPartOfCatClass, etIMPD), nonRedundant);
            }
            super.copyContext(enzyme.getConcept(), nonRedundant);
            super.copyContext(graph.getFactory().createRelation(enzyme.getCatBy().getConcept(), enzyme.getConcept(), this.rtCatBy, etIMPD), nonRedundant);
        }
    }
}
