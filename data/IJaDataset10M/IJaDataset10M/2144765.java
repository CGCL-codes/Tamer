package org.jcvi.fastX.fasta.qual;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author dkatzel
 *
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ TestQualityFastaRecord.class, TestDefaultQualityFastaDataStore.class, TestFilteredQualityFastaH2DataStore.class, TestFlowgramQualityFastaDataStore.class, TestLargeQualityFastaDataStore.class, TestQualityFastaH2DataStore.class })
public class AllFastaQualityTests {
}
