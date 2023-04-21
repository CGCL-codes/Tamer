package br.com.bafonline.controller.album;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import br.com.bafonline.model.business.AlbumBO;
import br.com.bafonline.model.business.FigurinhaBO;
import br.com.bafonline.model.dto.AlbumDTO;
import br.com.bafonline.model.dto.UsuarioDTO;
import br.com.bafonline.util.struts.action.BaseAction;
import br.com.bafonline.util.struts.action.Tokens;

/**
 * Action (View) que exclui um �lbum do sistema.
 * @author bafonline
 *
 */
public class ExcluirAlbumUsuarioAction extends BaseAction {

    @SuppressWarnings("unchecked")
    @Override
    public ActionForward processExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigo = request.getParameter("codigo");
        AlbumBO albumBO = new AlbumBO();
        FigurinhaBO figurinhaBO = new FigurinhaBO();
        AlbumDTO albumDTO = albumBO.findById(new Integer(codigo));
        UsuarioDTO usuarioDTO = (UsuarioDTO) request.getSession().getAttribute(Tokens.LOGIN);
        boolean sucesso = figurinhaBO.excluirFigurinhaUsuario(usuarioDTO, albumDTO);
        request.setAttribute("sucesso", new Boolean(sucesso));
        return mapping.findForward(SUCCESS);
    }
}
