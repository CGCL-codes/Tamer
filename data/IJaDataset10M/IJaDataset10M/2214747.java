package uk.co.ordnancesurvey.rabbitparser.testinfrastructure.result;

import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedFreeText;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedKeyphrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexCompoundObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexConceptObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.complexobject.IParsedComplexRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedCompoundRelationship;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedCompoundRelationshipBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedDefinitionPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedDefinitionPhraseList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedDefinitionPhraseListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedNegatedCompoundRelationship;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.compoundrelationship.IParsedSubsumptionPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.datavalue.IParsedDataValuePhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.datavalue.IParsedStringDataValue;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConcept;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedConceptCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstance;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedInstanceCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelation;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedRelationshipCandidate;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedToken;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonym;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.synonym.IParsedSynonymListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.impl.base.BaseParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedLabel;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedURL;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.importsbasic.IParsedUrlRef;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.invalid.IInvalidSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.invalid.IInvalidSequence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IObjectAndListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IObjectListBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedCompoundObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedConceptObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectAndList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedObjectList;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.object.IParsedRelationshipPhrase;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedArticle;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedNumber;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedNumberRestriction;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedObjectPrefix;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedPreposition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedPrepositionModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.objectmodifier.IParsedThatPostModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedConceptDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedImportSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedInstanceAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedInstanceDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRelationshipDeclaration;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRelationshipModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.IParsedSentenceBody;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedAllValueAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedClosureAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedConceptDefinition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedConceptSubsumption;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedMutualConceptExclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedNegativeAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedPositiveAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedProbableAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.conceptassertion.IParsedValuePartition;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceConceptSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceInstanceSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedReferenceRelationSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedUseMultipleOntologiesSentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.imports.IParsedUseSingleOntologySentence;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedDifferentInstances;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedNegativeInstanceAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedPositiveInstanceAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.instanceassertion.IParsedSameInstances;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedEquivalenceRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedFunctionalRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedInverseFunctionalRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedInverseRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedIrreflexiveRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedLiteralModifier;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedLiteralRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedMutualRelationExclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedReflexiveRelationAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedRelationDomainAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedRelationRangeAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.relationshipassertion.IParsedRelationSubsumptionAssertion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.roleinclusion.IParsedComplexRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.sentencebody.roleinclusion.IParsedGeneralRoleInclusion;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedIndefiniteSubject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedNegatedSubject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedSubject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.subject.IParsedUniversalSubject;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;
import uk.co.ordnancesurvey.rabbitparser.result.visitor.renderer.RabbitRenderer;
import uk.co.ordnancesurvey.rabbitparser.util.ParsedSpan;

/**
 * This utility class assigns values to the span of {@link IParsedPart}s by
 * using the rendered string for a {@link IRabbitParsedResult}.
 * 
 * @author rdenaux
 * 
 */
public class TestParsedResultSpanAssigner extends RabbitRenderer {

    @Override
    public void visit(IInvalidSentence invalid) {
        int start = getRendering().length();
        super.visit(invalid);
        int end = getRendering().length();
        setNewSpan(invalid, start, end);
    }

    private void setNewSpan(IParsedPart aPart, int start, int end) {
        final ParsedSpan newSpan = new ParsedSpan();
        newSpan.setBeginIndex(start);
        newSpan.setEndIndex(end - 1);
        ((BaseParsedPart) aPart).setParsedSpan(newSpan);
    }

    @Override
    public void visit(IInvalidSequence invalid) {
        int start = getRendering().length();
        super.visit(invalid);
        int end = getRendering().length();
        setNewSpan(invalid, start, end);
    }

    @Override
    public void visit(IObjectAndListBody objectAndListBody) {
        int start = getRendering().length();
        super.visit(objectAndListBody);
        int end = getRendering().length();
        setNewSpan(objectAndListBody, start, end);
    }

    @Override
    public void visit(IObjectListBody objectListBody) {
        int start = getRendering().length();
        super.visit(objectListBody);
        int end = getRendering().length();
        setNewSpan(objectListBody, start, end);
    }

