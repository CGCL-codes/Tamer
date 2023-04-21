package br.com.rafael.ladderWin.listener;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import br.com.rafael.ladderWin.controller.CampeonatoController;
import br.com.rafael.ladderWin.util.FacesUtils;

public class Autorizador implements PhaseListener {

    public void beforePhase(PhaseEvent event) {
        FacesContext contexto = event.getFacesContext();
        String paginaAtual = contexto.getViewRoot().getViewId();
        boolean paginaLogin = (paginaAtual.lastIndexOf("/jsp/index.jsp") > -1);
        boolean paginaCadastroCamp = (paginaAtual.lastIndexOf("/jsp/cadastroCampeonato.jsp") > -1);
        Object campeonato = ((CampeonatoController) FacesUtils.getManagedBean("campeonatoController")).getCampeonato();
        NavigationHandler navegacao = contexto.getApplication().getNavigationHandler();
        if (paginaCadastroCamp) {
            navegacao.handleNavigation(contexto, null, "cadastroCampeonatoPage");
            return;
        }
        if (!paginaLogin && campeonato == null) {
            navegacao.handleNavigation(contexto, null, "login");
        }
    }

    public void afterPhase(PhaseEvent event) {
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
}
