package org.jdeluxe.test;

import static org.junit.Assert.assertNotNull;
import java.io.IOException;
import org.jdeluxe.config.InitialConfigBuilder;
import org.jdom.Document;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.junit.Test;

/**
 * The Class TestConfigFileBuilder.
 */
public class TestConfigFileBuilder {

    /**
	 * Test build config.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
    public void testBuildConfig() throws IOException {
        System.out.println("------------ testBuildConfig ----------");
        Document doc = InitialConfigBuilder.buildEmptyJdeluxeConfig();
        XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
        serializer.output(doc, System.out);
        assertNotNull(doc);
        System.out.println("------------ testBuildConfig ----------");
    }

    /**
	 * Test build empty config.
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
    @Test
    public void testBuildEmptyConfig() throws IOException {
        System.out.println("------------ testBuildEmptyConfig ----------");
        Document doc = InitialConfigBuilder.buildEmptyJdeluxeConfig();
        XMLOutputter serializer = new XMLOutputter(Format.getPrettyFormat());
        serializer.output(doc, System.out);
        assertNotNull(doc);
        System.out.println("------------ testBuildEmptyConfig ----------");
    }
}
