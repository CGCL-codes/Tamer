package shu.cms.lcd.calibrate.measured.algo;

import java.util.*;
import shu.cms.colorspace.depend.*;
import shu.cms.util.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 * �H��׬���¦���ͩP��RGB�I
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class LightnessAroundAlgorithm extends StepAroundAlgorithm {

    public LightnessAroundAlgorithm() {
        super();
    }

    public LightnessAroundAlgorithm(double maxCode) {
        super(maxCode);
    }

    /**
   *
   * @param centerRGB RGB
   * @param delta double[]
   * @param step double
   * @return RGB[]
   */
    public RGB[] getAroundRGB(RGB centerRGB, double[] delta, double step) {
        List<RGB> rgbList = new ArrayList<RGB>(2);
        rgbList.add(centerRGB);
        double d = delta[0];
        if (d > 0) {
            double minvalue = centerRGB.getValue(centerRGB.getMinChannel());
            if (minvalue - step >= 0) {
                RGB rgb = (RGB) centerRGB.clone();
                rgb.addValues(-step);
                rgbList.add(rgb);
            }
        } else {
            double maxvalue = centerRGB.getValue(centerRGB.getMaxChannel());
            if (maxvalue + step <= maxCode) {
                RGB rgb = (RGB) centerRGB.clone();
                rgb.addValues(step);
                rgbList.add(rgb);
            }
        }
        return RGBArray.toRGBArray(rgbList);
    }

    /**
   *
   * @param centerRGB RGB
   * @param step double
   * @return RGB[]
   * @deprecated
   */
    public RGB[] getAroundRGB(RGB centerRGB, double step) {
        List<RGB> rgbList = new ArrayList<RGB>(3);
        rgbList.add(centerRGB);
        double maxvalue = centerRGB.getValue(centerRGB.getMaxChannel());
        if (maxvalue + step <= maxCode) {
            RGB rgb = (RGB) centerRGB.clone();
            rgb.addValues(step);
            rgbList.add(rgb);
        }
        double minvalue = centerRGB.getValue(centerRGB.getMinChannel());
        if (minvalue - step >= 0) {
            RGB rgb = (RGB) centerRGB.clone();
            rgb.addValues(-step);
            rgbList.add(rgb);
        }
        return RGBArray.toRGBArray(rgbList);
    }
}
