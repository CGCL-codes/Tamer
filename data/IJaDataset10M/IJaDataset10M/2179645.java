package uk.ac.ebi.intact.application.editor.struts.framework;

import org.apache.struts.action.ActionServlet;
import uk.ac.ebi.intact.application.editor.business.EditorService;
import uk.ac.ebi.intact.application.editor.event.EventListener;
import uk.ac.ebi.intact.application.editor.struts.framework.util.EditorConstants;
import uk.ac.ebi.intact.application.editor.util.LockManager;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * This is Intact editor specific action servlet class. This class is
 * responsible for initializing application wide resources.
 *
 * @author Sugath Mudali (smudali@ebi.ac.uk)
 * @version $Id: EditorActionServlet.java 3758 2005-03-07 13:16:53Z smudali $
 */
public class EditorActionServlet extends ActionServlet {

    public void init() throws ServletException {
        super.init();
        ServletContext ctx = super.getServletContext();
        EditorService service = EditorService.getInstance();
        ctx.setAttribute(EditorConstants.EDITOR_SERVICE, service);
        ctx.setAttribute(EditorConstants.EDITOR_TOPICS, service.getIntactTypes());
        ctx.setAttribute(EditorConstants.LOCK_MGR, LockManager.getInstance());
        ctx.setAttribute(EditorConstants.EVENT_LISTENER, EventListener.getInstance());
        ResourceBundle msgres = ResourceBundle.getBundle("uk.ac.ebi.intact.application.editor.MessageResources");
        ctx.setAttribute(EditorConstants.ANCHOR_MAP, getAnchorMap(msgres));
    }

    private Map getAnchorMap(ResourceBundle rb) {
        Map map = new HashMap();
        map.put("error.label", "info");
        map.put("error.annotation.topic", "annotation");
        map.put("error.annotation.exists", "annotation");
        map.put(rb.getString("annotations.button.add"), "annotation");
        map.put("error.edit.annotation.exists", "annot.edit");
        map.put(rb.getString("annotations.button.edit"), "annot.edit");
        map.put(rb.getString("annotations.button.save"), "annot.edit");
        map.put(rb.getString("annotations.button.delete"), "annot.edit");
        map.put("error.xref.database", "xref");
        map.put("error.xref.pid", "xref");
        map.put("error.xref.exists", "xref");
        map.put(rb.getString("xrefs.button.add"), "xref");
        map.put("error.edit.xref.exists", "xref.edit");
        map.put(rb.getString("xrefs.button.edit"), "xref.edit");
        map.put(rb.getString("xrefs.button.save"), "xref.edit");
        map.put(rb.getString("xrefs.button.delete"), "xref.edit");
        map.put("error.exp.biosrc", "info");
        map.put("error.exp.inter", "info");
        map.put("error.exp.ident", "info");
        map.put("error.exp.int.search.input", "exp.int.search");
        map.put("error.exp.int.search.empty", "exp.int.search");
        map.put("error.exp.int.search.many", "exp.int.search");
        map.put(rb.getString("int.proteins.button.save"), "int.protein.search");
        map.put("error.int.protein.edit.role", "int.protein.search");
        map.put(rb.getString("int.proteins.button.search"), "int.protein.search");
        map.put("error.int.sanity.unsaved.prot", "int.protein.search");
        map.put("error.int.protein.search.input", "int.protein.search");
        map.put("error.int.protein.search.ac", "int.protein.search");
        map.put("error.int.protein.search.sp", "int.protein.search");
        map.put("error.int.protein.search.many", "int.protein.search");
        map.put("error.int.protein.search.empty", "int.protein.search");
        map.put("error.int.protein.search.empty.parse", "int.protein.search");
        map.put("error.int.sanity.exp", "int.exp.search");
        map.put("error.int.exp.search.input", "int.exp.search");
        map.put("error.int.exp.search.empty", "int.exp.search");
        map.put("error.int.exp.search.many", "int.exp.search");
        map.put("int.interaction", "info");
        map.put("int.organism", "info");
        map.put("error.taxid.mask", "info");
        map.put("error.bs.sanity.taxid", "info");
        map.put("error.bs.sanity.taxid.dup", "info");
        map.put(rb.getString("int.proteins.button.feature.link"), "feature.link");
        map.put(rb.getString("int.proteins.button.feature.unlink"), "feature.link");
        map.put("error.int.feature.link.error", "feature.link");
        map.put("error.int.feature.unlink.error", "feature.link");
        map.put(rb.getString("int.proteins.button.feature.delete"), "feature.link");
        return map;
    }
}
