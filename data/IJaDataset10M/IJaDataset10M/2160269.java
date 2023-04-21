package uk.co.ordnancesurvey.rabbitparser.gate.jape.importsentencefinder;

import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.sentencebody.imports.ParsedReferenceRelationSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedLabel;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedImportSentence;

/**
 * Handles the finding of a relation import
 * 
 * @author rdenaux
 * 
 */
public class JapeCB_ReferenceRelationshipSentence extends BaseImportSentenceCB {

    @Override
    protected IParsedImportSentence retrieveFoundPart() {
        IParsedRelation impR = getRequiredPart("relation", IParsedRelation.class);
        IParsedLabel label = getRequiredPart("label", IParsedLabel.class);
        IParsedRelation locR = getRequiredPart("r1", IParsedRelation.class);
        return new ParsedReferenceRelationSentence(impR, label, locR, getParsedKeyphrases());
    }
}
