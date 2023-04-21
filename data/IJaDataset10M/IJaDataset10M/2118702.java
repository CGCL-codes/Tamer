package uk.co.ordnancesurvey.rabbitparser.parsedsentences.conceptdeclaration;

import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedExternalOntologyAlias;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.BaseParsedSentence;

/**
 * Parsed sentence for the ConceptDeclaration sentence type.
 * 
 * @author rdenaux
 * 
 */
public class ConceptDeclarationParsedSentence extends BaseParsedSentence {

    private boolean definesPlural;

    private boolean usesExternalOntology;

    public ConceptDeclarationParsedSentence(IRabbitParsedResult rabbitParsedResult) {
        super(rabbitParsedResult);
    }

    public DeclarativeSentenceType getSentenceType() {
        return DeclarativeSentenceType.CONCEPT_DECLARATION;
    }

    @Override
    public boolean isOptionalFeature(RabbitFeatureType featureType) {
        return RabbitFeatureType.C1.equals(featureType) || RabbitFeatureType.EXTERNAL_ONTOLOGY_ALIAS.equals(featureType);
    }

    @Override
    protected void validateSentence() {
        if (definesPlural) {
            validatePartAvailable(RabbitFeatureType.C1);
        }
        if (usesExternalOntology) {
            validatePartAvailable(RabbitFeatureType.EXTERNAL_ONTOLOGY_ALIAS);
        }
    }

    /**
	 * @return the subNounPhrase
	 */
    public ParsedConcept getSubNounPhrase() {
        return (ParsedConcept) getParsedPart(RabbitFeatureType.CONCEPT);
    }

    /**
	 * @return the conceptNamePlural
	 */
    public ParsedConcept getConceptNamePlural() {
        return (ParsedConcept) getParsedPart(RabbitFeatureType.C1);
    }

    /**
	 * @return the externalOntologyAlias
	 */
    public ParsedExternalOntologyAlias getExternalOntologyAlias() {
        return (ParsedExternalOntologyAlias) getParsedPart(RabbitFeatureType.EXTERNAL_ONTOLOGY_ALIAS);
    }

    @Override
    protected void entitiesToString(final StringBuilder aSB, String indent) {
        aSB.append("New concept : " + getSubNounPhrase());
        if (getConceptNamePlural() != null) {
            aSB.append("\n" + indent);
            aSB.append("pluralName: " + getConceptNamePlural());
        }
        if (getExternalOntologyAlias() != null) {
            aSB.append("\n" + indent);
            aSB.append("externalOntologyAlias: " + getExternalOntologyAlias());
        }
    }

    /**
	 * @return the definesPlural
	 */
    public boolean isDefinesPlural() {
        return definesPlural;
    }

    /**
	 * @return the usesExternalOntology
	 */
    public boolean isUsesExternalOntology() {
        return usesExternalOntology;
    }

    @Override
    protected void doSetParsedPart(RabbitFeatureType featureType) {
        super.doSetParsedPart(featureType);
        switch(featureType) {
            case CONCEPT:
                ParsedConcept snp = getSubNounPhrase();
                if (snp != null) {
                    snp.setBeingDefined(true);
                }
                break;
            case C1:
                if (getConceptNamePlural() != null) {
                    definesPlural = true;
                    getConceptNamePlural().setBeingDefined(true);
                }
                break;
            case EXTERNAL_ONTOLOGY_ALIAS:
                if (getExternalOntologyAlias() != null) {
                    usesExternalOntology = true;
                }
                break;
            default:
        }
    }
}
