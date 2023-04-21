package uk.co.ordnancesurvey.rabbitparser.testkit.testpart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import uk.co.ordnancesurvey.rabbitparser.exception.RabbitRuntimeException;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IDelegatingParsedPart;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.IParsedPart;
import uk.co.ordnancesurvey.rabbitparser.testkit.InvalidTestParsedPartException;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.complexobject.TestParsedComplexCompoundObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.complexobject.TestParsedComplexConceptObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.complexobject.TestParsedComplexObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.complexobject.TestParsedComplexRelationshipPhraseFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedCompoundRelationshipBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedCompoundRelationshipFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedDefinitionPhraseFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedDefinitionPhraseListBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedDefinitionPhraseListFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedNegatedCompoundRelationshipFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.compoundrelationship.TestParsedSubsumptionPhraseFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.datavalue.TestParsedDataValuePhraseFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.datavalue.TestParsedFreeTextFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.datavalue.TestParsedStringValueFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedConceptCandidateFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedConceptFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedEntityCandidateFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedInstanceCandidateFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedInstanceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedRelationFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedRelationshipCandidateFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.TestParsedTokenFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.synonym.TestParsedSynonymFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.synonym.TestParsedSynonymListBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.entity.synonym.TestParsedSynonymListFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.importsbasic.TestParsedLabelFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.importsbasic.TestParsedURLFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.importsbasic.TestParsedUrlRefFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestObjectAndListBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestObjectAndListFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestObjectListBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestObjectListFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestParsedCompoundObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestParsedConceptObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestParsedObjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.object.TestParsedRelationshpPhraseFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedArticleFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedNumberFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedNumberRestrictionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedObjectPrefixFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedPrepositionModifierFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.objectmodifier.TestParsedThatPostModifierFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentence.TestParsedInvalidSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentence.TestParsedSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.TestParsedSentenceBodyFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedAllValueAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedClosureAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedConceptAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedConceptDefinitionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedConceptSubsumptionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedMutualConceptExclusionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedNegativeAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedPositiveAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedProbableAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.conceptassertion.TestParsedValuePartitionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.entitydeclaration.TestParsedConceptDeclarationFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.entitydeclaration.TestParsedInstanceDeclarationFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.entitydeclaration.TestParsedRelationshipDeclarationFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedImportsSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedReferenceConceptSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedReferenceInstanceSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedReferenceRelationSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedUseMultipleOntologiesSentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.imports.TestParsedUseSingleOntologySentenceFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.instanceassertion.TestParsedDifferentInstancesFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.instanceassertion.TestParsedInstanceAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.instanceassertion.TestParsedNegativeInstanceAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.instanceassertion.TestParsedPositiveInstanceAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.instanceassertion.TestParsedSameInstancesFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedEquivalenceRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedFunctionalRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedInverseFunctionalRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedInverseRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedIrreflexiveRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedLiteralModifierFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedLiteralRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedMutualRelationExclusionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedReflexiveRelationAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedRelationDomainAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedRelationRangeAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedRelationSubsumptionAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.relationassertion.TestParsedRelationshipAssertionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.roleinclusion.TestParsedComplexRoleInclusionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.roleinclusion.TestParsedGeneralRoleInclusionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.sentencebody.roleinclusion.TestParsedRoleInclusionFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.subject.TestParsedIndefiniteSubjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.subject.TestParsedNegatedSubjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.subject.TestParsedSubjectFactory;
import uk.co.ordnancesurvey.rabbitparser.testkit.testpart.factory.subject.TestParsedUniversalSubjectFactory;
import uk.co.ordnancesurvey.rabbitparser.util.RbtHashMap;

/**
 * Provides a method for converting a {@link IParsedPart} into the corresponding
 * TestParsedPart implementation of that {@link IParsedPart}.
 * 
 * @author rdenaux
 * 
 */
public class TestPartFactory {

    private static TestPartFactory instance;

    private static Map<Class<? extends IParsedPart>, ITestParsedPartFactory<?>> factoriesByInterface = new RbtHashMap<Class<? extends IParsedPart>, ITestParsedPartFactory<?>>();

