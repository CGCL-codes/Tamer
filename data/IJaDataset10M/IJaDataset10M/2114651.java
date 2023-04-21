package org.zkoss.zkmax.zul.render;

import java.io.IOException;
import java.io.Writer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;
import org.zkoss.zul.Bandpopup;

/**
 * 
 * {@link Bandpopup}'s default mold.
 * 
 * @author jumperchen
 * @since 3.0.0
 */
public class BandpopupDefault implements ComponentRenderer {

    public void render(Component comp, Writer out) throws IOException {
        final SmartWriter wh = new SmartWriter(out);
        final Bandpopup self = (Bandpopup) comp;
        final String uuid = self.getUuid();
        wh.write("<div id=\"").write(uuid).write('"');
        wh.write(self.getOuterAttrs()).write(self.getInnerAttrs()).write(">");
        wh.writeChildren(self);
        wh.write("</div>");
    }
}
