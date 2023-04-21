package org.jcvi.assemblyArchive;

import java.util.Date;
import java.util.Set;
import org.jcvi.assembly.PlacedRead;

public interface AssemblyArchive<T extends PlacedRead> {

    String getSubmitterReference();

    AssemblyArchiveType getType();

    Set<AssemblyArchiveContigRecord<T>> getContigRecords();

    String getCenterName();

    long getTaxonId();

    String getDescription();

    String getStructure();

    Date getSubmissionDate();

    int getNumberOfContigs();

    long getNumberOfTraces();

    long getNumberOfContigBases();

    long getNumberOfTotalBasecalls();

    double getCoverageRatio();
}
