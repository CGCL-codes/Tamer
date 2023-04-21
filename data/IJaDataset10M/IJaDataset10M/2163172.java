package org.neodatis.odb.core.query.values;

import java.math.BigDecimal;
import org.neodatis.odb.OID;
import org.neodatis.odb.core.layers.layer2.meta.AttributeValuesMap;
import org.neodatis.odb.core.query.IQueryFieldAction;

/**
 * An action to compute the max value of a field
 * @author osmadja
 *
 */
public class MinValueAction extends AbstractQueryFieldAction {

    private BigDecimal minValue;

    private OID oidOfMinValues;

    public MinValueAction(String attributeName, String alias) {
        super(attributeName, alias, false);
        this.minValue = new BigDecimal(Long.MAX_VALUE);
        this.oidOfMinValues = null;
    }

    public void execute(OID oid, AttributeValuesMap values) {
        Number n = (Number) values.get(attributeName);
        BigDecimal bd = ValuesUtil.convert(n);
        if (minValue.compareTo(bd) > 0) {
            oidOfMinValues = oid;
            minValue = bd;
        }
    }

    public Object getValue() {
        return minValue;
    }

    public void end() {
    }

    public void start() {
    }

    public OID getOidOfMinValues() {
        return oidOfMinValues;
    }

    public IQueryFieldAction copy() {
        return new MinValueAction(attributeName, alias);
    }
}
