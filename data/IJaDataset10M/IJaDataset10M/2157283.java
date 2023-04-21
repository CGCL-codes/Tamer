package com.zubarev.htmltable.util;

import java.util.StringTokenizer;
import java.util.Vector;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.zubarev.htmltable.vo.Column;
import com.zubarev.htmltable.vo.UITable;

public class CookiesUtils {

    private static Log log = LogFactory.getLog(CookiesUtils.class);

    public static Vector getTableColumns(UITable table, HttpServletRequest request) {
        Vector result = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie.getName().equals(table.getId())) {
                    result = new Vector();
                    String value = cookie.getValue();
                    StringTokenizer stColumns = new StringTokenizer(value, "=");
                    while (stColumns.hasMoreTokens()) {
                        String columnFC = stColumns.nextToken();
                        StringTokenizer stColumn = new StringTokenizer(columnFC, "-");
                        String[] columnValues = new String[stColumn.countTokens()];
                        int j = 0;
                        while (stColumn.hasMoreTokens()) {
                            String columnValue = stColumn.nextToken();
                            columnValues[j++] = columnValue;
                        }
                        Column column = ColumnUtils.findColumn(table.getColumns().getColumns(), columnValues[0]);
                        if (column != null) {
                            Column ccolumn = new Column(column);
                            ccolumn.setVisible(new Boolean(columnValues[1]).booleanValue());
                            result.add(ccolumn);
                        }
                    }
                    break;
                }
            }
        }
        return result;
    }

    public static void setTableColumns(String tableID, Vector columns, HttpServletResponse response) {
        log.debug("setTableColumns");
        log.debug("tableID = " + tableID);
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < columns.size(); i++) {
            Column column = (Column) columns.get(i);
            sb.append(column.getId() + "-" + column.isVisible() + "=");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        Cookie cookie = new Cookie(tableID, sb.toString());
        response.addCookie(cookie);
    }
}
