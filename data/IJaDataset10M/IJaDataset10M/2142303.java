package net.openchrom.chromatogram.msd.converter.supplier.cdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import org.eclipse.core.runtime.NullProgressMonitor;
import net.openchrom.chromatogram.msd.converter.chromatogram.ChromatogramConverter;
import net.openchrom.chromatogram.msd.converter.exceptions.FileIsEmptyException;
import net.openchrom.chromatogram.msd.converter.exceptions.FileIsNotReadableException;
import net.openchrom.chromatogram.msd.converter.exceptions.FileIsNotWriteableException;
import net.openchrom.chromatogram.msd.converter.exceptions.NoChromatogramConverterAvailableException;
import net.openchrom.chromatogram.msd.model.core.IChromatogram;
import net.openchrom.chromatogram.msd.model.core.IChromatogramOverview;
import junit.framework.TestCase;

public class CDFConverterTestXP extends TestCase {

    private static final String EXTENSION_POINT_ID = "net.openchrom.chromatogram.msd.converter.supplier.cdf";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testConvert_1() {
        Date start;
        Date stop;
        String path = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_OP17760);
        File chromatogram = new File(path);
        String pathExport = "E:\\tmp\\netCDF\\131108.CDF";
        File chromatogramExport = new File(pathExport);
        try {
            start = new Date();
            IChromatogram chrom = ChromatogramConverter.convert(chromatogram, EXTENSION_POINT_ID, new NullProgressMonitor());
            stop = new Date();
            System.out.println("Milliseconds Lesen: " + (stop.getTime() - start.getTime()));
            assertEquals("Scans", 5726, chrom.getNumberOfScans());
            assertEquals("TS", 55822.0f, chrom.getScan(3).getTotalSignal());
            start = new Date();
            File test = ChromatogramConverter.convert(chromatogramExport, chrom, EXTENSION_POINT_ID, new NullProgressMonitor());
            stop = new Date();
            System.out.println("Milliseconds Schreiben: " + (stop.getTime() - start.getTime()));
            assertEquals("File path", pathExport, test.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIsNotReadableException e) {
            e.printStackTrace();
        } catch (FileIsEmptyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FileIsNotWriteableException e) {
            e.printStackTrace();
        } catch (NoChromatogramConverterAvailableException e) {
            e.printStackTrace();
        }
    }

    public void testConvert_2() {
        Date start;
        Date stop;
        String path = TestPathHelper.getAbsolutePath(TestPathHelper.TESTFILE_IMPORT_OP17760);
        File chromatogram = new File(path);
        try {
            start = new Date();
            IChromatogramOverview chrom = ChromatogramConverter.convertOverview(chromatogram, EXTENSION_POINT_ID, new NullProgressMonitor());
            stop = new Date();
            System.out.println("Milliseconds Lesen Overview: " + (stop.getTime() - start.getTime()));
            assertEquals("Scans", 5726, chrom.getNumberOfScans());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (FileIsNotReadableException e) {
            e.printStackTrace();
        } catch (FileIsEmptyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoChromatogramConverterAvailableException e) {
            e.printStackTrace();
        }
    }
}
