package com.umc.gfx;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import org.apache.log4j.Logger;
import com.umc.beans.BackdropImage;
import com.umc.beans.MovieFile;
import com.umc.helper.UMCConstants;

/**
 * Diese Klasse stellt diverse Methoden für Bilddateien zur Verfügung.
 *
 *
 * @author DonGyros
 * @version 0.1
 */
public class ImageUtils {

    private static Logger log = Logger.getLogger("com.umc.console");

    /**Gibt die Positionierung "mittig" an*/
    public static final int CENTER = 0;

    /**Gibt die Positionierung "oben links" an*/
    public static final int NORTHWEST = 1;

    /**Gibt die Positionierung "oben rechts" an*/
    public static final int NORTHEAST = 2;

    /**Gibt die Positionierung "unten links" an*/
    public static final int SOUTHWEST = 3;

    /**Gibt die Positionierung "unten rechts" an*/
    public static final int SOUTHEAST = 4;

    /**
     * Mit Hilfe dieser Methode kann ein Bild skaliert werden. Die Seitenverhältnisse werden dabei berücksichtigt.
     *
     * @param imgPath Pfad zur Grafik
     * @param imgX Breite in Pixel für das Image
     * @param imgY Höhe in Pixel für das Image
     * @param thumbnail Gibt an ob ein Thumbnail erstellt werden soll oder nicht (wird für den zu verwendeten Algorithmus der Skalierung benötigt => bei Thumbnail nur geringe Qualität)
     * @return {@link BufferedImage}
     */
    public static BufferedImage scaleImage(String imgPath, int imgX, int imgY, boolean thumbnail) {
        Image image = Toolkit.getDefaultToolkit().createImage(imgPath);
        int hint = Image.SCALE_SMOOTH;
        if (thumbnail) hint = Image.SCALE_FAST;
        BufferedImage buffImg = null;
        try {
            Image scaled = image.getScaledInstance(imgX, imgY, hint);
            buffImg = new BufferedImage(imgX, imgY, BufferedImage.TYPE_INT_RGB);
            buffImg.createGraphics().drawImage(scaled, 0, 0, null);
        } catch (Exception e) {
            System.out.println("FEHLER in ImageScaler.scaleImage(): " + e);
        }
        return buffImg;
    }

    /**
	 * Mit Hilfe dieser Methode kann ein Bild,unter Berücksichtigung des Verhältnisses, skaliert werden.
	 * 
	 * @param path Der vollständige Pfad zum Bild
	 * @param maxWidth die maximal gewünschte Breite des Bildes
	 * @param maxHeight die maximal gewünschte Höhe des Bildes
	 * @param hints Der Algorithmus der für das skalieren des Bildes benutzt werden soll
	 * @param fitType Gibt an ob die Breite(maxWidth)=2,die Höhe(maxHeight)=3 bzw. beides=1 berücksichtigt werden soll
	 * @return Das erzeugte Thumbnail
	 * 
	 * @see RenderingHints
	 */
    public static BufferedImage scaleToFit(String path, int maxWidth, int maxHeight, RenderingHints hints, int fitType) {
        Image img = new ImageIcon(path).getImage();
        BufferedImage image = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
        image.createGraphics().drawImage(img, 0, 0, null);
        BufferedImage thumbnail = null;
        if (image != null) {
            AffineTransform tx = new AffineTransform();
            double scale = 0;
            switch(fitType) {
                case 1:
                    scale = scaleToFitBoth(image.getWidth(), image.getHeight(), maxWidth, maxHeight);
                    break;
                case 2:
                    scale = scaleToFitHorizontal(image.getWidth(), image.getHeight(), maxWidth, maxHeight);
                    break;
                case 3:
                    scale = scaleToFitVertical(image.getWidth(), image.getHeight(), maxWidth, maxHeight);
                    break;
                default:
                    scale = scaleToFitBoth(image.getWidth(), image.getHeight(), maxWidth, maxHeight);
                    break;
            }
            tx.scale(scale, scale);
            double d1 = (double) image.getWidth() * scale;
            double d2 = (double) image.getHeight() * scale;
            thumbnail = new BufferedImage(((int) d1) < 1 ? 1 : (int) d1, ((int) d2) < 1 ? 1 : (int) d2, image.getType() == BufferedImage.TYPE_CUSTOM ? BufferedImage.TYPE_INT_RGB : image.getType());
            Graphics2D g2d = thumbnail.createGraphics();
            g2d.setRenderingHints(hints);
            g2d.drawImage(image, tx, null);
            g2d.dispose();
        }
        return thumbnail;
    }

