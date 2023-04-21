package org.nakedobjects.nos.client.web.html;

import java.io.PrintWriter;
import org.nakedobjects.nos.client.web.component.Component;

public class ErrorMessage implements Component {

    private final Exception e;

    private final boolean isDebug;

    public ErrorMessage(Exception e, boolean isDebug) {
        this.e = e;
        this.isDebug = isDebug;
    }

    public void write(PrintWriter writer) {
        writer.println("<div class=\"error\">");
        writer.println(e.getMessage());
        writer.println("</div>");
        if (isDebug) {
            writer.println("<pre class=\"error-trace\">");
            e.printStackTrace(writer);
            writer.println("</pre>");
        }
    }
}
