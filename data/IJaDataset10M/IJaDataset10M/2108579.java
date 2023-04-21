package net.sourceforge.dalutils4j;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import net.sourceforge.dalutils4j.jaxb.dao.Crud;
import net.sourceforge.dalutils4j.jaxb.dao.CrudAuto;
import net.sourceforge.dalutils4j.jaxb.dao.DaoClass;
import net.sourceforge.dalutils4j.jaxb.dao.Query;
import net.sourceforge.dalutils4j.jaxb.dao.QueryBean;
import net.sourceforge.dalutils4j.jaxb.dao.QueryBeanList;
import net.sourceforge.dalutils4j.jaxb.dao.QueryList;
import net.sourceforge.dalutils4j.jaxb.dao.TypeCrud;
import net.sourceforge.dalutils4j.jaxb.dao.Update;
import net.sourceforge.dalutils4j.jaxb.dto.DtoClass;
import net.sourceforge.dalutils4j.jaxb.dto.DtoClasses;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class CodeGenerator {

    private static final String DATASTORE_CLASS_NAME = "dalutils4j.Datastore";

    public static final String VM_DTO_CLASS = "DTO_class.vm";

    public static final String VM_DAO_CLASS = "DAO_class.vm";

    private static final String VM_METHOD_CRUD_CREATE = "DAO_method_create.vm";

    private static final String VM_METHOD_QUERY = "DAO_method_query.vm";

    private static final String VM_METHOD_UPDATE = "DAO_method_update.vm";

    public static final String VM_CRUD_SQL = "CRUD_SQL.vm";

    private static class FeildNamesMode {

        public static final String ToLowerCase = "ToLowerCase";

        public static final String ToLowerCamelCase = "ToLowerCamelCase";
    }

    public static final String REFERENCE_DAO_XSD = "dalutils4j.dao.xsd";

    public static final String REFERENCE_DTO_XSD = "dalutils4j.dto.xsd";

    /**
	 * FieldInfo is used from Velocity script
	 * 
	 */
    public static class FieldInfo {

        private String type;

        private String name;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getterMethod() {
            String X = replaceCharAt(name, 0, Character.toUpperCase(name.charAt(0)));
            return "get" + X;
        }

        public String setterMethod() {
            String X = replaceCharAt(name, 0, Character.toUpperCase(name.charAt(0)));
            return "set" + X;
        }

        private static String replaceCharAt(String s, int pos, char c) {
            StringBuffer buf = new StringBuffer(s);
            buf.setCharAt(pos, c);
            return buf.toString();
        }
    }

    /**
	 * KeyInfo is used from Velocity script
	 * 
	 */
    public static class KeyInfo extends FieldInfo {

        private String columnName;

        public KeyInfo(String columnName) {
            this.columnName = columnName;
        }

        public String getColumnName() {
            return columnName;
        }

        public void setColumnName(String columnName) {
            this.columnName = columnName;
        }
    }

    /**
	 * Just helper class
	 * 
	 */
    private static class TableColumnInfo extends FieldInfo {

        private boolean isAutoIncrement;

        public boolean isAutoIncrement() {
            return isAutoIncrement;
        }

        public void setAutoIncrement(boolean isAutoIncrement) {
            this.isAutoIncrement = isAutoIncrement;
        }
    }

    private static class Helpers {

        private static String getErrorMessage(String msg, Throwable e) {
            return msg + " " + e.getClass().getName() + ": " + e.getMessage();
        }

        private static VelocityEngine createVelocityEngine() {
            Properties props = new Properties();
            props.put("resource.loader", "class");
            props.put("class.resource.loader.class", ClasspathResourceLoader.class.getName());
            VelocityEngine ve = new VelocityEngine();
            ve.init(props);
            return ve;
        }

        private static Object unmarshal(Unmarshaller unmarshaller, InputStream xmlStream) throws Exception {
            try {
                return unmarshaller.unmarshal(xmlStream);
            } catch (JAXBException e) {
                if (e.getMessage() == null) {
                    throw new Exception(e.getLinkedException());
                } else {
                    throw new Exception(e);
                }
            }
        }

        private static Unmarshaller getUnmarshallerUsingXSD(String contextPath, String xsdFileName) throws Exception {
            ClassLoader cl = Helpers.class.getClassLoader();
            JAXBContext jc = JAXBContext.newInstance(contextPath, cl);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            InputStream is = new FileInputStream(xsdFileName);
            try {
                Source schemaSource = new StreamSource(is);
                Schema schema = sf.newSchema(schemaSource);
                unmarshaller.setSchema(schema);
            } finally {
                is.close();
            }
            return unmarshaller;
        }

        private static String concatPath(String seg0, String seg1) {
            String res = seg0 + "/" + seg1;
            return res;
        }

        private static String concatPath(String seg0, String seg1, String seg2) {
            String res = concatPath(seg0, seg1);
            return concatPath(res, seg2);
        }

        private static String getPackageFullPath(String project_root, String source_folder, String package_name) {
            String res = concatPath(project_root, source_folder, package_name.replace(".", "/"));
            return res;
        }

        private static String getXmlNodeName(Object element) {
            XmlRootElement attr = element.getClass().getAnnotation(XmlRootElement.class);
            return attr.name();
        }

        private static String sql2javaStr(StringBuffer sqlBuff) {
            return sql2javaStr(sqlBuff.toString());
        }

        private static String sql2javaStr(String sql) {
            String parts[] = sql.split("(\\n|\\r)+");
            String newLine = "\n";
            String newLineJ = org.apache.commons.lang.StringEscapeUtils.escapeJava(newLine);
            StringBuffer res = new StringBuffer();
            for (int i = 0; i < parts.length; i++) {
                String jstr = parts[i].replace('\t', ' ');
                jstr = org.apache.commons.lang.StringEscapeUtils.escapeJava(jstr);
                jstr = jstr.replace("\\/", "/");
                res.append(jstr);
                if (i < parts.length - 1) {
                    res.append(" " + newLineJ + "\" " + newLine + "\t\t\t\t + \"");
                }
            }
            return res.toString();
        }

        private static void saveTextToFile(String fileName, String text) throws IOException {
            File file = new File(fileName);
            file.delete();
            file.getParentFile().mkdirs();
            file.createNewFile();
            FileWriter fw = new FileWriter(file);
            try {
                Writer writer = new BufferedWriter(fw);
                try {
                    writer.write(text);
                } finally {
                    writer.flush();
                    writer.close();
                }
            } finally {
                fw.close();
            }
        }

        private static String loadTextFromFile(String fileName) throws IOException {
            File file = new File(fileName);
            FileReader reader = new FileReader(file);
            try {
                return loadText(reader);
            } finally {
                reader.close();
            }
        }

        private static String loadText(InputStreamReader reader) throws IOException {
            int len;
            char[] chr = new char[4096];
            final StringBuffer buffer = new StringBuffer();
            while ((len = reader.read(chr)) > 0) {
                buffer.append(chr, 0, len);
            }
            return buffer.toString();
        }

        private static String under_scores2lowerCamelCase(String underscores) {
            StringBuffer sb = new StringBuffer();
            String[] arr = underscores.split("_");
            for (int i = 0; i < arr.length; i++) {
                String s = arr[i];
                if (i == 0) {
                    sb.append(s.toLowerCase());
                } else {
                    sb.append(Character.toUpperCase(s.charAt(0)));
                    if (s.length() > 1) {
                        sb.append(s.substring(1, s.length()).toLowerCase());
                    }
                }
            }
            return sb.toString();
        }

        private static String[] getListedItems(String list) {
            if (list != null && list.length() > 0) {
                String[] items = null;
                items = list.split("[.,;]");
                for (int i = 0; i < items.length; i++) {
                    items[i] = items[i].trim();
                }
                return items;
            }
            return new String[] {};
        }

        private static Properties loadProperties(String fileName) throws IOException {
            FileInputStream fis = new FileInputStream(fileName);
            try {
                InputStream is = new BufferedInputStream(fis);
                try {
                    Properties properties = new Properties();
                    properties.load(is);
                    return properties;
                } finally {
                    is.close();
                }
            } finally {
                fis.close();
            }
        }
    }

    private static class JdbcMetadata {

        private static PreparedStatement createPreparedStatement(Connection conn, String tableName) throws SQLException {
            PreparedStatement ps = prepare(conn, "SELECT * FROM " + tableName + " WHERE 1 = 0");
            return ps;
        }

        private static String getColumnName(ResultSetMetaData meta, int col) throws SQLException {
            String columnName = null;
            try {
                columnName = meta.getColumnLabel(col);
            } catch (SQLException e) {
            }
            if (null == columnName || 0 == columnName.length()) {
                columnName = meta.getColumnName(col);
            }
            return columnName;
        }

        private static TableColumnInfo[] getTableColumnsInfo(Connection conn, String tableName, String explicitGenKeys) throws SQLException {
            String[] parts = tableName.split("\\.");
            if (parts.length == 2) {
                return getTableColumnsInfo(conn, parts[0], parts[1], explicitGenKeys);
            } else {
                return getTableColumnsInfo(conn, null, tableName, explicitGenKeys);
            }
        }

        private static TableColumnInfo[] getTableColumnsInfo(Connection conn, String tableShema, String tableName, String explicitGenKeys) throws SQLException {
            Set<String> genKeys = new HashSet<String>();
            if ("*".equals(explicitGenKeys) == false) {
                String[] genKeysArr = Helpers.getListedItems(explicitGenKeys);
                checkDuplicates(genKeysArr);
                for (String k : genKeysArr) {
                    genKeys.add(k.toLowerCase());
                }
            }
            PreparedStatement ps = createPreparedStatement(conn, tableName);
            try {
                ArrayList<TableColumnInfo> list = new ArrayList<TableColumnInfo>();
                ResultSetMetaData meta = ps.getMetaData();
                int columnCount = meta.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String colName = getColumnName(meta, i);
                    TableColumnInfo ci = new TableColumnInfo();
                    ci.setName(colName);
                    String javaClassName = getColumnClassName(meta, i);
                    ci.setType(javaClassName);
                    if ("*".equals(explicitGenKeys) == false) {
                        String key = colName.toLowerCase();
                        if (genKeys.contains(key)) {
                            ci.setAutoIncrement(true);
                            genKeys.remove(key);
                        }
                    } else {
                        boolean isAutoInc = meta.isAutoIncrement(i);
                        ci.setAutoIncrement(isAutoInc);
                    }
                    list.add(ci);
                }
                if (genKeys.size() > 0) {
                    String msg = "Unknown column names are listed as 'generated':";
                    Iterator<String> iter = genKeys.iterator();
                    while (iter.hasNext()) {
                        msg += " " + iter.next();
                    }
                    throw new SQLException(msg);
                }
                TableColumnInfo[] res = list.toArray(new TableColumnInfo[list.size()]);
                return res;
            } finally {
                ps.close();
            }
        }

        private static ArrayList<String> getPKColNames(Connection conn, String tableName) throws SQLException {
            ArrayList<String> res = new ArrayList<String>();
            DatabaseMetaData dbInfo = conn.getMetaData();
            ResultSet rs = dbInfo.getPrimaryKeys(null, null, tableName);
            try {
                while (rs.next()) {
                    res.add(rs.getString("COLUMN_NAME"));
                }
            } finally {
                rs.close();
            }
            return res;
        }

        private static ArrayList<KeyInfo> getPKInfo(Connection conn, String tableName, String feildNamesMode) throws SQLException {
            return getCrudUpdateInfo(conn, tableName, null, feildNamesMode);
        }

        private static ArrayList<KeyInfo> getCrudUpdateInfo(Connection conn, String tableName, ArrayList<KeyInfo> params, String feildNamesMode) throws SQLException {
            Set<String> keyColNamesSet = new HashSet<String>();
            ArrayList<KeyInfo> keys = new ArrayList<KeyInfo>();
            ArrayList<String> pk = getPKColNames(conn, tableName);
            for (int i = 0; i < pk.size(); i++) {
                String colName = pk.get(i);
                keyColNamesSet.add(colName);
            }
            PreparedStatement ps = createPreparedStatement(conn, tableName);
            try {
                ResultSetMetaData meta = ps.getMetaData();
                if (meta == null) {
                    throw new SQLException("PreparedStatement.getMetaData returns null for '" + tableName + "");
                }
                int columnCount = meta.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String colName = getColumnName(meta, i);
                    String javaClassName = getColumnClassName(meta, i);
                    KeyInfo f = createKeyInto(feildNamesMode, javaClassName, colName);
                    if (keyColNamesSet.contains(colName)) {
                        keys.add(f);
                    } else {
                        if (params != null) {
                            params.add(f);
                        }
                    }
                }
            } finally {
                ps.close();
            }
            return keys;
        }

        private static void validateTableName(Connection conn, String tableName) throws SQLException {
            DatabaseMetaData dbInfo = conn.getMetaData();
            ResultSet rs = dbInfo.getTables(null, null, tableName, null);
            try {
                if (rs.next()) {
                    return;
                }
            } finally {
                rs.close();
            }
            throw new SQLException("Data table '" + tableName + "' is not found. Table names can be case sensitive.");
        }

        private static void fillCrudCreateMetadata(Connection conn, String tableName, ArrayList<KeyInfo> keys, List<String> sqlColNames, ArrayList<FieldInfo> params, String feildNamesMode, String generated) throws SQLException {
            TableColumnInfo[] ciArr = getTableColumnsInfo(conn, tableName, generated);
            for (int i = 0; i < ciArr.length; i++) {
                TableColumnInfo ci = ciArr[i];
                String colName = ci.getName();
                if (ci.isAutoIncrement()) {
                    KeyInfo f = createKeyInto(feildNamesMode, ci.getType(), ci.getName());
                    keys.add(f);
                } else {
                    sqlColNames.add(colName);
                    if (FeildNamesMode.ToLowerCase.equals(feildNamesMode)) {
                        ci.setName(colName.toLowerCase());
                    } else if (FeildNamesMode.ToLowerCamelCase.equals(feildNamesMode)) {
                        ci.setName(Helpers.under_scores2lowerCamelCase(colName));
                    } else {
                        ci.setName(colName);
                    }
                    params.add(ci);
                }
            }
        }

        private static ArrayList<FieldInfo> getFieldsInfo(Connection con, String feildNamesMode, String sqlRootAbsPath, String ref) throws Exception {
            String[] parts = ref.split(":");
            String sql = null;
            if (parts.length >= 2) {
                if ("table".compareTo(parts[0].toLowerCase()) == 0) {
                    String tableName = ref.substring(parts[0].length() + 1);
                    sql = "SELECT * FROM " + tableName + " WHERE 1 = 0";
                }
            }
            if (sql == null) {
                String sqlPathFile = Helpers.concatPath(sqlRootAbsPath, ref);
                sql = Helpers.loadTextFromFile(sqlPathFile);
            }
            PreparedStatement st = prepare(con, sql);
            try {
                ResultSetMetaData md = st.getMetaData();
                if (md == null) {
                    throw new Exception("PreparedStatement.getMetaData returns null for '" + sql + "");
                }
                int colCount = md.getColumnCount();
                if (colCount == 0) {
                    throw new Exception("getColumnCount returned 0");
                }
                ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
                for (int i = 1; i <= colCount; i++) {
                    String javaClassName = getColumnClassName(md, i);
                    String x = getColumnName(md, i);
                    FieldInfo f = createFieldInto(feildNamesMode, javaClassName, x);
                    fields.add(f);
                }
                return fields;
            } finally {
                st.close();
            }
        }

        private static void sqlToMetaData(Connection con, String sql, ArrayList<FieldInfo> fields, String[] paramDescriptors, ArrayList<FieldInfo> params, String feildNamesMode) throws SQLException {
            checkDuplicates(paramDescriptors);
            PreparedStatement ps = prepare(con, sql);
            try {
                ResultSetMetaData rsmd = ps.getMetaData();
                if (rsmd != null) {
                    int colCount;
                    try {
                        colCount = rsmd.getColumnCount();
                    } catch (Throwable e) {
                        colCount = 0;
                    }
                    for (int i = 1; i <= colCount; i++) {
                        String javaClassName = getColumnClassName(rsmd, i);
                        String x = getColumnName(rsmd, i);
                        FieldInfo f = createFieldInto(feildNamesMode, javaClassName, x);
                        fields.add(f);
                    }
                }
                fillParams(ps, paramDescriptors, params, feildNamesMode);
            } finally {
                ps.close();
            }
        }

        private static String[] parseParamDescriptor(String paramDescriptor) {
            String paramTypeName;
            String paramName;
            String[] parts = paramDescriptor.split("\\s+");
            if (parts.length > 1) {
                paramName = parts[parts.length - 1];
                paramTypeName = paramDescriptor.substring(0, paramDescriptor.length() - 1 - paramName.length()).trim();
            } else {
                paramName = paramDescriptor;
                paramTypeName = null;
            }
            return new String[] { paramTypeName, paramName };
        }

        private static void fillParams(PreparedStatement ps, String[] paramDescriptors, ArrayList<FieldInfo> params, String feildNamesMode) throws SQLException {
            ParameterMetaData pm;
            try {
                pm = ps.getParameterMetaData();
            } catch (Throwable err) {
                if (paramDescriptors != null) {
                    for (int i = 0; i < paramDescriptors.length; i++) {
                        String paramDescriptor = paramDescriptors[i];
                        String paramTypeName;
                        String paramName;
                        String[] parts = parseParamDescriptor(paramDescriptor);
                        if (parts[0] == null) {
                            paramTypeName = Object.class.getName();
                            paramName = paramDescriptor;
                        } else {
                            paramTypeName = parts[0];
                            paramName = parts[1];
                        }
                        FieldInfo f = createPatamInto(feildNamesMode, paramTypeName, paramName);
                        params.add(f);
                    }
                }
                return;
            }
            int paramsCount = pm.getParameterCount();
            if (paramDescriptors == null && paramsCount > 0 || paramDescriptors != null && paramsCount != paramDescriptors.length) {
                throw new SQLException("Expected parameters count: " + Integer.toString(paramsCount));
            }
            for (int i = 0; i < paramsCount; i++) {
                String paramDescriptor = paramDescriptors[i];
                String paramTypeName;
                String paramName;
                String[] parts = parseParamDescriptor(paramDescriptor);
                if (parts[0] == null) {
                    paramTypeName = getParameterClassName(pm, i);
                    paramName = paramDescriptor;
                } else {
                    paramTypeName = parts[0];
                    paramName = parts[1];
                }
                FieldInfo f = createPatamInto(feildNamesMode, paramTypeName, paramName);
                params.add(f);
            }
        }

        private static final Map<String, Class<?>> primitiveClasses = new HashMap<String, Class<?>>();

        static {
            primitiveClasses.put("byte", Byte.class);
            primitiveClasses.put("short", Short.class);
            primitiveClasses.put("char", Character.class);
            primitiveClasses.put("int", Integer.class);
            primitiveClasses.put("long", Long.class);
            primitiveClasses.put("float", Float.class);
            primitiveClasses.put("double", Double.class);
        }

        private static String tryConvertPrimitive(String name) {
            if (primitiveClasses.containsKey(name)) {
                return primitiveClasses.get(name).getName();
            } else {
                return name;
            }
        }

        private static String getColumnClassName(ResultSetMetaData rsmd, int i) {
            String javaClassName;
            try {
                javaClassName = rsmd.getColumnClassName(i);
                javaClassName = processClassName(javaClassName);
            } catch (Exception ex) {
                javaClassName = Object.class.getName();
            }
            return javaClassName;
        }

        private static String getParameterClassName(ParameterMetaData pm, int i) {
            String javaClassName;
            try {
                javaClassName = pm.getParameterClassName(i + 1);
                javaClassName = processClassName(javaClassName);
            } catch (Exception ex) {
                javaClassName = Object.class.getName();
            }
            return javaClassName;
        }

        private static String processClassName(String javaClassName) throws ClassNotFoundException {
            javaClassName = tryConvertPrimitive(javaClassName);
            Class<?> cl = Class.forName(javaClassName);
            if (cl.isArray()) {
                javaClassName = cl.getSimpleName();
            } else if (java.sql.Date.class.equals(cl) || java.sql.Time.class.equals(cl) || java.sql.Timestamp.class.equals(cl)) {
                return java.util.Date.class.getName();
            }
            return javaClassName;
        }

        private static void initFieldInto(FieldInfo f, String feildNamesMode, String fieldTypeName, String columnName) {
            if (FeildNamesMode.ToLowerCase.equals(feildNamesMode)) {
                f.setName(columnName.toLowerCase());
            } else if (FeildNamesMode.ToLowerCamelCase.equals(feildNamesMode)) {
                f.setName(Helpers.under_scores2lowerCamelCase(columnName));
            } else {
                f.setName(columnName);
            }
            f.setType(fieldTypeName);
        }

        private static KeyInfo createKeyInto(String feildNamesMode, String fieldTypeName, String columnName) {
            KeyInfo f = new KeyInfo(columnName);
            initFieldInto(f, feildNamesMode, fieldTypeName, columnName);
            return f;
        }

        private static FieldInfo createFieldInto(String feildNamesMode, String fieldTypeName, String fieldName) {
            FieldInfo f = new FieldInfo();
            initFieldInto(f, feildNamesMode, fieldTypeName, fieldName);
            return f;
        }

        private static FieldInfo createPatamInto(String feildNamesMode, String paramTypeName, String paramName) {
            return createFieldInto(feildNamesMode, paramTypeName, paramName);
        }

        private static void checkDuplicates(String[] paramNames) throws SQLException {
            if (paramNames != null) {
                Set<String> set = new HashSet<String>();
                for (int i = 0; i < paramNames.length; i++) {
                    if (set.contains(paramNames[i])) {
                        throw new SQLException("Parameter names are duplicated");
                    }
                    set.add(paramNames[i]);
                }
            }
        }

        static void validateSQL(Connection conn, StringBuffer sqlTextBuff) throws SQLException {
            PreparedStatement s = prepare(conn, sqlTextBuff.toString());
            s.close();
        }

        private static PreparedStatement prepare(Connection conn, String sql) throws SQLException {
            try {
                return conn.prepareStatement(sql);
            } catch (Throwable e) {
                rethrow(e, sql);
            }
            return null;
        }

        private static void rethrow(Throwable cause, String sql) throws SQLException {
            String causeMessage = cause.getMessage();
            if (causeMessage == null) {
                causeMessage = "";
            }
            StringBuffer msg = new StringBuffer(causeMessage);
            msg.append(" Query: ");
            msg.append(sql);
            if (cause instanceof SQLException) {
                SQLException src = (SQLException) cause;
                SQLException e = new SQLException(msg.toString(), src.getSQLState(), src.getErrorCode());
                e.setNextException(src);
                throw e;
            } else {
                throw new SQLException(msg.toString());
            }
        }
    }

    public static class DTO {

        private Connection connnection;

        private String project_root;

        private String source_folder;

        private String dto_package;

        private String sql_root;

        private String feild_names_mode;

        private String dto_inheritance;

        public DTO(Connection connnection, String project_root, String source_folder, String dto_package, String sql_root, String dto_inheritance, String feild_names_mode) {
            this.connnection = connnection;
            this.project_root = project_root;
            this.source_folder = source_folder;
            this.dto_package = dto_package;
            this.sql_root = sql_root;
            this.dto_inheritance = dto_inheritance;
            this.feild_names_mode = feild_names_mode;
        }

        public void generate(String dtoClassName, String ref) throws Exception {
            process(dtoClassName, ref, null);
        }

        public void validate(String dtoClassName, String ref, StringBuffer validationBuff) throws Exception {
            process(dtoClassName, ref, validationBuff);
        }

        private void process(String dtoClassName, String ref, StringBuffer validationBuff) throws Exception {
            try {
                String sqlRootFolderFullPath = Helpers.concatPath(project_root, sql_root);
                ArrayList<FieldInfo> fields = JdbcMetadata.getFieldsInfo(connnection, feild_names_mode, sqlRootFolderFullPath, ref);
                VelocityContext context = new VelocityContext();
                context.put("package", dto_package);
                context.put("class_name", dtoClassName);
                context.put("implements", dto_inheritance);
                context.put("fields", fields);
                String templatePath = DTO.class.getPackage().getName().replace('.', '/');
                context.put("templatePath", templatePath);
                VelocityEngine ve = Helpers.createVelocityEngine();
                Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_DTO_CLASS));
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                String text = sw.toString();
                text = text.replace("java.lang.", "");
                String dtoDestination = Helpers.getPackageFullPath(project_root, source_folder, dto_package);
                String fileName = Helpers.concatPath(dtoDestination, dtoClassName + ".java");
                if (validationBuff != null) {
                    String oldText = Helpers.loadTextFromFile(fileName);
                    if (oldText == null) {
                        validationBuff.append("DTO class file is missing");
                    } else if (oldText.equals(text) == false) {
                        validationBuff.append("DTO class is out of date");
                    }
                } else {
                    Helpers.saveTextToFile(fileName, text);
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw new Exception(Helpers.getErrorMessage("", e));
            }
        }

        public Map<String, String> readDefinitions(String xmlFileName, String xsdFileName) throws Exception {
            InputStream fs = new FileInputStream(xmlFileName);
            try {
                return readDefinitions(fs, xsdFileName);
            } finally {
                fs.close();
            }
        }

        public Map<String, String> readDefinitions(InputStream xmlStream, String xsdFileName) throws Exception {
            try {
                String contextPath = DtoClasses.class.getPackage().getName();
                Unmarshaller unmarshaller = Helpers.getUnmarshallerUsingXSD(contextPath, xsdFileName);
                DtoClasses elements = (DtoClasses) Helpers.unmarshal(unmarshaller, xmlStream);
                Map<String, String> res = new HashMap<String, String>();
                for (int i = 0; i < elements.getDtoClass().size(); i++) {
                    DtoClass dto = elements.getDtoClass().get(i);
                    String className = dto.getName();
                    if (className == null || className.length() == 0) {
                        throw new Exception("attribute 'name' is not set");
                    }
                    if (res.containsKey(className)) {
                        throw new Exception("DTO duplicated: " + className);
                    }
                    String genFrom = dto.getRef();
                    if (genFrom == null || genFrom.length() == 0) {
                        throw new Exception("attribute 'ref' is not set in for '" + className + "'");
                    }
                    res.put(className, genFrom);
                }
                return res;
            } catch (Throwable e) {
                e.printStackTrace();
                throw new Exception(Helpers.getErrorMessage("", e));
            }
        }
    }

    public static class DAO {

        private Connection connection;

        private String project_root;

        private String source_folder;

        private String dao_package;

        private String dto_package;

        private String xml_root;

        private String sql_root;

        private String feild_names_mode;

        private Unmarshaller unmarshaller;

        public DAO(Connection connection, String xsdFileName, String project_root, String source_folder, String dto_package, String dao_package, String sql_root, String xml_root, String feild_names_mode) throws Exception {
            this.connection = connection;
            this.project_root = project_root;
            this.source_folder = source_folder;
            this.dto_package = dto_package;
            this.dao_package = dao_package;
            this.feild_names_mode = feild_names_mode;
            this.xml_root = xml_root;
            this.sql_root = sql_root;
            String contextPath = DaoClass.class.getPackage().getName();
            unmarshaller = Helpers.getUnmarshallerUsingXSD(contextPath, xsdFileName);
        }

        public void generate(String daoXmlPath) throws Exception {
            process(daoXmlPath, null);
        }

        public void validate(String daoXmlPath, StringBuffer validationBuff) throws Exception {
            process(daoXmlPath, validationBuff);
        }

        private void process(String daoXmlPath, StringBuffer validationBuff) throws Exception {
            String xdaoFileURL = Helpers.concatPath(project_root, xml_root, daoXmlPath);
            InputStream is = new FileInputStream(xdaoFileURL);
            try {
                process(is, validationBuff);
            } finally {
                is.close();
            }
        }

        private void process(InputStream xmlStream, StringBuffer validationBuff) throws Exception {
            DaoClass daoClass = (DaoClass) Helpers.unmarshal(unmarshaller, xmlStream);
            String className = daoClass.getName();
            Set<String> imports = new HashSet<String>();
            List<String> methods = new ArrayList<String>();
            if (daoClass.getCrudOrCrudAutoOrQuery() != null) {
                imports.add(DATASTORE_CLASS_NAME);
                for (int i = 0; i < daoClass.getCrudOrCrudAutoOrQuery().size(); i++) {
                    Object element = daoClass.getCrudOrCrudAutoOrQuery().get(i);
                    if (element instanceof Query || element instanceof QueryList || element instanceof QueryBean || element instanceof QueryBeanList) {
                        StringBuffer buf = renderQueryMethod(element, className, imports);
                        methods.add(buf.toString());
                    } else if (element instanceof Update) {
                        StringBuffer buf = renderUpdateMethod((Update) element, className, imports);
                        methods.add(buf.toString());
                    } else {
                        StringBuffer buf = renderCrudMethods(element, className, imports, validationBuff);
                        methods.add(buf.toString());
                    }
                }
            }
            String daoDestination = Helpers.getPackageFullPath(project_root, source_folder, dao_package);
            VelocityContext context = new VelocityContext();
            context.put("package", dao_package);
            String[] arr = imports.toArray(new String[imports.size()]);
            java.util.Arrays.sort(arr);
            context.put("imports", arr);
            context.put("class_name", className);
            context.put("methods", methods);
            String templatePath = DAO.class.getPackage().getName().replace('.', '/');
            context.put("templatePath", templatePath);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_DAO_CLASS));
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            String text = sw.toString();
            text = text.replace("java.lang.", "");
            String fileName = Helpers.concatPath(daoDestination, className + ".java");
            if (validationBuff != null) {
                String oldText = Helpers.loadTextFromFile(fileName);
                if (oldText == null) {
                    validationBuff.append("DAO class file is missing");
                } else if (oldText.equals(text) == false) {
                    validationBuff.append("DAO class is out of date");
                }
            } else {
                Helpers.saveTextToFile(fileName, text);
            }
        }

        private StringBuffer renderQueryMethod(Object element, String daoClassName, Set<String> imports) throws Exception {
            StringBuffer buff = new StringBuffer();
            String nodeName = Helpers.getXmlNodeName(element);
            String method;
            String sqlRes;
            if (element instanceof Query) {
                Query q = (Query) element;
                method = q.getMethod();
                sqlRes = q.getRef();
            } else if (element instanceof QueryList) {
                QueryList q = (QueryList) element;
                method = q.getMethod();
                sqlRes = q.getRef();
            } else if (element instanceof QueryBean) {
                QueryBean q = (QueryBean) element;
                method = q.getMethod();
                sqlRes = q.getRef();
            } else if (element instanceof QueryBeanList) {
                QueryBeanList q = (QueryBeanList) element;
                method = q.getMethod();
                sqlRes = q.getRef();
            } else {
                throw new Exception("Unexpected XML element: " + nodeName);
            }
            checkRequiredAttr(nodeName, method, sqlRes);
            try {
                String sqlRootFolderFullPath = Helpers.concatPath(project_root, sql_root);
                String sqlFileName = Helpers.concatPath(sqlRootFolderFullPath, sqlRes);
                String sql = Helpers.loadTextFromFile(sqlFileName);
                String dtoRetValueType = "";
                boolean fetchList;
                if ((element instanceof QueryBeanList) || (element instanceof QueryList)) {
                    fetchList = true;
                } else {
                    fetchList = false;
                }
                boolean usingDTO = false;
                if (element instanceof QueryBean) {
                    dtoRetValueType = ((QueryBean) element).getDto();
                    usingDTO = true;
                } else if (element instanceof QueryBeanList) {
                    dtoRetValueType = ((QueryBeanList) element).getDto();
                    usingDTO = true;
                }
                if (usingDTO) {
                    dtoRetValueType = processDtoName(dto_package, dtoRetValueType, imports);
                }
                String[] parsed = parseMethodDeclaration(method, dto_package, imports);
                String methodName = parsed[0];
                String dtoParamType = parsed[1];
                String paramDescriptors = parsed[2];
                String[] paramDescriptorsArr = Helpers.getListedItems(paramDescriptors);
                renderQuery(buff, sql, dtoRetValueType, fetchList, methodName, dtoParamType, paramDescriptorsArr, imports, "Based on '" + sqlRes + "'");
            } catch (Throwable e) {
                e.printStackTrace();
                String msg = "<" + nodeName + " method=\"" + method + "\" ref=\"" + sqlRes + "\"...\n";
                throw new Exception(Helpers.getErrorMessage(msg, e));
            }
            return buff;
        }

        private static String processDtoName(String dto_package, String dto, Set<String> imports) throws Exception {
            if (dto.contains(".")) {
                int lastDotIndex = dto.lastIndexOf('.');
                if (lastDotIndex == 0 || lastDotIndex == dto.length() - 1) {
                    throw new Exception("Invalid name of DTO class: " + dto);
                }
                dto_package = dto.substring(0, lastDotIndex);
                dto = dto.substring(lastDotIndex + 1);
            }
            imports.add(dto_package + "." + dto);
            return dto;
        }

        private StringBuffer renderUpdateMethod(Update element, String daoClassName, Set<String> imports) throws Exception {
            StringBuffer buff = new StringBuffer();
            String nodeName = Helpers.getXmlNodeName(element);
            String method = element.getMethod();
            String sqlRes = element.getRef();
            checkRequiredAttr(nodeName, method, sqlRes);
            try {
                String sqlRootFolderFullPath = Helpers.concatPath(project_root, sql_root);
                String sqlFileName = Helpers.concatPath(sqlRootFolderFullPath, sqlRes);
                String sql = Helpers.loadTextFromFile(sqlFileName);
                String[] parsed = parseMethodDeclaration(method, dto_package, imports);
                String methodName = parsed[0];
                String dtoParamType = parsed[1];
                String paramDescriptors = parsed[2];
                String[] paramDescriptorsArr = Helpers.getListedItems(paramDescriptors);
                renderUpdate(buff, daoClassName, sql, methodName, dtoParamType, paramDescriptorsArr, imports, sqlRes);
            } catch (Throwable e) {
                e.printStackTrace();
                String msg = "<" + nodeName + " method=\"" + method + "\" ref=\"" + sqlRes + "\"...\n";
                throw new Exception(Helpers.getErrorMessage(msg, e));
            }
            return buff;
        }

        private void renderQuery(StringBuffer buff, String sql, String retValueType, boolean fetchList, String methodName, String dtoParamType, String[] paramDescriptors, Set<String> imports, String comment) throws Exception {
            ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
            ArrayList<FieldInfo> params = new ArrayList<FieldInfo>();
            JdbcMetadata.sqlToMetaData(connection, sql, fields, paramDescriptors, params, feild_names_mode);
            int colCount = fields.size();
            if (colCount == 0) {
                throw new Exception("ResultSet is empty. Is SQL statement valid?");
            }
            boolean useBeans;
            String returnedTypeName;
            if (retValueType == null || retValueType.length() == 0) {
                if (colCount != 1) {
                    throw new Exception("ResultSet should contain 1 column. Is SQL statement valid?");
                }
                useBeans = false;
                String singleColumnClassName = fields.get(0).getType();
                returnedTypeName = singleColumnClassName;
            } else {
                useBeans = true;
                returnedTypeName = retValueType;
            }
            String javaSqlStr = Helpers.sql2javaStr(sql);
            VelocityContext context = new VelocityContext();
            assignParams(params, dtoParamType, context);
            context.put("fields", fields);
            context.put("methodName", methodName);
            context.put("comment", comment);
            context.put("sql", javaSqlStr);
            context.put("useBeans", useBeans);
            context.put("returnedTypeName", returnedTypeName);
            context.put("fetchList", fetchList);
            context.put("imports", imports);
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            context.put("templatePath", templatePath);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_QUERY));
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            buff.append(sw.getBuffer());
        }

        private void renderUpdate(StringBuffer buffer, String daoClassName, String sql, String methodName, String dtoParamType, String[] paramDescriptors, Set<String> imports, String sqlRes) throws Exception {
            ArrayList<FieldInfo> fields = new ArrayList<FieldInfo>();
            ArrayList<FieldInfo> params = new ArrayList<FieldInfo>();
            JdbcMetadata.sqlToMetaData(connection, sql, fields, paramDescriptors, params, feild_names_mode);
            String trimmed = sql.toLowerCase().trim();
            String[] parts = trimmed.split("\\s+");
            if (parts.length > 0) {
                if ("select".equals(parts[0])) {
                    throw new Exception("SELECT is not allowed here");
                }
            }
            String javaSqlStr = Helpers.sql2javaStr(sql);
            VelocityContext context = new VelocityContext();
            assignParams(params, dtoParamType, context);
            context.put("methodName", methodName);
            context.put("sql", javaSqlStr);
            context.put("comment", "Executes '" + sqlRes + "'");
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            context.put("templatePath", templatePath);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_UPDATE));
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            buffer.append(sw.getBuffer());
        }

        private static void assignParams(ArrayList<FieldInfo> params, String dtoParamType, VelocityContext context) throws Exception {
            int paramsCount = params.size();
            if (dtoParamType.length() > 0) {
                if (paramsCount == 0) {
                    throw new Exception("DTO parameter specified but SQL-query does not contain any parameters");
                }
                context.put("dtoParam", dtoParamType);
            } else {
                context.put("dtoParam", "");
            }
            context.put("params", params);
        }

        private static String[] parseDecl(String src) throws Exception {
            String beforeBreackets;
            String insideBreackets;
            src = src.trim();
            int pos = src.indexOf('(');
            if (pos == -1) {
                beforeBreackets = src;
                insideBreackets = "";
            } else {
                if (src.endsWith(")") == false) {
                    throw new Exception("')' expected");
                }
                beforeBreackets = src.substring(0, pos);
                insideBreackets = src.substring(beforeBreackets.length() + 1, src.length() - 1);
            }
            return new String[] { beforeBreackets, insideBreackets };
        }

        private static String[] parseMethodDeclaration(String methodDecl, String dtoPackage, Set<String> imports) throws Exception {
            String dtoParamType = "";
            String paramDescriptors = "";
            String methodName;
            String[] parts = parseDecl(methodDecl);
            methodName = parts[0];
            if (parts[1] != "") {
                parts = parseDecl(parts[1]);
                if (parts[1] != "") {
                    dtoParamType = parts[0];
                    paramDescriptors = parts[1];
                    if (dtoParamType.length() > 0) {
                        dtoParamType = processDtoName(dtoPackage, dtoParamType, imports);
                    }
                } else {
                    paramDescriptors = parts[0];
                }
            }
            return new String[] { methodName, dtoParamType, paramDescriptors };
        }

        private static void checkRequiredAttr(String nodeName, String methodNameAttr, String queryfileAttr) throws Exception {
            if (methodNameAttr == null || methodNameAttr.length() == 0) {
                throw new Exception("<" + nodeName + "...\n'method' is not set.");
            }
            if (queryfileAttr == null || queryfileAttr.length() == 0) {
                throw new Exception("<" + nodeName + " method=\"" + methodNameAttr + "\"...\n 'ref' is not set");
            }
        }

        private StringBuffer renderCrudReadMethod(StringBuffer sqlBuff, String methodName, String tableName, String retValueType, boolean fetchList, Set<String> imports) throws Exception {
            StringBuffer buffer = new StringBuffer();
            ArrayList<KeyInfo> keys = new ArrayList<KeyInfo>();
            if (fetchList == false) {
                keys = JdbcMetadata.getPKInfo(connection, tableName, feild_names_mode);
                if (keys.size() == 0) {
                    return buffer;
                }
            }
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            {
                VelocityContext context = new VelocityContext();
                context.put("keys", keys);
                String crud = fetchList ? "read_all" : "read_single";
                Template template = getSqlTemplate(context, tableName, templatePath, crud);
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                sqlBuff.append(sw.getBuffer());
            }
            String comment = fetchList ? "Reads all records from '" + tableName + "' data table." : "Reads specified record from '" + tableName + "' data table.";
            ArrayList<String> desc = new ArrayList<String>();
            for (KeyInfo k : keys) {
                desc.add(k.getType() + " " + k.getName());
            }
            String[] paramDescriptorsArr = desc.toArray(new String[desc.size()]);
            renderQuery(buffer, sqlBuff.toString(), retValueType, fetchList, methodName, "", paramDescriptorsArr, imports, comment);
            return buffer;
        }

        private StringBuffer renderCrudInsertMetod(StringBuffer sqlBuff, String methodName, String tableName, String dtoParamClassName, Set<String> imports, boolean fetchGenerated, String generated) throws Exception {
            ArrayList<FieldInfo> params = new ArrayList<FieldInfo>();
            ArrayList<KeyInfo> keys = new ArrayList<KeyInfo>();
            List<String> sqlColNames = new ArrayList<String>();
            JdbcMetadata.fillCrudCreateMetadata(connection, tableName, keys, sqlColNames, params, feild_names_mode, generated);
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            if (sqlBuff.length() == 0) {
                VelocityContext context = new VelocityContext();
                context.put("col_names", sqlColNames);
                Template template = getSqlTemplate(context, tableName, templatePath, "create");
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                sqlBuff.append(sw.getBuffer());
            }
            String javaSqlStr = Helpers.sql2javaStr(sqlBuff);
            VelocityContext context = new VelocityContext();
            context.put("methodType", "CREATE");
            context.put("comment", "Creates new record in '" + tableName + "' data table.");
            context.put("sql", javaSqlStr);
            context.put("methodName", methodName);
            context.put("params", params);
            context.put("dtoParam", dtoParamClassName);
            context.put("templatePath", templatePath);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template;
            if (fetchGenerated && keys.size() > 0) {
                if (dtoParamClassName.length() == 0) {
                    imports.add("java.util.Map");
                }
                context.put("keys", keys);
                template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_CRUD_CREATE));
            } else {
                template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_UPDATE));
            }
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            StringBuffer buffer = new StringBuffer();
            buffer.append(sw.getBuffer());
            return buffer;
        }

        private static Template getSqlTemplate(VelocityContext context, String tableName, String templatePath, String crud) {
            context.put("tableName", tableName);
            context.put("templatePath", templatePath);
            context.put("crud", crud);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template res = ve.getTemplate(Helpers.concatPath(templatePath, VM_CRUD_SQL));
            return res;
        }

        private StringBuffer renderCrudUpdateMetod(StringBuffer sqlBuff, String methodName, String tableName, String dtoParamClassName, Set<String> imports) throws Exception {
            StringBuffer buffer = new StringBuffer();
            ArrayList<KeyInfo> fieldValueParams = new ArrayList<KeyInfo>();
            ArrayList<KeyInfo> keys = JdbcMetadata.getCrudUpdateInfo(connection, tableName, fieldValueParams, feild_names_mode);
            if (keys.size() == 0) {
                return buffer;
            }
            if (fieldValueParams.size() == 0) {
                return buffer;
            }
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            if (sqlBuff.length() == 0) {
                VelocityContext context = new VelocityContext();
                context.put("params", fieldValueParams);
                context.put("keys", keys);
                Template template = getSqlTemplate(context, tableName, templatePath, "update");
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                sqlBuff.append(sw.getBuffer());
            }
            for (KeyInfo k : keys) {
                fieldValueParams.add(k);
            }
            String javaSqlStr = Helpers.sql2javaStr(sqlBuff);
            VelocityContext context = new VelocityContext();
            context.put("methodName", methodName);
            context.put("sql", javaSqlStr);
            context.put("methodType", "UPDATE");
            context.put("comment", "Updates specified record in '" + tableName + "' data table.");
            context.put("templatePath", templatePath);
            context.put("dtoParam", dtoParamClassName);
            context.put("params", fieldValueParams);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_UPDATE));
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            buffer.append(sw.getBuffer());
            return buffer;
        }

        private StringBuffer renderCrudDeleteMetod(StringBuffer sqlBuff, String methodName, String tableName, String dtoParamClassName, Set<String> imports) throws Exception {
            StringBuffer buffer = new StringBuffer();
            ArrayList<KeyInfo> keys = JdbcMetadata.getPKInfo(connection, tableName, feild_names_mode);
            if (keys.size() == 0) {
                return buffer;
            }
            String templatePath = this.getClass().getPackage().getName().replace('.', '/');
            if (sqlBuff.length() == 0) {
                VelocityContext context = new VelocityContext();
                context.put("keys", keys);
                Template template = getSqlTemplate(context, tableName, templatePath, "delete");
                StringWriter sw = new StringWriter();
                template.merge(context, sw);
                sqlBuff.append(sw.getBuffer());
            }
            String javaSqlStr = Helpers.sql2javaStr(sqlBuff);
            VelocityContext context = new VelocityContext();
            context.put("methodName", methodName);
            context.put("sql", javaSqlStr);
            context.put("methodType", "DELETE");
            context.put("comment", "Deletes specified record from '" + tableName + "' data table.");
            context.put("templatePath", templatePath);
            context.put("dtoParam", dtoParamClassName);
            context.put("params", keys);
            VelocityEngine ve = Helpers.createVelocityEngine();
            Template template = ve.getTemplate(Helpers.concatPath(templatePath, VM_METHOD_UPDATE));
            StringWriter sw = new StringWriter();
            template.merge(context, sw);
            buffer.append(sw.getBuffer());
            return buffer;
        }

        private StringBuffer renderCrudMethods(Object element, String daoClassName, Set<String> imports, StringBuffer validationBuff) throws Exception {
            if (element instanceof TypeCrud == false) {
                throw new Exception("Unexpected element found in DTO XML file");
            }
            TypeCrud crud = (TypeCrud) element;
            String nodeName = Helpers.getXmlNodeName(element);
            String dtoClassName = crud.getDto();
            if (dtoClassName.length() == 0) {
                throw new Exception("<" + nodeName + "...\nDTO class is not set");
            }
            String tableAttr = crud.getTable();
            if (tableAttr == null || tableAttr.length() == 0) {
                throw new Exception("<" + nodeName + "...\nRequired attribute is not set");
            }
            try {
                JdbcMetadata.validateTableName(connection, tableAttr);
                StringBuffer codeBuff = renderCrudMetods(crud, imports, validationBuff, daoClassName, dtoClassName, tableAttr);
                return codeBuff;
            } catch (Throwable e) {
                e.printStackTrace();
                String msg = "<" + nodeName + " dto=\"" + dtoClassName + "\" table=\"" + tableAttr + "\"...\n";
                throw new Exception(Helpers.getErrorMessage(msg, e));
            }
        }

        private StringBuffer renderCrudMetods(TypeCrud element, Set<String> imports, StringBuffer validationBuff, String daoClassName, String dtoClassName, String tableAttr) throws Exception {
            boolean isEmpty = true;
            String nodeName = Helpers.getXmlNodeName(element);
            dtoClassName = processDtoName(dto_package, dtoClassName, imports);
            StringBuffer codeBuff = new StringBuffer();
            boolean fetchGenerated = element.isFetchGenerated();
            String generated = element.getGenerated();
            {
                String methodName = null;
                if (element.getCreate() != null) {
                    methodName = element.getCreate().getMethod();
                } else {
                    if (element instanceof CrudAuto) {
                        methodName = "create" + dtoClassName;
                    }
                }
                if (methodName != null) {
                    isEmpty = false;
                    StringBuffer sqlBuff = new StringBuffer();
                    StringBuffer tmp = renderCrudInsertMetod(sqlBuff, methodName, tableAttr, dtoClassName, imports, fetchGenerated, generated);
                    codeBuff.append(tmp);
                    JdbcMetadata.validateSQL(connection, sqlBuff);
                    tmp = renderCrudInsertMetod(sqlBuff, methodName, tableAttr, "", imports, fetchGenerated, generated);
                    codeBuff.append(tmp);
                }
            }
            {
                String methodName = null;
                if (element.getReadAll() != null) {
                    methodName = element.getReadAll().getMethod();
                } else {
                    if (element instanceof CrudAuto) {
                        methodName = "read" + dtoClassName + "List";
                    }
                }
                if (methodName != null) {
                    isEmpty = false;
                    StringBuffer sqlBuff = new StringBuffer();
                    StringBuffer tmp = renderCrudReadMethod(sqlBuff, methodName, tableAttr, dtoClassName, true, imports);
                    codeBuff.append(tmp);
                    JdbcMetadata.validateSQL(connection, sqlBuff);
                }
            }
            {
                String methodName = null;
                if (element.getRead() != null) {
                    methodName = element.getRead().getMethod();
                } else {
                    if (element instanceof CrudAuto) {
                        methodName = "read" + dtoClassName;
                    }
                }
                if (methodName != null) {
                    isEmpty = false;
                    StringBuffer sqlBuff = new StringBuffer();
                    StringBuffer tmp = renderCrudReadMethod(sqlBuff, methodName, tableAttr, dtoClassName, false, imports);
                    codeBuff.append(tmp);
                    JdbcMetadata.validateSQL(connection, sqlBuff);
                }
            }
            {
                String methodName = null;
                if (element.getUpdate() != null) {
                    methodName = element.getUpdate().getMethod();
                } else {
                    if (element instanceof CrudAuto) {
                        methodName = "update" + dtoClassName;
                    }
                }
                if (methodName != null) {
                    isEmpty = false;
                    StringBuffer sqlBuff = new StringBuffer();
                    StringBuffer tmp = renderCrudUpdateMetod(sqlBuff, methodName, tableAttr, dtoClassName, imports);
                    codeBuff.append(tmp);
                    JdbcMetadata.validateSQL(connection, sqlBuff);
                    tmp = renderCrudUpdateMetod(sqlBuff, methodName, tableAttr, "", imports);
                    codeBuff.append(tmp);
                }
            }
            {
                String methodName = null;
                if (element.getDelete() != null) {
                    methodName = element.getDelete().getMethod();
                } else {
                    if (element instanceof CrudAuto) {
                        methodName = "delete" + dtoClassName;
                    }
                }
                if (methodName != null) {
                    isEmpty = false;
                    StringBuffer sqlBuff = new StringBuffer();
                    StringBuffer tmp = renderCrudDeleteMetod(sqlBuff, methodName, tableAttr, dtoClassName, imports);
                    codeBuff.append(tmp);
                    JdbcMetadata.validateSQL(connection, sqlBuff);
                    tmp = renderCrudDeleteMetod(sqlBuff, methodName, tableAttr, "", imports);
                    codeBuff.append(tmp);
                }
            }
            if ((element instanceof Crud) && isEmpty) {
                throw new Exception("Element '" + nodeName + "' is empty. Add the method declarations or change to 'crud-auto'");
            }
            return codeBuff;
        }
    }

    public interface IFileList {

        void add(String fileName);
    }

    public static class FileSearchHelpers {

        private static class DaoFilenameFilter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("dao.") && name.endsWith(".xml") || name.endsWith(".dao.xml")) {
                    return true;
                }
                return false;
            }
        }

        private static class DtoFilenameFilter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                if (name.startsWith("dto.") && name.endsWith(".xml") || name.endsWith(".dto.xml")) {
                    return true;
                }
                return false;
            }
        }

        ;

        private static class DtoXsdFilenameFilter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                if (REFERENCE_DTO_XSD.equals(name)) {
                    return true;
                }
                return false;
            }
        }

        ;

        private static class DaoXsdFilenameFilter implements FilenameFilter {

            @Override
            public boolean accept(File dir, String name) {
                if (REFERENCE_DAO_XSD.equals(name)) {
                    return true;
                }
                return false;
            }
        }

        ;

        public static ArrayList<String> findRecursive(File dir, final String fileName) {
            final ArrayList<String> res = new ArrayList<String>();
            IFileList fileList = new IFileList() {

                @Override
                public void add(String fileName) {
                    res.add(fileName);
                }
            };
            FilenameFilter filter = new FilenameFilter() {

                @Override
                public boolean accept(File arg0, String name) {
                    return name.compareTo(fileName) == 0;
                }
            };
            enumFieles(dir, fileList, filter);
            return res;
        }

        public static ArrayList<String> findAllDtoFilesRecursive(File dir) {
            return findAllFilesRecursive(dir, new DtoFilenameFilter());
        }

        public static ArrayList<String> findAllDtoXsdFilesRecursive(File dir) {
            return findAllFilesRecursive(dir, new DtoXsdFilenameFilter());
        }

        public static ArrayList<String> findAllDaoFilesRecursive(File dir) {
            return findAllFilesRecursive(dir, new DaoFilenameFilter());
        }

        public static ArrayList<String> findAllDaoXsdFilesRecursive(File dir) {
            return findAllFilesRecursive(dir, new DaoXsdFilenameFilter());
        }

        private static ArrayList<String> findAllFilesRecursive(File dir, FilenameFilter filter) {
            final ArrayList<String> res = new ArrayList<String>();
            IFileList fileList = new IFileList() {

                @Override
                public void add(String fileName) {
                    res.add(fileName);
                }
            };
            enumFieles(dir, fileList, filter);
            return res;
        }

        static void enumFieles(File dir, IFileList fileList, FilenameFilter filter) {
            String[] children = dir.list();
            if (children == null) {
            } else {
                for (int i = 0; i < children.length; i++) {
                    String filename = dir.getPath() + "/" + children[i];
                    filename = filename.replace("\\", "/");
                    File f1 = new File(filename);
                    if (f1.isDirectory()) {
                        enumFieles(f1, fileList, filter);
                    }
                    if (filter.accept(dir, children[i])) {
                        fileList.add(filename);
                    }
                }
            }
        }

        public static String findDtoXmlFile(String xml_configs_folder_full_path) throws Exception {
            File dir = new File(xml_configs_folder_full_path);
            ArrayList<String> list = findAllDtoFilesRecursive(dir);
            return getFile(xml_configs_folder_full_path, list, "DTO config");
        }

        public static String findDtoXsdFile(String xml_configs_folder_full_path) throws Exception {
            File dir = new File(xml_configs_folder_full_path);
            ArrayList<String> list = findAllDtoXsdFilesRecursive(dir);
            return getFile(xml_configs_folder_full_path, list, REFERENCE_DTO_XSD);
        }

        public static String findDaoXsdFile(String xml_configs_folder_full_path) throws Exception {
            File dir = new File(xml_configs_folder_full_path);
            ArrayList<String> list = findAllDaoXsdFilesRecursive(dir);
            return getFile(xml_configs_folder_full_path, list, REFERENCE_DAO_XSD);
        }

        private static String getFile(String xml_configs_folder_full_path, ArrayList<String> list, String msg) throws Exception {
            File dir = new File(xml_configs_folder_full_path);
            if (list.size() == 0) {
                throw new Exception(msg + " not found in '" + dir.getAbsolutePath() + "'");
            }
            if (list.size() > 1) {
                throw new Exception("More than one " + msg + " found in '" + dir.getAbsolutePath() + "'");
            }
            String fileName = list.get(0);
            return fileName;
        }

        public static void enumDaoConfigs(String xml_configs_folder_full_path, IFileList fileList) {
            File dir = new File(xml_configs_folder_full_path);
            enumFieles(dir, fileList, new DaoFilenameFilter());
        }
    }

    private static Connection getConnection(String projectRoot, String driverJar, String driverClassName, String url, String userName, String password) throws Exception {
        driverJar = Helpers.concatPath(projectRoot, driverJar);
        Class<?> cl = null;
        if (driverJar != null && !"".equals(driverJar)) {
            ClassLoader loader = new URLClassLoader(new URL[] { new File(driverJar).toURI().toURL() });
            cl = Class.forName(driverClassName, true, loader);
        } else {
            cl = Class.forName(driverClassName);
        }
        Driver driver = (Driver) cl.newInstance();
        Connection con;
        Properties props = new Properties();
        if (userName != null) {
            props.put("user", userName);
            props.put("password", password);
        }
        con = driver.connect(url, props);
        if (con == null) {
            throw new Exception("Invalid URL");
        }
        return con;
    }

    private class KeyNames {

        private static final String p = "-p";

        private static final String m = "-m";

        private static final String i = "-i";

        private static final String cjar = "-cjar";

        private static final String cd = "-cd";

        private static final String curl = "-curl";

        private static final String cu = "-cu";

        private static final String cp = "-cp";
    }

    private static HashMap<String, String> parseArgs(String[] args) {
        HashMap<String, String> res = new HashMap<String, String>();
        Set<String> argKeys = new HashSet<String>();
        argKeys.add(KeyNames.p);
        argKeys.add(KeyNames.m);
        argKeys.add(KeyNames.i);
        argKeys.add(KeyNames.cjar);
        argKeys.add(KeyNames.cd);
        argKeys.add(KeyNames.curl);
        argKeys.add(KeyNames.cu);
        argKeys.add(KeyNames.cp);
        String lastKey = null;
        for (int i = 0; i < args.length; i++) {
            String curr = args[i];
            if (argKeys.contains(curr)) {
                lastKey = curr;
            } else {
                if (lastKey != null) {
                    res.put(lastKey, curr);
                }
            }
        }
        return res;
    }

    /**
	 * @param args
	 * @throws Exception
	 */
    public static void main(String[] args) throws Exception {
        HashMap<String, String> parsedArgs = parseArgs(args);
        String project_root = parsedArgs.get(KeyNames.p);
        String mode = parsedArgs.get(KeyNames.m);
        String items = parsedArgs.get(KeyNames.i);
        String[] itemsArr = items.split("[|]");
        String driverJar = parsedArgs.get(KeyNames.cjar);
        String driverClassName = parsedArgs.get(KeyNames.cd);
        String url = parsedArgs.get(KeyNames.curl);
        String userName = parsedArgs.get(KeyNames.cu);
        String password = parsedArgs.get(KeyNames.cp);
        String fn = Helpers.concatPath(project_root, "dalutils4j.codegenerator.properties");
        Properties properties = Helpers.loadProperties(fn);
        String xml_configs_folder = properties.getProperty("xml_configs_folder", "dalutils4j");
        String source_folder = properties.getProperty("source_folder", "");
        String dto_package = properties.getProperty("dto_package", "myproject.dto");
        String dao_package = properties.getProperty("dao_package", "myproject.dal.dao");
        String sql_root_folder = properties.getProperty("sql_root_folder", "dalutils4j/sql");
        String dto_inheritance = properties.getProperty("dto_inheritance", "");
        String fieldnames_mode = properties.getProperty("dto_fieldnames_mode", FeildNamesMode.ToLowerCase);
        String xml_configs_folder_full_path = Helpers.concatPath(project_root, xml_configs_folder);
        Connection conn = getConnection(project_root, driverJar, driverClassName, url, userName, password);
        try {
            if ("dto_g".equals(mode) || "dto_v".equals(mode)) {
                CodeGenerator.DTO gen = new CodeGenerator.DTO(conn, project_root, source_folder, dto_package, sql_root_folder, dto_inheritance, fieldnames_mode);
                String dtoxmlfn = FileSearchHelpers.findDtoXmlFile(xml_configs_folder_full_path);
                String dtoxsdfn = FileSearchHelpers.findDtoXsdFile(xml_configs_folder_full_path);
                Map<String, String> refs = gen.readDefinitions(dtoxmlfn, dtoxsdfn);
                for (int i = 0; i < itemsArr.length; i++) {
                    String dtoClassName = itemsArr[i];
                    if (dtoClassName.length() == 0) {
                        continue;
                    }
                    String ref = refs.get(dtoClassName);
                    System.out.print(dtoClassName);
                    System.out.print("\t\t\t\t");
                    System.out.print(ref);
                    System.out.print("\t\t\t\t");
                    if ("dto_v".equals(mode)) {
                        try {
                            StringBuffer validationBuff = new StringBuffer();
                            gen.validate(dtoClassName, ref, validationBuff);
                            String status = validationBuff.toString();
                            if (status.length() == 0) {
                                System.out.println("DTO class is up to date");
                            } else {
                                System.out.println(status + " <<<-----------------");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        gen.generate(dtoClassName, ref);
                        System.out.println("DTO class generated successfully");
                    }
                }
            } else {
                String daoxsdfn = FileSearchHelpers.findDaoXsdFile(xml_configs_folder_full_path);
                CodeGenerator.DAO gen = new CodeGenerator.DAO(conn, daoxsdfn, project_root, source_folder, dto_package, dao_package, sql_root_folder, xml_configs_folder, fieldnames_mode);
                for (int i = 0; i < itemsArr.length; i++) {
                    String daoXmlPath = itemsArr[i];
                    System.out.print(daoXmlPath);
                    System.out.print("\t\t\t\t");
                    if ("dao_v".equals(mode)) {
                        try {
                            StringBuffer validationBuff = new StringBuffer();
                            gen.validate(daoXmlPath, validationBuff);
                            String status = validationBuff.toString();
                            if (status.length() == 0) {
                                System.out.println("DAO class is up to date");
                            } else {
                                System.out.println(status + " <<<-----------------");
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        gen.generate(daoXmlPath);
                        System.out.println("DAO class generated successfully");
                    }
                }
            }
        } finally {
            conn.close();
        }
    }
}
