package org.expasy.jpl.experimental.ms.lcmsms;

import java.util.ArrayList;
import org.expasy.jpl.experimental.ms.lcmsms.JPLRetentionTime.RTUnit;
import org.expasy.jpl.experimental.ms.peaklist.JPLChromatogram;
import org.expasy.jpl.experimental.ms.peaklist.JPLFragmentationSpectrum;
import org.expasy.jpl.experimental.ms.peaklist.JPLMS1LCSpectrum;
import org.expasy.jpl.experimental.ms.peaklist.JPLRunLCPeaklist;

/**
 * 
 * a chromatogram where each peak point towards a spectrum of instanciation
 * class (typically {@link JPLFragmentationSpectrum} or {@link JPLMS1LCSpectrum}
 * 
 * @author Alexandre Masselot
 * 
 * TODO see with Markus/PAB if one chromato peak can lead to a series of
 * spectrum or only one is enough (latest case for the moment)
 */
public class JPLChromatogramWithRef<PeakListClass extends JPLRunLCPeaklist> extends JPLChromatogram {

    /**
	 * 
	 */
    private static final long serialVersionUID = 5191944386844224249L;

    private ArrayList<PeakListClass> references;

    /**
	 * @param retentionTime
	 * @param intensities
	 */
    public JPLChromatogramWithRef(double[] retentionTime, double[] intensities, RTUnit unit) {
        super(retentionTime, intensities, unit);
        references = new ArrayList<PeakListClass>();
        for (int i = 0; i < retentionTime.length; i++) {
            references.add(null);
        }
    }

    public PeakListClass getRef(final int i) {
        return references.get(i);
    }

    public void setRef(final int i, PeakListClass spectrum) {
        references.set(i, spectrum);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("#");
        sb.append(getUnit());
        sb.append("\n");
        for (int i = 0; i < getNbPeak(); i++) {
            sb.append(getRetentionTimeAt(i));
            sb.append("\t");
            sb.append(getIntensityAt(i));
            if (getRef(i) != null) {
                sb.append("\t");
                sb.append(getRef(i).getTitle());
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
