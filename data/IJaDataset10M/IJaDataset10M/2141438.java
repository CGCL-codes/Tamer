package paystation.domain;

import java.util.*;

/** The strategy that determines what the display reads.

    author: (c) Henrik B�rbak Christensen 2006
*/
public interface DisplayStrategy {

    /** return what the output is from the pay station */
    int reading();
}
