package org.shestkoff.common.helper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Project: Smart Advertising
 * Created by: Yakim
 * Date: 06.11.2008
 * Time: 11:35:04
 * <p/>
 * Copyright (c) 1999-2008 Magenta Corporation Ltd. All Rights Reserved.
 * Magenta Technology proprietary and confidential.
 * Use is subject to license terms.
 *
 * @author Alexander Yakimov
 */
public class DbHelper {

    private static final String CHARACTER_ENCODING_UTF_8 = "?characterEncoding=UTF-8";

    public static final class ConnectionInfo {

        public final Connection connection;

        public final String targetDbName;

        public ConnectionInfo(Connection connection, String targetDbName) {
            this.connection = connection;
            this.targetDbName = targetDbName;
        }
    }

    public static String addUTF8(String url) {
        int index = url.lastIndexOf(CHARACTER_ENCODING_UTF_8);
        if (index == -1) {
            url += CHARACTER_ENCODING_UTF_8;
        }
        return url;
    }

    public static String cutParameters(String url) {
        int index = url.lastIndexOf("?");
        if (index != -1) {
            return url.substring(0, index);
        }
        return url;
    }

    public static String getDbName(String url) {
        String withPars = url.substring(url.lastIndexOf("/") + 1);
        int index = withPars.lastIndexOf("?");
        if (index != -1) {
            return withPars.substring(0, index);
        }
        return withPars;
    }

    public static int execute(Connection con, String sqlStatement) throws SQLException {
        final Statement st = con.createStatement();
        final int res;
        try {
            res = st.executeUpdate(sqlStatement);
        } finally {
            st.close();
        }
        return res;
    }
}
