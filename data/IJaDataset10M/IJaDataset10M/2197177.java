package org.jmol.bspt;

/**
 * the internal tree is made up of elements ... either Node or Leaf
 *
 * @author Miguel, miguel@jmol.org
 */
abstract class Element {

    Bspt bspt;

    int count;

    abstract Element addTuple(int level, Tuple tuple);
}
