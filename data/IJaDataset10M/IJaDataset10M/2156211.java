package net.sf.timeslottracker.data.filters;

/**
 * Filter returns all objects (do not filter)
 * 
 * @version File version: $Revision: 1.1 $, $Date: 2007-09-05 17:02:17 $
 * @author Last change: $Author: cnitsa $
 */
class TrueFilter implements Filter<Object> {

    public boolean accept(Object object) {
        return true;
    }
}
