package test.unit.be.fedict.eid.applet.maven;

import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import be.fedict.eid.applet.maven.DocbookMojo;

public class DocbookMojoTest {

    private static final Log LOG = LogFactory.getLog(DocbookMojoTest.class);

    @Test
    public void testCreateGraph() throws Exception {
        File tmpFile = File.createTempFile("graph-", ".png");
        DocbookMojo.generateGraph(tmpFile);
        LOG.debug("graph file: " + tmpFile.getAbsolutePath());
    }
}
