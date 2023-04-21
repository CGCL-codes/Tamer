package cn.saker.util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SqlUtil {

    public static Map getRetrieveTables(String query, boolean useFirstWhereInSQL) {
        String where = null;
        Map map = null;
        if (query != null && query.trim().length() > 0) {
            String lowerCaseSQL = query.toLowerCase();
            int beginIdx = lowerCaseSQL.indexOf("from ") + 5;
            int endIdx = lowerCaseSQL.indexOf(" where ");
            if (useFirstWhereInSQL == false) endIdx = lowerCaseSQL.lastIndexOf(" where ");
            if (endIdx < 0) endIdx = lowerCaseSQL.length();
            where = query.substring(beginIdx, endIdx);
        }
        if (where != null) {
            String tableName = null;
            map = new HashMap();
            StringTokenizer st = new StringTokenizer(where, " ,\r\n", true);
            boolean expectTable = true;
            boolean expectAlias = false;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (token.trim().length() == 0) continue;
                if (token.equals(",") || token.equalsIgnoreCase("join")) {
                    expectTable = true;
                    expectAlias = false;
                    continue;
                }
                if (expectTable) {
                    tableName = token;
                    expectTable = false;
                    expectAlias = true;
                    map.put(tableName, tableName);
                    System.out.println(tableName + " -------------- " + tableName);
                    continue;
                }
                if (expectAlias) {
                    if (token.equalsIgnoreCase("as")) {
                        expectAlias = true;
                    } else if (token.equalsIgnoreCase("left") || token.equalsIgnoreCase("inner") || token.equalsIgnoreCase("right") || token.equalsIgnoreCase("on")) {
                        expectAlias = false;
                    } else {
                        map.put(tableName, token);
                        System.out.println(tableName + " -------------- " + token);
                        expectAlias = false;
                    }
                    continue;
                }
            }
        }
        return map;
    }
}
