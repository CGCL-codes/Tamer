package newCarto;

import java.io.FileWriter;
import java.io.IOException;

/**
 * export.SVG ist eine Huellklasse für das Scalable Vector Graphics Format (svg).
 * Fuer eine vollstaendige Spezifikation zum Format siehe http://www.w3.org/TR/SVG11/
 * 
 */
public class SVG {

    private FileWriter file;

    private String name;

    private static double RANDX = 0;

    private static double RANDY = 0;

    /**
	 * Erstellt ein SVG-Dokument mit gegebenem Namen.
	 * 
	 * @param name  Dateiname
	 */
    public SVG(String name) {
        try {
            this.name = name;
            file = new FileWriter(name + ".svg");
            file.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" + "<svg xmlns:svg=\"http://www.w3.org/2000/svg\"" + " xmlns=\"http://www.w3.org/2000/svg\"" + " version=\"1.0\">");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean write(String s) {
        try {
            file.append(s);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * Fuegt dem Dokument eine Linie von (x1,y1) nach (x2,y2) an.
	 * 
	 * @param x1 x-Koordinate von P1
	 * @param y1 y-Koordinate von P1
	 * @param x2 x-Koordinate von P2
	 * @param y2 y-Koordinate von P2
	 * @param style Form der Linie nach Spezifikation
	 * @return false bei einem Schreibfehler, sonst true.
	 */
    public boolean line(double x1, double y1, double x2, double y2, String style) {
        String out = "\n\t<line  x1=\"" + (RANDX + x1) + "\" y1=\"" + (RANDY + y1) + "\" x2=\"" + (RANDX + x2) + "\" y2=\"" + (RANDY + y2) + "\" style=\"" + style + ";\" />";
        return write(out);
    }

    /**
	 * Wie line(double x1, double y1, double x2, double y2, String style) wobei die Form der
	 * Linie als schwarzer duenner Strich vorgegeben ist.
	 */
    public boolean line(double x1, double y1, double x2, double y2) {
        return line(x1, y1, x2, y2, "stroke:#000000");
    }

    /**
	 * Fuegt dem Dokument einen Kreis an.
	 * 
	 * @param x x-Koonrdinate des Mittelpunktes
	 * @param y y-Koonrdinate des Mittelpunktes
	 * @param radius Kreisradius
	 * @param style Form des Kreises nach Spezifikation
	 * @return false bei einem Schreibfehler, sonst true.
	 */
    public boolean circle(double x, double y, double radius, String style) {
        String out = "\n\t<circle  cx=\"" + (RANDX + x) + "\" cy=\"" + (RANDY + y) + "\" r=\"" + radius + "\" style=\"" + style + ";\" />";
        return write(out);
    }

    /**
	 * Wie circle(double x, double y, double radius, String style) wobei der Stil als
	 * 'ausgefuellt mit 10 als Radius' vorgegeben ist.
	 */
    public boolean circle(double x, double y) {
        return circle(x, y, 10, "fill:#000000");
    }

    /**
	 * Fuegt eine Linie ueber mehrere Punkte an das Dokument an.
	 * 
	 * @param xy ein double-Array mit jeweils folgenden x-,y-Koordinaten.
	 *           Muss eine gerade Anzahl an Elementen haben.
	 * @param style Form der Linie nach Spezifikation.
	 * @return false bei einem Schreibfehler, sonst true.
	 */
    public boolean polyline(double[] xy, String style) {
        if (xy.length % 2 != 0) System.out.println("Ungerade Anzahl an Elementen!");
        String out = "\n\t<polyline points=\"";
        for (int i = 0; i < xy.length; i++) {
            if (i % 2 == 0) out += ((xy[i] + RANDX) + ","); else out += ((xy[i] + RANDY + "  "));
        }
        out += "\" style=\"" + style + ";\" />";
        return write(out);
    }

    /**
	 * Wie polyline(double[] xy, String style) wobei die Form als 
	 * 'nicht ausgefuellt, mit duennem schwarzen Strich als Rand'
	 */
    public boolean polyline(double[] xy) {
        return polyline(xy, "fill:none; stroke:#000000");
    }

    /**
	 * Fuegt dem Dokument einen Text an.
	 * 
	 * @param x x-Koordinate der linken oberen Ecke.
	 * @param y y-Koordinate der linken oberen Ecke.
	 * @param text Text der angezeigt werden soll.
	 * @param style Form nach Spezifikation.
	 * @return false bei einem Schreibfehler, sonst true.
	 */
    public boolean text(double x, double y, String text, String style) {
        String out = "\n\t<text x=\"" + (RANDX + x) + "\" y=\"" + (RANDY + y) + "\" style=\"" + style + "\" >\n\t\t" + text + "\n\t</text>";
        return write(out);
    }

    /**
	 * Wie text(double x, double y, String text, String style) wobei die Form mit
	 * Verdena als Schrift, 35 als Schrftgroesse und schwarz als Farbe vorgegeben ist.
	 */
    public boolean text(double x, double y, String s) {
        return text(x, y, s, "font-family:Verdana; font-size:13px; stroke:#000000");
    }

    /**
	 * Speichert und schliesst die benutzte Grafik.
	 * Diese Funktion muss als letzte aufgerufen werden.
	 * 
	 * @return true wenn die Datei problemlos geschrieben und geschlossen wurde,
	 *         sonst false.
	 */
    public boolean close() {
        try {
            file.append("\n</svg>");
            file.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
	 * Generiert ein PNG-Bild mit Hilfe von inkscape.
	 * @param fileName ohne Endung.
	 * @return true wenn die Datei problemlos geschrieben und geschlossen wurde,
	 *         sonst false.
	 */
    public boolean toPng(String fileName) {
        boolean ok = true;
        try {
            new ProcessBuilder("inkscape", "--export-png=" + fileName + ".png", "--export-area-drawing", "--file=" + name + ".svg").start();
            ok = true;
        } catch (IOException e) {
            e.printStackTrace();
            ok = false;
        }
        return ok;
    }
}
