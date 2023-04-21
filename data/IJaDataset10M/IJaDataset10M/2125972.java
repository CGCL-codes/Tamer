package net.openchrom.chromatogram.msd.batchprocess.core;

import java.io.File;
import org.eclipse.core.runtime.NullProgressMonitor;
import net.openchrom.chromatogram.msd.batchprocess.TestPathHelper;
import net.openchrom.chromatogram.msd.batchprocess.core.BatchProcess;
import net.openchrom.chromatogram.msd.batchprocess.model.BatchProcessJob;
import net.openchrom.chromatogram.msd.batchprocess.model.ChromatogramInputEntry;
import net.openchrom.chromatogram.msd.batchprocess.model.ChromatogramOutputEntry;
import net.openchrom.chromatogram.msd.batchprocess.model.ChromatogramProcessEntry;
import net.openchrom.chromatogram.msd.batchprocess.model.IBatchProcessJob;
import net.openchrom.chromatogram.msd.batchprocess.model.ProcessType;
import junit.framework.TestCase;

/**
 * @author Philip (eselmeister) Wenig
 * 
 */
public class BatchProcess_1_ITest extends TestCase {

    private IBatchProcessJob batchProcessJob;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        batchProcessJob = new BatchProcessJob();
        String inputChromatogram = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_TEST);
        String outputChromatogram = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_EXPORT_TEST);
        File output = new File(outputChromatogram);
        if (output.exists()) {
            output.delete();
        }
        batchProcessJob.setReportFolder("/somefolder");
        batchProcessJob.setOverrideReport(true);
        batchProcessJob.getChromatogramInputEntries().add(new ChromatogramInputEntry(inputChromatogram));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.FILTER, "net.openchrom.chromatogram.msd.filter.supplier.denoising"));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.FILTER, "net.openchrom.chromatogram.msd.filter.supplier.savitzkygolay"));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.BASELINE_DETECTOR, "net.openchrom.chromatogram.msd.baseline.detector.supplier.smoothed"));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.PEAK_DETECTOR, "net.openchrom.chromatogram.msd.peak.detector.supplier.chemstation"));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.COMBINED_INTEGRATOR, "net.openchrom.chromatogram.msd.integrator.supplier.chemstation.chemstationIntegrator", true));
        batchProcessJob.getChromatogramProcessEntries().add(new ChromatogramProcessEntry(ProcessType.PEAK_IDENTIFIER, "net.openchrom.chromatogram.msd.identifier.supplier.nist.peak"));
        batchProcessJob.getChromatogramOutputEntries().add(new ChromatogramOutputEntry(outputChromatogram, "net.openchrom.chromatogram.msd.converter.supplier.cdf"));
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testProcess_1() {
        BatchProcess bp = new BatchProcess();
        bp.execute(batchProcessJob, new NullProgressMonitor());
        File file = new File(TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_EXPORT_TEST));
        assertNotNull(file);
    }
}
