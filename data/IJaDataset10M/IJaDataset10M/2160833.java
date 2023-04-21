package br.ufrj.cad.model.planilha;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import br.ufrj.cad.fwk.exception.BaseRuntimeException;
import br.ufrj.cad.fwk.exception.Notification;
import br.ufrj.cad.fwk.model.BaseService;
import br.ufrj.cad.fwk.model.ObjetoPersistente;
import br.ufrj.cad.fwk.model.Transaction;
import br.ufrj.cad.fwk.model.TransactionReadOnly;
import br.ufrj.cad.fwk.util.ResourceUtil;
import br.ufrj.cad.fwk.util.ValidacaoUtils;
import br.ufrj.cad.model.bo.AnoBase;
import br.ufrj.cad.model.bo.Departamento;
import br.ufrj.cad.model.bo.ItemLista;
import br.ufrj.cad.model.bo.ItemPlanilha;
import br.ufrj.cad.model.bo.ItemPlanilhaUsuario;
import br.ufrj.cad.model.bo.Lista;
import br.ufrj.cad.model.bo.Planilha;
import br.ufrj.cad.model.bo.Usuario;
import br.ufrj.cad.model.disciplina.DisciplinaService;
import br.ufrj.cad.model.to.ItemPlanilhaTO;
import br.ufrj.cad.model.to.PlanilhaTO;

public class PlanilhaService extends BaseService {

    private static PlanilhaService _instance;

    private static Logger logger = Logger.getLogger(PlanilhaService.class);

    public static synchronized PlanilhaService getInstance() {
        if (_instance == null) {
            _instance = new PlanilhaService();
        }
        return _instance;
    }

    @SuppressWarnings("unchecked")
    public List<Planilha> obtemListaPlanilhas(ObjetoPersistente departamentoPadrao) {
        return (List<Planilha>) transactionTemplate.execute(getTransactionObtemListaPlanilhas(departamentoPadrao));
    }

