package com.emeraldjb.generator.ddl;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import com.emeraldjb.base.EmeraldjbBean;
import com.emeraldjb.base.EmeraldjbException;
import com.emeraldjb.base.Entity;
import com.emeraldjb.base.FkColumn;
import com.emeraldjb.base.ForeignKey;
import com.emeraldjb.base.Index;
import com.emeraldjb.base.IndexColumn;
import com.emeraldjb.base.Member;
import com.emeraldjb.base.PkColumn;
import com.emeraldjb.base.PrimaryKey;
import com.emeraldjb.generator.GeneratorConst;

/**
 * <p>
 * MysqlDdlGenerator converts an Emeraldjb model into a MySql
 * DDL file.
 * </p><p>
 * It also maps the Emeraldjb types onto the JDBC types for use within
 * the selected EjbGenerator.
 * </p><p> 
 * Update Feb 28, 2004
 * Added the check for the default vaule.  This was added so TYPE=1 is not added to the ddl. ht
 * </p><p>
 * <pre>
 * TODO mysql supports a 60 character comment per table
 * TODO mysql supports  #, /* *&#47; , and -- ddl comment styles
 * </pre>
 * </p>
 * <p>
 * Copyright (c) 2003, 2004 by Emeraldjb LLC<br>
 * All Rights Reserved.
 * </p>
 */
public class MysqlDdlGenerator extends CommonDdlGenerator {

    private String tableName_;

    public String getDatabaseName() {
        return "Mysql";
    }

    /**
   * The create table syntac is defined here:
     * http://www.mysql.com/doc/en/CREATE_TABLE.html
     *
     * The FROM_SEQ column is moved to the front of the definition (per comments on web page)
     *
   * @param eb
   */
    public void createTable(EmeraldjbBean eb, StringBuffer analyse_tables, StringBuffer create_tables, StringBuffer drop_tables, StringBuffer create_indexes, StringBuffer drop_indexes, StringBuffer create_constraints, StringBuffer drop_constraints, StringBuffer create_sequence, StringBuffer drop_sequence, StringBuffer create_synonyms, StringBuffer drop_synonyms, StringBuffer grants, StringBuffer revokes) throws EmeraldjbException {
        Entity entity = (Entity) eb;
        StringBuffer tableDef = new StringBuffer();
        validateEntity(entity);
        tableName_ = entity.getName().toUpperCase();
        String createTable = "CREATE TABLE " + tableName_;
        analyse_tables.append("\nANALYZE TABLE " + tableName_ + ";");
        String dropTable = "\nDROP TABLE " + tableName_ + ";";
        tableDef.append(indent(1, createTable));
        tableDef.append(indent(1, "(\n"));
        Iterator it = entity.getMembers().iterator();
        while (it.hasNext()) {
            Member member = (Member) it.next();
            String createColumn = createColumn(member);
            if (it.hasNext()) {
                createColumn += ",";
            }
            createColumn += "\n";
            tableDef.append(indent(2, createColumn));
        }
        String table_type = entity.getPatternValue(GeneratorConst.PATTERN_MYSQL_TABLE_TYPE, "1");
        if (table_type != null && !table_type.equals("1")) {
            tableDef.append(indent(1, ") TYPE=" + table_type + ";\n"));
        } else {
            tableDef.append(indent(1, ");\n"));
        }
        create_tables.append(tableDef + "\n");
        drop_tables.append(dropTable + "\n");
    }

    /**
   * This accounts for the supported column attributes:
   * NULL_ALLOWED
   * FROM_SEQ
     * AUTO_INCREMENT (from_seq)
     * PRIMARY KEY
   *
   * if this member is a primary key, then that is also reflected in the column.
   * Note there is a pattern to override the FROM_SEQ to indirect through a pkey
   * getter function
   *
   * @param eb
   * @return String representation of column in MySql
   */
    public String createColumn(EmeraldjbBean eb) throws EmeraldjbException {
        Member member = (Member) eb;
        StringBuffer columnDef = new StringBuffer();
        columnDef.append(padTo(32, member.getName().toUpperCase()));
        columnDef.append(findDdlType(member));
        if (!member.isNullAllowed()) {
            columnDef.append(" NOT NULL");
        }
        if (!"".equals(member.getDefaultValue())) {
            columnDef.append(" DEFAULT " + member.getDefaultValue());
        }
        String func = member.getPatternValue(GeneratorConst.PATTERN_USE_SEQUENCE_FUNC);
        if (func == null && !"".equals(member.getFromSeq())) {
            columnDef.append(" AUTO_INCREMENT");
        }
        PrimaryKey pk = ((Entity) member.getParent()).getPrimaryKey();
        if (pk.getCols().size() == 1 && member.isPrimaryKey()) {
            columnDef.append(" PRIMARY KEY");
        }
        return columnDef.toString();
    }

