package com.daffodilwoods.daffodildb.server.sql99.expression.datetimevalueexpression;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import com.daffodilwoods.daffodildb.server.datasystem.interfaces.*;
import com.daffodilwoods.daffodildb.server.serversystem.*;
import com.daffodilwoods.daffodildb.server.sql99.common.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.rowvalueexpression.*;
import com.daffodilwoods.daffodildb.server.sql99.token.*;
import com.daffodilwoods.daffodildb.server.sql99.utils.*;
import com.daffodilwoods.daffodildb.utils.*;
import com.daffodilwoods.daffodildb.utils.field.*;
import com.daffodilwoods.database.resource.*;
import com.daffodilwoods.daffodildb.server.sql99.expression.stringvalueexpression.charactervaluefunction;
import com.daffodilwoods.daffodildb.server.sql99.expression.stringvalueexpression.AbstractStringValueExpression;

public class monthnamefunction extends AbstractStringValueExpression implements com.daffodilwoods.daffodildb.utils.parser.StatementExecuter, charactervaluefunction, TypeConstants, Datatypes {

    public Srightparen_1874859514 _Srightparen_18748595140;

    public datetimevalueexpression _datetimevalueexpression1;

    public Sleftparen653880241 _Sleftparen6538802412;

    public SNONRESERVEDWORD136444255 _SNONRESERVEDWORD1364442553;

    private String[] MONTHNAMES = { "JANUARY", "FEBURARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER" };

    public Object run(Object object) throws com.daffodilwoods.database.resource.DException {
        FieldBase result = (FieldBase) _datetimevalueexpression1.run(object);
        return (result.isNull()) ? new FieldLiteral(FieldUtility.NULLBUFFERRANGE, CHARACTER) : getResult(getDataType(result), result.getObject());
    }

    protected ParameterInfo[] getThisParameterInfo() throws DException {
        ParameterInfo parameterInfo = new ParameterInfo();
        parameterInfo.setName(toString());
        parameterInfo.setDataType(CHARACTER);
        return new ParameterInfo[] { parameterInfo };
    }

    protected Object getResult(int type, Object object) throws DException {
        if (object == null) {
            return new FieldStringLiteral(FieldUtility.NULLBUFFERRANGE, Datatype.CHARACTER);
        }
        GregorianCalendar calendar = new GregorianCalendar();
        switch(type) {
            case BYTE:
            case TINYINT:
            case INTEGER:
            case INT:
            case SHORT:
            case SMALLINT:
                Number operand = (Number) object;
                calendar.set(Calendar.DAY_OF_YEAR, operand.intValue());
                return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
            case REAL:
            case DOUBLE:
            case FLOAT:
            case DOUBLEPRECISION:
            case LONG:
            case BIGINT:
            case BIGDECIMAL:
            case DEC:
            case DECIMAL:
            case NUMERIC:
                throw new DException("DSE8106", new Object[] { StaticClass.getDataTypeName(type) });
            case CHARACTER:
            case VARCHAR:
            case CHAR:
            case CHARACTERVARYING:
                String string = (String) object;
                try {
                    Timestamp timestamp = TypeValidityHandler.getTimestamp(string);
                    Date d1 = new com.daffodilwoods.daffodildb.utils.DBDate(timestamp.getTime());
                    calendar.setTime(d1);
                    return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
                } catch (DException ex) {
                    try {
                        Object dateObj = TypeValidityHandler.getDBDate(string);
                        Date d2 = (Date) dateObj;
                        calendar.setTime(d2);
                        return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
                    } catch (DException ex1) {
                        try {
                            Time time = TypeValidityHandler.getTime(string);
                            Date d = new com.daffodilwoods.daffodildb.utils.DBDate(time.getTime());
                            calendar.setTime(d);
                            return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
                        } catch (DException ex2) {
                            try {
                                calendar.set(Calendar.DAY_OF_YEAR, new Integer(string).intValue());
                                return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
                            } catch (Exception ex3) {
                                throw new DException("DSE419", new Object[] { string });
                            }
                        }
                    }
                }
            case DATE:
                Date d2 = (Date) object;
                calendar.setTime(d2);
                return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
            case TIME:
                Time time = (Time) object;
                Date d = new com.daffodilwoods.daffodildb.utils.DBDate(time.getTime());
                calendar.setTime(d);
                return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
            case TIMESTAMP:
                Timestamp time1 = (Timestamp) object;
                Date d1 = new com.daffodilwoods.daffodildb.utils.DBDate(time1.getTime());
                calendar.setTime(d1);
                return new FieldStringLiteral(new String(MONTHNAMES[calendar.get(Calendar.MONTH)]), Datatype.CHARACTER);
            default:
                throw new DException("DSE4108", new Object[] { StaticClass.getDataTypeName(type), "MONTHNAME" });
        }
    }

