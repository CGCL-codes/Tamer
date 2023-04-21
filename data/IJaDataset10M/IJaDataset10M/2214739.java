package net.sf.lucis.core.impl;

import java.io.IOException;
import net.sf.lucis.core.Factory;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

/**
 * Empty Directory.
 * @author Andres Rodriguez
 */
final class EmptyDirectory {

    /** Not instantiable. */
    private EmptyDirectory() {
        throw new AssertionError();
    }

    private static volatile Directory directory = null;

    public static Directory get() throws IOException {
        if (directory != null) {
            return directory;
        }
        create();
        return directory;
    }

    private static synchronized void create() throws IOException {
        if (directory != null) {
            return;
        }
        final RAMDirectory ram = new RAMDirectory();
        IndexWriter w = new IndexWriter(ram, Factory.get().writerConfig());
        w.commit();
        w.close();
        directory = ram;
    }
}
