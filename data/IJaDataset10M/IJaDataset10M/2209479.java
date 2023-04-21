package at.vartmp.jschnellen.gui.items;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import at.vartmp.jschnellen.core.basic.Karte;
import at.vartmp.jschnellen.core.basic.Kartenstapel;

/**
 * Diese Klasse stellt das Mapping der Karten zu den Grafiken zur
 * Verf&uuml;gung.<br>
 * Um eigene Grafikpakete zu erstellen, werden die Grafiken in einer externen
 * JAR-Datei gespeichert und k&ouml;nnen so von dort geladen werden.<br>
 * <br>
 * <b>Hinweis</b>: Es ist &auml;&szlig;erst wichtig, dass die Dateien im
 * Grafikpaket die Namenskonventionen beachten, welche sich wie folgt
 * definieren:
 * <ul>
 * <li>Grafikformat: PNG</li>
 * <li>ID der Farbe der Karte</li>
 * <li>Bindestrich</li>
 * <li>Wert der Karte</li>
 * </ul>
 * <i>Beispiel</i>: F&uuml;r die Karte <code>Herz Ober</code> lautet der
 * Dateiname <code>1-12.png</code> <br>
 * <br>
 * Die Implementierung dieser Klasse basiert auf einer HashMap, wobei der Key
 * der Karten sich aus dem ersten Teil des Dateinamen zusammensetzt.<br>
 * <i>Beispiel</i>: F&uuml;r die Karte <code>Herz Ober</code> lautet der Key
 * <code>1-12</code><br>
 * Den Wert des Eintrags belegt die Grafik an sich<br>
 * 
 * @author luki
 */
public final class GKartenPoolImpl2 extends GKartenPool {

    private static final String DEFAULT = "default_karten.jar";

    private ReentrantLock lock = new ReentrantLock();

    private final Condition waitCondition = lock.newCondition();

    private ConcurrentHashMap<String, BufferedImage> karteToImageMapping;

    /**
	 * Default-Konstruktor; ladet die Default Karten und erstellt das Mapping
	 */
    public GKartenPoolImpl2() {
        karteToImageMapping = new ConcurrentHashMap<String, BufferedImage>();
        try {
            this.createMappings(DEFAULT);
        } catch (IOException e) {
            logger.fatal("Das Archiv mit den Grafiken der Karten konnte" + " nicht gefunden werden!");
        }
    }

    /**
	 * Diese Methode erstellt das Mapping f&uuml;r die Karten basierend auf
	 * einer Jar-Datei.
	 * 
	 * @param filename
	 *            der Dateiname der Jar-Datei
	 * @throws IOException
	 *             diese Exception wird geworfen, falls die Datei nicht gefunden
	 *             werden konnte
	 */
    public synchronized void createMappings(String filename) throws IOException {
        try {
            this.lock.lock();
            this.karteToImageMapping.clear();
            JarFile karten = new JarFile(filename);
            logger.trace("createMappings(String): " + "Trying to access " + filename);
            Kartenstapel ks = new Kartenstapel(true);
            while (!ks.isEmpty()) {
                Karte karte = ks.getKarte();
                logger.trace("createMappings(String): Got a card: " + karte.getKartenBezeichnung() + " " + karte.getKartenFarbe().getFarbenBezeichnung());
                putTheMapping(karte, karten);
            }
        } finally {
            this.waitCondition.signal();
            lock.unlock();
        }
    }

    /**
	 * In this method we put the mapping for a given card in the HashMap which
	 * holds all cards. The second argument is a reference to the JarFile.
	 * 
	 * @param karte
	 *            the card which should be putted in the HashMap
	 * @param karten
	 *            the reference to the JarFile which holds the image data
	 * @throws IOException
	 *             if a error has occurred while getting the image data from the
	 *             JarFile
	 */
    private void putTheMapping(Karte karte, JarFile karten) throws IOException {
        logger.trace(karte.getKartenBezeichnung() + " - " + karte.getKartenFarbe().getFarbenBezeichnung());
        String key = karte.getKartenFarbe().getFarbenId() + "-" + karte.getKartenWert();
        logger.trace("createMappings(String): Create mapping with key " + key);
        String dateiname = key + ".png";
        JarEntry y = karten.getJarEntry(dateiname);
        BufferedImage bufferedImage = ImageIO.read(getImageDataAsStream(karten, y));
        this.karteToImageMapping.put(key, bufferedImage);
    }

    /**
	 * Diese Methode gibt eine Instanz von <code>ImageIcon</code> zur&uuml;ck,
	 * welche mit der Karte als Parameter assoziert wurde.
	 * 
	 * @param k
	 *            Die Karte, f&uuml;r welche die Grafik zur&uuml;ckgegeben
	 *            werden soll
	 * @return Die Grafik, welche mit der Karte assoziert wurde
	 */
    public synchronized ImageIcon getKartenIcon(Karte k) {
        while (this.lock.isLocked()) {
            try {
                this.waitCondition.await();
            } catch (InterruptedException e) {
            }
        }
        lock.lock();
        ImageIcon toReturn = new ImageIcon(getImageData(k));
        lock.unlock();
        return toReturn;
    }

    @Override
    public BufferedImage getImageData(Karte k) {
        String key = k.getKartenFarbe().getFarbenId() + "-" + k.getKartenWert();
        if (logger.isTraceEnabled()) {
            logger.trace("getKartenIcon(Karte: " + k.toString() + ") --> key: " + key);
            logger.trace("Checking if key is in List: " + this.karteToImageMapping.containsKey(key));
            logger.trace("The size of the map is " + this.karteToImageMapping.size());
        }
        return this.karteToImageMapping.get(key);
    }

    /**
	 * Diese Methode entpackt das Bild einer Karte aus einem Jar-Archiv und gibt
	 * es als ein BufferedInputStream zur&uuml;ck.
	 * 
	 * @param jarFile
	 *            das JarFile, aus welchem die Datei entpackt werden soll
	 * @param target
	 *            der Namen des Eintrags im Jar-File
	 * @return eine Instanz von BufferedInputStream
	 * @throws IOException
	 */
    private static synchronized BufferedInputStream getImageDataAsStream(JarFile jarFile, JarEntry target) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(jarFile.getInputStream(target));
        return bis;
    }

    /**
	 * Diese Methode gibt die Ge&ouml;&szlig;e eines Images zur&uuml;ck.
	 * 
	 * @param scaleFactor
	 *            der Skalierungsgrad in Prozent. <code>1</code> entspricht
	 *            hierbei 100%
	 * @param rotated
	 *            if <code>true</code>, than the height and width is swapped
	 * 
	 * @return die Dimension eines Images
	 */
    public Dimension getSize(double scaleFactor, boolean rotated) {
        BufferedImage tmp = this.karteToImageMapping.values().iterator().next();
        int width = (int) (tmp.getWidth() * scaleFactor);
        int height = (int) (tmp.getHeight() * scaleFactor);
        Dimension dim = null;
        if (rotated) dim = new Dimension(height, width); else dim = new Dimension(width, height);
        return dim;
    }
}
