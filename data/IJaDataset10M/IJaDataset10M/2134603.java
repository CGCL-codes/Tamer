package ssgen.sql.common.writer.operator;

import ssgen.core.SsGenException;
import ssgen.core.element.SsGenElement;
import ssgen.core.writer.AbstractElementWriter;
import ssgen.sql.common.element.operator.OperatorType;
import ssgen.sql.common.element.operator.UnaryOperator;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author Tadaya Tsuyukubo
 * <p/>
 * $Id$
 */
public class UnaryOperatorWriter extends AbstractElementWriter {

    private static final Map<OperatorType, String> prependOprStrings = new HashMap<OperatorType, String>();

    private static final Map<OperatorType, String> appendOprStrings = new HashMap<OperatorType, String>();

    static {
        prependOprStrings.put(OperatorType.POSITIVE, "+");
        prependOprStrings.put(OperatorType.NEGATIVE, "-");
        prependOprStrings.put(OperatorType.NOT, "not ");
        prependOprStrings.put(OperatorType.EXISTS, "exists ");
        prependOprStrings.put(OperatorType.NOT_EXISTS, "not exists ");
        appendOprStrings.put(OperatorType.IS_NULL, " is null");
        appendOprStrings.put(OperatorType.IS_NOT_NULL, " is not null");
        appendOprStrings.put(OperatorType.ASC, " asc");
        appendOprStrings.put(OperatorType.DESC, " desc");
    }

    public String write(SsGenElement element) {
        UnaryOperator opr = (UnaryOperator) element;
        final OperatorType type = opr.getOperatorType();
        final String prepend = prependOprStrings.get(type);
        final String append = appendOprStrings.get(type);
        if (prepend == null && append == null) {
            throw new SsGenException("No corresponding writer for " + type);
        }
        StringBuilder sb = new StringBuilder();
        if (prepend != null) {
            sb.append(prepend);
        }
        sb.append(writeChildElement(opr.getElement()));
        if (append != null) {
            sb.append(append);
        }
        return sb.toString();
    }
}
