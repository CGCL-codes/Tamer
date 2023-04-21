package br.unb.cic.gerval.client;

import br.unb.cic.gerval.client.componentes.PanelMenu;
import br.unb.cic.gerval.client.componentes.TelaComAguarde;
import br.unb.cic.gerval.client.produto.TelaLinhaProduto;
import br.unb.cic.gerval.client.produto.TelaProduto;
import br.unb.cic.gerval.client.rpc.vo.Teste;
import br.unb.cic.gerval.client.rpc.vo.Usuario;
import br.unb.cic.gerval.client.teste.TelaAgenda;
import br.unb.cic.gerval.client.teste.TelaSolicitarNovoTeste;
import br.unb.cic.gerval.client.teste.alocar.TelaAlocarTecnico;
import br.unb.cic.gerval.client.teste.rvp.TelaRVP;
import br.unb.cic.gerval.client.usuario.TelaUsuario;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.HistoryListener;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point.
 */
public class Gerval extends TelaComAguarde implements EntryPoint, LoginLogoffListener, HistoryListener {

    private TelaUsuario usuario = null;

    private TelaLinhaProduto linha = null;

    private TelaProduto produto = null;

    private TelaAgenda agenda = null;

    private TelaLogin login = null;

    private TelaAlocarTecnico telaAlocarTecnico = null;

    private TelaSolicitarNovoTeste telaSolicitarTeste = null;

    private TelaRVP telaRVP = null;

    private PanelMenu cabecalho = null;

    /**
	 * This is the entry point method.
	 */
    public void onModuleLoad() {
        PopUpAguarde.inicia();
        ControladorSessao.carregarUsuarioSessao();
        ControladorSessao.setListener(this);
        History.addHistoryListener(this);
        String token = History.getToken();
        cabecalho = cabecalho == null ? new PanelMenu() : cabecalho;
        Usuario usuarioLogado = Sessao.getUsuarioLogado();
        String cookie = Cookies.getCookie("abcde");
        if (usuarioLogado == null && cookie == null) {
            token = History.getToken();
            if (token.equals("login")) History.onHistoryChanged(token); else History.newItem("login");
        } else {
            RootPanel.get("menuPrincipal").clear();
            RootPanel.get("menuPrincipal").add(cabecalho);
            onHistoryChanged(token);
        }
    }

    /**
	 * Notificação para quando a sessão de usuário for carregada.
	 */
    public void avisarLogin() {
        Usuario u = Sessao.getUsuarioLogado();
        if (u == null) {
            RootPanel.get("menuPrincipal").clear();
            RootPanel.get("centro").clear();
            RootPanel.get("centro").add(getPainelErroAbrirSessao());
        } else {
            Cookies.setCookie("abcde", u.getId() + "");
            RootPanel.get("centro").clear();
            RootPanel.get("menuPrincipal").clear();
            RootPanel.get("menuPrincipal").add(cabecalho);
            cabecalho.setLabelUsuarioLogado(Sessao.getUsuarioLogado().getNome());
            String token = "agenda";
            History.newItem(token);
        }
        PopUpAguarde.termina();
    }

    public void avisarLogoff() {
    }

    private Widget getPainelErroAbrirSessao() {
        return new Label("ERRO AO ABRIR A SESSÃO");
    }

    public void onHistoryChanged(String tela) {
        GWT.log("-> " + tela, null);
        Usuario u = Sessao.getUsuarioLogado();
        String cookie = Cookies.getCookie("abcde");
        if (u == null && cookie == null) {
            if (!History.getToken().equals("login")) {
                History.newItem("login");
                return;
            }
        } else {
            if (u == null) ControladorSessao.carregarUsuarioSessao();
            if (tela.equals("login")) History.back();
            if (u == null && tela.trim().length() == 0) History.newItem("login");
        }
        if (tela.equals("login")) {
            RootPanel.get("centro").clear();
            login = login == null ? new TelaLogin() : login;
            RootPanel.get("centro").add(login);
            login.start();
        } else if (tela.equals("agenda")) {
            RootPanel.get("centro").clear();
            agenda = agenda == null ? new TelaAgenda() : agenda;
            RootPanel.get("centro").add(agenda);
            agenda.start();
        } else if (tela.equals("linha")) {
            RootPanel.get("centro").clear();
            linha = linha == null ? new TelaLinhaProduto(agenda.getModelo()) : linha;
            RootPanel.get("centro").add(linha);
            linha.start();
        } else if (tela.equals("produto")) {
            RootPanel.get("centro").clear();
            produto = produto == null ? new TelaProduto() : produto;
            RootPanel.get("centro").add(produto);
            produto.start();
        } else if (tela.equals("usuario")) {
            RootPanel.get("centro").clear();
            usuario = usuario == null ? new TelaUsuario() : usuario;
            RootPanel.get("centro").add(usuario);
            usuario.start();
        } else if (tela.equals("solicitar")) {
            RootPanel.get("centro").clear();
            telaSolicitarTeste = telaSolicitarTeste == null ? new TelaSolicitarNovoTeste() : telaSolicitarTeste;
            RootPanel.get("centro").add(telaSolicitarTeste);
            telaSolicitarTeste.startSolicitaValidacao();
        } else {
            agenda = agenda == null ? new TelaAgenda() : agenda;
            Teste ultimoTesteEscolhido = agenda.getUltimoTesteEscolhido();
            if (tela.equals("alocar")) {
                RootPanel.get("centro").clear();
                telaAlocarTecnico = telaAlocarTecnico == null ? new TelaAlocarTecnico() : telaAlocarTecnico;
                RootPanel.get("centro").add(telaAlocarTecnico);
                telaAlocarTecnico.inicializaTela(ultimoTesteEscolhido);
            } else if (tela.equals("rvp")) {
                RootPanel.get("centro").clear();
                telaRVP = telaRVP == null ? new TelaRVP() : telaRVP;
                RootPanel.get("centro").add(telaRVP);
                telaRVP.start();
            } else {
                History.newItem("agenda");
            }
        }
    }
}
