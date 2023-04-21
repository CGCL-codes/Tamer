package asfabdesk.dominio;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author marco.machado
 */
@Entity
@Table(name = "CONVENIO_POR_ASSOCIADO")
@NamedQueries(value = { @NamedQuery(name = "ConvenioPorAssociadoDuplicado", query = "SELECT c FROM ConvenioPorAssociado c WHERE c.convenio=? AND c.associado=? "), @NamedQuery(name = "ConveniosPorAssociado", query = "SELECT c FROM ConvenioPorAssociado c WHERE c.associado=? "), @NamedQuery(name = "ConveniosPorAssociados", query = "SELECT c FROM ConvenioPorAssociado c WHERE c.convenio=? AND c.dataRegistro <=? ") })
public class ConvenioPorAssociado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRegistro;

    private boolean status = true;

    private boolean deletado = false;

    @JoinColumn(name = "CONVENIO", referencedColumnName = "id")
    @ManyToOne
    private Convenio convenio;

    @JoinColumn(name = "ASSOCIADO", referencedColumnName = "id")
    @ManyToOne
    private Associado associado;

    @JoinColumn(name = "USUARIO", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioCadastrante;

    public ConvenioPorAssociado() {
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Associado getAssociado() {
        return associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Usuario getUsuarioCadastrante() {
        return usuarioCadastrante;
    }

    public void setUsuarioCadastrante(Usuario usuarioCadastrante) {
        this.usuarioCadastrante = usuarioCadastrante;
    }
}
