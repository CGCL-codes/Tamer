package uk.co.ordnancesurvey.rabbitparser.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.corpora.DocumentContentImpl;
import gate.creole.ExecutionException;
import gate.creole.ExecutionInterruptedException;
import gate.creole.ResourceInstantiationException;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import uk.co.ordnancesurvey.rabbitparser.exception.RabbitException;
import uk.co.ordnancesurvey.rabbitparser.exception.RabbitParsingInterruptedException;
import uk.co.ordnancesurvey.rabbitparser.gate.gatetoresultconverter.GAnnToParsedEntityConverter;
import uk.co.ordnancesurvey.rabbitparser.parsedsentencepart.entity.IParsedEntity;

/**
 * Provides method {@link #getLexicalEntities(String)}, which returns the set of
 * possible entities that the gate rabbit parser would suggest for a given
 * input. For example, input "River" would result in a potential IParsedConcept,
 * but not in a IParsedRelation because the input is not a verb phrase.
 * 
 * @author rdenaux
 * 
 */
public class GateRbtLexicalEntityFinder {

    private static final Logger log = Logger.getLogger(GateRbtLexicalEntityFinder.class.getName());

    private static final String DOCUMENT_NAME = "Rabbit Entity Finder input document";

    /**
	 * Gate application which is used to parse the given input
	 */
    private CorpusController gateApp;

    /**
	 * By default, corpus only contains this one document.
	 */
    private Document corpusDocument;

    public GateRbtLexicalEntityFinder(CorpusController aGateApp) throws ResourceInstantiationException {
        gateApp = aGateApp;
        final Corpus corpus = Factory.newCorpus("Rabbit Entity Finder Corpus");
        corpusDocument = Factory.newDocument("");
        corpusDocument.setName(DOCUMENT_NAME);
        corpus.add(corpusDocument);
        gateApp.setCorpus(corpus);
    }

    public Set<IParsedEntity> getLexicalEntities(String aEntityLabel) throws RabbitException {
        corpusDocument.cleanup();
        corpusDocument.setContent(new DocumentContentImpl(aEntityLabel));
        final Set<IParsedEntity> result = new HashSet<IParsedEntity>();
        try {
            log.fine("executing gateApp");
            gateApp.execute();
            if (gateApp.isInterrupted()) {
                throw new RabbitParsingInterruptedException();
            }
            result.addAll(extractParsedEntities(corpusDocument));
        } catch (final ExecutionInterruptedException e) {
            throw new RabbitParsingInterruptedException(e);
        } catch (final ExecutionException e) {
            final Throwable cause = e.getCause();
            if (cause != null && cause instanceof RabbitException) {
                throw (RabbitException) cause;
            } else {
                e.printStackTrace();
                throw new RabbitException("Unexpected error parsing '" + aEntityLabel + "'", e);
            }
        } catch (final ConcurrentModificationException ex) {
            log.severe("Concurrent modification " + ex);
            throw new RabbitException("Concurrent modification ", ex);
        } finally {
            corpusDocument.cleanup();
        }
        return result;
    }

    private Set<IParsedEntity> extractParsedEntities(Document aDoc) {
        Set<IParsedEntity> result = new HashSet<IParsedEntity>();
        Long begin = 0L;
        Long end = aDoc.getContent().size();
        AnnotationSet annsOverWhole = aDoc.getAnnotations().get(begin, end);
        Iterator<Annotation> anns = annsOverWhole.iterator();
        GAnnToParsedEntityConverter entityExtractor = new GAnnToParsedEntityConverter();
        while (anns.hasNext()) {
            Annotation ann = anns.next();
            if (entityExtractor.canConvert(ann)) {
                IParsedEntity ent = entityExtractor.convert(ann);
                if (ent.getSpan().getLength() == end) result.add(ent);
            }
        }
        return result;
    }
}
