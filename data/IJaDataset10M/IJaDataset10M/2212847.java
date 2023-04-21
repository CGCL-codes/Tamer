package uk.co.ordnancesurvey.rabbitparser.gate.gatetoresultconverter;

import gate.Annotation;
import gate.AnnotationSet;
import java.util.ArrayList;
import java.util.List;
import uk.co.ordnancesurvey.rabbitparser.gate.annotation.RabbitAnnotation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedEntity;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.ParsedConceptCollection;

public class GateAnnotationToParsedConceptCollectionConverter extends BaseAnnotationToRabbitParserResultConverter {

    private GateAnnotationToParsedEntityConverter annToEntityConverter = new GateAnnotationToParsedEntityConverter();

    /**
	 * Converts aAnn into a
	 * 
	 * @param aAnn
	 */
    public ParsedConceptCollection convert(Annotation aAnn, AnnotationSet aFullASToMatch) {
        ParsedConceptCollection result = null;
        RabbitAnnotation annotationType = RabbitAnnotation.getByName(aAnn.getType());
        switch(annotationType) {
            case RABBIT_CONCEPT_COLLECTION:
                ParsedConceptCollection pConceptCollection = new ParsedConceptCollection();
                pConceptCollection.addAll(extractConceptsIn(aFullASToMatch));
                result = pConceptCollection;
                break;
            default:
                throw new RuntimeException("Can't convert annotationType " + annotationType + " into a ParsedConceptCollection");
        }
        if (result != null) {
            setSpan(aAnn, result);
        }
        return result;
    }

    /**
	 * Extract and convert all parsedConcepts in fullASToMatch
	 * @param fullASToMatch
	 * @return
	 */
    private List<ParsedConcept> extractConceptsIn(AnnotationSet fullASToMatch) {
        List<ParsedConcept> result = new ArrayList<ParsedConcept>();
        AnnotationSet conceptsAS = fullASToMatch.get(RabbitAnnotation.RABBIT_CONCEPT.getName());
        for (Annotation conceptAnn : conceptsAS) {
            IParsedEntity parsedEntity = annToEntityConverter.convert(conceptAnn);
            if (parsedEntity instanceof ParsedConcept) {
                result.add((ParsedConcept) parsedEntity);
            } else {
                throw new RuntimeException("Can only add ParsedConcepts to a ParsedConceptCollection, but tried to add " + parsedEntity);
            }
        }
        return result;
    }
}
