package asfabdesk.dominio;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author marco.machado
 */
@Entity
@NamedQueries(value = { @NamedQuery(name = "ItensPorEntrada", query = "SELECT i FROM ItemEntrada i WHERE entrada =? "), @NamedQuery(name = "ItensPorConvenioComp", query = "SELECT i FROM ItemEntrada i WHERE entrada.convenio =? AND entrada.competencia=? AND entrada.situacao=2 "), @NamedQuery(name = "ItensPorConvenioPeriodo", query = "SELECT i FROM ItemEntrada i WHERE entrada.convenio =? AND entrada.competencia BETWEEN (?) AND (?)  AND entrada.situacao=2 "), @NamedQuery(name = "ItensPorContribuicaoCont", query = "SELECT i FROM ItemEntrada i WHERE entrada.competencia=?  AND entrada.situacao=2 AND entrada.tipoEntrada.natureza= 0 ORDER BY i.associado.nome") })
@Table(name = "ITENS_ENTRADA")
public class ItemEntrada implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private double valorIten;

    @JoinColumn(name = "ASSOCIADO", referencedColumnName = "id")
    @ManyToOne
    private Associado associado;

    @JoinColumn(name = "DEPENDENTE", referencedColumnName = "id")
    @ManyToOne
    private Dependente dependente;

    @JoinColumn(name = "ENTRADA", referencedColumnName = "id")
    @ManyToOne
    private EntradaFinanceira entrada;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Associado getAssociado() {
        return associado;
    }

    public void setAssociado(Associado associado) {
        this.associado = associado;
    }

    public Dependente getDependente() {
        return dependente;
    }

    public void setDependente(Dependente dependente) {
        this.dependente = dependente;
    }

    public double getValorIten() {
        return valorIten;
    }

    public void setValorIten(double valorIten) {
        this.valorIten = valorIten;
    }

    public EntradaFinanceira getEntrada() {
        return entrada;
    }

    public void setEntrada(EntradaFinanceira entrada) {
        this.entrada = entrada;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemEntrada)) {
            return false;
        }
        ItemEntrada other = (ItemEntrada) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "asfabdesk.dominio.ItenEntrada[id=" + id + "]";
    }
}
