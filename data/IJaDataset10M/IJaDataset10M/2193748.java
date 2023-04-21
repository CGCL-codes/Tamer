package org.radeox.macro;

import org.radeox.api.engine.ImageRenderEngine;
import org.radeox.api.engine.RenderEngine;
import org.radeox.api.engine.context.RenderContext;
import org.radeox.macro.parameter.MacroParameter;
import org.radeox.util.Encoder;
import java.io.IOException;
import java.io.Writer;

public class LinkMacro extends BaseLocaleMacro {

    public String getLocaleKey() {
        return "macro.link";
    }

    public void execute(Writer writer, MacroParameter params) throws IllegalArgumentException, IOException {
        RenderContext context = params.getContext();
        RenderEngine engine = context.getRenderEngine();
        String text = params.get("text", 0);
        String url = params.get("url", 1);
        String img = params.get("img", 2);
        if (params.getLength() == 1) {
            url = text;
            text = Encoder.toEntity(text.charAt(0)) + Encoder.escape(text.substring(1));
        }
        if (url != null && text != null) {
            writer.write("<span class=\"nobr\">");
            if (!"none".equals(img) && engine instanceof ImageRenderEngine) {
                writer.write(((ImageRenderEngine) engine).getExternalImageLink());
            }
            writer.write("<a href=\"");
            writer.write(url);
            writer.write("\">");
            writer.write(text);
            writer.write("</a></span>");
        } else {
            throw new IllegalArgumentException("link needs a name and a url as argument: txt=" + text + " url=" + url);
        }
        return;
    }
}
