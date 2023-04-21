package br.com.edawir.controlador;

import java.util.ArrayList;
import java.util.List;
import javax.faces.event.ActionEvent;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import br.com.edawir.apresentacao.mensagem.ConstantesMensagens;
import br.com.edawir.apresentacao.util.CloneComSerializacao;
import br.com.edawir.apresentacao.util.UtilColecao;
import br.com.edawir.apresentacao.util.UtilNavegacao;
import br.com.edawir.context.AppContext;
import br.com.edawir.integracao.model.AgendaDisciplina;
import br.com.edawir.integracao.model.AreaDeAtuacao;
import br.com.edawir.integracao.model.Disciplina;
import br.com.edawir.integracao.model.EstruturaCurricular;
import br.com.edawir.integracao.model.Horario;
import br.com.edawir.integracao.model.Investimento;
import br.com.edawir.integracao.model.ItemDaEstruturaCurricular;
import br.com.edawir.integracao.model.ItemDoInvestimento;
import br.com.edawir.integracao.model.ItemQuadroDePessoa;
import br.com.edawir.integracao.model.Justificativa;
import br.com.edawir.integracao.model.Livro;
import br.com.edawir.integracao.model.Meta;
import br.com.edawir.integracao.model.MovimentacaoFinanceira;
import br.com.edawir.integracao.model.Projeto;
import br.com.edawir.integracao.model.QuadroDePessoa;
import br.com.edawir.negocio.servico.EstruturaCurricularServico;
import br.com.edawir.negocio.servico.InvestimentoServico;
import br.com.edawir.negocio.servico.JustificativaServico;
import br.com.edawir.negocio.servico.MetaServico;
import br.com.edawir.negocio.servico.MovimentacaoFinanceiraServico;
import br.com.edawir.negocio.servico.ProjetoServico;
import br.com.edawir.negocio.servico.QuadroDePessoaServico;

/**
 * 
 * Essa classe controla os Itens Do Projeto
 * 
 * @author Grupo EDAWIR
 * @since 02/12/2011
 */
@Scope("session")
@Controller("manterItensDoProjeto")
public class ManterItensDoProjetoBean extends BaseController {

    private static final int POSICAO_ESTRUTURA_CURRICULAR = 1;

    private static final int POSICAO_INVESTIMENTO = 2;

    private static final int POSICAO_JUSTIFICATIVA = 3;

    private static final int POSICAO_META = 4;

    private static final int POSICAO_MOVIMENTACAO_FINANCEIRA = 5;

    private static final int POSICAO_QUADRO_DE_PESSOA = 6;

    private static final String NAO_PREENCHIDO = "N�o Preenchido";

    private static final String PREENCHIDO_ANTIGO_NAO_SALVO = "Preenchido com dados antigos. Salve os dados antes de sair deste projeto !!!)";

    private static final String PREENCHIDO = "Preenchido";

    @Autowired
    private ProjetoServico projetoServico;

    @Autowired
    private EstruturaCurricularServico estruturaCurricularServico;

    @Autowired
    private InvestimentoServico investimentoServico;

    @Autowired
    private JustificativaServico justificativaServico;

    @Autowired
    private MetaServico metaServico;

    @Autowired
    private MovimentacaoFinanceiraServico movimentacaoFinanceiraServico;

    @Autowired
    private QuadroDePessoaServico quadroDePessoaServico;

    CloneComSerializacao clone = new CloneComSerializacao();

    private DataModel<Projeto> model;

    private DataModel<ItemDoProjeto> modelItemDoProjeto;

    private String botaoVoltar;

    private List<ItemDoProjeto> itensDoProjeto;

    private ApplicationContext ctx = AppContext.getApplicationContext();

    private boolean projetoImportado = false;

