package net.sf.joafip.java.util;

import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;

/**
 * test sub list of {@link PTreeList}
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestSubPTreeList extends AbstractListTest {

    public TestSubPTreeList() throws TestException {
        super();
    }

    public TestSubPTreeList(final String name) throws TestException {
        super(name);
    }

    @Override
    protected void setUp() throws Exception {
        final PTreeList<String> ptreeList = new PTreeList<String>();
        ptreeList.add("0");
        ptreeList.add("1");
        ptreeList.add("{");
        ptreeList.add("|");
        list = ptreeList.subList(2, 2);
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @Override
    protected boolean concurrentAccess() {
        return true;
    }

    @Override
    protected boolean acceptNullElement() {
        return true;
    }
}
