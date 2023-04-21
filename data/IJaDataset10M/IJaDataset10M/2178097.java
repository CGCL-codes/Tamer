package org.expasy.jpl.experimental.ms.peaklist;

import java.util.List;
import org.expasy.jpl.experimental.ms.lcmsms.JPLRetentionTime;

/**
 * @author alex
 *
 */
public class JPLMS1LCSpectrum extends JPLRunLCPeaklist {

    /**
	 * 
	 */
    private static final long serialVersionUID = -5872100730080779922L;

    JPLRetentionTime retentionTime;

    public JPLMS1LCSpectrum() {
    }

    /**
	 * @param masses
	 * @param intensities
	 */
    public JPLMS1LCSpectrum(double[] mzs, double[] intensities) {
        super(mzs, intensities);
        retentionTime = null;
    }

    public JPLMS1LCSpectrum clone() {
        JPLMS1LCSpectrum pl = (JPLMS1LCSpectrum) super.clone();
        pl.retentionTime = this.retentionTime;
        return pl;
    }

    public JPLMS1LCSpectrum subList(List<Integer> srcIndices, boolean newInstance) {
        JPLMS1LCSpectrum subList = (JPLMS1LCSpectrum) super.subList(srcIndices, newInstance);
        subList.retentionTime = this.getRetentionTime();
        return subList;
    }

    public JPLRetentionTime getRetentionTime() {
        return retentionTime;
    }

    public int getMsLevel() {
        return 1;
    }

    public void setRetentionTime(JPLRetentionTime retentionTime) {
        this.retentionTime = retentionTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
