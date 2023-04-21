package org.vrspace.server.filter;

import org.vrspace.server.*;
import org.vrspace.util.*;

/**
Test if client owns this transform
*/
public class OwnedTransformFilter extends TransformFilter {

    /**
  @return c.isOwner( t )
  */
    public boolean testTransform(Transform t, Client c) {
        return c.isOwner(t);
    }

    /**
  */
    public boolean equals(Object o) {
        return (o instanceof OwnedTransformFilter);
    }
}
