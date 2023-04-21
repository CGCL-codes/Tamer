package br.usp.poli.mfc.struts.action;

import br.usp.poli.mfc.struts.DAO.DAOFactory;
import br.usp.poli.mfc.struts.DAO.UsuarioDAO;
import br.usp.poli.mfc.struts.actionform.ConsultaUsuarioActionForm;
import br.usp.poli.mfc.struts.bean.Usuario;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Edson
 */
public class ConsultaUsuarioAction extends Action {

    private static final String SUCCESS = "success";

    /** Creates a new instance of ConsultaUsuarioAction */
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("Consulta Usuario Action");
        ConsultaUsuarioActionForm frmConsUser = (ConsultaUsuarioActionForm) form;
        HttpSession xsession = request.getSession();
        Usuario user = (Usuario) xsession.getAttribute("usuario");
        UsuarioDAO usuarioDAO = DAOFactory.getUsuarioDAO();
        ArrayList<Usuario> arrayUsuario;
        if (frmConsUser == null) {
            arrayUsuario = usuarioDAO.getNomeUsuario(null);
        } else {
            String nomeUsuario = frmConsUser.getNomeUsuario();
            arrayUsuario = usuarioDAO.getNomeUsuario(nomeUsuario);
        }
        xsession.setAttribute("arraylist", arrayUsuario);
        return mapping.findForward(SUCCESS);
    }
}
