package de.annee.mosaix.model;

/**
 * Definiert ein Kachelset für ein Bild der Bibliothek.
 * Ein Kachelset ist beschrieben durch die Größe einzelner Kacheln (in Pixeln) (<tt>tileWidth</tt>x<tt>tileHeight</tt>)
 * sowie pro Kachel den jeweiligen gewichteten HSB-Angaben (Farbton, Sättigung, Helligkeit) (<tt>hsbValues</tt>).
 * Die Dimensionen von <tt>hsbValues</tt> definieren gleichzeitig die Anzahl der Kacheln in x- sowie y-Richtung.
 * Die Attribute <tt>skipX</tt> und <tt>skipY</tt> geben an, um wieviele Pixel das Bild in die jeweilige Richtung
 * verschoben werden soll (die Bilder der Bibliothek werden immer zentriert verwendet, wenn das Seitenverhältnis nicht
 * einem Vielfachen des Zielbildes entspricht. 
 * 
 * @author Philipp Anné
 */
public class ImageTiles {

    /** Enthält die Breite einer Kachel in Pixeln. */
    private int tileWidth;

    /** Enthält die Höhe einer Kachel in Pixeln. */
    private int tileHeight;

    /** Enthält den Versatz in X-Richtung in Pixeln. */
    private int skipX;

    /** Enthält den Versatz in Y-Richtung in Pixeln. */
    private int skipY;

    /** Enthält die HSB-Werte der Kacheln. */
    private float[][][] hsbValues;

    public float[][][] getHsbValues() {
        return hsbValues;
    }

    public void setHsbValues(float[][][] hsbValues) {
        this.hsbValues = hsbValues;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getSkipX() {
        return skipX;
    }

    public void setSkipX(int skipX) {
        this.skipX = skipX;
    }

    public int getSkipY() {
        return skipY;
    }

    public void setSkipY(int skipY) {
        this.skipY = skipY;
    }
}
