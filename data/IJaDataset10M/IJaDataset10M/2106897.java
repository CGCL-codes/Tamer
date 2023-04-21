package br.com.progepe.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import br.com.progepe.constantes.Constantes;
import br.com.progepe.dao.AutenticacaoDAO;
import br.com.progepe.dao.CidadeDAO;
import br.com.progepe.dao.DAO;
import br.com.progepe.dao.FuncaoServidorDAO;
import br.com.progepe.dao.ServidorDAO;
import br.com.progepe.encripty.Encripty;
import br.com.progepe.entity.Autenticacao;
import br.com.progepe.entity.Banco;
import br.com.progepe.entity.Cargo;
import br.com.progepe.entity.Cidade;
import br.com.progepe.entity.Classe;
import br.com.progepe.entity.ContaBancaria;
import br.com.progepe.entity.CorPele;
import br.com.progepe.entity.Documento;
import br.com.progepe.entity.Endereco;
import br.com.progepe.entity.Estado;
import br.com.progepe.entity.EstadoCivil;
import br.com.progepe.entity.Funcao;
import br.com.progepe.entity.FuncaoServidor;
import br.com.progepe.entity.GrupoSanguineo;
import br.com.progepe.entity.Lotacao;
import br.com.progepe.entity.Padrao;
import br.com.progepe.entity.Pais;
import br.com.progepe.entity.RegimeTrabalho;
import br.com.progepe.entity.Servidor;
import br.com.progepe.entity.SituacaoFuncional;
import br.com.progepe.validator.Validator;

public class ServidorController {

    private Servidor servidor;

    private List<Servidor> servidores;

    private List<Servidor> servidoresList;

    private List<SelectItem> cargos = new ArrayList<SelectItem>();

    private List<SelectItem> classes = new ArrayList<SelectItem>();

    private List<SelectItem> bancos = new ArrayList<SelectItem>();

    private List<SelectItem> coresPeles = new ArrayList<SelectItem>();

    private List<SelectItem> estadosCivis = new ArrayList<SelectItem>();

    private List<SelectItem> funcoes = new ArrayList<SelectItem>();

    private List<SelectItem> gruposSanguineos = new ArrayList<SelectItem>();

    private List<SelectItem> estados = new ArrayList<SelectItem>();

    private List<SelectItem> ufs = new ArrayList<SelectItem>();

    private List<SelectItem> cidadesNascimento = new ArrayList<SelectItem>();

    private List<SelectItem> cidades = new ArrayList<SelectItem>();

    private List<SelectItem> lotacoes = new ArrayList<SelectItem>();

    private List<SelectItem> paises = new ArrayList<SelectItem>();

    private List<SelectItem> situacoesFuncionais = new ArrayList<SelectItem>();

    private List<SelectItem> padroes = new ArrayList<SelectItem>();

    private List<SelectItem> regimesTrabalhos = new ArrayList<SelectItem>();

    private List<SelectItem> tipoFuncoes = new ArrayList<SelectItem>();

    private Boolean servidorEstrangeiro = false;

    private Boolean servidorBrasileiro = true;

    private Boolean indPoupanca = false;

    private Boolean indFeminino = false;

    private Boolean indEstagiario = false;

    private String dataUltimaAlteracao;

    private String dataUltimaAprovacao;

    private Integer situacao = 0;

    public Integer getSituacao() {
        return situacao;
    }

    public void setSituacao(Integer situacao) {
        this.situacao = situacao;
    }

    public Servidor getPessoa() {
        return servidor;
    }

    public void setPessoa(Servidor pessoa) {
        this.servidor = pessoa;
    }

    public List<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(List<SelectItem> estados) {
        this.estados = estados;
    }

    public List<SelectItem> getCidades() {
        return cidades;
    }

