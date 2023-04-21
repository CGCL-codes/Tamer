package com.daffodilwoods.daffodildb.server.sql99.expression.numericvalueexpression;

import java.math.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.rowvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;

public class floorfunction extends AbstractNumericValueFunction {

    public parennumericvalueexpression _parennumericvalueexpression0;

    public SNONRESERVEDWORD136444255 _SNONRESERVEDWORD1364442551;

    protected Object getResult(int type, Object object) throws DException {
        if (object == null) {
            return new FieldLiteral(FieldUtility.NULLBUFFERRANGE, Datatype.INTEGER);
        }
        switch(type) {
            case BYTE:
            case TINYINT:
            case SHORT:
            case SMALLINT:
            case INTEGER:
            case INT:
            case REAL:
            case DOUBLE:
            case FLOAT:
            case DOUBLEPRECISION:
                Number operand = (Number) object;
                return new FieldLiteral(new Integer((new Double((Math.floor(operand.doubleValue())))).intValue()), Datatype.INTEGER);
            case LONG:
            case BIGINT:
                return new FieldLiteral(new Long(new Double(Math.floor(((Number) object).doubleValue())).longValue()), Datatype.LONG);
            case BIGDECIMAL:
            case DEC:
            case DECIMAL:
            case NUMERIC:
                return new FieldLiteral(new BigDecimal(Math.floor(((Number) object).doubleValue())), Datatype.BIGDECIMAL);
            default:
                throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "FLOOR" });
        }
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
        floorfunction tempClass = new floorfunction();
        tempClass._parennumericvalueexpression0 = (parennumericvalueexpression) _parennumericvalueexpression0.clone();
        tempClass._SNONRESERVEDWORD1364442551 = (SNONRESERVEDWORD136444255) _SNONRESERVEDWORD1364442551.clone();
        return tempClass;
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        ParameterInfo[] paramInfo = super.getParameterInfo();
        for (int i = 0; i < paramInfo.length; i++) {
            if (paramInfo[i].getQuestionMark()) {
                paramInfo[i].setDataType(Datatypes.BIGDECIMAL);
                paramInfo[i].setName("FLOOR Arg");
            }
        }
        return paramInfo;
    }

    public ByteComparison getByteComparison(Object object) throws DException {
        ByteComparison byteComparison = new ByteComparison(false, new int[] { getDatatype(object) });
        byteComparison.setSize(getColumnSize(object));
        return byteComparison;
    }

    private int getDatatype(Object object) throws DException {
        int type = getDataTypeForByte(_parennumericvalueexpression0.getByteComparison(object));
        switch(type) {
            case BYTE:
            case TINYINT:
            case SHORT:
            case SMALLINT:
            case INTEGER:
            case INT:
            case REAL:
            case DOUBLE:
            case FLOAT:
            case DOUBLEPRECISION:
                return INTEGER;
            case BIGINT:
            case LONG:
                return LONG;
            case -1:
            case BIGDECIMAL:
            case DEC:
            case DECIMAL:
            case NUMERIC:
                return BIGDECIMAL;
            default:
                throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "FLOOR" });
        }
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
        throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "FLOOR" });
    }

    public int getColumnSize(Object object) throws DException {
        ColumnDetails[] columnDetails = getChildColumnDetails();
        if (columnDetails[0].getType() != TypeConstants.CONSTANT) {
            int datatype = columnDetails[0].getDatatype();
            switch(datatype) {
                case BYTE:
                case TINYINT:
                case SHORT:
                case SMALLINT:
                    return SHORTSIZE;
                case INTEGER:
                case INT:
                    return INTSIZE;
                case REAL:
                    return FLOATSIZE;
                case DOUBLE:
                case FLOAT:
                case DOUBLEPRECISION:
                    return DOUBLESIZE;
                case LONG:
                case BIGINT:
                    return LONGSIZE;
                default:
                    if (columnDetails[0].getSize() != -5) {
                        return columnDetails[0].getSize();
                    }
            }
        } else if (columnDetails[0].getQuestion()) {
            return Datatypes.DOUBLESIZE;
        } else {
            FieldBase field = (FieldBase) _parennumericvalueexpression0.run(object);
            field.setDatatype(columnDetails[0].getDatatype());
            return field.getLength();
        }
        return -1;
    }
}
