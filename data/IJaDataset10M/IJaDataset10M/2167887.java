package net.sf.lucis.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.util.Version;
import com.google.common.base.Supplier;

/**
 * Support class the default values related to the lucene version for this lucis version.
 * @author Andres Rodriguez
 */
public final class Factory {

    /** Not instantiable. */
    private Factory() {
    }

    /** Instance. */
    private static final Factory INSTANCE = new Factory();

    /** Supplier. */
    private static final IndexWriterConfigSupplier SUPPLIER = new IndexWriterConfigSupplier();

    /** Returns the instance. */
    public static Factory get() {
        return INSTANCE;
    }

    /** Returns the used lucene version. */
    public Version version() {
        return Version.LUCENE_35;
    }

    /** Returns the default standard analyzer. */
    public Analyzer standardAnalyzer() {
        return new StandardAnalyzer(version());
    }

    /** Returns the default writer configuration. */
    public IndexWriterConfig writerConfig() {
        return new IndexWriterConfig(version(), standardAnalyzer());
    }

    /** Returns the default writer configuration supplier. */
    public Supplier<IndexWriterConfig> writerConfigSupplier() {
        return SUPPLIER;
    }

    /** Default IndexWriterConfig provider. */
    private static final class IndexWriterConfigSupplier implements Supplier<IndexWriterConfig> {

        IndexWriterConfigSupplier() {
        }

        public IndexWriterConfig get() {
            return Factory.get().writerConfig();
        }

        @Override
        public String toString() {
            return "Default IndexWriterConfig provider";
        }
    }
}
