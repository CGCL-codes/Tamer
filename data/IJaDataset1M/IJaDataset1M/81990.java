package net.sourceforge.plantuml.eggs;

import java.io.IOException;
import java.io.OutputStream;
import net.sourceforge.plantuml.AbstractPSystem;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.ugraphic.ColorMapperIdentity;

public class PSystemPath extends AbstractPSystem {

    private final GraphicsPath path;

    public PSystemPath(String s) {
        this.path = new GraphicsPath(new ColorMapperIdentity(), s);
    }

    public void exportDiagram(OutputStream os, StringBuilder cmap, int index, FileFormatOption fileFormat) throws IOException {
        path.writeImage(os);
    }

    public String getDescription() {
        return "(Path)";
    }
}
