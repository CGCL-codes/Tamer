package org.matsim.utils.vis.kml;

import java.io.BufferedWriter;
import java.io.IOException;
import org.matsim.utils.vis.kml.KMLWriter.XMLNS;
import org.matsim.utils.vis.kml.fields.Color;

public class LineStyle extends ColorStyle {

    private int width;

    public static final int DEFAULT_WIDTH = 1;

    public LineStyle() {
        super(Color.DEFAULT_COLOR, ColorStyle.DEFAULT_COLOR_MODE);
        this.width = LineStyle.DEFAULT_WIDTH;
    }

    public LineStyle(final Color color, final ColorMode colorMode, final int width) {
        super(color, colorMode);
        this.width = width;
    }

    public int getWidth() {
        return this.width;
    }

    @Override
    protected void writeObject(final BufferedWriter out, final XMLNS version, final int offset, final String offsetString) throws IOException {
        out.write(Object.getOffset(offset, offsetString));
        out.write("<LineStyle>");
        out.newLine();
        super.writeObject(out, version, offset + 1, offsetString);
        if (this.width != LineStyle.DEFAULT_WIDTH) {
            out.write(Object.getOffset(offset + 1, offsetString));
            out.write("<width>" + this.width + "</width>");
            out.newLine();
        }
        out.write(Object.getOffset(offset, offsetString));
        out.write("</LineStyle>");
        out.newLine();
    }
}
