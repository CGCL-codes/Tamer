package jphotoshop.filter;

import jphotoshop.ui.util.MultiSlider;
import jphotoshop.ui.util.MultiSlider.MultiSliderEntry;

/**
 * @author: Liu Ke
 * @email:  soulnew@gmail.com
 */
public class SwirlFilter extends ContortFilter {

    float scalDegree = 0;

    @Override
    public int doColor(int[][] image, int row, int col) {
        offsetx = col - midx;
        offsety = row - midy;
        radian = Math.atan2(offsety, offsetx);
        radius = Math.sqrt(offsety * offsety + offsetx * offsetx);
        row = (int) (radius * Math.sin(radian + scalDegree * radius)) + midy;
        col = (int) (radius * Math.cos(radian + scalDegree * radius)) + midx;
        if (row >= Height) {
            row = Height - 1;
        } else if (row < 0) {
            row = 0;
        }
        if (col >= Width) {
            col = Width - 1;
        } else if (col < 0) {
            col = 0;
        }
        return tempData[row][col];
    }

    @Override
    public MultiSliderEntry getMultiSliderEntry() {
        return new MultiSlider.MultiSliderEntry(0, 20, 100);
    }

    @Override
    public void setDegree(int degree) {
        scalDegree = (float) degree / 1000.0f;
    }
}
