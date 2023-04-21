package symbols;

import java.awt.*;
import java.util.*;
import java.io.*;
import xmlutils.*;
import utils.*;
import picskeeper.*;
import midi.*;

public class JHumNote extends JHumLengthSymbol implements Serializable {

    public static final int DBEMOL = -2;

    public static final int BEMOL = -1;

    public static final int BECARE = 0;

    public static final int DIESE = +1;

    public static final int DDIESE = +2;

    private static transient HashMap<Integer, String> noteImg = new HashMap<Integer, String>();

    private static transient HashMap<Integer, String> altImg = new HashMap<Integer, String>();

    static {
        noteImg.put(RONDE, "ronde");
        noteImg.put(BLANCHE, "blanche");
        noteImg.put(NOIRE, "noire");
        noteImg.put(CROCHE, "croche");
        noteImg.put(DCROCHE, "doublecroche");
        altImg.put(DBEMOL, "doublebemol");
        altImg.put(BEMOL, "bemol");
        altImg.put(BECARE, "becare");
        altImg.put(DIESE, "diese");
        altImg.put(DDIESE, "doublediese");
    }

    boolean alted = false;

    int alt = BECARE;

    public JHumNote(int nx, int len, int lenMod, boolean alted, int alt) {
        super(nx, len, lenMod);
        this.alted = alted;
        this.alt = alt;
    }

    public void draw(Graphics g, int offset, int y, int lineSpacing, boolean transparent) {
        int ny = y + (level * lineSpacing) / 2;
        super.draw(g, offset, ny, lineSpacing, transparent);
        int nx = x - offset;
        g.setColor(Color.BLACK);
        for (int i = 6; i <= Math.abs(level); i += 2) {
            int ly = y + utils.sgn(level) * i * lineSpacing / 2;
            g.drawLine(nx - lineSpacing, ly, nx + lineSpacing, ly);
        }
        JHumPicsKeeper.getJHumImage(noteImg.get(len)).draw(g, nx, ny, lineSpacing, level < 0, transparent);
        if (alted) JHumPicsKeeper.getJHumImage(altImg.get(alt)).draw(g, nx, ny, lineSpacing, false, transparent);
    }

    public int addMeTo(int i, JHumMidiPlayer p) {
        p.addNote(-level, i, alted, alt, getLength());
        return i + 1;
    }

    public JHumNote(XMLNode n) {
        super(n);
        level = Integer.decode(n.getNodeData("level", "0"));
        alted = (n.getNode("altMod") != null);
        if (alted) alt = Integer.decode(n.getNodeData("altMod"));
    }

    public void writeSymbol(XMLWriter w) {
        super.writeSymbol(w);
        w.data("level", level);
        if (alted) w.data("altMod", alt);
    }
}
