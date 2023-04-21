package org.fao.xsl;

import static org.junit.Assert.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import jeeves.utils.Xml;
import org.fao.geonet.constants.Edit;
import org.fao.geonet.constants.Geonet;
import org.fao.geonet.kernel.Validator;
import org.fao.geonet.kernel.schema.MetadataSchema;
import org.fao.geonet.kernel.schema.SchemaLoader;
import org.fao.geonet.services.gm03.ISO19139CHEtoGM03;
import org.fao.geonet.services.gm03.TranslateAndValidate;
import org.fao.geonet.services.gm03.ISO19139CHEtoGM03Base.FlattenerException;
import org.fao.geonet.util.ISODate;
import org.hamcrest.core.IsInstanceOf;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.filter.Filter;
import org.xml.sax.SAXException;

/**
 * Methods that support transformation and validation tests
 *
 * @author jeichar
 */
public final class TransformationTestSupport {

    static File transformGM03_2toIso(File src, File outputDir) throws Exception {
        return transformGM03_2toIso(src, outputDir, true);
    }

    static File transformGM03_2toIso(File src, File outputDir, boolean testValidity) throws Exception {
        try {
            TranslateAndValidate transformer = new TranslateAndValidate();
            transformer.outputDir = outputDir;
            transformer.run(new File(TransformationTestSupport.geonetworkRoot, "xsl/conversion/import/GM03_2-to-ISO19139CHE.xsl"), TransformationTestSupport.isoXsd, new String[] { src.getAbsolutePath() });
        } catch (AssertionError e) {
            if (testValidity) throw e;
        }
        return new File(outputDir, "result_" + src.getName());
    }

    static File transformIsoToGM03(File src, File outputDir) throws SAXException, TransformerConfigurationException, FlattenerException, IOException, TransformerException {
        return transformIsoToGM03(src, outputDir, true);
    }

    static File transformIsoToGM03(File src, File outputDir, boolean testValidity) throws SAXException, TransformerConfigurationException, FlattenerException, IOException, TransformerException {
        try {
            ISO19139CHEtoGM03 otherway = new ISO19139CHEtoGM03(TransformationTestSupport.gm03Xsd, TransformationTestSupport.toGm03StyleSheet.getAbsolutePath());
            otherway.convert(src.getAbsolutePath(), "TransformationTestSupport");
        } catch (AssertionError e) {
            if (testValidity) throw e;
        }
        return new File(outputDir, "result_" + src.getName());
    }

