package common;

import com.sun.opengl.util.BufferUtil;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Image loading class that converts BufferedImages into a data structure that
 * can be easily passed to OpenGL.
 *
 * @author Pepijn Van Eeckhoudt, Timo
 */
public final class TextureReader {

    /**
    * Konstrutor. Ohne Funktion.
    */
    private TextureReader() {
    }

    /**
    * Öffnet die Textur mit dem Namen filename und gibt diese als Objekt vom Typ
    * Texture zurück. Der alphakanal wird standardmäßig deaktiviert.
    *
    * @param filename
    *           Dateiname der Textur
    * @return Texturobjekt
    * @throws IOException
    *            Falls Datei nicht geöffnet werden kann.
    */
    public static Texture readTexture(final String filename) throws IOException {
        return readTexture(filename, false);
    }

    /**
    * Liest die Textur mit dem Namen filename und gibt diese als Objekt vom Typ
    * Texture zurück. Der Alphakanal wird je nach storAlphaChannel gespeichert.
    *
    * @param filename
    *           Dateiname der Textur
    * @param storeAlphaChannel
    *           true -> Alphakanal speichern
    * @return Texturobjekt
    * @throws IOException
    *            Falls Datei nicht geöffnet werden kann.
    */
    public static Texture readTexture(final String filename, final boolean storeAlphaChannel) throws IOException {
        BufferedImage bufferedImage;
        bufferedImage = readImage(filename);
        return readPixels(bufferedImage, storeAlphaChannel);
    }

    /**
    * Liest ein Bild mit dem Namen resourceName ein und gibt dieses als
    * BufferedImage zurück.
    *
    * @param resourceName
    *           Dateiname des Bildes
    * @return BufferedImage, das das Bild enthält.
    * @throws IOException
    *            Falls Bild nicht geöffnet werden kann.
    */
    public static BufferedImage readImage(final String resourceName) throws IOException {
        return ImageIO.read(ResourceRetriever.getResourceAsStream(resourceName));
    }