    /**
	 * M�todo que inicializa os itens do Projeto
	 * 
	 * @return UtilNavegacao.CADASTRAR_PROJETO comando para cadastrar
	 */
    public String initNovoProjeto() {
        try {
            setProjetoSessao(null);
            setEstruturaCurricularSessao(null);
            setInvestimentoSessao(null);
            setJustificativaSessao(null);
            setMetaSessao(null);
            setMovimentacaoFinanceiraSessao(null);
            setQuadroDePessoaSessao(null);
            setProjetoANTERIORSessao(null);
            setEstruturaCurricularANTERIORSessao(null);
            setInvestimentoANTERIORSessao(null);
            setJustificativaANTERIORSessao(null);
            setMetaANTERIORSessao(null);
            setMovimentacaoFinanceiraANTERIORSessao(null);
            setQuadroDePessoaANTERIORSessao(null);
            setBotaoVoltar("false");
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return UtilNavegacao.CADASTRAR_PROJETO;
    }

    /**
	 * M�todo que inicializa os itens do Projeto
	 * 
	 * @return UtilNavegacao.LISTAR_PROJETO comando para listar
	 */
    public String initListarProjeto() {
        try {
            setProjetoSessao(null);
            setEstruturaCurricularSessao(null);
            setInvestimentoSessao(null);
            setJustificativaSessao(null);
            setMetaSessao(null);
            setMovimentacaoFinanceiraSessao(null);
            setQuadroDePessoaSessao(null);
            setProjetoANTERIORSessao(null);
            setEstruturaCurricularANTERIORSessao(null);
            setInvestimentoANTERIORSessao(null);
            setJustificativaANTERIORSessao(null);
            setMetaANTERIORSessao(null);
            setMovimentacaoFinanceiraANTERIORSessao(null);
            setQuadroDePessoaANTERIORSessao(null);
            model = null;
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return UtilNavegacao.LISTAR_PROJETO;
    }

    /**
	 * M�todo que inicializa os itens do Projeto
	 * 
	 * @return UtilNavegacao.IMPORTAR_PROJETO comando para listar projetos a serem importados
	 */
    public String initImportarProjeto() {
        try {
            setProjetoSessao(null);
            setEstruturaCurricularSessao(null);
            setInvestimentoSessao(null);
            setJustificativaSessao(null);
            setMetaSessao(null);
            setMovimentacaoFinanceiraSessao(null);
            setQuadroDePessoaSessao(null);
            setProjetoANTERIORSessao(null);
            setEstruturaCurricularANTERIORSessao(null);
            setInvestimentoANTERIORSessao(null);
            setJustificativaANTERIORSessao(null);
            setMetaANTERIORSessao(null);
            setMovimentacaoFinanceiraANTERIORSessao(null);
            setQuadroDePessoaANTERIORSessao(null);
            model = null;
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return UtilNavegacao.IMPORTAR_PROJETO;
    }

    /**
	 * M�todo que inicializa a sess�o do Projeto anterior e de seus itens anteriores 
	 * com os dados das sess�es atuais
	 * 
	 * @return item.getAction() a��o do item
	 */
    public String vaiParaTela() {
        ItemDoProjeto item = (ItemDoProjeto) modelItemDoProjeto.getRowData();
        try {
            setProjetoANTERIORSessao((Projeto) clone.copy(getProjetoSessao()));
            setEstruturaCurricularANTERIORSessao((EstruturaCurricular) clone.copy(getEstruturaCurricularSessao()));
            setInvestimentoANTERIORSessao((Investimento) clone.copy(getInvestimentoSessao()));
            setJustificativaANTERIORSessao((Justificativa) clone.copy(getJustificativaSessao()));
            setMetaANTERIORSessao((Meta) clone.copy(getMetaSessao()));
            setMovimentacaoFinanceiraANTERIORSessao((MovimentacaoFinanceira) clone.copy(getMovimentacaoFinanceiraSessao()));
            setQuadroDePessoaANTERIORSessao((QuadroDePessoa) clone.copy(getQuadroDePessoaSessao()));
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return item.getAction();
    }

    /**
	 * M�todo que importa os dados de um projeto selecionado para um projeto novo
	 * 
	 * @return UtilNavegacao.CADASTRAR_PROJETO comando para cadastrar
	 */
    public String acaoImportar() {
        try {
            getProjetoFromModel();
            Long id = getProjetoSessao().getId();
            setEstruturaCurricularSessao(estruturaCurricularServico.obterPorIdDoProjeto(id));
            setInvestimentoSessao(investimentoServico.obterPorIdDoProjeto(id));
            setJustificativaSessao(justificativaServico.obterPorIdDoProjeto(id));
            setMetaSessao(metaServico.obterPorIdDoProjeto(id));
            setMovimentacaoFinanceiraSessao(movimentacaoFinanceiraServico.obterPorIdDoProjeto(id));
            setQuadroDePessoaSessao(quadroDePessoaServico.obterPorIdDoProjeto(id));
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
            return UtilNavegacao.IMPORTAR_PROJETO;
        }
        try {
            if (getProjetoSessao() != null) {
                getProjetoSessao().setId(null);
                getProjetoSessao().getTelefone().setId(null);
                ArrayList<Horario> array = new ArrayList<Horario>();
                Horario temp;
                for (Horario horario : getProjetoSessao().getHorarios()) {
                    temp = new Horario();
                    temp.setData(horario.getData());
                    temp.setHorarioDeInicio(horario.getHorarioDeInicio());
                    temp.setHorarioDeTermino(horario.getHorarioDeTermino());
                    temp.setVersao(0);
                    temp.setId(null);
                    array.add(temp);
                }
                getProjetoSessao().setHorarios(array);
            }
            if (getEstruturaCurricularSessao() != null) {
                getEstruturaCurricularSessao().setId(null);
                ArrayList<ItemDaEstruturaCurricular> array = new ArrayList<ItemDaEstruturaCurricular>();
                ItemDaEstruturaCurricular temp;
                for (ItemDaEstruturaCurricular item : getEstruturaCurricularSessao().getItensDaEstruturaCurricular()) {
                    temp = new ItemDaEstruturaCurricular();
                    ArrayList<AgendaDisciplina> array2 = new ArrayList<AgendaDisciplina>();
                    AgendaDisciplina temp2;
                    for (AgendaDisciplina item2 : item.getAgendaDisciplinas()) {
                        temp2 = new AgendaDisciplina();
                        temp2.setData(item2.getData());
                        temp2.setHorarioDeInicio(item2.getHorarioDeInicio());
                        temp2.setHorarioDeTermino(item2.getHorarioDeTermino());
                        temp2.setId(null);
                        temp2.setQtdHoraAula(item2.getQtdHoraAula());
                        temp2.setVersao(0);
                        array2.add(temp2);
                    }
                    temp.setAgendaDisciplinas(array2);
                    temp.setCargaHoraria(item.getCargaHoraria());
                    temp.setDisciplina(item.getDisciplina());
                    ArrayList<Livro> array3 = new ArrayList<Livro>();
                    Livro temp3;
                    for (Livro livro : item.getDisciplina().getBibliografia().getListaLivro()) {
                        temp3 = new Livro();
                        temp3.setAnoDeEdicao(livro.getAnoDeEdicao());
                        temp3.setAutor(livro.getAutor());
                        temp3.setEditora(livro.getEditora());
                        temp3.setId(null);
                        temp3.setIsbn(livro.getIsbn());
                        temp3.setNumeroDeEdicao(livro.getNumeroDeEdicao());
                        temp3.setTitulo(livro.getTitulo());
                        temp3.setVersao(0);
                        array3.add(temp3);
                    }
                    temp.getDisciplina().getBibliografia().setListaLivro(array3);
                    temp.setId(null);
                    temp.setProfessor(item.getProfessor());
                    temp.setVersao(0);
                    array.add(temp);
                }
                getEstruturaCurricularSessao().setItensDaEstruturaCurricular(array);
            }
            if (getInvestimentoSessao() != null) {
                getInvestimentoSessao().setId(null);
                ArrayList<ItemDoInvestimento> array = new ArrayList<ItemDoInvestimento>();
                ItemDoInvestimento temp;
                for (ItemDoInvestimento inv : getInvestimentoSessao().getInvestimentos()) {
                    temp = new ItemDoInvestimento();
                    temp.setId(null);
                    temp.setItem(inv.getItem());
                    temp.setQtd(inv.getQtd());
                    temp.setSomaTotal(inv.getSomaTotal());
                    temp.setTipoDeInvestimentoEnum(inv.getTipoDeInvestimentoEnum());
                    temp.setTotal(inv.getTotal());
                    temp.setValor(inv.getValor());
                    temp.setVersao(0);
                    array.add(temp);
                }
                getInvestimentoSessao().setInvestimentos(array);
            }
            if (getJustificativaSessao() != null) {
                getJustificativaSessao().setId(null);
            }
            if (getMetaSessao() != null) {
                getMetaSessao().setId(null);
            }
            if (getMovimentacaoFinanceiraSessao() != null) {
                getMovimentacaoFinanceiraSessao().setId(null);
                getMovimentacaoFinanceiraSessao().getConta().setId(null);
                getMovimentacaoFinanceiraSessao().getConta().getAgencia().setId(null);
                getMovimentacaoFinanceiraSessao().getConta().getAgencia().getBanco().setId(null);
                getMovimentacaoFinanceiraSessao().getCentroDeCusto().setId(null);
                getMovimentacaoFinanceiraSessao().getCentroDeCusto().getAgencia().setId(null);
                getMovimentacaoFinanceiraSessao().getCentroDeCusto().getAgencia().getBanco().setId(null);
            }
            if (getQuadroDePessoaSessao() != null) {
                getQuadroDePessoaSessao().setId(null);
                ArrayList<ItemQuadroDePessoa> array = new ArrayList<ItemQuadroDePessoa>();
                ItemQuadroDePessoa temp;
                for (ItemQuadroDePessoa item : getQuadroDePessoaSessao().getListaQuadroDePessoa()) {
                    temp = new ItemQuadroDePessoa();
                    temp.setCargaHoraria(item.getCargaHoraria());
                    temp.setFuncionario(item.getFuncionario());
                    temp.setId(null);
                    temp.setObservacao(item.getObservacao());
                    temp.setTotalHoraExtra(item.getTotalHoraExtra());
                    temp.setValorDaHoraAula(item.getValorDaHoraAula());
                    temp.setVersao(0);
                    array.add(temp);
                }
                getQuadroDePessoaSessao().setListaQuadroDePessoa(array);
            }
            setProjetoImportado(true);
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return UtilNavegacao.CADASTRAR_PROJETO;
    }

    /**
	 * M�todo que edita os itens do projeto
	 * 
	 * @return UtilNavegacao.TELA_ITENS_DO_PROJETO comando para listar
	 */
    public String acaoEditar() {
        try {
            getProjetoFromModel();
            Long id = getProjetoSessao().getId();
            setEstruturaCurricularSessao(estruturaCurricularServico.obterPorIdDoProjeto(id));
            setInvestimentoSessao(investimentoServico.obterPorIdDoProjeto(id));
            setJustificativaSessao(justificativaServico.obterPorIdDoProjeto(id));
            setMetaSessao(metaServico.obterPorIdDoProjeto(id));
            setMovimentacaoFinanceiraSessao(movimentacaoFinanceiraServico.obterPorIdDoProjeto(id));
            setQuadroDePessoaSessao(quadroDePessoaServico.obterPorIdDoProjeto(id));
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
            return UtilNavegacao.LISTAR_PROJETO;
        }
        return UtilNavegacao.TELA_ITENS_DO_PROJETO;
    }

    /**
	 * M�todo que atualiza o Projeto pelo objeto selecionado da lista de
	 * Projeto.
	 */
    private void getProjetoFromModel() {
        Projeto proj = (Projeto) model.getRowData();
        setProjetoSessao(proj);
    }

    /**
	 * M�todo que exclui o registro da base de dados.
	 * 
	 * @param event a��o do evento
	 */
    public void acaoExcluir(ActionEvent event) {
        try {
            getProjetoFromModel();
            Long id = getProjetoSessao().getId();
            EstruturaCurricular estrutura = estruturaCurricularServico.obterPorIdDoProjeto(id);
            if (estrutura != null) estruturaCurricularServico.excluir(estrutura);
            Investimento inv = investimentoServico.obterPorIdDoProjeto(id);
            if (inv != null) investimentoServico.excluir(inv);
            Justificativa jus = justificativaServico.obterPorIdDoProjeto(id);
            if (jus != null) justificativaServico.excluir(jus);
            Meta meta = metaServico.obterPorIdDoProjeto(id);
            if (meta != null) metaServico.excluir(meta);
            MovimentacaoFinanceira mov = movimentacaoFinanceiraServico.obterPorIdDoProjeto(id);
            if (mov != null) movimentacaoFinanceiraServico.excluir(mov);
            QuadroDePessoa quadro = quadroDePessoaServico.obterPorIdDoProjeto(id);
            if (quadro != null) quadroDePessoaServico.excluir(quadro);
            projetoServico.excluir(getProjetoSessao());
            atualizarListaProjeto();
            adicionarMensagemSucesso(ConstantesMensagens.REGISTRO_EXCLUIDO);
        } catch (DataIntegrityViolationException e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getRootCause().getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    /**
	 * M�todo que atualiza a lista de Projeto.
	 */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void atualizarListaProjeto() {
        this.model = new ListDataModel(listarProjeto());
    }

    /**
	 * M�todo que lista todos os Projetos cadastradas na base de dados.
	 * 
	 * @return List<Projeto> lista de Projeto
	 */
    private List<Projeto> listarProjeto() {
        try {
            List<Projeto> lista = projetoServico.listarTodos("nome");
            if (UtilColecao.isColecaoVazia(lista)) {
                return null;
            }
            return lista;
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
	 * M�todo que atualiza o estado de cada item como: PREENCHIDO, NAO_PREENCHIDO ou
	 * PREENCHIDO_ANTIGO_NAO_SALVO
	 */
    private void verificaOEstadoDeCadaItem() {
        try {
            if (getEstruturaCurricularSessao() == null || getEstruturaCurricularSessao().getProjeto() == null || getEstruturaCurricularSessao().getItensDaEstruturaCurricular().size() < 1) {
                itensDoProjeto.get(POSICAO_ESTRUTURA_CURRICULAR).setEstado(NAO_PREENCHIDO);
            } else {
                if (getEstruturaCurricularSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_ESTRUTURA_CURRICULAR).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_ESTRUTURA_CURRICULAR).setEstado(PREENCHIDO);
                }
            }
            if (getInvestimentoSessao() == null || getInvestimentoSessao().getProjeto() == null || getInvestimentoSessao().getInvestimentos().size() < 1) {
                itensDoProjeto.get(POSICAO_INVESTIMENTO).setEstado(NAO_PREENCHIDO);
            } else {
                if (getInvestimentoSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_INVESTIMENTO).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_INVESTIMENTO).setEstado(PREENCHIDO);
                }
            }
            if (getJustificativaSessao() == null || getJustificativaSessao().getProjeto() == null) {
                itensDoProjeto.get(POSICAO_JUSTIFICATIVA).setEstado(NAO_PREENCHIDO);
            } else {
                if (getJustificativaSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_JUSTIFICATIVA).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_JUSTIFICATIVA).setEstado(PREENCHIDO);
                }
            }
            if (getMetaSessao() == null || getMetaSessao().getProjeto() == null) {
                itensDoProjeto.get(POSICAO_META).setEstado(NAO_PREENCHIDO);
            } else {
                if (getMetaSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_META).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_META).setEstado(PREENCHIDO);
                }
            }
            if (getMovimentacaoFinanceiraSessao() == null || getMovimentacaoFinanceiraSessao().getProjeto() == null) {
                itensDoProjeto.get(POSICAO_MOVIMENTACAO_FINANCEIRA).setEstado(NAO_PREENCHIDO);
            } else {
                if (getMovimentacaoFinanceiraSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_MOVIMENTACAO_FINANCEIRA).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_MOVIMENTACAO_FINANCEIRA).setEstado(PREENCHIDO);
                }
            }
            if (getQuadroDePessoaSessao() == null || getQuadroDePessoaSessao().getProjeto() == null || getQuadroDePessoaSessao().getListaQuadroDePessoa().size() < 1) {
                itensDoProjeto.get(POSICAO_QUADRO_DE_PESSOA).setEstado(NAO_PREENCHIDO);
            } else {
                if (getQuadroDePessoaSessao().getProjeto().getId().longValue() != getProjetoSessao().getId().longValue()) {
                    itensDoProjeto.get(POSICAO_QUADRO_DE_PESSOA).setEstado(PREENCHIDO_ANTIGO_NAO_SALVO);
                } else {
                    itensDoProjeto.get(POSICAO_QUADRO_DE_PESSOA).setEstado(PREENCHIDO);
                }
            }
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
    }

