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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author lellis
 */
@Entity
@Table(name = "atendimento_pre_hospitalar")
@NamedQueries({ @NamedQuery(name = "AtendimentoPreHospitalar.findAll", query = "SELECT a FROM AtendimentoPreHospitalar a"), @NamedQuery(name = "AtendimentoPreHospitalar.findByIdAtendimentoPreHospitalar", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.idAtendimentoPreHospitalar = :idAtendimentoPreHospitalar"), @NamedQuery(name = "AtendimentoPreHospitalar.findByAtendimentomedico", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.atendimentomedico = :atendimentomedico"), @NamedQuery(name = "AtendimentoPreHospitalar.findByNomeDoMedico", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.nomeDoMedico = :nomeDoMedico"), @NamedQuery(name = "AtendimentoPreHospitalar.findByCrm", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.crm = :crm"), @NamedQuery(name = "AtendimentoPreHospitalar.findByNDaFichaProntuarioAtend", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.nDaFichaProntuarioAtend = :nDaFichaProntuarioAtend"), @NamedQuery(name = "AtendimentoPreHospitalar.findByNomeHospital", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.nomeHospital = :nomeHospital"), @NamedQuery(name = "AtendimentoPreHospitalar.findByPkidFK", query = "SELECT a FROM AtendimentoPreHospitalar a WHERE a.pkidFK = :pkidFK") })
public class AtendimentoPreHospitalar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_atendimento_pre_hospitalar")
    private Integer idAtendimentoPreHospitalar;

    @Column(name = "atendimentomedico")
    private Boolean atendimentomedico;

    @Column(name = "nome_do_medico")
    private String nomeDoMedico;

    @Column(name = "crm")
    private String crm;

    @Column(name = "n_da_ficha_prontuario_atend")
    private String nDaFichaProntuarioAtend;

    @Column(name = "nome_hospital")
    private String nomeHospital;

    @Basic(optional = false)
    @Column(name = "pk_id_FK")
    private int pkidFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<Queimadura> queimaduraCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<DestinoVitima> destinoVitimaCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<AcoesRealizadasBuscaesalvamento> acoesRealizadasBuscaesalvamentoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<DadosVitais> dadosVitaisCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<ItemVeiculo> itemVeiculoCollection;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<PrincipaisLesoes> principaisLesoesCollection;

    @JoinColumn(name = "id_bombeiro_FK", referencedColumnName = "id_bombeiro")
    @ManyToOne(optional = false)
    private Bombeiro idbombeiroFK;

    @JoinColumn(name = "id_aviso_FK", referencedColumnName = "id_aviso")
    @ManyToOne(optional = false)
    private Aviso idavisoFK;

    @JoinColumn(name = "id_codigodolocal_FK", referencedColumnName = "id_codigodolocal")
    @ManyToOne(optional = false)
    private Codigodolocal idcodigodolocalFK;

    @JoinColumn(name = "identificacao_FK", referencedColumnName = "pk_id")
    @ManyToOne(optional = false)
    private RelatorioOcorrencia identificacaoFK;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idatendimentoprehospitalarFK")
    private Collection<QualificacaoDaVitima> qualificacaoDaVitimaCollection;

    public AtendimentoPreHospitalar() {
    }

    public AtendimentoPreHospitalar(Integer idAtendimentoPreHospitalar) {
        this.idAtendimentoPreHospitalar = idAtendimentoPreHospitalar;
    }

    public AtendimentoPreHospitalar(Integer idAtendimentoPreHospitalar, int pkidFK) {
        this.idAtendimentoPreHospitalar = idAtendimentoPreHospitalar;
        this.pkidFK = pkidFK;
    }

    public Integer getIdAtendimentoPreHospitalar() {
        return idAtendimentoPreHospitalar;
    }

    public void setIdAtendimentoPreHospitalar(Integer idAtendimentoPreHospitalar) {
        this.idAtendimentoPreHospitalar = idAtendimentoPreHospitalar;
    }

    public Boolean getAtendimentomedico() {
        return atendimentomedico;
    }

    public void setAtendimentomedico(Boolean atendimentomedico) {
        this.atendimentomedico = atendimentomedico;
    }

    public String getNomeDoMedico() {
        return nomeDoMedico;
    }

    public void setNomeDoMedico(String nomeDoMedico) {
        this.nomeDoMedico = nomeDoMedico;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public String getNDaFichaProntuarioAtend() {
        return nDaFichaProntuarioAtend;
    }

    public void setNDaFichaProntuarioAtend(String nDaFichaProntuarioAtend) {
        this.nDaFichaProntuarioAtend = nDaFichaProntuarioAtend;
    }

    public String getNomeHospital() {
        return nomeHospital;
    }

    public void setNomeHospital(String nomeHospital) {
        this.nomeHospital = nomeHospital;
    }

    public int getPkidFK() {
        return pkidFK;
    }

    public void setPkidFK(int pkidFK) {
        this.pkidFK = pkidFK;
    }

    public Collection<Queimadura> getQueimaduraCollection() {
        return queimaduraCollection;
    }

    public void setQueimaduraCollection(Collection<Queimadura> queimaduraCollection) {
        this.queimaduraCollection = queimaduraCollection;
    }

    public Collection<DestinoVitima> getDestinoVitimaCollection() {
        return destinoVitimaCollection;
    }

    public void setDestinoVitimaCollection(Collection<DestinoVitima> destinoVitimaCollection) {
        this.destinoVitimaCollection = destinoVitimaCollection;
    }

    public Collection<AcoesRealizadasBuscaesalvamento> getAcoesRealizadasBuscaesalvamentoCollection() {
        return acoesRealizadasBuscaesalvamentoCollection;
    }

    public void setAcoesRealizadasBuscaesalvamentoCollection(Collection<AcoesRealizadasBuscaesalvamento> acoesRealizadasBuscaesalvamentoCollection) {
        this.acoesRealizadasBuscaesalvamentoCollection = acoesRealizadasBuscaesalvamentoCollection;
    }

    public Collection<DadosVitais> getDadosVitaisCollection() {
        return dadosVitaisCollection;
    }

    public void setDadosVitaisCollection(Collection<DadosVitais> dadosVitaisCollection) {
        this.dadosVitaisCollection = dadosVitaisCollection;
    }

    public Collection<ItemVeiculo> getItemVeiculoCollection() {
        return itemVeiculoCollection;
    }

    public void setItemVeiculoCollection(Collection<ItemVeiculo> itemVeiculoCollection) {
        this.itemVeiculoCollection = itemVeiculoCollection;
    }

    public Collection<PrincipaisLesoes> getPrincipaisLesoesCollection() {
        return principaisLesoesCollection;
    }

    public void setPrincipaisLesoesCollection(Collection<PrincipaisLesoes> principaisLesoesCollection) {
        this.principaisLesoesCollection = principaisLesoesCollection;
    }

    public Bombeiro getIdbombeiroFK() {
        return idbombeiroFK;
    }

    public void setIdbombeiroFK(Bombeiro idbombeiroFK) {
        this.idbombeiroFK = idbombeiroFK;
    }

    public Aviso getIdavisoFK() {
        return idavisoFK;
    }

    public void setIdavisoFK(Aviso idavisoFK) {
        this.idavisoFK = idavisoFK;
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

    public Collection<QualificacaoDaVitima> getQualificacaoDaVitimaCollection() {
        return qualificacaoDaVitimaCollection;
    }

    public void setQualificacaoDaVitimaCollection(Collection<QualificacaoDaVitima> qualificacaoDaVitimaCollection) {
        this.qualificacaoDaVitimaCollection = qualificacaoDaVitimaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAtendimentoPreHospitalar != null ? idAtendimentoPreHospitalar.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof AtendimentoPreHospitalar)) {
            return false;
        }
        AtendimentoPreHospitalar other = (AtendimentoPreHospitalar) object;
        if ((this.idAtendimentoPreHospitalar == null && other.idAtendimentoPreHospitalar != null) || (this.idAtendimentoPreHospitalar != null && !this.idAtendimentoPreHospitalar.equals(other.idAtendimentoPreHospitalar))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.brasilsemchamas.entidade.relatorio.AtendimentoPreHospitalar[idAtendimentoPreHospitalar=" + idAtendimentoPreHospitalar + "]";
    }
}