    /**
	 * Unter Berücksichtigung der Breite und der Höhe.
	 * 
	 * @param w1 Breite des Ausgangs-Bildes
	 * @param h1 Höhe des Ausgangs-Bildes
	 * @param w2 Breite des gewünschten Thumbnails
	 * @param h2 Höhe des gewünschten Thumbnails
	 * @return
	 */
    private static double scaleToFitBoth(double w1, double h1, double w2, double h2) {
        double scale = 1.0D;
        if (w1 > h1) {
            if (w1 > w2) scale = w2 / w1;
            h1 *= scale;
            if (h1 > h2) scale *= h2 / h1;
        } else {
            if (h1 > h2) scale = h2 / h1;
            w1 *= scale;
            if (w1 > w2) scale *= w2 / w1;
        }
        return scale;
    }

    /**
	 * Unter Berücksichtigung der Breite.
	 * 
	 * @param w1 Breite des Ausgangs-Bildes
	 * @param h1 Höhe des Ausgangs-Bildes
	 * @param w2 Breite des gewünschten Thumbnails
	 * @param h2 Höhe des gewünschten Thumbnails
	 * @return
	 */
    private static double scaleToFitHorizontal(double w1, double h1, double w2, double h2) {
        double scale = 1.0D;
        if (w1 > h1) {
            if (w1 > w2) scale = w2 / w1;
            h1 *= scale;
        } else {
            w1 *= scale;
            if (w1 > w2) scale *= w2 / w1;
        }
        return scale;
    }

    /**
	 * Unter Berücksichtigung der Höhe.
	 * 
	 * @param w1 Breite des Ausgangs-Bildes
	 * @param h1 Höhe des Ausgangs-Bildes
	 * @param w2 Breite des gewünschten Thumbnails
	 * @param h2 Höhe des gewünschten Thumbnails
	 * @return
	 */
    private static double scaleToFitVertical(double w1, double h1, double w2, double h2) {
        double scale = 1.0D;
        if (w1 > h1) {
            h1 *= scale;
            if (h1 > h2) scale *= h2 / h1;
        } else {
            if (h1 > h2) scale = h2 / h1;
            w1 *= scale;
        }
        return scale;
    }

    /**
     * Fügt einem Bild eine Signatur hinzu.
     *
     * @param path Vollständiger Pfad zum Bild
     * @param signature Die Signatur die gesetzt werden soll
     * @param font Die Schriftart die verwendet werden soll
     * @param location Gibt an wo die Signatur angebracht werden soll (1=oben links, 2=oben recht,3=unten links,4=unten rechts)
     * @return BufferedImage Das Bild mit der eingefügten Signatur
     *
     * @see Image
     * @see Font
     */
    public static BufferedImage addSignatureToImage(String path, String signature, Font font, int location) {
        BufferedImage bi = null;
        try {
            File f = new File(path);
            bi = ImageIO.read(f);
            Graphics g = bi.getGraphics();
            g.setFont(font);
            FontMetrics MyFM = g.getFontMetrics();
            int signatureWidth = MyFM.stringWidth(signature);
            int borderSpacing = 2;
            switch(location) {
                case NORTHWEST:
                    g.drawString(signature, borderSpacing, borderSpacing);
                    break;
                case NORTHEAST:
                    g.drawString(signature, bi.getWidth() - signatureWidth - borderSpacing, borderSpacing);
                    break;
                case SOUTHWEST:
                    g.drawString(signature, borderSpacing, bi.getHeight() - borderSpacing);
                    break;
                case SOUTHEAST:
                    g.drawString(signature, bi.getWidth() - signatureWidth - borderSpacing, bi.getHeight() - borderSpacing);
                    break;
            }
            g.dispose();
            return bi;
        } catch (IOException e1) {
            return bi;
        }
    }

