package uk.ac.shef.wit.trex.dataset.generator;

import uk.ac.shef.wit.trex.TrexException;
import uk.ac.shef.wit.trex.representation.Representation;

/**
 * Given the representation, provides an iterator over the objects to be classified. An 'object to be classified' is
 * simply a coherent set of <i>RepresentationNode</i>s taken from the representation.
 * This is the minimal interface any object enumerator implementation must adhere to.
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public interface ObjectEnumerator {

    ObjectIterator enumerate(Representation representation) throws TrexException;
}
