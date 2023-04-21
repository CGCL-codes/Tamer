package org.larozanam.arq.services;

import java.io.IOException;
import org.apache.log4j.Logger;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentClassResolver;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.larozanam.admin.beans.Permissao;
import org.larozanam.arq.acesso.GerenciadorFluxo;
import org.larozanam.arq.acesso.Processo;
import org.larozanam.arq.acesso.SessaoUsuario;
import org.larozanam.arq.excecoes.AcessoNegadoException;
import org.larozanam.arq.excecoes.ArquiteturaException;
import org.larozanam.arq.excecoes.RedirectException;
import org.larozanam.arq.pages.Home;
import org.larozanam.arq.pages.Index;

public class FiltroControleAcesso implements ComponentEventRequestFilter, PageRenderRequestFilter {

    private final ApplicationStateManager asm;

    private final ComponentClassResolver resolver;

    private final Logger log = Logger.getLogger(FiltroControleAcesso.class);

    private final Permissao permissao = new Permissao();

    public FiltroControleAcesso(final ApplicationStateManager asoManager, final ComponentClassResolver resolver) {
        this.asm = asoManager;
        this.resolver = resolver;
    }

    public void handle(ComponentEventRequestParameters params, ComponentEventRequestHandler handler) throws IOException {
        log.trace("----------------------handling component----------------------");
        log.trace("ActivePageName.......:" + params.getActivePageName());
        log.trace("ContainingPageName...:" + params.getContainingPageName());
        log.trace("EventType............:" + params.getEventType());
        log.trace("NestedComponentId....:" + params.getNestedComponentId());
        handler.handle(params);
    }

    public void handle(PageRenderRequestParameters params, PageRenderRequestHandler handler) throws IOException {
        String pageName = params.getLogicalPageName();
        String className = resolver.resolvePageNameToClassName(pageName);
        Processo p = Processo.fromClassName(className);
        log.trace("======================handling page==========================");
        log.trace("LogicalPageName......:" + pageName);
        log.trace("ClassName............:" + className);
        log.trace("Permission required..:" + p);
        GerenciadorFluxo gerenciadorPaginas = asm.get(GerenciadorFluxo.class);
        gerenciadorPaginas.addPagina(className, pageName);
        if (p != Processo.PROCESSOS_LIVRE_ACESSO) {
            SessaoUsuario sessaoUsuario = asm.get(SessaoUsuario.class);
            if (!sessaoUsuario.isUsuarioAtivo()) {
                gerenciadorPaginas.setBloquearRequestSemSessao(true);
                log.trace("ultimo request do usu�rio:" + gerenciadorPaginas.getRequestSemSessao());
                RedirectException ex = new RedirectException(Index.class);
                gerenciadorPaginas.informarErro(new AcessoNegadoException("A sua sess�o expirou. " + "Fa�a seu login novamente para continuar utilizando o sistema"));
                log.info("A sess�o expirou, uma exce��o de redirecionamento ser� lan�ada.");
                throw ex;
            } else {
                permissao.setProcesso(p);
                if (!sessaoUsuario.getUsuario().getPermissoesAcesso().contains(permissao)) {
                    gerenciadorPaginas.informarErro(new AcessoNegadoException(String.format("Voc� n�o possui acesso � pagina %s." + "\n Se o acesso � realmente necess�rio contate o Administrador e solicite-o", pageName)));
                    throw new RedirectException(Home.class);
                }
            }
        }
        handler.handle(params);
    }
}
