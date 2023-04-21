package com.germinus.xpression.cms.taglib;

import javax.servlet.jsp.JspException;
import com.germinus.xpression.cms.directory.DirectoryItem;

public class IsDraftContentTag extends BasicEmptyBodyTag {

    /**
     * 
     */
    private static final long serialVersionUID = -2003037996715941974L;

    private Object directoryItem;

    public IsDraftContentTag() {
        super();
    }

    protected Object buildVarValue() throws JspException {
        DirectoryItem item;
        if (directoryItem instanceof DirectoryItem) item = (DirectoryItem) directoryItem; else if (directoryItem instanceof String) item = (DirectoryItem) evaluateExpressionLanguage((String) directoryItem, "directoryItem"); else throw new JspException("Class not valid for directory item: " + directoryItem.getClass());
        boolean isContent = item.isDraft();
        return new Boolean(isContent);
    }

    public Object getDirectoryItem() {
        return directoryItem;
    }

    public void setDirectoryItem(Object directoryItem) {
        this.directoryItem = directoryItem;
    }
}
