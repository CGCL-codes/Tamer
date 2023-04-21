package schemacrawler.tools.analysis;

import java.util.ArrayList;
import java.util.List;
import schemacrawler.schema.Column;
import schemacrawler.schema.Table;
import sf.util.ObjectToString;

public class LinterTableWithQuotedNames extends BaseLinter<Table> {

    public void lint(final Table table) {
        if (table != null) {
            final List<String> spacesInNamesList = findColumnsWithQuotedNames(table.getColumns());
            final String tableName = table.getName();
            if (isQuotedName(tableName)) {
                spacesInNamesList.add(0, tableName);
            }
            if (!spacesInNamesList.isEmpty()) {
                final String[] spacesInNames = spacesInNamesList.toArray(new String[spacesInNamesList.size()]);
                addLint(table, new Lint("spaces in names, or reserved words", spacesInNames) {

                    private static final long serialVersionUID = 4306137113072609086L;

                    @Override
                    public String getLintValueAsString() {
                        return ObjectToString.toString(spacesInNames);
                    }
                });
            }
        }
    }

    private List<String> findColumnsWithQuotedNames(final Column[] columns) {
        final List<String> columnsWithQuotedNames = new ArrayList<String>();
        for (final Column column : columns) {
            final String columnName = column.getName();
            if (isQuotedName(columnName)) {
                columnsWithQuotedNames.add(columnName);
            }
        }
        return columnsWithQuotedNames;
    }

    private boolean isQuotedName(final String name) {
        final int nameLength = name.length();
        final char[] namechars = new char[nameLength];
        name.getChars(0, nameLength, namechars, 0);
        return !Character.isJavaIdentifierStart(namechars[0]) && namechars[0] == namechars[nameLength - 1];
    }
}