    private static final Logger log = Logger.getLogger(TestPartFactory.class.getName());

    public static TestPartFactory getInstance() {
        if (instance == null) {
            instance = new TestPartFactory();
        }
        return instance;
    }

    private TestPartFactory() {
        initFactory();
    }

    private void initFactory() {
        loadFactories();
    }

    /**
	 * Loads the {@link ITestParsedPartFactory}s using a relative package name
	 */
    private void loadFactories() {
        register(new TestParsedComplexCompoundObjectFactory());
        register(new TestParsedComplexConceptObjectFactory());
        register(new TestParsedComplexObjectFactory());
        register(new TestParsedComplexRelationshipPhraseFactory());
        register(new TestParsedFreeTextFactory());
        register(new TestParsedDataValuePhraseFactory());
        register(new TestParsedStringValueFactory());
        register(new TestParsedCompoundRelationshipFactory());
        register(new TestParsedCompoundRelationshipBodyFactory());
        register(new TestParsedDefinitionPhraseFactory());
        register(new TestParsedDefinitionPhraseListBodyFactory());
        register(new TestParsedDefinitionPhraseListFactory());
        register(new TestParsedNegatedCompoundRelationshipFactory());
        register(new TestParsedSubsumptionPhraseFactory());
        register(new TestParsedConceptCandidateFactory());
        register(new TestParsedConceptFactory());
        register(new TestParsedEntityCandidateFactory());
        register(new TestParsedInstanceCandidateFactory());
        register(new TestParsedInstanceFactory());
        register(new TestParsedRelationFactory());
        register(new TestParsedRelationshipCandidateFactory());
        register(new TestParsedTokenFactory());
        register(new TestParsedSynonymFactory());
        register(new TestParsedSynonymListBodyFactory());
        register(new TestParsedSynonymListFactory());
        register(new TestParsedURLFactory());
        register(new TestParsedUrlRefFactory());
        register(new TestParsedLabelFactory());
        register(new TestObjectAndListBodyFactory());
        register(new TestObjectAndListFactory());
        register(new TestObjectListBodyFactory());
        register(new TestObjectListFactory());
        register(new TestParsedCompoundObjectFactory());
        register(new TestParsedConceptObjectFactory());
        register(new TestParsedObjectFactory());
        register(new TestParsedRelationshpPhraseFactory());
        register(new TestParsedArticleFactory());
        register(new TestParsedNumberFactory());
        register(new TestParsedNumberRestrictionFactory());
        register(new TestParsedObjectPrefixFactory());
        register(new TestParsedPrepositionModifierFactory());
        register(new TestParsedThatPostModifierFactory());
        register(new TestParsedInvalidSentenceFactory());
        register(new TestParsedSentenceFactory());
        register(new TestParsedSentenceBodyFactory());
        register(new TestParsedConceptAssertionFactory());
        register(new TestParsedAllValueAssertionFactory());
        register(new TestParsedClosureAssertionFactory());
        register(new TestParsedConceptDefinitionFactory());
        register(new TestParsedConceptSubsumptionFactory());
        register(new TestParsedMutualConceptExclusionFactory());
        register(new TestParsedNegativeAssertionFactory());
        register(new TestParsedPositiveAssertionFactory());
        register(new TestParsedProbableAssertionFactory());
        register(new TestParsedValuePartitionFactory());
        register(new TestParsedConceptDeclarationFactory());
        register(new TestParsedInstanceDeclarationFactory());
        register(new TestParsedRelationshipDeclarationFactory());
        register(new TestParsedImportsSentenceFactory());
        register(new TestParsedReferenceConceptSentenceFactory());
        register(new TestParsedReferenceInstanceSentenceFactory());
        register(new TestParsedReferenceRelationSentenceFactory());
        register(new TestParsedUseMultipleOntologiesSentenceFactory());
        register(new TestParsedUseSingleOntologySentenceFactory());
        register(new TestParsedInstanceAssertionFactory());
        register(new TestParsedNegativeInstanceAssertionFactory());
        register(new TestParsedPositiveInstanceAssertionFactory());
        register(new TestParsedDifferentInstancesFactory());
        register(new TestParsedSameInstancesFactory());
        register(new TestParsedRelationshipAssertionFactory());
        register(new TestParsedEquivalenceRelationAssertionFactory());
        register(new TestParsedFunctionalRelationAssertionFactory());
        register(new TestParsedInverseFunctionalRelationAssertionFactory());
        register(new TestParsedInverseRelationAssertionFactory());
        register(new TestParsedIrreflexiveRelationAssertionFactory());
        register(new TestParsedLiteralModifierFactory());
        register(new TestParsedLiteralRelationAssertionFactory());
        register(new TestParsedMutualRelationExclusionFactory());
        register(new TestParsedReflexiveRelationAssertionFactory());
        register(new TestParsedRelationDomainAssertionFactory());
        register(new TestParsedRelationRangeAssertionFactory());
        register(new TestParsedRelationSubsumptionAssertionFactory());
        register(new TestParsedComplexRoleInclusionFactory());
        register(new TestParsedGeneralRoleInclusionFactory());
        register(new TestParsedRoleInclusionFactory());
        register(new TestParsedIndefiniteSubjectFactory());
        register(new TestParsedNegatedSubjectFactory());
        register(new TestParsedSubjectFactory());
        register(new TestParsedUniversalSubjectFactory());
    }

