package org.expasy.jpl.io.ms;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections15.Transformer;
import org.expasy.jpl.commons.base.builder.BuilderException;
import org.expasy.jpl.commons.base.builder.InstanceBuilder;
import org.expasy.jpl.commons.base.io.DecimalFormatFactory;
import org.expasy.jpl.commons.base.math.DoubleComparator;
import org.expasy.jpl.commons.base.record.DataTableFactory;
import org.expasy.jpl.core.ms.lc.RetentionTime;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotation;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotations;
import org.expasy.jpl.core.ms.spectrum.peak.Peak;

/**
 * Spectrum scanned from a mass spectrometer experiment.
 * 
 * @author nikitin
 * 
 * @version 1.0
 * 
 */
public final class MassSpectrumImpl implements MassSpectrum {

    private static final long serialVersionUID = -786466656839863026L;

    public static final int NA_SCAN_NUMBER = -1;

    public static final Transformer<MassSpectrum, PeakList> TO_PEAKLIST;

    public static final Transformer<List<MassSpectrum>, List<PeakList>> TO_PEAKLISTS;

    private static NumberFormat DEFAULT_SCAN_FORMAT = DecimalFormatFactory.valueOf(5, 0);

    public static final ScanNumComparator SCAN_NUM_COMPARATOR = new ScanNumComparator();

    public static final PrecMzComparator PREC_MZ_COMPARATOR = new PrecMzComparator();

    private String title;

    private String key;

    private List<Integer> scanNums;

    private int parentScanNum;

    private String comment;

    private PeakList peakList;

    private List<RetentionTime> rts;

    private double basePeakMz;

    private double basePeakIntensity;

    /** the activation method for fragmentation spectrum */
    protected String fragmentationMethod;

    /** true if the spectrum is a decoy */
    private boolean isDecoy;

    private DataTableFactory annotFactory;

    static {
        TO_PEAKLIST = new Transformer<MassSpectrum, PeakList>() {

            public final PeakList transform(MassSpectrum scan) {
                return scan.getPeakList();
            }
        };
        TO_PEAKLISTS = new Transformer<List<MassSpectrum>, List<PeakList>>() {

            public List<PeakList> transform(List<MassSpectrum> scans) {
                List<PeakList> list = new ArrayList<PeakList>();
                for (MassSpectrum scan : scans) {
                    if (scan != null) {
                        list.add(scan.getPeakList());
                    }
                }
                return list;
            }
        };
    }

    /**
	 * Compare {@code MassSpectrumImpl} by scan number values.
	 */
    public static class ScanNumComparator implements java.util.Comparator<MassSpectrum> {

        public int compare(MassSpectrum scan1, MassSpectrum scan2) {
            if (scan1.getScanNum() < scan2.getScanNum()) {
                return -1;
            } else if (scan1.getScanNum() > scan2.getScanNum()) {
                return 1;
            }
            return 0;
        }
    }

    /**
	 * Compare {@code MassSpectrumImpl} by precursor Mz values.
	 */
    public static class PrecMzComparator implements java.util.Comparator<MassSpectrum> {

        public static final PrecMzComparator INSTANCE = new PrecMzComparator();

        private DoubleComparator mzComparator;

        private PrecMzComparator() {
            mzComparator = DoubleComparator.getDefaultInstance();
        }

        private PrecMzComparator(double epsilon) {
            mzComparator = DoubleComparator.newInstance(epsilon);
        }

        public static PrecMzComparator getDefaultInstance() {
            return INSTANCE;
        }

        public static PrecMzComparator newInstance(double epsilon) {
            return new PrecMzComparator(epsilon);
        }

        public int compare(MassSpectrum scan1, MassSpectrum scan2) {
            PeakList pl1 = scan1.getPeakList();
            PeakList pl2 = scan2.getPeakList();
            if (pl1 == null || pl1.size() == 0) {
                throw new IllegalArgumentException("scan " + scan1.getScanNum() + " has no peaks");
            }
            if (pl2 == null || pl2.size() == 0) {
                throw new IllegalArgumentException("scan " + scan2.getScanNum() + " has no peaks");
            }
            Peak prec1 = pl1.getPrecursor();
            Peak prec2 = pl2.getPrecursor();
            if (prec1 == null) {
                throw new IllegalArgumentException("scan " + scan1.getScanNum() + " has no precursor");
            }
            if (prec2 == null) {
                throw new IllegalArgumentException("scan " + scan2.getScanNum() + " has no precursor");
            }
            return mzComparator.compare(prec1.getMz(), prec2.getMz());
        }
    }

