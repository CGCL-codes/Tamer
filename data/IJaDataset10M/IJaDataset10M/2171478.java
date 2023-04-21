package org.dita2indesign.indesign.builders;

import java.io.File;
import java.net.URL;
import java.util.List;
import org.dita2indesign.indesign.inx.model.InDesignDocument;
import org.dita2indesign.indesign.inx.model.Link;
import org.dita2indesign.indesign.inx.writers.InxWriter;
import org.dita2indesign.util.InxUtilTestCase;

/**
 * Tests the Issue builders
 */
public class InDesignFromDitaMapBuilderTests extends InxUtilTestCase {

    public void testIssueFromDitaMapBuilder() throws Exception {
        File inxFile = File.createTempFile("testIssueFromDitaMapBuilder", ".inx");
        InDesignFromDitaMapBuilder builder = new InDesignFromDitaMapBuilder();
        URL styleCatalogUrl = this.getClass().getClassLoader().getResource("resources/indesign/stylemaps/t+d-dita-indesign-style-catalog.xml");
        assertNotNull(styleCatalogUrl);
        URL xsltUrl = this.getClass().getClassLoader().getResource("tandd2indesign/tandd2indesign.xsl");
        assertNotNull(xsltUrl);
        Map2InDesignOptions options = new Map2InDesignOptions();
        options.setStyleCatalogUrl(styleCatalogUrl);
        options.setXsltUrl(xsltUrl);
        options.setDebug(true);
        options.setResultFile(inxFile);
        options.setInDesignTemplate(idTemplate01);
        InDesignDocument doc = builder.buildMapDocument(map01, options);
        InxWriter writer = new InxWriter(inxFile);
        writer.write(doc);
        System.err.println("inxFile=" + inxFile.getAbsolutePath());
        List<Link> links = doc.getLinks();
        assertNotNull(links);
        assertEquals("Expected 2 links in result doc", 2, links.size());
        for (int i = 0; i < links.size(); i++) {
            Link link = links.get(i);
            File linkedFile = new File(inxFile.getParentFile(), link.getWindowsFileName());
            assertTrue("File does not exist", linkedFile.exists());
        }
    }
}
