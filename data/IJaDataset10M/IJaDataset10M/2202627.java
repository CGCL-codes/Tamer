package net.sf.wgfa.struts.form.editor;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.ontology.DataRange;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntProperty;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Resource;
import net.sf.wgfa.exceptions.WgfaClassDoesntExistException;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.exceptions.WgfaOntologyDoesntExistException;
import net.sf.wgfa.util.ModelCache;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * @author blair
 */
public class AddDatarangeValueForm extends ActionForm {

    private String ontology;

    private String targetId;

    private String targetIsAnon;

    private String datatype;

    private String value;

    public org.apache.struts.action.ActionErrors validate(org.apache.struts.action.ActionMapping mapping, javax.servlet.http.HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        try {
            OntModel model = ModelCache.getSingleton().getModel(getOntology());
            if (targetIsAnon.equals("0")) {
                OntProperty property = model.getOntProperty(targetId);
                if (property == null && !property.isDataRange()) errors.add("targetId", new ActionMessage("editor.invalidPropertyName"));
            } else {
                Resource property = model.createResource(new AnonId(targetId));
                if (!model.containsResource(property) || !property.canAs(DataRange.class)) errors.add("targetId", new ActionMessage("editor.invalidPropertyName"));
            }
            RDFDatatype t = TypeMapper.getInstance().getTypeByName(getDatatype());
            if (t == null) errors.add("type", new ActionMessage("error.invalidValueType")); else if (!t.isValid(getValue())) errors.add("value", new ActionMessage("error.invalidValue"));
        } catch (WgfaDatabaseException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("editor.databaseException"));
        } catch (WgfaOntologyDoesntExistException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("editor.ontologyDoesntExistException", getOntology()));
        }
        return errors;
    }

    public String getOntology() {
        return ontology;
    }

    public void setOntology(String ontology) {
        this.ontology = ontology;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getTargetIsAnon() {
        return targetIsAnon;
    }

    public void setTargetIsAnon(String targetIsAnon) {
        this.targetIsAnon = targetIsAnon;
    }
}
