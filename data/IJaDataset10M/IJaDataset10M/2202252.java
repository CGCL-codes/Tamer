package org.dhcc.utils.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Label;
import java.awt.MediaTracker;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * Der <code>IconManager</code> verwaltet eine Sammlung von {@link Icon}s. Diese
 * Sammlung besteht aus einer Datei mit Eigenschaften und einem Iconbild. Das
 * Iconbild kann eine gro�e Anzahl von Icons in sich enthalten. Die Position und
 * Gr��e der Icons wird �ber die "Properties"-Datei spezifiziert.
 * <p>
 * 
 * Die Properties-Datei besteht aus zwei Angaben:
 * <ul>
 * <li>extension=png
 * <li>image.foo.rectangle=0,0,32,32
 * </ul>
 * Der erste Eintrag spezifiziert die Erweiterung des Iconbildes.
 * 
 * Der zweite Eintrag spezifiziert die Position eines bestimmten Icons in Form
 * von Startkoordinaten und L�ngenangaben: [X, Y, Breite, H�he].
 * <p>
 * 
 * Die Applikation kann mehrere Iconsammlungen verwenden. Jede Sammlung ist eine
 * Instanz von <code>IconManager</code>. Individuelle Instanzen m�ssen �ber die
 * <code>getInstance(Class, String)</code> Methode aufgerufen werden.
 * 
 * @author <a href="mailto:matthaeus.krzikalla@googlemail.com">Matth�us
 *         Krzikalla</a>
 */
@SuppressWarnings("unchecked")
public class IconManager {

    /** Das Iconbild */
    private Image iconImage;

    /** Cache f�r Effekt-Iconbilder */
    private Hashtable<Integer, Image> effectImages = new Hashtable<Integer, Image>();

    /** Eigenschaften der Iconsammlung */
    private Properties properties;

    /** Cache der Instanzen */
    private static Hashtable<String, IconManager> instances = new Hashtable<String, IconManager>();

    /** Dummy Komponente */
    private static final Component component = new Label();

    /** MediaTracker verfolgt Bildgenerierung */
    private static final MediaTracker tracker = new MediaTracker(component);

    /** [Flag] Icon zeichnet sich automatisch selbst. */
    private static final int AUTO_DISABLE = 1;

    /** [Flag] Icon produziert eine Rollover-Version von sich. */
    private static final int AUTO_ROLLOVER = 1 << 1;

    /** Iconeffekt: Konvertiere zu grau */
    private static final int EFFECT_GRAY = 1 << 2;

    /** Iconeffekt: Zeichne Icon heller. */
    private static final int EFFECT_LIGHTER = 1 << 3;

    /** Iconeffekt: Zeichne mit mehr Kontrast */
    private static final int EFFECT_MORECONTRAST = 1 << 7;

    /** Flags f�r Auto-Disable-Icons (Default: EFFECT_GRAY...) */
    private int autoDisableFlags = EFFECT_GRAY | EFFECT_LIGHTER | EFFECT_MORECONTRAST;

    /** Flags f�r Auto-Rollover-Icons (Default: EFFECT_LIGHTER) */
    private int autoRolloverFlags = EFFECT_LIGHTER;

    /**
	 * Standardeffekte die beim Erstellen eines Icons mit
	 * <code>getIcon(String)</code> angewendet werden. Default ist pro Instanz,
	 * z.B. pro Icon.
	 */
    private int defaultEffects = 0;

    /**	 */
    private IconManager() {
        super();
    }

    /**
	 * Erzeugt einen neuen IconManager.
	 * 
	 * @param properties die Icon Eigenschaften
	 * @param iconImage das Icon Bild
	 */
    private IconManager(Properties properties, Image iconImage) {
        super();
        this.properties = properties;
        this.iconImage = iconImage;
    }

