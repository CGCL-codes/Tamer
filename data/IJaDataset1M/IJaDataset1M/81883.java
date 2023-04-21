package com.aimluck.eip.orm.access.jdbc;

import java.sql.Connection;
import org.apache.cayenne.access.jdbc.ColumnDescriptor;
import org.apache.cayenne.access.jdbc.SelectAction;
import org.apache.cayenne.access.trans.SelectTranslator;
import org.apache.cayenne.dba.DbAdapter;
import org.apache.cayenne.map.EntityResolver;
import com.aimluck.eip.orm.access.trans.CustomSelectTranslator;
import com.aimluck.eip.orm.query.AbstractCustomQuery;

/**
 * 
 */
public class CustomSelectAction extends SelectAction {

    private final String customScript;

    private final ColumnDescriptor[] columns;

    private final String[] columnNames;

    private final int offset;

    private final int limit;

    private final boolean isDistinct;

    public CustomSelectAction(AbstractCustomQuery arg0, DbAdapter arg1, EntityResolver arg2, String customScript, ColumnDescriptor[] columns, String[] columnNames, int limit, int offset, boolean isDistinct) {
        super(arg0, arg1, arg2);
        this.customScript = customScript;
        this.columns = columns;
        this.columnNames = columnNames;
        this.offset = offset;
        this.limit = limit;
        this.isDistinct = isDistinct;
    }

    @Override
    protected SelectTranslator createTranslator(Connection connection) {
        CustomSelectTranslator translator = new CustomSelectTranslator();
        translator.setQuery(query);
        translator.setAdapter(adapter);
        translator.setEntityResolver(getEntityResolver());
        translator.setConnection(connection);
        translator.setCustomScript(customScript);
        translator.setCustomColumns(columns);
        translator.setCustomColumnNames(columnNames);
        translator.setFetchOffset(offset);
        translator.setFetchLimit(limit);
        translator.setIsDistinct(isDistinct);
        return translator;
    }
}
