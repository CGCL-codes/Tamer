package com.taobao.api.model;

import java.util.ArrayList;
import java.util.List;
import com.taobao.api.json.JSONArray;
import com.taobao.api.json.JSONException;
import com.taobao.api.json.JSONObject;

/**
 * 代表一个基本的产品统计属性结果
 * 
 * @author hukui
 * 
 */
public class ProductProp extends TaobaoModel {

    private static final long serialVersionUID = -3089655627874861592L;

    private static final String PID = "pid";

    private static final String CID = "cid";

    private static final String PROP_NAME = "prop_name";

    private static final String PROP_VALUES = "prop_values";

    private static final String PROP_NAMES = "prop_names";

    private String pid;

    private String cid;

    private String prop_name;

    private List<String> propValues = new ArrayList<String>();

    private List<String> propNames = new ArrayList<String>();

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getProp_name() {
        return prop_name;
    }

    public void setProp_name(String prop_name) {
        this.prop_name = prop_name;
    }

    public List<String> getPropValues() {
        return propValues;
    }

    public void setPropValues(List<String> propValues) {
        this.propValues = propValues;
    }

    public List<String> getPropNames() {
        return propNames;
    }

    public void setPropNames(List<String> propNames) {
        this.propNames = propNames;
    }

    /**
	 * 把JSON格式转成ProductProp对象
	 * 
	 * @param json
	 * @return
	 * @throws JSONException
	 */
    public static ProductProp convertJsonToObject(JSONObject json) throws JSONException {
        ProductProp prop = new ProductProp();
        if (json == null) {
            return prop;
        }
        if (json.has(ProductProp.CID)) {
            prop.setCid(json.getString(ProductProp.CID));
        }
        if (json.has(ProductProp.PID)) {
            prop.setPid(json.getString(ProductProp.PID));
        }
        if (json.has(ProductProp.PROP_NAME)) {
            prop.setProp_name(json.getString(ProductProp.PROP_NAME));
        }
        if (json.has(ProductProp.PROP_NAMES)) {
            JSONArray array = json.getJSONArray(ProductProp.PROP_NAMES);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    prop.getPropNames().add(array.getString(i));
                }
            }
        }
        if (json.has(ProductProp.PROP_VALUES)) {
            JSONArray array = json.getJSONArray(ProductProp.PROP_VALUES);
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    prop.getPropValues().add(array.getString(i));
                }
            }
        }
        return prop;
    }
}
