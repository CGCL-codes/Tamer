package br.com.progepe.entity;

import java.io.Serializable;
import java.util.Date;

public class Servidor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long codigo;

    private String nome;

    private Integer siape;

    private Cargo cargo;

    private Date dataAdmissao;

    private Lotacao lotacao;

    private Estado estadoNascimento;

    private Integer ramal;

    private Documento documento;

    private Padrao padrao;

    private ContaBancaria contaBancaria;

    private String sexo;

    private String Email;

    private Date dataNascimento;

    private GrupoSanguineo grupoSanguineo;

    private String nomePai;

    private String nomeMae;

    private Long cidadeNascimento;

    private EstadoCivil estadoCivil;

    private CorPele corPele;

    private Date dataSaida;

    private Boolean indEstrangeiro = false;

    private RegimeTrabalho regimeTrabalho;

    private SituacaoFuncional situacaoFuncional;

    private Endereco endereco;

    private String telefone;

    private String celular;

    private String identificacaoUnica;

    private Date dataAdmServicoPublico;

    private Pais pais;

    private Date dataUltimaAlteracao;

    private Date dataUltimaAprovacao;

    private Boolean dadosValidados;

    public Long getCodigo() {
        return codigo;
    }

    public void setCodigo(Long codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getSiape() {
        return siape;
    }

    public void setSiape(Integer siape) {
        this.siape = siape;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Date getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(Date dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }

    public Lotacao getLotacao() {
        return lotacao;
    }

    public void setLotacao(Lotacao lotacao) {
        this.lotacao = lotacao;
    }

    public Integer getRamal() {
        return ramal;
    }

    public void setRamal(Integer ramal) {
        this.ramal = ramal;
    }

    public Documento getDocumento() {
        return documento;
    }

    public void setDocumento(Documento documento) {
        this.documento = documento;
    }

    public Padrao getPadrao() {
        return padrao;
    }

    public void setPadrao(Padrao padrao) {
        this.padrao = padrao;
    }

    public ContaBancaria getContaBancaria() {
        return contaBancaria;
    }

    public void setContaBancaria(ContaBancaria contaBancaria) {
        this.contaBancaria = contaBancaria;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public GrupoSanguineo getGrupoSanguineo() {
        return grupoSanguineo;
    }

    public void setGrupoSanguineo(GrupoSanguineo grupoSanguineo) {
        this.grupoSanguineo = grupoSanguineo;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public Long getCidadeNascimento() {
        return cidadeNascimento;
    }

    public void setCidadeNascimento(Long cidadeNascimento) {
        this.cidadeNascimento = cidadeNascimento;
    }

    public EstadoCivil getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(EstadoCivil estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    public CorPele getCorPele() {
        return corPele;
    }

    public void setCorPele(CorPele corPele) {
        this.corPele = corPele;
    }

    public Boolean getIndEstrangeiro() {
        return indEstrangeiro;
    }

    public void setIndEstrangeiro(Boolean indEstrangeiro) {
        this.indEstrangeiro = indEstrangeiro;
    }

    public RegimeTrabalho getRegimeTrabalho() {
        return regimeTrabalho;
    }

    public void setRegimeTrabalho(RegimeTrabalho regimeTrabalho) {
        this.regimeTrabalho = regimeTrabalho;
    }

    public SituacaoFuncional getSituacaoFuncional() {
        return situacaoFuncional;
    }

    public void setSituacaoFuncional(SituacaoFuncional situacaoFuncional) {
        this.situacaoFuncional = situacaoFuncional;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getIdentificacaoUnica() {
        return identificacaoUnica;
    }

    public void setIdentificacaoUnica(String identificacaoUnica) {
        this.identificacaoUnica = identificacaoUnica;
    }

    public Date getDataAdmServicoPublico() {
        return dataAdmServicoPublico;
    }

    public void setDataAdmServicoPublico(Date dataAdmServicoPublico) {
        this.dataAdmServicoPublico = dataAdmServicoPublico;
    }

    public Date getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(Date dataSaida) {
        this.dataSaida = dataSaida;
    }

    public Pais getPais() {
        return pais;
    }

    public void setPais(Pais pais) {
        this.pais = pais;
    }

    public Date getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public Boolean getDadosValidados() {
        return dadosValidados;
    }

    public void setDadosValidados(Boolean dadosValidados) {
        this.dadosValidados = dadosValidados;
    }

    public Estado getEstadoNascimento() {
        return estadoNascimento;
    }

    public void setEstadoNascimento(Estado estadoNascimento) {
        this.estadoNascimento = estadoNascimento;
    }

    public Date getDataUltimaAprovacao() {
        return dataUltimaAprovacao;
    }

    public void setDataUltimaAprovacao(Date dataUltimaAprovacao) {
        this.dataUltimaAprovacao = dataUltimaAprovacao;
    }
}
