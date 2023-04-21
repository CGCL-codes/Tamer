package org.datanucleus.store.db4o.sql.parser;

import java.util.List;
import org.datanucleus.store.db4o.sql.query.Select;
import org.datanucleus.store.db4o.sql.query.SqlQuery;

/**
 * 
 */
public class SelectBuilder implements Builder {

    final String keyword = "SELECT";

    public String getKeyword() {
        return keyword;
    }

    public void build(SqlQuery sq, List<String> expr, List<String> quotedStrings) {
        Select select = new Select();
        List<String> values = SqlParser.separateCommas(expr, false);
        select.setFields(values);
        sq.setSelect(select);
    }
}
