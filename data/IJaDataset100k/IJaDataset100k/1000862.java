package javamicroweb.html.impl;

import java.util.List;
import javamicroweb.html.HTMLA;
import javamicroweb.html.HTMLAttribute;
import javamicroweb.html.HTMLInlineElement;
import javamicroweb.html.HTMLTextNode;

public class SimpleHTMLA<C> extends SimpleHTMLInlineElement<C> implements HTMLA<C> {

    private String charSet;

    private String coords;

    private String href;

    private String hrefLang;

    private String name;

    private String rel;

    private String shape;

    private String target;

    private String type;

    private String rev;

    private String accessKey;

    private String tabIndex;

    private String onFocus;

    private String onBlur;

    public String getElementTag() {
        return "a";
    }

    public List<HTMLAttribute<C>> getAttributes() {
        List<HTMLAttribute<C>> list = super.getAttributes();
        if (null != charSet) {
            list.add(new SimpleHTMLAttribute<C>("charset", charSet));
        }
        if (null != coords) {
            list.add(new SimpleHTMLAttribute<C>("coords", coords));
        }
        if (null != href) {
            list.add(new SimpleHTMLAttribute<C>("href", href));
        }
        if (null != hrefLang) {
            list.add(new SimpleHTMLAttribute<C>("hreflang", hrefLang));
        }
        if (null != name) {
            list.add(new SimpleHTMLAttribute<C>("name", name));
        }
        if (null != rel) {
            list.add(new SimpleHTMLAttribute<C>("rel", rel));
        }
        if (null != shape) {
            list.add(new SimpleHTMLAttribute<C>("shape", shape));
        }
        if (null != target) {
            list.add(new SimpleHTMLAttribute<C>("target", target));
        }
        if (null != type) {
            list.add(new SimpleHTMLAttribute<C>("type", type));
        }
        if (null != rev) {
            list.add(new SimpleHTMLAttribute<C>("rev", rev));
        }
        if (null != accessKey) {
            list.add(new SimpleHTMLAttribute<C>("accesskey", accessKey));
        }
        if (null != tabIndex) {
            list.add(new SimpleHTMLAttribute<C>("tabindex", tabIndex));
        }
        if (null != onFocus) {
            list.add(new SimpleHTMLAttribute<C>("onfocus", onFocus));
        }
        if (null != onBlur) {
            list.add(new SimpleHTMLAttribute<C>("onblur", onBlur));
        }
        return list;
    }

    public String getCharSet() {
        return charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getCoords() {
        return coords;
    }

    public void setCoords(String coords) {
        this.coords = coords;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHrefLang() {
        return hrefLang;
    }

    public void setHrefLang(String hrefLang) {
        this.hrefLang = hrefLang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getOnBlur() {
        return onBlur;
    }

    public void setOnBlur(String onBlur) {
        this.onBlur = onBlur;
    }

    public String getOnFocus() {
        return onFocus;
    }

    public void setOnFocus(String onFocus) {
        this.onFocus = onFocus;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
    }

    public String getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(String tabIndex) {
        this.tabIndex = tabIndex;
    }

    public void add(HTMLInlineElement<C> child) {
        addChild(child);
    }

    public void add(HTMLTextNode<C> child) {
        addChild(child);
    }
}
