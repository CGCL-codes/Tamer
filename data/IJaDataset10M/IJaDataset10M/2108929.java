package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.conceptassertion;

import java.util.List;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLObjectHasValue;
import org.semanticweb.owlapi.model.OWLObjectSomeValuesFrom;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.co.ordnancesurvey.rabbitparser.ISentenceType;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.mapper.RabbitToOwlApiEntityMapper;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.partconverter.OWLObjectFromParsedPartFactory;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.BaseSentenceConverterWithEntityMapperAndPartConverter;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedClosureAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.sentencetypedef.ConceptAssertionSentenceType;

/**
 * This class converts a {@link ClosureAxiomParsedSentence} into OWL.
 * 
 * Pattern C1 'only' R1 C2 '.' is translated as C1 is a subclass of the class of
 * things which are only related through R1 to instances of class R2.
 * 
 * For example Every mill stream only flows into a mill race. Is translated as
 * 'Mill Stream' is a subclass of the class of things which only 'flow into'
 * instances of 'Mill race'.
 * 
 * @author rdenaux
 * 
 */
public class ClosureAssertionConverter extends BaseSentenceConverterWithEntityMapperAndPartConverter<IParsedClosureAssertion> {

    public ClosureAssertionConverter(OWLOntology targetOnt, RabbitToOwlApiEntityMapper aMapper, OWLObjectFromParsedPartFactory aPartConverter) {
        super(targetOnt, aMapper, aPartConverter);
    }

    @Override
    public ISentenceType getSentenceType() {
        return ConceptAssertionSentenceType.CLOSURE_AXIOM;
    }

    @Override
    protected void fillAxiomsToAdd(List<OWLAxiom> axiomsToAdd, IParsedClosureAssertion parsedSentence) {
        IParsedConcept c1 = parsedSentence.getAssertedConcept();
        IParsedComplexRelationshipPhrase crp = parsedSentence.getComplexRelationshipPhrase();
        OWLClass class1 = getAsRequiredClass(c1);
        OWLClassExpression defaultDesc = partConverter.getOWLObject(crp, OWLClassExpression.class);
        OWLClassExpression someRestriction = null;
        OWLClassExpression restriction = null;
        if (defaultDesc instanceof OWLObjectSomeValuesFrom) {
            OWLObjectSomeValuesFrom someRest = (OWLObjectSomeValuesFrom) defaultDesc;
            someRestriction = someRest;
            restriction = dataFactory.getOWLObjectAllValuesFrom(someRest.getProperty(), someRest.getFiller());
        } else if (defaultDesc instanceof OWLObjectHasValue) {
            OWLObjectHasValue objValueRestriction = (OWLObjectHasValue) defaultDesc;
            someRestriction = objValueRestriction;
            restriction = dataFactory.getOWLObjectAllValuesFrom(objValueRestriction.getProperty(), dataFactory.getOWLObjectOneOf(objValueRestriction.getValue()));
        } else {
            assert true : "only a object some restriction or object value restriction should be returned but was " + defaultDesc + " based on " + crp.getAsString();
        }
        axiomsToAdd.add(makeSubClassAxiom(class1, someRestriction));
        axiomsToAdd.add(makeSubClassAxiom(class1, restriction));
    }

    @Override
    protected void fillAxiomsToRemove(List<OWLAxiom> axiomsToRemove, IParsedClosureAssertion parsedSentence) {
    }
}
