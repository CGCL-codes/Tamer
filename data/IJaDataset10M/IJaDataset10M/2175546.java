package serene.validation.handlers.error;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.BitSet;
import serene.validation.schema.active.Rule;
import serene.validation.schema.active.components.APattern;
import serene.validation.schema.active.components.ActiveTypeItem;
import serene.validation.schema.active.components.CharsActiveTypeItem;
import serene.validation.schema.active.components.DatatypedActiveTypeItem;
import serene.validation.schema.active.components.AElement;
import serene.validation.schema.active.components.AAttribute;
import serene.validation.schema.active.components.AValue;
import serene.validation.schema.active.components.AData;
import serene.validation.schema.active.components.AListPattern;
import serene.validation.schema.simplified.SimplifiedComponent;
import serene.validation.handlers.conflict.ExternalConflictHandler;
import serene.util.IntList;

interface CandidatesConflictErrorCatcher extends ErrorType {

    void unknownElement(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex);

    void unexpectedElement(int candidateIndex, int functionalEquivalenceCode, SimplifiedComponent definition, int inputRecordIndex);

    void unexpectedAmbiguousElement(int candidateIndex, int functionalEquivalenceCode, SimplifiedComponent[] possibleDefinitions, int inputRecordIndex);

    void unknownAttribute(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex);

    void unexpectedAttribute(int candidateIndex, int functionalEquivalenceCode, SimplifiedComponent definition, int inputRecordIndex);

    void unexpectedAmbiguousAttribute(int candidateIndex, int functionalEquivalenceCode, SimplifiedComponent[] possibleDefinitions, int inputRecordIndex);

    void misplacedContent(int candidateIndex, int functionalEquivalenceCode, APattern contextDefinition, int startInputRecordIndex, APattern definition, int inputRecordIndex, APattern sourceDefinition, APattern reper);

    void misplacedContent(int candidateIndex, int functionalEquivalenceCode, APattern contextDefinition, int startInputRecordIndex, APattern definition, int[] inputRecordIndex, APattern[] sourceDefinition, APattern reper);

    void excessiveContent(int candidateIndex, int functionalEquivalenceCode, Rule context, int startInputRecordIndex, APattern definition, int[] inputRecordIndex);

    void excessiveContent(int candidateIndex, int functionalEquivalenceCode, Rule context, APattern definition, int inputRecordIndex);

    void unresolvedAmbiguousElementContentError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AElement[] possibleDefinitions);

    void unresolvedUnresolvedElementContentError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AElement[] possibleDefinitions);

    void unresolvedAttributeContentError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AAttribute[] possibleDefinitions);

    void ambiguousUnresolvedElementContentWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AElement[] possibleDefinitions);

    void ambiguousAmbiguousElementContentWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AElement[] possibleDefinitions);

    void ambiguousAttributeContentWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AAttribute[] possibleDefinitions);

    void ambiguousCharacterContentWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void ambiguousAttributeValueWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void missingContent(int candidateIndex, int functionalEquivalenceCode, Rule context, int startInputRecordIndex, APattern definition, int expected, int found, int[] inputRecordIndex);

    void illegalContent(int candidateIndex, int functionalEquivalenceCode, Rule context, int startInputRecordIndex);

    void characterContentDatatypeError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, DatatypedActiveTypeItem charsDefinition, String datatypeErrorMessage);

    void attributeValueDatatypeError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, DatatypedActiveTypeItem charsDefinition, String datatypeErrorMessage);

    void characterContentValueError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AValue charsDefinition);

    void attributeValueValueError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AValue charsDefinition);

    void characterContentExceptedError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AData charsDefinition);

    void attributeValueExceptedError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AData charsDefinition);

    void unexpectedCharacterContent(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AElement elementDefinition);

    void unexpectedAttributeValue(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AAttribute attributeDefinition);

    void unresolvedCharacterContent(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void unresolvedAttributeValue(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void listTokenDatatypeError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, DatatypedActiveTypeItem charsDefinition, String datatypeErrorMessage);

    void listTokenValueError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AValue charsDefinition);

    void listTokenExceptedError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, AData charsDefinition);

    void unresolvedListTokenInContextError(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void ambiguousListTokenInContextWarning(int candidateIndex, int functionalEquivalenceCode, int inputRecordIndex, CharsActiveTypeItem[] possibleDefinitions);

    void missingCompositorContent(int candidateIndex, int functionalEquivalenceCode, Rule context, int startInputRecordIndex, APattern definition, int expected, int found);
}
