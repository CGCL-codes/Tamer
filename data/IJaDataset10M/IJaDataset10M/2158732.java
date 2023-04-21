package uk.co.ordnancesurvey.rabbitparser.owlapiconverter;

import java.net.URI;
import java.util.List;
import org.semanticweb.owl.model.OWLOntologyChange;
import org.semanticweb.owl.model.OWLOntologyChangeException;
import org.semanticweb.owl.model.OWLOntologyManager;
import uk.co.ordnancesurvey.rabbitparser.DeclarativeSentenceType;
import uk.co.ordnancesurvey.rabbitparser.IInvalidSentence;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParsedResult;
import uk.co.ordnancesurvey.rabbitparser.IRabbitParserClient;
import uk.co.ordnancesurvey.rabbitparser.IRabbitToOWLConverter;
import uk.co.ordnancesurvey.rabbitparser.exception.RabbitException;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.IParsedSentenceConverter;
import uk.co.ordnancesurvey.rabbitparser.owlapiconverter.sentenceconverter.ParsedSentenceToOWLConverterFactory;
import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;

/**
 * RabbitToOWLConverter that uses an OWLOntologyManager as its context
 * 
 * @author rdenaux
 * 
 */
public class RabbitToOWLAPIConverter implements IRabbitToOWLConverter {

    private enum ConversionMode {

        ADD_SENTENCE, REMOVE_SENTENCE
    }

    /**
	 * The OWLAPI model manager, which provides the methods required for
	 * changing the ontology
	 */
    private OWLOntologyManager modelManager;

    /**
	 * The parserClient used to search for OWL entities in the ontology. This is
	 * the same parserClient used during the parsing of the sentences.
	 */
    private IOWLAPIRabbitParserClient parserClient;

    public RabbitToOWLAPIConverter(OWLOntologyManager context) {
        modelManager = context;
    }

    /**
	 * Converts aParsedResult into OWL
	 */
    public void convertToOWL(IRabbitParsedResult aParsedResult) throws RabbitException {
        if (aParsedResult != null) {
            List<IInvalidSentence> invalids = aParsedResult.getInvalidSentences();
            if (invalids != null && !invalids.isEmpty()) {
                throw new RabbitException("Cannot convert parsed result because it contains invalid sentences");
            } else {
                extractParserClient(aParsedResult);
                for (IParsedSentence sentence : aParsedResult.getParsedSentences()) {
                    convert(sentence);
                }
            }
        }
    }

    public void retractFromOWL(IParsedSentence aParsedSentence) throws RabbitException {
        if (aParsedSentence == null) {
            throw new RabbitException("Cannot retract null sentence");
        }
        if (!aParsedSentence.containsAllNecessaryInformation()) {
            throw new RabbitException("Cannot retract sentence that contains errors");
        }
        extractParserClient(aParsedSentence.getWholeParsedResult());
        revert(aParsedSentence);
    }

    /**
	 * Extracts and sets this.parserClient to the parserClient of aParsedResult.
	 * 
	 * @param aParsedResult
	 */
    private void extractParserClient(IRabbitParsedResult aParsedResult) {
        IRabbitParserClient iParserClient = aParsedResult.getParserClient();
        assert iParserClient instanceof IOWLAPIRabbitParserClient : "Parserclient must be based on the owlapiConverter implementation" + iParserClient;
        parserClient = (IOWLAPIRabbitParserClient) iParserClient;
    }

    /**
	 * Implements the conversion of aParsedSentence into OWL
	 * 
	 * @param aParsedSentence
	 */
    private void convert(IParsedSentence aParsedSentence) throws RabbitException {
        applySentenceToOWL(aParsedSentence, ConversionMode.ADD_SENTENCE);
    }

    /**
	 * Implements the reversion (or deletion) of aParsedSentence from OWL.
	 * 
	 * @param aParsedSentence
	 * @throws RabbitException
	 */
    private void revert(IParsedSentence aParsedSentence) throws RabbitException {
        applySentenceToOWL(aParsedSentence, ConversionMode.REMOVE_SENTENCE);
    }

    /**
	 * 
	 * @param aParsedSentence
	 * @throws RabbitException
	 */
    private void applySentenceToOWL(IParsedSentence aParsedSentence, ConversionMode conversionMode) throws RabbitException {
        assert aParsedSentence != null;
        assert aParsedSentence.containsAllNecessaryInformation() : "Cannot convert sentence because it doesn't contain all required information" + aParsedSentence.getErrors();
        DeclarativeSentenceType sentenceType = aParsedSentence.getSentenceType();
        IParsedSentenceConverter sentenceConverter = ParsedSentenceToOWLConverterFactory.getSentenceConverter(sentenceType);
        if (sentenceConverter == null) {
            throw new RuntimeException("Cannot convert sentence type " + sentenceType);
        }
        URI activeOntologyURI = parserClient.getActiveOntologyURI();
        assert activeOntologyURI != null;
        sentenceConverter.setActiveOntologyURI(activeOntologyURI);
        sentenceConverter.setModelManager(modelManager);
        sentenceConverter.setParserClient(parserClient);
        List<OWLOntologyChange> changes = null;
        switch(conversionMode) {
            case ADD_SENTENCE:
                changes = sentenceConverter.getAddChanges(aParsedSentence);
                break;
            case REMOVE_SENTENCE:
                changes = sentenceConverter.getRemoveChanges(aParsedSentence);
                break;
            default:
                throw new RuntimeException("conversion Mode " + conversionMode + " not supported yet.");
        }
        if (!changes.isEmpty()) {
            applyChanges(changes);
        }
    }

    /**
	 * Applies aChanges to the ontology.
	 * 
	 * This method is protected to allow subclasses to override this behaviour.
	 * It is the responsability of subclasses to either commit the changes to
	 * the ontology or to throw a RabbitException when this is not possible.
	 * 
	 * @param aChanges
	 * @throws RabbitException
	 */
    protected void applyChanges(List<OWLOntologyChange> aChanges) throws RabbitException {
        try {
            modelManager.applyChanges(aChanges);
        } catch (OWLOntologyChangeException e) {
            throw new RabbitException("Error applying changes", e);
        }
    }
}
