package ch.ethz.globis.javaom.collection;

import ch.ethz.globis.javaom.collection.Collection.Pair;
import ch.ethz.globis.javaom.collection.imp.IsEmptyParameter;
import ch.ethz.globis.javaom.collection.imp.IsInParameter;
import ch.ethz.globis.javaom.collection.imp.SizeParameter;

/**
 * @author aldespin
 * @version 1.0
 */
public class CollectionUtils {

    private static Object reduce(final Collection collection, final ReduceParameter rules) {
        if (collection.isEmpty()) {
            return rules.isEmpty();
        }
        if (collection.isSingle()) {
            return rules.isSingle(collection.aMember());
        }
        final Pair split = collection.split();
        return rules.combine(CollectionUtils.reduce(split.first(), rules), CollectionUtils.reduce(split.second(), rules));
    }

    public static Boolean isEmpty(final Collection collection) {
        return (Boolean) CollectionUtils.reduce(collection, new IsEmptyParameter());
    }

    public static boolean isIn(final Collection collection, final Object member) {
        return ((Boolean) CollectionUtils.reduce(collection, new IsInParameter(member))).booleanValue();
    }

    public static int size(final Collection collection) {
        return ((Integer) CollectionUtils.reduce(collection, new SizeParameter())).intValue();
    }

    public static Collection union(final Collection lhs, final Collection rhs) {
        return Collection.combine(lhs, rhs);
    }
}
