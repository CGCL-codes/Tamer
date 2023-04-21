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
 * @author Marco Aurelio
 */
@NamedQueries(value = { @NamedQuery(name = "CentroCustoPorNome", query = "SELECT c FROM CentroDeCusto c WHERE upper(c.descricao) LIKE upper(?)"), @NamedQuery(name = "CentroCustoPorTipoSaida", query = "SELECT c FROM CentroDeCusto c WHERE c.tipoSaida=? AND c.status=true"), @NamedQuery(name = "CentroCustoPorTipoSaida2", query = "SELECT c FROM CentroDeCusto c WHERE c.tipoSaida=?") })
@Entity
@Table(name = "CENTRO_DE_CUSTO")
public class CentroDeCusto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String descricao;

    @JoinColumn(name = "TIPO_SAIDA", referencedColumnName = "id")
    @ManyToOne
    private TipoSaida tipoSaida;

    private boolean status = true;

    @JoinColumn(name = "USUARIOS", referencedColumnName = "id")
    @ManyToOne
    private Usuario usuarioCadastrante;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TipoSaida getTipoSaida() {
        return tipoSaida;
    }

    public void setTipoSaida(TipoSaida tipoSaida) {
        this.tipoSaida = tipoSaida;
    }

    public Usuario getUsuarioCadastrante() {
        return usuarioCadastrante;
    }

    public void setUsuarioCadastrante(Usuario usuarioCadastrante) {
        this.usuarioCadastrante = usuarioCadastrante;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof CentroDeCusto)) {
            return false;
        }
        CentroDeCusto other = (CentroDeCusto) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "asfabdesk.dominio.CentroDeCusto[id=" + id + "]";
    }
}
