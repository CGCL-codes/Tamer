package net.sf.wgfa.struts.action.editor;

import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.struts.action.OntologyAction;
import net.sf.wgfa.struts.form.editor.AddNamedRangeForm;
import net.sf.wgfa.util.ModelCache;

/**
 *
 * @author blair
 */
public class AddNamedRange extends OntologyAction {

    public org.apache.struts.action.ActionForward execute(org.apache.struts.action.ActionMapping mapping, org.apache.struts.action.ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {
        AddNamedRangeForm f = (AddNamedRangeForm) form;
        String ontology = f.getOntology();
        String targetId = f.getTargetId();
        String rangeId = f.getRangeId();
        try {
            OntModel model = ModelCache.getSingleton().getModel(f.getOntology());
            OntResource range = getOntResource(model, false, f.getRangeId());
            OntResource target = getOntResource(model, false, f.getTargetId());
            assert target != null && target.isProperty();
            assert range != null && range.isClass();
            OntProperty targetProperty = target.asProperty();
            OntClass rangeClass = range.asClass();
            try {
                if (targetProperty.isObjectProperty()) {
                    if (target.asObjectProperty().isTransitiveProperty() || target.asObjectProperty().isSymmetricProperty()) {
                        targetProperty.addDomain(rangeClass);
                    }
                }
            } catch (Exception e) {
            }
            targetProperty.addRange(rangeClass);
        } catch (WgfaDatabaseException e) {
        }
        return forwardToEntity(mapping, ontology, false, targetId);
    }
}
