package ee.webAppToolkit.json;

import com.google.gson.Gson;
import ee.webAppToolkit.core.expert.impl.DefaultResult;

public class JsonResult extends DefaultResult {

    private static String _createJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    public JsonResult(Object object) {
        super("application/json", _createJson(object), true);
    }
}
