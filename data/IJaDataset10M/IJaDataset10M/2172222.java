package net.datao.jung.ontologiesItems;

import net.datao.datamodel.OProperty;
import org.openrdf.elmo.annotations.rdf;

@rdf("http://workspace/type#directedEdge")
public interface PropertyEdge extends Edge {

    public OProperty getOProperty();

    public void setOProperty(OProperty p);

    public ClassVertex getOriginClassVertex();

    public void setOriginClassVertex(ClassVertex origin);

    public ClassVertex getDestClassVertex();

    public void setDestClassVertex(ClassVertex dest);

    @rdf("http://workspace/udc#hasDefault")
    public String getURI();

    public void setURI(String uri);
}
