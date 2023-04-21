package ssgen.sql.common.element.operator;

import ssgen.core.element.SsGenElement;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class AnyOperator extends AbstractQuantifiedOperator {

    public OperatorType getOperatorType() {
        return OperatorType.ANY;
    }

    public Class<? extends SsGenElement> getElementClass() {
        return AnyOperator.class;
    }
}
