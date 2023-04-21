package org.plazmaforge.studio.reportdesigner.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.plazmaforge.studio.reportdesigner.model.DesignFont;
import org.plazmaforge.studio.reportdesigner.model.ICloneable;
import org.plazmaforge.studio.reportdesigner.model.data.Evaluation;
import org.plazmaforge.studio.reportdesigner.model.data.Expression;

public class CloneUtils {

    public static DesignFont cloneFont(DesignFont original) {
        return (DesignFont) clone(original);
    }

    public static Evaluation cloneEvaluation(Evaluation original) {
        return (Evaluation) clone(original);
    }

    public static Expression cloneExpression(Expression original) {
        return (Expression) clone(original);
    }

    public static ICloneable clone(ICloneable original) {
        return original == null ? null : (ICloneable) original.clone();
    }

    public static <T> List<T> cloneList(List<T> original, List<T> result) {
        if (original == null) {
            return null;
        }
        if (result == null) {
            result = new ArrayList<T>();
        }
        transferCollection(original, result);
        return result;
    }

    public static <T> List<T> cloneList(List<T> original) {
        return cloneList(original, null);
    }

    public static <T> Set<T> cloneSet(Set<T> original, Set<T> result) {
        if (original == null) {
            return null;
        }
        if (result == null) {
            result = new HashSet<T>();
        }
        transferCollection(original, result);
        return result;
    }

    public static <T> Set<T> cloneSet(Set<T> original) {
        return cloneSet(original, null);
    }

    public static void transferCollection(Collection from, Collection to) {
        if (from == null || to == null) {
            return;
        }
        Iterator itr = from.iterator();
        while (itr.hasNext()) {
            to.add(((ICloneable) itr.next()).clone());
        }
    }
}
