package org.fudaa.dodico.crue.comparaison.tester;

import org.apache.commons.lang.ObjectUtils;
import org.fudaa.dodico.crue.metier.emh.ValParamEntier;

/**
 * @author denf01a
 * @creation 22 juin 2009
 * @version
 */
public class EqualsTesterValParamEntier extends AbstractEqualsTester<ValParamEntier> {

    FactoryEqualsTester factory;

    public EqualsTesterValParamEntier(final FactoryEqualsTester factory) {
        this.factory = factory;
    }

    @Override
    protected boolean mustTestAlreadyDone() {
        return false;
    }

    @Override
    public boolean isSameSafe(final ValParamEntier o1, final ValParamEntier o2, final ResultatTest res, final TesterContext context) throws Exception {
        if (!ObjectUtils.equals(o1.getId(), o2.getId())) {
            if (res != null) {
                res.addDiff(new ResultatTest(o1.getNom(), o2.getNom(), "compare.type.diff"));
                return false;
            }
        }
        final EqualsTesterDouble build = EqualsTesterItemBuilder.DOUBLE_TESTER.build(factory, o1.getNom(), context.getError());
        if (context.getError().containsFatalError()) {
            return false;
        }
        boolean isSame = build.isSameDouble(o1.getValeur(), o2.getValeur());
        if (!isSame && res != null) {
            final ResultatTest item = new ResultatTest(o1, o2, o1.getNom());
            item.setPrintA(context.getToStringTransformer().transform(o1));
            item.setPrintB(context.getToStringTransformer().transform(o2));
            res.addDiff(item);
        }
        return isSame;
    }
}