    @Override
    public void visit(IParsedAllValueAssertion allValuesAssertion) {
        int start = getRendering().length();
        super.visit(allValuesAssertion);
        int end = getRendering().length();
        setNewSpan(allValuesAssertion, start, end);
    }

    @Override
    public void visit(IParsedArticle article) {
        int start = getRendering().length();
        super.visit(article);
        int end = getRendering().length();
        setNewSpan(article, start, end);
    }

    @Override
    public void visit(IParsedClosureAssertion closureAssertion) {
        int start = getRendering().length();
        super.visit(closureAssertion);
        int end = getRendering().length();
        setNewSpan(closureAssertion, start, end);
    }

    @Override
    public void visit(IParsedComplexCompoundObject complexCompoundObject) {
        int start = getRendering().length();
        super.visit(complexCompoundObject);
        int end = getRendering().length();
        setNewSpan(complexCompoundObject, start, end);
    }

    @Override
    public void visit(IParsedComplexConceptObject complexConceptObject) {
        int start = getRendering().length();
        super.visit(complexConceptObject);
        int end = getRendering().length();
        setNewSpan(complexConceptObject, start, end);
    }

    @Override
    public void visit(IParsedComplexObject complexObject) {
        int start = getRendering().length();
        super.visit(complexObject);
        int end = getRendering().length();
        setNewSpan(complexObject, start, end);
    }

    @Override
    public void visit(IParsedComplexRelationshipPhrase complexRelationshipPhrase) {
        int start = getRendering().length();
        super.visit(complexRelationshipPhrase);
        int end = getRendering().length();
        setNewSpan(complexRelationshipPhrase, start, end);
    }

    @Override
    public void visit(IParsedComplexRoleInclusion complexRoleInclusion) {
        int start = getRendering().length();
        super.visit(complexRoleInclusion);
        int end = getRendering().length();
        setNewSpan(complexRoleInclusion, start, end);
    }

    @Override
    public void visit(IParsedCompoundObject compoundObject) {
        int start = getRendering().length();
        super.visit(compoundObject);
        int end = getRendering().length();
        setNewSpan(compoundObject, start, end);
    }

    @Override
    public void visit(IParsedCompoundRelationship compoundRelationship) {
        int start = getRendering().length();
        super.visit(compoundRelationship);
        int end = getRendering().length();
        setNewSpan(compoundRelationship, start, end);
    }

    @Override
    public void visit(IParsedCompoundRelationshipBody compoundRelationshipBody) {
        int start = getRendering().length();
        super.visit(compoundRelationshipBody);
        int end = getRendering().length();
        setNewSpan(compoundRelationshipBody, start, end);
    }

    @Override
    public void visit(IParsedConcept concept) {
        int start = getRendering().length();
        super.visit(concept);
        int end = getRendering().length();
        setNewSpan(concept, start, end);
    }

    @Override
    public void visit(IParsedConceptAssertion conceptAssertion) {
        int start = getRendering().length();
        super.visit(conceptAssertion);
        int end = getRendering().length();
        setNewSpan(conceptAssertion, start, end);
    }

    @Override
    public void visit(IParsedConceptCandidate conceptCandidate) {
        int start = getRendering().length();
        super.visit(conceptCandidate);
        int end = getRendering().length();
        setNewSpan(conceptCandidate, start, end);
    }

    @Override
    public void visit(IParsedConceptDeclaration conceptDeclaration) {
        int start = getRendering().length();
        super.visit(conceptDeclaration);
        int end = getRendering().length();
        setNewSpan(conceptDeclaration, start, end);
    }

    @Override
    public void visit(IParsedConceptDefinition conceptDefinition) {
        int start = getRendering().length();
        super.visit(conceptDefinition);
        int end = getRendering().length();
        setNewSpan(conceptDefinition, start, end);
    }

    @Override
    public void visit(IParsedConceptSubsumption conceptSubsumption) {
        int start = getRendering().length();
        super.visit(conceptSubsumption);
        int end = getRendering().length();
        setNewSpan(conceptSubsumption, start, end);
    }

    @Override
    public void visit(IParsedDataValuePhrase aDataValuePhrase) {
        int start = getRendering().length();
        super.visit(aDataValuePhrase);
        int end = getRendering().length();
        setNewSpan(aDataValuePhrase, start, end);
    }

