package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.imports.base;

import java.util.List;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDeclarationAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.mapper.RabbitToOwlApiEntityMapper;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.BaseOWLAPISentenceConverterWithEntityMapper;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedEntity;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceEntitySentence;

/**
 * Provides a basic strategy to handle reference entity sentences. Subclasses
 * will exist for concepts, relations and individuals.
 * 
 * @author rdenaux
 * 
 * @param <OWLEntityType>
 * @param <S>
 */
public abstract class BaseReferenceEntitySentenceConverter<ParsedEntityType extends IParsedEntity, OWLEntityType extends OWLEntity, S extends IParsedReferenceEntitySentence<ParsedEntityType>> extends BaseOWLAPISentenceConverterWithEntityMapper<S> {

    public BaseReferenceEntitySentenceConverter(OWLOntology targetOnt, RabbitToOwlApiEntityMapper aRbtToOwlEntityMapper) {
        super(targetOnt, aRbtToOwlEntityMapper);
    }

    /**
	 * All references to imported entities are handled the same way. The local
	 * reference is added to the ontology and then the axioms of the imported
	 * ontology are copied to the axiomsToAdd {@link List}.
	 * 
	 * @see uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.BaseSentenceConverter#fillAxiomsToAdd(java.util.List,
	 *      uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedSentenceBody)
	 */
    @Override
    protected final void fillAxiomsToAdd(List<OWLAxiom> axiomsToAdd, S parsedSentence) {
        ParsedEntityType locRef = parsedSentence.getLocalReference();
        OWLEntityType localEnt = createOWLEntity(locRef);
        OWLDeclarationAxiom declaration = createEntityDeclarationAxiom(localEnt);
        axiomsToAdd.add(declaration);
        axiomsToAdd.add(makeRdfLabel(localEnt, locRef));
        doFillAxiomsToAdd(axiomsToAdd, localEnt, parsedSentence);
    }

    /**
	 * Creates a new instance of an {@link OWLEntity} for locRef
	 * 
	 * @param locRef
	 * @return
	 */
    protected abstract OWLEntityType createOWLEntity(ParsedEntityType locRef);

    /**
	 * Returns an axioms stating that localEnt and importedEnt are equivalent.
	 * 
	 * @param localEnt
	 * @param importedEnt
	 * @return
	 */
    protected abstract OWLAxiom makeEquivalent(OWLEntityType localEnt, OWLEntityType importedEnt);

    /**
	 * Subclasses use this method to copy the axioms related to localEnt from
	 * the imported ontology to axiomsToAdd
	 * 
	 * @param axiomsToAdd
	 * @param localEnt
	 */
    protected abstract void doFillAxiomsToAdd(List<OWLAxiom> axiomsToAdd, OWLEntityType localEnt, S parsedSentence);

    @Override
    protected final void fillAxiomsToRemove(List<OWLAxiom> axiomsToRemove, S parsedSentence) {
    }
}