    /**
	 * Factory Methode: Liefert eine <code>IconManager</code>-Instanz f�r die
	 * spezifische Iconsammlungnamen. Iconeigenschaften und Iconbilder werden
	 * relativ zu einer spezifizierten Klasse gesucht.
	 * 
	 * @param loadClass die Klasse, von der aus relativ gesehen nach Iconbild und
	 *          Eigenschaftsdatei gesucht wird.
	 * @param iconSetName der Name der Iconsammlung
	 * @return den <code>IconManager</code> oder <code>null</code>, wenn der
	 *         <code>IconManager</code> nicht instanziert werden konnte (z.B. weil
	 *         Iconbild oder Eigenschaftsdatei nicht gefunden werden konnte).
	 */
    public static IconManager getInstance(Class loadClass, String iconSetName) {
        if (null == loadClass) {
            loadClass = IconManager.class;
        }
        String setName = loadClass.getPackage() + iconSetName;
        if (!instances.containsKey(setName)) {
            String resourcePath = resolveName(loadClass, iconSetName);
            IconManager im = null;
            if (instances.containsKey(resourcePath)) {
                im = (IconManager) instances.get(resourcePath);
            } else {
                im = createIconManager(loadClass, iconSetName);
                instances.put(resourcePath, im);
            }
            instances.put(setName, im);
        }
        return (IconManager) instances.get(setName);
    }

    /**
	 * Erzeugt einen neuen IconManager.
	 * 
	 * @param loadClass die Klasse, von der aus relativ gesehen nach Iconbild und
	 *          Eigenschaftsdatei gesucht wird.
	 * @param iconSetName der Name der Iconsammlung
	 * @return den <code>IconManager</code> oder <code>null</code>, wenn der
	 *         <code>IconManager</code> nicht instanziert werden konnte (z.B. weil
	 *         Iconbild oder Eigenschaftsdatei nicht gefunden werden konnte).
	 */
    private static IconManager createIconManager(Class loadClass, String iconSetName) {
        IconManager theInstance = null;
        try {
            InputStream is = null;
            String resourcePath = null;
            if (iconSetName.indexOf("../") >= 0) {
                resourcePath = resolveName(loadClass, iconSetName);
            } else {
                resourcePath = iconSetName;
            }
            is = loadClass.getResourceAsStream(resourcePath + ".properties");
            if (null == is) {
                resourcePath = "/" + iconSetName;
                is = loadClass.getResourceAsStream(resourcePath + ".properties");
                if (null == is) {
                    throw new IOException("Icon set properties for " + iconSetName + "(" + loadClass.getName() + ") not found.");
                }
            }
            Properties iconSetProps = new Properties();
            iconSetProps.load(is);
            String extension = iconSetProps.getProperty("extension", "png");
            Image iconImage = createImages(loadClass, resourcePath, extension);
            theInstance = new IconManager(iconSetProps, iconImage);
        } catch (IOException e) {
            System.err.println("IconManager: can't load icon set for " + iconSetName + "(" + loadClass.getName() + "): " + e);
        }
        return theInstance;
    }

    /**
	 * L�se Ressourcenname mit ../ und ./ auf.
	 * 
	 * @param ref die Referenzklasse
	 * @param name der Ressourcenname
	 * @return den aufgel�sten Namen
	 */
    private static String resolveName(Class ref, String name) {
        Vector<String> pathComponents = new Vector<String>();
        StringTokenizer tok;
        if (!name.startsWith("/")) {
            String packageName = ref.getPackage().getName();
            tok = new StringTokenizer(packageName, ".");
            while (tok.hasMoreTokens()) {
                pathComponents.addElement(tok.nextToken());
            }
        }
        tok = new StringTokenizer(name, "/");
        while (tok.hasMoreTokens()) {
            pathComponents.addElement(tok.nextToken());
        }
        for (int i = 0; i < pathComponents.size(); i++) {
            if (((String) pathComponents.get(i)).equals("..") && i >= 1) {
                pathComponents.removeElementAt(i);
                pathComponents.removeElementAt(i - 1);
                i -= 2;
            }
            if (((String) pathComponents.get(i)).equals(".")) {
                pathComponents.removeElementAt(i);
            }
        }
        StringBuffer res = new StringBuffer();
        for (Enumeration<String> iterator = pathComponents.elements(); iterator.hasMoreElements(); ) {
            res.append("/");
            res.append((String) iterator.nextElement());
        }
        return res.toString();
    }

