package org.opennms.netmgt.config.datacollection;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;
import org.junit.runners.Parameterized.Parameters;
import org.opennms.core.test.xml.XmlTest;
import org.opennms.netmgt.config.datacollection.Parameter;
import org.opennms.netmgt.config.datacollection.StorageStrategy;

public class StorageStrategyTest extends XmlTest<StorageStrategy> {

    public StorageStrategyTest(final StorageStrategy sampleObject, final String sampleXml, final String schemaFile) {
        super(sampleObject, sampleXml, schemaFile);
    }

    @Parameters
    public static Collection<Object[]> data() throws ParseException {
        final StorageStrategy strategy = new StorageStrategy();
        strategy.setClazz("org.opennms.netmgt.dao.support.IndexStorageStrategy");
        strategy.addParameter(new Parameter("foo", "bar"));
        return Arrays.asList(new Object[][] { { strategy, "<storageStrategy class=\"org.opennms.netmgt.dao.support.IndexStorageStrategy\"><parameter key=\"foo\" value=\"bar\" /></storageStrategy>", "target/classes/xsds/datacollection-config.xsd" } });
    }
}
