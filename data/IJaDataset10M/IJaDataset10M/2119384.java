package net.sf.joafip.java.util;

import net.sf.joafip.DoNotTransform;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.TestException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@DoNotTransform
public class TestIterPTreeSetConcurrent extends AbstractTestIterPTreeSet {

    public TestIterPTreeSetConcurrent() throws TestException {
        super();
    }

    public TestIterPTreeSetConcurrent(final String name) throws TestException {
        super(name);
    }

    @Override
    protected boolean concurrentAccess() {
        return true;
    }
}
