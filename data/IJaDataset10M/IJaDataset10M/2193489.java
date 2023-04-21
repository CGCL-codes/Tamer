package org.hsqldb.types;

import org.hsqldb.OpTypes;
import org.hsqldb.Session;
import org.hsqldb.SessionInterface;
import org.hsqldb.Tokens;
import org.hsqldb.error.Error;
import org.hsqldb.error.ErrorCode;
import org.hsqldb.store.BitMap;

/**
 * Type implementation for BOOLEAN.<p>
 *
 * @author Fred Toussi (fredt@users dot sourceforge.net)
 * @version 1.9.0
 * @since 1.9.0
 */
public final class BooleanType extends Type {

    static final BooleanType booleanType = new BooleanType();

    private BooleanType() {
        super(Types.SQL_BOOLEAN, Types.SQL_BOOLEAN, 0, 0);
    }

    public int displaySize() {
        return 5;
    }

    public int getJDBCTypeCode() {
        return Types.BOOLEAN;
    }

    public Class getJDBCClass() {
        return Boolean.class;
    }

    public String getJDBCClassName() {
        return "java.lang.Boolean";
    }

    public String getNameString() {
        return Tokens.T_BOOLEAN;
    }

    public String getDefinition() {
        return Tokens.T_BOOLEAN;
    }

    public boolean isBooleanType() {
        return true;
    }

    public Type getAggregateType(Type other) {
        if (typeCode == other.typeCode) {
            return this;
        }
        if (other.isCharacterType()) {
            return other.getAggregateType(this);
        }
        throw Error.error(ErrorCode.X_42562);
    }

    public Type getCombinedType(Type other, int operation) {
        switch(operation) {
            case OpTypes.EQUAL:
                if (other.isBooleanType()) {
                    return this;
                }
        }
        throw Error.error(ErrorCode.X_42562);
    }

    public int compare(Session session, Object a, Object b) {
        if (a == b) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        boolean boola = ((Boolean) a).booleanValue();
        boolean boolb = ((Boolean) b).booleanValue();
        return (boola == boolb) ? 0 : (boolb ? -1 : 1);
    }

    public Object convertToTypeLimits(SessionInterface session, Object a) {
        return a;
    }

    public Object convertToType(SessionInterface session, Object a, Type otherType) {
        if (a == null) {
            return a;
        }
        switch(otherType.typeCode) {
            case Types.SQL_BOOLEAN:
                return a;
            case Types.SQL_BIT:
            case Types.SQL_BIT_VARYING:
                {
                    BinaryData b = (BinaryData) a;
                    if (b.bitLength(session) == 1) {
                        return BitMap.isSet(b.getBytes(), 0) ? Boolean.TRUE : Boolean.FALSE;
                    }
                    break;
                }
            case Types.SQL_CLOB:
                a = Type.SQL_VARCHAR.convertToType(session, a, otherType);
            case Types.SQL_CHAR:
            case Types.SQL_VARCHAR:
            case Types.VARCHAR_IGNORECASE:
                {
                    a = ((CharacterType) otherType).trim(session, a, (int) ' ', true, true);
                    if (((String) a).equalsIgnoreCase(Tokens.T_TRUE)) {
                        return Boolean.TRUE;
                    } else if (((String) a).equalsIgnoreCase(Tokens.T_FALSE)) {
                        return Boolean.FALSE;
                    } else if (((String) a).equalsIgnoreCase(Tokens.T_UNKNOWN)) {
                        return null;
                    }
                    break;
                }
            case Types.SQL_NUMERIC:
            case Types.SQL_DECIMAL:
                return NumberType.isZero(a) ? Boolean.FALSE : Boolean.TRUE;
            case Types.TINYINT:
            case Types.SQL_SMALLINT:
            case Types.SQL_INTEGER:
            case Types.SQL_BIGINT:
                {
                    if (((Number) a).longValue() == 0) {
                        return Boolean.FALSE;
                    } else {
                        return Boolean.TRUE;
                    }
                }
        }
        throw Error.error(ErrorCode.X_22018);
    }

    /**
     * ResultSet getBoolean support
     */
    public Object convertToTypeJDBC(SessionInterface session, Object a, Type otherType) {
        if (a == null) {
            return a;
        }
        switch(otherType.typeCode) {
            case Types.SQL_BOOLEAN:
                return a;
            default:
                if (otherType.isLobType()) {
                    throw Error.error(ErrorCode.X_42561);
                }
                if (otherType.isCharacterType()) {
                    if ("0".equals(a)) {
                        return Boolean.TRUE;
                    } else if ("1".equals(a)) {
                        return Boolean.TRUE;
                    }
                }
                return convertToType(session, a, otherType);
        }
    }

    public Object convertToDefaultType(SessionInterface session, Object a) {
        if (a == null) {
            return null;
        }
        if (a instanceof Boolean) {
            return a;
        } else if (a instanceof String) {
            return convertToType(session, a, Type.SQL_VARCHAR);
        }
        throw Error.error(ErrorCode.X_42561);
    }

    public String convertToString(Object a) {
        if (a == null) {
            return null;
        }
        return ((Boolean) a).booleanValue() ? Tokens.T_TRUE : Tokens.T_FALSE;
    }

    public String convertToSQLString(Object a) {
        if (a == null) {
            return Tokens.T_UNKNOWN;
        }
        return ((Boolean) a).booleanValue() ? Tokens.T_TRUE : Tokens.T_FALSE;
    }

    public boolean canConvertFrom(Type otherType) {
        return otherType.typeCode == Types.SQL_ALL_TYPES || otherType.isBooleanType() || otherType.isCharacterType() || otherType.isIntegralType() || (otherType.isBitType() && otherType.precision == 1);
    }

    public int canMoveFrom(Type otherType) {
        return otherType.isBooleanType() ? 0 : -1;
    }

    public static BooleanType getBooleanType() {
        return booleanType;
    }
}
