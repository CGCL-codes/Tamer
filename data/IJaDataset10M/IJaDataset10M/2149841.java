package com.cateshop.def.attribute.display;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import com.cateshop.def.HTMLSupport;
import com.cateshop.def.attribute.storage.AnyAttributeDefinition;
import com.cateshop.exe.attribute.AnyAttribute;

/**
 * {@link DisplayAny}�Ļ�ʵ��.
 * 
 * @author notXX
 */
public abstract class DisplayAnyImpl extends HTMLSupport implements DisplayAny {

    /**
     * {@inheritDoc}
     */
    public final void renderDisplay(PageContext pageContext, AnyAttributeDefinition definition, AnyAttribute attribute) throws IOException, JspException {
        if (pageContext == null) throw new IllegalArgumentException("pageContext");
        if (definition == null) throw new IllegalArgumentException("definition");
        if (attribute == null) throw new IllegalArgumentException("attribute");
        renderDisplay0(pageContext, definition, attribute);
    }

    /**
     * �����ֵ���Ĵ��?��.
     * 
     * @param pageContext
     * @param definition
     * @param attribute
     * @throws IOException
     * @throws JspException
     */
    protected abstract void renderDisplay0(PageContext pageContext, AnyAttributeDefinition definition, AnyAttribute attribute) throws IOException, JspException;
}
