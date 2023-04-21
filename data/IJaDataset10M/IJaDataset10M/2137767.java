package asfabdesk.dominio;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
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
@Table(name = "observacoes")
@NamedQueries(value = { @NamedQuery(name = "ObservacoesPorAssociado", query = "SELECT o FROM Observacao o WHERE o.associado= ? AND o.deletado=false"), @NamedQuery(name = "ObservacoesPorDependente", query = "SELECT o FROM Observacao o WHERE o.dependente= ? AND o.deletado=false") })
public class Observacao implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date dataRegistro;

    private String textoObs;

    private String tipoObs;

    private boolean deletado = false;

    @ManyToOne
    private Associado associado;

    @JoinColumn(name = "DEPENDENTE", referencedColumnName = "id")
    @ManyToOne
    private Dependente dependente;

    @JoinColumn(name = "EMPRESAS_CONVENIADAS", referencedColumnName = "id")
    @ManyToOne
    private EmpresaConveniada empresa;

    @JoinColumn(name = "USUARIOS", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioCadastrante;

    public Observacao() {
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public EmpresaConveniada getEmpresa() {
        return empresa;
    }

    public void setEmpresa(EmpresaConveniada empresa) {
        this.empresa = empresa;
    }

    public String getTipoObs() {
        return tipoObs;
    }

    public void setTipoObs(String tipoObs) {
        this.tipoObs = tipoObs;
    }

    public Associado getAssociado() {
        return associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

    public Date getDataRegistro() {
        return dataRegistro;
    }

    public void setDataRegistro(Date dataRegistro) {
        this.dataRegistro = dataRegistro;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTextoObs() {
        return textoObs;
    }

    public void setTextoObs(String textoObs) {
        this.textoObs = textoObs;
    }

    public Usuario getUsuarioCadastrante() {
        return usuarioCadastrante;
    }

    public void setUsuarioCadastrante(Usuario usuarioCadastrante) {
        this.usuarioCadastrante = usuarioCadastrante;
    }

    public boolean isDeletado() {
        return deletado;
    }

    public void setDeletado(boolean deletado) {
        this.deletado = deletado;
    }
}
