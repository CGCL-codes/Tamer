package itea.shortRuler.impl.function.valueWrapper;

import itea.shortRuler.ExpressionException;
import itea.shortRuler.RuleContext;
import itea.shortRuler.expression.ValueWrapper;

/**
 * ֵ��װ����ʵ�֡�
 * @author itea
 * @date 2007-4-5
 * 
 */
public class ValueOf implements ValueWrapper<Object> {

    private Object value = null;

    private String arg = null;

    public void wrap(String expressionEntity) {
        this.arg = expressionEntity;
        if (arg == null) {
            throw new IllegalArgumentException("the argument could not be null");
        } else if ("null".equals(arg)) {
            value = null;
        } else if ("true".equals(arg)) {
            value = new Boolean(true);
        } else if ("false".equals(arg)) {
            value = new Boolean(false);
        } else if (arg.matches("\"([^\"])*\"")) {
            value = arg.substring(1, arg.length() - 1);
        } else if (arg.matches("([0-9])+")) {
            value = new Long(arg);
        } else if (arg.matches(fpRegex)) {
            value = Double.valueOf(arg);
        } else {
        }
    }

    public void wrapObject(Object object) {
        value = object;
    }

    public Object getValue(RuleContext ruleContext) throws ExpressionException {
        if (value == null && "null".equals(arg)) return null; else if (value != null) return value; else {
            Object value = null;
            if (arg.matches("([^\"])+[.]([^\"])+")) {
                String bean = arg.substring(0, arg.indexOf("."));
                String property = arg.substring(arg.indexOf(".") + 1);
                Object obj = ruleContext.get(bean);
                if (obj == null) throw new ExpressionException("could not get bean from ruleContext: " + bean);
                BeanWrapper bea = new BeanWrapperImpl(obj);
                value = bea.getPropertyValue(property);
            } else if (arg.matches("([^\"])+")) {
                Object obj = ruleContext.get(arg);
                if (obj == null) throw new ExpressionException("could not get bean from ruleContext: " + arg);
                BeanWrapper bea = new BeanWrapperImpl(obj);
                value = bea.getBean();
            }
            return value;
        }
    }

    public Class<?> getType(RuleContext ruleContext) {
        return null;
    }

    public String toString() {
        return arg;
    }

    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((arg == null) ? 0 : arg.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ValueOf other = (ValueOf) obj;
        if (arg == null) {
            if (other.arg != null) return false;
        } else if (!arg.equals(other.arg)) return false;
        return true;
    }

    final String Digits = "(\\p{Digit}+)";

    final String HexDigits = "(\\p{XDigit}+)";

    final String Exp = "[eE][+-]?" + Digits;

    final String fpRegex = ("[\\x00-\\x20]*" + "[+-]?(" + "NaN|" + "Infinity|" + "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|" + "(\\.(" + Digits + ")(" + Exp + ")?)|" + "((" + "(0[xX]" + HexDigits + "(\\.)?)|" + "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")" + ")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");
}
