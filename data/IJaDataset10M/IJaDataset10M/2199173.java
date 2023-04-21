package genorm;

public class DefaultFormat implements Format {

    static String toCamelCase(String str, boolean upcaseFirstChar) {
        StringBuffer sb = new StringBuffer();
        char[] cArr = str.toLowerCase().toCharArray();
        if (upcaseFirstChar) {
            cArr[0] = Character.toUpperCase(cArr[0]);
        }
        boolean upcaseNext = false;
        for (int I = 0; I < cArr.length; I++) {
            if (cArr[I] == '_') {
                upcaseNext = true;
            } else {
                if (upcaseNext) {
                    sb.append(Character.toUpperCase(cArr[I]));
                    upcaseNext = false;
                } else sb.append(cArr[I]);
            }
        }
        return (sb.toString());
    }

    public String formatClassName(String tableName) {
        return (toCamelCase(tableName, true));
    }

    public String formatStaticName(String columnName) {
        if (Character.isDigit(columnName.toCharArray()[0])) return ("A_" + columnName.toUpperCase()); else return (columnName.toUpperCase());
    }

    public String formatMethodName(String columnName) {
        return (toCamelCase(columnName, true));
    }

    public String formatForeignKeyMethod(ForeignKeySet fks) {
        String name = "";
        if (fks.getKeys().size() > 1) name = fks.getTableName(); else name = fks.getKeys().get(0).getName();
        if (name.endsWith("_id")) name = name.substring(0, (name.length() - 3));
        name += "_ref";
        return (formatMethodName(name));
    }

    public String formatParameterName(String columnName) {
        return (toCamelCase(columnName, false));
    }
}
