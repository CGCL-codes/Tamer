package in.co.codedoc.json.mapper;

import in.co.codedoc.cg.annotations.IsAJSONMapper;
import in.co.codedoc.json.JSONDecoderMethod;
import in.co.codedoc.json.JSONEncoderMethod;
import in.co.codedoc.json.JSONLongValue;
import in.co.codedoc.json.JSONValue;
import in.co.codedoc.sql.IDBase;
import java.lang.reflect.Method;

@IsAJSONMapper
public class IDBaseJSONMapper {

    @JSONEncoderMethod
    public static JSONLongValue Encode(IDBase id) {
        return new JSONLongValue(id.GetId());
    }

    @SuppressWarnings("unchecked")
    @JSONDecoderMethod
    public static IDBase Decode(JSONValue json, Class desiredType) {
        if (json == null) {
            return null;
        }
        try {
            Method createMethod = desiredType.getMethod("Create", new Class[] { Long.TYPE });
            return (IDBase) createMethod.invoke(null, new Object[] { new JSONLongValue(json).GetValue() });
        } catch (Throwable th) {
            throw new RuntimeException("Exception while creating ID Object:" + th.getMessage(), th);
        }
    }
}
