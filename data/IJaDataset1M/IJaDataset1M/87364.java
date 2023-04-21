package org.jumpmind.symmetric.ddl.platform.db2;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashSet;
import java.util.Map;
import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.PatternCompiler;
import org.apache.oro.text.regex.PatternMatcher;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;
import org.jumpmind.symmetric.ddl.DdlUtilsException;
import org.jumpmind.symmetric.ddl.Platform;
import org.jumpmind.symmetric.ddl.model.Column;
import org.jumpmind.symmetric.ddl.model.Index;
import org.jumpmind.symmetric.ddl.model.Table;
import org.jumpmind.symmetric.ddl.model.TypeMap;
import org.jumpmind.symmetric.ddl.platform.DatabaseMetaDataWrapper;
import org.jumpmind.symmetric.ddl.platform.JdbcModelReader;

/**
 * Reads a database model from a Db2 UDB database.
 *
 * @version $Revision: $
 */
public class Db2ModelReader extends JdbcModelReader {

    /** Known system tables that Db2 creates (e.g. automatic maintenance). */
    private static final String[] KNOWN_SYSTEM_TABLES = { "STMG_DBSIZE_INFO", "HMON_ATM_INFO", "HMON_COLLECTION", "POLICY" };

    /** The regular expression pattern for the time values that Db2 returns. */
    private Pattern _db2TimePattern;

    /** The regular expression pattern for the timestamp values that Db2 returns. */
    private Pattern _db2TimestampPattern;

    /**
     * Creates a new model reader for Db2 databases.
     * 
     * @param platform The platform that this model reader belongs to
     */
    public Db2ModelReader(Platform platform) {
        super(platform);
        setDefaultCatalogPattern(null);
        setDefaultSchemaPattern(null);
        PatternCompiler compiler = new Perl5Compiler();
        try {
            _db2TimePattern = compiler.compile("'(\\d{2}).(\\d{2}).(\\d{2})'");
            _db2TimestampPattern = compiler.compile("'(\\d{4}\\-\\d{2}\\-\\d{2})\\-(\\d{2}).(\\d{2}).(\\d{2})(\\.\\d{1,8})?'");
        } catch (MalformedPatternException ex) {
            throw new DdlUtilsException(ex);
        }
    }

    /**
     * {@inheritDoc}
     */
    protected Table readTable(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        String tableName = (String) values.get("TABLE_NAME");
        for (int idx = 0; idx < KNOWN_SYSTEM_TABLES.length; idx++) {
            if (KNOWN_SYSTEM_TABLES[idx].equals(tableName)) {
                return null;
            }
        }
        Table table = super.readTable(metaData, values);
        if (table != null) {
            determineAutoIncrementFromResultSetMetaData(table, table.getColumns());
        }
        return table;
    }

    /**
	 * {@inheritDoc}
	 */
    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException {
        Column column = super.readColumn(metaData, values);
        if (column.getDefaultValue() != null) {
            if (column.getTypeCode() == Types.TIME) {
                PatternMatcher matcher = new Perl5Matcher();
                if (matcher.matches(column.getDefaultValue(), _db2TimePattern)) {
                    StringBuffer newDefault = new StringBuffer();
                    newDefault.append("'");
                    newDefault.append(matcher.getMatch().group(1));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(2));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(3));
                    newDefault.append("'");
                    column.setDefaultValue(newDefault.toString());
                }
            } else if (column.getTypeCode() == Types.TIMESTAMP) {
                PatternMatcher matcher = new Perl5Matcher();
                if (matcher.matches(column.getDefaultValue(), _db2TimestampPattern)) {
                    StringBuffer newDefault = new StringBuffer();
                    newDefault.append("'");
                    newDefault.append(matcher.getMatch().group(1));
                    newDefault.append(" ");
                    newDefault.append(matcher.getMatch().group(2));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(3));
                    newDefault.append(":");
                    newDefault.append(matcher.getMatch().group(4));
                    if ((matcher.getMatch().groups() > 4) && (matcher.getMatch().group(4) != null)) {
                        newDefault.append(matcher.getMatch().group(5));
                    }
                    newDefault.append("'");
                    column.setDefaultValue(newDefault.toString());
                }
            } else if (TypeMap.isTextType(column.getTypeCode())) {
                column.setDefaultValue(unescape(column.getDefaultValue(), "'", "''"));
            }
        }
        return column;
    }

    /**
     * {@inheritDoc}
     */
    protected boolean isInternalPrimaryKeyIndex(DatabaseMetaDataWrapper metaData, Table table, Index index) throws SQLException {
        if (index.getName().startsWith("SQL")) {
            try {
                Long.parseLong(index.getName().substring(3));
                return true;
            } catch (NumberFormatException ex) {
            }
            return false;
        } else {
            ResultSet pkData = null;
            HashSet pkNames = new HashSet();
            try {
                pkData = metaData.getPrimaryKeys(table.getName());
                while (pkData.next()) {
                    Map values = readColumns(pkData, getColumnsForPK());
                    pkNames.add(values.get("PK_NAME"));
                }
            } finally {
                if (pkData != null) {
                    pkData.close();
                }
            }
            return pkNames.contains(index.getName());
        }
    }
}
