package net.datao.jung.ontologiesItems.support;

import net.datao.datamodel.OModelSingleton;
import net.datao.datamodel.OProperty;
import net.datao.jung.ontologiesItems.ClassVertex;
import net.datao.jung.ontologiesItems.PropertyEdge;
import org.openrdf.elmo.annotations.rdf;

@rdf("http://workspace/type#directedEdge")
public class PropertyEdgeSupport {

    private PropertyEdge propertyEdge;

    public PropertyEdgeSupport(PropertyEdge e) {
        this.propertyEdge = e;
    }

    public OProperty getOProperty() {
        return OModelSingleton.getOModel().generateOProperty(getOriginClassVertex().getOClass(), propertyEdge.getURI());
    }

    public void setOProperty(OProperty p) {
        propertyEdge.setURI(p.getURI());
    }

    public ClassVertex getOriginClassVertex() {
        return (ClassVertex) propertyEdge.getOrigin();
    }

    public void setOriginClassVertex(ClassVertex origin) {
        propertyEdge.setOrigin(origin);
    }

    public ClassVertex getDestClassVertex() {
        return (ClassVertex) propertyEdge.getDest();
    }

    public void setDestClassVertex(ClassVertex dest) {
        propertyEdge.setDest(dest);
    }
}
