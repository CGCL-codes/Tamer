package jaxlib.jdbc.tds;

import java.io.IOException;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.Types;
import javax.annotation.Nullable;

/**
 * This class is a descriptor for result set columns.
 * <p>
 * Implementation note:
 * <p>
 *      Getter/setter methods have not been provided to avoid clutter
 *      as this class is used in many places in the driver.
 *      As the class is package private this seems reasonable.
 *
 * @author  Mike Hutchinson
 * @author  <a href="mailto:joerg.wassmer@web.de">Joerg Wassmer</a>
 * @since   JaXLib 1.0
 * @version $Id: ColInfo.java 3042 2012-01-30 12:56:05Z joerg_wassmer $
 */
final class ColInfo extends Object {

    /**
   * Internal TDS data type
   */
    final int tdsType;

    /**
   * JDBC type constant from java.sql.Types
   */
    final int jdbcType;

    /**
   * Column data type supports SQL NULL
   */
    final int nullable;

    /**
   * Column name is case sensitive
   */
    final boolean isCaseSensitive;

    /**
   * Column may be updated
   */
    final boolean isWriteable;

    /**
   * Column is an indentity column
   */
    final boolean isIdentity;

    /**
   * Database ID for UDT
   */
    final int userType;

    /**
   * Column display size
   */
    final int displaySize;

    /**
   * Column buffer (max) size
   */
    private final int bufferSize;

    /**
   * Column precision.
   * -1 for lobs.
   */
    final int precision;

    /**
   * Column decimal scale
   */
    final int scale;

    /**
   * The SQL type name for this column.
   * Must always be an internalized strings constant, such we can use indentity comparison.
   *
   * @see String#intern()
   */
    final String sqlType;

    final int bytesRead;

    /**
   * Column actual table name
   */
    String realName;

    /**
   * Column label or name
   */
    String name;

    /**
   * Table name owning this column
   */
    String tableName;

    /**
   * Database owning this column
   */
    String catalog;

    /**
   * User owning this column
   */
    String schema;

    /**
   * Column may be used as a key
   */
    boolean isKey;

    /**
   * Column should be hidden
   */
    boolean isHidden;

    /**
   * Character set descriptor (if different from default)
   */
    CharsetInfo charsetInfo;

    /**
   * MS SQL2000 collation
   */
    long collation;

    /**
   * Create a new ColInfo for the specified JDBC type.
   * Called by CachedResultSet.
   */
    ColInfo(final String name, final String realName, final int jdbcType) throws SQLDataException {
        super();
        this.isCaseSensitive = false;
        this.isIdentity = false;
        this.isWriteable = false;
        this.jdbcType = jdbcType;
        this.name = name;
        this.nullable = 2;
        this.realName = realName;
        this.scale = 0;
        this.userType = 0;
        switch(jdbcType) {
            case Types.VARCHAR:
                this.tdsType = TdsTypes.XSYBVARCHAR;
                this.bufferSize = TdsTypes.MS_LONGVAR_MAX;
                this.displaySize = TdsTypes.MS_LONGVAR_MAX;
                this.precision = TdsTypes.MS_LONGVAR_MAX;
                break;
            case Types.INTEGER:
                this.tdsType = TdsTypes.SYBINT4;
                this.bufferSize = 4;
                this.displaySize = 11;
                this.precision = 10;
                break;
            case Types.SMALLINT:
                this.tdsType = TdsTypes.SYBINT2;
                this.bufferSize = 2;
                this.displaySize = 6;
                this.precision = 5;
                break;
            case Types.BIT:
                this.tdsType = TdsTypes.SYBBIT;
                this.bufferSize = 1;
                this.displaySize = 1;
                this.precision = 1;
                break;
            default:
                throw new SQLDataException(Messages.get("error.baddatatype", Integer.toString(jdbcType)), "22003");
        }
        this.sqlType = TdsSQLType.get(this.tdsType).sqlType;
        this.bytesRead = 0;
    }

