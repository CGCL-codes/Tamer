package org.nakedobjects.viewer.web.html;

import java.io.PrintWriter;

public class Page extends CompositeComponent {

    private String title;

    public void setTitle(String title) {
        this.title = title;
    }

    public void add(String text) {
        add(new TextBlock(text));
    }

    public void write(PrintWriter writer) {
        writer.println("<html>");
        writer.println("  <head>");
        writer.print("  <title>");
        writer.print(title);
        writer.println("</title>");
        writer.println("  <link href=\"./style.css\" rel=\"STYLESHEET\"/>");
        writer.println("  </head>");
        writer.println("  <body>");
        super.write(writer);
        writer.println("  </body>");
        writer.println("</html>");
    }
}
