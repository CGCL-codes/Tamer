package shu.cms.measure.util;

import java.io.*;
import java.util.*;
import java.util.List;
import java.awt.*;
import javax.swing.*;
import shu.cms.*;
import shu.cms.colorformat.logo.*;
import shu.cms.colorspace.depend.*;
import shu.cms.colorspace.independ.*;
import shu.cms.measure.*;
import shu.cms.measure.meter.*;

/**
 * <p>Title: Colour Management System</p>
 *
 * <p>Description: a Colour Management System by Java</p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: skygroup</p>
 *
 * @author skyforce
 * @version 1.0
 */
public class MonitorStabilityMeasureTool {

    protected MeterMeasurement mt;

    protected Meter meter;

    public MonitorStabilityMeasureTool(Meter meter) {
        this.meter = meter;
        mt = new MeterMeasurement(meter, true);
    }

    /**
   *
   * <p>Title: Colour Management System</p>
   *
   * <p>Description: a Colour Management System by Java</p>
   *
   * <p>Copyright: Copyright (c) 2008</p>
   *
   * <p>Company: skygroup</p>
   *
   * @author skyforce
   * @version 1.0
   * @deprecated
   */
    protected static final class Measure {

        protected Measure(long time, CIEXYZ XYZ) {
            this.time = time;
            this.XYZ = XYZ;
        }

        public long time;

        public CIEXYZ XYZ;
    }

    protected static final RGB gray = new RGB(RGB.ColorSpace.unknowRGB, Color.gray);

    /**
   *
   * <p>Title: Colour Management System</p>
   *
   * <p>Description: a Colour Management System by Java</p>
   *
   * <p>Copyright: Copyright (c) 2008</p>
   *
   * <p>Company: skygroup</p>
   *
   * @author skyforce
   * @version 1.0
   * @deprecated
   */
    public static final class Result {

        protected Result(List<Measure> measureList, List<Patch> patchList) {
            this.measureList = measureList;
            this.patchList = patchList;
        }

        List<Measure> measureList;

        List<Patch> patchList;
    }

    public List<Patch> measure(long periodInMinute) {
        JOptionPane.showMessageDialog(mt.getMeasureWindow(), "�бN" + meter.getType().name() + "��m�b�ù��W,�ë��U <�T�w> �i��q��.", "í�w�׶q��T�{", JOptionPane.INFORMATION_MESSAGE);
        long period = periodInMinute * 60 * 1000;
        long start = System.currentTimeMillis();
        List<Patch> patchList = new ArrayList<Patch>();
        long time = 0;
        while ((time = (System.currentTimeMillis() - start)) <= period) {
            Patch p = mt.measure(gray, String.valueOf(time));
            patchList.add(p);
            mt.setTitle("�Ѿl: " + String.valueOf((period - time) / 1000) + "��");
        }
        mt.setMeasureWindowsVisible(false);
        return patchList;
    }

    public static void main(String[] args) {
        EyeOneDisplay2 i1 = new EyeOneDisplay2(Meter.ScreenType.LCD);
        MonitorStabilityMeasureTool tool = new MonitorStabilityMeasureTool(i1);
        List<Patch> patchList = tool.measure(120);
        try {
            LogoFile file = new LogoFile("2407.txt", true);
            i1.setLogoFileHeader(file);
            i1.setLogoFileData(file, patchList);
            file.save();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
