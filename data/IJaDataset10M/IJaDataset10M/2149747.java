package org.jcvi.common.core.seq.fastx.fastq;

import org.jcvi.common.core.util.Builder;

/**
 * {@code FastqFileDataStoreBuilderVisitor} is a {@link FastqFileVisitor}
 * that will build a {@link FastqDataStore}
 * during the visit callbacks.  After the entire
 * file has been visited,
 * the  {@link #build()} method can be called to 
 * return a new instance of a {@link FastqDataStore}.
 * Instances of {@link FastqFileDataStoreBuilderVisitor}
 * are not thread-safe unless otherwise specified.
 * @author dkatzel
 *
 */
public interface FastqFileDataStoreBuilderVisitor extends FastqFileVisitor, Builder<FastqDataStore> {
}
