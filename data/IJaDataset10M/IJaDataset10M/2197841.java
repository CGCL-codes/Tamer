package netgest.bo.xwc.framework;

import javax.el.ValueExpression;
import netgest.bo.xwc.framework.components.XUIComponentBase;
import netgest.bo.xwc.xeo.workplaces.admin.localization.ExceptionMessage;

public class XUIStateBindProperty<V> extends XUIStateProperty<ValueExpression> {

    private Class<?> cValueType = null;

    public XUIStateBindProperty(String sPropertyName, XUIComponentBase oComponent, Class<?> cValueType) {
        super(sPropertyName, oComponent);
        this.cValueType = cValueType;
    }

    public XUIStateBindProperty(String sPropertyName, XUIComponentBase oComponent, String sExpressionString, Class<?> cValueType) {
        super(sPropertyName, oComponent, oComponent.createValueExpression(sExpressionString, cValueType));
        this.cValueType = cValueType;
    }

    public void setExpressionText(String sExpression) {
        if (sExpression != null) {
            if (!sExpression.equals(getExpressionString())) {
                super.setValue(getComponent().createValueExpression(sExpression, this.cValueType));
            }
        } else {
            super.setValue(null);
        }
    }

    public boolean isNull() {
        return super.getValue() == null;
    }

    public boolean isLiteralText() {
        if (super.getValue() != null) {
            return this.getValue().isLiteralText();
        }
        return false;
    }

    public String getExpressionString() {
        ValueExpression ve = super.getValue();
        if (ve != null) return ve.getExpressionString();
        return null;
    }

    public V getEvaluatedValue() {
        Object oRetValue;
        oRetValue = evaluateValue(getValue());
        return (V) oRetValue;
    }

    public final Object evaluateValue(ValueExpression oValExpr) {
        Object oRetValue;
        oRetValue = null;
        if (oValExpr != null) {
            if (oValExpr.isLiteralText()) {
                String sLiteralText = oValExpr.getExpressionString();
                if (oValExpr.getExpectedType() == String.class) {
                    oRetValue = sLiteralText;
                } else if (oValExpr.getExpectedType() == Double.class) {
                    oRetValue = Double.valueOf(sLiteralText);
                } else if (oValExpr.getExpectedType() == Integer.class) {
                    oRetValue = Integer.valueOf(sLiteralText);
                } else if (oValExpr.getExpectedType() == Long.class) {
                    oRetValue = Long.valueOf(sLiteralText);
                } else if (oValExpr.getExpectedType() == Boolean.class) {
                    oRetValue = Boolean.valueOf(sLiteralText);
                } else if (oValExpr.getExpectedType() == Byte.class) {
                    oRetValue = Byte.valueOf(sLiteralText);
                } else {
                    throw new RuntimeException(ExceptionMessage.CANNOT_CONVERT_EXPRESSION_TEXT.toString() + " [" + sLiteralText + "]" + ExceptionMessage.IN.toString() + " " + oValExpr.getExpectedType().getName());
                }
            } else {
                oRetValue = oValExpr.getValue(getComponent().getELContext());
            }
        }
        if (oRetValue == null) {
            if (this.cValueType == Double.class) {
                oRetValue = Double.valueOf(0);
            } else if (this.cValueType == Integer.class) {
                oRetValue = Integer.valueOf(0);
            } else if (this.cValueType == Long.class) {
                oRetValue = Long.valueOf(0);
            } else if (this.cValueType == Boolean.class) {
                oRetValue = Boolean.valueOf(false);
            } else if (this.cValueType == Byte.class) {
                oRetValue = Byte.valueOf((byte) 0);
            }
        }
        setLastEvaluatedValue(oRetValue);
        return oRetValue;
    }
}
