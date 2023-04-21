package net.sf.wgfa.struts.action.editor;

import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.struts.action.OntologyAction;
import net.sf.wgfa.struts.form.editor.SelectDatatypePropertyValueForm;
import net.sf.wgfa.struts.form.editor.SelectObjectPropertyValueForm;
import net.sf.wgfa.util.ModelCache;

/**
 *
 * @author blair
 */
public class AddDatatypePropertyValue extends OntologyAction {

    public org.apache.struts.action.ActionForward execute(org.apache.struts.action.ActionMapping mapping, org.apache.struts.action.ActionForm form, javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws Exception {
        SelectDatatypePropertyValueForm f = (SelectDatatypePropertyValueForm) form;
        String ontology = f.getOntology();
        String targetIsAnon = f.getTargetIsAnon();
        String targetId = f.getTargetId();
        String propertyUri = f.getDatatypePropertyUri();
        boolean isDatarange = f.getType().equals("datarange");
        String datarange = f.getDatarange();
        String datatype = f.getDatatype();
        String value = f.getValue();
        try {
            OntModel model = ModelCache.getSingleton().getModel(ontology);
            assert model != null;
            OntResource target = getOntResource(model, targetIsAnon.equals("1"), targetId);
            OntResource property = getOntResource(model, false, propertyUri);
            assert target != null;
            assert property != null && property.isProperty();
            OntProperty p = property.asProperty();
            if (isDatarange) {
                boolean hasRange = false;
                ExtendedIterator it = p.listRange();
                while (it.hasNext() && !hasRange) {
                    OntResource r = (OntResource) it.next();
                    if (r.isDataRange()) {
                        DataRange dr = r.asDataRange();
                        ExtendedIterator jt = dr.listOneOf();
                        while (jt.hasNext() && !hasRange) {
                            Literal l = (Literal) jt.next();
                            if (l.getLexicalForm().equals("value")) {
                                datatype = l.getDatatypeURI();
                                hasRange = true;
                            }
                        }
                    }
                }
                target.addProperty(p, model.createTypedLiteral(value, datatype));
            } else {
                target.addProperty(p, model.createTypedLiteral(value, datatype));
            }
            return forwardToEntity(mapping, ontology, targetIsAnon.equals("1"), targetId);
        } catch (WgfaDatabaseException e) {
        }
        return null;
    }
}
