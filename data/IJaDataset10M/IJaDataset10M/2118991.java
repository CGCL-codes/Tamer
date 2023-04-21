package br.com.bafonline.controller.album;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import br.com.bafonline.model.business.AlbumBO;
import br.com.bafonline.model.dto.AlbumDTO;
import br.com.bafonline.util.struts.action.BaseAction;

/**
 * Action (View) que prepara a edi��o de um �lbum, recuperando os respectivos dados do mesmo.
 * @author bafonline
 *
 */
public class PrepareEditarAlbumAction extends BaseAction {

    @Override
    public ActionForward processExecute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codigo = request.getParameter("codigo");
        if (codigo == null || "".equals(codigo)) {
            codigo = "0";
        }
        AlbumBO albumBO = new AlbumBO();
        AlbumDTO albumDTO = albumBO.findById(new Integer(codigo));
        request.setAttribute("albumDTO", albumDTO);
        return mapping.findForward(SUCCESS);
    }
}
