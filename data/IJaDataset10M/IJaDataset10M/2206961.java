package gnu.javax.imageio.bmp;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

public class DecodeRGB24 extends BMPDecoder {

    public DecodeRGB24(BMPFileHeader fh, BMPInfoHeader ih) {
        super(fh, ih);
    }

    public BufferedImage decode(ImageInputStream in) throws IOException, BMPException {
        skipToImage(in);
        Dimension d = infoHeader.getSize();
        int h = (int) d.getHeight();
        int w = (int) d.getWidth();
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int scansize = ((w * 3 & 3) != 0) ? w * 3 + 4 - (w * 3 & 3) : w * 3;
        int[] data = new int[w * h];
        for (int y = h - 1; y >= 0; y--) {
            byte[] scanline = new byte[scansize];
            if (in.read(scanline) != scansize) throw new IOException("Couldn't read image data.");
            for (int x = 0; x < w; x++) data[x + y * w] = scanline[x * 3] + (scanline[x * 3 + 1] << 8) + (scanline[x * 3 + 2] << 16);
        }
        image.setRGB(0, 0, w, h, data, 0, w);
        return image;
    }
}
