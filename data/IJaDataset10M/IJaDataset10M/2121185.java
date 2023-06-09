package org.apache.batik.css.engine.value.css2;

import org.apache.batik.css.engine.CSSContext;
import org.apache.batik.css.engine.CSSEngine;
import org.apache.batik.css.engine.CSSStylableElement;
import org.apache.batik.css.engine.StyleMap;
import org.apache.batik.css.engine.value.FloatValue;
import org.apache.batik.css.engine.value.IdentifierManager;
import org.apache.batik.css.engine.value.LengthManager;
import org.apache.batik.css.engine.value.StringMap;
import org.apache.batik.css.engine.value.Value;
import org.apache.batik.css.engine.value.ValueConstants;
import org.apache.batik.css.engine.value.ValueManager;
import org.apache.batik.util.CSSConstants;
import org.w3c.css.sac.LexicalUnit;
import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;

/**
 * This class provides a manager for the 'font-size' property values.
 *
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: FontSizeManager.java,v 1.1 2005/11/21 09:51:38 dev Exp $
 */
public class FontSizeManager extends LengthManager {

    /**
     * The identifier values.
     */
    protected static final StringMap values = new StringMap();

    static {
        values.put(CSSConstants.CSS_ALL_VALUE, ValueConstants.ALL_VALUE);
        values.put(CSSConstants.CSS_LARGE_VALUE, ValueConstants.LARGE_VALUE);
        values.put(CSSConstants.CSS_LARGER_VALUE, ValueConstants.LARGER_VALUE);
        values.put(CSSConstants.CSS_MEDIUM_VALUE, ValueConstants.MEDIUM_VALUE);
        values.put(CSSConstants.CSS_SMALL_VALUE, ValueConstants.SMALL_VALUE);
        values.put(CSSConstants.CSS_SMALLER_VALUE, ValueConstants.SMALLER_VALUE);
        values.put(CSSConstants.CSS_X_LARGE_VALUE, ValueConstants.X_LARGE_VALUE);
        values.put(CSSConstants.CSS_X_SMALL_VALUE, ValueConstants.X_SMALL_VALUE);
        values.put(CSSConstants.CSS_XX_LARGE_VALUE, ValueConstants.XX_LARGE_VALUE);
        values.put(CSSConstants.CSS_XX_SMALL_VALUE, ValueConstants.XX_SMALL_VALUE);
    }

    /**
     * Implements {@link IdentifierManager#getIdentifiers()}.
     */
    public StringMap getIdentifiers() {
        return values;
    }

    /**
     * Implements {@link ValueManager#isInheritedProperty()}.
     */
    public boolean isInheritedProperty() {
        return true;
    }

    /**
     * Implements {@link ValueManager#getPropertyName()}.
     */
    public String getPropertyName() {
        return CSSConstants.CSS_FONT_SIZE_PROPERTY;
    }

    /**
     * Implements {@link ValueManager#getDefaultValue()}.
     */
    public Value getDefaultValue() {
        return ValueConstants.MEDIUM_VALUE;
    }

    /**
     * Implements {@link ValueManager#createValue(LexicalUnit,CSSEngine)}.
     */
    public Value createValue(LexicalUnit lu, CSSEngine engine) throws DOMException {
        switch(lu.getLexicalUnitType()) {
            case LexicalUnit.SAC_INHERIT:
                return ValueConstants.INHERIT_VALUE;
            case LexicalUnit.SAC_IDENT:
                String s = lu.getStringValue().toLowerCase().intern();
                Object v = values.get(s);
                if (v == null) {
                    throw createInvalidIdentifierDOMException(s);
                }
                return (Value) v;
            default:
                break;
        }
        return super.createValue(lu, engine);
    }

    /**
     * Implements {@link
     * ValueManager#createStringValue(short,String,CSSEngine)}.
     */
    public Value createStringValue(short type, String value, CSSEngine engine) throws DOMException {
        if (type != CSSPrimitiveValue.CSS_IDENT) {
            throw createInvalidStringTypeDOMException(type);
        }
        Object v = values.get(value.toLowerCase().intern());
        if (v == null) {
            throw createInvalidIdentifierDOMException(value);
        }
        return (Value) v;
    }

