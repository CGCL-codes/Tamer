package eu.planets_project.ifr.core.services;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import junit.framework.Assert;
import eu.planets_project.services.compare.CompareResult;
import eu.planets_project.services.datatypes.Content;
import eu.planets_project.services.datatypes.DigitalObject;
import eu.planets_project.services.datatypes.Parameter;
import eu.planets_project.services.utils.FileUtils;

/**
 * Sample XCL services usage implementation using text files.
 * @see AbstractSampleXclUsage
 * @author Fabian Steeg (fabian.steeg@uni-koeln.de)
 */
public class SampleXclUsageTextDocxPdf extends AbstractSampleXclUsage {

    private static final String RES = "PP/xcl/src/test/resources/";

    private static final String SAMPLES = RES + "sample_files/basi0g01.";

    private static final String DOCX = "docx";

    private static final String PDF = "pdf";

    private static final String DOCX_FILE = SAMPLES + DOCX;

    private static final String PDF_FILE = SAMPLES + PDF;

    private static final String COCO = RES + "cocoText.xml";

    /**
     * {@inheritDoc}
     * @see eu.planets_project.ifr.core.services.AbstractSampleXclUsage#files()
     */
    @Override
    protected DigitalObject[] files() {
        return new DigitalObject[] { new DigitalObject.Builder(Content.byReference(new File(DOCX_FILE))).format(ids()[0]).build(), new DigitalObject.Builder(Content.byReference(new File(PDF_FILE))).format(ids()[1]).build() };
    }

    /**
     * {@inheritDoc}
     * @see eu.planets_project.ifr.core.services.AbstractSampleXclUsage#ids()
     */
    @Override
    protected URI[] ids() {
        return new URI[] { null, REGISTRY.getUrisForExtension(PDF).iterator().next() };
    }

    /**
     * {@inheritDoc}
     * @see eu.planets_project.ifr.core.services.AbstractSampleXclUsage#config()
     */
    @Override
    protected DigitalObject config() {
        return new DigitalObject.Builder(Content.byReference(new File(COCO))).build();
    }

    /**
     * {@inheritDoc}
     * @see eu.planets_project.ifr.core.services.AbstractSampleXclUsage#parameters()
     */
    @Override
    protected List<List<Parameter>> parameters() {
        ArrayList<List<Parameter>> list = new ArrayList<List<Parameter>>();
        list.add(Arrays.asList(new Parameter("optionalXCELString", FileUtils.readTxtFileIntoString(new File("PP/xcl/src/test/resources/sample_files/xcel_docx.xml")))));
        list.add(null);
        return list;
    }

    /**
     * {@inheritDoc}
     * @see eu.planets_project.ifr.core.services.AbstractSampleXclUsage#checkCompareResult(eu.planets_project.services.compare.CompareResult)
     */
    @Override
    protected void checkCompareResult(CompareResult result) {
        Assert.assertTrue("Text file comparison result should contain only embedded results", result.getProperties().size() == 0 && result.getResults().size() > 0);
    }
}