    /**
	 * Erzeugt ein Iconbild
	 * 
	 * @param loadClass die Klasse, von der aus relativ gesehen nach Iconbild und
	 *          Eigenschaftsdatei gesucht wird.
	 * @param extension Die Erweiterung des Bildes (Suffix)
	 * @param resourcePath Name des Bildes (ohne Suffix)
	 */
    private static Image createImages(Class loadClass, String resourcePath, String extension) throws IOException {
        InputStream is;
        Image iconImage = null;
        is = loadClass.getResourceAsStream(resourcePath + "." + extension);
        if (null == is) {
            throw new IOException("InputStream was null");
        }
        iconImage = loadNativeImage(is);
        if (null == iconImage) {
            throw new IOException("Image was null");
        }
        mediaTrackerImage(iconImage);
        return iconImage;
    }

    /**
	 * Stellt sicher, dass das Bild komplett mittels <code>MediaTracker</code>
	 * erzeugt wurde.
	 * 
	 * @param iconImage zu �berpr�fendes Bild
	 */
    private static int mediaTrackerImage(Image iconImage) {
        synchronized (tracker) {
            tracker.addImage(iconImage, 0);
            try {
                tracker.waitForID(0, 0);
            } catch (InterruptedException e) {
                System.err.println("IconManager: warning: interrupted while loading Image");
            }
            int status = tracker.statusID(0, true);
            tracker.removeImage(iconImage, 0);
            return status;
        }
    }

    /**
	 * L�dt Iconbild als natives Bild.
	 * 
	 * @param is Der <code>InputStream</code> von dem geladen werden soll.
	 * @return Ein <code>java.awt.Image</code>
	 */
    private static final Image loadNativeImage(InputStream is) throws IOException {
        BufferedInputStream in = new BufferedInputStream(is);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int n;
        while ((n = in.read(buffer)) > 0) {
            out.write(buffer, 0, n);
        }
        in.close();
        out.flush();
        buffer = out.toByteArray();
        if (buffer.length == 0) {
            throw new IOException("image has zero-length");
        }
        Image theImage = Toolkit.getDefaultToolkit().createImage(buffer);
        if (null == theImage) {
            throw new IOException("Image was null");
        }
        if (mediaTrackerImage(theImage) != MediaTracker.COMPLETE) {
            return null;
        }
        return theImage;
    }

    /**
	 * Ermittelt {@link Icon} mit Default-Effekten �ber dessen Namen.
	 * 
	 * @param name Name des Icons.
	 * @return Icon oder <code>null</code>, wenn keins gefunden wurde.
	 */
    public Icon getIcon(String name) {
        return getIcon(name, defaultEffects);
    }

    /**
	 * Ermittelt {@link Icon} mit spezifizierten Effekten �ber dessen Namen.
	 * 
	 * @param name Name des Icons.
	 * @param effectFlags Eine Kombination aus den Effekt-Flags.
	 * @return Icon oder <code>null</code>, wenn keins gefunden wurde.
	 * 
	 * @see #AUTO_DISABLE
	 * @see #AUTO_ROLLOVER
	 * @see #EFFECT_ALPHA50P
	 * @see #EFFECT_DARKER
	 * @see #EFFECT_LIGHTER
	 * @see #EFFECT_GRAY
	 */
    public Icon getIcon(String name, int effectFlags) {
        return getIcon(name, effectFlags, null);
    }

