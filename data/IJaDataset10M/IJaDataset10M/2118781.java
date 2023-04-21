package org.japano.action.parameter;

import java.util.ArrayList;
import java.util.List;
import org.japano.action.ActionMapping;
import org.japano.jasper.compiler.CompilationContext;
import org.japano.jasper.compiler.Node;
import org.japano.jasper.compiler.PageNodeCreator;
import org.japano.jasper.compiler.ServletWriter;
import org.japano.metadata.MetadataTag;
import org.japano.util.Util;
import xjavadoc.XTag;
import org.japano.pagenode.action.FormNode;
import org.japano.metadata.ActionTag;
import org.japano.metadata.Attribute;
import org.japano.metadata.BodyContent;
import org.japano.metadata.Tag;
import org.japano.action.Parameter;

/**
 HTML form submit button
 
 @author Sven Helmberger ( sven dot helmberger at gmx dot de )
 @version $Id: Submit.java,v 1.11 2005/11/26 22:33:38 fforw Exp $
 #SFLOGO# 
 */
public class Submit extends Parameter {

    private static final long serialVersionUID = 0xccce9ed87e74989aL;

    private String submitFunctionName;

    public Submit() {
        setName("submit");
    }

    public List createTags(ActionTag parent) {
        List l = new ArrayList();
        Tag t = new Tag(getTagName(parent.getName()), FormNode.Field.class.getName(), BodyContent.EMPTY, getDescription());
        addStyleAttributes(t);
        t.addAttribute("text", false, true, "java.lang.String", "The text to display on the button.");
        t.addAttribute("image", false, true, "java.lang.String", "The image to display on the button.");
        t.addAttribute("onclick", false, true, "java.lang.String", "javascript to execute when the button is clicked.");
        l.add(t);
        return l;
    }

    public void getCode(org.japano.jasper.compiler.ServletWriter servletWriter, CompilationContext context, String styleId, String styleClass, String style) {
        Node.CustomTag customTag = context.getCustomTag();
        String text = customTag.getAttributeValue("text");
        String image = customTag.getAttributeValue("image");
        String onclick = customTag.getAttributeValue("onclick");
        if (image == null && text == null) {
            throw new RuntimeException("Either text or image must be set.");
        }
        if (image != null && text != null) {
            throw new RuntimeException("Text and image cannot both be set.");
        }
        if (image != null) {
            servletWriter.printin("out.print(\"<input type=\\\"image\\\"");
            appendStyleAttributes(servletWriter, styleId, styleClass, style, context);
            appendAttribute(servletWriter, "src", image, context, String.class);
        } else {
            servletWriter.printin("out.print(\"<input type=\\\"submit\\\"");
            appendStyleAttributes(servletWriter, styleId, styleClass, style, context);
            appendAttribute(servletWriter, "value", text, context, String.class);
        }
        if (onclick != null) {
            servletWriter.print("onclick=\\\"");
            servletWriter.print(onclick);
            servletWriter.print("\\\"");
        }
        servletWriter.println("/>\");");
    }

    public void configure(MetadataTag tag) {
        setName(tag.getAttribute("name"));
    }
}
