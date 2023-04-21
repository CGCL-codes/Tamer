package uk.org.ogsadai.client.toolkit.property.convertor;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import uk.org.ogsadai.activity.ActivityInstanceName;
import uk.org.ogsadai.activity.ActivityStatus;
import uk.org.ogsadai.activity.request.status.ActivityProcessingStatus;
import uk.org.ogsadai.data.BinaryData;
import uk.org.ogsadai.data.CharData;
import uk.org.ogsadai.data.ListBegin;
import uk.org.ogsadai.data.ListEnd;
import uk.org.ogsadai.data.LongData;
import uk.org.ogsadai.data.StringData;
import uk.org.ogsadai.exception.DAIException;
import uk.org.ogsadai.exception.ErrorID;
import uk.org.ogsadai.resource.request.RequestStatus;
import uk.org.ogsadai.test.TestHelper;
import uk.org.ogsadai.util.xml.XML;

/**
 * Tests the XML to RequestStatus converter.
 *
 * @author The OGSA-DAI Project Team.
 */
public class RequestStatusXMLConvertorTest extends TestCase {

    /** Copyright statement */
    private static final String COPYRIGHT_NOTICE = "Copyright (c) The University of Edinburgh, 2007-2009.";

    /**
     * Constructor.
     * @param arg0
     */
    public RequestStatusXMLConvertorTest(String arg0) {
        super(arg0);
    }

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests parsing a correct document.
     * 
     * @throws Exception
     *             if an unexpected error occurs.
     */
    public void testParse() throws Exception {
        File xmlFile = TestHelper.getFileFromClassName(getClass(), "RequestStatus.xml");
        Document document = XML.fileToDocument(xmlFile.toString());
        RequestStatus status = RequestStatusXMLConvertor.convert(document.getDocumentElement());
        ActivityInstanceName activity1 = new ActivityInstanceName("activity1");
        ActivityInstanceName activity2 = new ActivityInstanceName("activity2");
        Iterator iterator = status.getDataValueIterator(activity1, "result1");
        assertTrue(iterator.hasNext());
        BinaryData binary = (BinaryData) iterator.next();
        byte[] bytes = binary.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(i + 1, bytes[i]);
        }
        assertTrue(iterator.hasNext());
        StringData stringdata = (StringData) iterator.next();
        assertEquals("ABCabc", stringdata.toString());
        assertTrue(iterator.hasNext());
        CharData chardata = (CharData) iterator.next();
        assertEquals("xyzXYZ", chardata.toString());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof ListBegin);
        assertTrue(iterator.hasNext());
        LongData longdata = (LongData) iterator.next();
        assertEquals(2459056456L, longdata.getLong());
        assertTrue(iterator.hasNext());
        assertTrue(iterator.next() instanceof ListEnd);
        assertTrue(!iterator.hasNext());
        iterator = status.getDataValueIterator(activity1, "result2");
        assertTrue(iterator.hasNext());
        binary = (BinaryData) iterator.next();
        bytes = binary.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(i + 1, bytes[i]);
        }
        assertTrue(!iterator.hasNext());
        iterator = status.getDataValueIterator(activity2, "result1");
        assertTrue(iterator.hasNext());
        binary = (BinaryData) iterator.next();
        bytes = binary.getBytes();
        for (int i = 0; i < bytes.length; i++) {
            assertEquals(i + 1, bytes[i]);
        }
        assertTrue(!iterator.hasNext());
        iterator = status.getActivities();
        assertTrue(iterator.hasNext());
        Map.Entry activity = (Map.Entry) iterator.next();
        assertEquals(activity1, activity.getKey());
        ActivityProcessingStatus status1 = (ActivityProcessingStatus) activity.getValue();
        assertEquals("Unexpected activity status,", ActivityStatus.ERROR, status1.getStatus());
        DAIException exception = status1.getError();
        assertEquals("Wrong error ID,", ErrorID.ACTIVITY_IO_EXCEPTION.toString(), exception.getErrorID().toString());
        assertEquals("Wrong number of parameters,", 0, exception.getParameters().length);
    }
}
