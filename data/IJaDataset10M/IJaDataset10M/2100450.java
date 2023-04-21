package com.museum4j.actions.director;

import java.util.*;
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
import com.museum4j.negocio.MuseoManager;
import com.museum4j.negocio.ContenidoManager;
import com.museum4j.modelo.*;
import com.museum4j.utils.*;
import com.museum4j.struts.ContenidoDispatchAction;

/**
 * @author Fran
 */
public class NovedadAction extends ContenidoDispatchAction {

    public ActionForward crear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List<IdiomaMuseo> idiomas = museoManager.getIdiomas(idMuseo);
        request.setAttribute("idiomas", idiomas);
        return (mapping.findForward("crear"));
    }

    public ActionForward guardar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String[] titulo = ((NovedadForm) form).getTitulo();
        String[] idioma = ((NovedadForm) form).getCodigo();
        String[] descripcion = ((NovedadForm) form).getDescripcion();
        logger.info("Despachando GUARDAR.....");
        for (int i = 0; i < titulo.length; i++) {
            logger.info("Idioma " + idioma[i] + ": " + titulo[i]);
            logger.info(descripcion[i]);
        }
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        contenidoManager.guardarNovedad(idMuseo, idioma, titulo, descripcion);
        return (mapping.findForward("exito"));
    }

    public ActionForward editar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idNovedad = (String) request.getParameter("contenido");
        Novedad novedad = (Novedad) contenidoManager.getContenido(Integer.parseInt(idNovedad.trim()));
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List<IdiomaMuseo> idiomas = museoManager.getIdiomas(idMuseo);
        request.setAttribute("contenido", novedad);
        request.setAttribute("idiomas", idiomas);
        return (mapping.findForward("editar"));
    }

    public ActionForward actualizar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idContenido = ((NovedadForm) form).getIdContenido();
        String estiloCSS = ((NovedadForm) form).getEstiloCSS();
        String[] titulo = ((NovedadForm) form).getTitulo();
        String[] idioma = ((NovedadForm) form).getCodigo();
        String[] descripcion = ((NovedadForm) form).getDescripcion();
        logger.info("Despachando ACTUALIZAR.....");
        for (int i = 0; i < titulo.length; i++) {
            logger.info("Idioma " + idioma[i] + ": " + titulo[i]);
            logger.info(descripcion[i]);
        }
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        contenidoManager.actualizarNovedad(idMuseo, idContenido, idioma, titulo, descripcion, estiloCSS);
        return (mapping.findForward("exito"));
    }
}
