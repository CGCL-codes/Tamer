package org.axsl.fo.fo;

import org.axsl.fo.Fo;
import org.axsl.fo.fo.prop.BreakAfterPa;
import org.axsl.fo.fo.prop.BreakBeforePa;
import org.axsl.fo.fo.prop.CaptionSidePa;
import org.axsl.fo.fo.prop.ClearPa;
import org.axsl.fo.fo.prop.CommonAccessibilityPa;
import org.axsl.fo.fo.prop.CommonAuralPa;
import org.axsl.fo.fo.prop.CommonBorderPaddingBgPa;
import org.axsl.fo.fo.prop.CommonMarginBlockPa;
import org.axsl.fo.fo.prop.CommonMarginInlineOptimumPa;
import org.axsl.fo.fo.prop.CommonRelativePositionPa;
import org.axsl.fo.fo.prop.IdPa;
import org.axsl.fo.fo.prop.IndexClassPa;
import org.axsl.fo.fo.prop.IndexKeyPa;
import org.axsl.fo.fo.prop.IntrusionDisplacePa;
import org.axsl.fo.fo.prop.KeepTogetherPa;
import org.axsl.fo.fo.prop.KeepWithNextPa;
import org.axsl.fo.fo.prop.KeepWithPreviousPa;
import org.axsl.fo.fo.prop.TextAlignPa;

/**
 * <p>An fo:table-and-caption object in XSL-FO.</p>
 *
 * <p>Note the inclusion of {@link CommonMarginInlineOptimumPa} in the "extends"
 * list.
 * The "space-start" and "space-end" properties are not included in the list of
 * properties that apply to fo:table-and-caption.
 * However, "margin-left", "margin-right", "margin-top", and "margin-bottom" are
 * included in that list, and, in the aXSL normalizations scheme, these can map
 * to "space-start" and "space-end". Therefore, access to those properties must
 * be made available to an fo:table-and-caption.</p>
 *
 * @see "XSL-FO Recommendation 1.0, Section 6.7.2"
 */
public interface TableAndCaption extends Fo, CommonAccessibilityPa, CommonAuralPa, CommonBorderPaddingBgPa, CommonMarginBlockPa, CommonMarginInlineOptimumPa, CommonRelativePositionPa, BreakAfterPa, BreakBeforePa, CaptionSidePa, ClearPa, IdPa, IndexClassPa, IndexKeyPa, IntrusionDisplacePa, KeepTogetherPa, KeepWithNextPa, KeepWithPreviousPa, TextAlignPa {
}
