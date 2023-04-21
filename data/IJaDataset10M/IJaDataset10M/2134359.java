package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.conceptassertion;

import java.util.List;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.co.ordnancesurvey.rabbitparser.ISentenceType;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.mapper.RabbitToOwlApiEntityMapper;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.partconverter.OWLObjectFromParsedPartFactory;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.BaseSentenceConverterWithEntityMapperAndPartConverter;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedConceptDefinition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.sentencetypedef.ConceptAssertionSentenceType;

/**
 * Converts a {@link IParsedConceptDefinition} into an OWL representation
 * 
 * @author rdenaux
 * 
 */
public class ConceptDefinitionConverter extends BaseSentenceConverterWithEntityMapperAndPartConverter<IParsedConceptDefinition> {

    public ConceptDefinitionConverter(OWLOntology targetOnt, RabbitToOwlApiEntityMapper aMapper, OWLObjectFromParsedPartFactory aPartConverter) {
        super(targetOnt, aMapper, aPartConverter);
    }

    @Override
    protected void fillAxiomsToAdd(List<OWLAxiom> axiomsToAdd, IParsedConceptDefinition parsedSentence) {
        IParsedConcept concept = parsedSentence.getAssertedConcept();
        OWLClass defClass = getAsRequiredClass(concept);
        OWLClassExpression description = partConverter.getOWLObject(parsedSentence.getDefinitionPhraseList(), OWLClassExpression.class);
        axiomsToAdd.add(dataFactory.getOWLEquivalentClassesAxiom(defClass, description));
    }

    @Override
    protected void fillAxiomsToRemove(List<OWLAxiom> axiomsToRemove, IParsedConceptDefinition parsedSentence) {
    }

    @Override
    public ISentenceType getSentenceType() {
        return ConceptAssertionSentenceType.CONCEPT_DEFINITION;
    }
}
