package net.sf.wgfa.struts.action.editor;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import net.sf.wgfa.struts.action.OntologyAction;
import org.apache.struts.action.ActionRedirect;

/**
 *
 * @author blair
 */
public class AddAnonEquivalentClass extends AddAnonClass {

    protected void addClass(OntClass target, OntModel model) {
        target.addEquivalentClass(model.createClass());
    }
}
