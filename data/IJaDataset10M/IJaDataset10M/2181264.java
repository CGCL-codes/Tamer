package net.sf.joafip.java.util;

import java.util.List;
import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;
import net.sf.joafip.java.util.support.CollectionElementForTest;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestEqualsHashComparatorForLinkedList extends AbstractTestEqualsHashComparatorForList {

    public TestEqualsHashComparatorForLinkedList() throws TestException {
        super();
    }

    public TestEqualsHashComparatorForLinkedList(final String name) throws TestException {
        super(name);
    }

    @Override
    protected List<CollectionElementForTest> newList() {
        return new PLinkedList<CollectionElementForTest>(COMPARATOR);
    }
}
