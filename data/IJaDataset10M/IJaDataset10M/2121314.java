package eu.annocultor.tests;

import java.io.PrintWriter;
import org.junit.Ignore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import eu.annocultor.api.CustomConverter;
import eu.annocultor.api.DataObjectPreprocessor;
import eu.annocultor.api.Factory;
import eu.annocultor.api.ObjectRule;
import eu.annocultor.api.Task;
import eu.annocultor.context.Environment;
import eu.annocultor.context.EnvironmentImpl;
import eu.annocultor.data.sources.XmlDataSource;
import eu.annocultor.rules.ObjectRuleImpl;
import eu.annocultor.tagger.vocabularies.VocabularyOfPeople;
import eu.annocultor.tagger.vocabularies.VocabularyOfPlaces;
import eu.annocultor.tagger.vocabularies.VocabularyOfTerms;
import eu.annocultor.xconverter.api.DataObject;
import eu.annocultor.xconverter.api.GeneratedConverterInt;
import eu.annocultor.xconverter.api.GeneratedCustomConverterInt;
import eu.annocultor.xconverter.impl.XConverterFactory;

/**
 * Converter for project
 * id: Ese
 * institution: Europeana project
 * publisherId: 000
 *
 **/
@Ignore
public class GeneratedConverter implements GeneratedConverterInt {

    Environment environment = new EnvironmentImpl() {

        @Override
        public void init() {
            setParameter(Environment.PARAMETERS.ANNOCULTOR_VOCABULARY_DIR, "vocabularies");
            setParameter(Environment.PARAMETERS.ANNOCULTOR_TMP_DIR, "tmp");
            setParameter(Environment.PARAMETERS.ANNOCULTOR_INPUT_DIR, "input_source/");
            setParameter(Environment.PARAMETERS.ANNOCULTOR_OUTPUT_DIR, "output_rdf");
            setParameter(Environment.PARAMETERS.ANNOCULTOR_DOC_DIR, "doc");
        }
    };

