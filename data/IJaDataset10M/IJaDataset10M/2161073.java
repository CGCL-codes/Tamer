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
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.action.DynaActionForm;
import org.apache.struts.upload.FormFile;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import com.museum4j.utils.*;
import com.museum4j.negocio.MultimediaManager;
import com.museum4j.modelo.Multimedia;

/**
 * @author Fran
 */
public class MultimediaAction extends DispatchAction {

    private Logger logger = Logger.getLogger(this.getClass());

    private MultimediaManager multimediaManager = null;

    public void setMultimediaManager(MultimediaManager multimediaManager) {
        this.multimediaManager = multimediaManager;
    }

    public ActionForward listar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ruta = request.getContextPath();
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List listaFicheros = multimediaManager.getFicheros(idMuseo);
        request.setAttribute("publicarEnFlickr", multimediaManager.getPublicarEnFlickr(idMuseo));
        request.setAttribute("publicarEnPicasa", multimediaManager.getPublicarEnPicasa(idMuseo));
        request.setAttribute("listaFicheros", listaFicheros);
        request.setAttribute("rutaFicheros", ruta);
        return (mapping.findForward("listadoFicheros"));
    }

    /**
	 * Obtiene una lista de los objetos multimedia que son
	 * imagenes
	 * @see <a href="http://wiki.moxiecode.com/index.php/TinyMCE:Configuration/external_image_list_url">TinyMCE External Image List</a>
	 */
    public ActionForward listarImagenes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List<Multimedia> imagenes = multimediaManager.getImagenes(idMuseo);
        request.setAttribute("listaImagenes", imagenes);
        return (mapping.findForward("listadoImagenesJS"));
    }

    /**
	 * Obtiene una lista de los objetos multimedia que se
	 * pueden embeber <object...
	 * @see <a href="http://wiki.moxiecode.com/index.php/TinyMCE:Plugins/media">TinyMCE External Media List</a>
	 */
    public ActionForward listarMedias(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        List<Multimedia> medias = multimediaManager.getEmbebibles(idMuseo);
        request.setAttribute("listaMedias", medias);
        return (mapping.findForward("listadoMediasJS"));
    }

    public ActionForward subir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FormFile fichero = ((MultimediaForm) form).getNombreFichero();
        logger.info("getNombreFichero" + fichero.getFileName());
        ActionErrors errors = new ActionErrors();
        if (fichero.getFileSize() <= 0) {
            errors.add("ficheroInvalido", new ActionMessage("error.ficheroInvalido"));
            saveMessages(request, errors);
            return mapping.findForward("ficheroInvalido");
        }
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        FicheroMultimedia.subirFichero(fichero, ruta + Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/");
        boolean resultado = multimediaManager.guardarFichero(idMuseo, fichero);
        ActionMessages messages = new ActionMessages();
        ActionMessage msg;
        if (resultado) msg = new ActionMessage("director.formularios.gestorMultimedia.subirFicheroOk"); else msg = new ActionMessage("director.formularios.gestorMultimedia.subirFicheroError");
        messages.add("subirFichero", msg);
        saveMessages(request, messages);
        return (mapping.findForward("exito"));
    }

    public ActionForward borrarFichero(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        int idMultimedia = Integer.parseInt(request.getParameter("f"));
        ruta += Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/";
        multimediaManager.borrarFichero(ruta, idMultimedia);
        return mapping.findForward("exito");
    }

    public ActionForward enviarFlickr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        int idMultimedia = Integer.parseInt(request.getParameter("f"));
        ruta += Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/";
        multimediaManager.publicarImagenFlickr(ruta, idMultimedia);
        return mapping.findForward("exito");
    }

    public ActionForward actualizarMiFlickr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        multimediaManager.actualizarImagenesEnFlickr(idMuseo);
        return mapping.findForward("exito");
    }

    public ActionForward actualizarMiPicasa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        multimediaManager.actualizarImagenesEnPicasa(idMuseo);
        return mapping.findForward("exito");
    }

    public ActionForward eliminarFlickr(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMultimedia = Integer.parseInt(request.getParameter("f"));
        multimediaManager.eliminarImagenFlickr(idMultimedia);
        return mapping.findForward("exito");
    }

    public ActionForward enviarPicasa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        int idMultimedia = Integer.parseInt(request.getParameter("f"));
        ruta += Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/";
        multimediaManager.publicarImagenPicasa(ruta, idMultimedia);
        return mapping.findForward("exito");
    }

    public ActionForward eliminarPicasa(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        int idMultimedia = Integer.parseInt(request.getParameter("f"));
        ruta += Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/";
        multimediaManager.eliminarImagenPicasa(idMultimedia);
        return mapping.findForward("exito");
    }

    public ActionForward editarFichero(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        request.setAttribute("rutaFichero", Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/");
        return mapping.findForward("editarFichero");
    }

    public ActionForward guardarFichero(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("nfile: " + request.getParameter("nfile"));
        int idMuseo = SessionManager.getIdMuseo(request.getSession());
        String ruta = getServlet().getServletConfig().getServletContext().getRealPath("/");
        File f = new File(ruta + "/" + Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/" + request.getParameter("nfile"));
        logger.info("new_name: " + request.getParameter("new_name"));
        File new_f = new File(FicheroM4j.getDir(f.getParent(), request.getParameter("new_name")));
        BufferedWriter outs = new BufferedWriter(new FileWriter(new_f));
        logger.info("text: " + request.getParameter("text"));
        StringReader text = new StringReader(request.getParameter("text"));
        int i;
        boolean cr = false;
        String lineend = "\n";
        if (request.getParameter("lineformat").equals("dos")) lineend = "\r\n";
        while ((i = text.read()) >= 0) {
            if (i == '\r') cr = true; else if (i == '\n') {
                outs.write(lineend);
                cr = false;
            } else if (cr) {
                outs.write(lineend);
                cr = false;
            } else {
                outs.write(i);
                cr = false;
            }
        }
        outs.flush();
        outs.close();
        logger.info("nfile Html:" + FicheroM4j.conv2Html(request.getParameter("nfile")));
        request.setAttribute("f", FicheroM4j.conv2Html(request.getParameter("nfile")));
        request.setAttribute("rutaFichero", Directorios.DIR_MULTIMEDIA + "/" + idMuseo + "/");
        return mapping.findForward("editarFichero");
    }
}