    /**
     * http://www.mysql.com/doc/en/CREATE_INDEX.html
     *
     * CREATE [UNIQUE|FULLTEXT] INDEX index_name
     * ON tbl_name (col_name[(length)],... )
     * @param eb
     * @throws EmeraldjbException
     */
    public void createIndexes(EmeraldjbBean eb, StringBuffer create_indexes, StringBuffer drop_indexes) throws EmeraldjbException {
        Entity entity = (Entity) eb;
        validateEntity(entity);
        StringBuffer sb = new StringBuffer();
        StringBuffer drop_sb = new StringBuffer();
        Iterator it = entity.getIndexes().iterator();
        while (it.hasNext()) {
            Index index = (Index) it.next();
            drop_sb.append("DROP INDEX " + index.getName() + "\n");
            String indexStr = "CREATE";
            if (index.isUnique()) {
                indexStr += " UNIQUE";
            }
            indexStr += " INDEX " + index.getName() + " ON ";
            indexStr += "" + entity.getName();
            indexStr += "(";
            Iterator iit = index.getCols().iterator();
            int i = 0;
            while (iit.hasNext()) {
                IndexColumn indexCol = (IndexColumn) iit.next();
                indexStr += indexCol.getColName();
                if (indexCol.getLength() != IndexColumn.UNSPECIFIED_LENGTH) {
                    indexStr += "(" + indexCol.getLength() + ")";
                }
                if (i < (index.getCols().size() - 1)) indexStr += ", ";
                i++;
            }
            indexStr += ");\n";
            sb.append(indent(1, indexStr));
        }
        if (sb.length() > 0) {
            create_indexes.append(sb.toString() + "\n");
            drop_indexes.append(drop_sb + "\n");
        }
    }

    /**
     * http://www.mysql.com/doc/en/ANSI_diff_Foreign_Keys.html
     * http://www.mysql.com/doc/en/ALTER_TABLE.html
     *
     * ADD [CONSTRAINT symbol] FOREIGN KEY [index_name] (index_col_name,...)
     *      [reference_definition]
     *
     * In MySQL Server 3.23.44 and up, InnoDB tables support checking
     * of foreign key constraints, including CASCADE, ON DELETE, and ON UPDATE.
     * See section 7.5 InnoDB Tables.
     * For other table types, MySQL Server only parses the FOREIGN KEY syntax
     * in CREATE TABLE commands, but does not use/store this info.
     * Note that foreign keys in SQL are not used to join tables,
     * but are used mostly for checking referential integrity
     * (foreign key constraints).      *
     * @param eb
     * @throws EmeraldjbException
     */
    public void createForeignKeys(EmeraldjbBean eb, StringBuffer create_constraints, StringBuffer drop_constraints) throws EmeraldjbException {
        Entity entity = (Entity) eb;
        validateEntity(entity);
        StringBuffer sb = new StringBuffer();
        Iterator it = entity.getForeignKeys().iterator();
        while (it.hasNext()) {
            ForeignKey fk = (ForeignKey) it.next();
            drop_constraints.append("DROP CONSTRAINT " + fk.getName() + ";");
            String fkStr = "ALTER TABLE " + entity.getName().toUpperCase() + " ADD CONSTRAINT " + fk.getName() + " FOREIGN KEY ";
            fkStr += "( ";
            Iterator fit = fk.getCols().iterator();
            int i = 0;
            while (fit.hasNext()) {
                FkColumn fkCol = (FkColumn) fit.next();
                fkStr += fkCol.getColName();
                if (i < (fk.getCols().size() - 1)) fkStr += ", ";
                i++;
            }
            fkStr += " )";
            fkStr += " REFERENCES ";
            fkStr += fk.getExternalTable();
            if (fk.getCols().size() > 0) {
                fkStr += " ( ";
                fit = fk.getCols().iterator();
                i = 0;
                while (fit.hasNext()) {
                    FkColumn fkCol = (FkColumn) fit.next();
                    fkStr += fkCol.getExternalColumn();
                    if (fkCol.getLength() != FkColumn.UNSPECIFIED_LENGTH) {
                        fkStr += "(" + fkCol.getLength() + ")";
                    }
                    if (i < (fk.getCols().size() - 1)) fkStr += ", ";
                    i++;
                }
                fkStr += " )";
            }
            sb.append(indent(1, fkStr));
        }
        if (sb.length() > 0) {
            create_constraints.append(sb.toString() + ";\n");
        }
    }

