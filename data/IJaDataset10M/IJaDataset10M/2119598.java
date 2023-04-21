package br.ufrgs.inf.prav.jsf;

import br.ufrgs.inf.prav.jsf.components.ScriptComponent;
import javax.faces.component.UIComponent;

/**
 *
 * @author Fernando
 */
public class ScriptTag extends PravBaseTag {

    private String charset, defer, src, xmlSpace, language, type;

    public ScriptTag() {
    }

    @Override
    public String getComponentType() {
        return ScriptComponent.COMPONENT_TYPE;
    }

    @Override
    public String getRendererType() {
        return null;
    }

    @Override
    protected void setProperties(UIComponent component) {
        super.setProperties(component);
        ScriptComponent script = (ScriptComponent) component;
        script.setCharset(charset);
        script.setDefer(defer);
        script.setLanguage(language);
        script.setSrc(src);
        script.setXmlSpace(xmlSpace);
        script.setType(type);
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getDefer() {
        return defer;
    }

    public void setDefer(String defer) {
        this.defer = defer;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getXmlSpace() {
        return xmlSpace;
    }

    public void setXmlSpace(String xmlSpace) {
        this.xmlSpace = xmlSpace;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
