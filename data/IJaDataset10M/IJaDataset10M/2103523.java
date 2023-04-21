package org.jmage.mapper;

import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.jmage.ApplicationContext;
import org.jmage.ConfigurationException;
import org.jmage.ImageRequest;
import org.jmage.JmageException;
import org.jmage.dispatcher.RequestDispatcher;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Maps Requests for JUNIT
 */
public class JunitMapper extends TestCase {

    protected ApplicationContext context;

    protected RequestDispatcher dispatcher;

    protected ImageRequest request;

    protected File outputDir;

    protected static Logger log = Logger.getLogger(JunitMapper.class.getName());

    /**
     * Set up a TestCase
     *
     * @throws URISyntaxException
     */
    protected void setUp() throws URISyntaxException, ConfigurationException {
        context = ApplicationContext.getContext();
        dispatcher = context.obtainRequestDispatcher();
        request = new ImageRequest();
        String dir = System.getProperty("JMAGE_OUTPUT_DIR", "images");
        outputDir = new File(dir);
        if (!outputDir.isDirectory()) {
            outputDir.mkdirs();
        }
        log.info("initialized JunitMapper with resourcedir: " + context.getProperty("resourcedir"));
        log.info("initialized JunitMapper with JMAGE_OUTPUT_DIR: " + outputDir.getAbsolutePath());
        this.fillRequest();
    }

    /**
     * Fill ImageRequest with params.
     *
     * @throws URISyntaxException
     */
    public void fillRequest() throws URISyntaxException {
        this.setImageURI();
        this.setFilterChainURI();
        this.setProperties();
        this.setEncodingFormat();
    }

    /**
     * Sets the imageURI
     *
     * @throws URISyntaxException
     */
    public void setImageURI() throws URISyntaxException {
        request.setImageURI(new URI("file:///sample/june_vase.jpg"));
    }

    /**
     * Sets the filterChainURI
     *
     * @throws URISyntaxException
     */
    public void setFilterChainURI() throws URISyntaxException {
        request.setFilterChainURI(new URI[] { new URI("chain:org.jmage.filter.NoOpFilter") });
    }

    /**
     * Sets the properties
     */
    public void setProperties() {
        Properties props = request.getFilterChainProperties();
        assert (props != null) : "filterProperties should not be null";
        request.setFilterChainProperties(props);
    }

    public void setEncodingFormat() {
        request.setEncodingFormat("jpg");
    }

    /**
     * Run the test by giving the ImageRequest to the ApplicationContext's dispatcher.
     *
     * @throws JmageException
     */
    public void testFilter() throws JmageException, IOException {
        long before = System.currentTimeMillis();
        dispatcher.dispatch(request);
        log.debug("processed image in seconds: " + (System.currentTimeMillis() - before) / 1000f);
        this.writeResults(request);
    }

    /**
     * Write the results to disk.
     *
     * @param request
     * @throws IOException
     */
    public void writeResults(ImageRequest request) throws IOException {
        String fileName = null;
        fileName = this.getClass().getName() + "@" + String.valueOf(request.hashCode());
        fileName += "." + request.getEncodingFormat();
        File file = new File(outputDir, fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(request.getEncoded());
        fos.flush();
        fos.close();
        assertTrue("did not write results to disk", file.isFile() && file.exists());
        log.info("wrote imagerequest: " + request + " test results to file: " + file.getAbsolutePath());
    }

    /**
     * Tear down the test.
     *
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        super.tearDown();
        context.releaseRequestDispatcher(dispatcher);
        outputDir = null;
    }

    public boolean dispatchFails() {
        boolean failed = false;
        try {
            dispatcher.dispatch(request);
            this.writeResults(request);
        } catch (Throwable t) {
            failed = true;
        }
        return failed;
    }
}