    /**
     *
     * @param eb
     * @throws EmeraldjbException if the primary key length is greater than 500 characters
     */
    public void createPrimaryKey(EmeraldjbBean eb, StringBuffer create_tables, StringBuffer drop_tables) throws EmeraldjbException {
        Entity entity = (Entity) eb;
        validateEntity(entity);
        StringBuffer sb = new StringBuffer();
        PrimaryKey pk = entity.getPrimaryKey();
        if (pk.getCols().size() > 1) {
            String pkStr = "ALTER TABLE " + entity.getName().toUpperCase() + " ADD CONSTRAINT PRIMARY KEY ";
            pkStr += "( ";
            Iterator it = pk.getCols().iterator();
            int i = 0;
            while (it.hasNext()) {
                PkColumn pkCol = (PkColumn) it.next();
                pkStr += pkCol.getColName();
                if (pkCol.getLength() != PkColumn.UNSPECIFIED_LENGTH) {
                    pkStr += "(" + pkCol.getLength() + ")";
                }
                if (i < (pk.getCols().size() - 1)) pkStr += ", ";
                i++;
            }
            pkStr += " );\n";
            sb.append(indent(1, pkStr));
        }
        if (sb.length() > 0) {
            create_tables.append(sb + "\n");
            drop_tables.append("\nALTER TABLE " + tableName_ + " drop PRIMARY KEY;");
        }
    }

