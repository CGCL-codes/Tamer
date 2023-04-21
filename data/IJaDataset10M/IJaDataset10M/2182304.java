package dr.app.oldbeauti;

import dr.util.Attribute;
import java.io.Writer;

/**
 * @author			Alexei Drummond
 * @version			$Id: XMLWriter.java,v 1.3 2005/06/27 21:18:40 rambaut Exp $
 */
public class XMLWriter extends java.io.PrintWriter {

    int level = 0;

    public XMLWriter(Writer writer) {
        super(writer);
    }

    public void increaseLevel() {
        level += 1;
    }

    public void decreaseLevel() {
        level -= 1;
    }

    public void writeComment(String comment) {
        writeComment(comment, 80);
    }

    public void writeComment(String comment, int length) {
        StringBuffer buffer = new StringBuffer("<!-- ");
        buffer.append(comment);
        for (int i = buffer.length(); i < (length - 3); i++) {
            buffer.append(' ');
        }
        buffer.append("-->");
        writeText(buffer.toString());
    }

    public void writeOpenTag(String tagname) {
        writeText("<" + tagname + ">");
        increaseLevel();
    }

    public void writeOpenTag(String tagname, Attribute attribute) {
        writeTag(tagname, new Attribute[] { attribute }, false);
    }

    public void writeOpenTag(String tagname, Attribute[] attributes) {
        writeTag(tagname, attributes, false);
    }

    public void writeTag(String tagname, Attribute attribute, boolean close) {
        writeTag(tagname, new Attribute[] { attribute }, close);
    }

    public void writeTag(String tagname, Attribute[] attributes, boolean close) {
        StringBuffer buffer = new StringBuffer("<");
        buffer.append(tagname);
        for (Attribute attribute : attributes) {
            buffer.append(' ');
            buffer.append(attribute.getAttributeName());
            buffer.append("=\"");
            buffer.append(attribute.getAttributeValue());
            buffer.append("\"");
        }
        if (close) {
            buffer.append("/");
        }
        buffer.append(">");
        writeText(buffer.toString());
        if (!close) {
            increaseLevel();
        }
    }

    public void writeTag(String tagname, Attribute[] attributes, String content, boolean close) {
        StringBuffer buffer = new StringBuffer("<");
        buffer.append(tagname);
        for (Attribute attribute : attributes) {
            buffer.append(' ');
            buffer.append(attribute.getAttributeName());
            buffer.append("=\"");
            buffer.append(attribute.getAttributeValue());
            buffer.append("\"");
        }
        if (content != null) {
            buffer.append(">");
            buffer.append(content);
            if (close) {
                buffer.append("</");
                buffer.append(tagname);
            }
        } else if (close) {
            buffer.append("/");
        }
        buffer.append(">");
        writeText(buffer.toString());
        if (!close) {
            increaseLevel();
        }
    }

    public void writeCloseTag(String tagname) {
        decreaseLevel();
        writeText("</" + tagname + ">");
    }

    public void writeText(String string) {
        for (int i = 0; i < level; i++) {
            write('\t');
        }
        println(string);
    }
}
