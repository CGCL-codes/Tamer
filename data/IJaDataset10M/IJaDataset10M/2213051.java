package net.sf.joafip.java.util;

import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;

/**
 * test {@link PTreeList}
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public abstract class AbstractTestPTreeList extends AbstractLinkedListTest {

    public AbstractTestPTreeList() throws TestException {
        super();
    }

    public AbstractTestPTreeList(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        linkedList = new PTreeList<String>(concurrentAccess());
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testClone() {
        linkedList.add("a");
        linkedList.add("b");
        linkedList.add("c");
        final PTreeList<String> clone = ((PTreeList<String>) linkedList).clone();
        assertNotSame(linkedList, clone);
        final boolean equals = linkedList.equals(clone);
        assertTrue("list must have same content", equals);
    }

    @Override
    protected boolean acceptNullElement() {
        return true;
    }
}