    public static class Builder implements InstanceBuilder<MassSpectrumImpl> {

        /** mandatory */
        private List<Integer> scanNums;

        private PeakList peakList;

        /** default values */
        private String title = null;

        private int parentScanNum = NA_SCAN_NUMBER;

        private double basePeakMz;

        private double basePeakIntensity;

        private List<RetentionTime> rts;

        private String fragMethod;

        private String comment;

        private boolean isDecoy;

        private DataTableFactory annotFactory;

        private Exception exception;

        private int peakCount;

        public Builder(int scanNum, int peakCount) {
            if (scanNum < 0) {
                exception = new IllegalArgumentException(scanNum + ", bad scan number (>=0)");
            }
            scanNums = new ArrayList<Integer>();
            rts = new ArrayList<RetentionTime>();
            scanNums.add(scanNum);
            this.peakCount = peakCount;
        }

        public Builder scanNum(int scanNum) {
            scanNums.add(scanNum);
            return this;
        }

        public int getScanNum() {
            return scanNums.get(0);
        }

        public Builder peakList(PeakList peakList) {
            this.peakList = peakList;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder parent(int parentScanNum) {
            this.parentScanNum = parentScanNum;
            return this;
        }

        public Builder fragMethod(String fragMethod) {
            this.fragMethod = fragMethod;
            return this;
        }

        public Builder rt(RetentionTime rt) {
            rts.add(rt);
            return this;
        }

        public Builder basePeakIntensity(double bpi) {
            if (bpi < 0) {
                exception = new IllegalArgumentException(bpi + ", base peak intensity has to be positive");
            }
            this.basePeakIntensity = bpi;
            return this;
        }

        public Builder basePeakMz(double bpmz) {
            if (bpmz < 0) {
                exception = new IllegalArgumentException(bpmz + ", base peak mz has to be positive");
            }
            this.basePeakMz = bpmz;
            return this;
        }

        public Builder decoy() {
            this.isDecoy = true;
            return this;
        }

        public Builder annotFactory(DataTableFactory annotFactory) {
            this.annotFactory = annotFactory;
            return this;
        }

        public MassSpectrumImpl build() throws BuilderException {
            if (exception != null) {
                throw new BuilderException("cannot build MassSpectrumImpl instance", exception);
            } else if (peakCount > 0 && (peakList == null || peakList.size() == 0)) {
                exception = new IllegalArgumentException("Waiting " + peakCount + " peaks, undefined peak list");
            }
            return new MassSpectrumImpl(this);
        }
    }

    private MassSpectrumImpl(Builder builder) {
        title = builder.title;
        scanNums = builder.scanNums;
        key = convertScanNumber(getScanNum());
        comment = builder.comment;
        parentScanNum = builder.parentScanNum;
        rts = builder.rts;
        basePeakMz = builder.basePeakMz;
        basePeakIntensity = builder.basePeakIntensity;
        peakList = builder.peakList;
        annotFactory = builder.annotFactory;
        fragmentationMethod = builder.fragMethod;
        isDecoy = builder.isDecoy;
    }

    public MassSpectrumImpl clone() {
        MassSpectrumImpl clone = null;
        try {
            clone = (MassSpectrumImpl) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException("cannot clone JPLMassSpectrumScan", e);
        }
        clone.peakList = (PeakList) peakList.clone();
        clone.title = title;
        clone.comment = comment;
        clone.scanNums = scanNums;
        clone.parentScanNum = parentScanNum;
        clone.annotFactory = annotFactory;
        clone.basePeakIntensity = basePeakIntensity;
        clone.basePeakMz = basePeakMz;
        clone.rts = rts;
        clone.fragmentationMethod = fragmentationMethod;
        return clone;
    }

    public int hashCode() {
        return peakList.hashCode();
    }

