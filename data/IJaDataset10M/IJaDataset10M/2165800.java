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

public class IndexUnidadeAction extends Action {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FacadeHelpDesk facade = FacadeHelpDesk.getInstance();
        try {
            Unidade unidade = (Unidade) MySession.getInstance().getUsuario();
            request.setAttribute(Constantes.UNIDADE, Util.capitular(unidade.toString()));
            List<Chamado> chamados = facade.getChamadosSolicitados(unidade.getIdUsuario());
            request.setAttribute(Constantes.CHAMADOS, chamados);
            request.setAttribute(Constantes.QUANT_CHAMADOS_RESULT, chamados.size());
            if (unidade.isSuporte()) {
                chamados = facade.getChamadosUnidade(unidade.getIdUsuario());
                request.setAttribute(Constantes.QUANT_CHAMADOS_RESULT, chamados.size());
                request.setAttribute(Constantes.CHAMADOS, chamados);
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
