package org.geogurus.cartoweb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Utility class for cartoweb objects 
 * @author nicolas
 */
public class CartowebUtil {

    /** this class message, set during a Utility method call (like Layer building for instance) */
    private String message;

    public CartowebUtil() {
    }

    /**
     * Builds a CartowebLayer from given json representation, setting all children
     * layers found in the structure.
     * @see CartowebLayer.getExtTreeJson() for expected json structure
     * @param json the json structure describing the layers to build (typically coming
     * from an Ext TreePanel representing the layers
     * @return a CartowebLayer or null if structure is invalid. see message in this case.
     * 
     */
    public CartowebLayer buildLayerFromJson(String json) {
        if (json == null) {
            message = "buildLayerFromJson: null input json structure";
            return null;
        }
        try {
            JSONObject jsonObj = new JSONObject(json);
            if (jsonObj.getString("id") == null) {
                message = "missing required id attribute in first object";
                return null;
            }
            return buildLayerFromJson(jsonObj);
        } catch (JSONException jse) {
            jse.printStackTrace();
            message = jse.getMessage();
            return null;
        }
    }

    /**
     * Builds a CartowebLayer from given json representation, setting all children
     * layers found in the structure.
     * @see CartowebLayer.getExtTreeJson() for expected json structure
     * @param json the json structure describing the layers to build (typically coming
     * from an Ext TreePanel representing the layers, after a JSONObject was built with this string)
     * @return a CartowebLayer or null if structure is invalid. see message in this case.
     * 
     */
    public CartowebLayer buildLayerFromJson(JSONObject jsonLayer) {
        if (jsonLayer == null) {
            message = "buildLayerFromJson: null input json Object";
            return null;
        }
        CartowebLayer layer = null;
        try {
            String id = jsonLayer.optString("id");
            if (id.length() == 0) {
                message = "missing required id attribute in first object";
                return null;
            }
            if ("cw_layer_tree".equals(id)) {
                JSONArray children = jsonLayer.optJSONArray("children");
                if (children == null || children.length() != 1 || children.getJSONObject(0) == null || !children.getJSONObject(0).optString("id").equals("root")) {
                    message = "invalid json strucutre: cannot find a children with id=root";
                    return null;
                }
                jsonLayer = children.getJSONObject(0);
                id = "root";
            }
            layer = new CartowebLayer(id);
            JSONObject cwAttributes = jsonLayer.optJSONObject("cwattributes");
            if (cwAttributes == null) {
                message = "missing valid cwattributes in current object (id: " + id + ")";
                return null;
            }
            layer.setClassName(cwAttributes.optString("className"));
            layer.setMsLayer(cwAttributes.optString("msLayer"));
            layer.setLabel(cwAttributes.optString("label"));
            layer.setIcon(cwAttributes.optString("icon"));
            layer.setLink(cwAttributes.optString("link"));
            layer.setChildren(cwAttributes.optString("children"));
            layer.setSwitchId(cwAttributes.optString("switchId"));
            layer.setAggregate(cwAttributes.optBoolean("aggregate"));
            layer.setRendering(cwAttributes.optString("rendering"));
            layer.setMdMinScale(cwAttributes.optString("mdMinScale"));
            layer.setMdMaxScale(cwAttributes.optString("mdMaxScale"));
            JSONArray children = jsonLayer.optJSONArray("children");
            if (children != null) {
                for (int i = 0; i < children.length(); i++) {
                    CartowebLayer l = buildLayerFromJson(children.getJSONObject(i));
                    if (l != null) {
                        layer.addLayer(l);
                    }
                }
            } else {
            }
        } catch (JSONException jse) {
            jse.printStackTrace();
            message = jse.getMessage();
            return null;
        }
        return layer;
    }

    public String getMessage() {
        return message;
    }
}
