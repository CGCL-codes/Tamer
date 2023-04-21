package nuts.exts.json.processors;

import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import nuts.core.util.Query;

/**
 */
public class QueryValueProcessor implements JsonValueProcessor {

    /**
	 * Constructor
	 */
    public QueryValueProcessor() {
    }

    /**
	 * @see net.sf.json.processors.JsonValueProcessor#processArrayValue(java.lang.Object, net.sf.json.JsonConfig)
	 */
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return value;
    }

    /**
	 * @see net.sf.json.processors.JsonValueProcessor#processObjectValue(java.lang.String, java.lang.Object, net.sf.json.JsonConfig)
	 */
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        if (value == null) {
            return JSONNull.getInstance();
        }
        if (!(value instanceof Query)) {
            throw new IllegalArgumentException();
        }
        Query query = (Query) value;
        JSONObject jo = new JSONObject();
        jo.put("n", query.getN());
        jo.put("k", query.getK());
        jo.put("fs", JSONObject.fromObject(query.getFilters(), jsonConfig));
        return jo;
    }
}