    /**
     * Generiert ein "leeres" transparentes Bild für eine weitere Verwendung.
     * 
     * @param aWidth Breite des Bildes
     * @param aHeight Höhe des Bildes;
     * @param alpha Mit diesem Wert kann die Transparenz prozentual eingestellt werden. 1.0f -> voll sichtbar, 0.0f -> ganz durchsichtig
     * @return
     */
    public static BufferedImage createTransparentImage(int aWidth, int aHeight, float alpha) {
        BufferedImage image = image = new BufferedImage(aWidth, aHeight, BufferedImage.TRANSLUCENT);
        Graphics2D g = image.createGraphics();
        AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
        g.setComposite(alphaComposite);
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return image;
    }

    /**
     * Mit Hilfe dieser Methode kann ein Bild für einen übergebenen Text erzeugt werden.
     * Wird benötigt um Text mit Hilfe von Bilder auf dem PCH darzustellen. Der PCH hat 
     * Probleme Text zu rendern (in Kombinaation mit Coverflow usw.). 
     * Durch Einsatz von Grafiken kann die Performance verbessert werden.
     * 
     * @param aText Ein Text der gezeichnet werden soll
     * @param fontSize Größe des Textes
     * @param aWidth Breite des Bildes (0 für Auto-Size)
     * @param aHeight Höhe des Bildes (0 für Auto-Size)
     * @param autoSize Gibt an ob das Bild an die Dimensionen des Textes angepasst werden soll
     * @param location Gibt an wo der Text angebracht werden soll (o=mittig, 1=oben links, 2=oben recht,3=unten links,4=unten rechts)
     * @param backgroundColor Hintergrundfarbe die verwendet werden soll (wird noch nicht unterstützt)
     * @param textColor Textfarbe die verwendet werden soll
     * @param transparent soll das Bild mit einem transparenten Hintergrund erzeugt werden
     * @param fileType jpg,png usw.
     * @param saveTo gibt an wo das Bild gespeichert werden soll
     */
    public static void createImageForText(String aText, int fontSize, int aWidth, int aHeight, boolean autoSize, int location, Color backgroundColor, Color textColor, boolean transparent, String fileType, String saveTo) {
        BufferedImage image = null;
        if (transparent) image = createTransparentImage(aWidth, aHeight, 0.5f); else image = new BufferedImage(aWidth, aHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        FontMetrics MyFM = g.getFontMetrics();
        int textWidth = MyFM.stringWidth(aText);
        int textHeight = MyFM.getHeight();
        if (autoSize) {
            if (transparent) image = createTransparentImage(textWidth, textHeight, 0.5f); else image = new BufferedImage(textWidth, textHeight, BufferedImage.TYPE_INT_ARGB);
            g = image.createGraphics();
            g.setFont(new Font("Arial", Font.PLAIN, fontSize));
        }
        g.setColor(textColor);
        int borderSpacing = 2;
        switch(location) {
            case CENTER:
                g.drawString(aText, (image.getWidth() / 2) - (textWidth / 2), (image.getHeight() / 2) + 4);
                break;
            case NORTHWEST:
                g.drawString(aText, borderSpacing, 0 + textHeight);
                break;
            case NORTHEAST:
                g.drawString(aText, image.getWidth() - textWidth - borderSpacing, borderSpacing);
                break;
            case SOUTHWEST:
                g.drawString(aText, borderSpacing, image.getHeight() - borderSpacing);
                break;
            case SOUTHEAST:
                g.drawString(aText, image.getWidth() - textWidth - borderSpacing, image.getHeight() - borderSpacing);
                break;
        }
        g.dispose();
        try {
            log.debug("speichere Bild " + saveTo);
            ImageIO.write(image, fileType, new File(saveTo));
        } catch (IOException exc) {
            log.error("Fehler in ImageUtils.createImageForText()", exc);
        }
    }

    /**
     * Erzeugt zwei Buttons (normal und OnMouseOver) für die Tastatur auf der HTML-Seite.
     * 
     * @param keyType 1 = Genre-Button, 2 = Alphanummerischer Button (A-Z,0-9)
     * @param key Text der auf den Button gezeichnet werden soll
     * @param c1 Farbe für Text im Normalzustand
     * @param c2 Farbe für Text bei OnMouseOver
     */
    public static void createKeyboardButton(int keyType, String key, Color c1, Color c2) {
        BufferedImage template = null;
        if (keyType == 1) template = loadImage(System.getProperty("user.dir") + "/resources/HTML/pics/Keyboard/taste_genre.png"); else template = loadImage(System.getProperty("user.dir") + "/resources/HTML/pics/Keyboard/taste.gif");
        BufferedImage image = createTransparentImage(template.getWidth(), template.getHeight(), 0.0f);
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        Graphics2D g = image.createGraphics();
        g.setRenderingHints(renderingHints);
        g.drawImage(template, null, 0, 0);
        g.setColor(c1);
        Font font = new Font("HAETTENSCHWEILER", Font.PLAIN, 45);
        if (keyType == 1) font = new Font("HAETTENSCHWEILER", Font.PLAIN, 15);
        g.setFont(font);
        if (keyType == 1) g.drawString(key, 20, 38); else g.drawString(key, 20, 45);
        String fileExtension = key.toLowerCase().replaceAll("/", "_");
        ImageUtils.saveImage(image, System.getProperty("user.dir") + "/resources/HTML/pics/Keyboard/taste_" + fileExtension + "_1.png");
        g.dispose();
        image = createTransparentImage(template.getWidth(), template.getHeight(), 0.0f);
        g = image.createGraphics();
        g.setRenderingHints(renderingHints);
        g.drawImage(template, null, 0, 0);
        g.setColor(c2);
        g.setFont(font);
        if (keyType == 1) g.drawString(key, 20, 38); else g.drawString(key, 20, 45);
        key = key.toLowerCase().replaceAll("/", "_");
        ImageUtils.saveImage(image, System.getProperty("user.dir") + "/resources/HTML/pics/Keyboard/taste_" + fileExtension + "_2.png");
        g.dispose();
    }

    /**
     * Liefert alle Zeichensätze die auf dem System installiert sind.
     * 
     * @return Liste mit allen gefundenen Zeichensätzen (z.B. Arial ,Arial Black , Arial Narrow , ...., Wingdings ,Wingdings 2 ,...)
     */
    public static Collection<String> getAvailableFontFamilyNames() {
        Collection<String> result = new ArrayList<String>();
        for (String fonts : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()) {
            result.add(fonts);
        }
        return result;
    }

    /**
	 * Ein Bild laden.
	 * 
	 * @param ref vollständiger Pfad zum Bild
	 * @return Das geladene Bild
	 */
    public static BufferedImage loadImage(String ref) {
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new File(ref));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimg;
    }

