package sketch.issta11.apachecommons;

import sketch.instrumenter.Tracer;
import sketch.main.GeneratingTestsBatch;
import sketch.specs.SketchTestProcessorBatch;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class ApacheCommonsCollectionsTest extends TestCase {

    public String source_dir = null;

    public String output_dir = null;

    public static Test suite() {
        return new TestSuite(ApacheCommonsCollectionsTest.class);
    }

    public void testCorrectProgramTransformation() {
        long t1 = System.currentTimeMillis();
        String src_dir = "/scratch/sketchtest/experiment_workspace/apache/rewritten-tests";
        SketchTestProcessorBatch batchProcessor = new SketchTestProcessorBatch(src_dir);
        String output_dir = "/scratch/sketchtest/experiment_workspace/apache/transformed-tests";
        batchProcessor.generateTestsFromSketchesWithoutExecution(output_dir);
        System.out.println("start to do value instanitation...");
        String final_output = "/scratch/sketchtest/experiment_workspace/apache/final-outputs";
        String entryJUnitClass = "org.apache.commons.collections.TestAll";
        Tracer.clearFile();
        GeneratingTestsBatch.obtainRuntimeInfoAndOutputTests(output_dir, final_output, entryJUnitClass, null);
        long elapsed = System.currentTimeMillis() - t1;
        System.out.println("Total analysis time: " + elapsed + " millis.");
    }
}
