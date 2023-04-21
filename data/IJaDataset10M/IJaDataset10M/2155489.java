package com.museum4j.actions.director;

import java.util.List;
import java.io.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.ServletContext;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.museum4j.negocio.IdiomaManager;
import com.museum4j.negocio.MuseoManager;
import com.museum4j.utils.*;

/**
 * @author Fran
 */
public class IdiomaAction extends DispatchAction {

    private Logger logger = Logger.getLogger(this.getClass());

    private IdiomaManager idiomaManager = null;

    public void setIdiomaManager(IdiomaManager idiomaManager) {
        this.idiomaManager = idiomaManager;
    }

    private MuseoManager museoManager = null;

    public void setMuseoManager(MuseoManager museoManager) {
        this.museoManager = museoManager;
    }

    public ActionForward listar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List listaIdiomas = idiomaManager.getIdiomas();
        request.setAttribute("idiomas", museoManager.getIdiomas(idMuseo));
        request.setAttribute("listaIdiomas", listaIdiomas);
        return (mapping.findForward("listado"));
    }

    public ActionForward insertar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        String codigoIdioma = (String) ((IdiomaForm) form).getCodigoIdioma();
        museoManager.agregarIdiomaComplementario(idMuseo, codigoIdioma);
        List listaIdiomas = idiomaManager.getIdiomas();
        request.setAttribute("idiomas", museoManager.getIdiomas(idMuseo));
        request.setAttribute("listaIdiomas", listaIdiomas);
        return mapping.findForward("listado");
    }

    public ActionForward borrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idiomas[];
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        idiomas = (String[]) ((IdiomaForm) form).getIdiomaBorrar();
        if (idiomas != null) for (int i = 0; i < idiomas.length; i++) {
            logger.info("Borrando el idioma " + idiomas[i]);
            museoManager.borrarIdioma(idMuseo, idiomas[i]);
        }
        request.setAttribute("mensajeInfoAccion", "bueno pues si que va");
        return (mapping.findForward("exito"));
    }

    public ActionForward predeterminar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        String idioma = (String) request.getParameter("idioma");
        logger.info("Accion PREDETERMINAR " + idioma);
        museoManager.setIdiomaPrincipal(idMuseo, idioma);
        return (mapping.findForward("exito"));
    }
}
