package com.mlib.web;

/**
 * SQL 参数Bean
 * 
 * @author zitee@163.com
 * 
 */
public class SQLBean {

    private String sqlName;

    private String paraName;

    private int paraType;

    private int paraSource;

    private boolean find;

    private Object value;

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    /**
	 * 
	 * @param sqlName
	 *            SQL字段名
	 * @param paraName
	 *            参数名
	 * @param paraType
	 *            参数类型
	 * @param paraSource
	 *            参数来源
	 */
    public SQLBean(String sqlName, String paraName, int paraType, int paraSource) {
        super();
        this.sqlName = sqlName;
        this.paraName = paraName;
        this.paraType = paraType;
        this.paraSource = paraSource;
    }

    public String getSqlName() {
        return sqlName;
    }

    public void setSqlName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName;
    }

    public int getParaType() {
        return paraType;
    }

    public void setParaType(int paraType) {
        this.paraType = paraType;
    }

    public int getParaSource() {
        return paraSource;
    }

    public void setParaSource(int paraSource) {
        this.paraSource = paraSource;
    }

    public boolean isFind() {
        return find;
    }

    public void setFind(boolean find) {
        this.find = find;
    }
}
