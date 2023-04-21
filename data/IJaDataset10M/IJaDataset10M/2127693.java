package org.matsim.utils.vis.kml;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.GregorianCalendar;
import org.matsim.utils.vis.kml.KMLWriter.XMLNS;

/**
 * For documentation, refer to
 * <a href="http://earth.google.com/kml/kml_tags_21.html#timestamp">
 * http://earth.google.com/kml/kml_tags_21.html#timestamp</a>
 */
public class TimeStamp extends TimePrimitive {

    private GregorianCalendar when;

    /**
	 * Constructs a <code>TimeStamp</code> from a date.
	 * 
	 * @param when
	 * the <a href="http://earth.google.com/kml/kml_tags_21.html#when">
	 * when</a> attribute of the new time stamp.
	 */
    public TimeStamp(GregorianCalendar when) {
        super();
        this.when = when;
    }

    @Override
    protected void writeObject(BufferedWriter out, XMLNS version, int offset, String offsetString) throws IOException {
        out.write(Object.getOffset(offset, offsetString));
        out.write("<TimeStamp>");
        out.newLine();
        out.write(Object.getOffset(offset + 1, offsetString));
        out.write("<when>" + getKMLDateFormat(when) + "</when>");
        out.newLine();
        out.write(Object.getOffset(offset, offsetString));
        out.write("</TimeStamp>");
        out.newLine();
    }
}
