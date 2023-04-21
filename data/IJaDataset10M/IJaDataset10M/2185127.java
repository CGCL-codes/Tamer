package org.snipsnap.render.macro.list;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Iterator;

/**
 * Simple list formatter.
 *
 * @author Matthias L. Jugel
 * @version $Id: SimpleList.java 1609 2004-05-17 10:56:18Z leo $
 */
public class SimpleList implements ListFormatter {

    public String getName() {
        return "simple";
    }

    public void format(Writer writer, Linkable current, String listComment, Collection c, String emptyText, boolean showSize) throws IOException {
        writer.write("<div class=\"list\"><div class=\"list-title\">");
        writer.write(listComment);
        if (showSize) {
            writer.write(" (");
            writer.write("" + c.size());
            writer.write(")");
        }
        writer.write("</div>");
        if (c.size() > 0) {
            writer.write("<blockquote>");
            Iterator nameIterator = c.iterator();
            while (nameIterator.hasNext()) {
                Object object = nameIterator.next();
                if (object instanceof Linkable) {
                    writer.write(((Linkable) object).getLink());
                } else if (object instanceof Nameable) {
                    writer.write(((Nameable) object).getName());
                } else {
                    writer.write(object.toString());
                }
                if (nameIterator.hasNext()) {
                    writer.write(", ");
                }
            }
            writer.write("</blockquote>");
        } else {
            writer.write(emptyText);
        }
        writer.write("</div>");
    }
}
