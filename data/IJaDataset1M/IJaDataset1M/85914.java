package nmazurs.sample_gae.server.word_connections.processor.impl;

import java.util.Map;
import nmazurs.commons.generic.SizableIterable;
import nmazurs.sample_gae.server.word_connections.pool.WordConnectionPool;
import nmazurs.sample_gae.server.word_connections.pool.WordConnectionPoolContainer;
import nmazurs.sample_gae.server.word_connections.pool.WordConnectionStatistic;
import nmazurs.sample_gae.server.word_connections.pool.impl.WordConnectionPoolImpl;
import nmazurs.sample_gae.server.word_connections.processor.SentenceProcessor;
import nmazurs.sample_gae.server.word_connections.processor.TextProcessor;
import org.apache.commons.lang.StringUtils;

/**
 * @author Normunds Mazurs (MAZE)
 * 
 */
public class TextProcessorImpl implements TextProcessor, WordConnectionPoolContainer {

    protected WordConnectionPool createWordConnectionPool() {
        return WordConnectionPoolImpl.create();
    }

    private final WordConnectionPool pool = createWordConnectionPool();

    protected TextProcessorImpl() {
    }

    public static TextProcessor create() {
        return new TextProcessorImpl();
    }

    @Override
    public WordConnectionPool getWordConnectionPool() {
        return pool;
    }

    @Override
    public SizableIterable<WordConnectionStatistic> getAllWordConnections() {
        return getWordConnectionPool().getAllWordConnections();
    }

    @Override
    public SizableIterable<WordConnectionStatistic> getSortedAllWordConnections() {
        return getWordConnectionPool().getSortedAllWordConnections();
    }

    @Override
    public Map<String, ? extends WordConnectionStatistic> getAllWordConnectionsMap() {
        return getWordConnectionPool().getAllWordConnectionsMap();
    }

    @Override
    public void processText(final String text) {
        final SentenceProcessor sentenceProcessor = SentenceProcessorImpl.create(this);
        final String sentences[] = StringUtils.split(text, "\r\n.;!?:");
        for (final String sentence : sentences) {
            sentenceProcessor.processSentence(sentence);
        }
    }
}
