package net.sf.wgfa.struts.action.editor;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntResource;

/**
 *
 * @author blair
 */
public class ShowAddNamedUnionClass extends ShowAddNamedClass {

    protected boolean filter(OntClass target, OntResource possibleClass) {
        if (target.isUnionClass()) return !target.asUnionClass().hasOperand(possibleClass); else return true;
    }
}
