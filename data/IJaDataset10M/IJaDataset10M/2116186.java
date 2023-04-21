package org.redsocial.action.perfil;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.redsocial.action.BaseAction;
import org.redsocial.dao.EstudiosDAO;
import org.redsocial.dao.TrabajosDAO;
import org.redsocial.dao.UsuarioDAO;
import org.redsocial.form.UsuarioForm;
import org.redsocial.form.VerResultadoYRedirectHelper;
import org.redsocial.model.Estudios;
import org.redsocial.model.Trabajos;
import org.redsocial.model.Usuario;

public class VerPerfilAction extends BaseAction {

    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        UsuarioForm bean = (UsuarioForm) form;
        UsuarioDAO usrDao = (UsuarioDAO) getSpringBean(request, "usuarioDAO");
        Usuario usr = usrDao.select(bean.getId());
        if (usr == null || !usr.getPerfilpublico()) {
            VerResultadoYRedirectHelper hlp = new VerResultadoYRedirectHelper();
            hlp.setTitulo(getMessage("error.perfilOFF", null, request.getLocale()));
            request.setAttribute(VerResultadoYRedirectHelper.RQST_KEY, hlp);
            return mapping.findForward("perfilOFF");
        }
        TrabajosDAO currosDao = (TrabajosDAO) getSpringBean(request, "trabajosDAO");
        EstudiosDAO estudiosDao = (EstudiosDAO) getSpringBean(request, "estudiosDAO");
        List<Trabajos> curros = currosDao.getTrabajosUsuario(bean.getId());
        List<Estudios> estudios = estudiosDao.getEstudiosUsuario(bean.getId());
        List<Usuario> amigos = usrDao.selectFriends(bean.getId());
        request.setAttribute("usuario", usr);
        request.setAttribute("trabajos", curros);
        request.setAttribute("estudios", estudios);
        request.setAttribute("amigos", amigos);
        return mapping.findForward("success");
    }
}
