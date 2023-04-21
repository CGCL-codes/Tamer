package br.ufrgs.inf.prav.interop.jsf;

import br.ufrgs.inf.prav.interop.jsf.components.LinkComponent;
import javax.faces.component.UIComponent;

/**
 *
 * @author Fernando Arena Varella
 * @version 1.0
 */
public class LinkTag extends PravBaseTag {

    private String charset, href, hreflang, media, rel, rev, target, type;

    public LinkTag() {
    }

    @Override
    public String getComponentType() {
        return LinkComponent.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        LinkComponent link = (LinkComponent) component;
        link.setCharset(charset);
        link.setHref(href);
        link.setHreflang(hreflang);
        link.setMedia(media);
        link.setRel(rel);
        link.setRev(rev);
        link.setTarget(target);
        link.setType(type);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getHreflang() {
        return hreflang;
    }

    public void setHreflang(String hreflang) {
        this.hreflang = hreflang;
    }

    public String getMedia() {
        return media;
    }

    public void setMedia(String media) {
        this.media = media;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRev() {
        return rev;
    }

    public void setRev(String rev) {
        this.rev = rev;
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
}
