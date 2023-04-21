package org.axsl.fo.fo;

import org.axsl.fo.fo.prop.ClipPa;
import org.axsl.fo.fo.prop.CommonBorderPaddingBgPa;
import org.axsl.fo.fo.prop.DisplayAlignPa;
import org.axsl.fo.fo.prop.ExtentPa;
import org.axsl.fo.fo.prop.OverflowPa;
import org.axsl.fo.fo.prop.ReferenceOrientationPa;
import org.axsl.fo.fo.prop.RegionNamePa;
import org.axsl.fo.fo.prop.WritingModePa;

/**
 * An fo:region-after object in XSL-FO.
 * @see "XSL-FO Recommendation 1.0, Section 6.4.15"
 */
public interface RegionAfter extends Region, CommonBorderPaddingBgPa, ClipPa, DisplayAlignPa, ExtentPa, OverflowPa, RegionNamePa, ReferenceOrientationPa, WritingModePa {
}
