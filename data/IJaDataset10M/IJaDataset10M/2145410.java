package com.google.code.cana.web.gratificacao;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.springframework.dao.DataIntegrityViolationException;
import com.google.code.cana.pojo.financeiro.Gratificacao;
import com.google.code.cana.service.GratificacaoService;
import com.google.code.cana.service.RestricaoIntegridadeException;

/**
 * A��o respons�vel por persistir registro de nova gratifica��o.
 * 
 * @author Rodrigo Barbosa Lira
 *
 */
public class CriarAction extends GratificacaoAction {

    private static Log log = LogFactory.getLog(CriarAction.class);

    @Override
    protected void executarLogica(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Instanciando DynaActionForm");
        DynaActionForm dynaForm = (DynaActionForm) form;
        log.debug("Recuperando login do usu�rio escolhido");
        String usuarioLogin = dynaForm.getString("usuarioLogin");
        log.debug("Instanciando novo registro de gratifica��o");
        Gratificacao gratificacao = new Gratificacao();
        log.debug("Copiando informa��es do formul�rio para bean de gratifica��o");
        BeanUtils.copyProperties(gratificacao, form);
        log.debug("Instanciando Servi�o de Gratifica��o");
        GratificacaoService gratificacaoService = (GratificacaoService) getWebApplicationContext().getBean("gratificacaoService");
        log.debug("Persistindo novo registro");
        gratificacaoService.criar(usuarioLogin, gratificacao);
        log.debug("Publicando mensagem de sucesso da opera��o");
        ActionMessages mensagens = getMensagensInformativas(request, true);
        mensagens.add(TAG_SUCESSO, new ActionMessage("mensagem.gratificacao.criar.sucesso", gratificacao.getUsuario().getNome(), gratificacao.getValorPago(), gratificacao.getDataRegistro()));
    }

    @Override
    protected void capturarExcecao(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Instanciando cole��o de mensagens de erro");
        ActionMessages erros = getMensagensErros(request, true);
        log.debug("Recuperando exce��o no contexto da requisi��o");
        Exception ex = getExcecao(request);
        if (ex instanceof RestricaoIntegridadeException || ex instanceof DataIntegrityViolationException) {
            log.debug("Restri��o de integridade");
            erros.add(TAG_FALHA, new ActionMessage("mensagem.gratificacao.criar.falha"));
        } else {
            super.capturarExcecao(mapping, form, request, response);
        }
    }
}
