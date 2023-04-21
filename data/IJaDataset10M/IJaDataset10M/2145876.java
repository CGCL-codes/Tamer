package test.net.sf.japi.xml;

import java.util.NoSuchElementException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.japi.xml.NodeListIterator;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/** Test class for {@link NodeListIterator}.
 * @author <a href="mailto:chris@itcqis.com">Christian Hujer</a>
 */
public class NodeListIterator2Test {

    /** Object Under Test: A NodeListIterator. */
    private NodeListIterator<Node> testling;

    /** Mock NodeList. */
    private NodeList mockNodeList;

    /** Creates the testling.
     * @throws Exception (unexpected).
     */
    @Before
    public void setUp() throws Exception {
        mockNodeList = createMockNodeList();
        testling = new NodeListIterator<Node>(mockNodeList);
    }

    /** Removes the testling.
     * @throws Exception (unexpected).
     */
    @SuppressWarnings({ "AssignmentToNull" })
    @After
    public void tearDown() throws Exception {
        testling = null;
    }

    /** Test case for {@link NodeListIterator#hasNext()}. */
    @Test
    public void testHasNext() {
        for (int i = 0; i < mockNodeList.getLength(); i++) {
            Assert.assertTrue("Iterator must return as many elements as the underlying NodeList has.", testling.hasNext());
            testling.next();
        }
        Assert.assertFalse("Iterator must not return more elements than the underlying NodeList.", testling.hasNext());
    }

    /** Test case for {@link NodeListIterator#next()}. */
    @Test
    public void testNext() {
        for (int i = 0; i < mockNodeList.getLength(); i++) {
            Assert.assertSame("Iterator must return Nodes in original NodeList order.", testling.next(), mockNodeList.item(i));
        }
        try {
            testling.next();
            Assert.fail("Iterator must throw NoSuchElementException if invoking next() more often than available Nodes.");
        } catch (final NoSuchElementException ignore) {
        }
    }

    /** Test case for {@link NodeListIterator#remove()}. */
    @Test
    public void testRemove() {
        try {
            testling.remove();
            Assert.fail("NodeListIterator.remove() is expected to always throw UnsupportedOperationException.");
        } catch (final UnsupportedOperationException ignore) {
        }
    }

    /** Create a mock NodeList.
     * @return a mock NodeList
     * @throws Exception in case the test setup couldn't be created
     */
    private static NodeList createMockNodeList() throws Exception {
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        final DocumentBuilder db = dbf.newDocumentBuilder();
        final Document doc = db.newDocument();
        doc.appendChild(doc.createComment("bla"));
        doc.appendChild(doc.createElement("bla"));
        doc.appendChild(doc.createComment("bla"));
        return doc.getChildNodes();
    }
}
