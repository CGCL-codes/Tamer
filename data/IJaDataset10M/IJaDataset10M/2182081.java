package com.smb.MMUtil.testcase.framework;

import java.util.HashMap;
import java.util.Map;

public class RequestDataSource {

    public static Map<String, String> getRequestDataSourceE() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("name", "sdfsdfsdfsdf");
        map.put("id", "123456");
        map.put("address", "asdsadasddddddddddddddddddd");
        return map;
    }

    public static Map<String, String[]> getRequestDataSourceM() {
        String nameValues[] = { "username" };
        String idValues[] = { "1" };
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("name", nameValues);
        map.put("id", idValues);
        return map;
    }
}
