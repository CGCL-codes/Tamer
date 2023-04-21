package mortiforo.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServlet;
import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;

/**
 * Process text with html and remove possible
 * malicious tags and attributes.
 * 
 * @author Rafael Steil (JForum project)
 * 
 *  */
public class ValidateHtml extends HttpServlet {

    private Set welcomeTags;

    public ValidateHtml(String validationfield) {
        this.welcomeTags = new HashSet();
        String valid_html = null;
        if (validationfield.equals("subject")) {
            valid_html = "";
        } else valid_html = "u, a, img, i, li, ul, font, br, p, b, tt, ol, pre, span".toUpperCase();
        String[] tags = valid_html.split(",");
        for (int i = 0; i < tags.length; i++) {
            this.welcomeTags.add(tags[i].trim());
        }
    }

    private String processAllNodes(String contents) throws Exception {
        StringBuffer sb = new StringBuffer(512);
        Lexer lexer = new Lexer(contents);
        Node node;
        while ((node = lexer.nextNode()) != null) {
            if (this.isTagWelcome(node)) {
                sb.append(node.toHtml());
            } else {
                sb.append(node.toHtml().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
            }
        }
        return sb.toString();
    }

    private boolean isTagWelcome(Node node) {
        if (node instanceof TextNode) {
            return true;
        }
        Tag tag = (Tag) node;
        if (!this.welcomeTags.contains(tag.getTagName())) {
            return false;
        }
        this.checkAndValidateAttributes(tag);
        return true;
    }

    private void checkAndValidateAttributes(Tag tag) {
        Vector newAttributes = new Vector();
        for (Iterator iter = tag.getAttributesEx().iterator(); iter.hasNext(); ) {
            Attribute a = (Attribute) iter.next();
            String name = a.getName();
            if (name != null) {
                name = name.toLowerCase();
                if (("href".equals(name) || "src".equals(name)) && a.getValue() != null) {
                    if (a.getValue().toLowerCase().indexOf("javascript:") > -1) {
                        a.setValue("#");
                    } else if (a.getValue().indexOf("&#") > -1) {
                        a.setValue(a.getValue().replaceAll("&#", "&amp;#"));
                    }
                    newAttributes.add(a);
                } else if (!name.startsWith("on") && !name.startsWith("style")) {
                    newAttributes.add(a);
                }
            } else {
                newAttributes.add(a);
            }
        }
        tag.setAttributesEx(newAttributes);
    }

    public static String makeSafe(String contents, String validationfield) {
        if (contents == null || contents.trim().length() == 0) {
            return contents;
        }
        try {
            contents = new ValidateHtml(validationfield).processAllNodes(contents);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return contents;
    }
}
