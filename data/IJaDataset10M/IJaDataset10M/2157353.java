package javamicroweb.html.impl;

import java.util.List;
import javamicroweb.html.HTMLAttribute;
import javamicroweb.html.HTMLInput;

public class SimpleHTMLInput<C> extends SimpleHTMLInlineElement<C> implements HTMLInput<C> {

    private String type;

    private String name;

    private String value;

    private boolean checked;

    private Integer size;

    private Integer maxLength;

    private String src;

    private String alt;

    private String useMap;

    private boolean isMap;

    private boolean disabled;

    private boolean readOnly;

    private String accept;

    private String accessKey;

    private Integer tabIndex;

    private String onFocus;

    private String onBlur;

    private String onSelect;

    private String onChange;

    public String getElementTag() {
        return "input";
    }

    public List<HTMLAttribute<C>> getAttributes() {
        List<HTMLAttribute<C>> list = super.getAttributes();
        if (null != type) {
            list.add(new SimpleHTMLAttribute<C>("type", type));
        }
        if (null != name) {
            list.add(new SimpleHTMLAttribute<C>("name", name));
        }
        if (null != value) {
            list.add(new SimpleHTMLAttribute<C>("value", value));
        }
        if (checked) {
            list.add(new SimpleHTMLAttribute<C>("checked", "true"));
        }
        if (null != size) {
            list.add(new SimpleHTMLAttribute<C>("size", size.toString()));
        }
        if (null != maxLength) {
            list.add(new SimpleHTMLAttribute<C>("maxlength", maxLength.toString()));
        }
        if (null != src) {
            list.add(new SimpleHTMLAttribute<C>("src", src));
        }
        if (null != alt) {
            list.add(new SimpleHTMLAttribute<C>("alt", alt));
        }
        if (null != useMap) {
            list.add(new SimpleHTMLAttribute<C>("usemap", useMap));
        }
        if (isMap) {
            list.add(new SimpleHTMLAttribute<C>("ismap", "true"));
        }
        if (disabled) {
            list.add(new SimpleHTMLAttribute<C>("disabled", "true"));
        }
        if (readOnly) {
            list.add(new SimpleHTMLAttribute<C>("readonly", "true"));
        }
        if (null != accept) {
            list.add(new SimpleHTMLAttribute<C>("accept", accept));
        }
        if (null != accessKey) {
            list.add(new SimpleHTMLAttribute<C>("accesskey", accessKey));
        }
        if (null != tabIndex) {
            list.add(new SimpleHTMLAttribute<C>("tabindex", tabIndex.toString()));
        }
        if (null != onFocus) {
            list.add(new SimpleHTMLAttribute<C>("onfocus", onFocus));
        }
        if (null != onBlur) {
            list.add(new SimpleHTMLAttribute<C>("onblur", onBlur));
        }
        if (null != onSelect) {
            list.add(new SimpleHTMLAttribute<C>("onselect", onSelect));
        }
        if (null != onChange) {
            list.add(new SimpleHTMLAttribute<C>("onchange", onChange));
        }
        return list;
    }

    public String getAccept() {
        return accept;
    }

    public void setAccept(String accept) {
        this.accept = accept;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getAlt() {
        return alt;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isMap() {
        return isMap;
    }

    public void setMap(boolean isMap) {
        this.isMap = isMap;
    }

    public Integer getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOnBlur() {
        return onBlur;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOnChange() {
        return onChange;
    }

    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    public String getOnFocus() {
        return onFocus;
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getOnSelect() {
        return onSelect;
    }

    public void setOnSelect(String onSelect) {
        this.onSelect = onSelect;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public Integer getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(Integer tabIndex) {
        this.tabIndex = tabIndex;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUseMap() {
        return useMap;
    }

    public void setUseMap(String useMap) {
        this.useMap = useMap;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
