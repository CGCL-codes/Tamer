package com.simpledata.bc.components.worksheet.workplace.tools;

import com.simpledata.bc.tools.OrderedMapOfDoubles;

/**
 * Interface for all data that represent a slice context
 */
public interface DataBySlice {

    /** create a Line at this key position **/
    public void createLineAt(double key);

    /** get OrderedMapOfDoubles **/
    public OrderedMapOfDoubles getOmod();
}
