package net.sf.wgfa.struts.action.selection;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.wgfa.exceptions.WgfaDatabaseException;
import net.sf.wgfa.selection.RDQLResourceFilter;
import net.sf.wgfa.selection.ResourceFilter;
import net.sf.wgfa.selection.ResourceSelection;
import net.sf.wgfa.selection.ResourceSelector;
import net.sf.wgfa.struts.form.selection.ElementsSelectionForm;
import net.sf.wgfa.struts.form.selection.POFilterForm;
import net.sf.wgfa.struts.form.selection.RDQLFilterForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author tobias
 */
public class AddFilterAction extends SelectionAction {

    public ActionForward doSelectionAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, ResourceSelector selector) throws WgfaDatabaseException {
        Model m = selector.getModel();
        if (form instanceof POFilterForm) {
            POFilterForm poff = (POFilterForm) form;
            Property p = m.getProperty(poff.getProperty());
            RDFNode n = m.getResource(poff.getObject());
            ResourceSelection rf = new ResourceFilter(p, n);
            selector.addFilter(rf);
        } else if (form instanceof RDQLFilterForm) {
            RDQLFilterForm rff = (RDQLFilterForm) form;
            ResourceSelection rf = new RDQLResourceFilter(rff.getRdql(), "x");
            selector.addFilter(rf);
        }
        ResourceSelection visibleSelected = selector.getSelection().intersection(selector.getShowSelection());
        selector.setSelection(visibleSelected);
        return mapping.findForward("success");
    }
}
