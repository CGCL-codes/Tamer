package com.volantis.mcs.runtime.configuration.xml;

import com.volantis.mcs.runtime.configuration.MarinerConfiguration;
import com.volantis.mcs.runtime.configuration.ArgumentConfiguration;
import com.volantis.mcs.runtime.configuration.IntegrationPluginConfiguration;
import com.volantis.synergetics.testtools.TestCaseAbstract;
import java.util.Iterator;
import java.util.Map;
import org.xml.sax.SAXParseException;

/**
 * Test case for {@link ArgumentRuleSet}.
 */
public class ArgumentRuleSetTestCase extends TestCaseAbstract {

    /**
     * The copyright statement.
     */
    private static String mark = "(c) Volantis Systems Ltd 2002.";

    public ArgumentRuleSetTestCase(String s) {
        super(s);
    }

    /**
     * Create a subset of the mcs-config XML document from the values
     * supplied, parse it into a {@link ArgumentConfiguration} object.
     */
    public void doArgumentTest(ArgumentConfiguration values) throws Exception {
        String doc = "";
        doc += "  <mcs-plugins> \n";
        doc += "    <markup-plugin ";
        doc += "        name=\"myName\" ";
        doc += "        class=\"com.volantis.myClass\" ";
        doc += "        scope=\"application\"> \n";
        if (values != null) {
            doc += "      <initialize> \n";
            doc += "        <argument ";
            if (values.getName() != null) {
                doc += "name=\"" + values.getName() + "\" ";
            }
            if (values.getValue() != null) {
                doc += "value=\"" + values.getValue() + "\"";
            }
            doc += "/> \n";
            doc += "      </initialize> \n";
        }
        doc += "    </markup-plugin> \n";
        doc += "  </mcs-plugins> \n";
        TestXmlConfigurationBuilder configBuilder = new TestXmlConfigurationBuilder(doc);
        MarinerConfiguration config = configBuilder.buildConfiguration();
        assertNotNull(config);
        Iterator plugins = config.getMarkupPluginsListIterator();
        IntegrationPluginConfiguration mpc = (IntegrationPluginConfiguration) plugins.next();
        if (values != null) {
            Iterator args = mpc.getArguments().entrySet().iterator();
            if (args.hasNext()) {
                Map.Entry entry = (Map.Entry) args.next();
                assertEquals("Name should be the same as specified.", values.getName(), entry.getKey());
                assertEquals("Value name should be the same as specified.", values.getValue(), entry.getValue());
            } else {
                fail("Should have been an ArgumentConfiguration created.");
            }
        }
    }

    public void testNull() throws Exception {
        doArgumentTest(null);
    }

    public void testEmpty() throws Exception {
        ArgumentConfiguration values = new ArgumentConfiguration();
        values.setName("");
        values.setValue("");
        doArgumentTest(values);
    }

    public void testFull() throws Exception {
        ArgumentConfiguration values = new ArgumentConfiguration();
        values.setName("myArg");
        values.setValue("argValue");
        doArgumentTest(values);
    }
}
