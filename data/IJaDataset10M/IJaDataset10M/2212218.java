package com.google.gdt.eclipse.designer.model.module;

/**
 * @author scheglov_ke
 * @coverage gwt.model.module
 */
public class PublicElement extends AbstractModuleElement {

    public PublicElement() {
        super("public");
    }

    public void setPath(String path) {
        setAttribute("path", path);
    }

    public String getPath() {
        return getAttribute("path");
    }
}
