package org.expasy.jpl.dev.libraryviewer.data;

import java.util.List;
import org.expasy.jpl.bio.annotation.JPLMSnPeakAnnotation;
import org.jfree.data.xy.XYDataItem;

/**
 * @author eahrne
 *
 */
public class PeakDataItem extends XYDataItem {

    /**
	 * 
	 */
    private static final long serialVersionUID = -236127636103804329L;

    private List<JPLMSnPeakAnnotation> annotation;

    public PeakDataItem(double mz, double inty, List<JPLMSnPeakAnnotation> annots) {
        super(mz, inty);
        annotation = annots;
    }

    @Override
    public String toString() {
        return super.getX() + " " + super.getYValue();
    }

    /**
	 * @return the annotation
	 */
    public List<JPLMSnPeakAnnotation> getAnnotation() {
        return annotation;
    }

    /**
	 * @param annotation the annotation to set
	 */
    public void setAnnotation(List<JPLMSnPeakAnnotation> annotation) {
        this.annotation = annotation;
    }
}