    /**
	 * Ermittelt {@link Icon} mit spezifizierten Effekten und skalierter Gr��e
	 * �ber dessen Namen.
	 * 
	 * @param name Name des Icons.
	 * @param effectFlags Eine Kombination aus den Effekt-Flags.
	 * @param targetSize Ausgabegr��e des Icons.
	 * @return Icon oder <code>null</code>, wenn keins gefunden wurde.
	 * 
	 * @see #AUTO_DISABLE
	 * @see #AUTO_ROLLOVER
	 * @see #EFFECT_ALPHA50P
	 * @see #EFFECT_DARKER
	 * @see #EFFECT_LIGHTER
	 * @see #EFFECT_GRAY
	 */
    public Icon getIcon(String name, int effectFlags, Dimension targetSize) {
        Rectangle iconRectangle[] = getIconRectangle(name);
        if (null != iconRectangle) {
            return new IconManagerIcon(iconRectangle, effectFlags, targetSize);
        }
        return null;
    }

    /**
	 * Holt ein {@link Image} des Icons, anstatt das Icon selbst.
	 * 
	 * @param name Name des Icons.
	 * @param effectFlags Eine Kombination aus den Effekt-Flags.
	 * @return Image oder <code>null</code>, wenn keins gefunden wurde.
	 * 
	 * @see #AUTO_DISABLE
	 * @see #AUTO_ROLLOVER
	 * @see #EFFECT_ALPHA50P
	 * @see #EFFECT_DARKER
	 * @see #EFFECT_LIGHTER
	 * @see #EFFECT_GRAY
	 */
    public Image getImage(String name, int effectFlags) {
        Rectangle iconRectangle[] = getIconRectangle(name);
        Image image = null;
        if (null != iconRectangle && iconRectangle.length > 0) {
            if (iconRectangle.length != 0) {
                image = new BufferedImage(iconRectangle[0].width, iconRectangle[0].height, BufferedImage.TYPE_INT_ARGB);
                Graphics g2 = image.getGraphics();
                for (int j = 0; j < iconRectangle.length; j++) {
                    Rectangle r = iconRectangle[j];
                    Image i = getImageForEffect(effectFlags);
                    g2.drawImage(i, -r.x, -r.y, null);
                }
            } else {
                System.out.println("Image is " + image);
            }
        }
        return image;
    }

    /**
	 * Holt ein {@link Image} des Icons, anstatt das Icon selbst.
	 * 
	 * @param name Name des Icons.
	 * @return Image oder <code>null</code>, wenn keins gefunden wurde.
	 */
    public Image getImage(String name) {
        return getImage(name, 0);
    }

