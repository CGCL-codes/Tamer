package org.japano.action.parameter;

import java.util.ArrayList;
import java.util.List;
import org.japano.Session;
import org.japano.action.InvalidParameterException;
import org.japano.jasper.compiler.CompilationContext;
import org.japano.metadata.MetadataTag;
import xjavadoc.XTag;
import org.japano.action.ActionMapping;
import org.japano.jasper.compiler.PageNodeCreator;
import org.japano.jasper.compiler.ServletWriter;
import org.japano.pagenode.action.FormNode;
import org.japano.metadata.ActionTag;
import org.japano.metadata.Attribute;
import org.japano.metadata.BodyContent;
import org.japano.metadata.Tag;
import org.japano.action.Parameter;

/**
 HTML form input field.
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: TextField.java,v 1.9 2005/11/26 22:33:38 fforw Exp $
 #SFLOGO# 
 */
public class TextField extends Parameter {

    private static final long serialVersionUID = 0x14d0e8cbadc757a7L;

    /**
   Maximum length of the text field.
   @serial
   */
    private int maxLength;

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public List createTags(ActionTag parent) {
        List l = new ArrayList();
        Tag t = new Tag(getTagName(parent.getName()), FormNode.Field.class.getName(), BodyContent.EMPTY, getDescription());
        addStyleAttributes(t);
        t.addAttribute("size", false, true, "int", "Size of the input field.");
        l.add(t);
        return l;
    }

    public void configure(MetadataTag tag) {
        String defaultValue = tag.getAttribute("value");
        if (defaultValue != null) setDefaultValue(defaultValue);
        setName(tag.getAttribute("name"));
        String sMaxLength = tag.getAttribute("maxLength");
        if (sMaxLength == null) tag.error("You must specify a maxLength attribute.");
        maxLength = 0;
        try {
            maxLength = Integer.parseInt(sMaxLength);
        } catch (NumberFormatException e) {
        }
        if (maxLength <= 0) tag.error("Invalid maxLength attribute value.");
    }

    public void getCode(org.japano.jasper.compiler.ServletWriter servletWriter, CompilationContext context, String styleId, String styleClass, String style) {
        servletWriter.printin("out.print(\"<input type=\\\"" + getInputType() + "\\\"");
        appendStyleAttributes(servletWriter, styleId, styleClass, style, context);
        appendAttribute(servletWriter, "value", "\"+" + valueExpression().getCode() + "+\"", null, null);
        String sSize = context.getCustomTag().getAttributeValue("size");
        if (sSize != null) {
            appendAttribute(servletWriter, "size", sSize, context, int.class);
        }
        appendAttribute(servletWriter, "maxLength", String.valueOf(maxLength), null, null);
        appendAttribute(servletWriter, "name", this.getQualifiedName(), null, null);
        servletWriter.println(">\");");
    }

    protected String getInputType() {
        return "text";
    }

    public String toString() {
        return super.toString() + ", maxLength=" + maxLength;
    }

    protected String validate(Session session, String value) throws org.japano.action.InvalidParameterException {
        if (value != null && value.length() > maxLength) {
            throw new InvalidParameterException("value too long");
        }
        return super.validate(session, value);
    }
}
