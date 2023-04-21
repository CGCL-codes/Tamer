package proj.zoie.impl.indexing;

import java.io.File;
import org.apache.lucene.index.IndexReader;
import proj.zoie.api.indexing.ZoieIndexableInterpreter;

/**
 * @deprecated use {@link ZoieSystem#buildDefaultInstance(File, ZoieIndexableInterpreter, int, long, boolean)}
 * @param <V>
 */
public class SimpleZoieSystem<V> extends ZoieSystem<IndexReader, V> {

    /**
	 * @param idxDir
	 * @param interpreter
	 * @param batchSize
	 * @param batchDelay
	 */
    public SimpleZoieSystem(File idxDir, ZoieIndexableInterpreter<V> interpreter, int batchSize, long batchDelay) {
        super(idxDir, interpreter, new DefaultIndexReaderDecorator(), null, null, batchSize, batchDelay, true);
    }
}
