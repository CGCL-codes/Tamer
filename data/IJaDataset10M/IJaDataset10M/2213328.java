package n2hell.http;

import java.util.HashMap;
import org.json.JSONObject;

public class JsonRpcService extends JSONRPC {

    private final HashMap<String, Object> objectsMap = new HashMap<String, Object>();

    public JsonRpcService(Jsonifer jsonifer) throws Exception {
        super(jsonifer);
    }

    public void register(String objectName, Object object) throws Exception {
        objectsMap.put(objectName, object);
    }

    @SuppressWarnings(value = "unchecked")
    public String generateAPI(String key, String url) throws Exception {
        Object obj = objectsMap.get(key);
        if (obj == null) throw new Exception("unregistered object " + key);
        return super.generateAPI(obj.getClass(), url);
    }

    public JSONRPCResult call(JSONObject jsonReq, String key) throws Exception {
        Object obj = objectsMap.get(key);
        if (obj == null) throw new Exception("unregistered object " + key);
        return super.call(jsonReq, obj);
    }
}
