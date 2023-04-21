package org.patchca.filter.predefined;

import java.awt.image.BufferedImageOp;
import java.util.ArrayList;
import java.util.List;
import org.patchca.filter.AbstractFilterFactory;
import org.patchca.filter.library.DoubleRippleImageOp;

public class DoubleRippleFilterFactory extends AbstractFilterFactory {

    protected List<BufferedImageOp> filters;

    protected DoubleRippleImageOp ripple;

    public DoubleRippleFilterFactory() {
        ripple = new DoubleRippleImageOp();
    }

    @Override
    public List<BufferedImageOp> getFilters() {
        if (filters == null) {
            filters = new ArrayList<BufferedImageOp>();
            filters.add(ripple);
        }
        return filters;
    }
}
