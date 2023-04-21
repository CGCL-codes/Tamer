package org.moyoman.module.expert;

import org.moyoman.module.*;
import org.moyoman.util.*;

/** The class that implements this interface
  * will determine good and bad moves.  This module type
  * is meant for things like neural networks that suggest
  * moves without using it as the only source of moves.
  * The MoveGenerator module can use the output of the
  * various implementations of Expert along with the results
  * of various other modules.
  * <p>
  * The Expert module type is different from the others in that
  * for most module types, only one implementation is used.  For
  * Expert, if there are multiple implementations, it is anticipated
  * that the results of all of them will be used in a weighted manner.
  * <p>
  * There are no methods defined in this interface since
  * the implementing class just has to extend Module.
  */
public interface Expert extends ModuleInterface {

    public final float CONFIDENCE = 0.4f;
}
