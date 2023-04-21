package net.mikaboshi.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * RusultSet を単純な方法でフォーマットする。
 * </p><p>
 * <ul>
 * 	<li>nullの場合は空文字を出力する</li>
 * 	<li>それ以外の場合は、ResultSet#getObject()#toString() の値を出力する</li>
 * </ul>
 * </p><p>
 * 例えば、BLOB型のtoString()でバイナリダンプを出力するようなJDBCの実装があった場合、
 * 大量の文字列が出力される可能性があることに注意すること。
 * </p>
 * 
 * @author Takuma Umezawa
 *
 */
public class SimpleFormatter implements ResultDataFormatter {

    public String format(ResultSet rs, int columnIndex, int columnType) throws SQLException {
        Object data = rs.getObject(columnIndex);
        if (data == null) {
            return getNullString();
        }
        return rs.getObject(columnIndex).toString();
    }

    private String nullString = StringUtils.EMPTY;

    public void setNullString(String nullString) {
        this.nullString = nullString;
    }

    protected String getNullString() {
        return this.nullString;
    }
}
