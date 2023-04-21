package uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.conceptassertion;

import org.junit.Test;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.OWLAPIRabbitParserClient;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.mapper.RabbitToOwlApiEntityMapper;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.partconverter.OWLObjectFromParsedPartFactory;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.impl.base.BaseSentenceConverterTest;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.testkit.RbtToOwlConverterOntologyFixture;
import uk.co.ordnancesurvey.rabbitparser.testkit.fixture.SentenceFixtures;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Test the {@link ConceptDefinitionConverter} class
 * 
 * @author rdenaux
 * 
 */
public class ConceptDefinitionConverterTest extends BaseSentenceConverterTest<ConceptDefinitionConverter> {

    @Test
    public void testConvertOk() throws Exception {
        assertConversionEqualsOntologyDifference(RbtToOwlConverterOntologyFixture.Prefilled, RbtToOwlConverterOntologyFixture.CityDefinition, SentenceFixtures.Ids.CityDefinition);
    }

    @Override
    protected ConceptDefinitionConverter createTestObj(OWLAPIRabbitParserClient aPC) {
        OWLOntology targetOnt = aPC.getTargetOntology();
        Injector injector = Guice.createInjector(getTestRegisteredMappingModule());
        RabbitToOwlApiEntityMapper r2oMapper = r2oMapperFor(injector, targetOnt);
        OWLObjectFromParsedPartFactory partConverter = new OWLObjectFromParsedPartFactory(r2oMapper);
        return new ConceptDefinitionConverter(targetOnt, r2oMapper, partConverter);
    }
}
