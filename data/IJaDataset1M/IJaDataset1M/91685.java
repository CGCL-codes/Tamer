package uk.co.markfrimston.utils;

import java.sql.*;
import java.util.*;
import java.lang.reflect.*;
import java.util.regex.*;

public class DatabaseUtils {

    private static int[] NUMERIC_TYPES = { Types.BIGINT, Types.DECIMAL, Types.DOUBLE, Types.FLOAT, Types.INTEGER, Types.NUMERIC, Types.REAL, Types.SMALLINT, Types.TINYINT };

    private static int[] STRING_TYPES = { Types.CHAR, Types.CLOB, Types.LONGVARCHAR, Types.VARCHAR };

    static {
        Arrays.sort(NUMERIC_TYPES);
        Arrays.sort(STRING_TYPES);
    }

    public static boolean isNumericType(int sqlType) {
        return Arrays.binarySearch(NUMERIC_TYPES, sqlType) >= 0;
    }

    public static boolean isStringType(int sqlType) {
        return Arrays.binarySearch(STRING_TYPES, sqlType) >= 0;
    }

    public static <T> List<T> mapResults(ResultSet results, Class<T> clazz) throws ColumnMappingException, SQLException {
        ResultSetMetaData rsmd = results.getMetaData();
        return mapResults(results, new ColumnMapping<T>(clazz, rsmd.getColumnCount()));
    }

    public static <T> List<T> mapResults(ResultSet results, ColumnMapping<T> root, ColumnMapping<?>... children) throws ColumnMappingException, SQLException {
        ColumnMapping<?>[] mappings = new ColumnMapping[children.length + 1];
        mappings[0] = root;
        int mappingCols = root.cols;
        for (int i = 0; i < children.length; i++) {
            mappings[i + 1] = children[i];
            mappingCols += children[i].cols;
        }
        ResultSetMetaData rsmd = results.getMetaData();
        int numCols = rsmd.getColumnCount();
        if (numCols != mappingCols) {
            throw new ColumnMappingException("Number of columns in result set does not match number in mapping");
        }
        List<T> returnVal = new ArrayList<T>();
        String[][] lastRow = null;
        Bean[] currentObjects = new Bean[mappings.length];
        Bean[] newObjects = new Bean[mappings.length];
        boolean hadResults = false;
        while (results.next()) {
            if (!hadResults) {
                hadResults = true;
            }
            String[][] row = new String[mappings.length][];
            int colIndex = 0;
            for (int i = 0; i < mappings.length; i++) {
                row[i] = new String[numCols];
                for (int j = 0; j < mappings[i].cols; j++) {
                    row[i][j] = results.getString(colIndex + 1);
                    colIndex++;
                }
            }
            int colIndexStart = 0;
            boolean changed = false;
            for (int i = 0; i < mappings.length; i++) {
                if (!changed) {
                    if (lastRow == null) {
                        changed = true;
                    } else {
                        for (int j = 0; j < mappings[i].cols; j++) {
                            if ((row[i][j] == null) != (lastRow[i][j] == null) || (row[i][j] != null && !row[i][j].equals(lastRow[i][j]))) {
                                changed = true;
                            }
                        }
                    }
                }
                if (changed) {
                    if (currentObjects[i] != null) {
                        storeObject(currentObjects, i, returnVal);
                    }
                    boolean allNull = true;
                    for (int j = 0; j < mappings[i].cols; j++) {
                        if (row[i][j] != null) {
                            allNull = false;
                            break;
                        }
                    }
                    if (!allNull) {
                        Map<String, String> fields = new HashMap<String, String>();
                        for (int j = 0; j < mappings[i].cols; j++) {
                            int index = colIndexStart + j;
                            fields.put(rsmd.getColumnLabel(index + 1), row[i][j]);
                        }
                        newObjects[i] = makeBeanAndSetFields(mappings[i].clazz, fields);
                    } else {
                        newObjects[i] = null;
                    }
                }
                colIndexStart += mappings[i].cols;
            }
            lastRow = new String[mappings.length][];
            for (int i = 0; i < mappings.length; i++) {
                lastRow[i] = new String[mappings[i].cols];
                for (int j = 0; j < mappings[i].cols; j++) {
                    lastRow[i][j] = row[i][j];
                }
                currentObjects[i] = newObjects[i];
            }
        }
        if (hadResults) {
            for (int i = 0; i < mappings.length; i++) {
                if (currentObjects[i] != null) {
                    storeObject(currentObjects, i, returnVal);
                }
            }
        }
        return returnVal;
    }

