package it.cefriel.glue2.metamodel.wsml;

import it.cefriel.glue2.util.GlueIdentifier;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.Set;
import java.util.LinkedList;
import org.wsmo.common.IRI;
import it.cefriel.glue2.metamodel.*;
import org.wsmo.mediator.Mediator;
import org.wsmo.mediator.GGMediator;
import it.cefriel.glue2.exceptions.*;

public class WSML_GiGcMediator extends WSML_GlueMediator implements GiGcMediator {

    protected GGMediator obj_content = null;

    public WSML_GiGcMediator(String identifier) {
        super(identifier);
    }

    public WSML_GiGcMediator(String identifier, String content) throws GlueException {
        this(identifier);
        setContent(content);
    }

    public WSML_GiGcMediator(String identifier, Object content) throws GlueException {
        this(identifier);
        setContent(content);
    }

    public void setContent(String content) throws GlueException {
        super.setContent(content);
        obj_content = (GGMediator) obj_content;
    }

    public void setContent(Object content) throws GlueException {
        super.setContent(content);
        if (content instanceof GGMediator) obj_content = (GGMediator) content;
    }

    public Model getGraph() {
        Model base_graph = null;
        LinkedList<String> sources = new LinkedList<String>();
        Set<IRI> mediator_sources = null;
        Resource s = null;
        Resource t = null;
        base_graph = super.getGraph();
        t = base_graph.createResource(obj_content.getTarget().getNamespace() + obj_content.getTarget().getLocalName());
        mediator_sources = ((Mediator) obj_content).listSources();
        for (IRI s_iri : mediator_sources) {
            sources.add(s_iri.getNamespace() + s_iri.getLocalName());
        }
        base_graph.add(t, RDF.type, base_graph.createResource(GlueIdentifier.goal_class_id));
        for (String source : sources) {
            s = base_graph.createResource(source);
            base_graph.add(s, RDF.type, base_graph.createResource(GlueIdentifier.goal_instance_id));
            base_graph.add(s, base_graph.createProperty(GlueIdentifier.glue_ns, GlueIdentifier.goal_memberOf), t);
        }
        return base_graph;
    }
}
