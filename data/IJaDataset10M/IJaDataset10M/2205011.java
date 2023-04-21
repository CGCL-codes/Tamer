package com.google.code.cana.web.produto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import com.google.code.cana.service.ObjetoNaoEncontradoException;
import com.google.code.cana.web.CanaBaseAction;

/**
 * A��o base para tratamento das informa��es de Produto em n�vel web.
 * 
 * @author Rodrigo Barbosa Lira
 * @author Taciano Pinheiro
 * 
 */
public class ProdutoAction extends CanaBaseAction {

    private static Log log = LogFactory.getLog(ProdutoAction.class);

    @Override
    protected void capturarExcecao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Instanciando cole��o de mensagens de erro");
        ActionMessages erros = getMensagensErros(request, true);
        log.debug("Recuperando exce��o no contexto da requisi��o");
        Exception exception = getExcecao(request);
        if (exception instanceof ObjetoNaoEncontradoException) {
            log.debug("Produto n�o foi encontrado");
            erros.add(TAG_FALHA, new ActionMessage("mensagem.produto.recuperar.falha"));
        } else {
            super.capturarExcecao(mapping, form, request, response);
        }
    }
}