    public static synchronized BufferedImage createMovieDetail(int width, int height, MovieFile mf) throws Exception {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
        BufferedImage resizedImg = null;
        if (mf.getBackdrop() != null) {
            if (new File(mf.getBackdrop().getRootPath() + mf.getBackdrop().getRelativePath()).exists()) resizedImg = scaleToFit(mf.getBackdrop().getRootPath() + mf.getBackdrop().getRelativePath(), height, height, renderingHints, 3);
        }
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHints(renderingHints);
        LinearGradientPaint grayToTransparent = null;
        if (resizedImg != null) {
            Point2D startPoint = new Point2D.Float(dimg.getWidth() - resizedImg.getWidth(), 0);
            Point2D endPoint = new Point2D.Float(dimg.getWidth(), 0);
            float[] dist = { 0.0f, 1.0f };
            Color[] colors = { new Color(40, 41, 59, 255), new Color(40, 41, 59, 0) };
            grayToTransparent = new LinearGradientPaint(startPoint, endPoint, dist, colors);
        }
        if (resizedImg != null) g.drawImage(resizedImg, null, dimg.getWidth() - resizedImg.getWidth(), 0);
        g.setColor(new Color(40, 41, 59));
        if (resizedImg != null) g.fillRect(0, 0, dimg.getWidth() - resizedImg.getWidth(), dimg.getHeight()); else g.fillRect(0, 0, dimg.getWidth(), dimg.getHeight());
        if (resizedImg != null) {
            g.setColor(new Color(255, 255, 255, 50));
            int start = dimg.getWidth() - resizedImg.getWidth();
            int x = dimg.getWidth() - resizedImg.getWidth();
            int y = 20;
            for (int a = dimg.getWidth() - resizedImg.getWidth(); a <= dimg.getWidth(); a++) {
                if (a < (dimg.getWidth() + 1)) {
                    g.drawLine(x, y, 1100, y);
                    g.drawLine(start, 0, start, 502);
                    start += 10;
                    y += 10;
                }
            }
        }
        if (resizedImg != null) {
            g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            g.setPaint(grayToTransparent);
            g.fillRect(dimg.getWidth() - resizedImg.getWidth(), 0, resizedImg.getWidth(), dimg.getHeight());
        }
        g.setRenderingHints(renderingHints);
        int posY = 40;
        FontMetrics MyFM = g.getFontMetrics();
        int posYear = 0;
        g.setColor(new Color(196, 196, 196, 255));
        Font font = new Font("HAETTENSCHWEILER", Font.PLAIN, 45);
        g.setFont(font);
        MyFM = g.getFontMetrics();
        posYear = MyFM.stringWidth(mf.getTitle()) + 20;
        g.drawString(mf.getTitle(), 10, 40);
        font = new Font("HAETTENSCHWEILER", Font.PLAIN, 30);
        g.setFont(font);
        g.drawString("(" + mf.getYear() + ")", posYear, 40);
        BufferedImage mediaInfo = null;
        if (mf.getResolution() != null && !mf.getResolution().equals("")) {
            if (mf.getResolution().equals(UMCConstants.res576i)) {
                mediaInfo = loadImage("resources/sd576i.png");
            } else if (mf.getResolution().equals(UMCConstants.res576p)) {
                mediaInfo = loadImage("resources/sd576p.png");
            } else if (mf.getResolution().equals(UMCConstants.res720p)) {
                mediaInfo = loadImage("resources/hd720p.png");
            } else if (mf.getResolution().equals(UMCConstants.res1080i)) {
                mediaInfo = loadImage("resources/hd1080i.png");
            } else if (mf.getResolution().equals(UMCConstants.res1080p)) {
                mediaInfo = loadImage("resources/hd1080p.png");
            }
            if (mediaInfo != null) g.drawImage(mediaInfo, null, posYear + MyFM.stringWidth("" + mf.getYear()) + 20, 10);
        }
        mediaInfo = null;
        if (mf.getFSK() > -1) {
            switch(mf.getFSK()) {
                case 0:
                    mediaInfo = loadImage("resources/fsk_0_white.png");
                    break;
                case 6:
                    mediaInfo = loadImage("resources/fsk_6_white.png");
                    break;
                case 12:
                    mediaInfo = loadImage("resources/fsk_12_white.png");
                    break;
                case 16:
                    mediaInfo = loadImage("resources/fsk_16_white.png");
                    break;
                case 18:
                    mediaInfo = loadImage("resources/fsk_18_white.png");
                    break;
            }
            g.drawImage(mediaInfo, null, posYear + MyFM.stringWidth("" + mf.getYear()) + 105, 10);
        }
        posY = 60;
        if (mf.getRatingOFDB() > 0) {
            switch(mf.getRatingOFDB()) {
                case 0:
                    g.drawImage(loadImage("resources/rating_000.png"), null, 10, posY);
                    break;
                case 1:
                    g.drawImage(loadImage("resources/rating_010.png"), null, 10, posY);
                    break;
                case 2:
                    g.drawImage(loadImage("resources/rating_020.png"), null, 10, posY);
                    break;
                case 3:
                    g.drawImage(loadImage("resources/rating_030.png"), null, 10, posY);
                    break;
                case 4:
                    g.drawImage(loadImage("resources/rating_040.png"), null, 10, posY);
                    break;
                case 5:
                    g.drawImage(loadImage("resources/rating_050.png"), null, 10, posY);
                    break;
                case 6:
                    g.drawImage(loadImage("resources/rating_060.png"), null, 10, posY);
                    break;
                case 7:
                    g.drawImage(loadImage("resources/rating_070.png"), null, 10, posY);
                    break;
                case 8:
                    g.drawImage(loadImage("resources/rating_080.png"), null, 10, posY);
                    break;
                case 9:
                    g.drawImage(loadImage("resources/rating_090.png"), null, 10, posY);
                    break;
                case 10:
                    g.drawImage(loadImage("resources/rating_100.png"), null, 10, posY);
                    break;
            }
            posY = 100;
        }
        if (mf.getCast().size() > 0) {
            font = new Font("Arial", Font.PLAIN, 12);
            g.setFont(font);
            g.setColor(new Color(196, 196, 196, 255));
            g.drawString(mf.getCastCommaSeparated(), 10, posY);
            posY = 140;
        }
        if (mf.getStory() != null && mf.getStory().length() > 0) {
            font = new Font("Arial", Font.PLAIN, 20);
            g.setFont(font);
            MyFM = g.getFontMetrics();
            g.setColor(new Color(196, 196, 196, 255));
            Scanner sc = new Scanner(mf.getStory());
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {
                sb.append(sc.next() + " ");
                if (MyFM.stringWidth(sb.toString()) > 600) {
                    g.drawString(sb.toString(), 10, posY);
                    sb.delete(0, sb.toString().length());
                    posY += 20;
                }
            }
            g.drawString(sb.toString(), 10, posY);
        }
        g.setColor(new Color(255, 255, 255, 100));
        BufferedImage scene = null;
        int screenshotX = 5;
        File f = null;
        for (String previewScreenshot : mf.getPreviewScreenshots()) {
            f = new File(previewScreenshot);
            if (f.exists()) {
                scene = scaleToFit(previewScreenshot, 100, 100, renderingHints, 1);
                g.fillRect(screenshotX, dimg.getHeight() - scene.getHeight() - 20, 2 + scene.getWidth() + 2, 2 + scene.getHeight() + 2);
                g.drawImage(scene, null, screenshotX + 2, dimg.getHeight() - scene.getHeight() - 18);
                screenshotX += 125;
            }
        }
        font = new Font("HAETTENSCHWEILER", Font.PLAIN, 20);
        g.setFont(font);
        g.drawString("Video-Codec: " + mf.getCodec() + " | Framerate: " + mf.getFramerate() + " | Bitrate: " + mf.getBitrate(), screenshotX + 10, dimg.getHeight() - 20);
        g.setColor(new Color(0, 0, 0, 120));
        g.fillRect(dimg.getWidth() - 100, 0, 100, dimg.getHeight());
        font = new Font("Arial", Font.PLAIN, 15);
        g.setFont(font);
        MyFM = g.getFontMetrics();
        g.setColor(new Color(255, 255, 255, 255));
        g.drawString("Play", (dimg.getWidth() - 50) - (MyFM.stringWidth("Play") / 2), 20);
        g.drawString("Delete", (dimg.getWidth() - 50) - (MyFM.stringWidth("Delete") / 2), 45);
        g.drawString("Info", (dimg.getWidth() - 50) - (MyFM.stringWidth("Info") / 2), 70);
        g.drawString("Return", (dimg.getWidth() - 50) - (MyFM.stringWidth("Return") / 2), 95);
        g.drawString("<  >", (dimg.getWidth() - 50) - (MyFM.stringWidth("Return") / 2), dimg.getHeight() - 20);
        if (mf.isMovieGroup() || mf.isMultipartGroup() || mf.isSerienGroup()) {
            int partCount = mf.getMultiparts().size();
            int y = dimg.getHeight() - (partCount * 25);
            for (int a = 1; a <= partCount; a++) {
                g.drawString(a + ") Part " + a, (dimg.getWidth() - 50) - (MyFM.stringWidth("Play") / 2), y);
                y = y + 25;
            }
        }
        g.dispose();
        return dimg;
    }

