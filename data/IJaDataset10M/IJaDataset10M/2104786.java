package org.zkoss.zkmax.zul.render;

import java.io.IOException;
import java.io.Writer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;
import org.zkoss.zk.ui.render.Out;
import org.zkoss.zul.Treecol;

/**
 * {@link Treecol}'s default mold.
 * 
 * @author robbiecheng
 * 
 * @since 3.0.0
 */
public class TreecolDefault implements ComponentRenderer {

    public void render(Component comp, Writer out) throws IOException {
        final SmartWriter wh = new SmartWriter(out);
        final Treecol self = (Treecol) comp;
        wh.write("<th id=\"").write(self.getUuid()).write("\" z.type=\"Tcol\"").write(self.getOuterAttrs()).write(self.getInnerAttrs()).write("><div id=\"").write(self.getUuid()).write("!cave\" class=\"").write(self.getZclass()).write("-cnt\">").write(self.getImgTag());
        new Out(self.getLabel()).render(out);
        wh.writeChildren(self);
        wh.writeln("</div></th>");
    }
}
