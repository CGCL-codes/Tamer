package uk.ac.ebi.intact.externalservices.searchengine;

import org.junit.Assert;
import org.junit.Test;
import uk.ac.ebi.intact.model.Experiment;
import java.io.StringWriter;
import java.io.Writer;

/**
 * ExperimentIndexExporter Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>11/24/2006</pre>
 */
public class ExperimentIndexExporterTest extends AbstractIndexExporterTestCase {

    @Test
    public void testBuildIndex() throws Exception {
        Experiment experiment = getMockBuilder().createExperimentRandom(1);
        persistExperiment(experiment);
        Writer writer = new StringWriter();
        IndexExporter exporter = new ExperimentIndexExporter(writer);
        exporter.buildIndex();
        int lineCount = writer.toString().split(System.getProperty("file.separator")).length;
        Assert.assertEquals(23, lineCount);
    }
}
