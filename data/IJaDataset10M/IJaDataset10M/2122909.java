package uk.co.ordnancesurvey.rabbitparser.result.visitor.collector;

import java.util.Set;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRelationshipDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRelationshipModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedSentenceBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceRelationSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;
import uk.co.ordnancesurvey.rabbitparser.result.ParsedResultIterator;
import uk.co.ordnancesurvey.rabbitparser.result.visitor.collector.base.AbstractParsedPartCollector;
import uk.co.ordnancesurvey.rabbitparser.util.filter.Filter;

/**
 * Collects the {@link IParsedSentence}s which define a {@link #filter}
 * {@link IParsedRelation}.
 * 
 * @author rdenaux
 * 
 */
public class RelationDefinitionSentencesCollector extends AbstractParsedPartCollector<IParsedSentence> {

    private final Filter<IParsedRelation> filter;

    private final ParsedResultIterator iterator = new ParsedResultIterator();

    private boolean includeDeclaration = false;

    public RelationDefinitionSentencesCollector(Filter<IParsedRelation> aFilter) {
        assert aFilter != null;
        filter = aFilter;
    }

    @Override
    public void visit(IParsedSentence sentence) {
        RelationDefinitionSentenceBodyCollector cDefSentenceBodyCollector = new RelationDefinitionSentenceBodyCollector();
        iterator.transverse(sentence, cDefSentenceBodyCollector);
        Set<IParsedSentenceBody> foundBodies = cDefSentenceBodyCollector.getFoundParsedParts();
        if (!foundBodies.isEmpty()) {
            assert foundBodies.size() == 1 : foundBodies;
            addPart(sentence);
        }
    }

    public class RelationDefinitionSentenceBodyCollector extends AbstractParsedPartCollector<IParsedSentenceBody> {

        @Override
        public void visit(IParsedReferenceRelationSentence referenceRelationSentence) {
            if (includeDeclaration && filter.isAllowed(referenceRelationSentence.getLocalReference())) {
                addPart(referenceRelationSentence);
            }
        }

        @Override
        public void visit(IParsedRelationshipDeclaration relationshipDeclaration) {
            if (includeDeclaration && filter.isAllowed(relationshipDeclaration.getDeclaredRelation())) {
                addPart(relationshipDeclaration);
            }
        }

        @Override
        public void visit(IParsedRelationshipModifier relationshipModifier) {
            if (filter.isAllowed(relationshipModifier.getModifiedRelation())) {
                addPart(relationshipModifier);
            }
        }
    }
}
