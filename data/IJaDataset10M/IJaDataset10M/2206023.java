package com.gwtsandbox.colorpicker.client.util;

public class ColorUtils {

    /**
     * Method returns three integers representing the RGB values of a HSV input
     * Values in input must be between 0 and 1
     * Values in output are between 0 and 255.
     * 
     * See http://www.easyrgb.com/math.php?MATH=M21#text21 for details of algorithm used.
     * 
     * @param hue
     * @param saturation
     * @param value
     * @return
     */
    public static int[] HSVtoRGB(double hue, double saturation, double value) {
        int R, G, B;
        if (saturation == 0) {
            R = new Double(value * 255).intValue();
            G = new Double(value * 255).intValue();
            B = new Double(value * 255).intValue();
        } else {
            double var_h = hue * 6;
            if (var_h == 6) var_h = 0;
            int var_i = new Double(Math.floor(var_h)).intValue();
            double var_1 = value * (1 - saturation);
            double var_2 = value * (1 - saturation * (var_h - var_i));
            double var_3 = value * (1 - saturation * (1 - (var_h - var_i)));
            double var_r;
            double var_g;
            double var_b;
            if (var_i == 0) {
                var_r = value;
                var_g = var_3;
                var_b = var_1;
            } else if (var_i == 1) {
                var_r = var_2;
                var_g = value;
                var_b = var_1;
            } else if (var_i == 2) {
                var_r = var_1;
                var_g = value;
                var_b = var_3;
            } else if (var_i == 3) {
                var_r = var_1;
                var_g = var_2;
                var_b = value;
            } else if (var_i == 4) {
                var_r = var_3;
                var_g = var_1;
                var_b = value;
            } else {
                var_r = value;
                var_g = var_1;
                var_b = var_2;
            }
            R = new Double(var_r * 255).intValue();
            G = new Double(var_g * 255).intValue();
            B = new Double(var_b * 255).intValue();
        }
        int[] returnArray = new int[3];
        returnArray[0] = R;
        returnArray[1] = G;
        returnArray[2] = B;
        return returnArray;
    }

    /**
     * Method returns three doubles representing the HSV values of a RGB input
     * Values in input must be between 0 and 255
     * Values in output are between 0 and 1.
     * 
     * See http://www.easyrgb.com/math.php?MATH=M21#text21 for details of algorithm used.
     * 
     * @param red
     * @param green
     * @param blue
     * @return
     */
    public static double[] RGBtoHSV(int red, int green, int blue) {
        double H = 0, S = 0, V = 0;
        double R = red / 255.0;
        double G = green / 255.0;
        double B = blue / 255.0;
        double var_Min = Math.min(Math.min(R, G), B);
        double var_Max = Math.max(Math.max(R, G), B);
        double del_Max = var_Max - var_Min;
        V = var_Max;
        if (del_Max == 0) {
            H = 0;
            S = 0;
        } else {
            S = del_Max / var_Max;
            double del_R = (((var_Max - R) / 6) + (del_Max / 2)) / del_Max;
            double del_G = (((var_Max - G) / 6) + (del_Max / 2)) / del_Max;
            double del_B = (((var_Max - B) / 6) + (del_Max / 2)) / del_Max;
            if (R == var_Max) {
                H = del_B - del_G;
            } else if (G == var_Max) {
                H = (1 / 3) + del_R - del_B;
            } else if (B == var_Max) {
                H = (2 / 3) + del_G - del_R;
            }
            if (H < 0) {
                H += 1;
            }
            if (H > 1) {
                H -= 1;
            }
        }
        return new double[] { H, S, V };
    }
}
