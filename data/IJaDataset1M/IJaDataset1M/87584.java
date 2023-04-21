package com.iver.cit.gvsig.fmap.spatialindex;

import java.util.Map;
import org.gvsig.exceptions.BaseException;

public class SpatialIndexException extends BaseException {

    Exception e;

    public SpatialIndexException(Exception e) {
        this.e = e;
    }

    public SpatialIndexException() {
    }

    protected Map values() {
        return null;
    }

    public void printStackTrace() {
        e.printStackTrace();
    }
}
