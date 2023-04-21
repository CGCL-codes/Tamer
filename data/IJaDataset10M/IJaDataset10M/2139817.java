package net.sf.wgfa.struts.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.struts.form.StatementLookupForm;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/** 
 * MyEclipse Struts
 * Creation date: 06-17-2005
 * 
 * XDoclet definition:
 * @struts:action path="/statementLookup" name="statementLookupForm" input="/form/statementLookup.jsp" scope="request" validate="true"
 * @struts:action-forward name="success" path="/listStatements.jsp"
 */
public class StatementLookupAction extends Action {

    /** 
	 * Method execute
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        StatementLookupForm slf = (StatementLookupForm) form;
        Model m = (Model) this.getServlet().getServletContext().getAttribute("model");
        Resource subject = findResource(m, slf.getSubjectURI());
        Property predicate = findProperty(m, slf.getPredicateURI());
        Resource object = findResource(m, slf.getObjectURI());
        ExtendedIterator ei = m.listStatements(subject, predicate, object);
        System.out.println("Subject: " + debugObjRes(subject));
        System.out.println("Predicate: " + debugProp(predicate));
        System.out.println("Object: " + debugObjRes(object));
        return mapping.findForward("success");
    }

    private String debugObjRes(Resource r) {
        if (r != null) {
            return r.getURI();
        } else {
            return "null";
        }
    }

    private String debugProp(Property p) {
        if (p != null) {
            return p.getURI();
        } else {
            return "null";
        }
    }

    private Resource findResource(Model m, String uri) {
        uri = m.expandPrefix(uri);
        Resource r = m.createResource(uri);
        if (m.contains(r, null)) {
            return r;
        } else {
            return null;
        }
    }

    private Property findProperty(Model m, String uri) {
        uri = m.expandPrefix(uri);
        Property p = m.createProperty(uri);
        if (m.contains((Resource) null, p, (RDFNode) null)) {
            return p;
        } else {
            return null;
        }
    }
}
