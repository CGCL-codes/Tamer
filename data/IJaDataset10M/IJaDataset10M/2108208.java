package aurora.hwc.config;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Vector;
import com.vividsolutions.jts.geom.Coordinate;

public class GISEdge {

    public Vector<Coordinate> coordinates;

    public ArrayList<String> successors;

    public ArrayList<String> predecessors;

    protected String id = "";

    protected double lanes = 0.0;

    protected double length = 0.0;

    protected String type = "Unknown";

    private String auroraClass;

    protected double[][] splitRatioMatrix;

    public GISEdge() {
        coordinates = new Vector<Coordinate>();
        successors = new ArrayList<String>();
        predecessors = new ArrayList<String>();
        auroraClass = null;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLanes(double lanes) {
        this.lanes = lanes;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setAuroraClass(String name) {
        this.auroraClass = name;
    }

    /**
	 * Generates XML description of the simple Node.<br>
	 * If the print stream is specified, then XML buffer is written to the stream.
	 * @param out print stream.
	 * @return XML buffer.
	 * @throws IOException
	 */
    public String xmlDump(PrintStream out) throws IOException {
        if (this.auroraClass == null) {
            this.auroraClass = "aurora.hwc.LinkStreet";
        }
        String buf = "<link class=\"" + this.auroraClass + "\" id=\"" + id + "\" length=\"" + Double.toString(length / 5280.0) + "\" lanes=\"" + Double.toString(lanes) + "\">";
        if (predecessors.size() > 0) buf += "<begin id=\"" + predecessors.get(0) + "\"/>";
        if (successors.size() > 0) buf += "<end id=\"" + successors.get(0) + "\"/>";
        String dynamicsName = "aurora.hwc.DynamicsCTM";
        buf += "<dynamics class=\"" + dynamicsName + "\"/>";
        if (out != null) out.print(buf);
        String buf2 = "<position>";
        for (int i = 0; i < coordinates.size(); i++) buf2 += "<point x=\"" + Double.toString(coordinates.get(i).x) + "\" y=\"" + Double.toString(-coordinates.get(i).y) + "\" z=\"" + 0.0 + "\"/>";
        buf2 += "</position>";
        if (out != null) out.print(buf2);
        buf += buf2;
        if (out != null) out.print("</link>");
        buf += "</link>";
        return buf;
    }

    public boolean equivalent(GISEdge edge) {
        return true;
    }
}
