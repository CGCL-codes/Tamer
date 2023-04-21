package net.sf.jcgm.core;

import java.io.*;

/**
 * Class=5, Element=8
 * @author xphc (Philippe Cadé)
 * @author BBNT Solutions
 * @version $Id: MarkerColour.java 46 2011-12-14 08:26:44Z phica $
 */
public class MarkerColour extends ColourCommand {

    public MarkerColour(int ec, int eid, int l, DataInput in) throws IOException {
        super(ec, eid, l, in);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MarkerColour");
        sb.append(super.toString());
        return sb.toString();
    }

    @Override
    public void paint(CGMDisplay d) {
        if (this.color != null) {
            d.setMarkerColor(this.color);
        } else {
            d.setMarkerColorIndex(this.colorIndex);
        }
    }
}
