package uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.imports;

import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.ISentenceType;
import uk.co.ordnancesurvey.rabbitparser.RabbitKeyphraseType;
import uk.co.ordnancesurvey.rabbitparser.featuretype.ExtendedRabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.featuretype.IRabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.featuretype.RabbitFeatureType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.imports.validation.ImportedConceptVR;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedLabel;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceConceptSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.sentencetypedef.ImportsSentenceType;
import uk.co.ordnancesurvey.rabbitparser.result.imports.extractor.IImportSentenceExtractor;
import uk.co.ordnancesurvey.rabbitparser.result.imports.extractor.ImportedConceptSentenceExtractor;
import uk.co.ordnancesurvey.rabbitparser.util.filter.Filter;
import uk.co.ordnancesurvey.rabbitparser.util.filter.impl.EqualCanonicalNameConceptFilter;

/**
 * Default implementation of {@link IParsedReferenceConceptSentence}
 * 
 * @author rdenaux
 * 
 */
public class ParsedReferenceConceptSentence extends BaseReferenceEntitySentence<IParsedConcept> implements IParsedReferenceConceptSentence {

    private static final long serialVersionUID = -5834567772291106685L;

    public ParsedReferenceConceptSentence(IParsedConcept aImportC, IParsedLabel aImportLabel, IParsedConcept aLocalC, List<IParsedKeyphrase> keyphrases) {
        super(aImportC, aImportLabel, aLocalC, keyphrases, IParsedConcept.class);
    }

    public ISentenceType getSentenceType() {
        return ImportsSentenceType.REFERENCE_CONCEPT;
    }

    @Override
    protected IRabbitFeatureType getImportedEntityFT() {
        return RabbitFeatureType.CONCEPT;
    }

    @Override
    protected IRabbitFeatureType getLocalEntityFT() {
        return ExtendedRabbitFeatureType.C1;
    }

    @Override
    public void performValidation() {
        super.performValidation();
        new ImportedConceptVR(this).validate();
    }

    public boolean isSecondary() {
        boolean result = false;
        for (IParsedKeyphrase kp : getKeyphrases()) {
            if (RabbitKeyphraseType.Secondary.equals(kp.getKeyphraseType())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Return a filter which only allows concepts with the same canonical name
     * as the imported entity.
     * 
     * @see uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceEntitySentence#getImportedEntityFilter()
     */
    public Filter<IParsedConcept> getImportedEntityFilter() {
        return new EqualCanonicalNameConceptFilter(getImportedEntity(), getImportedParsedResult().getParserClient());
    }

    @Override
    protected IImportSentenceExtractor getImportSentenceExtractor() {
        return new ImportedConceptSentenceExtractor(getImportedEntityFilter());
    }
}