    public boolean equals(Object o) {
        if (o instanceof MassSpectrumImpl) {
            MassSpectrumImpl scan = (MassSpectrumImpl) o;
            if (scan.scanNums.equals(scanNums) && scan.parentScanNum == parentScanNum && scan.peakList.equals(peakList)) {
                return true;
            }
        }
        return false;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return key;
    }

    public static final String convertScanNumber(int scanNumber) {
        return DEFAULT_SCAN_FORMAT.format(scanNumber);
    }

    /**
	 * Sets the format of the scan number string used eg in the mgf title (00126
	 * in B0-1000_c.00126.00126.2). The description is the format is given in
	 * the documentation of the NumberFormat class. If format = "", the scan
	 * number is printed as it is without 0 padding (B0-1000_c.126.126.2). If
	 * format = "00000", the scan number is printed as 00126.
	 * 
	 * @param format
	 */
    public static void setScanNumberFormat(NumberFormat format) {
        DEFAULT_SCAN_FORMAT = format;
    }

    public static NumberFormat getScanNumberFormat() {
        return DEFAULT_SCAN_FORMAT;
    }

    public final int getScanNum() {
        return scanNums.get(0);
    }

    public final List<Integer> getScanNumList() {
        return scanNums;
    }

    public void setScanNum(int... sns) {
        scanNums.clear();
        for (int scan : sns) {
            scanNums.add(scan);
        }
    }

    public final int getParentScanNum() {
        return parentScanNum;
    }

    public final int getMSLevel() {
        return peakList.getMSLevel();
    }

    public final RetentionTime getRetentionTime() {
        return rts.get(0);
    }

    public final List<RetentionTime> getRetentionTimeList() {
        return rts;
    }

    public void setRetentionTime(RetentionTime... times) {
        rts.clear();
        for (RetentionTime rt : times) {
            rts.add(rt);
        }
    }

    public final double getBasePeakMz() {
        return basePeakMz;
    }

    public final double getBasePeakIntensity() {
        return basePeakIntensity;
    }

    public final Peak getPrecursor() {
        return peakList.getPrecursor();
    }

    public final PeakList getPeakList() {
        return peakList;
    }

    public void setPeakList(PeakList peakList) {
        this.peakList = peakList;
    }

    public final FragmentAnnotations getAnnotations() {
        return peakList.getAnnotations();
    }

    public final List<FragmentAnnotation> getAnnotationsAt(int index) {
        return peakList.getAnnotationsAt(index);
    }

    public final boolean hasAnnotationsAt(int index) {
        return peakList.hasAnnotationsAt(index);
    }

    public final boolean hasAnnotationsAtPeak(int index) {
        return peakList.hasAnnotationsAt(index);
    }

    public final DataTableFactory getAnnotFactory() {
        return annotFactory;
    }

    public final String getFragmentationMethod() {
        return fragmentationMethod;
    }

    public final double[] getIntensities() {
        return peakList.getIntensities();
    }

    public final double getIntensityAt(int i) {
        return peakList.getIntensityAt(i);
    }

    public final double getMzAt(int i) {
        return peakList.getMzAt(i);
    }

    public final double[] getMzs() {
        return peakList.getMzs();
    }

    public final boolean hasAnnotations() {
        return peakList.hasAnnotations();
    }

    public final boolean hasIntensities() {
        return peakList.hasIntensities();
    }

    public final int size() {
        return peakList.size();
    }

    public final String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public final boolean isDecoy() {
        return isDecoy;
    }

    public void setDecoy(boolean isDecoy) {
        this.isDecoy = isDecoy;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (title != null) {
            sb.append("title: " + title + "\n");
        }
        sb.append("spectrum id: " + getScanNum());
        if (scanNums.size() > 1) {
            sb.append(" (scan range=" + scanNums + ")");
        }
        sb.append("\n");
        if (rts.size() > 0) {
            sb.append("retention time: " + getRetentionTime());
            if (rts.size() > 1) {
                sb.append(" (rt range=" + rts + ")");
            }
            sb.append("\n");
        }
        if (parentScanNum != -1) {
            sb.append("parent scan num: " + parentScanNum + "\n");
        }
        if (peakList == null) {
            sb.append("[]");
        } else {
            sb.append(peakList);
        }
        return sb.toString();
    }
}
