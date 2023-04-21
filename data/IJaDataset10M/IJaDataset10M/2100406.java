package uk.co.ordnancesurvey.rabbitparser.result.imports.extractor;

import java.util.Set;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.exception.NotVisitableException;
import uk.co.ordnancesurvey.rabbitparser.exception.RabbitRuntimeException;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;
import uk.co.ordnancesurvey.rabbitparser.result.visitor.collector.ConceptDefinitionSentencesCollector;
import uk.co.ordnancesurvey.rabbitparser.util.filter.Filter;

/**
 * This class encapsulates the extraction of a {@link Set} of
 * {@link IParsedSentence}s from a {@link IRabbitParsedResult} based on a
 * filter {@link IParsedConcept}. The filter concept must be specified at
 * construction time, and then the
 * {@link #extractSentences(IRabbitParsedResult)} method can be used.
 * 
 * @author rdenaux
 * 
 */
public class ImportedConceptSentenceExtractor implements IImportSentenceExtractor {

    private final Filter<IParsedConcept> conceptFilter;

    /**
     * 
     * @param aFilter
     *            must not be <code>null</code>
     */
    public ImportedConceptSentenceExtractor(Filter<IParsedConcept> aFilter) {
        assert aFilter != null;
        conceptFilter = aFilter;
    }

    public Set<IParsedSentence> extractSentences(IRabbitParsedResult aResult) {
        ConceptDefinitionSentencesCollector sentenceCollector = new ConceptDefinitionSentencesCollector(conceptFilter);
        try {
            aResult.accept(sentenceCollector);
        } catch (NotVisitableException e) {
            throw new RabbitRuntimeException("Can't extract imported sentences", e);
        }
        return sentenceCollector.getFoundParsedParts();
    }
}
