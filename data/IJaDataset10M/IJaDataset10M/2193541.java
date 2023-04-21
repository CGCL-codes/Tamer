package org.expasy.jpl.tools.delib;

import java.util.List;
import org.expasy.jpl.core.ms.spectrum.PeakList;
import org.expasy.jpl.core.ms.spectrum.annot.FragmentAnnotation;
import org.expasy.jpl.core.ms.spectrum.filter.AbstractPeakListFilter;

/**
 * A filter that select only annotated peaks.
 * 
 * @author nikitin
 * 
 * @version 1.0.0
 * 
 */
public class AnnotatedNotPrecPeakListFilter extends AbstractPeakListFilter {

    @Override
    public boolean isPassingFilter(final PeakList pl, final int idx) {
        if (pl.hasAnnotationsAt(idx)) {
            final List<FragmentAnnotation> annots = pl.getAnnotationsAt(idx);
            for (final FragmentAnnotation annot : annots) {
                if (annot.getResidueNumber() == pl.getPrecursor().getPeptide().length()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