    public AbstractRowValueExpression[] getChilds() {
        AbstractRowValueExpression[] childs = new AbstractRowValueExpression[] { (AbstractRowValueExpression) (_datetimevalueexpression1) };
        return childs;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(" ");
        sb.append(_SNONRESERVEDWORD1364442553);
        sb.append(" ");
        sb.append(_Sleftparen6538802412);
        sb.append(" ");
        sb.append(_datetimevalueexpression1);
        sb.append(" ");
        sb.append(_Srightparen_18748595140);
        return sb.toString();
    }

    public Object clone() throws CloneNotSupportedException {
        monthnamefunction tempClass = new monthnamefunction();
        tempClass._Srightparen_18748595140 = (Srightparen_1874859514) _Srightparen_18748595140.clone();
        tempClass._datetimevalueexpression1 = (datetimevalueexpression) _datetimevalueexpression1.clone();
        tempClass._Sleftparen6538802412 = (Sleftparen653880241) _Sleftparen6538802412.clone();
        tempClass._SNONRESERVEDWORD1364442553 = (SNONRESERVEDWORD136444255) _SNONRESERVEDWORD1364442553.clone();
        return tempClass;
    }

    public ByteComparison getByteComparison(Object object) throws DException {
        ByteComparison byteComparison = new ByteComparison(false, new int[] { CHARACTER });
        byteComparison.setSize(getColumnSize(object));
        return byteComparison;
    }

    public String getType() throws DException {
        return (String) _SNONRESERVEDWORD1364442553.run(null);
    }

    public _Reference[] checkSemantic(_ServerSession parent) throws DException {
        _Reference[] ref = super.checkSemantic(parent);
        if (ref != null) {
            return ref;
        }
        int type = _datetimevalueexpression1.getByteComparison(parent).getDataTypes()[0];
        switch(type) {
            case -1:
            case BYTE:
            case TINYINT:
            case INTEGER:
            case INT:
            case REAL:
            case DOUBLE:
            case FLOAT:
            case DOUBLEPRECISION:
            case LONG:
            case BIGINT:
            case SHORT:
            case SMALLINT:
            case BIGDECIMAL:
            case DEC:
            case DECIMAL:
            case NUMERIC:
            case DATE:
            case TIME:
            case TIMESTAMP:
                return ref;
            case CHARACTER:
            case VARCHAR:
            case CHAR:
            case CHARACTERVARYING:
            default:
                throw new DException("DSE419", new Object[] { StaticClass.getDataTypeName(type), "MONTH" });
        }
    }

    public ParameterInfo[] getParameterInfo() throws DException {
        ParameterInfo[] paramInfo = super.getParameterInfo();
        for (int i = 0; i < paramInfo.length; i++) {
            if (paramInfo[i].getQuestionMark()) {
                paramInfo[i].setDataType(Datatypes.DATE);
                paramInfo[i].setName("MONTHNAME Arg");
            }
        }
        return paramInfo;
    }

    public int getColumnSize(Object object) throws DException {
        int maxLengthOfName = 9;
        return maxLengthOfName;
    }

    public ColumnDetails[] getChildColumnDetails() throws DException {
        return columnDetails;
    }
}
