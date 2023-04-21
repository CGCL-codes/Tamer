package org.axsl.fo.fo;

import org.axsl.fo.Fo;
import org.axsl.fo.fo.prop.BlockProgressionDimensionPa;
import org.axsl.fo.fo.prop.BorderAfterPrecedencePa;
import org.axsl.fo.fo.prop.BorderBeforePrecedencePa;
import org.axsl.fo.fo.prop.BorderEndPrecedencePa;
import org.axsl.fo.fo.prop.BorderStartPrecedencePa;
import org.axsl.fo.fo.prop.ColumnNumberPa;
import org.axsl.fo.fo.prop.CommonAccessibilityPa;
import org.axsl.fo.fo.prop.CommonAuralPa;
import org.axsl.fo.fo.prop.CommonBorderPaddingBgPa;
import org.axsl.fo.fo.prop.CommonRelativePositionPa;
import org.axsl.fo.fo.prop.DisplayAlignPa;
import org.axsl.fo.fo.prop.EmptyCellsPa;
import org.axsl.fo.fo.prop.IdPa;
import org.axsl.fo.fo.prop.IndexClassPa;
import org.axsl.fo.fo.prop.IndexKeyPa;
import org.axsl.fo.fo.prop.InlineProgressionDimensionPa;
import org.axsl.fo.fo.prop.NumberColumnsSpannedPa;
import org.axsl.fo.fo.prop.NumberRowsSpannedPa;
import org.axsl.fo.fo.prop.RelativeAlignPa;

/**
 * An fo:table-cell object in XSL-FO.
 * @see "XSL-FO Recommendation 1.0, Section 6.7.10"
 */
public interface TableCell extends Fo, CommonAccessibilityPa, CommonAuralPa, CommonBorderPaddingBgPa, CommonRelativePositionPa, BorderAfterPrecedencePa, BorderBeforePrecedencePa, BorderEndPrecedencePa, BorderStartPrecedencePa, BlockProgressionDimensionPa, ColumnNumberPa, DisplayAlignPa, RelativeAlignPa, EmptyCellsPa, IdPa, IndexClassPa, IndexKeyPa, InlineProgressionDimensionPa, NumberColumnsSpannedPa, NumberRowsSpannedPa {
}
