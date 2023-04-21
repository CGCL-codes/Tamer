package org.makumba.db.makumba.sql;

import java.sql.SQLException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang.StringUtils;

/** the database adapter for MySQL Server */
public class MySqlDatabase extends org.makumba.db.makumba.sql.Database {

    public static final String regExpForeignKeyError = ".*\\(`(.*)`, CONSTRAINT `(.*)` FOREIGN KEY \\(`(.*)`\\) REFERENCES `(.*)` \\(`(.*)`\\)\\)";

    public static final Pattern patternForeignKeyError = Pattern.compile(regExpForeignKeyError);

    public MySqlDatabase(Properties p) {
        super(p);
    }

    @Override
    public String parseReadableForeignKeyErrorMessage(SQLException se) {
        String msg = se.getMessage();
        Matcher matcher = patternForeignKeyError.matcher(msg.trim());
        if (matcher.matches()) {
            try {
                String group = matcher.group(1);
                String referingTableName = getMddName(group.substring(group.indexOf("/") + 1));
                String referedTableName = getMddName(matcher.group(4));
                return "Trying to delete an entry from " + referedTableName + ", while an entry " + referingTableName + " still refers to it. Try to invert the order of deletion.";
            } catch (Exception e) {
                e.printStackTrace();
                return se.getMessage();
            }
        } else {
            return se.getMessage();
        }
    }

    public static String getMddName(String referingTableName) {
        return StringUtils.removeEnd(referingTableName, "_").replaceAll("__", "->").replaceAll("_", ".");
    }
}
