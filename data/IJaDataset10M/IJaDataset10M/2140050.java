package org.marcont.rdftranslator;

import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.reasoner.ReasonerRegistry;

/**
 * This is main class that represents the RDF translation  
 *
 * 
 * @author Sebastian Ryszard Kruk &lt;sebastian.kruk@deri.org&gt;
 */
public class Translation {

    /**
     * The list of rules defined in this translation (the position on the list is important)
     */
    List<Rule> rules = new ArrayList<Rule>();

    /**
     * Source model 
     */
    Model srcModel = null;

    /**
     * Destination model
     */
    Model destModel = null;

    /**
     * Namespaces definition
     */
    Map<String, String> namespaces = new HashMap<String, String>();

    /**
     * The list of premises used to finilize the optimization of premises when the input model is known
     */
    Set<Premise> premises = new HashSet<Premise>();

    /**
     * 
     */
    public Translation() {
        super();
        this.srcModel = ModelFactory.createDefaultModel();
        this.destModel = ModelFactory.createDefaultModel();
    }

    /**
     * Executes this translation scenario
     *
     */
    public void execute(InputStream rd) {
        this.srcModel.read(rd, "");
        for (Premise p : premises) {
            p.optimize();
        }
        for (Rule rule : rules) {
            rule.execute(null);
            if (rule.isTerminal()) {
                break;
            }
        }
    }

    /**
     * Executes this translation scenario with inferencing
     *
     */
    public void execute(InputStream model, InputStream ontology) {
        Model schema = ModelFactory.createDefaultModel();
        schema.read(ontology, "");
        srcModel.read(model, "");
        srcModel = ModelFactory.createInfModel(ReasonerRegistry.getOWLMicroReasoner(), schema, srcModel);
        for (Premise p : premises) {
            p.optimize();
        }
        for (Rule rule : rules) {
            rule.execute(null);
            if (rule.isTerminal()) {
                break;
            }
        }
    }

    /** 
     * This method has been overriden. 
     * @see com.hp.hpl.jena.rdf.model.Model#read(java.io.InputStream, java.lang.String)
     */
    public Model readModel(InputStream arg0, String arg1) {
        return srcModel.read(arg0, arg1);
    }

    /** 
     * This method has been overriden. 
     * @see com.hp.hpl.jena.rdf.model.Model#read(java.io.Reader, java.lang.String)
     */
    public Model readModel(Reader arg0, String arg1) {
        return srcModel.read(arg0, arg1);
    }

    /**
     * Adds new premis to the list of premises defined in this translation
     * @param p
     */
    public void addPremise(Premise p) {
        this.premises.add(p);
    }

    /**
     * @return Returns the destModel.
     */
    public Model getDestModel() {
        return destModel;
    }

    /**
     * @return Returns the srcModel.
     */
    public Model getSrcModel() {
        return srcModel;
    }

    /** 
     * This method has been overriden. 
     * @see java.util.List#add(E)
     */
    public boolean addRule(Rule o) {
        return rules.add(o);
    }

    /** 
     * This method has been overriden. 
     * @see java.util.List#listIterator()
     */
    public ListIterator<Rule> rulesIterator() {
        return rules.listIterator();
    }

    /** 
     * This method has been overriden. 
     * @see java.util.List#remove(java.lang.Object)
     */
    public boolean removeRule(Rule o) {
        return rules.remove(o);
    }

    /**
     * Adds new namespace definition
     * @param id
     * @param uri
     */
    public void addNamespace(String id, String uri) {
        this.namespaces.put(id, uri);
        this.getDestModel().setNsPrefix(id, uri);
    }

    /**
     * Returns namespace URI by Id
     * 
     * @param uri
     * @return
     */
    public String getNamespace(String id) {
        return this.namespaces.get(id);
    }

    /**
     * 
     * @return Collection of possible URIs of namespaces
     */
    public Collection<String> getNamespaceUris() {
        return this.namespaces.values();
    }
}
