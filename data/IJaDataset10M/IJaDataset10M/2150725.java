package jp.go.aist.six.test.oval.core.xml;

import java.io.File;
import java.io.FilenameFilter;
import jp.go.aist.six.oval.model.Family;
import jp.go.aist.six.oval.model.definitions.DefinitionType;
import jp.go.aist.six.oval.model.definitions.OvalDefinitions;
import jp.go.aist.six.oval.model.sc.OvalSystemCharacteristics;
import jp.go.aist.six.test.oval.core.TestBase;
import jp.go.aist.six.test.oval.core.XmlFilenameFilter;
import org.testng.Reporter;

/**
 * Tests: OvalXmlMapper.
 *
 * @author  Akihito Nakamura, AIST
 * @version $Id: OvalXmlMapperTests.java 2297 2012-04-06 02:38:20Z nakamura5akihito@gmail.com $
 */
public class OvalXmlMapperTests extends TestBase {

    /**
     */
    public OvalXmlMapperTests() {
    }

    /**
     */
    protected <T> void _testXml(final Class<T> object_type, final String input_filepath, final T expected_object, final String output_filepath) throws Exception {
        T actual = null;
        try {
            actual = _unmarshalObject(object_type, input_filepath, expected_object);
            if (actual instanceof OvalDefinitions) {
                OvalDefinitions oval_defs = OvalDefinitions.class.cast(actual);
                for (DefinitionType def : oval_defs.getDefinitions().getDefinition()) {
                    Reporter.log("  @ definition: id=" + def.getOvalID(), true);
                    Reporter.log("                title=" + def.getMetadata().getTitle(), true);
                }
            }
        } catch (Exception ex) {
            Reporter.log("  !!! Unsupported XML element(s) found.", true);
            throw ex;
        }
        if (actual != null) {
            _marshalObject(actual, output_filepath);
            _unmarshalObject(object_type, output_filepath, expected_object);
        }
    }

    /**
     */
    @org.testng.annotations.Test(groups = { "oval.core.xml" }, dataProvider = "oval.test_content.def", alwaysRun = true)
    public void testOvalDefinitions(final Class<OvalDefinitions> object_type, final String oval_schema_version, final Family family, final String dirpath, final String xml_filepath, final OvalDefinitions expected_object) throws Exception {
        Reporter.log("\n//////////////////////////////////////////////////////////", true);
        Reporter.log("* object type: " + object_type, true);
        Reporter.log("* OVAL schema version: " + oval_schema_version, true);
        Reporter.log("* family: " + family, true);
        Reporter.log("* dir: " + dirpath, true);
        Reporter.log("* XML file: " + xml_filepath, true);
        File dir = new File(dirpath);
        if (xml_filepath == null) {
            FilenameFilter filter = new XmlFilenameFilter();
            File[] files = dir.listFiles(filter);
            for (File file : files) {
                Reporter.log("  * file= " + file, true);
                _testXml(object_type, file.getCanonicalPath(), expected_object, "unmarshalled_" + file.getName());
            }
        } else {
            File file = new File(dir, xml_filepath);
            Reporter.log("  * file= " + file, true);
            _testXml(object_type, file.getCanonicalPath(), expected_object, "unmarshalled_" + file.getName());
        }
    }

    /**
     */
    @org.testng.annotations.Test(groups = { "oval.core.xml" }, dataProvider = "oval.test_content.sc", alwaysRun = true)
    public void testOvalSc(final Class<OvalSystemCharacteristics> object_type, final String oval_schema_version, final Family family, final String dirpath, final String xml_filepath, final OvalSystemCharacteristics expected_object) throws Exception {
        Reporter.log("\n//////////////////////////////////////////////////////////", true);
        Reporter.log("* object type: " + object_type, true);
        Reporter.log("* OVAL schema version: " + oval_schema_version, true);
        Reporter.log("* family: " + family, true);
        Reporter.log("* dir: " + dirpath, true);
        Reporter.log("* XML file: " + xml_filepath, true);
        File dir = new File(dirpath);
        if (xml_filepath == null) {
            FilenameFilter filter = new XmlFilenameFilter();
            File[] files = dir.listFiles(filter);
            for (File file : files) {
                Reporter.log("  * file= " + file, true);
                _testXml(object_type, file.getCanonicalPath(), expected_object, "unmarshalled_" + file.getName());
            }
        } else {
            File file = new File(dir, xml_filepath);
            Reporter.log("  * file= " + file, true);
            _testXml(object_type, file.getCanonicalPath(), expected_object, "unmarshalled_" + file.getName());
        }
    }
}