    static File copy(File in, File out) throws IOException {
        FileChannel inChannel = new FileInputStream(in).getChannel();
        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            return out;
        } catch (IOException e) {
            throw e;
        } finally {
            if (inChannel != null) inChannel.close();
            if (outChannel != null) outChannel.close();
        }
    }

    public static void delete(File current) {
        if (current.isDirectory()) {
            for (File file : current.listFiles()) {
                delete(file);
            }
        }
        current.delete();
    }

    public static final File data = new File(GM03Iso19139RountripTest.class.getResource("data").getFile()).getAbsoluteFile();

    public static final File outputDir = new File(data.getParentFile(), "output").getAbsoluteFile();

    public static final File geonetworkRoot = new File(data.getParentFile(), "../../../../web/geonetwork");

    public static final File toIsoStyleSheet = new File(geonetworkRoot, "xsl/conversion/import/GM03-to-ISO19139CHE.xsl");

    public static final File toGm03StyleSheet = new File(geonetworkRoot, "xsl/conversion/import/ISO19139CHE-to-GM03.xsl");

    public static final File gm03Xsd = new File(geonetworkRoot, "xml/schemas/iso19139.che/GM03_2.xsd");

    public static final File isoXsd = new File(geonetworkRoot, "xml/schemas/iso19139.che/schema.xsd");

    public static File transformGM03_1ToIso(File src, File outputDir) throws Exception {
        return transformGM03_1ToIso(src, outputDir, true);
    }

    public static File transformGM03_1ToIso(File src, File outputDir, boolean testValidity) throws Exception {
        try {
            TranslateAndValidate transformer = new TranslateAndValidate();
            transformer.outputDir = outputDir;
            transformer.run(toIsoStyleSheet, isoXsd, new String[] { src.getAbsolutePath() });
        } catch (AssertionError e) {
            if (testValidity) {
                throw e;
            }
        }
        return new File(outputDir, "result_" + src.getName());
    }

    public static Element transform(Class root, String pathToXsl, String testData) throws IOException, JDOMException, Exception {
        Element xml = getXML(root, testData);
        String sSheet = new File(pathToXsl).getAbsolutePath();
        Element transform = Xml.transform(xml, sSheet);
        return transform;
    }

    public static Element getXML(Class root, String name) throws IOException, JDOMException {
        if (root == null) root = TransformationTestSupport.class;
        InputStream xmlsource = root.getResourceAsStream(name);
        return Xml.loadStream(xmlsource);
    }

    static Validator VALIDATOR = new Validator(outputDir + "/htmlCache");

    public static void schematronValidation(File metadataFile) throws Exception {
        Element metadata = Xml.loadFile(metadataFile);
        String chePath = "/xml/schemas/iso19139.che/";
        Element env = new Element("env");
        env.addContent(new Element("id").setText("RandomID"));
        env.addContent(new Element("uuid").setText(UUID.randomUUID().toString()));
        env.addContent(new Element("changeDate").setText(new ISODate().toString()));
        env.addContent(new Element("siteURL").setText("http://localhost:8080/geonetwork/srv/eng"));
        Element root = new Element("root");
        root.addContent(metadata);
        root.addContent(env);
        String fixedDataSheet = geonetworkRoot + chePath + Geonet.File.UPDATE_FIXED_INFO;
        root = Xml.transform(root, fixedDataSheet);
        String path = geonetworkRoot + chePath;
        String name = "iso19139.che";
        String xmlSchemaFile = geonetworkRoot + chePath + Geonet.File.SCHEMA;
        String xmlSubstitutionsFile = geonetworkRoot + chePath + Geonet.File.SCHEMA_SUBSTITUTES;
        ;
        MetadataSchema mds = new SchemaLoader().load(xmlSchemaFile, name, xmlSubstitutionsFile);
        mds.setName(name);
        mds.setSchemaDir(path);
        mds.loadSchematronRules();
        Element schemaTronXml = VALIDATOR.getSchemaTronXmlReport(mds, root, Geonet.DEFAULT_LANGUAGE);
        List<Element> schematronReport = schemaTronXml.getChildren("report", Edit.NAMESPACE);
        StringBuilder errors = new StringBuilder();
        for (Element report : schematronReport) {
            Element schematronerrors = report.getChild("schematronerrors", Edit.NAMESPACE);
            if (schematronerrors.getChildren("errorFound", Edit.NAMESPACE).size() != 0) {
                for (Element element : (List<Element>) schematronerrors.getChildren("errorFound", Edit.NAMESPACE)) {
                    String patternName = element.getChild("pattern", Edit.NAMESPACE).getAttributeValue("name");
                    Iterator<Text> diagTxt = element.getChild("diagnostics", Edit.NAMESPACE).getDescendants(new Filter() {

                        public boolean matches(Object arg0) {
                            return (arg0 instanceof Text);
                        }
                    });
                    StringBuilder diagnostics = new StringBuilder();
                    while (diagTxt.hasNext()) {
                        diagnostics.append(diagTxt.next().getTextTrim());
                    }
                    if (!diagnostics.toString().startsWith("INSPIRE")) {
                        errors.append("name=" + patternName + "\n" + diagnostics);
                        errors.append("\n");
                    }
                }
            }
        }
        assertTrue(metadataFile + " is not valid: \n" + errors.toString(), errors.length() == 0);
    }

    public static File file(String fileName) {
        Class<TransformationTestSupport> root = TransformationTestSupport.class;
        return new File(root.getResource(fileName).getFile());
    }
}
