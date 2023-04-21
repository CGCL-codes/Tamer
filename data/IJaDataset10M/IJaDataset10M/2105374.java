package org.zkoss.zkmax.zul.render;

import java.io.IOException;
import java.io.Writer;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.render.ComponentRenderer;
import org.zkoss.zk.ui.render.SmartWriter;
import org.zkoss.zk.ui.render.Out;
import org.zkoss.zul.Tab;

/**
 * {@link Tab}'s default mold in vertical only.
 * 
 * @author RyanWu
 * 
 * @since 3.5.0
 * 
 */
public class Tab2DefaultV implements ComponentRenderer {

    public void render(Component comp, Writer out) throws IOException {
        final Tab self = (Tab) comp;
        final SmartWriter wh = new SmartWriter(out);
        final String zcs = self.getZclass() + '-';
        wh.write("<li id=\"" + self.getUuid() + "\"");
        wh.write(" z.type=\"Tab2\"").write(self.getOuterAttrs()).write(self.getInnerAttrs()).writeln(">");
        if (self.isClosable()) {
            wh.writeln("<a id=\"" + self.getUuid() + "!close\" class=\"" + zcs + "close\"  ></a>");
        } else {
            wh.writeln("<a class=\"" + zcs + "noclose\" ></a>");
        }
        wh.write("<div class=\"" + zcs + "hl\" id=\"" + self.getUuid() + "!real\"");
        wh.writeln(self.getInnerAttrs() + " >");
        wh.writeln("<div id=\"" + self.getUuid() + "!hr\" class=\"" + zcs + "hr\">");
        if (self.isClosable()) {
            wh.writeln("<div id=\"" + self.getUuid() + "!hm\" class=\"" + zcs + "hm " + zcs + "hm-close\" >");
        } else {
            wh.writeln("<div id=\"" + self.getUuid() + "!hm\" class=\"" + zcs + "hm \">");
        }
        wh.writeln("<span class=\"" + zcs + "text\">");
        wh.write(self.getImgTag());
        new Out(self.getLabel()).render(out);
        wh.writeln("</span></div></div></div></li>");
    }
}
