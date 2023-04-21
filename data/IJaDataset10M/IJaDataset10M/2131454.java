package struts;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import usuario.Unidade;
import util.Util;
import chamado.Chamado;
import excecoes.HelpDeskException;
import excecoes.SessaoFinalizadaException;
import facade.FacadeHelpDesk;

/**
 * Chamados solicitados por uma unidade
 * @author almfarias
 *
 */
public class ListarChamadosSolicitadosUnidadeAction extends Action {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FacadeHelpDesk facade = FacadeHelpDesk.getInstance();
        try {
            Unidade unidade = (Unidade) MySession.getInstance().getUsuario();
            List<Chamado> chamadosSolicitadoPorEstaUnidade = facade.getChamadosSolicitados(unidade.getIdUsuario());
            request.setAttribute(Constantes.UNIDADE, Util.capitular(unidade.toString()));
            request.setAttribute(Constantes.QUANT_CHAMADOS_RESULT, chamadosSolicitadoPorEstaUnidade.size());
            request.setAttribute(Constantes.CHAMADOS, chamadosSolicitadoPorEstaUnidade);
            request.setAttribute(Constantes.PODE_VER_CHAMADOS_REPASSADOS, true);
            if (unidade.isSuporte()) {
                return map.findForward(Constantes.UNIDADE_SUPORTE);
            } else {
                return map.findForward(Constantes.UNIDADE);
            }
        } catch (SessaoFinalizadaException e) {
            return map.findForward(Constantes.INDEX);
        } catch (HelpDeskException e) {
            return LoginAction.redirecionarErro(map, request, e.getMessage());
        }
    }
}
