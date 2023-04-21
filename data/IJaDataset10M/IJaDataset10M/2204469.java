package org.jcvi.fasta;

import org.jcvi.fasta.fastq.AllFastqUnitTests;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TestQualityFastaRecord.class, TestDefaultEncodedNuclotideFastaRecord.class, TestDefaultQualityFastaMap.class, TestDefaultSequenceFastaMap.class, TestLargeSequenceFastaMap.class, TestDefaultSequenceFastaDataStoreWithNoComment.class, TestLargeSequenceFastaMapWithNoComment.class, TestFlowgramQualityFastaMap.class, TestPositionFastaRecord.class, TestDefaultPositionsFastaDataStore.class, TestLargePositionsFastaMap.class, TestLargeQualityFastaMap.class, TestNucleotideFastaH2DataStore.class, TestFilteredNucleotideFastaH2DataStore.class, TestQualityFastaH2DataStore.class, TestFilteredQualityFastaH2DataStore.class, TestNucleotideDataStoreFastaAdatper.class, AllFastqUnitTests.class })
public class AllFastaUnitTests {
}
