package org.axsl.fo.fo;

import org.axsl.fo.Fo;
import org.axsl.fo.fo.prop.ClearPa;
import org.axsl.fo.fo.prop.FloatPa;
import org.axsl.fo.fo.prop.IdPa;
import org.axsl.fo.fo.prop.IndexClassPa;
import org.axsl.fo.fo.prop.IndexKeyPa;

/**
 * An fo:float object in XSL-FO.
 * @see "XSL-FO Recommendation 1.0, Section 6.10.2"
 */
public interface Float extends Fo, FloatPa, ClearPa, IdPa, IndexClassPa, IndexKeyPa {
}
