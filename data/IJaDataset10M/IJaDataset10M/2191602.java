package com.marketfarm.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.marketfarm.client.footer.Ajuda;
import com.marketfarm.client.footer.Contato;
import com.marketfarm.client.footer.Sobre;
import com.marketfarm.client.service.AbstractAsynCallback;
import com.marketfarm.client.service.UsuarioService;
import com.marketfarm.client.service.UsuarioServiceAsync;

public class CachePagina {

    public static enum Pagina {

        LOGIN(Login.class, Boolean.FALSE), NOVO_USUARIO(NovoUsuario.class, Boolean.FALSE), AJUDA(Ajuda.class, Boolean.FALSE), SOBRE(Sobre.class, Boolean.FALSE), CONTATO(Contato.class, Boolean.FALSE), HOME(Home.class, Boolean.TRUE);

        private static Map<String, Pagina> items = new HashMap<String, CachePagina.Pagina>();

        private Class<? extends Widget> clazz;

        private Boolean loginRequerido;

        static {
            for (Pagina p : Pagina.values()) {
                items.put(p.toString(), p);
            }
        }

        private Pagina(Class<? extends Widget> clazz, Boolean loginRequerido) {
            this.clazz = clazz;
            this.loginRequerido = loginRequerido;
        }

        public Class<? extends Widget> getWidget() {
            return clazz;
        }

        public Boolean isLoginRequerido() {
            return loginRequerido;
        }

        public static Pagina get(String data) {
            return items.get(data);
        }
    }

    private Map<Pagina, Widget> paginas = new HashMap<Pagina, Widget>();

    private static CachePagina instance = new CachePagina();

    private CachePagina() {
    }

    public static CachePagina getInstance() {
        return instance;
    }

    public Widget getPagina(Pagina pagina) {
        return paginas.get(pagina);
    }

    public void updatePaginaAtual(Pagina pagina) {
        try {
            updatePaginaAtual(pagina, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void apagarPagina(Pagina pagina) {
        paginas.remove(pagina);
    }

    private void updatePaginaAtual(final Pagina pagina, Boolean sessaoVerificada) {
        try {
            if (pagina.isLoginRequerido() && !sessaoVerificada) {
                String sessao = Cookies.getCookie("sid");
                UsuarioServiceAsync service = GWT.create(UsuarioService.class);
                service.sessaoContinuaValida(sessao, new Date().getTime(), new AbstractAsynCallback<Boolean>() {

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) updatePaginaAtual(pagina, true); else History.newItem(Pagina.LOGIN.toString());
                    }
                });
                return;
            }
            if (paginas.get(pagina) == null) {
                paginas.put(pagina, getWidget(pagina));
            }
            limparPagina();
            RootPanel rootPanel = RootPanel.get("mainDiv");
            rootPanel.clear();
            rootPanel.add(paginas.get(pagina));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void limparPagina() {
        NodeList<Element> elementos = Document.get().getElementsByTagName("input");
        for (int i = 0; i < elementos.getLength(); i++) {
            elementos.getItem(i).setPropertyString("value", "");
        }
    }

    private Widget getWidget(Pagina pagina) {
        switch(pagina) {
            case LOGIN:
                return new Login();
            case NOVO_USUARIO:
                return new NovoUsuario();
            case AJUDA:
                return new Ajuda();
            case SOBRE:
                return new Sobre();
            case CONTATO:
                return new Contato();
            case HOME:
                return new Home();
        }
        return null;
    }
}
