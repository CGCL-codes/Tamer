package net.sf.joafip.service.rel300;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.TestException;
import net.sf.joafip.store.service.objectfortest.SubstitutionBob;
import net.sf.joafip.store.service.objectio.manager.ISubstituteObjectManager;

/**
 * 
 * @author luc peuvrier
 * 
 */
@NotStorableClass
@StorableAccess
public class TestSubstitutionWithMethodInterception2 extends AbstractTestSubstitutionWithMethodInterception {

    public TestSubstitutionWithMethodInterception2() throws TestException {
        super();
    }

    public TestSubstitutionWithMethodInterception2(final String name) throws TestException {
        super(name);
    }

    @Override
    protected Class<?> getSubstitutionClass() {
        return SubstitutionBob.class;
    }

    @Override
    protected ISubstituteObjectManager createSubstituteObjectManager() {
        return new SubstituteObjectManager2();
    }
}
