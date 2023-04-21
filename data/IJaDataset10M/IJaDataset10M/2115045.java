package org.jtools.sql.values;

import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import org.jpattern.mapper.Mapper;
import org.jtools.sql.SQLType;
import org.jtools.sql.SQLTypeVisitor;

final class ValueFactory implements SQLTypeVisitor<Value<?>, ValueMetaData>, Mapper<Value<?>, ValueMetaData> {

    private static final ValueFactory singleton = new ValueFactory();

    public static ValueFactory getInstance() {
        return singleton;
    }

    private ValueFactory() {
    }

    @Override
    public Value<?> visitARRAY(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitBIGINT(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new LongValue(md);
    }

    @Override
    public Value<?> visitBINARY(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitBIT(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitBOOLEAN(sqlType, md);
    }

    @Override
    public Value<?> visitBLOB(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitBOOLEAN(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new BooleanValue(md);
    }

    @Override
    public Value<?> visitCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitCLOB(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitDATALINK(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitDATE(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new DateValue(md);
    }

    @Override
    public Value<?> visitDECIMAL(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new BigDecimalValue(md);
    }

    @Override
    public Value<?> visitDOUBLE(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new DoubleValue(md);
    }

    @Override
    public Value<?> visitFLOAT(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitDOUBLE(sqlType, md);
    }

    @Override
    public Value<?> visitINTEGER(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new IntValue(md);
    }

    @Override
    public Value<?> visitLONGNVARCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitLONGVARBINARY(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitBINARY(sqlType, md);
    }

    @Override
    public Value<?> visitLONGVARCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitNCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitNCLOB(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitNUMERIC(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitDECIMAL(sqlType, md);
    }

    @Override
    public Value<?> visitNVARCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitVARCHAR(sqlType, md);
    }

    @Override
    public Value<?> visitREAL(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new FloatValue(md);
    }

    @Override
    public Value<?> visitREF(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitROWID(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitSMALLINT(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new ShortValue(md);
    }

    @Override
    public Value<?> visitSQLXML(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitSTRUCT(SQLType sqlType, ValueMetaData md) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitTIME(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new TimeValue(md);
    }

    @Override
    public Value<?> visitTIMESTAMP(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new TimestampValue(md);
    }

    @Override
    public Value<?> visitTINYINT(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new ByteValue(md);
    }

    @Override
    public Value<?> visitVARBINARY(SQLType sqlType, ValueMetaData md) throws SQLException {
        return visitBINARY(sqlType, md);
    }

    @Override
    public Value<?> visitVARCHAR(SQLType sqlType, ValueMetaData md) throws SQLException {
        return new StringValue(md);
    }

    @Override
    public Value<?> map(ValueMetaData item) {
        try {
            return item.getSQLType().accept(this, item);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Value<?> visitDISTINCT(SQLType sqlType, ValueMetaData data) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitJAVA_OBJECT(SQLType sqlType, ValueMetaData data) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }

    @Override
    public Value<?> visitNULL(SQLType sqlType, ValueMetaData data) throws SQLException {
        return NullValue.getInstance();
    }

    @Override
    public Value<?> visitOTHER(SQLType sqlType, ValueMetaData data) throws SQLException {
        throw new SQLFeatureNotSupportedException(sqlType.name());
    }
}
