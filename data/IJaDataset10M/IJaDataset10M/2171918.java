package org.json.compliance;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.test.compliance.std.DecodeException;
import org.json.test.compliance.std.JSONDecoder;

public class DecoderImpl implements JSONDecoder {

    public Object fromJSONString(String jsonString) throws DecodeException {
        Object value = null;
        try {
            if (jsonString.trim().charAt(0) == '[') value = new JSONArray(jsonString); else value = new JSONObject(jsonString);
        } catch (Exception e) {
            throw new DecodeException(e);
        }
        return Mapper.transformToStd(value);
    }
}