    XConverterFactory factory = new XConverterFactory(environment) {

        @Override
        public void init() throws Exception {
            addNamespace("dc", "http://purl.org/dc/elements/1.1/");
            addNamespace("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
            addNamespace("oldskos", "http://www.w3.org/2004/02/skos/core#");
            addNamespace("ac", "http://annocultor.eu/XConverter/");
            addNamespace("rdf", "http://www.w3.org/1999/02/22-rdf-syntax-ns#");
            addNamespace("ese", "http://www.europeana.eu/ese");
            addNamespace("europeana", "http://www.europeana.eu");
            addNamespace("skos", "http://www.w3.org/2008/05/skos#");
            addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
            addNamespace("dcterms", "http://purl.org/dc/terms/");
            addVocabularyOfTerms("terms", new VocabularyOfTerms("terms", null) {

                @Override
                public void init() throws Exception {
                }

                @Override
                public String onNormaliseLabel(String label, NormaliseCaller caller) throws Exception {
                    return label.toLowerCase();
                }
            });
            addVocabularyOfPeople("people", new VocabularyOfPeople("people", null) {

                @Override
                public void init() throws Exception {
                }

                @Override
                public String onNormaliseLabel(String label, NormaliseCaller caller) throws Exception {
                    return super.onNormaliseLabel(label.toLowerCase(), caller);
                }
            });
            addVocabularyOfPlaces("places", new VocabularyOfPlaces("places", null) {

                @Override
                public void init() throws Exception {
                }

                @Override
                public String onNormaliseLabel(String label, NormaliseCaller caller) throws Exception {
                    return label.toLowerCase();
                }
            });
        }
    };

    private class Ese extends CustomConverter implements GeneratedCustomConverterInt {

        Logger log = LoggerFactory.getLogger(Ese.class.getName());

        Task task = Factory.makeTask("Ese", "Ese", "Europeana project", factory.makeNamespace("europeana"), environment);

        ObjectRule subjectRule;

        public Ese() throws Exception {
            super();
            factory.addGraph("People", Factory.makeGraph(task, "People", ""));
            factory.addGraph("Places", Factory.makeGraph(task, "Places", ""));
            factory.addGraph("PeopleLinks", Factory.makeGraph(task, "PeopleLinks", ""));
            factory.addGraph("PlacesLinks", Factory.makeGraph(task, "PlacesLinks", ""));
            subjectRule = ObjectRuleImpl.makeObjectRule(task, factory.makePath("/metadata/record"), factory.makePath("/metadata/record/europeana:uri"), factory.makePath("/metadata/record/dc:identifier"), null, true);
            subjectRule.addPreprocessor(new DataObjectPreprocessor() {

                public boolean onPreCondition(DataObject sourceDataObject) throws Exception {
                    return true;
                }

                @Override
                public boolean preCondition(DataObject sourceDataObject) throws Exception {
                    return onPreCondition(sourceDataObject);
                }
            });
            subjectRule.addRule(new eu.annocultor.rules.RenameLiteralPropertyRule(factory.makePath("dc:creator1"), factory.makeProperty("dc:creator2"), factory.makeGraph("People")));
            subjectRule.addRule(new eu.annocultor.rules.RenameLiteralPropertyRule(factory.makePath("dc:contributor1"), factory.makeProperty("dc:contributor"), factory.makeGraph("People")));
            subjectRule.addRule(new eu.annocultor.tagger.rules.LookupPersonRule(factory.makePath("dc:creator"), factory.makeProperty("dc:creator"), factory.makeGraph("People"), factory.makeGraph("PeopleLinks"), factory.makePath("dates"), factory.makePath("dates"), factory.makeProperty("dc:creator"), factory.makeString("country"), factory.makeString("( *; *)|( *, *)"), factory.makeVocabularyOfPeople("people")));
            subjectRule.addRule(new eu.annocultor.tagger.rules.LookupPersonRule(factory.makePath("dc:contributor"), factory.makeProperty("dc:contributor"), factory.makeGraph("People"), factory.makeGraph("PeopleLinks"), factory.makePath("dates"), factory.makePath("dates"), factory.makeProperty("dc:contributor"), factory.makeString("contributor"), factory.makeString("( *; *)|( *, *)"), factory.makeVocabularyOfPeople("people")));
            subjectRule.addRule(new eu.annocultor.rules.RenameLiteralPropertyRule(factory.makePath("dc:country2"), factory.makeProperty("dc:country"), factory.makeGraph("Places")));
            subjectRule.addRule(new eu.annocultor.rules.RenameLiteralPropertyRule(factory.makePath("dcterms:spatial1"), factory.makeProperty("dcterms:spatial"), factory.makeGraph("Places")));
            subjectRule.addRule(new eu.annocultor.tagger.rules.LookupPlaceRule(factory.makePath("dc:country"), factory.makeProperty("dc:country"), factory.makeGraph("Places"), factory.makeGraph("PlacesLinks"), factory.makeProperty("dc:country"), factory.makeString("country"), factory.makeString("( *; *)|( *, *)"), factory.makeVocabularyOfPlaces("places")));
            subjectRule.addRule(new eu.annocultor.tagger.rules.LookupPlaceRule(factory.makePath("dcterms:spatial"), factory.makeProperty("dcterms:spatial"), factory.makeGraph("Places"), factory.makeGraph("PlacesLinks"), factory.makeProperty("dc:country"), factory.makeString("spatial"), factory.makeString("( *; *)|( *, *)"), factory.makeVocabularyOfPlaces("places")));
        }

        PrintWriter console;

        public int run(PrintWriter out) throws Exception {
            this.console = out;
            selectDataSource();
            onConversionStarts();
            out.println("Starting generated converter");
            out.flush();
            int result;
            try {
                result = super.run(task, null, 50);
            } catch (Exception e) {
                console.flush();
                throw new Exception("Exception running generated converter", e);
            }
            onConversionEnds();
            return result;
        }

        public void selectDataSource() throws Exception {
            XmlDataSource source = new XmlDataSource(environment, "003*.xml");
            source.setMergeSourceFiles(true);
            task.setDataSource(source);
        }

        public void onConversionStarts() throws Exception {
        }

        public void onConversionEnds() throws Exception {
        }
    }

    public int run(PrintWriter out) throws Exception {
        int result = 0;
        Ese c = new Ese();
        if (c == null) {
            throw new Exception("failed to create a Ese");
        }
        result = c.run(out);
        if (result != 0) {
            throw new Exception("Converter 'Ese' failed. Execution of other converters from this profile is terminated");
        }
        return result;
    }

    public static void main(String args[]) throws Exception {
        GeneratedConverter converter = new GeneratedConverter();
        converter.run(new PrintWriter(System.out));
    }
}
