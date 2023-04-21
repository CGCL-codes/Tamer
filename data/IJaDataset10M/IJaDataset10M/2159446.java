package nl.ikarus.nxt.priv.imageio.icoreader.obj;

import java.io.*;
import java.awt.image.*;

public abstract class RgbBitmap extends Bitmap {

    protected int amountOfBytesToRead = 3;

    protected RgbBitmap(IconEntry pEntry, int pAmountOfBytesToRead) throws IOException {
        this(pEntry);
        this.amountOfBytesToRead = pAmountOfBytesToRead;
    }

    protected RgbBitmap(IconEntry pEntry) throws IOException {
        super(pEntry);
    }

    /**
   * createImage
   *
   * @return BufferedImage
   */
    protected BufferedImage createImage() throws IOException {
        int w = entry.getHeight();
        int h = entry.getWidth();
        int[] pixeldata = new int[h * w];
        for (int rijNr = 0; rijNr < w; rijNr++) {
            byte[] rij = reader.readBytes(h * amountOfBytesToRead);
            int rByte = 0;
            int oPos = (w - rijNr - 1) * h;
            for (int colNr = 0; colNr < h; colNr++) {
                int pos = oPos++;
                pixeldata[pos] = (rij[rByte++] & 0xFF);
                if (amountOfBytesToRead > 1) pixeldata[pos] += ((rij[rByte++] & 0xFF) << 8); else pixeldata[pos] += (0 << 8);
                if (amountOfBytesToRead > 2) pixeldata[pos] += ((rij[rByte++] & 0xFF) << 16); else pixeldata[pos] += (0 << 16);
                if (amountOfBytesToRead > 3) pixeldata[pos] += ((rij[rByte++] & 0xFF) << 24); else pixeldata[pos] += ((255) << 24);
            }
        }
        BufferedImage bIm;
        bIm = new BufferedImage(entry.getWidth(), entry.getHeight(), BufferedImage.TYPE_INT_ARGB);
        bIm.setRGB(0, 0, h, w, pixeldata, 0, h);
        return bIm;
    }
}
