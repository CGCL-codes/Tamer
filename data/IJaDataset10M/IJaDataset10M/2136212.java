package net.sf.wgfa.struts.action.editor;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.RDFList;
import com.hp.hpl.jena.rdf.model.RDFNode;
import net.sf.wgfa.struts.action.OntologyAction;

/**
 *
 * @author blair
 */
public class ShowAddNamedSuperClass extends ShowAddNamedClass {

    protected boolean filter(OntClass target, OntResource possibleClass) {
        if (target.hasSuperClass(possibleClass)) return false; else return true;
    }

    protected com.hp.hpl.jena.util.iterator.ExtendedIterator listClasses(OntModel model) {
        return model.listNamedClasses();
    }
}
