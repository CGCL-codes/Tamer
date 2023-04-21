package com.nhncorp.cubridqa.console.bean;

/**
 * 
 * @ClassName: SqlParam
 * @Description: the java bean for sql script parameter .
 * @date 2009-9-4
 * @version V1.0 Copyright (C) www.nhn.com
 */
public class SqlParam {

    private Object value;

    private int type;

    private int index;

    private String paramType;

    public SqlParam(String paramType, int index, Object value, int type) {
        this.index = index;
        this.value = value;
        this.type = type;
        this.paramType = paramType;
    }

    public int getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }

    public int getIndex() {
        return index;
    }

    public String getParamType() {
        return paramType;
    }
}
