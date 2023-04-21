package jp.go.aist.six.test.oval.core.xml;

import jp.go.aist.six.oval.model.definitions.OvalDefinitions;
import jp.go.aist.six.oval.model.results.OvalResults;
import jp.go.aist.six.test.oval.core.CoreTestBase;
import org.testng.Reporter;

/**
 * @author  Akihito Nakamura, AIST
 * @version $Id: OvalXmlTest.java 2017 2011-09-21 01:58:34Z nakamura5akihito $
 */
public class OvalXmlTest extends CoreTestBase {

    /**
     */
    public OvalXmlTest() {
    }

    /**
     */
    protected <T> void _testXml(final Class<T> type, final String sourceFilepath, final T expected, final String resultFilepath) throws Exception {
        T actual = _unmarshalWithValidation(type, sourceFilepath, expected);
        _marshal(actual, resultFilepath);
        _unmarshalWithValidation(type, resultFilepath, expected);
    }

    @org.testng.annotations.Test(groups = { "oval.core.xml", "oval.definitions.oval_definitions" }, dataProvider = "oval.definitions.xml", alwaysRun = true)
    public void testDefinitionsOvalDefinitions(final Class<OvalDefinitions> type, final String sourceFilepath, final OvalDefinitions expected, final String resultFilepath) throws Exception {
        Reporter.log("\n////////////////////////////////////////////", true);
        _testXml(type, sourceFilepath, expected, resultFilepath);
    }

    @org.testng.annotations.Test(groups = { "oval.core.xml", "oval.results.oval_results" }, dataProvider = "oval.results.xml", alwaysRun = true)
    public void testResutlsOvalResults(final Class<OvalResults> type, final String sourceFilepath, final OvalResults expected, final String resultFilepath) throws Exception {
        Reporter.log("\n////////////////////////////////////////////", true);
        _testXml(type, sourceFilepath, expected, resultFilepath);
    }
}
