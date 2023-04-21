package net.sf.joafip.redblacktree.service;

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
public class TestByPositionUnique extends AbstractTestByPosition {

    public TestByPositionUnique() throws TestException {
        super();
    }

    public TestByPositionUnique(final String name) throws TestException {
        super(name);
    }

    @Override
    protected boolean uniqueValue() {
        return true;
    }
}
