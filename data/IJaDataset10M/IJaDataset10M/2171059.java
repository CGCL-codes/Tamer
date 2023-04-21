package org.fudaa.dodico.crue.comparaison.tester;

/**
 * @author denf01a
 * @creation 22 juin 2009
 * @version
 */
public class EqualsTesterObject extends AbstractEqualsTester<Object> {

    @Override
    public boolean isSameSafe(final Object o1, final Object o2, ResultatTest res, TesterContext context) {
        return o1.equals(o2);
    }

    @Override
    protected boolean mustTestAlreadyDone() {
        return false;
    }
}
