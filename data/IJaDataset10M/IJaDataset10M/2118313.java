package org.fudaa.dodico.crue.comparaison.tester;

import java.util.Collection;
import java.util.Iterator;
import org.fudaa.dodico.crue.metier.transformer.ToStringTransformer;

/**
 * @author denf01a
 * @creation 22 juin 2009
 * @version
 */
public class EqualsTesterCollection extends AbstractEqualsTester<Collection> {

    private final FactoryEqualsTester factory;

    private final String propName;

    private final EqualsTester equalTester;

    private ToStringTransformer toStringTransformer;

    public static final String TAILLE_DIFF = "comparaison.collection.size";

    private String diffMsg = "comparaison.collection.item";

    public EqualsTesterCollection(final FactoryEqualsTester factory, final String propName) {
        super();
        this.factory = factory;
        this.propName = propName;
        this.equalTester = null;
    }

    public EqualsTesterCollection(final EqualsTester tester) {
        super();
        this.factory = null;
        this.propName = null;
        this.equalTester = tester;
    }

    @Override
    protected boolean mustTestAlreadyDone() {
        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean isSameSafe(final Collection o1, final Collection o2, final ResultatTest res, final TesterContext context) throws Exception {
        if (o1.size() != o2.size()) {
            if (res != null) {
                res.addDiff(new ResultatTest(o1.size(), o2.size(), TAILLE_DIFF));
            }
            return false;
        }
        if (o1.size() == 0) {
            return true;
        }
        final Iterator it1 = o1.iterator();
        final Iterator it2 = o2.iterator();
        int idx = 0;
        EqualsTester tester = equalTester;
        final boolean canDigg = context.canDigg();
        boolean isSame = true;
        while (it1.hasNext()) {
            idx++;
            final Object obj1 = it1.next();
            final Object obj2 = it2.next();
            if (obj1 != obj2) {
                if (obj1 == null) {
                    addDiff(res, idx, obj1, obj2);
                    isSame = false;
                }
                if (tester == null) {
                    tester = factory.buildTesterFor(null, obj1.getClass(), propName, context.getError());
                    if (context.getError().containsFatalError()) return false;
                    if (TesterContextImpl.isComplexEqualsTester(tester) && !canDigg) {
                        return true;
                    }
                }
                ResultatTest itemDifferences = new ResultatTest(obj1, obj2, null);
                if (!tester.isSame(obj1, obj2, itemDifferences, context)) {
                    ResultatTest addDiff = addDiff(res, idx, obj1, obj2);
                    if (addDiff != null) {
                        addDiff.addDiffsAndUpdatePrint(itemDifferences.getFils(), context.getToStringTransformer());
                    }
                    isSame = false;
                }
            }
        }
        return isSame;
    }

    private ResultatTest addDiff(final ResultatTest res, final int idx, final Object obj1, final Object obj2) {
        if (res != null) {
            ResultatTest item = new ResultatTest(obj1, obj2, diffMsg, idx);
            if (toStringTransformer != null) {
                item.setPrintA(toStringTransformer.transform(obj1));
                item.setPrintB(toStringTransformer.transform(obj2));
            }
            res.addDiff(item);
            return item;
        }
        return null;
    }

    /**
   * @return the diffMsg
   */
    public String getDiffMsg() {
        return diffMsg;
    }

    /**
   * @param diffMsg the diffMsg to set
   */
    public void setDiffMsg(String diffMsg) {
        this.diffMsg = diffMsg;
    }

    /**
   * @return the toStringTransformer
   */
    public ToStringTransformer getToStringTransformer() {
        return toStringTransformer;
    }

    /**
   * @param toStringTransformer the toStringTransformer to set
   */
    public void setToStringTransformer(ToStringTransformer toStringTransformer) {
        this.toStringTransformer = toStringTransformer;
    }
}
