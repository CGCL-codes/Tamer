package net.sourceforge.fluxion.runcible.graph;

import org.semanticweb.owl.model.OWLClass;
import org.semanticweb.owl.model.OWLObject;
import net.sourceforge.fluxion.runcible.graph.exception.UnsupportedTypeException;

/**
 * A type of node representing owl individuals at the end of an owl object
 * property.  This {@link net.sourceforge.fluxion.graph.Node} can be
 * used to formulate sets of individuals which are the values of a property on
 * some other node.  The {@link OWLObject} for this class is an {@link
 * OWLClass}, so all values are members of the given class.
 *
 * @author Tony Burdett
 * @version 0.1
 * @date 17-Aug-2006
 * @see net.sourceforge.fluxion.graph.Graph
 * @see net.sourceforge.fluxion.graph.Edge
 */
public class ObjectValueNode extends OWLMappingNode {

    public void setOWLObject(OWLObject owlObject) throws UnsupportedTypeException {
        if (owlObject instanceof OWLClass) {
            this.owlObject = owlObject;
        } else {
            throw new UnsupportedTypeException("ObjectValueNode only handles OWLClasses - actually got " + owlObject);
        }
    }
}
