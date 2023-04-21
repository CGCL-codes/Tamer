package it.paolomind.pwge.utils;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * Utilità generiche per la manipolazione delle immagini.
 * @author paolomind
 */
public final class ImageUtils {

    /**
     * restituisce la giusta dimensione proporzionata alla originale. Così che l'immagine non si
     * deformi per le nuove dimensioni
     * @param dimNew ridimensionamento
     * @param dimOld dimensione originale
     * @return nuova dimensione proporzionata
     */
    public static Dimension getRatio(final Dimension dimNew, final Dimension dimOld) {
        int thumbWidth = dimNew.width;
        int thumbHeight = dimNew.height;
        double thumbRatio = (double) thumbWidth / (double) thumbHeight;
        int imageWidth = dimOld.width;
        int imageHeight = dimOld.height;
        double imageRatio = (double) imageWidth / (double) imageHeight;
        if (thumbRatio < imageRatio) {
            thumbHeight = (int) (thumbWidth / imageRatio);
        } else {
            thumbWidth = (int) (thumbHeight * imageRatio);
        }
        return new Dimension(thumbWidth, thumbHeight);
    }

    /**
     * Calcola le coordinate che si possono raggiungere.
     * @param map dimensioni originali
     * @param area dimensioni dell'area
     * @return le coordinate massime visibili.
     */
    public static Point maxPoint(final Dimension map, final Dimension area) {
        return new Point(map.width - area.width, map.height - area.height);
    }

    /**
     * Calcola le coordinate che si possono raggiungere.
     * @param map dimensioni originali
     * @param area dimensioni dell'area
     * @param zoom il fattore di ingrandimento
     * @return le coordinate massime visibili.
     */
    public static Point maxPoint(final Dimension map, final Dimension area, final double zoom) {
        double mwz = map.width / zoom;
        double mhz = map.height / zoom;
        double maxX = (mwz - area.width) * zoom;
        double maxY = (mhz - area.height) * zoom;
        return new Point((int) maxX, (int) maxY);
    }

    /**
     * Calcola il fattore di ingrandimento massimo, perchè l'immagine occupi l'intera area.
     * @param map dimensioni originali
     * @param area dimensioni dell'area
     * @return il fattore di ingrandimento massimo.
     */
    public static double maxZoom(final Dimension map, final Dimension area) {
        double maxZoom;
        if (map.width / map.height < 1) {
            maxZoom = ((double) map.width) / ((double) area.width);
        } else {
            maxZoom = ((double) map.height) / ((double) area.width);
        }
        return maxZoom;
    }

    /**
     * Legge da un file le informazioni sull'immagine.
     * @param url l'url dell'immagine.
     * @return le proprietà dell'immagine
     * @throws IOException in caso di errori di lettura
     */
    public static Map<String, Object> readMetaData(final String url) throws IOException {
        URL u = AURLUtil.toURL(url);
        if (u == null) {
            throw new FileNotFoundException(url);
        }
        return readMetaData(u);
    }

    /**
     * Legge da un file le informazioni sull'immagine.<br>
     * Informazioni: <br>
     * <li>KEYS.IMAGE_KEYS.K_SIZE = dimensione dell'immagine in pixel</li>
     * <li>KEYS.IMAGE_KEYS.K_FORMAT = formato dell'immagine</li>
     * @param u l'url dell'immagine.
     * @return le proprietà dell'immagine
     * @throws IOException in caso di errori di lettura
     */
    public static Map<String, Object> readMetaData(final URL u) throws IOException {
        InputStream uin = null;
        Hashtable<String, Object> params = new Hashtable<String, Object>();
        if (u != null) {
            try {
                uin = u.openStream();
                ImageInputStream iis = ImageIO.createImageInputStream(uin);
                ImageReader reader = ImageIO.getImageReaders(iis).next();
                int index = reader.getMinIndex();
                reader.setInput(iis, true, true);
                params.put(KEYS.IMAGE.K_SIZE, new Dimension(reader.getWidth(index), reader.getHeight(index)));
                params.put(KEYS.IMAGE.K_FORMAT, reader.getFormatName());
            } finally {
                if (uin != null) {
                    uin.close();
                }
            }
        }
        return params;
    }

    /**
     * Costruisce una nuova immagine ridimensionata.
     * @param image immagine da ridimensionare
     * @param dim le nuove dimensioni
     * @param prop true se si vuole mantenere le proporzioni
     * @return la nuova immagine ridimensionata
     */
    public static BufferedImage scaleImage(final Image image, final Dimension dim, final boolean prop) {
        Dimension dimNew = (prop ? getRatio(dim, new Dimension(image.getWidth(null), image.getHeight(null))) : dim);
        BufferedImage thumbImage = new BufferedImage(dimNew.width, dimNew.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = thumbImage.createGraphics();
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphics2D.drawImage(image, 0, 0, dimNew.width, dimNew.height, null);
        return thumbImage;
    }

    /** */
    private ImageUtils() {
    }
}
