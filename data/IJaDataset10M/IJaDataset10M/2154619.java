package org.hoydaa.codesnippet.core.filter.docfrag;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import org.hoydaa.codesnippet.core.ParserConstants;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * 
 * @author Utku Utkan
 */
public class DebugFilter extends DocumentFragmentFilter {

    private Class<? extends ParserConstants> parserConstantsClass;

    private Map<Integer, String> tokenKindLabels;

    private String tagClass;

    private String tagStyle;

    public DebugFilter(Class<? extends ParserConstants> parserConstantsClass) {
        this.parserConstantsClass = parserConstantsClass;
        tokenKindLabels = new HashMap<Integer, String>();
    }

    public String getTokenKindLabel(int tokenKind) {
        return tokenKindLabels.get(tokenKind);
    }

    public void addTokenKindLabel(int tokenKind, String tokenKindLabel) {
        tokenKindLabels.put(tokenKind, tokenKindLabel);
    }

    public void removeTokenKindLabel(int tokenKind) {
        tokenKindLabels.remove(tokenKind);
    }

    public String getTagClass() {
        return tagClass;
    }

    public void setTagClass(String tagClass) {
        this.tagClass = tagClass;
    }

    public String getTagStyle() {
        return tagStyle;
    }

    public void setTagStyle(String tagStyle) {
        this.tagStyle = tagStyle;
    }

    protected void run(DocumentFragment docFrag, int tokenKind, String tokenImage) {
        Node node = docFrag.getFirstChild();
        if (node.getNodeType() != Node.ELEMENT_NODE) {
            return;
        }
        StringBuilder title = new StringBuilder();
        title.append(findTokenKindLabel(tokenKind));
        title.append(" (");
        title.append(tokenKind);
        title.append(")");
        Element element = (Element) node;
        element.setAttribute("title", title.toString());
        if (tagClass != null) {
            element.setAttribute("onmouseover", onmouseoverClassJS(tagClass));
            element.setAttribute("onmouseout", onmouseoutClassJS(tagClass));
        }
        if (tagStyle != null) {
            element.setAttribute("onmouseover", onmouseoverStyleJS(tagStyle));
            element.setAttribute("onmouseout", onmouseoutStyleJS(tagStyle));
        }
    }

    private String findTokenKindLabel(int tokenKind) {
        Field[] fields = parserConstantsClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                if (field.getType().getName().equals("int") && field.getInt(null) == tokenKind) {
                    return field.getName();
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        String tokenKindLabel = getTokenKindLabel(tokenKind);
        if (tokenKindLabel != null) {
            return tokenKindLabel;
        }
        return "?";
    }

    private String onmouseoverClassJS(String tagClass) {
        StringBuilder js = new StringBuilder();
        js.append("this.className += ' ");
        js.append(tagClass);
        js.append("';");
        return js.toString();
    }

    private String onmouseoutClassJS(String tagClass) {
        StringBuilder js = new StringBuilder();
        js.append("this.className = this.className.substring(0, this.className.length - ");
        js.append(tagClass.length());
        js.append(");");
        return js.toString();
    }

    private String onmouseoverStyleJS(String tagStyle) {
        StringBuilder js = new StringBuilder();
        js.append("this.style.cssText += ' ");
        js.append(tagStyle);
        js.append("';");
        return js.toString();
    }

    private String onmouseoutStyleJS(String tagStyle) {
        StringBuilder js = new StringBuilder();
        js.append("this.style.cssText = this.style.cssText.substring(0, this.style.cssText.length - ");
        js.append(tagStyle.length());
        js.append(");");
        return js.toString();
    }
}
