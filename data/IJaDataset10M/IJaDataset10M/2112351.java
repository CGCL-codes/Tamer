package projeto_web.model;

import java.util.Collection;
import java.util.Date;
import javax.persistence.*;
import java.io.Serializable;

/**
 * Entity implementation class for Entity: Sala
 *
 */
@Entity
@Table(name = "sala")
public class Sala implements Serializable {

    @Id
    private String titulo;

    private Boolean salaAberta;

    private String descricao;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date dataCriacao;

    private Integer ipCamera;

    @OneToMany()
    @JoinColumn(name = "sala_titulo")
    private Collection<Mensagem> mensagens;

    @OneToMany
    @JoinColumn(name = "sala_titulo")
    private Collection<SituacaoUsuarioSala> situacoes;

    public Sala() {
        super();
    }

    public String getTitulo() {
        return this.titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Boolean getSalaAberta() {
        return this.salaAberta;
    }

    public void setSalaAberta(Boolean salaAberta) {
        this.salaAberta = salaAberta;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDataCriacao() {
        return this.dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Integer getIpCamera() {
        return this.ipCamera;
    }

    public void setIpCamera(Integer ipCamera) {
        this.ipCamera = ipCamera;
    }

    public void setMensagens(Collection<Mensagem> mensagens) {
        this.mensagens = mensagens;
    }

    public Collection<Mensagem> getMensagens() {
        return mensagens;
    }

    public void setSituacoes(Collection<SituacaoUsuarioSala> situacoes) {
        this.situacoes = situacoes;
    }

    public Collection<SituacaoUsuarioSala> getSituacoes() {
        return situacoes;
    }

    private static final long serialVersionUID = 1L;
}
