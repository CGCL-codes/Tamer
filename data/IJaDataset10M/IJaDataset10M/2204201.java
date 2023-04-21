package org.opoo.oqs.criterion;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.opoo.oqs.type.Type;
import org.opoo.util.ArrayUtils;
import org.opoo.util.Assert;

/**
 * �߼����ʽ
 *
 * @author Alex Lin(alex@opoo.org)
 * @version 1.0
 */
public class Logic implements Criterion {

    private static final Log log = LogFactory.getLog(Logic.class);

    private int criterionCount = 0;

    private StringBuffer qs = null;

    private Object[] values = null;

    private Type[] types = null;

    private Logic() {
        qs = new StringBuffer();
    }

    Logic(Criterion criterion) {
        this();
        addCriterion(criterion, "");
    }

    private void addCriterion(Criterion criterion, String op) {
        Assert.notNull(criterion, "criterion cannot be null");
        String nqs = criterion.toString();
        if (StringUtils.isBlank(nqs)) {
            log.debug("ignore empty criterion: " + criterion);
            return;
        }
        Object[] vals = criterion.getValues();
        Type[] tps = criterion.getTypes();
        if (vals != null && vals.length != 0 && tps != null && tps.length != 0) {
            if (values == null || types == null) {
                values = vals;
                types = tps;
            } else {
                values = ArrayUtils.concat(values, vals);
                types = (Type[]) ArrayUtils.concat(types, tps, Type.class);
            }
        }
        boolean moreThanOne = false;
        if (criterion instanceof Logic) {
            Logic res = (Logic) criterion;
            if (res.criterionCount > 1) {
                moreThanOne = true;
            }
            criterionCount += res.criterionCount;
        } else {
            criterionCount++;
        }
        if (moreThanOne) {
            qs.append(op + "(" + nqs + ")");
        } else {
            qs.append(op + nqs);
        }
    }

    public int getCriterionCount() {
        return criterionCount;
    }

    public Logic and(Criterion criterion) {
        addCriterion(criterion, " AND ");
        return this;
    }

    public Logic or(Criterion criterion) {
        addCriterion(criterion, " OR ");
        return this;
    }

    public Object[] getValues() {
        if (ArrayUtils.isEmpty(values)) {
            return null;
        }
        return values;
    }

    public String toString() {
        if (qs.length() == 0) {
            return null;
        }
        return qs.toString();
    }

    public Type[] getTypes() {
        if (ArrayUtils.isEmpty(types)) {
            return null;
        }
        return types;
    }
}
