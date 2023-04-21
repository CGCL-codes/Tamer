package net.sourceforge.circuitsmith.cam;

import java.awt.Component;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import net.sourceforge.circuitsmith.cam.GerberObject;
import net.sourceforge.circuitsmith.panes.EdaCamOutputsPane;

/**
 * detail of what layers to use to generate a particular cam file
 * @author Kenneth MacCallum
 *
 */
public abstract class CamFile implements CamOutputTableItem {

    private String fileName;

    private ArrayList<String> layerNames = new ArrayList<String>();

    private double lastCoordX, lastCoordY;

    protected ArrayList<Aperture> apertures = new ArrayList<Aperture>();

    protected Aperture m_apt;

    protected ArrayList<GerberObject> gerberObjects = new ArrayList<GerberObject>();

    public enum Digits {

        TWOTWO("2:2", 2, 2), TWOTHREE("2:3", 2, 3), TWOFOUR("2:4", 2, 4), THREETWO("3:2", 3, 2), THREETHREE("3:3", 3, 3), THREEFOUR("3:4", 3, 4);

        private int wholeDigits;

        private int fractionalDigits;

        private String name;

        Digits(String n, int w, int f) {
            name = n;
            wholeDigits = w;
            fractionalDigits = f;
        }

        public int getWholeDigits() {
            return wholeDigits;
        }

        public int getFractionalDigits() {
            return fractionalDigits;
        }

        public String toString() {
            return name;
        }
    }

    private Digits digits = Digits.TWOFOUR;

    public enum Units {

        MILLIMETRES("Millimetres"), INCHES("Inches");

        private String name;

        Units(String name) {
            this.name = name;
        }

        public String toString() {
            return name;
        }
    }

    ;

    private Units units = Units.INCHES;

    public enum ZeroRemoval {

        NONE("None", "No zero removal"), LEADING("Leading", "Remove leading zeroes"), TRAILING("Trailing", "Remove trailing zeroes");

        private String name;

        private String description;

        ZeroRemoval(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String toString() {
            return name;
        }

        String getDescription() {
            return description;
        }
    }

    ;

    private ZeroRemoval zeroRemoval = ZeroRemoval.NONE;

    public enum CoordinateNotation {

        ABSOLUTE("Absolute", "Absolute coordinate notation"), RELATIVE("Relative", "Relative coordinate notation");

        private String name;

        private String description;

        CoordinateNotation(String name, String description) {
            this.name = name;
            this.description = description;
        }

        public String toString() {
            return name;
        }

        String getDescription() {
            return description;
        }
    }

    ;

    private CoordinateNotation coordinateNotation = CoordinateNotation.ABSOLUTE;

    /**
	 * formats a number in metres into the appropriate form according to the configuration of this CamFile
	 * @param n
	 * @return
	 */
    private String formatNumber(double n) {
        double temp = n;
        boolean negative = false;
        if (temp < 0) {
            negative = true;
            temp = -temp;
        }
        if (units == Units.MILLIMETRES) {
            temp *= 1000;
        } else if (units == Units.INCHES) {
            temp *= 1 / 25.4e-3;
        }
        if (temp > .5) {
            temp = temp * 1;
        }
        temp = Math.round(temp * Math.pow(10, digits.getFractionalDigits()));
        String s = Integer.toString((int) temp);
        if (s.length() > digits.getWholeDigits() + digits.getFractionalDigits()) {
            throw (new RuntimeException("Value " + temp + " is too big to be encoded in " + digits.getWholeDigits() + "." + digits.getFractionalDigits() + " format."));
        }
        if (zeroRemoval != ZeroRemoval.LEADING) {
            while (s.length() < digits.getWholeDigits() + digits.getFractionalDigits()) {
                s = "0" + s;
            }
        } else if (s.equals("0")) {
            s = "";
        }
        if (zeroRemoval == ZeroRemoval.TRAILING) {
            int i = s.length();
            while (i > 0 && s.endsWith("0")) {
                s = s.substring(s.length() - 2);
            }
        }
        if (negative) {
            s = "-" + s;
        }
        return s;
    }

    public String getCoordString(double x, double y) {
        double x1 = x;
        double y1 = y;
        if (coordinateNotation == CoordinateNotation.RELATIVE) {
            x1 -= lastCoordX;
            y1 -= lastCoordY;
        }
        lastCoordX = x;
        lastCoordY = y;
        String result = "";
        String r = formatNumber(x);
        if (!r.equals("")) {
            result = "X" + r;
        }
        r = formatNumber(y);
        if (!r.equals("")) {
            result += "Y" + r;
        }
        return result;
    }

    public void addLayerNames(String layer) {
        for (String ln : layerNames) {
            if (ln.equals(layer)) {
                return;
            }
        }
        layerNames.add(layer);
    }

    public void removeLayerName(String layer) {
        for (String ln : layerNames) {
            if (ln.equals(layer)) {
                layerNames.remove(ln);
                return;
            }
        }
    }

    public Iterable<String> getLayerNames() {
        return layerNames;
    }

    public void setUnits(Units u) {
        units = u;
    }

    public Units getUnits() {
        return units;
    }

    public void setZeroRemoval(ZeroRemoval zr) {
        zeroRemoval = zr;
    }

    public ZeroRemoval getZeroRemoval() {
        return zeroRemoval;
    }

    public void setCoordinateNotation(CoordinateNotation cn) {
        coordinateNotation = cn;
    }

    public CoordinateNotation getCoordinateNotation() {
        return coordinateNotation;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fn) {
        fileName = fn;
    }

    public void setDigits(Digits d) {
        digits = d;
    }

    public Digits getDigits() {
        return digits;
    }

    public String toString() {
        return getFileName();
    }

    public Component getSplitPaneComponent(EdaCamOutputsPane pane) {
        LayersToExportTable t = new LayersToExportTable(pane);
        t.setCamFile(this);
        return t;
    }

    ArrayList<GerberObject> getGerberObjects() {
        return gerberObjects;
    }

    /**
	 * when in relative coordinates, this should be called once before using <code>formatNumber</code> for the first time
	 *
	 */
    public void zeroRelativeCoordinates() {
    }

    public void setApertures(ArrayList<Aperture> al) {
        apertures = al;
    }

    public ArrayList<Aperture> getApertures() {
        return apertures;
    }

    public abstract void writeCamFile(Writer fw) throws IOException;

    public int getApertureId(Aperture a) {
        return apertures.indexOf(a) + 10;
    }

    public Aperture getCurrentAperture() {
        return m_apt;
    }

    public void setCurrentAperture(Aperture a) {
        m_apt = a;
    }

    public void addGerberObject(GerberObject go) {
        gerberObjects.add(go);
    }
}
