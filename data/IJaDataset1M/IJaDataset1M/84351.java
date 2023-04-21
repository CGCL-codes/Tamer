package com.google.code.cana.web.usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.google.code.cana.pojo.pessoas.Usuario;
import com.google.code.cana.service.ObjetoNaoEncontradoException;
import com.google.code.cana.web.CanaBaseAction;

/**
 * A��o base para tratamento de cadastro de Usu�rio
 * 
 * @author Taciano Pinheiro de Almeida Alc�ntara
 *
 */
public class UsuarioAction extends CanaBaseAction {

    private static Log log = LogFactory.getLog(UsuarioAction.class);

    @Override
    protected void executarLogica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Publicando lista de poss�veis pap�is no contexto da requisi��o");
        request.setAttribute("papeis", Usuario.papeis);
    }

    @Override
    protected void capturarExcecao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Iniciando tratamento de exce��o");
        log.debug("Instanciando cole��o de mensagens de erro");
        ActionMessages erros = getMensagensErros(request, true);
        log.debug("Recuperanco exce��o lan�ada");
        Exception exception = getExcecao(request);
        if (exception instanceof ObjetoNaoEncontradoException) {
            log.debug("Foi detectada uma exce��o de objeto n�o encontrado");
            erros.add(TAG_FALHA, new ActionMessage("mensagem.usuario.recuperar.falha"));
        } else {
            super.capturarExcecao(mapping, form, request, response);
        }
    }
}