    /**
   * Read the TDS datastream and populate the ColInfo parameter with
   * data type and related information.
   * <p>The type infomation conforms to one of the following formats:
   * <ol>
   * <li> [int1 type]  - eg SYBINT4.
   * <li> [int1 type] [int1 buffersize]  - eg VARCHAR &lt; 256
   * <li> [int1 type] [int2 buffersize]  - eg VARCHAR &gt; 255.
   * <li> [int1 type] [int4 buffersize] [int1 tabnamelen] [int1*n tabname] - eg text.
   * <li> [int1 type] [int4 buffersize] - eg sql_variant.
   * <li> [int1 type] [int1 buffersize] [int1 precision] [int1 scale] - eg decimal.
   * </ol>
   * For TDS 8 large character types include a 5 byte collation field after the buffer size.
   *
   * @param in The server response stream.
   *
   * @throws IOException
   * @throws TdsProtocolException
   */
    ColInfo(final TdsConnection connection, final TdsInputStream in, @Nullable String catalog, @Nullable String schema, @Nullable String tableName, @Nullable String name, @Nullable String realName, final int userType, final int nullable, final boolean identity, final boolean key, final boolean writeable, final boolean caseSensitive, final boolean hidden, final boolean tds7ResultToken) throws IOException, SQLException {
        super();
        final TdsVersion tdsVersion = in.getTdsVersion();
        final int tdsType = in.readUnsignedByte();
        final TdsSQLType tdsSqlType = TdsSQLType.get(tdsType);
        int bytesRead = 1;
        if ((tdsSqlType == null) || ((tdsVersion.major == 5) && (tdsType == TdsTypes.SYBLONGDATA))) throw new SQLNonTransientConnectionException("Invalid TDS data type 0x" + Integer.toHexString(tdsType & 0xFF));
        int bufferSize = tdsSqlType.size;
        long collation = 0;
        int displaySize = tdsSqlType.displaySize;
        int jdbcType = tdsSqlType.jdbcType;
        int precision = tdsSqlType.precision;
        int scale = 0;
        String sqlType = tdsSqlType.sqlType(identity);
        switch(tdsSqlType.size) {
            case -5:
                {
                    bufferSize = in.readInt();
                    bytesRead += 4;
                }
                break;
            case -4:
                {
                    bufferSize = in.readInt();
                    if ((tdsVersion.major >= 8) && TdsSQLType.isCollation(tdsType)) {
                        collation = in.readUnsignedInt() | (((long) in.readUnsignedByte()) << 32);
                        bytesRead += 5;
                    }
                    if (tdsVersion.major < 9) {
                        final int lenName = in.readShort();
                        tableName = in.readString(lenName);
                        bytesRead += 6 + ((tdsVersion.major >= 7) ? (lenName * 2) : lenName);
                    } else {
                        final int count = in.readUnsignedByte();
                        if (count > 4) throw new IOException("unexpected count of elements in SQL identifier: " + count);
                        bytesRead++;
                        if (count > 0) {
                            int len = in.readUnsignedShort();
                            bytesRead += 2;
                            if (len > 0) {
                                tableName = in.readUnicodeString(len);
                                bytesRead += (len * 2);
                            }
                            if (count > 1) {
                                len = in.readUnsignedShort();
                                bytesRead += 2;
                                if (len > 0) {
                                    schema = in.readUnicodeString(len);
                                    bytesRead += (len * 2);
                                }
                                if (count > 2) {
                                    len = in.readUnsignedShort();
                                    bytesRead += 2;
                                    if (len > 0) {
                                        catalog = in.readUnicodeString(len);
                                        bytesRead += (len * 2);
                                    }
                                    if (count > 3) {
                                        len = in.readUnsignedShort();
                                        bytesRead += 2;
                                        if (len > 0) {
                                            in.skip(len * 2);
                                            bytesRead += (len * 2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            case -3:
                break;
            case -2:
                {
                    if ((tdsVersion.major == 5) && (tdsType == TdsTypes.XSYBCHAR)) {
                        bufferSize = in.readInt();
                        bytesRead += 4;
                    } else {
                        bufferSize = in.readShort();
                        bytesRead += 2;
                    }
                    if ((tdsVersion.major >= 8) && TdsSQLType.isCollation(tdsType)) {
                        collation = in.readUnsignedInt() | (((long) in.readUnsignedByte()) << 32);
                        bytesRead += 5;
                    }
                }
                break;
            case -1:
                bytesRead += 1;
                bufferSize = in.readUnsignedByte();
                break;
            default:
                break;
        }
        switch(tdsSqlType) {
            case DATETIME:
                scale = 3;
                break;
            case TIMEN2:
                scale = bufferSize;
                break;
            case DATETIMEN:
                if (bufferSize == 8) {
                    displaySize = TdsSQLType.DATETIME.displaySize;
                    precision = TdsSQLType.DATETIME.precision;
                    scale = 3;
                } else {
                    displaySize = TdsSQLType.DATETIME4.displaySize;
                    precision = TdsSQLType.DATETIME4.precision;
                    sqlType = TdsSQLType.DATETIME4.sqlType(identity);
                    scale = 0;
                }
                break;
            case DATETIME2N:
                scale = bufferSize;
                if (scale > 7) throw new SQLNonTransientConnectionException("illegal datetime2 scale: " + scale);
                precision = "yyyy-mm-dd hh:mm:ss".length() + ((scale <= 0) ? 0 : (scale + 1));
                break;
            case DATETIMEOFFSET:
                scale = bufferSize;
                if (scale > 7) throw new SQLNonTransientConnectionException("illegal datetimeoffset scale: " + scale);
                precision = "yyyy-mm-dd hh:mm:ss +HH:MM".length() + ((scale <= 0) ? 0 : (scale + 1));
                break;
            case FLTN:
                if (bufferSize == 8) {
                    displaySize = TdsSQLType.FLT8.displaySize;
                    precision = TdsSQLType.FLT8.precision;
                } else {
                    displaySize = TdsSQLType.REAL.displaySize;
                    precision = TdsSQLType.REAL.precision;
                    jdbcType = TdsSQLType.REAL.jdbcType;
                    sqlType = TdsSQLType.REAL.sqlType(identity);
                }
                break;
            case INTN:
                switch(bufferSize) {
                    case 1:
                        displaySize = TdsSQLType.INT1.displaySize;
                        precision = TdsSQLType.INT1.precision;
                        jdbcType = TdsSQLType.INT1.jdbcType;
                        sqlType = TdsSQLType.INT1.sqlType(identity);
                        break;
                    case 2:
                        displaySize = TdsSQLType.INT2.displaySize;
                        precision = TdsSQLType.INT2.precision;
                        jdbcType = TdsSQLType.INT2.jdbcType;
                        sqlType = TdsSQLType.INT2.sqlType(identity);
                        break;
                    case 4:
                        displaySize = TdsSQLType.INT4.displaySize;
                        precision = TdsSQLType.INT4.precision;
                        break;
                    case 8:
                        displaySize = TdsSQLType.INT8.displaySize;
                        precision = TdsSQLType.INT8.precision;
                        jdbcType = TdsSQLType.INT8.jdbcType;
                        sqlType = TdsSQLType.INT8.sqlType(identity);
                        break;
                    default:
                        throw new SQLNonTransientConnectionException("unexpected column buffer size: " + bufferSize);
                }
                break;
            case UINTN:
                if (bufferSize == 8) {
                    displaySize = TdsSQLType.UINT8.displaySize;
                    precision = TdsSQLType.UINT8.precision;
                    jdbcType = TdsSQLType.UINT8.jdbcType;
                    sqlType = TdsSQLType.UINT8.sqlType(identity);
                } else if (bufferSize == 4) {
                    displaySize = TdsSQLType.UINT4.displaySize;
                    precision = TdsSQLType.UINT4.precision;
                } else if (bufferSize == 2) {
                    displaySize = TdsSQLType.UINT2.displaySize;
                    precision = TdsSQLType.UINT2.precision;
                    jdbcType = TdsSQLType.UINT2.jdbcType;
                    sqlType = TdsSQLType.UINT2.sqlType(identity);
                } else {
                    throw new SQLNonTransientConnectionException("unsigned int null (size 1) not supported");
                }
                break;
            case MONEY:
            case MONEY4:
                scale = 4;
                break;
            case MONEYN:
                if (bufferSize == 8) {
                    displaySize = TdsSQLType.MONEY.displaySize;
                    precision = TdsSQLType.MONEY.precision;
                } else {
                    displaySize = TdsSQLType.MONEY4.displaySize;
                    precision = TdsSQLType.MONEY4.precision;
                    sqlType = TdsSQLType.MONEY4.sqlType(identity);
                }
                scale = 4;
                break;
            case DECIMAL:
            case NUMERIC:
                precision = in.readUnsignedByte();
                scale = in.readUnsignedByte();
                displaySize = ((scale > 0) ? 2 : 1) + precision;
                bytesRead += 2;
                sqlType = tdsSqlType.sqlType(identity);
                break;
            case IMAGE:
                precision = Integer.MAX_VALUE;
                displaySize = Integer.MAX_VALUE;
                break;
            case LONGBINARY:
            case VARBINARY:
            case BINARY:
            case XBINARY:
            case XVARBINARY:
                if (userType == TdsTypes.UDT_TIMESTAMP) {
                    displaySize = TdsSQLType.ROWVERSION.displaySize;
                    jdbcType = TdsSQLType.ROWVERSION.jdbcType;
                    precision = TdsSQLType.ROWVERSION.precision;
                    sqlType = (tdsVersion.major >= 8) ? "rowversion" : "timestamp";
                } else {
                    displaySize = precision * 2;
                    precision = bufferSize;
                }
                break;
            case NTEXT:
                precision = Integer.MAX_VALUE >> 1;
                displaySize = Integer.MAX_VALUE >> 1;
                break;
            case UNITEXT:
                precision = Integer.MAX_VALUE >> 1;
                displaySize = Integer.MAX_VALUE >> 1;
                break;
            case XNCHAR:
            case XNVARCHAR:
                displaySize = bufferSize >> 1;
                precision = displaySize;
                if (userType == TdsTypes.UDT_NEWSYSNAME) sqlType = "sysname";
                break;
            case CHAR:
            case NVARCHAR:
            case TEXT:
            case VARCHAR:
            case XCHAR:
            case XVARCHAR:
                precision = bufferSize;
                displaySize = precision;
                if (userType == TdsTypes.UDT_SYSNAME) sqlType = "sysname";
                break;
            default:
                break;
        }
        if (tdsVersion.major == 5) {
            if (tdsType == TdsTypes.SYBLONGBINARY) {
                switch(userType) {
                    case TdsTypes.UDT_BINARY:
                        sqlType = "binary";
                        displaySize = bufferSize * 2;
                        jdbcType = java.sql.Types.BINARY;
                        break;
                    case TdsTypes.UDT_VARBINARY:
                        sqlType = "varbinary";
                        displaySize = bufferSize * 2;
                        jdbcType = java.sql.Types.VARBINARY;
                        break;
                    case TdsTypes.UDT_UNICHAR:
                        sqlType = "unichar";
                        displaySize = bufferSize >> 1;
                        precision = displaySize;
                        jdbcType = java.sql.Types.CHAR;
                        break;
                    case TdsTypes.UDT_UNIVARCHAR:
                        sqlType = "univarchar";
                        displaySize = bufferSize >> 1;
                        precision = displaySize;
                        jdbcType = java.sql.Types.VARCHAR;
                        break;
                    default:
                        break;
                }
            } else if (tdsType == TdsTypes.XSYBCHAR) {
                switch(userType) {
                    case TdsTypes.UDT_CHAR:
                        sqlType = "char";
                        displaySize = bufferSize;
                        jdbcType = java.sql.Types.CHAR;
                        break;
                    case TdsTypes.UDT_VARCHAR:
                        sqlType = "varchar";
                        displaySize = bufferSize;
                        jdbcType = java.sql.Types.VARCHAR;
                        break;
                    case TdsTypes.UDT_NCHAR:
                        sqlType = "nchar";
                        displaySize = bufferSize;
                        jdbcType = java.sql.Types.CHAR;
                        break;
                    case TdsTypes.UDT_NVARCHAR:
                        sqlType = "nvarchar";
                        displaySize = bufferSize;
                        jdbcType = java.sql.Types.VARCHAR;
                        break;
                    default:
                        break;
                }
            }
        }
        if (tds7ResultToken) {
            realName = in.readUnicodeString(in.readUnsignedByte());
            name = realName;
            if ((collation != 0) && (tdsVersion.major >= 8)) {
                this.collation = collation;
                setCharset(connection);
                collation = this.collation;
            }
        }
        this.bufferSize = bufferSize;
        this.bytesRead = bytesRead;
        this.catalog = catalog;
        this.collation = collation;
        this.displaySize = displaySize;
        this.isCaseSensitive = caseSensitive;
        this.isHidden = hidden;
        this.isIdentity = identity;
        this.isKey = key;
        this.isWriteable = writeable;
        this.jdbcType = jdbcType;
        this.name = name;
        this.nullable = nullable;
        this.precision = precision;
        this.realName = realName;
        this.scale = scale;
        this.schema = schema;
        this.sqlType = sqlType;
        this.tableName = tableName;
        this.tdsType = tdsType;
        this.userType = userType;
    }

    /**
   * Only used by TdsInputStream readTdsVARIANT.
   */
    ColInfo(final int tdsType) {
        super();
        this.bufferSize = 0;
        this.bytesRead = 0;
        this.displaySize = 0;
        this.isCaseSensitive = false;
        this.isIdentity = false;
        this.isWriteable = false;
        this.jdbcType = 0;
        this.nullable = 0;
        this.precision = 0;
        this.scale = 0;
        this.sqlType = null;
        this.tdsType = tdsType;
        this.userType = 0;
    }

    final boolean isCollation() {
        return TdsSQLType.isCollation(this.tdsType);
    }

    /**
   * Retrieve the currency status of the column.
   *
   * @param ci The column meta data.
   * @return <code>boolean</code> true if the column is a currency type.
   */
    final boolean isCurrency() {
        return (this.tdsType == TdsTypes.SYBMONEY) || (this.tdsType == TdsTypes.SYBMONEY4) || (this.tdsType == TdsTypes.SYBMONEYN);
    }

    /**
   * Retrieve the searchable status of the column.
   *
   * @param ci the column meta data
   * @return <code>true</code> if the column is not a text or image type.
   */
    final boolean isSearchable() {
        return TdsSQLType.get(this.tdsType).size != -4;
    }

    final boolean isSigned() {
        return ((this.tdsType == TdsTypes.SYBINTN) && (this.bufferSize == 1)) ? TdsSQLType.INT1.signed : TdsSQLType.get(this.tdsType).signed;
    }

    final boolean isTextOrImage() {
        return (this.sqlType == "text") || (this.sqlType == "ntext") || (this.sqlType == "image");
    }

    final boolean isUnicode() {
        switch(this.tdsType) {
            case TdsTypes.SYBNVARCHAR:
            case TdsTypes.SYBNTEXT:
            case TdsTypes.XSYBNCHAR:
            case TdsTypes.XSYBNVARCHAR:
            case TdsTypes.XSYBCHAR:
            case TdsTypes.SYBVARIANT:
                return true;
            default:
                return false;
        }
    }

    /**
   * Set the <code>charsetInfo</code> field of <code>ci</code> according to
   * the value of its <code>collation</code> field.
   * <p>
   * The <code>Connection</code> is used to find out whether a specific
   * charset was requested. In this case, the column charset will be ignored.
   *
   * @param ci         the <code>ColInfo</code> instance to update
   * @param connection a <code>Connection</code> instance to check whether it
   *                   has a fixed charset or not
   * @throws SQLException if a <code>CharsetInfo</code> is not found for this
   *                      particular column collation
   */
    final void setCharset(final TdsConnection connection) throws SQLException {
        if (connection.isCharsetSpecified()) {
            this.charsetInfo = connection.getCharsetInfo();
        } else if (this.collation != 0) {
            if (this.collation == connection.getCollation()) this.charsetInfo = connection.getCharsetInfo(); else this.charsetInfo = CharsetInfo.forCollation(this.collation);
        }
    }
}
