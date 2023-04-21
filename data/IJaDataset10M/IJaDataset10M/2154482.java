package auo.cms.cm;

import shu.cms.lcd.LCDTarget;
import shu.cms.measure.meter.*;
import shu.cms.measure.MeterMeasurement;
import java.util.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.plot.Plot2D;
import shu.cms.*;
import java.awt.Color;
import java.io.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2009</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class GamutChecker {

    public static void main(String[] args) {
        Meter ca210 = new CA210();
        MeterMeasurement mm = new MeterMeasurement(ca210, false);
        try {
            for (; ; ) {
                System.in.read();
                List<RGB> rgbList = LCDTarget.Instance.getRGBList(LCDTarget.Number.WHQL);
                rgbList = rgbList.subList(7, 13);
                LCDTarget target = LCDTarget.Measured.measure(mm, rgbList);
                Plot2D plot = Plot2D.getInstance();
                List<Patch> patchList = target.getPatchList();
                CIEXYZ sRGBwhiteXYZ = RGB.ColorSpace.sRGB.referenceWhite.getNormalizeXYZ();
                CIExyY sRGBwhitexyY = new CIExyY(sRGBwhiteXYZ);
                plot.addScatterPlot("White", sRGBwhitexyY.x, sRGBwhitexyY.y);
                for (Patch p : patchList) {
                    RGB rgb = p.getRGB();
                    CIEXYZ XYZ = p.getXYZ();
                    CIExyY xyY = new CIExyY(XYZ);
                    plot.addCacheScatterPlot("measure", Color.red, xyY.x, xyY.y);
                    CIEXYZ sRGBXYZ = rgb.toXYZ(RGB.ColorSpace.sRGB);
                    CIExyY sRGBxyY = new CIExyY(sRGBXYZ);
                    plot.addCacheScatterPlot("sRGB", Color.green, sRGBxyY.x, sRGBxyY.y);
                }
                plot.addLegend();
                plot.setVisible();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
