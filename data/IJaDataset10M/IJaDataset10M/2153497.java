package org.japano.action.parameter;

import java.util.ArrayList;
import java.util.List;
import org.japano.Session;
import org.japano.action.ActionMapping;
import org.japano.action.InvalidParameterException;
import org.japano.jasper.compiler.CompilationContext;
import org.japano.jasper.compiler.Node;
import org.japano.jasper.compiler.ServletWriter;
import org.japano.metadata.MetadataTag;
import org.japano.pagenode.action.FormNode;
import org.japano.metadata.BodyContent;
import xjavadoc.XTag;
import org.japano.metadata.ActionTag;
import org.japano.metadata.Tag;
import org.japano.action.Parameter;

/**
 HTML form textarea 
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: TextArea.java,v 1.11 2005/11/26 22:33:38 fforw Exp $
 #SFLOGO# 
 */
public class TextArea extends Parameter {

    private static final long serialVersionUID = 0x59e9d6efbe41be31L;

    /**
   Maximum length of the text in the text area.
   @serial
   */
    private int maxLength;

    public List createTags(ActionTag parent) {
        List l = new ArrayList();
        Tag t = new Tag(getTagName(parent.getName()), FormNode.Field.class.getName(), BodyContent.EMPTY, getDescription());
        addStyleAttributes(t);
        t.addAttribute("cols", true, true, "int", getColsDescription());
        t.addAttribute("rows", true, true, "int", getRowsDescription());
        l.add(t);
        return l;
    }

    protected String getColsDescription() {
        return "number of colums in the text area";
    }

    protected String getRowsDescription() {
        return "number of rows in the text area";
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
        Node.CustomTag customTag = context.getCustomTag();
        String cols = customTag.getAttributeValue("cols");
        String rows = customTag.getAttributeValue("rows");
        servletWriter.printin("out.print(\"<textarea");
        appendStyleAttributes(servletWriter, styleId, styleClass, style, context);
        appendAttribute(servletWriter, "name", this.getQualifiedName(), null, null);
        appendAttribute(servletWriter, "cols", cols, context, int.class);
        appendAttribute(servletWriter, "rows", rows, context, int.class);
        servletWriter.print(">\"+");
        servletWriter.print(valueExpression().getCode());
        servletWriter.println("+\"</textarea>\");");
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
