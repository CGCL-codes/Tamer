package com.guanghua.brick.db;

import java.util.Map;

/**
 * 用于修改query result的接口
 */
public interface QueryResultRowListener {

    public Object modifyValue(Map<String, String> row);

    public String modifyKey();
}
