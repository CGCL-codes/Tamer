package org.jcvi.assemblyArchive;

import org.jcvi.assembly.Contig;
import org.jcvi.assembly.PlacedRead;

public interface AssemblyArchiveContigRecord<T extends PlacedRead> {

    Contig<T> getContig();

    String getSubmitterReference();

    ContigConformation getConformation();

    AssemblyArchiveType getType();
}
