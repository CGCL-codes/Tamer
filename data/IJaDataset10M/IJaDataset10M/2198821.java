package br.com.brasilsemchamas.entidade.relatorio;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "informacoes_adicionais")
@NamedQueries({ @NamedQuery(name = "InformacoesAdicionais.findAll", query = "SELECT i FROM InformacoesAdicionais i"), @NamedQuery(name = "InformacoesAdicionais.findByPkId", query = "SELECT i FROM InformacoesAdicionais i WHERE i.pkId = :pkId"), @NamedQuery(name = "InformacoesAdicionais.findByConsumoAguaLitros", query = "SELECT i FROM InformacoesAdicionais i WHERE i.consumoAguaLitros = :consumoAguaLitros"), @NamedQuery(name = "InformacoesAdicionais.findByConsumoAgenteHigoscopico", query = "SELECT i FROM InformacoesAdicionais i WHERE i.consumoAgenteHigoscopico = :consumoAgenteHigoscopico"), @NamedQuery(name = "InformacoesAdicionais.findByTipo", query = "SELECT i FROM InformacoesAdicionais i WHERE i.tipo = :tipo"), @NamedQuery(name = "InformacoesAdicionais.findByEstimativaPublico", query = "SELECT i FROM InformacoesAdicionais i WHERE i.estimativaPublico = :estimativaPublico"), @NamedQuery(name = "InformacoesAdicionais.findByDuracaoEvento", query = "SELECT i FROM InformacoesAdicionais i WHERE i.duracaoEvento = :duracaoEvento"), @NamedQuery(name = "InformacoesAdicionais.findByDuracaoDias", query = "SELECT i FROM InformacoesAdicionais i WHERE i.duracaoDias = :duracaoDias"), @NamedQuery(name = "InformacoesAdicionais.findByDuracaoHoras", query = "SELECT i FROM InformacoesAdicionais i WHERE i.duracaoHoras = :duracaoHoras"), @NamedQuery(name = "InformacoesAdicionais.findByConsumoSacos", query = "SELECT i FROM InformacoesAdicionais i WHERE i.consumoSacos = :consumoSacos"), @NamedQuery(name = "InformacoesAdicionais.findByConsumoLitros", query = "SELECT i FROM InformacoesAdicionais i WHERE i.consumoLitros = :consumoLitros"), @NamedQuery(name = "InformacoesAdicionais.findByOutras", query = "SELECT i FROM InformacoesAdicionais i WHERE i.outras = :outras"), @NamedQuery(name = "InformacoesAdicionais.findByDescricaoOutras", query = "SELECT i FROM InformacoesAdicionais i WHERE i.descricaoOutras = :descricaoOutras") })
public class InformacoesAdicionais implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "pk_id")
    private Integer pkId;

    @Column(name = "consumo_agua_litros")
    private Integer consumoAguaLitros;

    @Column(name = "consumo_agente_higoscopico")
    private Integer consumoAgenteHigoscopico;

    @Column(name = "tipo")
    private String tipo;

    @Column(name = "estimativa_publico")
    private Integer estimativaPublico;

    @Column(name = "duracao_evento")
    private Integer duracaoEvento;

    @Column(name = "duracao_dias")
    private Boolean duracaoDias;

    @Column(name = "duracao_horas")
    private Boolean duracaoHoras;

    @Column(name = "consumo_sacos")
    private Boolean consumoSacos;

    @Column(name = "consumo_litros")
    private Boolean consumoLitros;

    @Column(name = "outras")
    private Boolean outras;

    @Column(name = "descricao_outras")
    private String descricaoOutras;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "fKinformacoesadicionais")
    private Collection<AtividadeComunitaria> atividadeComunitariaCollection;

    public InformacoesAdicionais() {
    }

    public InformacoesAdicionais(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getPkId() {
        return pkId;
    }

    public void setPkId(Integer pkId) {
        this.pkId = pkId;
    }

    public Integer getConsumoAguaLitros() {
        return consumoAguaLitros;
    }

    public void setConsumoAguaLitros(Integer consumoAguaLitros) {
        this.consumoAguaLitros = consumoAguaLitros;
    }

    public Integer getConsumoAgenteHigoscopico() {
        return consumoAgenteHigoscopico;
    }

    public void setConsumoAgenteHigoscopico(Integer consumoAgenteHigoscopico) {
        this.consumoAgenteHigoscopico = consumoAgenteHigoscopico;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getEstimativaPublico() {
        return estimativaPublico;
    }

    public void setEstimativaPublico(Integer estimativaPublico) {
        this.estimativaPublico = estimativaPublico;
    }

    public Integer getDuracaoEvento() {
        return duracaoEvento;
    }

    public void setDuracaoEvento(Integer duracaoEvento) {
        this.duracaoEvento = duracaoEvento;
    }

    public Boolean getDuracaoDias() {
        return duracaoDias;
    }

    public void setDuracaoDias(Boolean duracaoDias) {
        this.duracaoDias = duracaoDias;
    }

    public Boolean getDuracaoHoras() {
        return duracaoHoras;
    }

    public void setDuracaoHoras(Boolean duracaoHoras) {
        this.duracaoHoras = duracaoHoras;
    }

    public Boolean getConsumoSacos() {
        return consumoSacos;
    }

    public void setConsumoSacos(Boolean consumoSacos) {
        this.consumoSacos = consumoSacos;
    }

    public Boolean getConsumoLitros() {
        return consumoLitros;
    }

    public void setConsumoLitros(Boolean consumoLitros) {
        this.consumoLitros = consumoLitros;
    }

    public Boolean getOutras() {
        return outras;
    }

    public void setOutras(Boolean outras) {
        this.outras = outras;
    }

    public String getDescricaoOutras() {
        return descricaoOutras;
    }

    public void setDescricaoOutras(String descricaoOutras) {
        this.descricaoOutras = descricaoOutras;
    }

    public Collection<AtividadeComunitaria> getAtividadeComunitariaCollection() {
        return atividadeComunitariaCollection;
    }

    public void setAtividadeComunitariaCollection(Collection<AtividadeComunitaria> atividadeComunitariaCollection) {
        this.atividadeComunitariaCollection = atividadeComunitariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (pkId != null ? pkId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof InformacoesAdicionais)) {
            return false;
        }
        InformacoesAdicionais other = (InformacoesAdicionais) object;
        if ((this.pkId == null && other.pkId != null) || (this.pkId != null && !this.pkId.equals(other.pkId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.InformacoesAdicionais[pkId=" + pkId + "]";
    }
}
