package org.jnet.bspt;

import java.io.Serializable;
import javax.vecmath.Point3f;

/**
 * the internal tree is made up of elements ... either Node or Leaf
 *
 * @author Miguel, miguel@jnet.org
 */
abstract class Element implements Serializable {

    static final long serialVersionUID = 1L;

    Bspt bspt;

    int count;

    abstract Element addTuple(int level, Point3f tuple);
}
