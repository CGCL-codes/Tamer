package photospace.search;

import java.io.*;
import photospace.meta.*;

/**
 * C5
 */
public interface Indexer {

    int index(Meta[] metas, boolean fromScratch) throws IOException;

    void merge(Meta[] metas) throws Exception;

    void delete(Meta[] metas) throws IOException;
}
