package com.sysnet_pioneer.model;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import com.sysnet_pioneer.mysql.read.Fields;
import com.sysnet_pioneer.mysql.write.writeCondition;
import com.sysnet_pioneer.mysql.write.writeExecution;

public class Model {

    private HashMap<Object, Object> dbStructure = new HashMap<Object, Object>();

    private ArrayList<Object> fields = new ArrayList<Object>();

    private String primaryKey = new String();

    private String table = new String();

    private String sqlCommand = new String();

    private HashMap<String, Object> relations = new HashMap<String, Object>();

    private HashMap<String, Object> fieldsWithValues = new HashMap<String, Object>();

    private HashMap<String, Object> fieldRules = new HashMap<String, Object>();

    private JSONArray fieldsViolatedRules = new JSONArray();

    private int error = 0;

    public Model() {
        this.setTable();
        this.setFields();
    }

    public void setFields() {
        Field[] f = this.getClass().getFields();
        for (int i = 0; i < f.length; i++) {
            this.fields.add(f[i].getName());
        }
    }

    public void setPrimaryKey(String field) {
        this.primaryKey = field;
    }

    public void setTable() {
        String table = this.getClass().getSimpleName();
        this.table = table;
    }

    public void setOneToMany(String attribName, String localKey, String foreignTable, String foreignKey) {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("localKey", localKey);
        values.put("foreignTable", foreignTable);
        values.put("foreignKey", foreignKey);
        values.put("type", "OneToMany");
        this.relations.put(attribName, values);
    }

    public void setOneToOne(String attribName, String localKey, String foreignTable, String foreignKey) {
        HashMap<String, String> values = new HashMap<String, String>();
        values.put("localKey", localKey);
        values.put("foreignTable", foreignTable);
        values.put("foreignKey", foreignKey);
        values.put("type", "OneToOne");
        this.relations.put(attribName, values);
    }

    public void setRule(String fieldName, ArrayList<String> rules) {
        if (this.fields.contains(fieldName)) {
            this.fieldRules.put(fieldName, rules);
        }
    }

    public void setRule(ArrayList<String> fieldNames, ArrayList<String> rules) {
        for (String fields : fieldNames) {
            if (this.fields.contains(fields)) {
                this.fieldRules.put(fields, rules);
            }
        }
    }

    public void saveStructure() {
        this.dbStructure.put("table", this.table);
        this.dbStructure.put("fields", this.fields);
        this.dbStructure.put("fieldRules", this.fieldRules);
        this.dbStructure.put("primaryKey", this.primaryKey);
        this.dbStructure.put("relation", this.relations);
    }

    public void showConfig() {
        System.out.println(this.dbStructure);
    }

    public void showFields() {
        System.out.println(this.dbStructure.get("fields"));
    }

    public void showFieldRules() {
        System.out.println(this.dbStructure.get("fieldRules"));
    }

    public void showValues() {
        this.getValues();
        Iterator<Entry<String, Object>> it = this.fieldsWithValues.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes") Map.Entry pairs = it.next();
            System.out.println(pairs.getKey() + " = " + pairs.getValue());
            it.remove();
        }
    }

    public Fields select() {
        this.sqlCommand = "select";
        return new Fields(this.dbStructure, this.sqlCommand);
    }

    public writeExecution save() {
        try {
            String pk = this.dbStructure.get("primaryKey").toString();
            Field a = this.getClass().getField(pk);
            int primaryKey = Integer.parseInt(a.get(this).toString());
            if (primaryKey == 0) {
                this.generateInsertSql();
            } else {
                this.generateUpdateSql(primaryKey);
            }
            if (this.error == 0) {
                return new writeExecution(this.dbStructure, this.sqlCommand);
            } else {
                return new writeExecution(this.error, this.fieldsViolatedRules);
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public writeCondition delete(int primaryKey) {
        this.sqlCommand = "delete from " + this.table + " where " + this.primaryKey + " = " + primaryKey;
        return new writeCondition(this.dbStructure, this.sqlCommand);
    }

    private void getValues() {
        Class<? extends Model> c = this.getClass();
        Field[] f = c.getFields();
        for (int i = 0; i < f.length; i++) {
            try {
                fieldsWithValues.put(f[i].getName(), f[i].get(this));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateInsertSql() {
        String sqlCommand = new String();
        String fields = new String();
        String values = new String();
        sqlCommand = "insert into " + this.table + " ";
        this.getValues();
        Iterator<Entry<String, Object>> it = this.fieldsWithValues.entrySet().iterator();
        fields += "(";
        values += "(";
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes") Map.Entry pairs = it.next();
            this.checkRules(pairs.getKey().toString(), pairs.getValue());
            if (!pairs.getValue().equals("")) {
                String temp = new String();
                fields += pairs.getKey();
                temp = pairs.getValue() + "";
                temp = temp.replace("'", "&#39;");
                temp = temp.replace("\"", "&quot;");
                temp = temp.replace("-", "&#45;");
                values += "'" + temp + "'";
                fields += ",";
                values += ",";
            }
        }
        fields += "--)";
        values += "--)";
        sqlCommand = sqlCommand + fields + "values" + values;
        this.sqlCommand = sqlCommand.replace(",--", "");
    }

    private void generateUpdateSql(int id) {
        String sqlCommand = new String();
        String data = new String();
        sqlCommand = "update " + this.table + " set ";
        this.getValues();
        Iterator<Entry<String, Object>> it = this.fieldsWithValues.entrySet().iterator();
        while (it.hasNext()) {
            @SuppressWarnings("rawtypes") Map.Entry pairs = it.next();
            this.checkRules(pairs.getKey().toString(), pairs.getValue());
            if (pairs.getValue().equals("") || pairs.getKey().equals(this.primaryKey)) {
                continue;
            } else {
                try {
                    int a = Integer.parseInt(pairs.getValue().toString());
                    if (a == 0) {
                        continue;
                    }
                } catch (Exception e) {
                }
                String temp = new String();
                temp = pairs.getValue() + "";
                temp = temp.replace("'", "&#39;");
                temp = temp.replace("\"", "&quot;");
                temp = temp.replace("-", "&#45;");
                data += pairs.getKey() + " = " + "'" + temp + "'";
                data += ",";
            }
        }
        data += "--";
        data = data.replace(",--", "");
        this.sqlCommand = sqlCommand + data + " where " + this.primaryKey + " = " + id;
    }

    @SuppressWarnings("unchecked")
    private void checkRules(String field, Object values) {
        if (!field.equals(this.dbStructure.get("primaryKey"))) {
            JSONObject data = new JSONObject();
            HashMap<String, Object> rules = (HashMap<String, Object>) this.dbStructure.get("fieldRules");
            if (rules.containsKey(field)) {
                ArrayList<String> ruleList = (ArrayList<String>) rules.get(field);
                for (String rule : ruleList) {
                    if (rule.equals("not_null")) {
                        if (values.equals("") || values.equals(0)) {
                            this.error = 1;
                            data.put("error", "not_null");
                            data.put("name", field);
                            this.fieldsViolatedRules.add(data);
                        }
                    }
                }
            }
        }
    }
}
