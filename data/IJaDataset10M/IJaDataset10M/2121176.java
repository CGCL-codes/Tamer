package com.dashboard;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import com.dashboard.domain.Autorizacao;
import com.dashboard.domain.Autorizacoes;
import com.dashboard.domain.Organizacao;
import com.dashboard.domain.Usuario;
import com.dashboard.service.ServicoOrganizacao;
import com.dashboard.service.ServicoPapel;
import com.dashboard.service.ServicoUsuario;

/**
 * @author Joao
 * 
 */
@Component("dashboardUserBean")
@Controller
@Scope("request")
public class DashboardUserBean implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = -2668441801894569091L;

    private ServicoUsuario servicoUsuario;

    private ServicoOrganizacao servicoOrganizacao;

    private ServicoPapel servicoPapel;

    private Usuario usuario = new Usuario();

    private List<Usuario> usuarios;

    private String papel;

    private List<SelectItem> papeis;

    private String organizacao;

    private List<SelectItem> organizacoes;

    /**
	 * Construtor com os servi&ccedil;os necess&aacute;rios para inje&ccedil;&atilde;o.
	 * 
	 * @param servicoUsuario servi&ccedil;o de usu&aacute;rios
	 * @param servicoOrganizacao servi&ccedil;o de organiza&ccedil;&atilde;o
	 * @param servicoPapel servi&ccedil;o de papel
	 */
    @Autowired
    public DashboardUserBean(ServicoUsuario servicoUsuario, ServicoOrganizacao servicoOrganizacao, ServicoPapel servicoPapel) {
        this.servicoUsuario = servicoUsuario;
        this.servicoOrganizacao = servicoOrganizacao;
        this.servicoPapel = servicoPapel;
    }

    /**
	 * M&eacute;todo get de User.
	 * 
	 * @return o usu&aacute;rio.
	 */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
	 * M&eacute;todo set de User.
	 * 
	 * @param usuario o usu&aacute;rio a ser setado.
	 */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
	 * M&eacute;todo get de Users.
	 * 
	 * @return os usu&aacute;rios.
	 */
    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    /**
	 * M&eacute;todo set de lista de User.
	 * 
	 * @param usuarios a lista de usu&aacute;rios a ser setado.
	 */
    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    /**
	 * M&eacute;todo get de Role.
	 * 
	 * @return o papel.
	 */
    public String getPapel() {
        return papel;
    }

    /**
	 * M&eacute;todo set de Role.
	 * 
	 * @param papel o papel a ser setado.
	 */
    public void setPapel(String papel) {
        this.papel = papel;
    }

    /**
	 * M&eacute;todo get de Roles.
	 * 
	 * @return os pap&eacuteis do usu&aacute;rio.
	 */
    public List<SelectItem> getPapeis() {
        if (papeis == null) {
            papeis = new ArrayList<SelectItem>();
            for (Autorizacao autorizacao : servicoPapel.listarTodos()) {
                papeis.add(new SelectItem(autorizacao.getChave(), autorizacao.getNome()));
            }
        }
        return papeis;
    }

    /**
	 * M&eacute;todo set de lista de Role.
	 * 
	 * @param roles a lista de  pap&eacute;is a ser setado.
	 */
    public void setPapeis(List<SelectItem> roles) {
        this.papeis = roles;
    }

    /**
	 * M&eacute;todo get de Organization.
	 * 
	 * @return a organiza&ccedil;&atilde;o selecionada.
	 */
    public String getOrganizacao() {
        return organizacao;
    }

    /**
	 * M&eacute;todo set de Organization.
	 * 
	 * @param organizacao a organiza&ccedil;&atilde;o a ser setado.
	 */
    public void setOrganizacao(String organizacao) {
        this.organizacao = organizacao;
    }

    /**
	 * M&eacute;todo get de lista de Organization.
	 * 
	 * @return a lista de organiza&ccedil;&otilde;es.
	 */
    public List<SelectItem> getOrganizacoes() {
        if (organizacoes == null) {
            organizacoes = new ArrayList<SelectItem>();
            for (Organizacao organizacaoLocal : servicoOrganizacao.listarTodos()) {
                this.organizacoes.add(new SelectItem(organizacaoLocal.getId(), organizacaoLocal.getNome()));
            }
        }
        return organizacoes;
    }

    /**
	 * M&eacute;todo set de lista de Organization.
	 * 
	 * @param organizacoes a lista de organiza&ccedil;&otilde;es a ser setado.
	 */
    public void setOrganizacoes(List<SelectItem> organizacoes) {
        this.organizacoes = organizacoes;
    }

    public String prepararSalvar() {
        usuario = new Usuario();
        return "admin/createUser";
    }

    /**
	 * Salva um usu&aacute;rio.
	 * 
	 * @return o resultado da opera&ccedil;&atilde;o.
	 */
    public String salvar() {
        usuario.setOrganizacao(servicoOrganizacao.recuperar(Long.valueOf(organizacao)));
        if (usuario.getAutorizacoes() == null) {
            usuario.setAutorizacoes(new HashSet<Autorizacoes>());
        }
        Autorizacao authority = servicoPapel.recuperar(papel);
        Autorizacoes authorities = new Autorizacoes();
        authorities.setAutorizacao(authority.getChave());
        authorities.setAutorizacaoReferencia(authority);
        authorities.setLogin(usuario.getLogin());
        authorities.setUsuario(usuario);
        usuario.getAutorizacoes().add(authorities);
        usuario.setAtivo(false);
        servicoUsuario.criar(usuario);
        FacesMessage facesMessage = new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuário salvo com sucesso!", "OK");
        FacesContext.getCurrentInstance().addMessage(null, facesMessage);
        return "admin/listUsers";
    }

    /**
	 * Lista todos os usu&atilde;rios do sistema.
	 * 
	 * @return a lista de usu&aacute;rios do sistema.
	 */
    public List<Usuario> listarTodos() {
        return servicoUsuario.listarTodos();
    }
}
