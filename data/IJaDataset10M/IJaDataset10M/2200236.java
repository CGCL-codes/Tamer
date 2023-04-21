package net.sourceforge.ondex.parser.ecocyc.parser.transformer;

import net.sourceforge.ondex.core.CV;
import net.sourceforge.ondex.core.ConceptClass;
import net.sourceforge.ondex.core.ONDEXConcept;
import net.sourceforge.ondex.event.type.CVMissingEvent;
import net.sourceforge.ondex.parser.ecocyc.Parser;
import net.sourceforge.ondex.parser.ecocyc.parser.MetaData;
import net.sourceforge.ondex.parser.ecocyc.sink.AbstractNode;
import net.sourceforge.ondex.parser.ecocyc.sink.ECNumber;

/**
 * Transforms a net.sourceforge.ondex.parser.ecocyc.sink.ECNumber to a Concept.
 * @author peschr
 */
public class ECNumberTransformer extends AbstractTransformer {

    private ConceptClass ccEC;

    private CV cvEC;

    public ECNumberTransformer(Parser parser) {
        super(parser);
        ccEC = graph.getMetaData().getConceptClass(MetaData.CC_EC);
        if (ccEC == null) {
            Parser.propagateEventOccurred(new CVMissingEvent(MetaData.CC_EC, Parser.getCurrentMethodName()));
        }
        cvEC = graph.getMetaData().getCV(MetaData.CV_EC);
        if (cvEC == null) {
            Parser.propagateEventOccurred(new CVMissingEvent(MetaData.CV_EC, Parser.getCurrentMethodName()));
        }
    }

    @Override
    public void nodeToConcept(AbstractNode node) {
        ECNumber ecNumber = (ECNumber) node;
        ONDEXConcept concept = graph.getFactory().createConcept(ecNumber.getUniqueId(), cvMetaC, ccEC, etIMPD);
        concept.createConceptAccession(ecNumber.getUniqueId(), cvEC, false);
        ecNumber.setConcept(concept);
    }

    @Override
    public void pointerToRelation(AbstractNode node) {
    }
}
