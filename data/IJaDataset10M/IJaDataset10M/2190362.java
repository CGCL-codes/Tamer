package org.progeeks.util;

import java.io.IOException;

/**
 *  Combines the ResourceLocator and ObjectStreamer interfaces
 *  to present a logical object repsository with additional support
 *  methods.
 *
 *  @version   $Revision: 3846 $
 *  @author    Paul Speed
 */
public interface ResourceRepository extends ResourceLocator, ObjectStreamer {

    /**
     *  Reads an object in an implemetation specific way
     *  based on the specified type and location.
     */
    public Object readObject(String type, String location) throws IOException;

    /**
     *  Writes an object in an implemetation specific way
     *  based on the specified type and location.
     */
    public void writeObject(Object o, String type, String location) throws IOException;
}
