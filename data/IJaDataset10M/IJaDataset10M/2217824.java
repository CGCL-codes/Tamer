package struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import usuario.Usuario;
import util.HelpDeskUtil;
import excecoes.HelpDeskException;
import excecoes.SessaoFinalizadaException;
import facade.FacadeHelpDesk;

public class CriarUnidadeAction extends Action {

    public ActionForward execute(ActionMapping map, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FacadeHelpDesk facade = FacadeHelpDesk.getInstance();
        String id = "";
        String idGerente = "";
        try {
            Usuario usuario = MySession.getInstance().getUsuario();
            String nome = request.getParameter(Constantes.NOME);
            id = request.getParameter(Constantes.EMAIL);
            String dominio = MySession.getInstance().getDominio();
            if (!id.contains(dominio)) {
                id = id + dominio;
            }
            String suporte = request.getParameter(Constantes.SUPORTE);
            boolean isSuporte = !HelpDeskUtil.isNullOrVazio(suporte);
            facade.criarUnidade(id, nome, isSuporte);
            if (isSuporte) {
                String gerencia = request.getParameter(Constantes.GERENCIA);
                String nomeGerente = request.getParameter(Constantes.NOME_GERENTE);
                idGerente = request.getParameter(Constantes.EMAIL_GERENTE);
                facade.setGerencia(id, gerencia);
                if (!idGerente.contains(dominio)) {
                    idGerente = idGerente + dominio;
                }
                facade.criarTecnico(idGerente, nomeGerente);
                facade.relacionarTecnicoComUnidade(idGerente, id);
                facade.setarGerente(idGerente, id);
            }
            return map.findForward(Constantes.CARREGAR_CRIAR_UNIDADE);
        } catch (SessaoFinalizadaException e) {
            return map.findForward(Constantes.INDEX);
        } catch (HelpDeskException e) {
            try {
                facade.removerUnidade(id);
                facade.removerTecnico(idGerente);
            } catch (Exception qualquer) {
            }
            return LoginAction.redirecionarErro(map, request, e.getMessage());
        }
    }
}
