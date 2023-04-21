package com.doxological.doxquery.values.impl;

import java.lang.String;
import javax.xml.namespace.QName;
import com.doxological.doxquery.XQueryException;
import com.doxological.doxquery.values.*;
import com.doxological.doxquery.utils.ContextUtils;
import com.doxological.doxquery.context.SchemaDefinitions;
import static com.doxological.doxquery.utils.XQueryConstants.*;

/**
 * <p>Default implementation of an item.</p>
 *
 * @author John Snelson
 */
public abstract class AtomicItemImpl extends ItemImpl implements AtomicItem {

    protected Primitive primitive_;

    public AtomicItemImpl(QName type, Primitive primitive) {
        super(type);
        primitive_ = primitive;
    }

    public boolean isAtomic() {
        return true;
    }

    public Primitive getPrimitiveType() {
        return primitive_;
    }

    public QName getPrimitiveTypeName() {
        return ContextUtils.getPrimitiveTypeName(primitive_);
    }

    public AtomicItem castAs(QName targetTypeName, SchemaDefinitions schemaDefs) {
        if (type_.equals(targetTypeName)) {
            return this;
        }
        Primitive targetPrimitive = ContextUtils.getPrimitive(targetTypeName, schemaDefs);
        if (!castTable_[primitive_.ordinal()][targetPrimitive.ordinal()]) {
            throw new XQueryException("FORG0001", "Casting is not supported between " + type_.toString() + " and " + targetTypeName.toString());
        }
        return castAsImpl(targetTypeName, targetPrimitive);
    }

    public abstract AtomicItem castAsImpl(QName targetTypeName, Primitive targetPrimitive);

    /**
	 * S\T   uA str flt dbl dec int dur yMD dTD dT tim dat gYM gYr gMD gDay gMon bool b64 hxB aURI QN NOT<br/>
	 * uA    Y   Y   M   M   M   M   M   M   M   M   M   M   M   M   M   M   M    M    M   M   M   N   N<br/>
	 * str   Y   Y   M   M   M   M   M   M   M   M   M   M   M   M   M   M   M    M    M   M   M   M   M<br/>
	 * flt   Y   Y   Y   Y   M   M   N   N   N   N   N   N   N   N   N   N   N    Y    N   N   N   N   N<br/>
	 * dbl   Y   Y   Y   Y   M   M   N   N   N   N   N   N   N   N   N   N   N    Y    N   N   N   N   N<br/>
	 * dec   Y   Y   Y   Y   Y   Y   N   N   N   N   N   N   N   N   N   N   N    Y    N   N   N   N   N<br/>
	 * int   Y   Y   Y   Y   Y   Y   N   N   N   N   N   N   N   N   N   N   N    Y    N   N   N   N   N<br/>
	 * dur   Y   Y   N   N   N   N   Y   Y   Y   N   N   N   N   N   N   N   N    N    N   N   N   N   N<br/>
	 * yMD   Y   Y   N   N   N   N   Y   Y   N   N   N   N   N   N   N   N   N    N    N   N   N   N   N<br/>
	 * dTD   Y   Y   N   N   N   N   Y   N   Y   N   N   N   N   N   N   N   N    N    N   N   N   N   N<br/>
	 * dT    Y   Y   N   N   N   N   N   N   N   Y   Y   Y   Y   Y   Y   Y   Y    N    N   N   N   N   N<br/>
	 * tim   Y   Y   N   N   N   N   N   N   N   N   Y   N   N   N   N   N   N    N    N   N   N   N   N<br/>
	 * dat   Y   Y   N   N   N   N   N   N   N   Y   N   Y   Y   Y   Y   Y   Y    N    N   N   N   N   N<br/>
	 * gYM   Y   Y   N   N   N   N   N   N   N   N   N   N   Y   N   N   N   N    N    N   N   N   N   N<br/>
	 * gYr   Y   Y   N   N   N   N   N   N   N   N   N   N   N   Y   N   N   N    N    N   N   N   N   N<br/>
	 * gMD   Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   Y   N   N    N    N   N   N   N   N<br/>
	 * gDay  Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   Y   N    N    N   N   N   N   N<br/>
	 * gMon  Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   Y    N    N   N   N   N   N<br/>
	 * bool  Y   Y   Y   Y   Y   Y   N   N   N   N   N   N   N   N   N   N   N    Y    N   N   N   N   N<br/>
	 * b64   Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   N    N    Y   Y   N   N   N<br/>
	 * hxB   Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   N    N    Y   Y   N   N   N<br/>
	 * aURI  Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   N    N    N   N   Y   N   N<br/>
	 * QN    Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   N    N    N   N   N   Y   N<br/>
	 * NOT   Y   Y   N   N   N   N   N   N   N   N   N   N   N   N   N   N   N    N    N   N   N   N   Y
	 */
    private boolean castTable_[][] = { { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, false, false }, { true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true }, { true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false }, { true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false }, { true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false }, { true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false }, { true, true, false, false, false, false, true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, true, false, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, true, true, true, true, true, true, true, true, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, true, false, true, true, true, true, true, true, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false }, { true, true, true, true, true, true, false, false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, true, false, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true, false }, { true, true, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, true } };
}
