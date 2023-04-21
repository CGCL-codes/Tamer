package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;

/**
 * @author cesar.reyes
 * Date: 12/11/2008
 * Time: 03:53:28 PM
 */
public class TimeTag extends FieldTag {

    private Integer increment;

    public Integer getIncrement() {
        return increment;
    }

    public void setIncrement(Integer increment) {
        this.increment = increment;
    }

    public String getType() {
        return "timefield";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();
        json.put("fieldLabel", title != null ? title : getText(titleKey));
        json.put("name", getField());
        json.elementOpt("description", getDescription());
        json.elementOpt("value", getValue());
        json.elementOpt("width", Integer.valueOf(width));
        json.elementOpt("increment", increment);
        json.put("validateOnBlur", false);
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("disabled", disabled);
        json.put("xtype", getType());
        return json;
    }

    public String getHtmlDeclaration() {
        StringBuilder sb = new StringBuilder();
        sb.append("<input style=\"width: ").append(getWidth()).append(";\" class=\"x-form-text x-form-field\" size=\"20\" autocomplete=\"off\" id=\"");
        sb.append(getField());
        sb.append("\" name=\"");
        sb.append(getField());
        sb.append("\" type=\"text\">");
        return sb.toString();
    }
}
