package com.alianzamedica.controllers.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.objectsearch.sqlsearch.ObjectSearch;
import org.w3c.dom.Document;
import com.alianzamedica.businessobject.Lot;
import com.alianzamedica.tools.Enviroment;

/**
 * @author Carlos
 * 
 */
public class DeleteLotAction extends Action {

    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Enviroment env = Enviroment.getInstance();
        Document doc = env.getDocument();
        ObjectSearch search = new ObjectSearch(doc, "com.alianzamedica.connection.ConnectionImpl");
        String id = request.getParameter("id");
        int idN = Integer.parseInt(id);
        if (id != null) {
            Lot l = new Lot();
            l.setId(idN);
            search.deleteObject(l);
        }
        String mensaje = "Se ha borrado el registro de manera exitosa";
        String linkRetorno = "/admin/tag/lotList.do";
        request.setAttribute("mensaje", mensaje);
        request.setAttribute("linkRetorno", linkRetorno);
        return mapping.findForward("success");
    }
}
