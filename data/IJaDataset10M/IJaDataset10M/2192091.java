package net.rptools.maptool.client.macro.impl;

import java.awt.Color;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.maptool.model.TextMessage;

@MacroDefinition(name = "ooc", aliases = { "ooc" }, description = "ooc.desc")
public class OOCMacro extends AbstractMacro {

    public void execute(MacroContext context, String macro, MapToolMacroContext executionContext) {
        macro = processText(macro);
        StringBuilder sb = new StringBuilder();
        sb.append(MapTool.getFrame().getCommandPanel().getIdentity());
        sb.append(": ");
        Color color = MapTool.getFrame().getCommandPanel().getTextColorWell().getColor();
        if (color != null) {
            sb.append("<span style='color:#").append(String.format("%06X", (color.getRGB() & 0xFFFFFF))).append("'>");
        }
        sb.append("(( ").append(macro).append(" ))");
        if (color != null) {
            sb.append("</span>");
        }
        MapTool.addMessage(TextMessage.say(context.getTransformationHistory(), sb.toString()));
    }
}
