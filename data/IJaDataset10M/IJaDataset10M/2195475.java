package net.sf.joafip.btree.impl.memory.entity;

import java.util.Comparator;
import net.sf.joafip.btree.BTreeException;
import net.sf.joafip.btree.entity.IBTreeNode;
import net.sf.joafip.btree.entity.IBTreePage;
import net.sf.joafip.btree.service.IBTreePageAndNodeManager;
import junit.framework.TestCase;

public class TestBTreePage extends TestCase implements IBTreePageAndNodeManager<String> {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private static final String MUST_FOUND_EQUALS = "must found equals";

    private static final String MUST_FOUND_GREATER = "must found greater";

    private static final String BAD_NUMBER_OF_ELEMENT = "bad number of element";

    private static final String PAGE_MUST_NOT_BE_TOO_LARGE = "page must not be too large";

    private transient BTreePage<String> page;

    public TestBTreePage() {
        super();
    }

    public TestBTreePage(final String arg0) {
        super(arg0);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        page = new BTreePage<String>(this, 4);
    }

    public void testAdd() throws BTreeException {
        assertFalse(PAGE_MUST_NOT_BE_TOO_LARGE, page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 0, page.getNumberOfElement());
        IBTreeNode<String> btreeNode;
        btreeNode = createLeafNode("e1", null);
        page.addNode(btreeNode);
        assertFalse(PAGE_MUST_NOT_BE_TOO_LARGE, page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 1, page.getNumberOfElement());
        btreeNode = createLeafNode("e2", null);
        page.addNode(btreeNode);
        assertFalse(PAGE_MUST_NOT_BE_TOO_LARGE, page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 2, page.getNumberOfElement());
        btreeNode = createLeafNode("e3", null);
        page.addNode(btreeNode);
        assertFalse(PAGE_MUST_NOT_BE_TOO_LARGE, page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 3, page.getNumberOfElement());
        btreeNode = createLeafNode("e4", null);
        page.addNode(btreeNode);
        assertFalse(PAGE_MUST_NOT_BE_TOO_LARGE, page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 4, page.getNumberOfElement());
        btreeNode = createLeafNode("e5", null);
        page.addNode(btreeNode);
        assertTrue("page must be too large", page.isTooLarge());
        assertEquals(BAD_NUMBER_OF_ELEMENT, 5, page.getNumberOfElement());
    }

    public void testNodeWithGreaterElement() throws BTreeException, BTreeException {
        IBTreeNode<String> btreeNode;
        btreeNode = createLeafNode("bbb", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ddd", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("fff", null);
        page.addNode(btreeNode);
        btreeNode = page.searchGreater("aaa");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'bbb'", "bbb", btreeNode.getElement());
        btreeNode = page.searchGreater("bbb");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        btreeNode = page.searchGreater("ccc");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        btreeNode = page.searchGreater("ddd");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreater("eee");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreater("fff");
        assertNull("must not found greater", btreeNode);
        btreeNode = createLeafNode(null, null);
        page.addNode(btreeNode);
        btreeNode = page.searchGreater("fff");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertNull("must be null element", btreeNode.getElement());
    }

    public void testSearchGreaterOrEquals() throws BTreeException {
        IBTreeNode<String> btreeNode;
        btreeNode = createLeafNode("bbb", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ddd", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("fff", null);
        page.addNode(btreeNode);
        btreeNode = page.searchGreaterOrEquals("aaa");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'bbb'", "bbb", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("bbb");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'bbb'", "bbb", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("ccc");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("ddd");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("eee");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("fff");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("ggg");
        assertNull("must not found", btreeNode);
        btreeNode = createLeafNode(null, null);
        page.addNode(btreeNode);
        btreeNode = page.searchGreaterOrEquals("ggg");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertNull("must be null element", btreeNode.getElement());
    }

    public void testGetLastNode() throws BTreeException {
        IBTreeNode<String> btreeNode;
        btreeNode = page.getLastNode();
        assertNull("must not have last node", btreeNode);
        btreeNode = createLeafNode("bbb", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ddd", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("fff", null);
        page.addNode(btreeNode);
        btreeNode = page.getLastNode();
        assertNotNull("must have last node", btreeNode);
        assertEquals("must be fff", "fff", btreeNode.getElement());
        btreeNode = createLeafNode(null, null);
        page.addNode(btreeNode);
        btreeNode = page.getLastNode();
        assertNotNull("must have last node", btreeNode);
        assertNull("must be null element", btreeNode.getElement());
    }

    public void testRemoveExisting() throws BTreeException, BTreeException {
        IBTreeNode<String> btreeNode;
        btreeNode = createLeafNode("bbb", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ddd", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("fff", null);
        page.addNode(btreeNode);
        btreeNode = page.searchGreaterOrEquals("ddd");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        page.removeExisting(btreeNode);
        btreeNode = page.searchGreaterOrEquals("bbb");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'bbb'", "bbb", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("ddd");
        assertNotNull(MUST_FOUND_GREATER, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("fff");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
    }

    public void testSplit() throws BTreeException, BTreeException {
        IBTreeNode<String> btreeNode;
        btreeNode = createLeafNode("bbb", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ddd", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("fff", null);
        page.addNode(btreeNode);
        btreeNode = createLeafNode("ggg", null);
        page.addNode(btreeNode);
        final IBTreePage<String> leftPage = page.split();
        assertEquals("1 element for splitted page", 1, page.getNumberOfElement());
        assertEquals("3 element for left page", 3, leftPage.getNumberOfElement());
        btreeNode = leftPage.searchGreaterOrEquals("bbb");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'bbb'", "bbb", btreeNode.getElement());
        btreeNode = leftPage.searchGreaterOrEquals("ddd");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'ddd'", "ddd", btreeNode.getElement());
        btreeNode = leftPage.searchGreaterOrEquals("fff");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'fff'", "fff", btreeNode.getElement());
        btreeNode = page.searchGreaterOrEquals("ggg");
        assertNotNull(MUST_FOUND_EQUALS, btreeNode);
        assertEquals("must be 'ggg'", "ggg", btreeNode.getElement());
    }

    public IBTreeNode<String> createLeafNode(final String element, final Comparator<String> comparator) {
        return new BTreeNode<String>(element, comparator);
    }

    public IBTreeNode<String> createNode(final IBTreePage<String> pageOfPreviousElement, final String element, final Comparator<String> comparator) {
        return new BTreeNode<String>(pageOfPreviousElement, element, comparator);
    }

    public IBTreePage<String> createNewPage() {
        return new BTreePage<String>(this, 4);
    }

    public void removePage(final IBTreePage<String> page) {
        throw new UnsupportedOperationException("not used in test");
    }

    public IBTreePage<String> getRootPage() {
        throw new UnsupportedOperationException("not used in test");
    }

    public void setRootPage(final IBTreePage<String> page) {
        throw new UnsupportedOperationException("not used in test");
    }
}