    private static <T> void storeObject(Bean[] currentObjects, int mappingIndex, List<T> rootList) throws ColumnMappingException {
        if (mappingIndex == 0) {
            rootList.add((T) currentObjects[mappingIndex].getObject());
        } else {
            if (currentObjects[mappingIndex - 1] == null) {
                throw new ColumnMappingException("Cannot store child of null parent object");
            }
            currentObjects[mappingIndex - 1].add(currentObjects[mappingIndex].getObject());
        }
    }

    private static Bean makeBeanAndSetFields(Class<?> clazz, Map<String, String> fields) throws ColumnMappingException {
        Bean bean = null;
        try {
            bean = new Bean(clazz);
        } catch (IllegalArgumentException e) {
            throw new ColumnMappingException("Failed to create new " + clazz.getName());
        }
        for (Map.Entry<String, String> field : fields.entrySet()) {
            bean.setFromString(field.getKey(), field.getValue());
        }
        return bean;
    }

    public static PreparedStatement prepareNumberedStatement(Connection con, String query, Object... parameters) throws SQLException {
        List<Object> paramList = new ArrayList<Object>();
        Pattern p = Pattern.compile("\\?([0-9]+)?");
        Matcher m = p.matcher(query);
        StringBuffer newQuery = new StringBuffer();
        int nextNonNumbered = 0;
        while (m.find()) {
            if (m.group(1) != null) {
                int pNum = Integer.parseInt(m.group(1));
                paramList.add(parameters[pNum]);
                m.appendReplacement(newQuery, "?");
            } else {
                int pNum = nextNonNumbered;
                nextNonNumbered++;
                paramList.add(parameters[pNum]);
            }
        }
        m.appendTail(newQuery);
        return prepareStatement(con, newQuery.toString(), paramList.toArray(new Object[] {}));
    }

    public static PreparedStatement prepareStatement(Connection con, String query, Object... parameters) throws SQLException {
        List<Object> paramList = new ArrayList<Object>();
        Pattern p = Pattern.compile("\\?");
        Matcher m = p.matcher(query);
        StringBuffer newQuery = new StringBuffer();
        for (int i = 0; i < parameters.length; i++) {
            if ((parameters[i].getClass().isArray() || parameters[i] instanceof Collection) && Sequence.isSequenceable(parameters[i])) {
                Sequence<?> s = Sequence.make(parameters[i]);
                String[] qMarks = new String[s.size()];
                Arrays.fill(qMarks, "?");
                m.find();
                m.appendReplacement(newQuery, StringUtils.implode(qMarks));
                paramList.addAll(s);
            } else {
                m.find();
                paramList.add(parameters[i]);
            }
        }
        m.appendTail(newQuery);
        PreparedStatement stmt = con.prepareStatement(newQuery.toString());
        for (int i = 0; i < paramList.size(); i++) {
            stmt.setObject(i + 1, paramList.get(i));
        }
        return stmt;
    }

    public static Connection mysqlConnect(String host, int port, String database, String username, String password) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password);
    }

    public static String hardcodedTable(Object[][] data, String[] colNames) {
        boolean first = true;
        String[] rowReps = new String[data.length];
        for (int j = 0; j < data.length; j++) {
            Object[] row = data[j];
            String[] colReps = new String[row.length];
            for (int i = 0; i < row.length; i++) {
                Object col = row[i];
                if (ObjectUtils.isNumeric(col)) {
                    colReps[i] = String.valueOf(col);
                } else {
                    colReps[i] = "'" + StringUtils.sqEscape(String.valueOf(col)) + "'";
                }
                if (first) {
                    colReps[i] += " `" + colNames[i] + "`";
                }
            }
            rowReps[j] = "SELECT " + StringUtils.implode(colReps);
        }
        return StringUtils.implode(rowReps, " UNION ALL ");
    }

    public static void main(String[] args) {
        System.out.println(DatabaseUtils.hardcodedTable(new Object[][] { { "Jeff", 23, 35.7 }, { "Apostraphe'man", 2, 99.9 }, { "Slash\\man", 12, 14.2 } }, new String[] { "name", "age", "weight" }));
    }
}
