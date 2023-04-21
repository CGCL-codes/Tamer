package org.adapit.wctoolkit.models.util;

public interface TaggedValueInterceptor {

    public void setOwningElement(org.adapit.wctoolkit.uml.ext.core.IElement owningElement);

    public org.adapit.wctoolkit.uml.ext.core.IElement getOwningElement();

    public void setTagName(String tagName);

    public String getTagName();

    public void convert(String tagName, Object content);
}
