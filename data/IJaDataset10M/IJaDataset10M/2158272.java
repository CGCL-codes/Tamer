package struts;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import relacionamento.ChamadoAtendido;
import transacao.Transacao;
import usuario.Unidade;
import usuario.Usuario;
import chamado.Chamado;
import excecoes.HelpDeskException;
import excecoes.SessaoFinalizadaException;
import facade.FacadeHelpDesk;

public class DelegarChamadoAction extends Action {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FacadeHelpDesk facade = FacadeHelpDesk.getInstance();
        int idChamado = Integer.parseInt(request.getParameter(Constantes.ID_CHAMADO));
        try {
            Usuario usuario = MySession.getInstance().getUsuario();
            String idTecnico = request.getParameter(Constantes.TECNICO);
            facade.delegarChamado(idChamado, usuario.getIdUsuario(), idTecnico);
            Chamado chamado = facade.getChamado(idChamado);
            request.setAttribute(Constantes.CHAMADO, chamado);
            ChamadoAtendido chamadoAtendido = facade.getChamadoAtendido(idChamado);
            request.setAttribute(Constantes.ID_CHAMADO, idChamado);
            request.setAttribute(Constantes.CHAMADO_ATENDIDO, chamadoAtendido);
            List<Transacao> transacoes = facade.getTransacaoParaTecnico(idChamado);
            request.setAttribute(Constantes.TRANSACOES, transacoes);
            Unidade unidade = facade.getUnidadeDoTecnico(usuario.getIdUsuario());
            boolean ehDahUnidade = facade.chamadoEhAtendido(idChamado, unidade.getIdUsuario());
            request.setAttribute(Constantes.EH_DAH_UNIDADE, ehDahUnidade);
            boolean isDono = usuario.getIdUsuario().equals(chamado.getResponsavel());
            request.setAttribute(Constantes.IS_DONO, isDono);
            String gerencia = facade.getGerencia(unidade.getIdUsuario());
            request.setAttribute(Constantes.GERENCIA, gerencia);
            boolean isGerente = facade.isGerente(unidade.getIdUsuario(), usuario.getIdUsuario());
            request.setAttribute(Constantes.IS_GERENTE, isGerente);
            return map.findForward(Constantes.VER_CHAMADO_TECNICO);
        } catch (SessaoFinalizadaException e) {
            return map.findForward(Constantes.INDEX);
        } catch (HelpDeskException e) {
            return LoginAction.redirecionarErro(map, request, e.getMessage());
        }
    }
}
