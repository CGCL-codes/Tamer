package org.nakedobjects.nos.client.web.html;

import org.nakedobjects.nos.client.web.component.Component;
import java.io.PrintWriter;

final class Checkbox implements Component {

    private final boolean set;

    private final boolean editable;

    Checkbox(final boolean set, final boolean editable) {
        this.set = set;
        this.editable = editable;
    }

    public void write(final PrintWriter writer) {
        writer.print("<input class=\"value\" type=\"checkbox\"");
        if (set) {
            writer.print(" checked");
        }
        if (!editable) {
            writer.print(" disabled");
        }
        writer.println("/>");
    }
}
