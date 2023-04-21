package br.com.brasilsemchamas.entidade.relatorio;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "relatorioincendio")
@NamedQueries({ @NamedQuery(name = "Relatorioincendio.findAll", query = "SELECT r FROM Relatorioincendio r"), @NamedQuery(name = "Relatorioincendio.findByIdRelatorioIncendio", query = "SELECT r FROM Relatorioincendio r WHERE r.idRelatorioIncendio = :idRelatorioIncendio"), @NamedQuery(name = "Relatorioincendio.findByToExtincao", query = "SELECT r FROM Relatorioincendio r WHERE r.toExtincao = :toExtincao"), @NamedQuery(name = "Relatorioincendio.findByToRescaldo", query = "SELECT r FROM Relatorioincendio r WHERE r.toRescaldo = :toRescaldo"), @NamedQuery(name = "Relatorioincendio.findByCaeAgua", query = "SELECT r FROM Relatorioincendio r WHERE r.caeAgua = :caeAgua"), @NamedQuery(name = "Relatorioincendio.findByCaeLgeEfe", query = "SELECT r FROM Relatorioincendio r WHERE r.caeLgeEfe = :caeLgeEfe"), @NamedQuery(name = "Relatorioincendio.findByEdiAreaPresumidaOriInc", query = "SELECT r FROM Relatorioincendio r WHERE r.ediAreaPresumidaOriInc = :ediAreaPresumidaOriInc"), @NamedQuery(name = "Relatorioincendio.findByEdiPavimentosAtingidosDo", query = "SELECT r FROM Relatorioincendio r WHERE r.ediPavimentosAtingidosDo = :ediPavimentosAtingidosDo"), @NamedQuery(name = "Relatorioincendio.findByEdiPavimentosAtingidosAo", query = "SELECT r FROM Relatorioincendio r WHERE r.ediPavimentosAtingidosAo = :ediPavimentosAtingidosAo"), @NamedQuery(name = "Relatorioincendio.findByEdiAreaAtingida", query = "SELECT r FROM Relatorioincendio r WHERE r.ediAreaAtingida = :ediAreaAtingida"), @NamedQuery(name = "Relatorioincendio.findByEdiAreaTotal", query = "SELECT r FROM Relatorioincendio r WHERE r.ediAreaTotal = :ediAreaTotal"), @NamedQuery(name = "Relatorioincendio.findByVegApa", query = "SELECT r FROM Relatorioincendio r WHERE r.vegApa = :vegApa"), @NamedQuery(name = "Relatorioincendio.findByVegAreasAtingida", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasAtingida = :vegAreasAtingida"), @NamedQuery(name = "Relatorioincendio.findByVegAreasAtingidaUrbana", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasAtingidaUrbana = :vegAreasAtingidaUrbana"), @NamedQuery(name = "Relatorioincendio.findByVegAreasAtingidaRural", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasAtingidaRural = :vegAreasAtingidaRural"), @NamedQuery(name = "Relatorioincendio.findByVegAreasNaoAtingida", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasNaoAtingida = :vegAreasNaoAtingida"), @NamedQuery(name = "Relatorioincendio.findByVegAreasNaoAtingidaUrbana", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasNaoAtingidaUrbana = :vegAreasNaoAtingidaUrbana"), @NamedQuery(name = "Relatorioincendio.findByVegAreasNaoAtingidaRural", query = "SELECT r FROM Relatorioincendio r WHERE r.vegAreasNaoAtingidaRural = :vegAreasNaoAtingidaRural"), @NamedQuery(name = "Relatorioincendio.findByBensMoveisEImoveisAtingidos", query = "SELECT r FROM Relatorioincendio r WHERE r.bensMoveisEImoveisAtingidos = :bensMoveisEImoveisAtingidos"), @NamedQuery(name = "Relatorioincendio.findByBensRecolhidos", query = "SELECT r FROM Relatorioincendio r WHERE r.bensRecolhidos = :bensRecolhidos"), @NamedQuery(name = "Relatorioincendio.findByBensRecolhidosA", query = "SELECT r FROM Relatorioincendio r WHERE r.bensRecolhidosA = :bensRecolhidosA"), @NamedQuery(name = "Relatorioincendio.findByBensRecolhidosCautela", query = "SELECT r FROM Relatorioincendio r WHERE r.bensRecolhidosCautela = :bensRecolhidosCautela"), @NamedQuery(name = "Relatorioincendio.findByPkidFK", query = "SELECT r FROM Relatorioincendio r WHERE r.pkidFK = :pkidFK") })
public class Relatorioincendio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_relatorio_incendio")
    private Integer idRelatorioIncendio;

    @Column(name = "to_extincao")
    @Temporal(TemporalType.TIME)
    private Date toExtincao;

    @Column(name = "to_rescaldo")
    @Temporal(TemporalType.TIME)
    private Date toRescaldo;

    @Column(name = "cae_agua")
    private Double caeAgua;

    @Column(name = "cae_lge_efe")
    private Float caeLgeEfe;

    @Column(name = "edi_area_presumida_ori_inc")
    private String ediAreaPresumidaOriInc;

    @Lob
    @Column(name = "edi_classes_prodominante")
    private byte[] ediClassesProdominante;

    @Column(name = "edi_pavimentos_atingidos_do")
    private Character ediPavimentosAtingidosDo;

    @Column(name = "edi_pavimentos_atingidos_ao")
    private String ediPavimentosAtingidosAo;

    @Column(name = "edi_area_atingida")
    private Double ediAreaAtingida;

    @Column(name = "edi_area_total")
    private Double ediAreaTotal;

    @Column(name = "veg_apa")
    private Boolean vegApa;

    @Column(name = "veg_areas_atingida")
    private Float vegAreasAtingida;

    @Column(name = "veg_areas_atingida_urbana")
    private Boolean vegAreasAtingidaUrbana;

    @Column(name = "veg_areas_atingida_rural")
    private Boolean vegAreasAtingidaRural;

    @Column(name = "veg_areas_nao_atingida")
    private Float vegAreasNaoAtingida;

    @Column(name = "veg_areas_nao_atingida_urbana")
    private Boolean vegAreasNaoAtingidaUrbana;

    @Column(name = "veg_areas_nao_atingida_rural")
    private Boolean vegAreasNaoAtingidaRural;

    @Column(name = "bens_moveis_e_imoveis_atingidos")
    private String bensMoveisEImoveisAtingidos;

    @Column(name = "bens_recolhidos")
    private String bensRecolhidos;

    @Column(name = "bens_recolhidos_a")
    private String bensRecolhidosA;

    @Column(name = "bens_recolhidos_cautela")
    private Integer bensRecolhidosCautela;

    @Basic(optional = false)
    @Column(name = "pk_id_FK")
    private int pkidFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<AcoesRealizadasBuscaesalvamento> acoesRealizadasBuscaesalvamentoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<AmbienteDeTrabalho> ambienteDeTrabalhoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<Subgrupo> subgrupoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<MilitarOcorrencia> militarOcorrenciaCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<RecursosHidricos> recursosHidricosCollection;

    @JoinColumn(name = "id_aviso_FK", referencedColumnName = "id_aviso")
    @ManyToOne(optional = false)
    private Aviso idavisoFK;

    @JoinColumn(name = "id_bombeiro_FK", referencedColumnName = "id_bombeiro")
    @ManyToOne(optional = false)
    private Bombeiro idbombeiroFK;

    @JoinColumn(name = "id_codigodolocal_FK", referencedColumnName = "id_codigodolocal")
    @ManyToOne(optional = false)
    private Codigodolocal idcodigodolocalFK;

    @JoinColumn(name = "identificacao_FK", referencedColumnName = "pk_id")
    @ManyToOne(optional = false)
    private RelatorioOcorrencia identificacaoFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<TipoRevestimentoEstutural> tipoRevestimentoEstuturalCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idrelatorioincendioFK")
    private Collection<PreventivosExistentes> preventivosExistentesCollection;

    public Relatorioincendio() {
    }

    public Relatorioincendio(Integer idRelatorioIncendio) {
        this.idRelatorioIncendio = idRelatorioIncendio;
    }

    public Relatorioincendio(Integer idRelatorioIncendio, int pkidFK) {
        this.idRelatorioIncendio = idRelatorioIncendio;
        this.pkidFK = pkidFK;
    }

    public Integer getIdRelatorioIncendio() {
        return idRelatorioIncendio;
    }

    public void setIdRelatorioIncendio(Integer idRelatorioIncendio) {
        this.idRelatorioIncendio = idRelatorioIncendio;
    }

    public Date getToExtincao() {
        return toExtincao;
    }

    public void setToExtincao(Date toExtincao) {
        this.toExtincao = toExtincao;
    }

    public Date getToRescaldo() {
        return toRescaldo;
    }

    public void setToRescaldo(Date toRescaldo) {
        this.toRescaldo = toRescaldo;
    }

    public Double getCaeAgua() {
        return caeAgua;
    }

    public void setCaeAgua(Double caeAgua) {
        this.caeAgua = caeAgua;
    }

    public Float getCaeLgeEfe() {
        return caeLgeEfe;
    }

    public void setCaeLgeEfe(Float caeLgeEfe) {
        this.caeLgeEfe = caeLgeEfe;
    }

    public String getEdiAreaPresumidaOriInc() {
        return ediAreaPresumidaOriInc;
    }

    public void setEdiAreaPresumidaOriInc(String ediAreaPresumidaOriInc) {
        this.ediAreaPresumidaOriInc = ediAreaPresumidaOriInc;
    }

    public byte[] getEdiClassesProdominante() {
        return ediClassesProdominante;
    }

    public void setEdiClassesProdominante(byte[] ediClassesProdominante) {
        this.ediClassesProdominante = ediClassesProdominante;
    }

    public Character getEdiPavimentosAtingidosDo() {
        return ediPavimentosAtingidosDo;
    }

    public void setEdiPavimentosAtingidosDo(Character ediPavimentosAtingidosDo) {
        this.ediPavimentosAtingidosDo = ediPavimentosAtingidosDo;
    }

    public String getEdiPavimentosAtingidosAo() {
        return ediPavimentosAtingidosAo;
    }

    public void setEdiPavimentosAtingidosAo(String ediPavimentosAtingidosAo) {
        this.ediPavimentosAtingidosAo = ediPavimentosAtingidosAo;
    }

    public Double getEdiAreaAtingida() {
        return ediAreaAtingida;
    }

    public void setEdiAreaAtingida(Double ediAreaAtingida) {
        this.ediAreaAtingida = ediAreaAtingida;
    }

    public Double getEdiAreaTotal() {
        return ediAreaTotal;
    }

    public void setEdiAreaTotal(Double ediAreaTotal) {
        this.ediAreaTotal = ediAreaTotal;
    }

    public Boolean getVegApa() {
        return vegApa;
    }

    public void setVegApa(Boolean vegApa) {
        this.vegApa = vegApa;
    }

    public Float getVegAreasAtingida() {
        return vegAreasAtingida;
    }

    public void setVegAreasAtingida(Float vegAreasAtingida) {
        this.vegAreasAtingida = vegAreasAtingida;
    }

    public Boolean getVegAreasAtingidaUrbana() {
        return vegAreasAtingidaUrbana;
    }

    public void setVegAreasAtingidaUrbana(Boolean vegAreasAtingidaUrbana) {
        this.vegAreasAtingidaUrbana = vegAreasAtingidaUrbana;
    }

    public Boolean getVegAreasAtingidaRural() {
        return vegAreasAtingidaRural;
    }

    public void setVegAreasAtingidaRural(Boolean vegAreasAtingidaRural) {
        this.vegAreasAtingidaRural = vegAreasAtingidaRural;
    }

    public Float getVegAreasNaoAtingida() {
        return vegAreasNaoAtingida;
    }

    public void setVegAreasNaoAtingida(Float vegAreasNaoAtingida) {
        this.vegAreasNaoAtingida = vegAreasNaoAtingida;
    }

    public Boolean getVegAreasNaoAtingidaUrbana() {
        return vegAreasNaoAtingidaUrbana;
    }

    public void setVegAreasNaoAtingidaUrbana(Boolean vegAreasNaoAtingidaUrbana) {
        this.vegAreasNaoAtingidaUrbana = vegAreasNaoAtingidaUrbana;
    }

    public Boolean getVegAreasNaoAtingidaRural() {
        return vegAreasNaoAtingidaRural;
    }

    public void setVegAreasNaoAtingidaRural(Boolean vegAreasNaoAtingidaRural) {
        this.vegAreasNaoAtingidaRural = vegAreasNaoAtingidaRural;
    }

    public String getBensMoveisEImoveisAtingidos() {
        return bensMoveisEImoveisAtingidos;
    }

    public void setBensMoveisEImoveisAtingidos(String bensMoveisEImoveisAtingidos) {
        this.bensMoveisEImoveisAtingidos = bensMoveisEImoveisAtingidos;
    }

    public String getBensRecolhidos() {
        return bensRecolhidos;
    }

    public void setBensRecolhidos(String bensRecolhidos) {
        this.bensRecolhidos = bensRecolhidos;
    }

    public String getBensRecolhidosA() {
        return bensRecolhidosA;
    }

    public void setBensRecolhidosA(String bensRecolhidosA) {
        this.bensRecolhidosA = bensRecolhidosA;
    }

    public Integer getBensRecolhidosCautela() {
        return bensRecolhidosCautela;
    }

    public void setBensRecolhidosCautela(Integer bensRecolhidosCautela) {
        this.bensRecolhidosCautela = bensRecolhidosCautela;
    }

    public int getPkidFK() {
        return pkidFK;
    }

    public void setPkidFK(int pkidFK) {
        this.pkidFK = pkidFK;
    }

    public Collection<AcoesRealizadasBuscaesalvamento> getAcoesRealizadasBuscaesalvamentoCollection() {
        return acoesRealizadasBuscaesalvamentoCollection;
    }

    public void setAcoesRealizadasBuscaesalvamentoCollection(Collection<AcoesRealizadasBuscaesalvamento> acoesRealizadasBuscaesalvamentoCollection) {
        this.acoesRealizadasBuscaesalvamentoCollection = acoesRealizadasBuscaesalvamentoCollection;
    }

    public Collection<AmbienteDeTrabalho> getAmbienteDeTrabalhoCollection() {
        return ambienteDeTrabalhoCollection;
    }

    public void setAmbienteDeTrabalhoCollection(Collection<AmbienteDeTrabalho> ambienteDeTrabalhoCollection) {
        this.ambienteDeTrabalhoCollection = ambienteDeTrabalhoCollection;
    }

    public Collection<Subgrupo> getSubgrupoCollection() {
        return subgrupoCollection;
    }

    public void setSubgrupoCollection(Collection<Subgrupo> subgrupoCollection) {
        this.subgrupoCollection = subgrupoCollection;
    }

    public Collection<MilitarOcorrencia> getMilitarOcorrenciaCollection() {
        return militarOcorrenciaCollection;
    }

    public void setMilitarOcorrenciaCollection(Collection<MilitarOcorrencia> militarOcorrenciaCollection) {
        this.militarOcorrenciaCollection = militarOcorrenciaCollection;
    }

    public Collection<RecursosHidricos> getRecursosHidricosCollection() {
        return recursosHidricosCollection;
    }

    public void setRecursosHidricosCollection(Collection<RecursosHidricos> recursosHidricosCollection) {
        this.recursosHidricosCollection = recursosHidricosCollection;
    }

    public Aviso getIdavisoFK() {
        return idavisoFK;
    }

    public void setIdavisoFK(Aviso idavisoFK) {
        this.idavisoFK = idavisoFK;
    }

    public Bombeiro getIdbombeiroFK() {
        return idbombeiroFK;
    }

    public void setIdbombeiroFK(Bombeiro idbombeiroFK) {
        this.idbombeiroFK = idbombeiroFK;
    }

    public Codigodolocal getIdcodigodolocalFK() {
        return idcodigodolocalFK;
    }

    public void setIdcodigodolocalFK(Codigodolocal idcodigodolocalFK) {
        this.idcodigodolocalFK = idcodigodolocalFK;
    }

    public RelatorioOcorrencia getIdentificacaoFK() {
        return identificacaoFK;
    }

    public void setIdentificacaoFK(RelatorioOcorrencia identificacaoFK) {
        this.identificacaoFK = identificacaoFK;
    }

    public Collection<TipoRevestimentoEstutural> getTipoRevestimentoEstuturalCollection() {
        return tipoRevestimentoEstuturalCollection;
    }

    public void setTipoRevestimentoEstuturalCollection(Collection<TipoRevestimentoEstutural> tipoRevestimentoEstuturalCollection) {
        this.tipoRevestimentoEstuturalCollection = tipoRevestimentoEstuturalCollection;
    }

    public Collection<PreventivosExistentes> getPreventivosExistentesCollection() {
        return preventivosExistentesCollection;
    }

    public void setPreventivosExistentesCollection(Collection<PreventivosExistentes> preventivosExistentesCollection) {
        this.preventivosExistentesCollection = preventivosExistentesCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRelatorioIncendio != null ? idRelatorioIncendio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Relatorioincendio)) {
            return false;
        }
        Relatorioincendio other = (Relatorioincendio) object;
        if ((this.idRelatorioIncendio == null && other.idRelatorioIncendio != null) || (this.idRelatorioIncendio != null && !this.idRelatorioIncendio.equals(other.idRelatorioIncendio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.Relatorioincendio[idRelatorioIncendio=" + idRelatorioIncendio + "]";
    }
}
