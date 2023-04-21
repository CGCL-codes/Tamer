package cn.com.ethos.search.index;

import java.util.ArrayList;
import java.util.Properties;
import org.apache.commons.cli.MissingOptionException;

public class Select {

    static String Empty = "";

    /**
	 * sql
	 */
    static String PREFIX = "sql";

    /**
	 * sql.query
	 */
    static String SELECT = PREFIX + ".query";

    /**
	 * field
	 */
    static String FIELD = "field";

    /**
	 * param
	 */
    static String PARAM = "param";

    static String STORE_VALUE = "NO";

    static String INDEX_VALUE = "NO";

    /**
	 * every select need have an unique name  
	 */
    private String name;

    private String sql;

    private Field[] fields;

    private String parameterKey;

    private String parameterValue;

    private Properties params = new Properties();

    public String getSql() {
        return buildSql();
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Field[] getFields() {
        return fields;
    }

    public void setFields(Field[] fields) {
        this.fields = fields;
    }

    public String getParameterKey() {
        return this.parameterKey;
    }

    public String getParameterValue() {
        return this.parameterValue;
    }

    public void setParameterValue(String value) {
        this.parameterValue = value;
    }

    public String getParameterHashKey() {
        return join(this.name, this.parameterKey);
    }

    /**
	 * sql.query=query1,query2
	 * sql.query1=select * from node
	 * sql.query1.field=nid,tid,name
	 * sql.query1.nid.index=TOKENIZER
	 * 
	 * sql.query2=select * from comment
	 * sql.query2.field=cid,title
	 * 
	 * @param prop
	 * @param name
	 * @param prefix
	 */
    public Select(String name, String sql, Field[] fields) {
        this(name, sql, fields, Empty, Empty);
    }

    public Select(String name, String sql, Field[] fields, String key, String value) {
        this.name = name;
        this.sql = sql;
        this.fields = fields;
        this.parameterKey = key;
        this.parameterValue = value;
    }

    public static Select[] createFromProperties(Properties prop) throws MissingOptionException {
        ArrayList<Select> list = new ArrayList<Select>();
        String[] selects = prop.getProperty(SELECT).split(",");
        for (String sel : selects) {
            String queryKey = join(PREFIX, sel);
            String paramKey = join(queryKey, PARAM);
            if (prop.containsKey(queryKey)) {
                String sql = prop.getProperty(queryKey);
                Field[] fields = createFields(queryKey, prop);
                String[] param = prop.getProperty(paramKey, Empty).split(",");
                String key = param[0];
                String value = param.length == 2 ? param[1] : Empty;
                list.add(new Select(sel, sql, fields, key, value));
            }
        }
        return list.toArray(new Select[0]);
    }

    private static Field[] createFields(String queryKey, Properties prop) throws MissingOptionException {
        String fieldKey = join(queryKey, FIELD);
        if (!prop.containsKey(fieldKey)) {
            throw new MissingOptionException("Can't find the setting key in setting file," + fieldKey);
        }
        String[] fieldList = prop.getProperty(fieldKey).split(",");
        ArrayList<Field> fields = new ArrayList<Field>();
        for (String fieldName : fieldList) {
            String pre = join(queryKey, fieldName);
            String store = prop.getProperty(join(pre, "store"), STORE_VALUE);
            String index = prop.getProperty(join(pre, "index"), INDEX_VALUE);
            fields.add(new Field(fieldName, store, index));
        }
        return fields.toArray(new Field[0]);
    }

    public Properties setBuildParamters(Properties hash) {
        String key = join(this.name, this.parameterKey);
        if (hash.containsKey(key)) {
            this.parameterValue = hash.getProperty(key);
        }
        return this.params = hash;
    }

    private String buildSql() {
        String str = this.sql.replaceAll("@" + parameterKey, parameterValue);
        for (Object key : this.params.keySet()) {
            String keyString = key.toString();
            str = str.replaceAll("@" + keyString, this.params.getProperty(keyString));
        }
        return str;
    }

    private static String join(String prefix, String suffix) {
        return prefix.trim() + '.' + suffix.trim();
    }
}
