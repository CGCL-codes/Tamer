package br.org.acessobrasil.portal.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.servlet.http.HttpSession;
import br.org.acessobrasil.portal.util.LoginInterceptor;

/**
 * Representa um usu&aacute;rio do sistema de gerenciamento na parte
 * administrativa
 * 
 * @author Fabio Issamu Oshiro
 * 
 */
@Entity
@Table(name = "sgctb_usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = -1973681179987982610L;

    @Id
    @GeneratedValue
    private Long nu_usuario;

    private String no_usuario;

    private String co_matricula;

    private String hash;

    private Long nu_tentativa;

    private String identificacao;

    @Column(unique = true)
    private String co_usuario;

    private String no_email;

    private String nu_telefone;

    private String co_senha;

    private boolean co_ativo;

    private Date dt_criacao;

    private Date dt_alteracao;

    @Column(name = "dt_espera")
    private Date dtEspera;

    @Column(name = "nu_hierarquia")
    private int nuHierarquia;

    @ManyToMany
    private List<Sitio> listSitio;

    @Transient
    private List<Privilegio> listPrivilegio;

    @Transient
    private Setor setor;

    @Transient
    private Perfil perfil;

    @Transient
    private List<String> actionMessages = new ArrayList<String>();

    @Transient
    private List<String> errorMessages = new ArrayList<String>();

    @Transient
    private boolean modoDesenvolvimento = false;

    @Transient
    private transient HttpSession session;

    public Long getNuUsuario() {
        return nu_usuario;
    }

    public void setNuUsuario(Long nu_usuario) {
        this.nu_usuario = nu_usuario;
    }

    public String getNoUsuario() {
        return no_usuario;
    }

    public void setNoUsuario(String no_usuario) {
        this.no_usuario = no_usuario;
    }

    public String getCoMatricula() {
        return co_matricula;
    }

    public void setCoMatricula(String co_matricula) {
        this.co_matricula = co_matricula;
    }

    public String getCoUsuario() {
        return co_usuario;
    }

    public void setCoUsuario(String co_usuario) {
        this.co_usuario = co_usuario;
    }

    public String getNoEmail() {
        return no_email;
    }

    public void setNoEmail(String no_email) {
        this.no_email = no_email;
    }

    public String getNuTelefone() {
        return nu_telefone;
    }

    public void setNuTelefone(String nu_telefone) {
        this.nu_telefone = nu_telefone;
    }

    public String getCoSenha() {
        return co_senha;
    }

    public void setCoSenha(String co_senha) {
        this.co_senha = co_senha;
    }

    public boolean isCoAtivo() {
        return co_ativo;
    }

    public void setCoAtivo(boolean co_ativo) {
        this.co_ativo = co_ativo;
    }

    public Date getDtCriacao() {
        return dt_criacao;
    }

    public void setDtCriacao(Date dt_criacao) {
        this.dt_criacao = dt_criacao;
    }

    public Date getDtAlteracao() {
        return dt_alteracao;
    }

    public void setDtAlteracao(Date dt_alteracao) {
        this.dt_alteracao = dt_alteracao;
    }

    public Setor getSetor() {
        return setor;
    }

    public void setSetor(Setor setor) {
        this.setor = setor;
    }

    /**
	 * Nao vem do banco
	 * 
	 * @return perfil
	 */
    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<Sitio> getListSitio() {
        return listSitio;
    }

    public void setListSitio(List<Sitio> listSitio) {
        this.listSitio = listSitio;
    }

    public List<Privilegio> getListPrivilegio() {
        return listPrivilegio;
    }

    public void setListPrivilegio(List<Privilegio> listPrivilegio) {
        this.listPrivilegio = listPrivilegio;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Usuario)) {
            return false;
        }
        Usuario u = (Usuario) o;
        if (this.getNuUsuario().equals(u.getNuUsuario())) {
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        return "\ngetNuUsuario=" + this.nu_usuario + "\ngetCoSenha=" + this.getCoSenha() + "\ngetDtAlteracao=" + this.getDtAlteracao() + "\ngetCoMatricula=" + this.getCoMatricula() + "\ngetCoUsuario=" + this.getCoUsuario() + "\ngetNoEmail=" + this.getNoEmail() + "\ngetNuTelefone=" + this.getNuTelefone() + "\ngetDtCriacao=" + this.getDtCriacao();
    }

    public int getNuHierarquia() {
        return nuHierarquia;
    }

    public void setNuHierarquia(int nuHierarquia) {
        this.nuHierarquia = nuHierarquia;
    }

    public void setModoDesenvolvimento(boolean modoDesenvolvimento) {
        this.modoDesenvolvimento = modoDesenvolvimento;
    }

    public boolean isModoDesenvolvimento() {
        return modoDesenvolvimento;
    }

    /**
	 * Atualiza na sessao, se existir.
	 * Atualize a sessao na Action, depois de chamar o facade
	 * @param string mensagem
	 */
    public void addActionError(String string) {
        errorMessages.add(string);
        if (session != null) session.setAttribute(LoginInterceptor.USER_HANDLE, this);
    }

    /**
	 * Atualiza na sessao, se houver.
	 * Atualize a sessao na Action, depois de chamar o facade
	 * @param string mensagem
	 */
    public void addActionMessage(String string) {
        actionMessages.add(string);
        if (session != null) session.setAttribute(LoginInterceptor.USER_HANDLE, this);
    }

    /**
	 * Mostra as mensagens de erro em HTML.
	 * A lista de erros sera limpa ao final do metodo
	 * @return lista de erros
	 */
    public String getHtmlErrorMessages() {
        StringBuilder retorno = new StringBuilder();
        if (errorMessages.size() > 0) {
            retorno.append("<ul>");
            for (String msg : errorMessages) {
                retorno.append("<li>").append(msg).append("</li>");
            }
            retorno.append("</ul>");
            errorMessages.clear();
        }
        return retorno.toString();
    }

    /**
	 * Mostra as mensagens em HTML.
	 * A lista de mensagens sera limpa ao final do metodo
	 * @return lista de mensagens
	 */
    public String getHtmlActionMessages() {
        StringBuilder retorno = new StringBuilder();
        if (actionMessages.size() > 0) {
            retorno.append("<ul>");
            for (String msg : actionMessages) {
                retorno.append("<li>").append(msg).append("</li>");
            }
            retorno.append("</ul>");
            actionMessages.clear();
        }
        return retorno.toString();
    }

    public void setSession(HttpSession session) {
        this.session = session;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public void setIdentificacao(String identificacao) {
        this.identificacao = identificacao;
    }

    public Long getNuTentativa() {
        return nu_tentativa;
    }

    public void setNuTentativa(Long nuTentativa) {
        this.nu_tentativa = nuTentativa;
    }

    /**
	 * 
	 * @return Data para esperar at� poder logar.
	 */
    public Date getDtEspera() {
        return dtEspera;
    }

    /**
	 * Data para esperar at� poder logar.
	 * @param dtEspera
	 */
    public void setDtEspera(Date dtEspera) {
        this.dtEspera = dtEspera;
    }
}