    /**
    * Liest das Bild img in ein Texturobjekt ein und gibt dieses zurück. Über
    * die Option storeAlphaChannel kann festgelegt werden, ob der Alphakanal
    * gespeichert werden soll.
    *
    * @param img
    *           BufferedImage mit dem Quellbild
    * @param storeAlphaChannel
    *           Wenn true wird der Alphakanal gespeichert
    * @return Texturobjekt
    */
    public static Texture readPixels(final BufferedImage img, boolean storeAlphaChannel) {
        int[] packedPixels = new int[img.getWidth() * img.getHeight()];
        PixelGrabber pixelgrabber = new PixelGrabber(img, 0, 0, img.getWidth(), img.getHeight(), packedPixels, 0, img.getWidth());
        try {
            pixelgrabber.grabPixels();
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        if (img.getType() == BufferedImage.TYPE_CUSTOM) {
            storeAlphaChannel = true;
        }
        int bytesPerPixel = storeAlphaChannel ? 4 : 3;
        ByteBuffer unpackedPixels = BufferUtil.newByteBuffer(packedPixels.length * bytesPerPixel);
        for (int row = img.getHeight() - 1; row >= 0; row--) {
            for (int col = 0; col < img.getWidth(); col++) {
                int packedPixel = packedPixels[row * img.getWidth() + col];
                if (img.getType() == BufferedImage.TYPE_BYTE_GRAY) {
                    unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                } else {
                    unpackedPixels.put((byte) ((packedPixel >> 16) & 0xFF));
                    unpackedPixels.put((byte) ((packedPixel >> 8) & 0xFF));
                    unpackedPixels.put((byte) ((packedPixel >> 0) & 0xFF));
                    if (storeAlphaChannel) {
                        unpackedPixels.put((byte) ((packedPixel >> 24) & 0xFF));
                    }
                }
            }
        }
        unpackedPixels.flip();
        return new Texture(unpackedPixels, img.getWidth(), img.getHeight(), img.getType());
    }

    /**
    * Definiert eine Textur. Enthält Funktionen um die Textur in eine NormalMap
    * (BumpMapping) umzuwandeln.
    *
    * @author timo
    *
    */
    public static class Texture {

        /**
       * Buffer mit den Farbwerten der Textur.
       */
        private ByteBuffer pixels;

        /**
       * Breite der Textur.
       */
        private int width;

        /**
       * Höhe der Textur.
       */
        private int height;

        /**
       * Typ der Textur.
       */
        private int type;

        /**
       * Konstruktor. Erzeugt einen Texturobjekt mit der Breite iwidth, der Höhe
       * iheight und den Farbwerten ipixels. Der Typ der Textur wird über itype
       * festgelegt.
       *
       * @param ipixels
       *           Farbwerte der Textur.
       * @param iwidth
       *           Breite der Textur
       * @param iheight
       *           Höhe der Textur
       * @param itype
       *           Typ der Textur
       */
        public Texture(final ByteBuffer ipixels, final int iwidth, final int iheight, final int itype) {
            this.height = iheight;
            this.pixels = ipixels;
            this.width = iwidth;
            this.type = itype;
        }

        /**
       * Gibt den Typ der Textur zurück.
       *
       * @return Typ der Textur
       */
        public final int getType() {
            return type;
        }

        /**
       * Gibt die Höhe der Textur zurück.
       *
       * @return Höhe der Textur
       */
        public final int getHeight() {
            return height;
        }

        /**
       * Liefert die Farbwerte der Textur zurück.
       *
       * @return Farbwerte der Textur
       */
        public final ByteBuffer getPixels() {
            return pixels;
        }

        /**
       * Gibt die Breite der Textur zurück.
       *
       * @return Breite der Textur
       */
        public final int getWidth() {
            return width;
        }

        /**
       * Wandelt ein 8INT Bild in eine Normalmap um.
       *
       * @param useRGBA
       *           gibt die Reihenfolge der rot / grün / blau bytes an.
       */
        public final void toNormalMap(final boolean useRGBA) {
            int blue, red;
            int w, h, x, y;
            int sx, sy, len, predx, succx, dest;
            if (useRGBA) {
                blue = 0;
                red = 2;
            } else {
                blue = 2;
                red = 0;
            }
            System.out.println("Source: " + pixels.capacity() + "(" + width + "," + height + ")");
            ByteBuffer target = BufferUtil.newByteBuffer(pixels.capacity());
            dest = 0;
            ByteBuffer src = pixels;
            h = height;
            w = width;
            int row1 = (h - 1) * w;
            int row2 = 0;
            int row0;
            y = h;
            do {
                row0 = row1;
                row1 = row2;
                row2 += w;
                if (y == 1) {
                    row2 = 0;
                }
                x = w - 1;
                succx = 0;
                do {
                    predx = x;
                    x = succx++;
                    if (succx == w) {
                        succx = 0;
                    }
                    sx = (src.get(row0 + predx) + 2 * src.get(row1 + predx) + src.get(row2 + predx)) - (src.get(row0 + succx) + 2 * src.get(row1 + succx) + src.get(row2 + succx));
                    sy = (src.get(row0 + predx) + 2 * src.get(row0 + x) + src.get(row0 + succx)) - (src.get(row2 + predx) + 2 * src.get(row2 + x) + src.get(row2 + succx));
                    len = (int) (1044480.0f / Math.sqrt((float) (sx * sx + sy * sy + 65536)));
                    sx *= len;
                    sy *= len;
                    target.put(dest + red, (byte) ((sx + 1044480) >> 13));
                    target.put(dest + 1, (byte) ((sy + 1044480) >> 13));
                    target.put(dest + blue, (byte) ((len + 4080) >> 5));
                    dest += 3;
                } while (succx != 0);
            } while (--y != 0);
            pixels = target;
        }
    }
}
