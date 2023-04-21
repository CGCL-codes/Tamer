package org.forzaframework.web.servlet.tags.form;

import net.sf.json.JSONObject;
import org.forzaframework.web.servlet.tags.Listener;
import org.forzaframework.web.servlet.tags.JSONFunction;

/**
 * User: Cesar Reyes
 * Date: 29/03/2009
 * Time: 10:09:49 PM
 */
public class ImageTag extends FieldTag {

    private Boolean allowBlank;

    private Integer maxLength;

    private Integer minLength;

    private Boolean enableKeyEvents;

    private String inputType;

    private String src;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Boolean getAllowBlank() {
        return allowBlank;
    }

    public void setAllowBlank(Boolean allowBlank) {
        this.allowBlank = allowBlank;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public Integer getMinLength() {
        return minLength;
    }

    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    public Boolean getEnableKeyEvents() {
        return enableKeyEvents;
    }

    public void setEnableKeyEvents(Boolean enableKeyEvents) {
        this.enableKeyEvents = enableKeyEvents;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getType() {
        return "box";
    }

    public Object toJSON() {
        JSONObject json = new JSONObject();
        if (getTitle() != null || getTitleKey() != null) {
            json.put("fieldLabel", title != null ? title : getText(titleKey));
        }
        json.put("name", this.getField());
        json.elementOpt("value", this.getValue());
        json.elementOpt("disabled", this.getDisabled());
        json.elementOpt("width", getWidth());
        json.elementOpt("autoHeight", getAutoHeight());
        json.elementOpt("anchor", anchor);
        json.elementOpt("description", getDescription());
        json.elementOpt("allowBlank", allowBlank);
        json.elementOpt("renderHidden", hidden);
        json.elementOpt("hideLabel", getHideLabel());
        json.elementOpt("minLength", minLength);
        json.elementOpt("maxLength", maxLength);
        json.elementOpt("labelSeparator", getLabelSeparator());
        json.elementOpt("inputType", "image");
        json.elementOpt("src", src);
        json.element("autoEl", JSONObject.fromObject("{tag: 'img', src: '" + src + "'}"));
        json.put("xtype", getType());
        json.put("enableKeyEvents", enableKeyEvents);
        if (this.listeners.size() > 0) {
            JSONObject listeners = new JSONObject();
            for (Listener listener : this.listeners) {
                listeners.put(listener.getEventName(), new JSONFunction(listener.getHandler()));
            }
            json.put("listeners", listeners);
        }
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
