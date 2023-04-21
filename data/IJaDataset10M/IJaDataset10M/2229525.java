package uk.co.ordnancesurvey.confluence.model.rabbitdao.converter;

import java.util.logging.Logger;
import org.semanticweb.owl.model.OWLObject;
import org.semanticweb.owl.model.OWLObjectProperty;
import uk.co.ordnancesurvey.confluence.model.ConfluenceModelManager;
import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;

/**
 * This class converts a parsed rabbit sentence with a parsed relation as its
 * main part into an OWL annotation.
 * 
 * @author rdenaux
 * 
 */
public class RelationRelatedSentenceToAnnotationConverter extends BaseSentenceToOWLEntityAnnotationConverter<OWLObjectProperty, ParsedRelation> {

    private static final Logger log = Logger.getLogger(RelationRelatedSentenceToAnnotationConverter.class.getName());

    public RelationRelatedSentenceToAnnotationConverter(ConfluenceModelManager modelManager) {
        super(modelManager);
    }

    @Override
    protected boolean canBeCast(OWLObject object) {
        boolean result = object instanceof OWLObjectProperty;
        return result;
    }

    @Override
    protected OWLObjectProperty cast(OWLObject object) {
        return (OWLObjectProperty) object;
    }

    @Override
    protected ParsedRelation getMainParsedEntity(IParsedSentence aParsedSentence) {
        return getMainParsedPartExtractor().getMainRelation(aParsedSentence);
    }

    @Override
    protected boolean hasMainPartSupported(DeclarativeSentenceType aSentenceType) {
        return getMainParsedPartExtractor().hasMainPartRelation(aSentenceType);
    }

    @Override
    protected OWLObjectProperty mapRabbitParsedPartToOWLEntity(ParsedRelation aRabbitParsedPart) {
        OWLObjectProperty result = null;
        if (aRabbitParsedPart != null) {
            result = getRabbitToOWLMapper().mapToObjectProperty(aRabbitParsedPart);
            if (result != null) {
                log.info("Couldn't map relation " + aRabbitParsedPart + " with canonicalname " + aRabbitParsedPart.getCanonicalName());
            }
        }
        return result;
    }
}