    public <T extends IParsedPart> ITestParsedPart<T> convert(T aParsedPart, Class<T> aClassHint) {
        ITestParsedPartFactory<?> factory = factoriesByInterface.get(aClassHint);
        if (factory == null) {
            throw new RabbitRuntimeException("factory missing for type " + aClassHint + " registered are " + factoriesByInterface);
        }
        ITestParsedPart<T> result = null;
        try {
            log.fine("Converting " + aParsedPart + " into a test parsed part " + " using " + factory + " because hintClass " + aClassHint + " equals the supported class " + factory.getSupportedClass());
            result = convert(factory, aParsedPart);
        } catch (InvalidTestParsedPartException ex) {
            throw new RabbitRuntimeException("Unexpected error " + ex.getMessage(), ex);
        }
        return result;
    }

    /**
	 * Converts aParsedPartList into an equivalent list containing all parsed
	 * parts implementing the {@link ITestParsedPart} interface.
	 * 
	 * @param <T>
	 * @param aParsedPartList
	 * @return
	 */
    public <T extends IParsedPart> List<ITestParsedPart<T>> convert(List<T> aParsedPartList, Class<T> aHintClass) {
        List<ITestParsedPart<T>> result = new ArrayList<ITestParsedPart<T>>();
        for (T aListElt : aParsedPartList) {
            result.add(convert(aListElt, aHintClass));
        }
        return result;
    }

    /**
	 * Tries to converts aParsedPart using aPPClass. Throws a
	 * {@link InvalidTestParsedPartException} if aPPClass cannot convert
	 * aParsedPart.
	 * 
	 * @param <T>
	 * @param aPPClass
	 * @param aParsedPart
	 * @return
	 * @throws InvalidTestParsedPartException
	 */
    private <T extends IParsedPart> ITestParsedPart<T> convert(ITestParsedPartFactory<?> aPPFactory, T aParsedPart) throws InvalidTestParsedPartException {
        T toConvert = aParsedPart;
        if (aParsedPart instanceof IDelegatingParsedPart<?>) {
            toConvert = ((IDelegatingParsedPart<T>) aParsedPart).getDelegate();
        }
        return (ITestParsedPart<T>) aPPFactory.convertToTestPart(toConvert);
    }

    /**
	 * Adds aTestPPFactory to the {@link Set} of {@link #testPPFactories}
	 * 
	 * @param aTestPPFactory
	 */
    public static void register(ITestParsedPartFactory<?> aTestPPFactory) {
        factoriesByInterface.put(aTestPPFactory.getSupportedClass(), aTestPPFactory);
    }
}