    @Override
    public void visit(IParsedDefinitionPhrase definitionPhrase) {
        int start = getRendering().length();
        super.visit(definitionPhrase);
        int end = getRendering().length();
        setNewSpan(definitionPhrase, start, end);
    }

    @Override
    public void visit(IParsedDefinitionPhraseList definitionPhraseList) {
        int start = getRendering().length();
        super.visit(definitionPhraseList);
        int end = getRendering().length();
        setNewSpan(definitionPhraseList, start, end);
    }

    @Override
    public void visit(IParsedDefinitionPhraseListBody definitionPhraseListBody) {
        int start = getRendering().length();
        super.visit(definitionPhraseListBody);
        int end = getRendering().length();
        setNewSpan(definitionPhraseListBody, start, end);
    }

    @Override
    public void visit(IParsedDifferentInstances diffInstances) {
        int start = getRendering().length();
        super.visit(diffInstances);
        int end = getRendering().length();
        setNewSpan(diffInstances, start, end);
    }

    @Override
    public void visit(IParsedEquivalenceRelationAssertion equivalenceRelationAssertion) {
        int start = getRendering().length();
        super.visit(equivalenceRelationAssertion);
        int end = getRendering().length();
        setNewSpan(equivalenceRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedFreeText aFreeText) {
        int start = getRendering().length();
        super.visit(aFreeText);
        int end = getRendering().length();
        setNewSpan(aFreeText, start, end);
    }

    @Override
    public void visit(IParsedFunctionalRelationAssertion functionalRelationAssertion) {
        int start = getRendering().length();
        super.visit(functionalRelationAssertion);
        int end = getRendering().length();
        setNewSpan(functionalRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedGeneralRoleInclusion generalRoleInclusion) {
        int start = getRendering().length();
        super.visit(generalRoleInclusion);
        int end = getRendering().length();
        setNewSpan(generalRoleInclusion, start, end);
    }

    @Override
    public void visit(IParsedImportSentence importSentence) {
        int start = getRendering().length();
        super.visit(importSentence);
        int end = getRendering().length();
        setNewSpan(importSentence, start, end);
    }

    @Override
    public void visit(IParsedIndefiniteSubject indefiniteSubject) {
        int start = getRendering().length();
        super.visit(indefiniteSubject);
        int end = getRendering().length();
        setNewSpan(indefiniteSubject, start, end);
    }

    @Override
    public void visit(IParsedInstance instance) {
        int start = getRendering().length();
        super.visit(instance);
        int end = getRendering().length();
        setNewSpan(instance, start, end);
    }

    @Override
    public void visit(IParsedInstanceAssertion instanceAssertion) {
        int start = getRendering().length();
        super.visit(instanceAssertion);
        int end = getRendering().length();
        setNewSpan(instanceAssertion, start, end);
    }

    @Override
    public void visit(IParsedInstanceCandidate instanceCandidate) {
        int start = getRendering().length();
        super.visit(instanceCandidate);
        int end = getRendering().length();
        setNewSpan(instanceCandidate, start, end);
    }

    @Override
    public void visit(IParsedInstanceDeclaration instanceDeclaration) {
        int start = getRendering().length();
        super.visit(instanceDeclaration);
        int end = getRendering().length();
        setNewSpan(instanceDeclaration, start, end);
    }

    @Override
    public void visit(IParsedInverseFunctionalRelationAssertion invFunctRelAssertion) {
        int start = getRendering().length();
        super.visit(invFunctRelAssertion);
        int end = getRendering().length();
        setNewSpan(invFunctRelAssertion, start, end);
    }

    @Override
    public void visit(IParsedInverseRelationAssertion inverseRelationAssertion) {
        int start = getRendering().length();
        super.visit(inverseRelationAssertion);
        int end = getRendering().length();
        setNewSpan(inverseRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedIrreflexiveRelationAssertion irreflexiveRelationAssertion) {
        int start = getRendering().length();
        super.visit(irreflexiveRelationAssertion);
        int end = getRendering().length();
        setNewSpan(irreflexiveRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedKeyphrase parsedKeyphrase) {
        int start = getRendering().length();
        super.visit(parsedKeyphrase);
        int end = getRendering().length();
        setNewSpan(parsedKeyphrase, start, end);
    }

    @Override
    public void visit(IParsedLabel label) {
        int start = getRendering().length();
        super.visit(label);
        int end = getRendering().length();
        setNewSpan(label, start, end);
    }

    @Override
    public void visit(IParsedLiteralModifier literalModifier) {
        int start = getRendering().length();
        super.visit(literalModifier);
        int end = getRendering().length();
        setNewSpan(literalModifier, start, end);
    }

    @Override
    public void visit(IParsedLiteralRelationAssertion literalRelationAssertion) {
        int start = getRendering().length();
        super.visit(literalRelationAssertion);
        int end = getRendering().length();
        setNewSpan(literalRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedMutualConceptExclusion mutualConceptExclusion) {
        int start = getRendering().length();
        super.visit(mutualConceptExclusion);
        int end = getRendering().length();
        setNewSpan(mutualConceptExclusion, start, end);
    }

    @Override
    public void visit(IParsedMutualRelationExclusion mutualRelationExclusion) {
        int start = getRendering().length();
        super.visit(mutualRelationExclusion);
        int end = getRendering().length();
        setNewSpan(mutualRelationExclusion, start, end);
    }

    @Override
    public void visit(IParsedNegatedCompoundRelationship negatedCompoundRelationship) {
        int start = getRendering().length();
        super.visit(negatedCompoundRelationship);
        int end = getRendering().length();
        setNewSpan(negatedCompoundRelationship, start, end);
    }

    @Override
    public void visit(IParsedNegatedSubject negatedSubject) {
        int start = getRendering().length();
        super.visit(negatedSubject);
        int end = getRendering().length();
        setNewSpan(negatedSubject, start, end);
    }

    @Override
    public void visit(IParsedNegativeAssertion negativeAssertion) {
        int start = getRendering().length();
        super.visit(negativeAssertion);
        int end = getRendering().length();
        setNewSpan(negativeAssertion, start, end);
    }

    @Override
    public void visit(IParsedNegativeInstanceAssertion negativeInstanceAssertion) {
        int start = getRendering().length();
        super.visit(negativeInstanceAssertion);
        int end = getRendering().length();
        setNewSpan(negativeInstanceAssertion, start, end);
    }

    @Override
    public void visit(IParsedNumber number) {
        int start = getRendering().length();
        super.visit(number);
        int end = getRendering().length();
        setNewSpan(number, start, end);
    }

    @Override
    public void visit(IParsedNumberRestriction numberRestriction) {
        int start = getRendering().length();
        super.visit(numberRestriction);
        int end = getRendering().length();
        setNewSpan(numberRestriction, start, end);
    }

    @Override
    public void visit(IParsedConceptObject conceptObject) {
        int start = getRendering().length();
        super.visit(conceptObject);
        int end = getRendering().length();
        setNewSpan(conceptObject, start, end);
    }

    @Override
    public void visit(IParsedObject object) {
        int start = getRendering().length();
        super.visit(object);
        int end = getRendering().length();
        setNewSpan(object, start, end);
    }

    @Override
    public void visit(IParsedObjectAndList objectAndList) {
        int start = getRendering().length();
        super.visit(objectAndList);
        int end = getRendering().length();
        setNewSpan(objectAndList, start, end);
    }

    @Override
    public void visit(IParsedObjectList objectList) {
        int start = getRendering().length();
        super.visit(objectList);
        int end = getRendering().length();
        setNewSpan(objectList, start, end);
    }

    @Override
    public void visit(IParsedObjectPrefix objectPrefix) {
        int start = getRendering().length();
        super.visit(objectPrefix);
        int end = getRendering().length();
        setNewSpan(objectPrefix, start, end);
    }

    @Override
    public void visit(IParsedPositiveAssertion positiveAssertion) {
        int start = getRendering().length();
        super.visit(positiveAssertion);
        int end = getRendering().length();
        setNewSpan(positiveAssertion, start, end);
    }

    @Override
    public void visit(IParsedPositiveInstanceAssertion positiveInstanceAssertion) {
        int start = getRendering().length();
        super.visit(positiveInstanceAssertion);
        int end = getRendering().length();
        setNewSpan(positiveInstanceAssertion, start, end);
    }

    @Override
    public void visit(IParsedPreposition preposition) {
        int start = getRendering().length();
        super.visit(preposition);
        int end = getRendering().length();
        setNewSpan(preposition, start, end);
    }

    @Override
    public void visit(IParsedPrepositionModifier prepositionModifier) {
        int start = getRendering().length();
        super.visit(prepositionModifier);
        int end = getRendering().length();
        setNewSpan(prepositionModifier, start, end);
    }

    @Override
    public void visit(IParsedProbableAssertion probableAssertion) {
        int start = getRendering().length();
        super.visit(probableAssertion);
        int end = getRendering().length();
        setNewSpan(probableAssertion, start, end);
    }

    @Override
    public void visit(IParsedReferenceConceptSentence referenceConceptSentence) {
        int start = getRendering().length();
        super.visit(referenceConceptSentence);
        int end = getRendering().length();
        setNewSpan(referenceConceptSentence, start, end);
    }

    @Override
    public void visit(IParsedReferenceInstanceSentence referenceInstnaceSentence) {
        int start = getRendering().length();
        super.visit(referenceInstnaceSentence);
        int end = getRendering().length();
        setNewSpan(referenceInstnaceSentence, start, end);
    }

    @Override
    public void visit(IParsedReferenceRelationSentence referenceRelationSentence) {
        int start = getRendering().length();
        super.visit(referenceRelationSentence);
        int end = getRendering().length();
        setNewSpan(referenceRelationSentence, start, end);
    }

    @Override
    public void visit(IParsedReflexiveRelationAssertion reflexiveRelationAssertion) {
        int start = getRendering().length();
        super.visit(reflexiveRelationAssertion);
        int end = getRendering().length();
        setNewSpan(reflexiveRelationAssertion, start, end);
    }

    @Override
    public void visit(IParsedRelation relation) {
        int start = getRendering().length();
        super.visit(relation);
        int end = getRendering().length();
        setNewSpan(relation, start, end);
    }

    @Override
    public void visit(IParsedRelationDomainAssertion relationDomainAssertion) {
        int start = getRendering().length();
        super.visit(relationDomainAssertion);
        int end = getRendering().length();
        setNewSpan(relationDomainAssertion, start, end);
    }

    @Override
    public void visit(IParsedRelationRangeAssertion relationRangeAssertion) {
        int start = getRendering().length();
        super.visit(relationRangeAssertion);
        int end = getRendering().length();
        setNewSpan(relationRangeAssertion, start, end);
    }

    @Override
    public void visit(IParsedRelationshipCandidate relationshipCandidate) {
        int start = getRendering().length();
        super.visit(relationshipCandidate);
        int end = getRendering().length();
        setNewSpan(relationshipCandidate, start, end);
    }

    @Override
    public void visit(IParsedRelationshipDeclaration relationshipDeclaration) {
        int start = getRendering().length();
        super.visit(relationshipDeclaration);
        int end = getRendering().length();
        setNewSpan(relationshipDeclaration, start, end);
    }

    @Override
    public void visit(IParsedRelationshipModifier relationshipModifier) {
        int start = getRendering().length();
        super.visit(relationshipModifier);
        int end = getRendering().length();
        setNewSpan(relationshipModifier, start, end);
    }

    @Override
    public void visit(IParsedRelationshipPhrase relationshipPhrase) {
        int start = getRendering().length();
        super.visit(relationshipPhrase);
        int end = getRendering().length();
        setNewSpan(relationshipPhrase, start, end);
    }

    @Override
    public void visit(IParsedRelationSubsumptionAssertion relationSubsumptionAssertion) {
        int start = getRendering().length();
        super.visit(relationSubsumptionAssertion);
        int end = getRendering().length();
        setNewSpan(relationSubsumptionAssertion, start, end);
    }

    @Override
    public void visit(IParsedRoleInclusion roleInclusion) {
        int start = getRendering().length();
        super.visit(roleInclusion);
        int end = getRendering().length();
        setNewSpan(roleInclusion, start, end);
    }

    @Override
    public void visit(IParsedSameInstances sameInstances) {
        int start = getRendering().length();
        super.visit(sameInstances);
        int end = getRendering().length();
        setNewSpan(sameInstances, start, end);
    }

    @Override
    public void visit(IParsedSentence sentence) {
        int start = getRendering().length();
        super.visit(sentence);
        int end = getRendering().length();
        setNewSpan(sentence, start, end);
    }

    @Override
    public void visit(IParsedSentenceBody sentenceBody) {
        int start = getRendering().length();
        super.visit(sentenceBody);
        int end = getRendering().length();
        setNewSpan(sentenceBody, start, end);
    }

    @Override
    public void visit(IParsedStringDataValue aStringDataValue) {
        int start = getRendering().length();
        super.visit(aStringDataValue);
        int end = getRendering().length();
        setNewSpan(aStringDataValue, start, end);
    }

    @Override
    public void visit(IParsedSubject subject) {
        int start = getRendering().length();
        super.visit(subject);
        int end = getRendering().length();
        setNewSpan(subject, start, end);
    }

    @Override
    public void visit(IParsedSubsumptionPhrase subsumptionphrase) {
        int start = getRendering().length();
        super.visit(subsumptionphrase);
        int end = getRendering().length();
        setNewSpan(subsumptionphrase, start, end);
    }

    @Override
    public void visit(IParsedSynonym synonym) {
        int start = getRendering().length();
        super.visit(synonym);
        int end = getRendering().length();
        setNewSpan(synonym, start, end);
    }

    @Override
    public void visit(IParsedSynonymList synonymList) {
        int start = getRendering().length();
        super.visit(synonymList);
        int end = getRendering().length();
        setNewSpan(synonymList, start, end);
    }

    @Override
    public void visit(IParsedSynonymListBody synonymListBody) {
        int start = getRendering().length();
        super.visit(synonymListBody);
        int end = getRendering().length();
        setNewSpan(synonymListBody, start, end);
    }

    @Override
    public void visit(IParsedThatPostModifier thatPostModifier) {
        int start = getRendering().length();
        super.visit(thatPostModifier);
        int end = getRendering().length();
        setNewSpan(thatPostModifier, start, end);
    }

    @Override
    public void visit(IParsedToken token) {
        int start = getRendering().length();
        super.visit(token);
        int end = getRendering().length();
        setNewSpan(token, start, end);
    }

    @Override
    public void visit(IParsedUniversalSubject universalSubject) {
        int start = getRendering().length();
        super.visit(universalSubject);
        int end = getRendering().length();
        setNewSpan(universalSubject, start, end);
    }

    @Override
    public void visit(IParsedURL aurl) {
        int start = getRendering().length();
        super.visit(aurl);
        int end = getRendering().length();
        setNewSpan(aurl, start, end);
    }

    @Override
    public void visit(IParsedUrlRef urlRef) {
        int start = getRendering().length();
        super.visit(urlRef);
        int end = getRendering().length();
        setNewSpan(urlRef, start, end);
    }

    @Override
    public void visit(IParsedUseMultipleOntologiesSentence useMultipleOntologiesSentence) {
        int start = getRendering().length();
        super.visit(useMultipleOntologiesSentence);
        int end = getRendering().length();
        setNewSpan(useMultipleOntologiesSentence, start, end);
    }

    @Override
    public void visit(IParsedUseSingleOntologySentence useSingleOntologySentence) {
        int start = getRendering().length();
        super.visit(useSingleOntologySentence);
        int end = getRendering().length();
        setNewSpan(useSingleOntologySentence, start, end);
    }

    @Override
    public void visit(IParsedValuePartition valuePartition) {
        int start = getRendering().length();
        super.visit(valuePartition);
        int end = getRendering().length();
        setNewSpan(valuePartition, start, end);
    }

    @Override
    public void visit(IRabbitParsedResult parsedResult) {
        int start = getRendering().length();
        super.plainVisit(parsedResult);
        int end = getRendering().length();
        setNewSpan(parsedResult, start, end);
    }
}