    /**
	 * Alle Schauspieler aus dem übergebenen {@link MovieFile} werden ausgelesen und die entsprechenden Schauspieler-Bilder
	 * werden geladen.
	 * Diese Bilder werden zusammen mit den Schauspieler-Infos (Name,Geburtstag,..) in das ein neues Schauspielerdetail-Bild
	 * gezeichnet.
	 *
	 * @param width Breite des zu erstellenden Bildes
	 * @param height Höhe des zu erstellenden Bildes
	 * @param mf {@link MovieFile} aus welchem die Filminformationen die gezeichnet werden angefordert werden
	 * 
	 * @see MovieFile
	 */
    public static synchronized BufferedImage createActorDetail(int width, int height, MovieFile mf) throws Exception {
        RenderingHints renderingHints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        renderingHints.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        renderingHints.add(new RenderingHints(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY));
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHints(renderingHints);
        g.setColor(new Color(0, 0, 0, 100));
        g.fillRect(0, 0, dimg.getWidth(), dimg.getHeight());
        BufferedImage resizedActorImg = null;
        int imgPosY = 5;
        int txtPosX = 5;
        Font font = new Font("Arial", Font.PLAIN, 12);
        g.setFont(font);
        g.setColor(new Color(196, 196, 196, 255));
        FontMetrics fm = g.getFontMetrics();
        for (String actor : mf.getCast()) {
            if (new File(System.getProperty("user.dir") + "/resources/Actors/" + actor + ".jpg").exists()) {
                resizedActorImg = scaleToFit(System.getProperty("user.dir") + "/resources/Actors/" + actor + ".jpg", 50, 75, renderingHints, 2);
                if (resizedActorImg != null) {
                    g.drawImage(resizedActorImg, null, 5, imgPosY);
                    txtPosX = txtPosX + resizedActorImg.getWidth() + 10;
                }
            }
            g.drawString(actor, txtPosX, imgPosY + fm.getHeight());
            txtPosX = 5;
            if (resizedActorImg != null) {
                imgPosY = imgPosY + resizedActorImg.getHeight() + 10;
            }
        }
        g.dispose();
        return dimg;
    }

