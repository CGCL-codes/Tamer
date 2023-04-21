package net.sourceforge.plantuml.font;

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.graphic.GraphicStrings;
import net.sourceforge.plantuml.graphic.HtmlColor;
import net.sourceforge.plantuml.ugraphic.UFont;

public class PSystemListFonts extends AbstractPSystem {

    private final List<String> strings = new ArrayList<String>();

    public PSystemListFonts(String text) {
        strings.add("   <b><size:16>Fonts available:");
        strings.add(" ");
        final String name[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        for (String n : name) {
            strings.add(n + " : <font:" + n + ">" + text);
        }
    }

    public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat) throws IOException {
        getGraphicStrings().writeImage(os, fileFormat);
    }

    private GraphicStrings getGraphicStrings() throws IOException {
        final UFont font = new UFont("SansSerif", Font.PLAIN, 12);
        final GraphicStrings result = new GraphicStrings(strings, font, HtmlColor.BLACK, HtmlColor.WHITE, false);
        return result;
    }

    public String getDescription() {
        return "(List fonts)";
    }
}
