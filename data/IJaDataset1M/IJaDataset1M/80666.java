package control;

import javax.servlet.http.HttpServletRequest;
import model.Projeto;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ModificarForm extends ActionForm {

    private Projeto projeto = new Projeto();

    public Projeto getProjeto() {
        return this.projeto;
    }

    public ActionErrors validate(ActionMapping map, HttpServletRequest req) {
        ActionErrors erros = new ActionErrors();
        if (projeto.getNomeCompleto() == null || projeto.getNomeCompleto().equals("")) {
            erros.add("nomeCompletoVazio", new ActionMessage("erro.cadastroCampoNomeCompletoVazio"));
        }
        if (projeto.getNomeCompleto().length() > 40) {
            erros.add("nomeCompletoLimite", new ActionMessage("erro.cadastroCampoNomeCompletoLimite"));
        }
        if (projeto.getDescricao().length() > 200) {
            erros.add("descricaoLimite", new ActionMessage("erro.cadastroCampoDescricaoLimite"));
        }
        if (projeto.getDescricaoPublica().length() > 200) {
            erros.add("descricaoPublicaLimite", new ActionMessage("erro.cadastroCampoDescricaoPublicaLimite"));
        }
        return erros;
    }

    public void reset(ActionMapping map, HttpServletRequest req) {
        this.projeto = new Projeto();
    }
}