    /** 
	    * Saves a BufferedImage to the given file, pathname must not have any 
	    * periods "." in it except for the one before the format, i.e. C:/images/fooimage.png 
	    * @param img 
	    * @param saveFile 
	    */
    public static void saveImage(BufferedImage img, String ref) {
        try {
            if (img != null) {
                String format = (ref.endsWith(".png")) ? "png" : "jpg";
                ImageIO.write(img, format, new File(ref));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImage(BufferedImage img, ByteArrayOutputStream out, float quality) throws IOException {
        ImageWriter writer = (ImageWriter) ImageIO.getImageWritersByFormatName("jpg").next();
        ImageOutputStream ios = ImageIO.createImageOutputStream(out);
        writer.setOutput(ios);
        ImageWriteParam iwparam = new JPEGImageWriteParam(Locale.getDefault());
        iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        iwparam.setCompressionQuality(quality);
        writer.write(null, new IIOImage(img, null, null), iwparam);
        ios.flush();
        writer.dispose();
        ios.close();
    }

    /**
	    * Bild tranparent machen.
	    * 
	    * @param url
	    * @param transperancy
	    * @return
	    */
    public static BufferedImage loadTranslucentImage(String url, float transperancy) {
        BufferedImage loaded = loadImage(url);
        BufferedImage aimg = new BufferedImage(loaded.getWidth(), loaded.getHeight(), BufferedImage.TRANSLUCENT);
        Graphics2D g = aimg.createGraphics();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, transperancy));
        g.drawImage(loaded, null, 0, 0);
        g.dispose();
        return aimg;
    }

    /**
	    * Eine Farbe transparent machen
	    * @param ref
	    * @param color
	    * @return
	    */
    public static BufferedImage makeColorTransparent(String ref, Color color) {
        BufferedImage image = loadImage(ref);
        BufferedImage dimg = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dimg.createGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(image, null, 0, 0);
        g.dispose();
        for (int i = 0; i < dimg.getHeight(); i++) {
            for (int j = 0; j < dimg.getWidth(); j++) {
                if (dimg.getRGB(j, i) == color.getRGB()) {
                    dimg.setRGB(j, i, 0x8F1C1C);
                }
            }
        }
        return dimg;
    }

    /**
	    * Bild horizontal spiegeln.
	    * 
	    * @param img
	    * @return
	    */
    public static BufferedImage horizontalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null);
        g.dispose();
        return dimg;
    }

    /**
	    * Bild vertikal spiegel.
	    * 
	    * @param img
	    * @return
	    */
    public static BufferedImage verticalflip(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(w, h, img.getColorModel().getTransparency());
        Graphics2D g = dimg.createGraphics();
        g.drawImage(img, 0, 0, w, h, 0, h, w, 0, null);
        g.dispose();
        return dimg;
    }

    /**
	    * Bild drehen/rotieren.
	    * 
	    * @param img
	    * @param angle
	    * @return
	    */
    public static BufferedImage rotate(BufferedImage img, int angle) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(w, h, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.rotate(Math.toRadians(angle), w / 2, h / 2);
        g.drawImage(img, null, 0, 0);
        return dimg;
    }

    /**
	    * Bild skalieren.
	    * 
	    * @param img
	    * @param newW
	    * @param newH
	    * @return
	    */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        int w = img.getWidth();
        int h = img.getHeight();
        BufferedImage dimg = dimg = new BufferedImage(newW, newH, img.getType());
        Graphics2D g = dimg.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(img, 0, 0, newW, newH, 0, 0, w, h, null);
        g.dispose();
        return dimg;
    }

    /**
		 * Lädt ein Bild von einer URL und gibt dieses zurück
		 * 
		 * @param url vollständige URL zum Bild
		 * @return Das geladene Bild
		 */
    public static BufferedImage loadImageFromURL(String url) {
        if (!exists(url)) return null;
        BufferedImage bimg = null;
        try {
            bimg = ImageIO.read(new URL(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bimg;
    }

    public static boolean exists(String URLName) {
        try {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection con = (HttpURLConnection) new URL(URLName).openConnection();
            con.setRequestMethod("HEAD");
            return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        } catch (Exception e) {
            return false;
        }
    }
}
