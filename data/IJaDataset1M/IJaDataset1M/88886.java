package com.daffodilwoods.daffodildb.server.sql99.expression.numericvalueexpression;

import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.rowvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;

public class acosfunction extends AbstractNumericValueFunction {

    public parennumericvalueexpression _parennumericvalueexpression0;

    public SNONRESERVEDWORD136444255 _SNONRESERVEDWORD1364442551;

    protected Object getResult(int type, Object object) throws DException {
        if (object == null) {
            return new FieldLiteral(FieldUtility.NULLBUFFERRANGE, Datatype.DOUBLE);
        }
        if (type <= 15) {
            Number number = (Number) object;
            return new FieldLiteral(new Double(Math.acos(number.doubleValue())), Datatype.DOUBLE);
        }
        throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "ACOS" });
    }

    public AbstractRowValueExpression[] getChilds() {
        AbstractRowValueExpression[] childs = new AbstractRowValueExpression[] { _parennumericvalueexpression0 };
        return childs;
    }

    public String getType() throws DException {
        return (String) _SNONRESERVEDWORD1364442551.run(null);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SNONRESERVEDWORD1364442551);
        sb.append(_parennumericvalueexpression0);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        acosfunction tempClass = new acosfunction();
        tempClass._parennumericvalueexpression0 = (parennumericvalueexpression) _parennumericvalueexpression0.clone();
        tempClass._SNONRESERVEDWORD1364442551 = (SNONRESERVEDWORD136444255) _SNONRESERVEDWORD1364442551.clone();
        return tempClass;
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        ParameterInfo[] paramInfo = super.getParameterInfo();
        for (int i = 0; i < paramInfo.length; i++) {
            if (paramInfo[i].getQuestionMark()) {
                paramInfo[i].setDataType(Datatypes.DOUBLE);
                paramInfo[i].setName("ACOS Arg");
            }
        }
        return paramInfo;
    }

    public ByteComparison getByteComparison(Object object) throws DException {
        ByteComparison byteComparison = new ByteComparison(false, new int[] { DOUBLE });
        byteComparison.setSize(getColumnSize(object));
        return byteComparison;
    }

    public _Reference[] checkSemantic(_ServerSession parent) throws DException {
        _Reference[] ref = super.checkSemantic(parent);
        if (ref != null) {
            return ref;
        }
        int type = _parennumericvalueexpression0.getByteComparison(parent).getDataTypes()[0];
        if (type == -1 || type <= 15) {
            return ref;
        }
        throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "ACOS" });
    }

    public int getColumnSize(Object object) throws DException {
        return Datatypes.DOUBLESIZE;
    }
}