    public void setCidades(List<SelectItem> cidades) {
        this.cidades = cidades;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public List<Servidor> getServidores() {
        return servidores;
    }

    public void setServidores(List<Servidor> servidores) {
        this.servidores = servidores;
    }

    public List<SelectItem> getLotacoes() {
        return lotacoes;
    }

    public void setLotacoes(List<SelectItem> lotacoes) {
        this.lotacoes = lotacoes;
    }

    public List<SelectItem> getCidadesNascimento() {
        return cidadesNascimento;
    }

    public void setCidadesNascimento(List<SelectItem> cidadesNascimento) {
        this.cidadesNascimento = cidadesNascimento;
    }

    public List<SelectItem> getPaises() {
        return paises;
    }

    public void setPaises(List<SelectItem> paises) {
        this.paises = paises;
    }

    public List<SelectItem> getEstadosCivis() {
        return estadosCivis;
    }

    public void setEstadosCivis(List<SelectItem> estadosCivis) {
        this.estadosCivis = estadosCivis;
    }

    public List<SelectItem> getSituacoesFuncionais() {
        return situacoesFuncionais;
    }

    public void setSituacoesFuncionais(List<SelectItem> situacoesFuncionais) {
        this.situacoesFuncionais = situacoesFuncionais;
    }

    public List<SelectItem> getPadroes() {
        return padroes;
    }

    public void setPadroes(List<SelectItem> padroes) {
        this.padroes = padroes;
    }

    public List<SelectItem> getRegimesTrabalhos() {
        return regimesTrabalhos;
    }

    public void setRegimesTrabalhos(List<SelectItem> regimesTrabalhos) {
        this.regimesTrabalhos = regimesTrabalhos;
    }

    public List<SelectItem> getCargos() {
        return cargos;
    }

    public void setCargos(List<SelectItem> cargos) {
        this.cargos = cargos;
    }

    public List<SelectItem> getBancos() {
        return bancos;
    }

    public void setBancos(List<SelectItem> bancos) {
        this.bancos = bancos;
    }

    public List<SelectItem> getCoresPeles() {
        return coresPeles;
    }

    public void setCoresPeles(List<SelectItem> coresPeles) {
        this.coresPeles = coresPeles;
    }

    public List<SelectItem> getFuncoes() {
        return funcoes;
    }

    public void setFuncoes(List<SelectItem> funcoes) {
        this.funcoes = funcoes;
    }

    public List<SelectItem> getGruposSanguineos() {
        return gruposSanguineos;
    }

    public void setGruposSanguineos(List<SelectItem> gruposSanguineos) {
        this.gruposSanguineos = gruposSanguineos;
    }

    public List<SelectItem> getUfs() {
        return ufs;
    }

    public void setUfs(List<SelectItem> ufs) {
        this.ufs = ufs;
    }

    public Boolean getServidorEstrangeiro() {
        return servidorEstrangeiro;
    }

    public void setServidorEstrangeiro(Boolean servidorEstrangeiro) {
        this.servidorEstrangeiro = servidorEstrangeiro;
    }

    public Boolean getIndPoupanca() {
        return indPoupanca;
    }

    public void setIndPoupanca(Boolean indPoupanca) {
        this.indPoupanca = indPoupanca;
    }

    public List<SelectItem> getClasses() {
        return classes;
    }

    public void setClasses(List<SelectItem> classes) {
        this.classes = classes;
    }

    public List<SelectItem> getTipoFuncoes() {
        return tipoFuncoes;
    }

    public void setTipoFuncoes(List<SelectItem> tipoFuncoes) {
        this.tipoFuncoes = tipoFuncoes;
    }

    public List<Servidor> getServidoresList() {
        return servidoresList;
    }

    public void setServidoresList(List<Servidor> servidoresList) {
        this.servidoresList = servidoresList;
    }

    public Boolean getServidorBrasileiro() {
        return servidorBrasileiro;
    }

    public void setServidorBrasileiro(Boolean servidorBrasileiro) {
        this.servidorBrasileiro = servidorBrasileiro;
    }

    public Boolean getIndFeminino() {
        return indFeminino;
    }

    public void setIndFeminino(Boolean indFeminino) {
        this.indFeminino = indFeminino;
    }

    public String getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public void setDataUltimaAlteracao(String dataUltimaAlteracao) {
        this.dataUltimaAlteracao = dataUltimaAlteracao;
    }

    public String getDataUltimaAprovacao() {
        return dataUltimaAprovacao;
    }

    public void setDataUltimaAprovacao(String dataUltimaAprovacao) {
        this.dataUltimaAprovacao = dataUltimaAprovacao;
    }

    public Boolean getIndEstagiario() {
        return indEstagiario;
    }

    public void setIndEstagiario(Boolean indEstagiario) {
        this.indEstagiario = indEstagiario;
    }

    public void cadastrar() throws IOException {
        servidor = new Servidor();
        servidor.setCargo(new Cargo());
        servidor.getCargo().setClasse(new Classe());
        servidor.setEstadoNascimento(new Estado());
        servidor.setContaBancaria(new ContaBancaria());
        servidor.getContaBancaria().setBanco(new Banco());
        servidor.setCorPele(new CorPele());
        servidor.setDocumento(new Documento());
        servidor.setEndereco(new Endereco());
        servidor.getEndereco().setCidade(new Cidade());
        servidor.getEndereco().getCidade().setEstado(new Estado());
        servidor.setEstadoCivil(new EstadoCivil());
        servidor.setGrupoSanguineo(new GrupoSanguineo());
        servidor.setLotacao(new Lotacao());
        servidor.setEndereco(new Endereco());
        servidor.getEndereco().setCidade(new Cidade());
        servidor.getEndereco().getCidade().setEstado(new Estado());
        servidor.setPadrao(new Padrao());
        servidor.setRegimeTrabalho(new RegimeTrabalho());
        servidor.setSituacaoFuncional(new SituacaoFuncional());
        servidor.setPais(new Pais());
        cargos = new ArrayList<SelectItem>();
        classes = new ArrayList<SelectItem>();
        ufs = new ArrayList<SelectItem>();
        cidadesNascimento = new ArrayList<SelectItem>();
        cidades = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        bancos = new ArrayList<SelectItem>();
        coresPeles = new ArrayList<SelectItem>();
        estadosCivis = new ArrayList<SelectItem>();
        funcoes = new ArrayList<SelectItem>();
        lotacoes = new ArrayList<SelectItem>();
        gruposSanguineos = new ArrayList<SelectItem>();
        padroes = new ArrayList<SelectItem>();
        regimesTrabalhos = new ArrayList<SelectItem>();
        situacoesFuncionais = new ArrayList<SelectItem>();
        paises = new ArrayList<SelectItem>();
        tipoFuncoes = new ArrayList<SelectItem>();
        listarBancos();
        listarEstados();
        listarUfs();
        listarLotacoes();
        listarPadroes();
        listarSituacoesFuncionais();
        listarFuncoes();
        listarCargos();
        listarRegimesTrabalhos();
        listarGrupoSanguineo();
        listarCorPele();
        listarEstadosCivis();
        FacesContext.getCurrentInstance().getExternalContext().redirect("cadastrarServidor.jsp");
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarUfs() {
        ufs = new ArrayList<SelectItem>();
        List<Estado> estadoList = new ArrayList<Estado>();
        estadoList = DAO.getInstance().list(Estado.class, "descricao");
        for (Estado estado : estadoList) {
            ufs.add(new SelectItem(estado.getCodigo(), estado.getUf()));
        }
        return ufs;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarEstados() {
        estados = new ArrayList<SelectItem>();
        List<Estado> estadoList = new ArrayList<Estado>();
        estadoList = DAO.getInstance().list(Estado.class, "descricao");
        for (Estado estado : estadoList) {
            estados.add(new SelectItem(estado.getCodigo(), estado.getDescricao()));
        }
        return estados;
    }

    public List<SelectItem> listarCidadesNascimentoServidor() {
        cidadesNascimento = new ArrayList<SelectItem>();
        List<Cidade> cidadeList = new ArrayList<Cidade>();
        cidadeList = CidadeDAO.getInstance().listByEstado(servidor.getEstadoNascimento());
        for (Cidade cidade : cidadeList) {
            cidadesNascimento.add(new SelectItem(cidade.getCodigo(), cidade.getDescricao()));
        }
        return cidadesNascimento;
    }

    public List<SelectItem> listarCidadesContato() {
        cidades = new ArrayList<SelectItem>();
        List<Cidade> cidadeList = new ArrayList<Cidade>();
        cidadeList = CidadeDAO.getInstance().listByEstado(servidor.getEndereco().getCidade().getEstado());
        for (Cidade cidade : cidadeList) {
            cidades.add(new SelectItem(cidade.getCodigo(), cidade.getDescricao()));
        }
        return cidades;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarLotacoes() {
        lotacoes = new ArrayList<SelectItem>();
        List<Lotacao> lotacaoList = new ArrayList<Lotacao>();
        lotacaoList = DAO.getInstance().list(Lotacao.class, "descricao");
        for (Lotacao lotacao : lotacaoList) {
            lotacoes.add(new SelectItem(lotacao.getCodigo(), lotacao.getDescricao()));
        }
        return lotacoes;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarEstadosCivis() {
        estadosCivis = new ArrayList<SelectItem>();
        List<EstadoCivil> estadoCivilList = new ArrayList<EstadoCivil>();
        estadoCivilList = DAO.getInstance().list(EstadoCivil.class, "descricao");
        for (EstadoCivil estadoCivil : estadoCivilList) {
            estadosCivis.add(new SelectItem(estadoCivil.getCodigo(), estadoCivil.getDescricao()));
        }
        return estadosCivis;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarCorPele() {
        coresPeles = new ArrayList<SelectItem>();
        List<CorPele> corPeleList = new ArrayList<CorPele>();
        corPeleList = DAO.getInstance().list(CorPele.class, "descricao");
        for (CorPele corPele : corPeleList) {
            coresPeles.add(new SelectItem(corPele.getCodigo(), corPele.getDescricao()));
        }
        return coresPeles;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarGrupoSanguineo() {
        gruposSanguineos = new ArrayList<SelectItem>();
        List<GrupoSanguineo> grupoSanguineoList = new ArrayList<GrupoSanguineo>();
        grupoSanguineoList = DAO.getInstance().list(GrupoSanguineo.class, "descricao");
        for (GrupoSanguineo grupoSanguineo : grupoSanguineoList) {
            gruposSanguineos.add(new SelectItem(grupoSanguineo.getCodigo(), grupoSanguineo.getDescricao()));
        }
        return gruposSanguineos;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarPadroes() {
        padroes = new ArrayList<SelectItem>();
        List<Padrao> padraoList = new ArrayList<Padrao>();
        padraoList = DAO.getInstance().list(Padrao.class, "nivel");
        for (Padrao padrao : padraoList) {
            padroes.add(new SelectItem(padrao.getCodigo(), padrao.getNivel().toString()));
        }
        return padroes;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarBancos() {
        bancos = new ArrayList<SelectItem>();
        List<Banco> bancoList = new ArrayList<Banco>();
        bancoList = DAO.getInstance().list(Banco.class, "descricao");
        for (Banco banco : bancoList) {
            bancos.add(new SelectItem(banco.getCodigo(), banco.getDescricao()));
        }
        return bancos;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarFuncoes() {
        funcoes = new ArrayList<SelectItem>();
        List<Funcao> funcoesList = new ArrayList<Funcao>();
        funcoesList = DAO.getInstance().list(Funcao.class, "descricao");
        for (Funcao funcao : funcoesList) {
            funcoes.add(new SelectItem(funcao.getCodigo(), funcao.getDescricao()));
        }
        return funcoes;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarClasses() {
        classes = new ArrayList<SelectItem>();
        List<Classe> classeList = new ArrayList<Classe>();
        classeList = DAO.getInstance().list(servidor.getCargo().getClasse().getClass(), "descricao");
        for (Classe classe : classeList) {
            classes.add(new SelectItem(classe.getCodigo(), classe.getSigla()));
        }
        return classes;
    }

    public void carregarClasse() {
        Cargo cargo = servidor.getCargo();
        servidor.setCargo(new Cargo());
        servidor.getCargo().setClasse(new Classe());
        cargo = (Cargo) DAO.getInstance().refresh(cargo);
        servidor.setCargo(cargo);
        if ((Constantes.CARGO_ESTAGIARIO).equals(servidor.getCargo().getCodigo())) {
            indEstagiario = true;
            servidor.getPadrao().setCodigo(Constantes.PADRAO_DEFAULT);
            servidor.getCargo().getClasse().setCodigo(Constantes.CLASSE_DEFAULT);
        } else {
            indEstagiario = false;
        }
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarSituacoesFuncionais() {
        situacoesFuncionais = new ArrayList<SelectItem>();
        List<SituacaoFuncional> situacaoFuncionalList = new ArrayList<SituacaoFuncional>();
        situacaoFuncionalList = DAO.getInstance().list(SituacaoFuncional.class, "descricao");
        for (SituacaoFuncional situacaoFuncional : situacaoFuncionalList) {
            situacoesFuncionais.add(new SelectItem(situacaoFuncional.getCodigo(), situacaoFuncional.getDescricao()));
        }
        return situacoesFuncionais;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarRegimesTrabalhos() {
        regimesTrabalhos = new ArrayList<SelectItem>();
        List<RegimeTrabalho> regimeTrabalhoList = new ArrayList<RegimeTrabalho>();
        regimeTrabalhoList = DAO.getInstance().list(RegimeTrabalho.class, "descricao");
        for (RegimeTrabalho regimeTrabalho : regimeTrabalhoList) {
            regimesTrabalhos.add(new SelectItem(regimeTrabalho.getCodigo(), regimeTrabalho.getDescricao()));
        }
        return regimesTrabalhos;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarCargos() {
        cargos = new ArrayList<SelectItem>();
        List<Cargo> cargoList = new ArrayList<Cargo>();
        cargoList = DAO.getInstance().list(Cargo.class, "descricao");
        for (Cargo cargo : cargoList) {
            cargos.add(new SelectItem(cargo.getCodigo(), cargo.getDescricao()));
        }
        return cargos;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> listarPais() {
        paises = new ArrayList<SelectItem>();
        List<Pais> paisList = new ArrayList<Pais>();
        paisList = DAO.getInstance().list(Pais.class, "descricao");
        for (Pais pais : paisList) {
            paises.add(new SelectItem(pais.getCodigo(), pais.getDescricao()));
        }
        return paises;
    }

    public void isEstrangeiro() {
        if (servidor.getIndEstrangeiro()) {
            servidorEstrangeiro = true;
            servidor.setPais(new Pais());
            listarPais();
            servidor.setCidadeNascimento(null);
            servidor.setEstadoNascimento(new Estado());
            servidorBrasileiro = false;
            cidadesNascimento = new ArrayList<SelectItem>();
        } else {
            servidor.setPais(null);
            servidor.setEstadoNascimento(new Estado());
            servidorEstrangeiro = false;
            servidorBrasileiro = true;
        }
    }

    public void isFeminino() {
        if (("M").equals(servidor.getSexo())) {
            indFeminino = false;
        } else {
            indFeminino = true;
        }
    }

    public void isPoupanca() {
        if ((Constantes.CAIXA_ECONOMICA_FEDERAL).equals(servidor.getContaBancaria().getBanco().getCodigo())) {
            indPoupanca = true;
        } else {
            indPoupanca = false;
        }
    }

    public void salvar() throws IOException {
        try {
            servidor.setDataUltimaAlteracao(new Date());
            servidor.setDadosValidados(false);
            if (servidor.getIndEstrangeiro() == false) {
                servidor.setPais(null);
            }
            if (servidor.getDataSaida() != null) {
                Autenticacao autenticacao = new Autenticacao();
                autenticacao.setSiape(servidor.getSiape());
                AutenticacaoDAO.getInstance().deleteAutenticacao(autenticacao);
                FuncaoServidor funcaoServidor = new FuncaoServidor();
                funcaoServidor.setServidor(servidor);
                List<FuncaoServidor> list = FuncaoServidorDAO.getInstance().listByFilter(funcaoServidor, true);
                if (list != null) {
                    for (FuncaoServidor item : list) {
                        item.setDataSaida(servidor.getDataSaida());
                        FuncaoServidorDAO.getInstance().updateFuncaoServidor(item);
                    }
                }
            }
            if (servidor.getCodigo() == null || servidor.getCodigo() == 0) {
                Autenticacao autenticacao = new Autenticacao();
                autenticacao.setSiape(servidor.getSiape());
                autenticacao.setSenha(Encripty.criptografaSenha(servidor.getSiape().toString()));
                AutenticacaoDAO.getInstance().saveAutenticacao(autenticacao);
            }
            DAO.getInstance().saveOrUpdate(servidor);
            servidor = (Servidor) DAO.getInstance().refresh(servidor);
            if (servidor.getDataUltimaAlteracao() != null) {
                this.setDataUltimaAlteracao(Validator.formatarDataHoraBR(servidor.getDataUltimaAlteracao()));
            }
            if (servidor.getDataUltimaAprovacao() != null) {
                this.setDataUltimaAprovacao(Validator.formatarDataHoraBR(servidor.getDataUltimaAprovacao()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void aprovar() throws IOException {
        try {
            servidor.setDataUltimaAprovacao(new Date());
            servidor.setDadosValidados(true);
            DAO.getInstance().update(servidor);
            servidor = (Servidor) DAO.getInstance().refresh(servidor);
            if (servidor.getDataUltimaAprovacao() != null) {
                this.setDataUltimaAprovacao(Validator.formatarDataHoraBR(servidor.getDataUltimaAprovacao()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void validarCPF() {
        if (!Validator.validaCPF(servidor.getDocumento().getCpf())) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Campo CPF inv�lido!", "Campo CPF inv�lido!");
            FacesContext.getCurrentInstance().addMessage("", message);
            servidor.getDocumento().setCpf("");
        }
    }

    @SuppressWarnings("unchecked")
    public List<Servidor> listar() throws IOException {
        servidor = new Servidor();
        this.setServidores(DAO.getInstance().list(Servidor.class));
        FacesContext.getCurrentInstance().getExternalContext().redirect("listarServidores.jsp");
        return this.getServidores();
    }

    public void carregar() throws IOException, ParseException {
        cargos = new ArrayList<SelectItem>();
        classes = new ArrayList<SelectItem>();
        ufs = new ArrayList<SelectItem>();
        cidadesNascimento = new ArrayList<SelectItem>();
        cidades = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        bancos = new ArrayList<SelectItem>();
        coresPeles = new ArrayList<SelectItem>();
        estadosCivis = new ArrayList<SelectItem>();
        funcoes = new ArrayList<SelectItem>();
        lotacoes = new ArrayList<SelectItem>();
        gruposSanguineos = new ArrayList<SelectItem>();
        padroes = new ArrayList<SelectItem>();
        regimesTrabalhos = new ArrayList<SelectItem>();
        situacoesFuncionais = new ArrayList<SelectItem>();
        paises = new ArrayList<SelectItem>();
        tipoFuncoes = new ArrayList<SelectItem>();
        listarBancos();
        listarEstados();
        listarUfs();
        listarLotacoes();
        listarPadroes();
        listarSituacoesFuncionais();
        listarFuncoes();
        listarCargos();
        listarRegimesTrabalhos();
        listarGrupoSanguineo();
        listarCorPele();
        listarEstadosCivis();
        FacesContext context = FacesContext.getCurrentInstance();
        servidor = (Servidor) context.getExternalContext().getRequestMap().get("list");
        if (servidor.getCidadeNascimento() != null) {
            servidor.setEstadoNascimento(new Estado());
            Cidade cidade = new Cidade();
            cidade.setCodigo(servidor.getCidadeNascimento());
            cidade = (Cidade) DAO.getInstance().refresh(cidade);
            servidor.setEstadoNascimento(cidade.getEstado());
            servidor.setCidadeNascimento(cidade.getCodigo());
            listarCidadesNascimentoServidor();
        }
        if (servidor.getEndereco() != null && servidor.getEndereco().getCidade().getCodigo() != 0) {
            listarCidadesContato();
        }
        if ((new Long(104)).equals(servidor.getContaBancaria().getBanco().getCodigo())) {
            indPoupanca = true;
        } else {
            indPoupanca = false;
        }
        isFeminino();
        if (servidor.getIndEstrangeiro()) {
            listarPais();
            servidorEstrangeiro = Boolean.TRUE;
            servidorBrasileiro = Boolean.FALSE;
        } else {
            servidorEstrangeiro = Boolean.FALSE;
            servidorBrasileiro = Boolean.TRUE;
        }
        if (servidor.getDataUltimaAlteracao() != null) {
            this.setDataUltimaAlteracao(Validator.formatarDataHoraBR(servidor.getDataUltimaAlteracao()));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("cadastrarServidor.jsp");
    }

    public void pesquisarServidores() throws IOException {
        servidor = new Servidor();
        servidor.setLotacao(new Lotacao());
        servidor.setCargo(new Cargo());
        cargos = new ArrayList<SelectItem>();
        lotacoes = new ArrayList<SelectItem>();
        servidoresList = new ArrayList<Servidor>();
        listarLotacoes();
        listarCargos();
        if (servidor.getCidadeNascimento() != null) {
            Cidade cidade = new Cidade();
            servidor.setEstadoNascimento(new Estado());
            cidade.setCodigo(servidor.getCidadeNascimento());
            cidade = (Cidade) DAO.getInstance().refresh(cidade);
            servidor.setEstadoNascimento(cidade.getEstado());
            servidor.setCidadeNascimento(cidade.getCodigo());
            listarCidadesNascimentoServidor();
        }
        if (servidor.getEndereco() != null && servidor.getEndereco().getCidade().getCodigo() != 0) {
            listarCidadesContato();
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("listarServidoresFilter.jsp");
    }

    public List<Servidor> listarServidoresFiltro() {
        servidoresList = new ArrayList<Servidor>();
        setServidoresList(ServidorDAO.getInstance().listByFilter(servidor, situacao));
        if (getServidoresList().size() == 0) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Nenhum registro para o filtro informado!", "Nenhum registro para o filtro informado!");
            FacesContext.getCurrentInstance().addMessage("", message);
        }
        return servidoresList;
    }

    public void buscarServidorLogado() throws IOException, ParseException {
        servidor = new Servidor();
        servidor.setCargo(new Cargo());
        servidor.getCargo().setClasse(new Classe());
        servidor.setEstadoNascimento(new Estado());
        servidor.setContaBancaria(new ContaBancaria());
        servidor.getContaBancaria().setBanco(new Banco());
        servidor.setCorPele(new CorPele());
        servidor.setDocumento(new Documento());
        servidor.setEndereco(new Endereco());
        servidor.getEndereco().setCidade(new Cidade());
        servidor.getEndereco().getCidade().setEstado(new Estado());
        servidor.setEstadoCivil(new EstadoCivil());
        servidor.setGrupoSanguineo(new GrupoSanguineo());
        servidor.setLotacao(new Lotacao());
        servidor.setEndereco(new Endereco());
        servidor.getEndereco().setCidade(new Cidade());
        servidor.getEndereco().getCidade().setEstado(new Estado());
        servidor.setPadrao(new Padrao());
        servidor.setRegimeTrabalho(new RegimeTrabalho());
        servidor.setSituacaoFuncional(new SituacaoFuncional());
        servidor.setPais(new Pais());
        cargos = new ArrayList<SelectItem>();
        classes = new ArrayList<SelectItem>();
        ufs = new ArrayList<SelectItem>();
        cidadesNascimento = new ArrayList<SelectItem>();
        cidades = new ArrayList<SelectItem>();
        estados = new ArrayList<SelectItem>();
        bancos = new ArrayList<SelectItem>();
        coresPeles = new ArrayList<SelectItem>();
        estadosCivis = new ArrayList<SelectItem>();
        funcoes = new ArrayList<SelectItem>();
        lotacoes = new ArrayList<SelectItem>();
        gruposSanguineos = new ArrayList<SelectItem>();
        padroes = new ArrayList<SelectItem>();
        regimesTrabalhos = new ArrayList<SelectItem>();
        situacoesFuncionais = new ArrayList<SelectItem>();
        paises = new ArrayList<SelectItem>();
        tipoFuncoes = new ArrayList<SelectItem>();
        listarBancos();
        listarEstados();
        listarUfs();
        listarLotacoes();
        listarPadroes();
        listarSituacoesFuncionais();
        listarFuncoes();
        listarCargos();
        listarRegimesTrabalhos();
        listarGrupoSanguineo();
        listarCorPele();
        listarEstadosCivis();
        Autenticacao siapeAutenticado = (Autenticacao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
        servidor.setSiape(siapeAutenticado.getSiape());
        servidor = ServidorDAO.getInstance().refreshBySiape(servidor);
        if (servidor.getCidadeNascimento() != null) {
            Cidade cidade = new Cidade();
            servidor.setEstadoNascimento(new Estado());
            cidade.setCodigo(servidor.getCidadeNascimento());
            cidade = (Cidade) DAO.getInstance().refresh(cidade);
            servidor.setEstadoNascimento(cidade.getEstado());
            servidor.setCidadeNascimento(cidade.getCodigo());
            listarCidadesNascimentoServidor();
        }
        if (servidor.getEndereco() != null && servidor.getEndereco().getCidade().getCodigo() != 0) {
            listarCidadesContato();
        }
        if ((new Long(104)).equals(servidor.getContaBancaria().getBanco().getCodigo())) {
            indPoupanca = true;
        } else {
            indPoupanca = false;
        }
        isFeminino();
        if (servidor.getIndEstrangeiro()) {
            listarPais();
            servidorEstrangeiro = Boolean.TRUE;
            servidorBrasileiro = Boolean.FALSE;
        } else {
            servidorEstrangeiro = Boolean.FALSE;
            servidorBrasileiro = Boolean.TRUE;
        }
        if (servidor.getDataUltimaAlteracao() != null) {
            this.setDataUltimaAlteracao(Validator.formatarDataHoraBR(servidor.getDataUltimaAlteracao()));
        }
        if (servidor.getDataUltimaAprovacao() != null) {
            this.setDataUltimaAprovacao(Validator.formatarDataHoraBR(servidor.getDataUltimaAprovacao()));
        }
        FacesContext.getCurrentInstance().getExternalContext().redirect("cadastrarServidor.jsp");
    }
}
