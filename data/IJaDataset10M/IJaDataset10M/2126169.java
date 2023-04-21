package net.sf.maltcms.evaluation.spi.caap;

import java.io.File;
import java.io.FileFilter;
import java.util.List;
import java.util.TreeMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import ucar.ma2.ArrayDouble;
import ucar.ma2.ArrayInt;
import ucar.nc2.Dimension;
import cross.Factory;
import cross.datastructures.fragments.FileFragment;
import cross.datastructures.fragments.IFileFragment;
import cross.datastructures.fragments.VariableFragment;
import cross.tools.StringTools;
import java.io.InputStream;
import java.util.*;
import maltcms.io.xml.bindings.openms.featurexml.FeatureMap;
import maltcms.io.xml.bindings.openms.featurexml.FeatureType;
import maltcms.io.xml.bindings.openms.featurexml.FeatureType.Position;

/**
 *
 * @author nilshoffmann
 */
public class PeakFactory {

    public static FeatureMap.FeatureList getFeatureList(File f) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("maltcms.io.xml.bindings.openms.featurexml");
            final Unmarshaller u = jc.createUnmarshaller();
            final FeatureMap mzd = (FeatureMap) u.unmarshal(f);
            return mzd.getFeatureList();
        } catch (final JAXBException e) {
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

    public static FeatureMap.FeatureList getFeatureList(InputStream s) {
        JAXBContext jc;
        try {
            jc = JAXBContext.newInstance("maltcms.io.xml.bindings.openms.featurexml");
            final Unmarshaller u = jc.createUnmarshaller();
            final FeatureMap mzd = (FeatureMap) u.unmarshal(s);
            return mzd.getFeatureList();
        } catch (final JAXBException e) {
            throw new RuntimeException(e.fillInStackTrace());
        }
    }

    public static double getRt(FeatureType ft) {
        List<Position> l = ft.getPosition();
        if (l.get(0).getDim().equals("0")) {
            return Double.parseDouble(((String) l.get(0).getValue()));
        } else if (l.get(1).getDim().equals("0")) {
            return Double.parseDouble(((String) l.get(1).getValue()));
        }
        return Double.NaN;
    }

    public static double getMz(FeatureType ft) {
        List<Position> l = ft.getPosition();
        if (l.get(0).getDim().equals("1")) {
            return Double.parseDouble(((String) l.get(0).getValue()));
        } else if (l.get(1).getDim().equals("1")) {
            return Double.parseDouble(((String) l.get(1).getValue()));
        }
        return Double.NaN;
    }

    public static IFileFragment joinFeatures(File outputDir, String filename, FeatureMap.FeatureList fl) {
        int features = 0;
        List<FeatureType> featureList = fl.getFeature();
        features = featureList.size();
        Collections.sort(featureList, new Comparator<FeatureType>() {

            @Override
            public int compare(FeatureType t, FeatureType t1) {
                double deltaRt = PeakFactory.getRt(t) - PeakFactory.getRt(t1);
                if (deltaRt < 0) {
                    return -1;
                } else if (deltaRt > 0) {
                    return 1;
                } else {
                    double deltaMz = PeakFactory.getMz(t) - PeakFactory.getMz(t1);
                    if (deltaMz < 0) {
                        return -1;
                    } else if (deltaMz > 0) {
                        return 1;
                    } else {
                        double deltaInten = t.getIntensity() - t1.getIntensity();
                        if (deltaInten < 0) {
                            return -1;
                        } else if (deltaInten > 0) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                }
            }
        });
        ArrayDouble.D1 masses = new ArrayDouble.D1(features);
        ArrayDouble.D1 intens = new ArrayDouble.D1(features);
        ArrayDouble.D1 sat = new ArrayDouble.D1(features);
        ArrayDouble.D1 tic = new ArrayDouble.D1(features);
        ArrayDouble.D1 mr_min = new ArrayDouble.D1(features);
        ArrayDouble.D1 mr_max = new ArrayDouble.D1(features);
        ArrayInt.D1 sidx = new ArrayInt.D1(features);
        int scanIdx = 0;
        for (FeatureType ft : featureList) {
            sidx.set(scanIdx, scanIdx);
            double mz = PeakFactory.getMz(ft);
            masses.set(scanIdx, mz);
            intens.set(scanIdx, ft.getIntensity());
            mr_min.set(scanIdx, mz);
            mr_max.set(scanIdx, mz);
            sat.set(scanIdx, PeakFactory.getRt(ft));
            tic.set(scanIdx, ft.getIntensity());
            scanIdx++;
        }
        String fragName = StringTools.removeFileExt(filename) + ".cdf";
        IFileFragment chromMS = new FileFragment(outputDir, fragName);
        VariableFragment mv = new VariableFragment(chromMS, "mass_values");
        Dimension pointNumber = new Dimension("point_number", features, true);
        mv.setDimensions(new Dimension[] { pointNumber });
        mv.setArray(masses);
        VariableFragment iv = new VariableFragment(chromMS, "intensity_values");
        iv.setDimensions(new Dimension[] { pointNumber });
        iv.setArray(intens);
        Dimension scanNumber = new Dimension("scan_number", features, true);
        VariableFragment satv = new VariableFragment(chromMS, "scan_acquisition_time");
        satv.setDimensions(new Dimension[] { scanNumber });
        satv.setArray(sat);
        VariableFragment siv = new VariableFragment(chromMS, "scan_index");
        siv.setDimensions(new Dimension[] { scanNumber });
        siv.setArray(sidx);
        VariableFragment ticv = new VariableFragment(chromMS, "total_intensity");
        ticv.setDimensions(new Dimension[] { scanNumber });
        ticv.setArray(tic);
        VariableFragment minMZv = new VariableFragment(chromMS, "mass_range_min");
        minMZv.setDimensions(new Dimension[] { scanNumber });
        minMZv.setArray(mr_min);
        VariableFragment maxMZv = new VariableFragment(chromMS, "mass_range_max");
        maxMZv.setDimensions(new Dimension[] { scanNumber });
        maxMZv.setArray(mr_max);
        chromMS.save();
        return chromMS;
    }

    public static void main(String[] args) {
        Factory fac = Factory.getInstance();
        fac.getConfiguration().setProperty("output.overwrite", true);
        String output = args[0];
        File inputDir = new File(args[1]);
        final String suff = args[2];
        File[] files = inputDir.listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
                return pathname.getName().endsWith(suff);
            }
        });
        for (File f : files) {
            FeatureMap.FeatureList fl = getFeatureList(f);
            IFileFragment frag = joinFeatures(new File(output), f.getName(), fl);
        }
        System.out.println("Processed " + files.length + " files!");
        System.exit(0);
    }
}
