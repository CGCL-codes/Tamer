package org.gzigzag.impl;

import java.util.*;
import org.gzigzag.*;
import org.gzigzag.vob.CharRangeIter;

/** A space capable of containing other spaces.
 *  This space does in itself not have functionality for loading and saving.
 * <p>
 * XXX A large amount of complexity is caused by using the Cell's 
 * inclusionObject field as the cell of the space below, since
 * for a while, spaces have been saved so that the creation of a new
 * inclusion and creating connections into the inclusion happen
 * at the same time.
 */
interface CompoundSpaceDim extends Dim {

    CopyableDim getBase();
}
