package com.museum4j.actions.admin;

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
import org.apache.struts.Globals;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.museum4j.negocio.MuseoManager;
import com.museum4j.negocio.DirectorManager;
import com.museum4j.negocio.PlantillaManager;
import com.museum4j.utils.*;
import com.museum4j.modelo.Museo;

/**
 * @author Fran
 */
public class MuseoAction extends DispatchAction {

    private Logger logger = Logger.getLogger(this.getClass());

    private MuseoManager museoManager = null;

    public void setMuseoManager(MuseoManager museoManager) {
        this.museoManager = museoManager;
    }

    private DirectorManager directorManager = null;

    public void setDirectorManager(DirectorManager directorManager) {
        this.directorManager = directorManager;
    }

    private PlantillaManager plantillaManager = null;

    public void setPlantillaManager(PlantillaManager plantillaManager) {
        this.plantillaManager = plantillaManager;
    }

    public ActionForward listar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List listaMuseos = museoManager.getMuseos();
        request.setAttribute("listaMuseos", listaMuseos);
        return (mapping.findForward("listado"));
    }

    public ActionForward crear(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List directores = directorManager.getUsuariosSinMuseo();
        if (directores.size() == 0) {
            ActionMessages messages = new ActionMessages();
            ActionMessage msg = new ActionMessage("admin.formularios.museos.errores.crear");
            messages.add("errorCrearMuseo", msg);
            saveMessages(request, messages);
            return mapping.findForward("noHayDirectores");
        } else {
            request.setAttribute("directores", directores);
            List plantillas = plantillaManager.getPlantillas();
            request.setAttribute("plantillas", plantillas);
            return mapping.findForward("crear");
        }
    }

    public ActionForward guardar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("Guardando museo");
        String nombreMuseo = (String) ((MuseoForm) form).getNombreMuseo();
        String usuario = (String) ((MuseoForm) form).getUsuario();
        String nombrePlantilla = (String) ((MuseoForm) form).getPlantilla();
        String direccion = (String) ((MuseoForm) form).getDireccion();
        String localidad = (String) ((MuseoForm) form).getLocalidad();
        String telefono = (String) ((MuseoForm) form).getTelefono();
        String fax = (String) ((MuseoForm) form).getFax();
        String email = (String) ((MuseoForm) form).getEmail();
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        boolean hayErrores = false;
        ActionMessages messages = new ActionMessages();
        if (nombreMuseo.length() == 0) {
            ActionMessage msg = new ActionMessage("admin.formularios.museos.nombreVacio");
            messages.add("nombreVacio", msg);
            hayErrores = true;
        }
        if (!hayErrores) {
            logger.debug("A guardarlo " + nombreMuseo + "," + usuario + "," + nombrePlantilla);
            Museo museo = museoManager.guardarMuseo(nombreMuseo, usuario, nombrePlantilla, direccion, localidad, telefono, fax, email, ruta);
            if (museo != null) {
                return (mapping.findForward("exito"));
            } else {
                logger.error("No se puede crear museo");
                ActionMessage msg = new ActionMessage("admin.errors.userHasMuseum");
                messages.add("userHasMuseum", msg);
                saveMessages(request, messages);
                return (mapping.findForward("exito"));
            }
        } else {
            saveMessages(request, messages);
            return mapping.findForward("errorInsercion");
        }
    }

    public ActionForward borrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String museos[];
        int nMuseosBorrados = 0;
        try {
            museos = (String[]) ((MuseoForm) form).getMuseosABorrar();
            for (int i = 0; i < museos.length; i++) {
                logger.info("Borrando " + museos[i]);
                museoManager.borrarMuseo(Integer.parseInt(museos[i]));
                nMuseosBorrados++;
            }
        } catch (Exception e) {
            logger.info("No hay museos para borrar");
        }
        ActionMessages messages = new ActionMessages();
        ActionMessage msg = new ActionMessage("admin.formularios.museos.borrarOk", nMuseosBorrados);
        messages.add("borrarMuseo", msg);
        saveMessages(request, messages);
        return (mapping.findForward("exito"));
    }

    public ActionForward administrar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idMuseoString = (String) request.getParameter("idMuseo");
        logger.info("Administrar Museo:" + idMuseoString);
        Museo museo = museoManager.getMuseo(Integer.parseInt(idMuseoString));
        SessionManager.setRol(request.getSession(), "usuarioRegistrado", "");
        SessionManager.setRol(request.getSession(), "rol", "admin");
        SessionManager.setRol(request.getSession(), "museo", museo.getId() + "");
        SessionManager.setRol(request.getSession(), "nombreMuseo", museo.getNombre());
        request.setAttribute("museo", museo);
        return (mapping.findForward("administrar"));
    }

    public ActionForward cambiarCultura(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String cultura = (String) request.getParameter("idioma");
        if (cultura != null) {
            logger.info("Intentando cambiar Locale de " + request.getSession().getAttribute(org.apache.struts.Globals.LOCALE_KEY) + " a " + cultura);
            Locale locale = new Locale(cultura);
            if (locale != null) request.getSession().setAttribute(Globals.LOCALE_KEY, locale);
        }
        return mapping.findForward("cambioCultura");
    }
}
