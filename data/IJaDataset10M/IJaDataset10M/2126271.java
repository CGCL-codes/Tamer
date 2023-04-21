package jphotoshop.math;

import jphotoshop.util.ArrayUtil;

/**
 * @author liuke
 * @email:  soulnew@gmail.com
 */
public class FeatherSide {

    static short[] list;

    static int radius;

    static int scal;

    public static short[][] getOP(short[][] image, int r) {
        if (r <= 1) {
            return image;
        }
        scal = r / 2;
        radius = r;
        iniByte();
        short[][] tofill = ArrayUtil.copy2D2D(image);
        int row = image.length;
        int col = image[0].length;
        for (int i = scal; i < row - scal; i++) {
            for (int j = scal; j < col - scal; j++) {
                tofill[i][j] = pointProcess(image, r, i, j);
            }
        }
        return tofill;
    }

    public static short[] iniByte() {
        list = new short[radius * radius + 1];
        for (int i = 1; i < list.length - 1; i++) {
            list[i] = (short) (((float) i / list.length) * 0xff);
        }
        list[list.length - 1] = 0xff;
        return list;
    }

    public static short pointProcess(short[][] image, int r, int pointRow, int pointCol) {
        int col = pointCol - scal;
        int row = pointRow - scal;
        short alpha = 0;
        int hit = 0;
        for (int i = 0; i < r; i++) {
            for (int ii = 0; ii < r; ii++) {
                if (image[row + i][col + ii] == 0xff) {
                    hit++;
                }
            }
        }
        return list[hit];
    }
}