    /**
	 * Holt sich anhand des Namens das Rechteck des Icons.
	 * 
	 * @param name Name des Icons.
	 * @return Ein {@link Rectangle} oder <code>null</code>, wenn kein Icon
	 *         gefunden wurde.
	 */
    private Rectangle[] getIconRectangle(String name) {
        Rectangle iconRectangle[] = null;
        Object propsEntry = properties.getProperty("icon." + name + ".rectangle");
        if (null != propsEntry) {
            StringTokenizer tok = new StringTokenizer((String) propsEntry, " \t,");
            try {
                iconRectangle = new Rectangle[] { new Rectangle(Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken()), Integer.parseInt(tok.nextToken())) };
            } catch (Exception e) {
                System.err.println("IconManager: Warning: Can't parse the icon rectangle for " + name);
            }
        } else {
            propsEntry = properties.getProperty("icon." + name + ".composite");
            if (null != propsEntry) {
                List<Rectangle> rectangles = new LinkedList<Rectangle>();
                StringTokenizer tok = new StringTokenizer((String) propsEntry, " \t,");
                try {
                    while (tok.hasMoreTokens()) {
                        Rectangle r[] = getIconRectangle(tok.nextToken());
                        if (null != r) {
                            for (int i = 0; i < r.length; i++) {
                                if (null != r[i]) {
                                    rectangles.add(r[i]);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Can't parse the icon composite for " + name);
                }
                iconRectangle = new Rectangle[rectangles.size()];
                rectangles.toArray(iconRectangle);
            } else {
                System.out.println("Can't find icon named " + name);
            }
        }
        return iconRectangle;
    }

    /**
	 * 
	 * 
	 * @param effect Eine OR-Kombination von Effektflags
	 * @see #EFFECT_ALPHA50P
	 * @see #EFFECT_DARKER
	 * @see #EFFECT_LIGHTER
	 * @see #EFFECT_GRAY
	 * @return the image instance
	 */
    protected Image getImageForEffect(int effect) {
        effect &= ~(AUTO_DISABLE | AUTO_ROLLOVER);
        if (effect == 0) return iconImage;
        Integer ei = new Integer(effect);
        if (!effectImages.containsKey(ei)) {
            ImageProducer prod = iconImage.getSource();
            Image effectImage = Toolkit.getDefaultToolkit().createImage(prod);
            mediaTrackerImage(effectImage);
            effectImages.put(ei, effectImage);
        }
        return (Image) effectImages.get(ei);
    }

    /**
	 * Die {@link Icon}-Implementation f�r Verarbeitung im
	 * <code>IconManager</code>
	 */
    public class IconManagerIcon implements Icon {

        private Rectangle iconRectangles[] = null;

        private int effect = 0;

        private Dimension targetSize = null;

        private boolean useScaling = false;

        /**
		 * Konstruktor f�r IconManagerIcon.
		 * 
		 * @param iconRectangles
		 * @param effect
		 * @param targetSize
		 */
        protected IconManagerIcon(Rectangle iconRectangles[], int effect, Dimension targetSize) {
            super();
            this.iconRectangles = iconRectangles;
            this.effect = effect;
            if (null != targetSize) {
                this.targetSize = targetSize;
            } else {
                this.targetSize = iconRectangles[0].getSize();
                useScaling = this.targetSize.width != iconRectangles[0].width || this.targetSize.height != iconRectangles[0].height;
            }
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            int usedEffect = effect;
            if ((effect & AUTO_DISABLE) != 0 && !c.isEnabled()) {
                usedEffect = autoDisableFlags;
            }
            try {
                if ((effect & AUTO_ROLLOVER) != 0 && (c instanceof JButton)) {
                    JButton b = (JButton) c;
                    if (b.isRolloverEnabled() && b.getModel().isRollover()) {
                        usedEffect |= autoRolloverFlags;
                    }
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            Object savedRenderingHint = null;
            try {
                if (useScaling && g instanceof Graphics2D) {
                    Graphics2D g2 = (Graphics2D) g;
                    savedRenderingHint = g2.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
            for (int i = 0; i < iconRectangles.length; i++) if (null != iconRectangles[i]) g.drawImage(getImageForEffect(usedEffect), x, y, x + targetSize.width, y + targetSize.height, iconRectangles[i].x, iconRectangles[i].y, iconRectangles[i].x + iconRectangles[i].width, iconRectangles[i].y + iconRectangles[i].height, c);
            try {
                if (null != savedRenderingHint) {
                    ((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, savedRenderingHint);
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }

        public int getIconWidth() {
            return targetSize.width;
        }

        public int getIconHeight() {
            return targetSize.height;
        }
    }

    public int getAutoDisableFlags() {
        return autoDisableFlags;
    }

    public void setAutoDisableFlags(int autoDisableFlags) {
        this.autoDisableFlags = autoDisableFlags;
    }

    public int getAutoRolloverFlags() {
        return autoRolloverFlags;
    }

    public void setAutoRolloverFlags(int autoRolloverFlags) {
        this.autoRolloverFlags = autoRolloverFlags;
    }

    public int getDefaultEffects() {
        return defaultEffects;
    }

    public void setDefaultEffects(int defaultEffects) {
        this.defaultEffects = defaultEffects;
    }
}
