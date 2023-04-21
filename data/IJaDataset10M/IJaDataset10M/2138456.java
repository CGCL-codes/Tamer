package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.entitydeclaration;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.OWLAPIRabbitParserClient;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.base.BaseSentenceConverterTest;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.testkit.RbtToOwlConverterOntologyFixture;
import uk.co.ordnancesurvey.rabbitparser.testkit.fixture.SentenceFixtures;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Tests class {@link ConceptDeclarationConverter}
 * 
 * @author rdenaux
 * 
 */
public class ConceptDeclarationConverterTest extends BaseSentenceConverterTest<ConceptDeclarationConverter> {

    @Test
    public void testConvertOk() throws Exception {
        assertConversionEqualsOntologyDifference(RbtToOwlConverterOntologyFixture.EmptyOntology, RbtToOwlConverterOntologyFixture.RiverIsAConcept, SentenceFixtures.Ids.RiverIsAConcept);
    }

    @Test
    public void testConvertSecondaryConceptDeclarationOk() throws Exception {
        assertConversionEqualsOntologyDifference(RbtToOwlConverterOntologyFixture.EmptyOntology, RbtToOwlConverterOntologyFixture.RiverIsASecondaryConcept, SentenceFixtures.Ids.RiverIsASecondaryConcept);
    }

    @Test
    public void testConvertWithDisambiguatorOk() throws Exception {
        assertConversionEqualsOntologyDifference(RbtToOwlConverterOntologyFixture.EmptyOntology, RbtToOwlConverterOntologyFixture.DamWaterIsAConcept, SentenceFixtures.Ids.DamWaterIsAConcept);
    }

    @Override
    protected ConceptDeclarationConverter createTestObj(OWLAPIRabbitParserClient aPC) {
        OWLOntology targetOnt = aPC.getTargetOntology();
        Injector injector = Guice.createInjector(getTestRegisteredMappingModule());
        return new ConceptDeclarationConverter(targetOnt, r2oMapperFor(injector, targetOnt));
    }
}
