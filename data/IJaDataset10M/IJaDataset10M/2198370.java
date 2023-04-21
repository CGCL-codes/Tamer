package com.odontosis.view.alterarsenha;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.taglib.tiles.GetAttributeTag;
import com.odontosis.entidade.Usuario;
import com.odontosis.service.UsuarioService;
import com.odontosis.util.StringUtilsOdontosis;
import com.odontosis.view.OdontosisForm;

public class FormAlterarSenha extends OdontosisForm {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private String login;

    private String senhaAntiga;

    private String novaSenha;

    private String confirmacaoSenha;

    public void resetar() {
        login = null;
        senhaAntiga = null;
        novaSenha = null;
        confirmacaoSenha = null;
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        ActionMessages messages = new ActionMessages();
        Collection<String> erros = null;
        if (StringUtilsOdontosis.isVazia(getLogin())) {
            messages.add("login", new ActionMessage("O campo Login é obrigatório"));
        }
        if (StringUtilsOdontosis.isVazia(getSenhaAntiga())) {
            messages.add("senha", new ActionMessage("O campo Senha Atual é obrigatório"));
        }
        if (StringUtilsOdontosis.isVazia(getNovaSenha())) {
            messages.add("novaSenha", new ActionMessage("O campo Nova Senha é  obrigatório"));
        }
        if (messages.size() == 0) {
            try {
                UsuarioService service = new UsuarioService();
                Usuario usuario = service.buscarPorLogin(getLogin());
                if (usuario == null) {
                    messages.add("login", new ActionMessage("O login iformado não corresponde a um usuário válido."));
                } else {
                    if (!(usuario.getSenha().equals(getSenhaAntiga()))) {
                        messages.add("login", new ActionMessage("A senha difere da cadastrada para o login: " + getLogin()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (messages.size() == 0) {
            if (!getNovaSenha().equals(getConfirmacaoSenha())) {
                messages.add("senha", new ActionMessage("A confirmação da senha difere do campo Nova Senha"));
            }
        }
        if (messages.size() > 0 && getMetodo() != null) {
            errors.add(messages);
            erros = new ArrayList<String>();
            for (Iterator iterator = messages.get(); iterator.hasNext(); ) {
                ActionMessage string = (ActionMessage) iterator.next();
                erros.add(string.getKey());
            }
            request.setAttribute("mensagens", erros);
            return errors;
        } else {
            request.setAttribute("mensagens", null);
            return super.validate(mapping, request);
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenhaAntiga() {
        return senhaAntiga;
    }

    public void setSenhaAntiga(String senha) {
        this.senhaAntiga = senha;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }

    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
}
