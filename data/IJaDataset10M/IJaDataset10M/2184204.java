package br.com.progepe.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import br.com.progepe.constantes.Constantes;
import br.com.progepe.dao.AutenticacaoDAO;
import br.com.progepe.encripty.Encripty;
import br.com.progepe.entity.Autenticacao;

public class AutenticacaoController {

    private Autenticacao autenticacao;

    private String novaSenha;

    private String confirmarSenha;

    private Autenticacao siapeAutenticado;

    public Autenticacao getAutenticacao() {
        return autenticacao;
    }

    public void setAutenticacao(Autenticacao autenticacao) {
        this.autenticacao = autenticacao;
    }

    public String getNovaSenha() {
        return novaSenha;
    }

    public void setNovaSenha(String novaSenha) {
        this.novaSenha = novaSenha;
    }

    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public Autenticacao getSiapeAutenticado() {
        return siapeAutenticado;
    }

    public void setSiapeAutenticado(Autenticacao siapeAutenticado) {
        this.siapeAutenticado = siapeAutenticado;
    }

    public AutenticacaoController() {
        if (this.autenticacao == null) {
            this.autenticacao = new Autenticacao();
        }
    }

    public void login() throws Exception {
        siapeAutenticado = new Autenticacao();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        siapeAutenticado = AutenticacaoDAO.getInstance().autentica(autenticacao);
        if (siapeAutenticado != null) {
            session.setAttribute("usuarioLogado", siapeAutenticado);
            FacesContext.getCurrentInstance().getExternalContext().redirect("menus.jsp");
            autenticacao = new Autenticacao();
            session.setMaxInactiveInterval(Constantes.TEMPO_DA_SESSAO);
        } else {
            session.setAttribute("usuarioLogado", null);
            session.removeAttribute("user");
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Siape ou Senha inv�lida!", "Siape ou Senha inv�lida!");
            FacesContext.getCurrentInstance().addMessage("", message);
        }
    }

    public void alterarSenha() throws IOException {
        autenticacao = new Autenticacao();
        FacesContext.getCurrentInstance().getExternalContext().redirect("alterarSenha.jsp");
    }

    public void verificarSenha() throws NoSuchAlgorithmException {
        siapeAutenticado = (Autenticacao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        String senhaAtual = Encripty.criptografaSenha(autenticacao.getSenha());
        if (!siapeAutenticado.getSenha().equals(senhaAtual)) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Senha Atual incorreta!", "Senha Atual incorreta!");
            FacesContext.getCurrentInstance().addMessage("", message);
        } else if (!this.getNovaSenha().equals(this.getConfirmarSenha())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "A Nova Senha n�o corresponde � Senha de confirma��o!", "A Nova Senha n�o corresponde � Senha de confirma��o!");
            FacesContext.getCurrentInstance().addMessage("", message);
        } else {
            String novaSenha = Encripty.criptografaSenha(this.getNovaSenha());
            siapeAutenticado.setSenha(novaSenha);
            AutenticacaoDAO.getInstance().saveOrUpdate(siapeAutenticado);
        }
    }

    public void isAutenticado() throws IOException {
        if (siapeAutenticado == null) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsp");
        }
    }

    public void logout() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(false);
        session.invalidate();
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("login.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
