package zuul.game;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import zuul.core.PixelTool;
import zuul.game.quests.Quest;

/**
 * Die Objekte des Spieles -
 * abstrakte Klasse
 * 
 * @author swe_0802
 */
public abstract class GameObject implements Cloneable, Drawable, Serializable {

    /** Notwendig zum zeichnen auf dem Bildschirm bzw. im Graphics-Objekt */
    protected PixelTool pixeltool;

    /** Position des Objektes auf der Karte */
    private Point currentMapPosition;

    /** Vermutliche position des Objektes auf dem Bildschirm */
    private Point screenPosition;

    /** Bild des Objektes */
    private transient BufferedImage image;

    /** Dimension bzw. Groesse des Objektes */
    public Dimension size;

    /** Bezeichnung */
    private String name;

    /** Ort des Bildes. Wird zum laden des Objektes benoetigt. */
    private String imagePath;

    /** Quest das mit dem Objekt im Zusammenhang steht*/
    Quest quest;

    /**
     * Konstruktor des GameObjects
     * 
     * @param String name
     * 		  PixelTool tool
     */
    public GameObject(String name, PixelTool tool) {
        this.name = name;
        pixeltool = tool;
    }

    /**
     * Set-Methode des Pixeltools
     * 
     * @param Pixeltool tool
     */
    public void setPixeltool(PixelTool tool) {
        pixeltool = tool;
    }

    /**
     * Update-Methode
     * 
     * @param boolean paused
     */
    public void update(boolean paused) {
    }

    /**
     * Zeichnet ein Objekt des Spieles wenn es noch auf dem Bildschirm sichtbar
     * waere.
     * 
     * @param g
     *         Grafic-Objekt auf dem gezeichnet werden soll.
     *         Window window
     */
    @Override
    public synchronized void draw(Graphics2D g, Window window) {
        setScreenPosition(pixeltool.posOnMapToPosOnScreen(getCurrentMapPosition()));
        if (checkScreenVisible(window)) {
            int x = screenPosition.x;
            int y = screenPosition.y;
            if (image != null) {
                g.drawImage(getImage(), x, y, null);
            } else {
                File file = new File("./build/classes/zuul/");
                if (file == null) {
                    file = new File("./zuul/");
                }
                int last = imagePath.indexOf("images/");
                int length = imagePath.length();
                imagePath = file.getPath() + "/" + imagePath.substring(last, length);
                setImage(imagePath);
                if (image == null) {
                    System.out.println("No image to draw.");
                }
            }
        }
    }

    /** Bild des Charakters */
    public BufferedImage getImage() {
        return image;
    }

    /**
     * Erzeugt das Bild des Objektes und legt auch den Pfad des Bildes fest.
     * 
     * @param imagePath
     *                  Pfad des Bildes
     */
    public void setImage(String imagePath) {
        this.imagePath = imagePath;
        try {
            image = ImageIO.read(new File(imagePath));
            size = new Dimension(image.getWidth(), image.getHeight());
        } catch (IOException ex) {
            Logger.getLogger(GameObject.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Ueberprueft Sichtbarkeit
     * 
     * @param Window window
     * @return boolean sichtbar?
     */
    public boolean checkScreenVisible(Window window) {
        int x = getScreenPosition().x;
        int y = getScreenPosition().y;
        int w = window.getWidth();
        int h = window.getHeight();
        int critXLeft = -size.width;
        int critXRight = w + getSize().width;
        int critYTop = -size.height;
        int critYDown = h + getSize().height;
        if (x < critXLeft || x > critXRight || y < critYTop || y > critYDown) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Kopiert ein Objekt - ueberschreibt clone von java.lang
     * 
     * @return Object o
     */
    @Override
    public Object clone() {
        Object o = null;
        try {
            o = super.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(GameObject.class.getName()).log(Level.SEVERE, null, ex);
        }
        return o;
    }

    /**
     * Get-Methode von der aktullen Position auf der Map
     * 
     * @return Point aktuelle MapPosition
     */
    public Point getCurrentMapPosition() {
        return currentMapPosition;
    }

    /**
     * Set-Methode von der aktullen Position auf der Map
     * 
     * @param Point aktuelle MapPosition
     */
    public void setCurrentMapPosition(Point currentMapPosition) {
        this.currentMapPosition = currentMapPosition;
        this.screenPosition = pixeltool.posOnMapToPosOnScreen(currentMapPosition);
    }

    /**
     * Set-Methode von der aktullen Position auf der Map
     * 
     * @param int x, int y
     */
    public void setCurrentMapPosition(int x, int y) {
        this.currentMapPosition.setLocation(x, y);
        this.screenPosition = pixeltool.posOnMapToPosOnScreen(currentMapPosition);
    }

    /**
     * Get-Methode f�r die aktuelle Bildschirmposition
     * 
     * @return Point screenPosition
     */
    public Point getScreenPosition() {
        return screenPosition;
    }

    /**
     * Set-Methode f�r die aktuelle Bildschirmposition
     * 
     * @param Point screenPosition
     */
    public synchronized void setScreenPosition(Point screenPosition) {
        int x = screenPosition.x;
        int y = screenPosition.y;
        setScreenPosition(x, y);
    }

    /**
     * Set-Methode f�r die aktuelle Bildschirmposition
     * 
     * @param int x, int y
     */
    public synchronized void setScreenPosition(int x, int y) {
        if (screenPosition == null) {
            screenPosition = new Point();
        }
        this.screenPosition.setLocation(x, y);
        this.currentMapPosition = pixeltool.posOnScreenToPosOnMap(screenPosition);
    }

    /**
     * Get-Methode f�r den Namen des GameObjects
     * 
     * @return String name
     */
    public String getName() {
        return name;
    }

    /**
     * Set-Methode f�r den Namen des GameObjects
     * 
     * @param String name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get-Methode f�r die Groesse
     * 
     * @return Dimension size
     */
    public Dimension getSize() {
        return size;
    }

    /**
     * Get-Methode f�r den Bildpfad
     * 
     * @return String imagePath
     */
    public String getImagePath() {
        return imagePath;
    }

    /**
     * Get-Methode f�r das PixelTool
     * 
     * @return PixelTool pixeltool
     */
    public PixelTool getPixelTool() {
        return pixeltool;
    }

    /**
     * Get-Methode f�r ein Quest
     * 
     * @return Quest quest
     */
    public Quest getQuest() {
        return quest;
    }

    /**
     * Set-Methode f�r ein Quest
     * 
     * @param Quest quest
     */
    public void setQuest(Quest quest) {
        this.quest = quest;
    }
}