    private Transaction getTransactionObtemListaPlanilhas(final ObjetoPersistente departamentoPadrao) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (List) PlanilhaDAO.obtemListaPlanilhas(departamentoPadrao, session);
            }
        };
    }

    public Planilha salvaPlanilha(Planilha planilha, Departamento departamento, boolean edicao) {
        planilha.setDepartamento(departamento);
        if (edicao) {
            return alteraPlanilha(planilha, departamento);
        } else {
            return inserePlanilha(planilha, departamento);
        }
    }

    private Planilha alteraPlanilha(Planilha planilha, Departamento departamento) {
        if (!planilha.getDepartamento().equals(departamento)) {
            throw new RuntimeException("planilha.outro.departamento");
        }
        Planilha planilhaCompleta = obtemPlanilhaPorIdComItens(planilha.getId(), departamento);
        planilha.setFormula20Horas(planilhaCompleta.getFormula20Horas());
        planilha.setFormula40Horas(planilhaCompleta.getFormula40Horas());
        planilha.setItens(planilhaCompleta.getItens());
        return (Planilha) transactionTemplate.execute(getTransactionAlteraPlanilha(planilha));
    }

    private Transaction getTransactionAlteraPlanilha(final Planilha planilha) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (Planilha) PlanilhaDAO.alteraPlanilha(planilha, session);
            }
        };
    }

    private Planilha inserePlanilha(Planilha planilha, Departamento departamento) {
        if (anoBaseJaPossuiPlanilha(planilha.getAnoBase(), departamento)) {
            throw new BaseRuntimeException("anobase.ja.possui.planilha");
        }
        ajustaFilhos(planilha);
        return (Planilha) transactionTemplate.execute(getTransactionSalvaPanilha(planilha));
    }

    private void ajustaFilhos(Planilha planilha) {
        if (!ObjectUtils.equals(planilha.getItens(), null) && !planilha.getItens().isEmpty()) {
            for (ItemPlanilha item : planilha.getItens()) {
                item.setPlanilha(planilha);
                item.setId(null);
                atualizaFilhos(item, planilha);
            }
        }
    }

    private void atualizaFilhos(ItemPlanilha item, Planilha planilha) {
        if (!ObjectUtils.equals(item.getFilhos(), null) && !item.getFilhos().isEmpty()) {
            for (ItemPlanilha itemFilho : item.getFilhos()) {
                itemFilho.setId(null);
                itemFilho.setItemPai(item);
                itemFilho.setPlanilha(planilha);
                atualizaFilhos(itemFilho, planilha);
            }
        }
    }

    private boolean anoBaseJaPossuiPlanilha(AnoBase anoBase, Departamento departamento) {
        AnoBase anoBaseNoBanco = DisciplinaService.getInstance().obtemAnoBasePorId(anoBase.getId(), departamento);
        List<Planilha> planilhas = obtemPLanilhasPorAno(anoBaseNoBanco, departamento);
        return (planilhas.size() > 0);
    }

    @SuppressWarnings("unchecked")
    private List<Planilha> obtemPLanilhasPorAno(AnoBase valorAnoBase, Departamento departamento) {
        valorAnoBase.setDepartamento(departamento);
        return (List<Planilha>) transactionTemplate.execute(getTransactionObtemPlanilhasPorAno(valorAnoBase));
    }

    private Transaction getTransactionObtemPlanilhasPorAno(final AnoBase valorAnoBase) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (List) PlanilhaDAO.obtemPlanilhasPorAno(valorAnoBase, session);
            }
        };
    }

    private Transaction getTransactionSalvaPanilha(final Planilha planilha) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (Planilha) PlanilhaDAO.salvaPlanilha(planilha, session);
            }
        };
    }

    public Planilha obtemPlanilhaPorId(Long planilhaId, Departamento departamento) {
        if (ObjectUtils.equals(planilhaId, null)) {
            throw new RuntimeException("planilha.id.nulo");
        }
        Planilha planilha = new Planilha(planilhaId);
        planilha.setDepartamento(departamento);
        Planilha planilhaNoBanco = (Planilha) transactionTemplate.execute(getTransactionObtemPlanilhaPorId(planilhaId));
        if (ObjectUtils.equals(planilhaNoBanco, null)) {
            throw new RuntimeException("nao existe Planilha com ID: " + planilhaId);
        }
        if (!planilhaNoBanco.getDepartamento().equals(departamento)) {
            throw new RuntimeException("planilha.outro.departamento");
        }
        return planilhaNoBanco;
    }

    private Transaction getTransactionObtemPlanilhaPorId(final Long planilhaId) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (Planilha) PlanilhaDAO.obtemPlanilhaPorId(planilhaId, session);
            }
        };
    }

    public Planilha obtemPlanilhaPorIdComItens(Long planilhaId, Departamento departamento) {
        Planilha planilha = obtemPlanilhaPorId(planilhaId, departamento);
        Planilha planilhaComItens = preencheItensPlanilha(planilha);
        return planilhaComItens;
    }

    private Planilha preencheItensPlanilha(Planilha planilha) {
        return (Planilha) transactionTemplate.execute(getTransactionPreencheItensPlanilha(planilha));
    }

    private Transaction getTransactionPreencheItensPlanilha(final Planilha planilha) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                Planilha planilhaComItens = PlanilhaDAO.caregaPlanilhaCompleta(planilha, session);
                return planilhaComItens;
            }
        };
    }

    public Planilha criaNovaPlanilha(Departamento departamento, Long idPlanilhaReferencia) {
        AnoBase anoBaseAtual = DisciplinaService.getInstance().obtemAnoBaseCorrente(departamento);
        Planilha novaPlanilha = new Planilha();
        novaPlanilha.setAnoBase(anoBaseAtual);
        if (!ObjectUtils.equals(idPlanilhaReferencia, null)) {
            return salvaPlanilhaBaseadaEmPlanilhaAnterior(departamento, idPlanilhaReferencia, novaPlanilha);
        } else {
            return salvaPlanilhaSemBaseEmNenhumaOutra(departamento, novaPlanilha);
        }
    }

    /**
 * 
 * Cria uma planilha vazia, sem basear-se em nenuma outra
 * 
 * @param departamento
 * @param novaPlanilha
 * @return
 */
    private Planilha salvaPlanilhaSemBaseEmNenhumaOutra(Departamento departamento, Planilha novaPlanilha) {
        ItemPlanilha novoItem = new ItemPlanilha();
        novoItem.setTipo(ItemPlanilha.TIPO_OUTROS);
        novoItem.setDescricao("Item Adicional");
        novoItem.setLista(null);
        novoItem.setPlanilha(novaPlanilha);
        novaPlanilha.setItens(new ArrayList<ItemPlanilha>());
        novaPlanilha.getItens().add(novoItem);
        return salvaPlanilha(novaPlanilha, departamento, false);
    }

    private Planilha salvaPlanilhaBaseadaEmPlanilhaAnterior(Departamento departamento, Long idPlanilhaReferencia, Planilha novaPlanilha) {
        Planilha planilhaReferencia = obtemPlanilhaPorIdComItens(idPlanilhaReferencia, departamento);
        novaPlanilha.setId(null);
        if (!ObjectUtils.equals(planilhaReferencia.getItens(), null) && !planilhaReferencia.getItens().isEmpty()) {
            List<ItemPlanilha> itens = new ArrayList<ItemPlanilha>();
            for (ItemPlanilha item : planilhaReferencia.getItens()) {
                itens.add(item);
            }
            novaPlanilha.setItens(itens);
        }
        novaPlanilha.setObservacao(planilhaReferencia.getObservacao());
        return salvaPlanilha(novaPlanilha, departamento, false);
    }

    public ItemPlanilha obtemItemPlanilhaPorIdComItensFilhos(Long itemPlanilhaId, ObjetoPersistente departamento) {
        verificaItemPlanilhaIdNulo(itemPlanilhaId);
        ItemPlanilha itemSemFilhos = obtemItemPlanilhaPorId(itemPlanilhaId);
        ItemPlanilha itemPlanilhaComFilhos = (ItemPlanilha) transactionTemplate.execute(getTransactionObtemItemPlanilhaPorIdComItensFilhos(itemSemFilhos));
        return itemPlanilhaComFilhos;
    }

    private void verificaItemPertenceDepartamentoCorreto(ObjetoPersistente departamento, ItemPlanilha item) {
        if (!item.getPlanilha().getDepartamento().equals(departamento)) {
            throw new RuntimeException("itemplanilha.outro.departamento");
        }
    }

    private void verificaItemPlanilhaIdNulo(Long itemPlanilhaId) {
        if (itemPlanilhaId == null) {
            throw new RuntimeException("itemplanilha.id == null");
        }
    }

    private Transaction getTransactionObtemItemPlanilhaPorIdComItensFilhos(final ItemPlanilha filtro) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                ItemPlanilha item = PlanilhaDAO.obtemItemPlanilhaPorId(filtro, session);
                Hibernate.initialize(item.getFilhos());
                return item;
            }
        };
    }

    public ItemPlanilha obtemItemPlanilhaPorId(Long idItemPlanilha, ObjetoPersistente departamento) {
        verificaItemPlanilhaIdNulo(idItemPlanilha);
        ItemPlanilha itemPlanilha = obtemItemPlanilhaPorId(idItemPlanilha);
        verificaItemPertenceDepartamentoCorreto(departamento, itemPlanilha);
        return itemPlanilha;
    }

    private ItemPlanilha obtemItemPlanilhaPorId(Long idItemPlanilha) {
        ItemPlanilha filtro = new ItemPlanilha();
        filtro.setId(idItemPlanilha);
        return (ItemPlanilha) transactionTemplate.execute(getTransactionObtemItemPlanilhaPorId(filtro));
    }

    private Transaction getTransactionObtemItemPlanilhaPorId(final ItemPlanilha filtro) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return PlanilhaDAO.obtemItemPlanilhaPorId(filtro, session);
            }
        };
    }

    public ItemPlanilha salvaItemPlanilha(ItemPlanilhaTO itemPlanilhaTO, Departamento departamento, boolean edicao) {
        ItemPlanilha itemPlanilha = new ItemPlanilha(itemPlanilhaTO);
        Long idPlanilha = null;
        if (edicao) {
            idPlanilha = obtemItemPlanilhaPorId(itemPlanilha.getId()).getPlanilha().getId();
        } else {
            idPlanilha = itemPlanilha.getPlanilha().getId();
        }
        Planilha planilha = obtemPlanilhaPorId(idPlanilha, departamento);
        if (edicao) {
            ItemPlanilha itemPlanilhaDoBanco = PlanilhaService.getInstance().obtemItemPlanilhaPorId(itemPlanilha.getId(), departamento);
            itemPlanilha.setXqueryLattesComprovacao(itemPlanilhaDoBanco.getXqueryLattesComprovacao());
        }
        if (!planilha.isEditavel()) {
            throw new RuntimeException("planilha.nao.editavel." + planilha.getAnoBase().getValorAnoBase());
        }
        Notification erros = new Notification();
        ValidacaoUtils.validaCampoObrigatorio(erros, itemPlanilha.getDescricao(), new Object[] { "_nls.mensagem.itemplanilha.atributo.descricao" });
        ValidacaoUtils.validaCampoObrigatorio(erros, itemPlanilha.getTipo(), new Object[] { "_nls.mensagem.itemplanilha.atributo.tipo" });
        if (!itemPlanilha.isBoleano()) {
            verificaPesoMaximoMaiorQuePeso(itemPlanilha, erros);
        }
        if (itemPlanilha.isTipoLista()) {
            ValidacaoUtils.validaCampoObrigatorio(erros, itemPlanilha.getLista().getId(), new Object[] { "_nls.mensagem.itemplanilha.atributo.lista" });
            if (!ObjectUtils.equals(itemPlanilha.getLista().getId(), null)) {
                obtemListaPorId(itemPlanilha.getLista().getId(), departamento);
            }
        } else {
            itemPlanilha.setLista(null);
        }
        if (itemPlanilha.isBoleano()) {
            ValidacaoUtils.validaCampoObrigatorio(erros, itemPlanilha.getPeso(), new Object[] { "_nls.mensagem.itemplanilha.atributo.peso" });
            itemPlanilha.setPesoMaximo(ItemPlanilha.PESO_INFINITO);
        }
        if (erros.size() > 0) {
            throw new BaseRuntimeException(erros);
        }
        if (ObjectUtils.equals(itemPlanilha.getPeso(), null)) {
            itemPlanilha.setPeso(ItemPlanilha.PESO_INFINITO);
        }
        if (ObjectUtils.equals(itemPlanilha.getPesoMaximo(), null)) {
            itemPlanilha.setPesoMaximo(ItemPlanilha.PESO_INFINITO);
        }
        verificaItemPai(departamento, itemPlanilha);
        if (edicao) {
            return alteraItemPlanilha(departamento, itemPlanilha);
        } else {
            return insereItemPlanilha(itemPlanilha, departamento);
        }
    }

    private void verificaPesoMaximoMaiorQuePeso(ItemPlanilha itemPlanilha, Notification erros) {
        Integer pesoMaximo = itemPlanilha.getPesoMaximo();
        Integer peso = itemPlanilha.getPeso();
        if (!ObjectUtils.equals(pesoMaximo, null) && !ObjectUtils.equals(peso, null) && pesoMaximo < peso) {
            erros.addEvent("itemplanilha.peso.maximo.menor.peso");
        }
    }

    private ItemPlanilha alteraItemPlanilha(ObjetoPersistente departamento, ItemPlanilha itemPlanilha) {
        verificaItemPlanilhaIdNulo(itemPlanilha.getId());
        ItemPlanilha itemNoBanco = obtemItemPlanilhaPorId(itemPlanilha.getId());
        verificaItemPertenceDepartamentoCorreto(departamento, itemNoBanco);
        if (!itemNoBanco.getTipo().equals(itemPlanilha.getTipo()) && itemNoBanco.getPlanilha().jaPreenchidaPorAlguem()) {
            throw new BaseRuntimeException("impossivel.alterar.tipo.quando.planilha.ja.foi.preenchida");
        }
        return (ItemPlanilha) transactionTemplate.execute(getTransactionAlteraItemPlanilha(itemPlanilha));
    }

    private ItemPlanilha insereItemPlanilha(ItemPlanilha itemPlanilha, Departamento departamento) {
        Planilha planilha = obtemPlanilhaPorId(itemPlanilha.getPlanilha().getId(), departamento);
        List<Usuario> usuariosComPLanilhaPreenchida = planilha.getUsuariosComPLanilhaPreenchida();
        return (ItemPlanilha) transactionTemplate.execute(getTransactionInsereItemPlanilha(itemPlanilha, usuariosComPLanilhaPreenchida));
    }

    private Transaction getTransactionInsereItemPlanilha(final ItemPlanilha itemPlanilha, final List<Usuario> usuariosComPlanilhaPreenchida) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (ItemPlanilha) PlanilhaDAO.insereItemPlanilha(itemPlanilha, session, usuariosComPlanilhaPreenchida);
            }
        };
    }

    private void verificaItemPai(ObjetoPersistente departamento, ItemPlanilha itemPlanilha) {
        if (itemPlanilha.getItemPai() != null && itemPlanilha.getItemPai().getId() != null) {
            ItemPlanilha itemPai = obtemItemPlanilhaPorId(itemPlanilha.getItemPai().getId(), departamento);
            itemPlanilha.setItemPai(itemPai);
        }
    }

    private Transaction getTransactionAlteraItemPlanilha(final ItemPlanilha itemOriginal) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (ItemPlanilha) PlanilhaDAO.alteraItemPlanilha(itemOriginal, session);
            }
        };
    }

    public ItemPlanilha salvaSubItemPlanilha(ItemPlanilhaTO subItemTO, Departamento departamento, boolean edicao) {
        subItemTO.setId(null);
        ItemPlanilha subItem = new ItemPlanilha(subItemTO);
        if (subItem.getItemPai().getId() == null) {
            throw new RuntimeException("itempai.id.nulo");
        }
        ItemPlanilha itemPai = obtemItemPlanilhaPorId(subItem.getItemPai().getId(), departamento);
        PlanilhaTO planilha = new PlanilhaTO();
        planilha.setId(itemPai.getPlanilha().getId().toString());
        subItemTO.setPlanilha(planilha);
        return salvaItemPlanilha(subItemTO, departamento, edicao);
    }

    public ItemPlanilha excluiItemPlanilha(ItemPlanilhaTO itemParaExclusao, ObjetoPersistente departamento) {
        ItemPlanilha item = new ItemPlanilha(itemParaExclusao);
        verificaItemPlanilhaIdNulo(item.getId());
        ItemPlanilha itemNoBanco = obtemItemPlanilhaPorId(item.getId(), departamento);
        return (ItemPlanilha) transactionTemplate.execute(getTransactionExcluiItemPlanilha(itemNoBanco));
    }

    private Transaction getTransactionExcluiItemPlanilha(final ItemPlanilha itemNoBanco) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (ItemPlanilha) PlanilhaDAO.excluirItemPlanilha(itemNoBanco, session);
            }
        };
    }

    @SuppressWarnings("unchecked")
    public List<Lista> obtemListasCadastradas(ObjetoPersistente departamento) {
        return (List<Lista>) transactionTemplate.execute(getTransactionObtemListasCadastradas(departamento));
    }

    private Transaction getTransactionObtemListasCadastradas(final ObjetoPersistente departamento) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return PlanilhaDAO.obtemListasCadastradas(departamento, session);
            }
        };
    }

    public Lista obtemListaPorId(Long listaId, ObjetoPersistente departamento) {
        if (ObjectUtils.equals(listaId, null)) {
            throw new RuntimeException("lista.id.nulo");
        }
        Lista listaNoBanco = (Lista) transactionTemplate.execute(getTransactionObtemListaPorId(listaId, departamento));
        if (!listaNoBanco.getDepartamento().equals(departamento)) {
            throw new RuntimeException("lista.outro.departamento");
        }
        return listaNoBanco;
    }

    private Transaction getTransactionObtemListaPorId(final Long listaId, final ObjetoPersistente departamento) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (Lista) PlanilhaDAO.obtemListaPorId(listaId, session);
            }
        };
    }

    public Lista salvaLista(Lista lista, Departamento departamento, boolean edicao) {
        Notification erros = new Notification();
        ValidacaoUtils.validaCampoObrigatorio(erros, lista.getDescricao(), new Object[] { "_nls.mensagem.lista.atributo.descricao" });
        if (erros.size() > 0) {
            throw new BaseRuntimeException(erros);
        }
        lista.setDepartamento(departamento);
        if (edicao) {
            return alteraLista(lista, departamento);
        }
        return insereLista(lista, departamento);
    }

    private Lista alteraLista(Lista lista, ObjetoPersistente departamento) {
        Lista listaNoBanco = obtemListaPorIdComItens(lista.getId(), departamento);
        for (ItemLista item : listaNoBanco.getItens()) {
            item.setLista(lista);
            lista.getItens().add(item);
        }
        return alteraLista(lista);
    }

    private Lista alteraLista(Lista lista) {
        return (Lista) transactionTemplate.execute(getTransactionAlteraLista(lista));
    }

    private Transaction getTransactionAlteraLista(final Lista lista) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (Lista) PlanilhaDAO.alteraLista(lista, session);
            }
        };
    }

    private Lista insereLista(Lista lista, ObjetoPersistente departamento) {
        return (Lista) transactionTemplate.execute(getTansactionInsereLista(lista));
    }

    private Transaction getTansactionInsereLista(final Lista lista) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (Lista) PlanilhaDAO.insereLista(lista, session);
            }
        };
    }

    public Lista obtemListaPorIdComItens(Long listaId, ObjetoPersistente departamento) {
        obtemListaPorId(listaId, departamento);
        return (Lista) transactionTemplate.execute(getTransactionObtemListaPorIdComItens(listaId));
    }

    private Transaction getTransactionObtemListaPorIdComItens(final Long listaId) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (Lista) PlanilhaDAO.obtemListaPorIdComItens(listaId, session);
            }
        };
    }

    public ItemLista excluiItemListaPorId(Long itemListaId, Usuario usuario) {
        ItemLista item = obtemItemListaPorId(itemListaId, usuario.getDepartamento());
        return (ItemLista) transactionTemplate.execute(getTransactionExcluiItemListaPorId(item));
    }

    private ItemLista obtemItemListaPorId(Long itemListaId, ObjetoPersistente departamento) {
        ItemLista item;
        if (ObjectUtils.equals(itemListaId, null)) {
            throw new RuntimeException("itemlista.id.nulo");
        }
        item = (ItemLista) transactionTemplate.execute(getTransactionObtemItemListaPorId(itemListaId));
        if (ObjectUtils.equals(item, null)) {
            throw new RuntimeException("itemlista.nao.existe.id=" + itemListaId);
        }
        if (!item.pertence(departamento)) {
            throw new RuntimeException("itemlista.outro.departamento");
        }
        return item;
    }

    private Transaction getTransactionExcluiItemListaPorId(final ItemLista item) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (ItemLista) PlanilhaDAO.excluirItemListaPorId(item, session);
            }
        };
    }

    private Transaction getTransactionObtemItemListaPorId(final Long itemListaId) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (ItemLista) PlanilhaDAO.obtemItemListaPorId(itemListaId, session);
            }
        };
    }

    public Lista excluiListaPorId(Long listaId, Usuario usuario) {
        Lista lista = obtemListaPorIdComItens(listaId, usuario.getDepartamento());
        if (listaJaVinculadaAUmItemDePlanilha(lista.getId(), usuario.getDepartamento())) {
            throw new BaseRuntimeException("lista.ja.vinculada.a.um.item.de.planilha");
        }
        return (Lista) transactionTemplate.execute(getTransactionExcluiListaPorId(listaId));
    }

    private boolean listaJaVinculadaAUmItemDePlanilha(Long idLista, ObjetoPersistente departamento) {
        List<ItemPlanilha> itens = obtemItensPlanilhiaQueReferenciamLista(idLista, departamento);
        return itens.size() > 0;
    }

    @SuppressWarnings("unchecked")
    private List<ItemPlanilha> obtemItensPlanilhiaQueReferenciamLista(Long idLista, ObjetoPersistente departamento) {
        return (List<ItemPlanilha>) transactionTemplate.execute(getTransactionObtemItensPlanilhaQueReferenciamLista(idLista, departamento));
    }

    private Transaction getTransactionObtemItensPlanilhaQueReferenciamLista(final Long idLista, final ObjetoPersistente departamento) {
        return new TransactionReadOnly() {

            public Object doInTransaction(Session session) {
                return (List) PlanilhaDAO.obtemItensPlanilhaQueReferenciamLista(idLista, departamento, session);
            }
        };
    }

    private Transaction getTransactionExcluiListaPorId(final Long listaId) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (Lista) PlanilhaDAO.excluiListaPorId(listaId, session);
            }
        };
    }

    public ItemLista salvaItemLista(ItemLista novoItemLista, ObjetoPersistente departamento) {
        Notification erros = new Notification();
        Lista lista = obtemListaPorId(novoItemLista.getLista().getId(), departamento);
        ValidacaoUtils.validaCampoObrigatorio(erros, novoItemLista.getDescricao(), new Object[] { "_nls.mensagem.lista.atributo.descricao" });
        if (erros.size() > 0) {
            throw new BaseRuntimeException(erros);
        }
        novoItemLista.setLista(lista);
        verificaItemListaJaExiste(novoItemLista, departamento);
        return (ItemLista) transactionTemplate.execute(getTrasactionInserveItemLista(novoItemLista));
    }

    private void verificaItemListaJaExiste(ItemLista novoItemLista, ObjetoPersistente departamento) {
        List<ItemLista> itenNoBanco = obtemItemListaPorDescricao(novoItemLista, departamento);
        if (itenNoBanco.size() > 0) {
            throw new BaseRuntimeException("itemlista.descricao.repetida");
        }
    }

    @SuppressWarnings("unchecked")
    private List<ItemLista> obtemItemListaPorDescricao(ItemLista novoItemLista, ObjetoPersistente departamento) {
        return (List<ItemLista>) transactionTemplate.execute(getTransactionObtemItemListaPorDescricao(novoItemLista, departamento));
    }

    private Transaction getTransactionObtemItemListaPorDescricao(final ItemLista novoItemLista, final ObjetoPersistente departamento) {
        return new TransactionReadOnly() {

            @SuppressWarnings("unchecked")
            public Object doInTransaction(Session session) {
                return (List<ItemLista>) PlanilhaDAO.obtemItemListaPorDescricao(novoItemLista, departamento, session);
            }
        };
    }

    private Transaction getTrasactionInserveItemLista(final ItemLista novoItemLista) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (ItemLista) PlanilhaDAO.insereItemLista(novoItemLista, session);
            }
        };
    }

    @SuppressWarnings("unchecked")
    private List<ItemPlanilhaUsuario> inializaPlanilha(Planilha planihaCorrente, Usuario usuario) {
        Planilha planilhaAtualComItens = obtemPlanilhaPorIdComItens(planihaCorrente.getId(), usuario.getDepartamento());
        List<ItemPlanilhaUsuario> itensusuario = new ArrayList<ItemPlanilhaUsuario>();
        PlanilhaIterator iterador = planilhaAtualComItens.iterator();
        while (iterador.hasNext()) {
            ItemPlanilha itemPlanilha = iterador.next().getItem();
            ItemPlanilhaUsuario itemusuario = ItemPlanilhaUsuario.getInstance(itemPlanilha);
            itemusuario.setUsuario(usuario);
            if (!ItemPlanilha.TIPO_OUTROS.equals(itemPlanilha.getTipo())) {
                itensusuario.add(itemusuario);
            }
        }
        logger.debug(ResourceUtil.getLOGMessage("debug.criar.planilha", usuario.getLogin(), String.valueOf(itensusuario.size())));
        return (List<ItemPlanilhaUsuario>) transactionTemplate.execute(getTransactionInicializaPlanilha(itensusuario));
    }

    private Transaction getTransactionInicializaPlanilha(final List<ItemPlanilhaUsuario> itensUsuario) {
        return new Transaction() {

            public Object doInTransaction(Session session) {
                return (List) PlanilhaDAO.inicializaPlanilha(itensUsuario, session);
            }
        };
    }

    public Planilha obtemPlanilhaAnoBaseCorrente(Departamento departamento) {
        AnoBase anoBaseCorrente = DisciplinaService.getInstance().obtemAnoBaseCorrente(departamento);
        verificaAnoBasePossuiPlanilha(departamento, anoBaseCorrente);
        return obtemPLanilhasPorAno(anoBaseCorrente, departamento).get(0);
    }

    private void verificaAnoBasePossuiPlanilha(Departamento departamento, AnoBase anoBaseCorrente) {
        if (!anoBaseJaPossuiPlanilha(anoBaseCorrente, departamento)) {
            throw new BaseRuntimeException("planilha.corrente.nao.criada");
        }
    }

    public List<ItemPlanilhaUsuario> inializaPlanilha(Usuario usuario) {
        Planilha planilhaCorrente = obtemPlanilhaAnoBaseCorrente(usuario.getDepartamento());
        return inializaPlanilha(planilhaCorrente, usuario);
    }

    public Planilha obtemPlanilhaAnoBaseQualquer(AnoBase anoBase, Departamento departamento) {
        AnoBase anoBaseNoBanco = DisciplinaService.getInstance().obtemAnoBasePorId(anoBase.getId(), departamento);
        verificaAnoBasePossuiPlanilha(departamento, anoBaseNoBanco);
        return obtemPLanilhasPorAno(anoBaseNoBanco, departamento).get(0);
    }
}
