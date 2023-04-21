package uk.org.ogsadai.client.toolkit.property.convertor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import junit.framework.TestCase;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.util.xml.XML;

/**
 * Converter test.
 *
 * @author The OGSA-DAI Project Team.
 */
public class ActivityProgressXMLConvertorTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2009.";

    public ActivityProgressXMLConvertorTest(String name) {
        super(name);
    }

    /**
     * Tests a normal property.
     */
    public void testCorrectProperty() {
        String xml = "<ns2:ActivityStatus xmlns:ns2=\"http://ogsadai.org.uk/namespaces/2007/04/types\">" + "<ns2:activity instanceName=\"activity1\" status=\"PROCESSING\">" + "        <ns2:input blocksConsumed=\"5\" name=\"projectColumnIds2\"/> " + "       <ns2:input blocksConsumed=\"2\" name=\"columnIds2\"/> " + " <ns2:input blocksConsumed=\"2\" name=\"columnIds1\"/> " + "<ns2:input blocksConsumed=\"201\" name=\"data2\"/> " + "  <ns2:input blocksConsumed=\"15\" name=\"projectColumnIds1\"/> " + "   <ns2:input blocksConsumed=\"201\" name=\"data1\"/> " + " <ns2:output blocksProduced=\"200\" name=\"result\"/> " + " </ns2:activity></ns2:ActivityStatus>";
        Map<ActivityInstanceName, ActivityProgress> results = ActivityProgressXMLConvertor.convertToMap(XML.toElement(xml));
        ActivityProgress progress = results.get(new ActivityInstanceName("activity1"));
        assertNotNull(progress);
        long actual = progress.getBlocksConsumed("projectColumnIds1").get(0);
        assertEquals(15, actual);
        actual = progress.getBlocksConsumed("projectColumnIds2").get(0);
        assertEquals(5, actual);
        actual = progress.getBlocksConsumed("data1").get(0);
        assertEquals(201, actual);
        actual = progress.getBlocksConsumed("data2").get(0);
        assertEquals(201, actual);
        assertEquals(Arrays.asList(200L), progress.getBlocksProduced("result"));
    }

    /**
     * Tests multiple inputs and outputs.
     */
    public void testMultipleInputsAndOutputs() {
        String xml = "<ns2:ActivityStatus xmlns:ns2=\"http://ogsadai.org.uk/namespaces/2007/04/types\">" + "<ns2:activity instanceName=\"activity1\" status=\"PROCESSING\">" + "  <ns2:input blocksConsumed=\"5\" name=\"data\"/> " + "  <ns2:input blocksConsumed=\"2\" name=\"data\"/> " + "  <ns2:input blocksConsumed=\"1\" name=\"data\"/> " + "  <ns2:output blocksProduced=\"200\" name=\"result\"/> " + "  <ns2:output blocksProduced=\"30\" name=\"result\"/> " + "  <ns2:output blocksProduced=\"600\" name=\"result\"/> " + " </ns2:activity></ns2:ActivityStatus>";
        Map<ActivityInstanceName, ActivityProgress> results = ActivityProgressXMLConvertor.convertToMap(XML.toElement(xml));
        ActivityProgress progress = results.get(new ActivityInstanceName("activity1"));
        List<Long> blocks = progress.getBlocksConsumed("data");
        assertNotNull(blocks);
        assertEquals(Arrays.asList(5L, 2L, 1L), blocks);
        assertEquals(Arrays.asList(200L, 30L, 600L), progress.getBlocksProduced("result"));
    }
}