    /**
     * @return itensDoProjeto os itens Do Projeto
     */
    public List<ItemDoProjeto> getItensDoProjeto() {
        try {
            if (itensDoProjeto == null) {
                itensDoProjeto = new ArrayList<ItemDoProjeto>();
                ItemDoProjeto item = new ItemDoProjeto();
                item.setNome("Dados do Projeto");
                item.setEstado(PREENCHIDO);
                item.setAction(UtilNavegacao.CADASTRAR_PROJETO);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Estrutura Curricular");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_ESTRUTURA_CURRICULAR);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Investimento");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_INVESTIMENTO);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Justificativa");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_JUSTIFICATIVA);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Meta");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_META);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Movimenta��o Financeira");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_MOVIMENTACAO_FINANCEIRA);
                itensDoProjeto.add(item);
                item = new ItemDoProjeto();
                item.setNome("Quadro de Pessoa");
                item.setEstado("");
                item.setAction(UtilNavegacao.CADASTRAR_QUADRO_DE_PESSOA);
                itensDoProjeto.add(item);
            }
            verificaOEstadoDeCadaItem();
        } catch (Exception e) {
            adicionarMensagemErro(ConstantesMensagens.OPERACAO_NAO_REALIZADA);
            adicionarMensagemErro(e.getCause().getMessage());
            e.printStackTrace();
        }
        return itensDoProjeto;
    }

    /**
     * @return EstruturaCurricular a Estrutura Curricular
     */
    public EstruturaCurricular getEstruturaCurricularSessao() {
        ManterEstruturaCurricularBean mpb = ctx.getBean(ManterEstruturaCurricularBean.class);
        return mpb.getEstruturaCurricular();
    }

    /**
	 * @param est a estrutura Curricular � atualizado/a
	 */
    public void setEstruturaCurricularSessao(EstruturaCurricular est) {
        ManterEstruturaCurricularBean mpb = ctx.getBean(ManterEstruturaCurricularBean.class);
        mpb.setEstruturaCurricular(est);
    }

    /**
     * @return Investimento o Investimento
     */
    public Investimento getInvestimentoSessao() {
        ManterInvestimentoBean mpb = ctx.getBean(ManterInvestimentoBean.class);
        return mpb.getInvestimento();
    }

    /**
	 * @param inv o Investimento � atualizado/a
	 */
    public void setInvestimentoSessao(Investimento inv) {
        ManterInvestimentoBean mpb = ctx.getBean(ManterInvestimentoBean.class);
        mpb.setInvestimento(inv);
    }

    /**
     * @return Justificativa a Justificativa
     */
    public Justificativa getJustificativaSessao() {
        ManterJustificativaBean mpb = ctx.getBean(ManterJustificativaBean.class);
        return mpb.getJustificativa();
    }

    /**
	 * @param jus a Justificativa � atualizado/a
	 */
    public void setJustificativaSessao(Justificativa jus) {
        ManterJustificativaBean mpb = ctx.getBean(ManterJustificativaBean.class);
        mpb.setJustificativa(jus);
    }

    /**
     * @return Meta a Meta
     */
    public Meta getMetaSessao() {
        ManterMetaBean mpb = ctx.getBean(ManterMetaBean.class);
        return mpb.getMeta();
    }

    /**
	 * @param meta a Meta � atualizado/a
	 */
    public void setMetaSessao(Meta meta) {
        ManterMetaBean mpb = ctx.getBean(ManterMetaBean.class);
        mpb.setMeta(meta);
    }

    /**
     * @return MovimentacaoFinanceira a Movimentacao Financeira
     */
    public MovimentacaoFinanceira getMovimentacaoFinanceiraSessao() {
        ManterMovimentacaoFinanceiraBean mpb = ctx.getBean(ManterMovimentacaoFinanceiraBean.class);
        return mpb.getMovimentacaoFinanceira();
    }

    /**
	 * @param mov a Movimentacao Financeira � atualizado/a
	 */
    public void setMovimentacaoFinanceiraSessao(MovimentacaoFinanceira mov) {
        ManterMovimentacaoFinanceiraBean mpb = ctx.getBean(ManterMovimentacaoFinanceiraBean.class);
        mpb.setMovimentacaoFinanceira(mov);
    }

    /**
     * @return QuadroDePessoa o Quadro De Pessoa
     */
    public QuadroDePessoa getQuadroDePessoaSessao() {
        ManterQuadroDePessoaBean mpb = ctx.getBean(ManterQuadroDePessoaBean.class);
        return mpb.getQuadroDePessoa();
    }

    /**
	 * @param quadro o Quadro De Pessoa � atualizado/a
	 */
    public void setQuadroDePessoaSessao(QuadroDePessoa quadro) {
        ManterQuadroDePessoaBean mpb = ctx.getBean(ManterQuadroDePessoaBean.class);
        mpb.setQuadroDePessoa(quadro);
    }

    /**
	 * @param proj o Projeto � atualizado/a
	 */
    public void setProjetoSessao(Projeto proj) {
        ManterProjetoBean mpb = ctx.getBean(ManterProjetoBean.class);
        mpb.setProjeto(proj);
    }

    /**
     * @return ProjetoANTERIOR o Projeto ANTERIOR
     */
    public Projeto getProjetoANTERIORSessao() {
        ManterProjetoBean mpb = ctx.getBean(ManterProjetoBean.class);
        return mpb.getProjetoANTERIOR();
    }

    /**
	 * @param proj o Projeto ANTERIOR � atualizado/a
	 */
    public void setProjetoANTERIORSessao(Projeto proj) {
        ManterProjetoBean mpb = ctx.getBean(ManterProjetoBean.class);
        mpb.setProjetoANTERIOR(proj);
    }

    /**
     * @return EstruturaCurricularANTERIOR a Estrutura Curricular ANTERIOR
     */
    public EstruturaCurricular getEstruturaCurricularANTERIORSessao() {
        ManterEstruturaCurricularBean mpb = ctx.getBean(ManterEstruturaCurricularBean.class);
        return mpb.getEstruturaCurricularANTERIOR();
    }

    /**
	 * @param est a Estrutura Curricular ANTERIOR � atualizado/a
	 */
    public void setEstruturaCurricularANTERIORSessao(EstruturaCurricular est) {
        ManterEstruturaCurricularBean mpb = ctx.getBean(ManterEstruturaCurricularBean.class);
        mpb.setEstruturaCurricularANTERIOR(est);
    }

    /**
     * @return InvestimentoANTERIOR o Investimento ANTERIOR
     */
    public Investimento getInvestimentoANTERIORSessao() {
        ManterInvestimentoBean mpb = ctx.getBean(ManterInvestimentoBean.class);
        return mpb.getInvestimentoANTERIOR();
    }

    /**
	 * @param inv o Investimento ANTERIOR � atualizado/a
	 */
    public void setInvestimentoANTERIORSessao(Investimento inv) {
        ManterInvestimentoBean mpb = ctx.getBean(ManterInvestimentoBean.class);
        mpb.setInvestimentoANTERIOR(inv);
    }

    /**
     * @return JustificativaANTERIOR a Justificativa ANTERIOR
     */
    public Justificativa getJustificativaANTERIORSessao() {
        ManterJustificativaBean mpb = ctx.getBean(ManterJustificativaBean.class);
        return mpb.getJustificativaANTERIOR();
    }

    /**
	 * @param jus a Justificativa ANTERIOR � atualizado/a
	 */
    public void setJustificativaANTERIORSessao(Justificativa jus) {
        ManterJustificativaBean mpb = ctx.getBean(ManterJustificativaBean.class);
        mpb.setJustificativaANTERIOR(jus);
    }

    /**
     * @return MetaANTERIOR a Meta ANTERIOR
     */
    public Meta getMetaANTERIORSessao() {
        ManterMetaBean mpb = ctx.getBean(ManterMetaBean.class);
        return mpb.getMetaANTERIOR();
    }

    /**
	 * @param meta a Meta ANTERIOR � atualizado/a
	 */
    public void setMetaANTERIORSessao(Meta meta) {
        ManterMetaBean mpb = ctx.getBean(ManterMetaBean.class);
        mpb.setMetaANTERIOR(meta);
    }

    /**
     * @return MovimentacaoFinanceiraANTERIOR a Movimentacao Financeira ANTERIOR
     */
    public MovimentacaoFinanceira getMovimentacaoFinanceiraANTERIORSessao() {
        ManterMovimentacaoFinanceiraBean mpb = ctx.getBean(ManterMovimentacaoFinanceiraBean.class);
        return mpb.getMovimentacaoFinanceiraANTERIOR();
    }

    /**
	 * @param mov a Movimentacao Financeira ANTERIOR � atualizado/a
	 */
    public void setMovimentacaoFinanceiraANTERIORSessao(MovimentacaoFinanceira mov) {
        ManterMovimentacaoFinanceiraBean mpb = ctx.getBean(ManterMovimentacaoFinanceiraBean.class);
        mpb.setMovimentacaoFinanceiraANTERIOR(mov);
    }

    /**
     * @return QuadroDePessoaANTERIOR o Quadro De Pessoa ANTERIOR
     */
    public QuadroDePessoa getQuadroDePessoaANTERIORSessao() {
        ManterQuadroDePessoaBean mpb = ctx.getBean(ManterQuadroDePessoaBean.class);
        return mpb.getQuadroDePessoaANTERIOR();
    }

    /**
	 * @param quadro o Quadro De Pessoa ANTERIOR � atualizado/a
	 */
    public void setQuadroDePessoaANTERIORSessao(QuadroDePessoa quadro) {
        ManterQuadroDePessoaBean mpb = ctx.getBean(ManterQuadroDePessoaBean.class);
        mpb.setQuadroDePessoaANTERIOR(quadro);
    }

    /**
     * @return botaoVoltar o botaoVoltar
     */
    public String getBotaoVoltar() {
        return botaoVoltar;
    }

    /**
	 * @param botaoVoltar o botaoVoltar � atualizado/a
	 */
    public void setBotaoVoltar(String botaoVoltar) {
        this.botaoVoltar = botaoVoltar;
    }

    /**
     * @return DataModel<Projeto> lista de Projeto
     */
    public DataModel<Projeto> getModel() {
        model = new ListDataModel<Projeto>(listarProjeto());
        return model;
    }

    /**
	 * @param model lista de Projeto � atualizado/a
	 */
    public void setModel(DataModel<Projeto> model) {
        this.model = model;
    }

    /**
     * @return DataModel<ItemDoProjeto> lista de Itens Do Projeto
     */
    public DataModel<ItemDoProjeto> getModelItemDoProjeto() {
        modelItemDoProjeto = new ListDataModel<ItemDoProjeto>(getItensDoProjeto());
        botaoVoltar = "true";
        return modelItemDoProjeto;
    }

    /**
	 * @param modelItemDoProjeto lista de Itens Do Projeto � atualizado/a
	 */
    public void setModelItemDoProjeto(DataModel<ItemDoProjeto> modelItemDoProjeto) {
        this.modelItemDoProjeto = modelItemDoProjeto;
    }

    /**
	 * @return projetoImportado verdadeiro ou falso
	 */
    public boolean isProjetoImportado() {
        return projetoImportado;
    }

    /**
	 * @param projetoImportado o projetoImportado � atualizado/a
	 */
    public void setProjetoImportado(boolean projetoImportado) {
        this.projetoImportado = projetoImportado;
    }
}
