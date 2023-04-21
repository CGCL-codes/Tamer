package de.realriu.riulib.gui.imagelist;

import java.awt.Image;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import de.realriu.riulib.helpers.ScaleImage;

/**
 * Verwaltet eine Bilderliste mit ImageObjekten.<br>
 * Bilder werden beim Hinzufügen auf eine bestimmte Größe skaliert.<br>
 * Eignet sich gut, wenn die Komponente nicht in der Größe variert.
 *
 * @author michi
 * @version 1.1
 *
 * @see AbstractImageList
 * @see #ScaledImageList(riulib.gui.imagelist.AbstractImageList.Alignment, boolean, int, int)
 * @see #addImage(java.awt.Image, java.lang.String)
 * @see #addImage(java.awt.Image, java.lang.String, int, int)
 * @see #removeImage(int)
 * @see #getSelectedImage()
 * @see #getImage(int)
 */
public class ScaledImageList extends AbstractImageList<Image> implements MouseListener, MouseMotionListener, MouseWheelListener {

    protected int width;

    protected int heigth;

    /**
     * 
     * @param a Scrollrichtung
     * @param autoAlignment Automatische Scrollrichtungsanpassung
     * @param defaultWidth Breite auf die standartmäßig Bilder skaliert werden.
     * @param defaultHeigth Höhe auf die standartmäßig Bilder skaliert werden.
     */
    public ScaledImageList(Alignment a, boolean autoAlignment, int defaultWidth, int defaultHeigth) {
        super(a, autoAlignment);
        if (defaultWidth <= 0 || defaultHeigth <= 0) {
            throw new IllegalArgumentException("width(" + defaultWidth + ") und heigth(" + defaultHeigth + ") dürfen nicht negativ oder 0 sein!");
        }
        this.width = defaultWidth;
        this.heigth = defaultHeigth;
    }

    /**
     * Gibt die Standartbreite zurück
     * @return <b>Standartbreite</b>
     */
    public int getDefaultWidth() {
        return width;
    }

    public int getDefaultHeigth() {
        return heigth;
    }

    /**
     * Fügt ein Bild mit einem Titel hinzu.<br>
     * Das Bild wird auf die Standartgröße skaliert.
     * @param img Imageobjekt
     * @param title Titel, null für keinen Titel
     * @see #getDefaultWidth()
     * @see #getDefaultHeigth()  
     */
    @Override
    public void addImage(Image img, String title) {
        addImage(img, title, width, heigth);
    }

    /**
     * Fügt ein Bild mit einem Titel und explizieter Skalierung ein.
     * @param img Image Objekt
     * @param title Bildtitel (<b>null</b> für keinen Titel)
     * @param width Expliziete Breite
     * @param heigth Expliziete Höhe
     */
    public synchronized void addImage(Image img, String title, int width, int heigth) {
        if (width <= 0 || heigth <= 0) {
            throw new IllegalArgumentException("width(" + width + ") und heigth(" + heigth + ") dürfen nicht negativ oder 0 sein!");
        }
        if (img == null) {
            throw new IllegalArgumentException("Nullpointer für das Bild übergeben!");
        }
        if (img instanceof BufferedImage) {
            ScaleImage.Rectangle preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, width, heigth), (BufferedImage) img);
            images.add(ScaleImage.scale((BufferedImage) img, preferedSize.width, preferedSize.heigth));
        } else {
            images.add(img);
        }
        titles.add(title == null ? "" : title);
        repaint();
        fireImageAdded(img);
        System.gc();
    }

    /**
     * Fügt ein Bild ohne Titel und explizieter Skalierung ein.
     * @param img Bildobjekt
     * @param width Expliziete Breite
     * @param heigth Expliziete Höhe
     */
    public synchronized void addImage(Image img, int width, int heigth) {
        addImage(img, null, width, heigth);
    }

    /**
     * Entfernt ein Bild aus der Liste anhand seiner Position
     * @param pos Bildposition
     * @return
     */
    @Override
    public Image removeImage(int pos) {
        Image i = images.remove(pos);
        titles.remove(pos);
        repaint();
        fireImageRemoved(i);
        return i;
    }

    /**
     *
     * Ersetzt das Bild an der angegebenen Position mit einem neuen.
     * @param pos zu ersetzendes Bild
     * @param newImage Neues Bild
     * @param width Bildbreite
     * @param heigth Bildhöhe
     * @return Das vorherige Bild oder null wenn eine ungültige Position angegeben wird.
     */
    public synchronized Image replaceImage(int pos, Image newImage, int width, int heigth) {
        if (width <= 0 || heigth <= 0) {
            throw new IllegalArgumentException("width(" + width + ") und heigth(" + heigth + ") dürfen nicht negativ oder 0 sein!");
        }
        if (newImage == null) {
            throw new IllegalArgumentException("Nullpointer für das Bild übergeben!");
        }
        if (pos >= 0 && pos < images.size()) {
            Image old;
            if (newImage instanceof BufferedImage) {
                ScaleImage.Rectangle preferedSize = ScaleImage.fitToRect(new ScaleImage.Rectangle(0, 0, width, heigth), (BufferedImage) newImage);
                old = images.set(pos, newImage = ScaleImage.scale((BufferedImage) newImage, preferedSize.width, preferedSize.heigth));
            } else {
                old = images.set(pos, newImage);
            }
            repaint();
            fireImageReplaced(old, newImage, pos);
            return old;
        }
        return null;
    }

    /**
     * Ersetzt das Bild an der angegebenen Position mit einem neuen.
     * @param pos zu ersetzendes Bild
     * @param newImage Neues Bild
     * @return Das vorherige Bild oder null wenn eine ungültige Position angegeben wird.
     */
    @Override
    public synchronized Image replaceImage(int pos, Image newImage) {
        return replaceImage(pos, newImage, width, heigth);
    }

    /**
     * Gibt das im Moment ausgewählte Bild zurück.<br>
     * Falls kein Bild ausgewählt wurde wird <b>null</b> zurückgegeben.
     * @return Ausgewähltes bild
     */
    public synchronized Image getSelectedImage() {
        if (getSelectedImageIndex() < 0 || getSelectedImageIndex() >= images.size()) {
            return null;
        }
        return images.get(getSelectedImageIndex());
    }

    /**
     * Gibt ein Bild anhand seiner Position zurück.
     * @param i Bildposition
     * @return Bild
     */
    @Override
    public Image getImage(int i) {
        if (i < 0 || i >= images.size()) {
            return null;
        }
        return images.get(i);
    }

    /**
     * Entfernt alle Bilder aus der Liste
     */
    @Override
    public synchronized void clear() {
        super.clear();
        repaint();
    }
}
