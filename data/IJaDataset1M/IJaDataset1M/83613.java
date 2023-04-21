package cc.mallet.extract.test;

import junit.framework.*;
import java.io.IOException;
import java.io.File;
import cc.mallet.extract.CRFExtractor;
import cc.mallet.extract.DocumentViewer;
import cc.mallet.extract.Extraction;
import cc.mallet.fst.CRF;
import cc.mallet.fst.CRFTrainerByLabelLikelihood;
import cc.mallet.fst.tests.TestCRF;
import cc.mallet.fst.tests.TestMEMM;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.iterator.ArrayIterator;
import cc.mallet.types.InstanceList;

/**
 * Created: Mar 30, 2005
 *
 * @author <A HREF="mailto:casutton@cs.umass.edu>casutton@cs.umass.edu</A>
 * @version $Id: TestDocumentViewer.java,v 1.1 2007/10/22 21:38:02 mccallum Exp $
 */
public class TestDocumentViewer extends TestCase {

    public TestDocumentViewer(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestDocumentViewer.class);
    }

    private File outputDir = new File("extract");

    public void testSpaceViewer() throws IOException {
        Pipe pipe = TestMEMM.makeSpacePredictionPipe();
        String[] data0 = { TestCRF.data[0] };
        String[] data1 = { TestCRF.data[1] };
        InstanceList training = new InstanceList(pipe);
        training.addThruPipe(new ArrayIterator(data0));
        InstanceList testing = new InstanceList(pipe);
        testing.addThruPipe(new ArrayIterator(data1));
        CRF crf = new CRF(pipe, null);
        crf.addFullyConnectedStatesForLabels();
        CRFTrainerByLabelLikelihood crft = new CRFTrainerByLabelLikelihood(crf);
        crft.trainIncremental(training);
        CRFExtractor extor = TestLatticeViewer.hackCrfExtor(crf);
        Extraction extraction = extor.extract(new ArrayIterator(data1));
        if (!outputDir.exists()) outputDir.mkdir();
        DocumentViewer.writeExtraction(outputDir, extraction);
    }

    public static void main(String[] args) throws Throwable {
        TestSuite theSuite;
        if (args.length > 0) {
            theSuite = new TestSuite();
            for (int i = 0; i < args.length; i++) {
                theSuite.addTest(new TestDocumentViewer(args[i]));
            }
        } else {
            theSuite = (TestSuite) suite();
        }
        junit.textui.TestRunner.run(theSuite);
    }
}
