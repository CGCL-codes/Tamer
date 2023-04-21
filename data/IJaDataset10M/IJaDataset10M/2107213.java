package org.eclipse.emf.test.xml.rss;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EPackage.Registry;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.util.XMLProcessor;
import org.eclipse.emf.test.common.TestUtil;
import org.eclipse.emf.test.xml.AllSuites;
import org.eclipse.emf.test.xml.xmi.CompareXML;
import org.eclipse.xsd.XSDDiagnostic;
import org.eclipse.xsd.XSDDiagnosticSeverity;
import org.eclipse.xsd.ecore.XSDEcoreBuilder;
import org.eclipse.xsd.impl.XSDDiagnosticImpl;
import org.eclipse.xsd.util.XSDResourceFactoryImpl;

/**
 * General tests for RSS reading/writing
 */
public class RSSTests extends TestCase {

    XSDEcoreBuilder xsdEcoreBuilder;

    URI schemaURI;

    DocumentBuilder builder;

    static final String BASE_RSS_URI = "file:///" + TestUtil.getPluginDirectory(AllSuites.PLUGIN_ID) + "/data/rss/";

    public RSSTests(String name) {
        super(name);
    }

    public static Test suite() {
        TestSuite ts = new TestSuite("RSSTests");
        ts.addTest(new RSSTests("testLoadEMF21_AtomRSS10v11"));
        ts.addTest(new RSSTests("testLoadEMF22_XMLProcessor_AtomRSS10v11"));
        return ts;
    }

    /**
   * @see junit.framework.TestCase#setUp()
   */
    @Override
    protected void setUp() throws Exception {
        xsdEcoreBuilder = new XSDEcoreBuilder();
        xsdEcoreBuilder.setValidate(true);
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("xsd", new XSDResourceFactoryImpl());
        Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put("*", new XMLResourceFactoryImpl());
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        builder = factory.newDocumentBuilder();
    }

    public void testLoadEMF21_AtomRSS10v11() throws Exception {
        String schema = "atom10v11_simple.xsd";
        validateSchema(schema);
        loadAndSaveEMF21(schema, "atom10v11_sample.xml");
    }

    public void testLoadEMF22_XMLProcessor_AtomRSS10v11() throws Exception {
        String schema = "atom10v11_simple.xsd";
        schemaURI = URI.createURI(BASE_RSS_URI + schema);
        loadAndSaveEMF22_XMLProcessor(schema, "atom10v11_sample.xml");
    }

    protected void validateSchema(String schema) throws Exception {
        schemaURI = URI.createURI(BASE_RSS_URI + schema);
        assertTrue("Schema file is not available - " + BASE_RSS_URI + schema, schemaURI.isFile());
        xsdEcoreBuilder.generate(schemaURI);
        List<XSDDiagnostic> results = xsdEcoreBuilder.getDiagnostics();
        for (Iterator<XSDDiagnostic> iter = results.iterator(); iter.hasNext(); ) {
            assertTrue(getXSDDiagnosticMessage((XSDDiagnosticImpl) iter.next()).toString(), false);
        }
    }

    protected void loadAndSaveEMF21(String schema, String rss) throws Exception {
        HashMap<String, Object> options = new HashMap<String, Object>();
        ResourceSet rs = new ResourceSetImpl();
        Registry packageRegistry = rs.getPackageRegistry();
        @SuppressWarnings("unchecked") Collection<EPackage> packageList = (Collection) xsdEcoreBuilder.generate(schemaURI);
        for (EPackage epackage : packageList) {
            String nsURI = epackage.getNsURI();
            packageRegistry.put(nsURI, epackage);
        }
        ExtendedMetaData extendedMetaData = new BasicExtendedMetaData(packageRegistry);
        options.put(XMLResource.OPTION_EXTENDED_META_DATA, extendedMetaData);
        options.put(XMLResource.OPTION_USE_ENCODED_ATTRIBUTE_STYLE, Boolean.TRUE);
        Resource resource = rs.createResource(URI.createURI(BASE_RSS_URI + rss));
        resource.load(options);
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream(2064);
        resource.save(outputstream, options);
        CompareXML.compareFiles(builder, BASE_RSS_URI + rss, new ByteArrayInputStream(outputstream.toByteArray()));
    }

    protected void loadAndSaveEMF22_XMLProcessor(String schema, String rss) throws Exception {
        XMLProcessor processor = new XMLProcessor(schemaURI);
        Resource resource = processor.load(BASE_RSS_URI + rss, null);
        ByteArrayOutputStream outputstream = new ByteArrayOutputStream(2064);
        processor.save(outputstream, resource, null);
        CompareXML.compareFiles(builder, BASE_RSS_URI + rss, new ByteArrayInputStream(outputstream.toByteArray()));
    }

    private static StringBuffer getXSDDiagnosticMessage(XSDDiagnosticImpl diagnostic) {
        StringBuffer message = new StringBuffer();
        XSDDiagnosticSeverity sev = diagnostic.getSeverity();
        switch(sev.getValue()) {
            case XSDDiagnosticSeverity.WARNING:
                message.append("Warning: ");
                break;
            case XSDDiagnosticSeverity.ERROR:
                message.append("Error: ");
                break;
            case XSDDiagnosticSeverity.FATAL:
                message.append("Fatal Error: ");
                break;
        }
        message.append("Line ");
        message.append(diagnostic.getLine());
        message.append(" - ");
        message.append(diagnostic.getMessage());
        return message;
    }
}
