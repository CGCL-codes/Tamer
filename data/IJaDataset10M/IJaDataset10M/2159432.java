package com.ixora.rms.ui.dataviewboard.charts;

import org.jfree.chart.labels.StandardCategoryToolTipGenerator;
import org.jfree.data.category.CategoryDataset;
import com.ixora.common.ui.UIConfiguration;
import com.ixora.common.ui.UIUtils;

/**
 * CategoryToolTipGenerator
 */
public final class RMSCategoryToolTipGenerator extends StandardCategoryToolTipGenerator {

    private static final long serialVersionUID = -8132713650839746090L;

    /**
     * Constructor.
     */
    public RMSCategoryToolTipGenerator() {
        super();
    }

    /**
     * @see org.jfree.chart.labels.CategoryToolTipGenerator#generateToolTip(org.jfree.data.category.CategoryDataset, int, int)
     */
    public String generateToolTip(CategoryDataset dataset, int row, int column) {
        return UIUtils.getMultilineHtmlText(super.generateToolTip(dataset, row, column), UIConfiguration.getMaximumLineLengthForToolTipText());
    }
}
