package com.spaceprogram.db4o.sql.parser;

import com.spaceprogram.db4o.sql.query.SqlQuery;
import com.spaceprogram.db4o.sql.query.From;
import java.util.List;

/**
 * User: treeder
 * Date: Feb 4, 2007
 * Time: 4:37:57 PM
 */
public class FromBuilder implements Builder {

    final String keyword = "FROM";

    public String getKeyword() {
        return keyword;
    }

    public void build(SqlQuery sq, List<String> expr, List<String> quotedStrings) {
        From from = new From();
        for (int i = 0; i < expr.size(); i++) {
            String s = expr.get(i);
            if (s.equals(",")) {
                continue;
            }
            if (s.endsWith(",")) {
                from.addClass(SqlParser.stripQuotes(SqlParser.replaceQuotedValue(quotedStrings, s.substring(0, s.length() - 1))));
                continue;
            }
            if (i == expr.size() - 1) {
                from.addClass(SqlParser.stripQuotes(SqlParser.replaceQuotedValue(quotedStrings, s)));
                continue;
            }
            if (expr.size() > i + 1) {
                String s2 = expr.get(i + 1);
                if (s2.equals(",")) {
                    from.addClass(SqlParser.stripQuotes(SqlParser.replaceQuotedValue(quotedStrings, s)));
                } else {
                    if (s2.endsWith(",")) {
                        s2 = s2.substring(0, s2.length());
                    }
                    from.addClass(SqlParser.stripQuotes(SqlParser.replaceQuotedValue(quotedStrings, s)), s2);
                }
                i++;
            }
        }
        sq.setFrom(from);
    }
}
