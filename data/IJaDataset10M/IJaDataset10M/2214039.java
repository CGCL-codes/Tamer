package br.org.ged.direto.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "anotacoes")
public class Anotacao implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 3937434284783381510L;

    @Id
    @GeneratedValue
    @Column(name = "Id")
    private Integer idAnotacao;

    @Column(name = "Anotacao")
    private String anotacao;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "IdMensagem", nullable = false)
    private DocumentoDetalhes documentoDetalhes;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "IdUsuario", nullable = false)
    private Usuario usuario;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DataHora", nullable = true)
    private Date dataHoraAnotacao;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "IdCarteira", nullable = false)
    private Carteira carteira;

    public Carteira getCarteira() {
        return carteira;
    }

    public void setCarteira(Carteira carteira) {
        this.carteira = carteira;
    }

    public Integer getIdAnotacao() {
        return idAnotacao;
    }

    public void setIdAnotacao(Integer idAnotacao) {
        this.idAnotacao = idAnotacao;
    }

    public String getAnotacao() {
        return anotacao;
    }

    public void setAnotacao(String anotacao) {
        this.anotacao = anotacao;
    }

    public DocumentoDetalhes getDocumentoDetalhes() {
        return documentoDetalhes;
    }

    public void setDocumentoDetalhes(DocumentoDetalhes documentoDetalhes) {
        this.documentoDetalhes = documentoDetalhes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataHoraAnotacao() {
        return dataHoraAnotacao;
    }

    public void setDataHoraAnotacao(Date dataHoraAnotacao) {
        this.dataHoraAnotacao = dataHoraAnotacao;
    }
}
