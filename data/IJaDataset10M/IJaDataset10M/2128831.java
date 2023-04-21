package bookshelf.makefont;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.io.File;
import java.io.RandomAccessFile;
import bookshelf.font.BitmapFont;
import bookshelf.font.Font;

public class FsPft {

    class PftHelper {

        private PftFont pftFont;

        private BufferedImage image;

        PftHelper(PftFont pftFont) {
            this.pftFont = pftFont;
            image = new BufferedImage(pftFont.width(), pftFont.height(), BufferedImage.TYPE_BYTE_BINARY);
            byte[] buffer = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
            byte[] bitmap = pftFont.bitmap();
            System.arraycopy(pftFont.bitmap(), 0, buffer, 0, buffer.length);
        }

        private int getGlyphWidth(int i) {
            return pftFont.getRealWidth(i);
        }

        private byte[] getGlyphBytes(int i) throws Exception {
            int glyphWidth = pftFont.getRealWidth(i);
            if (glyphWidth == -1) {
                return null;
            }
            int glyphLoc = pftFont.getRealLocation(i);
            Raster glyphRaster = image.getData(new Rectangle(glyphLoc, 0, glyphWidth, pftFont.height()));
            return ((DataBufferByte) glyphRaster.getDataBuffer()).getData();
        }

        int getDefaultGlyphWidth() {
            return pftFont.getDefaultWidth();
        }

        private byte[] getDefaultGlyphBytes() throws Exception {
            int glyphWidth = pftFont.getDefaultWidth();
            int glyphLoc = pftFont.getDefaultLocation();
            Raster glyphRaster = image.getData(new Rectangle(glyphLoc, 0, glyphWidth, pftFont.height()));
            return ((DataBufferByte) glyphRaster.getDataBuffer()).getData();
        }
    }

    public Font loadPftFont(File file, String encoding) throws Exception {
        RandomAccessFile in = new RandomAccessFile(file, "r");
        PftFont pftFont = new PftFont(in);
        in.close();
        PftHelper helper = new PftHelper(pftFont);
        IndexColorModel cm = new IndexColorModel(1, 2, new byte[] { (byte) 255, 0 }, new byte[] { (byte) 255, 0 }, new byte[] { (byte) 255, 0 });
        BitmapFont font = new BitmapFont();
        font.setFirstChar((char) pftFont.firstChar());
        font.setLastChar((char) (pftFont.lastChar() + 1));
        font.setMaxWidth(pftFont.getMaxWidth());
        font.setHeight(pftFont.height());
        font.setEncoding(encoding);
        int glyphCount = pftFont.lastChar() - pftFont.firstChar() + 2;
        font.setGlyphTable(new BufferedImage[glyphCount]);
        for (int i = 0; i < glyphCount - 1; i++) {
            if (pftFont.getRealWidth(i) != -1) {
                BufferedImage glyph = new BufferedImage(pftFont.getRealWidth(i), font.getHeight(), BufferedImage.TYPE_BYTE_BINARY, cm);
                writeToImage(glyph, helper.getGlyphBytes(i));
                font.getGlyphTable()[i] = glyph;
            }
        }
        BufferedImage glyph = new BufferedImage(helper.getDefaultGlyphWidth(), font.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        writeToImage(glyph, helper.getDefaultGlyphBytes());
        font.getGlyphTable()[font.getGlyphTable().length - 1] = glyph;
        return font;
    }

    /**
     * Writes contents of byte array to a BufferedImage.
     * 
     * @param image
     *            <code>BufferedImage</code> of <code>TYPE_BYTE_BINARY</code>
     *            and appropriate size.
     * @param data
     *            bits to write to image
     * @return image with appropriate bits set
     */
    private BufferedImage writeToImage(BufferedImage image, byte[] data) {
        byte buffer[] = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(data, 0, buffer, 0, data.length);
        return image;
    }
}
