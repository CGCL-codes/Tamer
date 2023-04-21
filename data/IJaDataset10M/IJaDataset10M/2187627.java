package com.softwarementors.extjs.djn.router.processor.standard.json;

import com.google.gson.JsonArray;
import com.softwarementors.extjs.djn.router.TransferType;
import com.softwarementors.extjs.djn.router.processor.standard.StandardRequestData;

public class JsonRequestData extends StandardRequestData {

    public static final String ACTION_ELEMENT = "action";

    public static final String METHOD_ELEMENT = "method";

    public static final String TID_ELEMENT = "tid";

    public static final String TYPE_ELEMENT = "type";

    public static final String DATA_ELEMENT = "data";

    private JsonArray jsonData;

    JsonRequestData(String type, String action, String method, Long tid, JsonArray jsonData) {
        super(type, action, method, tid);
        assert type.equals(TransferType.RPC);
        this.jsonData = jsonData;
    }

    public JsonArray getJsonData() {
        return this.jsonData;
    }
}
