package uk.ac.shef.wit.aleph.dataset;

import no.uib.cipr.matrix.Vector;
import uk.ac.shef.wit.aleph.AlephException;

/**
 * An {@link Instance} is an example of the class of objects being classified.

 * It is composed of a target, an identifier and a set of features.

 * The target is a double value representing a class label, a class confidence value or a ranking prior.
 * The identifier is any integer value that uniquely identifies the object.
 * The set of features characterizes the object being classified, in the form of a vector of doubles (indexed by
 * feature integer identifiers).
 *
 * @author Jose' Iria, NLP Group, University of Sheffield
 *         (<a  href="mailto:J.Iria@dcs.shef.ac.uk" >email</a>)
 */
public interface Instance extends Comparable<Instance> {

    /**
    * Returns the identifier of the object encoded by this instance.
    * @throws AlephException
    */
    int getObjectId() throws AlephException;

    /**
    * Returns one target associated with this instance. A target is typically a class label, a class confidence value
    * or a ranking prior. For single-label problems, this returns the only target. For multi-label problems, this
    * returns one of the targets at random.
    * @throws AlephException
    */
    double getTarget() throws AlephException;

    /**
    * Returns the targets associated with this instance. A target is typically a class label, a class confidence value
    * or a ranking prior.
    * @throws AlephException
    */
    Vector getTargets() throws AlephException;

    /**
    * Returns the weighed set of features associated with this instance, in the form of a {@link Vector}.
    * @return
    * @throws AlephException
    */
    Vector getFeatures() throws AlephException;
}
