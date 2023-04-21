package abbot.swt.utilities;

import java.util.Comparator;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.RGB;

/**
 * Compares <code>org.eclipse.swt.graphics.Image</code> objects on a per-pixel basis.
 * 
 * @author Gary Johnston
 * @author Kevin Dale
 */
public class ImageComparator implements Comparator<Image> {

    /**
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
    public int compare(Image image1, Image image2) {
        ImageData data1 = image1.getImageData();
        ImageData data2 = image2.getImageData();
        if (data1.width != data2.width || data1.height != data2.height) {
            int size1 = data1.width * data1.height;
            int size2 = data2.width * data2.height;
            if (size1 != size2) return size1 - size2;
            if (data1.height != data2.height) return data1.height - data2.height;
            return data1.width - data2.width;
        }
        double h1 = 0.0;
        double s1 = 0.0;
        double b1 = 0.0;
        double h2 = 0.0;
        double s2 = 0.0;
        double b2 = 0.0;
        for (int i = 0; i < data1.width; i++) {
            for (int j = 0; j < data1.height; j++) {
                RGB rgb1 = data1.palette.getRGB(data1.getPixel(i, j));
                RGB rgb2 = data2.palette.getRGB(data2.getPixel(i, j));
                if (!rgb1.equals(rgb2)) {
                    float[] hsb1 = rgb1.getHSB();
                    float[] hsb2 = rgb2.getHSB();
                    h1 += hsb1[0];
                    s1 += hsb1[1];
                    b1 += hsb1[2];
                    h2 += hsb2[0];
                    s2 += hsb2[1];
                    b2 += hsb2[2];
                } else {
                }
            }
        }
        if (b1 != b2) return (int) (b1 - b2);
        if (s1 != s2) return (int) (s1 - s2);
        return (int) (h1 - h2);
    }
}