    /**
     * Implements {@link
     * ValueManager#computeValue(CSSStylableElement,String,CSSEngine,int,StyleMap,Value)}.
     */
    public Value computeValue(CSSStylableElement elt, String pseudo, CSSEngine engine, int idx, StyleMap sm, Value value) {
        float scale = 1.0f;
        boolean doParentRelative = false;
        switch(value.getPrimitiveType()) {
            case CSSPrimitiveValue.CSS_NUMBER:
            case CSSPrimitiveValue.CSS_PX:
                return value;
            case CSSPrimitiveValue.CSS_MM:
                CSSContext ctx = engine.getCSSContext();
                float v = value.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v / ctx.getPixelUnitToMillimeter());
            case CSSPrimitiveValue.CSS_CM:
                ctx = engine.getCSSContext();
                v = value.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 10f / ctx.getPixelUnitToMillimeter());
            case CSSPrimitiveValue.CSS_IN:
                ctx = engine.getCSSContext();
                v = value.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 25.4f / ctx.getPixelUnitToMillimeter());
            case CSSPrimitiveValue.CSS_PT:
                ctx = engine.getCSSContext();
                v = value.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, v * 25.4f / (72f * ctx.getPixelUnitToMillimeter()));
            case CSSPrimitiveValue.CSS_PC:
                ctx = engine.getCSSContext();
                v = value.getFloatValue();
                return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, (v * 25.4f / (6f * ctx.getPixelUnitToMillimeter())));
            case CSSPrimitiveValue.CSS_EMS:
                doParentRelative = true;
                scale = value.getFloatValue();
                break;
            case CSSPrimitiveValue.CSS_EXS:
                doParentRelative = true;
                scale = value.getFloatValue() * 0.5f;
                break;
            case CSSPrimitiveValue.CSS_PERCENTAGE:
                doParentRelative = true;
                scale = value.getFloatValue() * 0.01f;
                break;
            default:
        }
        if (value == ValueConstants.LARGER_VALUE) {
            doParentRelative = true;
            scale = 1.2f;
        } else if (value == ValueConstants.SMALLER_VALUE) {
            doParentRelative = true;
            scale = 1 / 1.2f;
        }
        if (doParentRelative) {
            sm.putParentRelative(idx, true);
            CSSStylableElement p;
            p = CSSEngine.getParentCSSStylableElement(elt);
            float fs;
            if (p == null) {
                CSSContext ctx = engine.getCSSContext();
                fs = ctx.getMediumFontSize();
            } else {
                fs = engine.getComputedStyle(p, null, idx).getFloatValue();
            }
            return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, fs * scale);
        }
        CSSContext ctx = engine.getCSSContext();
        float fs = ctx.getMediumFontSize();
        String s = value.getStringValue();
        switch(s.charAt(0)) {
            case 'm':
                break;
            case 's':
                fs = (float) (fs / 1.2);
                break;
            case 'l':
                fs = (float) (fs * 1.2);
                break;
            default:
                switch(s.charAt(1)) {
                    case 'x':
                        switch(s.charAt(3)) {
                            case 's':
                                fs = (float) (((fs / 1.2) / 1.2) / 1.2);
                                break;
                            default:
                                fs = (float) (fs * 1.2 * 1.2 * 1.2);
                        }
                        break;
                    default:
                        switch(s.charAt(2)) {
                            case 's':
                                fs = (float) ((fs / 1.2) / 1.2);
                                break;
                            default:
                                fs = (float) (fs * 1.2 * 1.2);
                        }
                }
        }
        return new FloatValue(CSSPrimitiveValue.CSS_NUMBER, fs);
    }

    /**
     * Indicates the orientation of the property associated with
     * this manager.
     */
    protected int getOrientation() {
        return VERTICAL_ORIENTATION;
    }
}
