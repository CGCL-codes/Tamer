package geovista.jts.io;

import geovista.jts.feature.*;

/**
 * A stream of features from an external source, which
 * may throw exceptions during processing.
 */
public interface FeatureInputStream {

    public FeatureSchema getFeatureSchema();

    public Feature next() throws Exception;

    public boolean hasNext() throws Exception;

    public void close() throws Exception;
}
