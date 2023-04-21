package com.volantis.mcs.protocols.renderer.layouts.spatial;

import com.volantis.mcs.layouts.Format;
import com.volantis.mcs.layouts.NDimensionalIndex;
import com.volantis.mcs.layouts.SpatialFormatIterator;
import com.volantis.mcs.layouts.spatial.CoordinateConverter;
import com.volantis.mcs.localization.LocalizationFactory;
import com.volantis.mcs.protocols.layouts.FormatInstance;
import com.volantis.mcs.protocols.renderer.layouts.FormatRendererContext;
import com.volantis.synergetics.localization.ExceptionLocalizer;

/**
 * Calculates the required slices for a spatial iterator.
 */
public abstract class RequiredSlicesCalculatorImpl implements RequiredSlicesCalculator {

    /**
     * Used to retrieve localized exception messages.
     */
    private static final ExceptionLocalizer exceptionLocalizer = LocalizationFactory.createExceptionLocalizer(RequiredSlicesCalculatorImpl.class);

    private final FormatInstance formatInstance;

    private final FormatRendererContext formatRendererContext;

    private final CoordinateConverter coordinateConverter;

    protected final int rows;

    protected final int columns;

    /**
     * Initialise.
     *
     * @param formatInstance        the spatial iterator instance to be
     *                              processed
     * @param formatRendererContext the context with which the iterator is
     *                              associated
     * @param coordinateConverter   the coordinate converter
     */
    protected RequiredSlicesCalculatorImpl(final FormatInstance formatInstance, final FormatRendererContext formatRendererContext, final CoordinateConverter coordinateConverter) {
        this.formatInstance = formatInstance;
        this.formatRendererContext = formatRendererContext;
        this.coordinateConverter = coordinateConverter;
        rows = coordinateConverter.getRows();
        columns = coordinateConverter.getColumns();
    }

    protected boolean calculate() {
        final SpatialFormatIterator spatial = (SpatialFormatIterator) formatInstance.getFormat();
        if ((formatInstance == null) || formatInstance.isEmpty() || rows == 0) {
            return false;
        }
        NDimensionalIndex childIndex = formatInstance.getIndex().addDimension();
        int numChildren = spatial.getNumChildren();
        final Format child = spatial.getChildAt(0);
        if (numChildren != 1) {
            throw new IllegalStateException(exceptionLocalizer.format("render-spatial-iterator-multiple-children"));
        }
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int position = coordinateConverter.getPosition(col, row);
                childIndex = childIndex.setCurrentFormatIndex(position);
                formatRendererContext.setCurrentFormatIndex(childIndex);
                FormatInstance instance = formatRendererContext.getFormatInstance(child, childIndex);
                if (!instance.isEmpty()) {
                    updateInstance(instance, row, col);
                }
            }
        }
        formatRendererContext.setCurrentFormatIndex(formatInstance.getIndex());
        return true;
    }

    protected abstract void updateInstance(FormatInstance instance, int row, int col);
}