    /**
     * MySql types (supported types are in <b>bold</b>):
     *           TINYINT[(length)] [UNSIGNED] [ZEROFILL]
     *     or    <b>SMALLINT</b>[(length)] [UNSIGNED] [ZEROFILL]
     *     or    MEDIUMINT[(length)] [UNSIGNED] [ZEROFILL]
     *     or    INT[(length)] [UNSIGNED] [ZEROFILL]
     *     or    <b>INTEGER</b>[(length)] [UNSIGNED] [ZEROFILL]
     *     or    BIGINT[(length)] [UNSIGNED] [ZEROFILL]
     *     or    REAL[(length,decimals)] [UNSIGNED] [ZEROFILL]
     *     or    <b>DOUBLE</b>[(length,decimals)] [UNSIGNED] [ZEROFILL]
     *     or    <b>FLOAT</b>[(length,decimals)] [UNSIGNED] [ZEROFILL]
     *     or    DECIMAL(length,decimals) [UNSIGNED] [ZEROFILL]
     *     or    NUMERIC(length,decimals) [UNSIGNED] [ZEROFILL]
     *     or    CHAR(length) [BINARY]
     *     or    <b>VARCHAR(length)</b> [BINARY]
     *     or    <b>DATE</b>
     *     or    <b>TIME</b>
     *     or    <b>TIMESTAMP</b>
     *     or    DATETIME
     *     or    TINYBLOB
     *     or    <b>BLOB</b>
     *     or    MEDIUMBLOB
     *     or    LONGBLOB
     *     or    TINYTEXT
     *     or    TEXT
     *     or    MEDIUMTEXT
     *     or    LONGTEXT
     *     or    ENUM(value1,value2,value3,...)
     *     or    SET(value1,value2,value3,...)
     * @param member
     * @return
     * @throws EmeraldjbException if the Emeraldjb member type is not supported.
     */
    public String findDdlType(Member member) throws EmeraldjbException {
        String memberType = member.getType();
        if (Member.TY_INT.equalsIgnoreCase(memberType)) {
            return "INTEGER";
        } else if (Member.TY_SHORT.equalsIgnoreCase(memberType)) {
            return "INTEGER";
        } else if (Member.TY_BOOLEAN.equalsIgnoreCase(memberType)) {
            return "SMALLINT";
        } else if (Member.TY_STRING.equalsIgnoreCase(memberType)) {
            int colLen = member.getColLen();
            if (colLen < 255) return "VARCHAR(" + colLen + ")";
            return "BLOB";
        } else if (Member.TY_DATE.equalsIgnoreCase(memberType)) {
            return "DATE";
        } else if (Member.TY_LONG.equalsIgnoreCase(memberType)) {
            return "BIGINT";
        } else if (Member.TY_TIME.equalsIgnoreCase(memberType)) {
            return "TIME";
        } else if (Member.TY_TIMESTAMP.equalsIgnoreCase(memberType)) {
            return "TIMESTAMP";
        } else if (Member.TY_FLOAT.equalsIgnoreCase(memberType)) {
            return "FLOAT";
        } else if (Member.TY_DOUBLE.equalsIgnoreCase(memberType)) {
            return "DOUBLE";
        } else if (Member.TY_CLOB.equalsIgnoreCase(memberType)) {
            return "BLOB";
        }
        throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E_NAME_NOT_FOUND, "Member type not found in DDL type map: " + memberType);
    }

    public String findJdbcType(String memberType) throws EmeraldjbException {
        if (Member.TY_INT.equalsIgnoreCase(memberType)) {
            return "int";
        } else if (Member.TY_SHORT.equalsIgnoreCase(memberType)) {
            return "int";
        } else if (Member.TY_BOOLEAN.equalsIgnoreCase(memberType)) {
            return "boolean";
        } else if (Member.TY_STRING.equalsIgnoreCase(memberType)) {
            return "String";
        } else if (Member.TY_DATE.equalsIgnoreCase(memberType)) {
            return "java.sql.Date";
        } else if (Member.TY_LONG.equalsIgnoreCase(memberType)) {
            return "long";
        } else if (Member.TY_TIME.equalsIgnoreCase(memberType)) {
            return "java.sql.Time";
        } else if (Member.TY_TIMESTAMP.equalsIgnoreCase(memberType)) {
            return "java.sql.Timestamp";
        } else if (Member.TY_FLOAT.equalsIgnoreCase(memberType)) {
            return "float";
        } else if (Member.TY_DOUBLE.equalsIgnoreCase(memberType)) {
            return "double";
        } else if (Member.TY_CLOB.equalsIgnoreCase(memberType)) {
            return "String";
        }
        throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E_NAME_NOT_FOUND, "Member type not found in DDL type map: " + memberType);
    }

    public String findJdbcTypeConstant(String memberType, int colLen) throws EmeraldjbException {
        if (Member.TY_INT.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.INTEGER";
        } else if (Member.TY_SHORT.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.INTEGER";
        } else if (Member.TY_BOOLEAN.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.SMALLINT";
        } else if (Member.TY_STRING.equalsIgnoreCase(memberType)) {
            if (colLen < 255) return "java.sql.Types.VARCHAR";
            return "java.sql.Types.BLOB";
        } else if (Member.TY_DATE.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.DATE";
        } else if (Member.TY_LONG.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.BIGINT";
        } else if (Member.TY_TIME.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.TIME";
        } else if (Member.TY_TIMESTAMP.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.TIMESTAMP";
        } else if (Member.TY_FLOAT.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.FLOAT";
        } else if (Member.TY_DOUBLE.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.DOUBLE";
        } else if (Member.TY_CLOB.equalsIgnoreCase(memberType)) {
            return "java.sql.Types.BLOB";
        }
        throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E_NAME_NOT_FOUND, "Member type not found in jdbc type constants map: " + memberType);
    }

    public String findJdbcSetMethod(String memberType, int colLen) throws EmeraldjbException {
        if (Member.TY_INT.equalsIgnoreCase(memberType)) {
            return "setInt";
        } else if (Member.TY_SHORT.equalsIgnoreCase(memberType)) {
            return "setShort";
        } else if (Member.TY_BOOLEAN.equalsIgnoreCase(memberType)) {
            return "setBoolean";
        } else if (Member.TY_STRING.equalsIgnoreCase(memberType)) {
            return "setString";
        } else if (Member.TY_DATE.equalsIgnoreCase(memberType)) {
            return "setDate";
        } else if (Member.TY_LONG.equalsIgnoreCase(memberType)) {
            return "setLong";
        } else if (Member.TY_TIME.equalsIgnoreCase(memberType)) {
            return "setTime";
        } else if (Member.TY_TIMESTAMP.equalsIgnoreCase(memberType)) {
            return "setTimestamp";
        } else if (Member.TY_FLOAT.equalsIgnoreCase(memberType)) {
            return "setFloat";
        } else if (Member.TY_DOUBLE.equalsIgnoreCase(memberType)) {
            return "setDouble";
        } else if (Member.TY_CLOB.equalsIgnoreCase(memberType)) {
            return "setBlob";
        }
        throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E_NAME_NOT_FOUND, "Member type not found in jdbc set method map: " + memberType);
    }

    public String findJdbcGetMethod(String memberType, int colLen) throws EmeraldjbException {
        if (Member.TY_INT.equalsIgnoreCase(memberType)) {
            return "getInt";
        } else if (Member.TY_SHORT.equalsIgnoreCase(memberType)) {
            return "getShort";
        } else if (Member.TY_BOOLEAN.equalsIgnoreCase(memberType)) {
            return "getBoolean";
        } else if (Member.TY_STRING.equalsIgnoreCase(memberType)) {
            return "getString";
        } else if (Member.TY_DATE.equalsIgnoreCase(memberType)) {
            return "getDate";
        } else if (Member.TY_LONG.equalsIgnoreCase(memberType)) {
            return "getLong";
        } else if (Member.TY_TIME.equalsIgnoreCase(memberType)) {
            return "getTime";
        } else if (Member.TY_TIMESTAMP.equalsIgnoreCase(memberType)) {
            return "getTimestamp";
        } else if (Member.TY_FLOAT.equalsIgnoreCase(memberType)) {
            return "getFloat";
        } else if (Member.TY_DOUBLE.equalsIgnoreCase(memberType)) {
            return "getDouble";
        } else if (Member.TY_CLOB.equalsIgnoreCase(memberType)) {
            return "getBlob";
        }
        throw new EmeraldjbException(EmeraldjbException.EMERALDJB_E_NAME_NOT_FOUND, "Member type not found in jdbc get method map: " + memberType);
    }

    /**
   * MySQL:
   * http://www.mysql.com/doc/en/INSERT.html
   *
   * INSERT [LOW_PRIORITY | DELAYED] [IGNORE]
   *     [INTO] tbl_name
   *     SET col_name=(expression | DEFAULT), ...
   *     [ ON DUPLICATE KEY UPDATE col_name=expression, ... ]
   * @return The String query used to insert the entity,
   * by default a parameterized sql statement.
   */
    protected String getInsertSql(Entity entity) throws EmeraldjbException {
        StringBuffer sb = new StringBuffer();
        sb.append("INSERT INTO " + entity.getName().toUpperCase() + "\n");
        sb.append("SET \n");
        Member pkMember = null;
        if (entity.getPrimaryKey().isFromSeq()) {
            PkColumn pkCol = (PkColumn) entity.getPrimaryKey().getCols().iterator().next();
            pkMember = pkCol.getKeyMember();
        }
        Iterator it = entity.getMembers().iterator();
        int i = 0;
        while (it.hasNext()) {
            Member member = (Member) it.next();
            if (pkMember != null && member.getName().equalsIgnoreCase(pkMember.getName())) {
                continue;
            }
            if (i > 0) sb.append(",");
            sb.append(member.getName().toUpperCase() + "= ?\n");
            i++;
        }
        return sb.toString();
    }

    protected List getInsertSqlParams(Entity entity) throws EmeraldjbException {
        Member pkMember = null;
        if (entity.getPrimaryKey().isFromSeq()) {
            PkColumn pkCol = (PkColumn) entity.getPrimaryKey().getCols().iterator().next();
            pkMember = pkCol.getKeyMember();
        }
        List paramList = new Vector();
        Iterator it = entity.getMembers().iterator();
        int i = 1;
        while (it.hasNext()) {
            Member member = (Member) it.next();
            if (pkMember != null && member.getName().equalsIgnoreCase(pkMember.getName())) {
                continue;
            }
            SqlParam sqlParam = new SqlParam();
            sqlParam.setEmeraldjbBean(member);
            sqlParam.setSqlColumn(member.getName());
            sqlParam.setJdbcType(findJdbcType(member));
            sqlParam.setJdbcTypeConstant(findJdbcTypeConstant(member));
            sqlParam.setJdbcSetMethod(findJdbcSetMethod(member));
            sqlParam.setPositionNumber(i);
            paramList.add(sqlParam);
            i++;
        }
        return paramList;
    }

    public boolean supportsPreInsertSequences() {
        return false;
    }

    public String getSequenceSelectSql(String sequence_name) {
        return "Not supported";
    }

    public String getLastIdSelectSql(String sequence_name) {
        return "select LAST_INSERT_ID()";
    }

    /**
   * Will return a string holding source XXX lines suitable for a script that
   * calls other scipts.  Each item in the file_names list is a full file name
   * i.e. including path.
   * @param file_names The list of file names
   * @return The script
   */
    public String createMetaScript(Vector file_names) {
        StringBuffer ret = new StringBuffer();
        Enumeration en = file_names.elements();
        while (en.hasMoreElements()) {
            String nm = (String) en.nextElement();
            ret.append("source " + nm + ";\n");
        }
        return ret.toString();
    }
}
