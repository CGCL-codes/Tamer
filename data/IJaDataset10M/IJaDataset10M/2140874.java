package org.wings;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;
import org.wings.io.Device;
import org.wings.io.IOUtil;
import org.wings.io.StringBuilderDevice;
import org.wings.macro.MacroContainer;
import org.wings.macro.MacroContext;
import org.wings.macro.impl.VelocityMacroProcessor;
import org.wings.plaf.IntegrationTableCG;
import org.wings.plaf.ComponentCG;
import org.wings.plaf.IntegrationComponentCG;
import org.wings.template.IntegrationTemplateParseContext;
import org.wings.template.PropertyManager;
import org.wings.template.parser.ParseContext;
import org.wings.template.parser.PositionReader;
import org.wings.template.parser.SGMLTag;
import org.wings.template.parser.SpecialTagHandler;

/**
 * <code>MacroTagHandler<code>.
 * <p/>
 * User: raedler
 * Date: 08.08.2007
 * Time: 14:35:57
 *
 * @author raedler
 * @version $Id
 */
public class MacroTagHandler implements SpecialTagHandler {

    boolean close_is_missing = false;

    long startPos;

    long endPos;

    Map<String, String> properties;

    String name;

    private String macroTemplate;

    public long getTagStart() {
        return startPos;
    }

    public long getTagLength() {
        return endPos - startPos;
    }

    public void executeTag(ParseContext context, Reader input) throws Exception {
        IntegrationTemplateParseContext tcontext = (IntegrationTemplateParseContext) context;
        Device sink = tcontext.getDevice();
        SComponent c = tcontext.getComponent(name);
        if (c == null) {
            sink.print("<!-- Template: Component '" + name + "' is unknown -->");
            input.skip(getTagLength());
        } else {
            properties(c);
            int length = (int) getTagLength();
            if (macroTemplate == null) {
                StringWriter output = new StringWriter();
                IOUtil.copy(input, output, length, new char[length]);
                macroTemplate = output.toString();
                macroTemplate = macroTemplate.substring(macroTemplate.indexOf('>') + 1, macroTemplate.lastIndexOf('<'));
            } else {
                input.skip(length);
            }
            if (macroTemplate != null && !"".equals(macroTemplate.trim())) {
                VelocityMacroProcessor macroProcessor = VelocityMacroProcessor.getInstance();
                MacroContainer macroContainer = macroProcessor.buildMacro(macroTemplate);
                MacroContext ctx = new MacroContext();
                ctx.setDevice(sink);
                ctx.setComponent(c);
                ctx.put(name, c);
                macroContainer.setContext(ctx);
                if (c instanceof STable) {
                    IntegrationTableCG cg = new IntegrationTableCG();
                    cg.setMacros(macroContainer);
                    if (c.getClientProperty("cg") == null) c.putClientProperty("cg", c.getCG());
                    c.setCG(cg);
                    c.write(sink);
                } else {
                    ComponentCG cg = c.getCG();
                    if (cg instanceof IntegrationComponentCG) {
                        ((IntegrationComponentCG) cg).setMacros(macroContainer);
                    } else {
                        cg = new IntegrationComponentCG();
                        ((IntegrationComponentCG) cg).setMacros(macroContainer);
                        if (c.getClientProperty("cg") == null) c.putClientProperty("cg", c.getCG());
                        c.setCG(cg);
                    }
                    c.write(sink);
                }
            } else {
                ComponentCG cg = (ComponentCG) c.getClientProperty("cg");
                if (cg != null) c.setCG(cg);
                c.write(sink);
            }
        }
    }

    private void properties(SComponent c) {
        if (properties.size() > 0) {
            PropertyManager propManager = TemplateIntegrationLayout.getPropertyManager(c.getClass());
            if (propManager != null) {
                Iterator<String> iter = properties.keySet().iterator();
                while (iter.hasNext()) {
                    String key = iter.next();
                    String value = properties.get(key);
                    propManager.setProperty(c, key, value);
                }
            }
        }
    }

    public SGMLTag parseTag(ParseContext context, PositionReader input, long startPosition, SGMLTag startTag) throws IOException {
        IntegrationTemplateParseContext tcontext = (IntegrationTemplateParseContext) context;
        final String startTagName = startTag.getName();
        final String endTagName = "/" + startTagName;
        startTag.parse(input);
        startPos = startPosition + startTag.getOffset();
        properties = startTag.getAttributes();
        name = startTag.value("NAME", null);
        if (name == null) return null;
        tcontext.addContainedComponent(name);
        endPos = input.getPosition();
        while (!startTag.finished()) {
            startTag = new SGMLTag(input, true);
            if (startTag.isNamed(endTagName) || startTag.isNamed(startTagName)) break;
        }
        if (startTag.finished() || startTag.isNamed(startTagName)) {
            close_is_missing = true;
        } else {
            endPos = input.getPosition();
        }
        properties.remove("NAME");
        properties.remove("TYPE");
        context.setProperties(name, properties);
        return startTag;
    }
}
