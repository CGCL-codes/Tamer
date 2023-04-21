package org.cubicunit.internal.selenium;

import java.util.HashMap;
import java.util.Map;
import net.sf.json.JSONObject;
import org.cubicunit.internal.CubicElement;

public class SeleniumCubicElement extends CubicElement {

    public String createJavaScript() {
        Map<String, Object> result = new HashMap<String, Object>();
        if (tagName != null) {
            result.put("tagName", tagName);
            result.put("tagNameProb", tagNameProb);
        }
        if (id != null) {
            result.put("id", id);
            result.put("idProb", idProb);
        }
        if (name != null) {
            result.put("name", name);
            result.put("nameProb", nameProb);
        }
        if (path != null) {
            result.put("path", path);
            result.put("pathProb", pathProb);
        }
        if (value != null) {
            result.put("value", value);
            result.put("valueProb", valueProb);
        }
        if (label != null) {
            result.put("label", label);
            result.put("labelProb", labelProb);
        }
        if (src != null) {
            result.put("src", src);
            result.put("srcProp", srcProb);
        }
        if (href != null) {
            result.put("href", href);
            result.put("hrefProb", hrefProb);
        }
        if (multiple != null) {
            result.put("multiple", multiple);
            result.put("multipleProb", multipleProb);
        }
        if (type != null) {
            result.put("type", type);
            result.put("typeProb", typeProb);
        }
        if (text != null) {
            result.put("text", text);
            result.put("textProb", textProb);
        }
        if (selected != null) {
            result.put("selected", selected);
            result.put("selectedProb", selectedProb);
        }
        if (title != null) {
            result.put("title", title);
            result.put("titleProb", titleProb);
        }
        if (index != null) {
            result.put("index", index);
            result.put("indexProb", indexProb);
        }
        return JSONObject.fromObject(result).toString();
    }
}
