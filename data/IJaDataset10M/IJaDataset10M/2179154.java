package org.specrunner.source.resource.element.impl;

import nu.xom.Element;
import org.specrunner.source.ISource;
import org.specrunner.source.resource.EType;
import org.specrunner.source.resource.element.IResourceElement;
import org.specrunner.source.resource.impl.AbstractResource;

public abstract class AbstractResourceElement extends AbstractResource implements IResourceElement {

    private Element element;

    protected AbstractResourceElement(ISource parent, String path, boolean classpath, EType type, Element element) {
        super(parent, path, classpath, type);
        this.element = element;
    }

    @Override
    public Element getElement() {
        return element;
    }

    @Override
    public void setElement(Element element) {
        this.element = element;
    }
}
