package org.jtools.siterenderer;

import org.jtools.elements.XMLRenderer;
import org.jtools.siterenderer.layout.LayoutGroup;
import org.jtools.siterenderer.layout.LayoutElement;
import org.jtools.siterenderer.Renderable;

@org.jtools.tmpl.Templet(execute = "execute", output = "html")
public class Document_Htmltmpl extends org.jtools.siterenderer.AbstractDocumentWriter {

    public void execute(java.io.PrintWriter out) {
        out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
        out.println("<html>");
        out.println("  <head>");
        if (getDocument().getTitle() != null) {
            out.print("    <title>");
            out.print(java.lang.String.valueOf(getDocument().getTitle()));
            out.println("</title>");
        }
        if (getDocument().getHead() != null) {
            for (org.jtools.elements.Child e : getDocument().getHead().getChildElements()) e.accept(new XMLRenderer("", "    "), out);
        }
        out.println("    <style type=\"text/css\" media=\"all\">");
        java.util.Set<String> styles = new java.util.HashSet<String>();
        for (LayoutGroup group : getDocument().getLayout().getLayoutGroups()) {
            if (getDocument().contains(group)) {
                for (LayoutElement element : group.getLayoutElements()) {
                    Renderable data = getDocument().get(element);
                    if (data != null && element.getStyle() != null && styles.add(element.getStyle())) {
                        out.print("      @import url(\"");
                        out.print(java.lang.String.valueOf(getDocument().resolveHref(element.getStyle())));
                        out.println("\");");
                    }
                }
                if (group.getStyle() != null && styles.add(group.getStyle())) {
                    out.print("      @import url(\"");
                    out.print(java.lang.String.valueOf(getDocument().resolveHref(group.getStyle())));
                    out.println("\");");
                }
            }
        }
        if (styles.add("/css/layout.css")) {
            out.print("      @import url(\"");
            out.print(java.lang.String.valueOf(getDocument().resolveHref("/css/layout.css")));
            out.println("\");");
        }
        if (styles.add("/css/site.css")) {
            out.print("      @import url(\"");
            out.print(java.lang.String.valueOf(getDocument().resolveHref("/css/site.css")));
            out.println("\");");
        }
        out.println("    </style>");
        out.print("    <link rel=\"stylesheet\" href=\"");
        out.print(java.lang.String.valueOf(getDocument().resolveHref("/css/print.css")));
        out.println("\" type=\"text/css\" media=\"print\" />");
        out.println("    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\" />");
        out.println("    </head>");
        out.println("<body>");
        for (LayoutGroup group : getDocument().getLayout().getLayoutGroups()) {
            if (getDocument().contains(group)) {
                out.print("<div");
                out.print(java.lang.String.valueOf(group.toClassLiteral()));
                out.print(java.lang.String.valueOf(group.toIdLiteral()));
                out.println(">");
                for (LayoutElement element : group.getLayoutElements()) {
                    Renderable data = getDocument().get(element);
                    if (data != null) {
                        out.print("<div");
                        out.print(java.lang.String.valueOf(element.toClassLiteral()));
                        out.print(java.lang.String.valueOf(element.toIdLiteral()));
                        out.println(">");
                        data.render(element, out);
                        out.println("</div>");
                    }
                }
                out.println("</div>");
                out.println("<div id=\"clear\"></div>");
            }
        }
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
}
