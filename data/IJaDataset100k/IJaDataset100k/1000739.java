package br.com.wepa.webapps.orca.logica.negocio.facade;

import static br.com.wepa.webapps.orca.util.CheckValue.IF;
import static br.com.wepa.webapps.orca.util.CheckValue.Condition.NOTEMPTYSTRING;
import static br.com.wepa.webapps.orca.util.CheckValue.Condition.NOTNULL;
import static br.com.wepa.webapps.orca.util.CheckValue.Condition.NOTZERO;
import java.util.Date;
import java.util.List;
import br.com.wepa.webapps.logger.TraceLogger;
import br.com.wepa.webapps.orca.logica.modelo.Especificacaoproduto;
import br.com.wepa.webapps.orca.logica.modelo.Fornecedor;
import br.com.wepa.webapps.orca.logica.modelo.Orcamento;
import br.com.wepa.webapps.orca.logica.modelo.Produto;
import br.com.wepa.webapps.orca.logica.modelo.ProdutoId;
import br.com.wepa.webapps.orca.logica.negocio.BusinessException;
import br.com.wepa.webapps.orca.logica.negocio.BusinessManager;
import br.com.wepa.webapps.orca.logica.negocio.facade.to.OrcamentoTO;
import br.com.wepa.webapps.orca.logica.negocio.grupos.NivelGrupo;
import br.com.wepa.webapps.orca.logica.persistencia.DAOFactory;
import br.com.wepa.webapps.orca.logica.persistencia.OrcamentoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.ProdutoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.search.BeanSearch;
import br.com.wepa.webapps.orca.logica.persistencia.search.HqlQuerySearch;
import br.com.wepa.webapps.orca.logica.persistencia.search.NamedQuerySearch;
import br.com.wepa.webapps.orca.logica.persistencia.transaction.Transaction;

class OrcamentoFacade extends BusinessManager<OrcamentoTO, Orcamento, Integer, OrcamentoDAO> implements OrcamentoFacadeHome {

    private static TraceLogger logger = new TraceLogger(OrcamentoFacade.class);

    @Override
    public OrcamentoTO find(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            HqlQuerySearch<Orcamento> query = new HqlQuerySearch<Orcamento>(to.getPagingBean());
            query.select("distinct orc");
            query.from("Orcamento orc");
            query.from(" left join orc.produtos as produto");
            query.andIf("orc.fornecedor.idFornecedor = ? ", to.getIdFornecedor(), NOTNULL, NOTZERO);
            query.andIf("orc.idOrcamento = ? ", to.getIdOrcamento(), NOTNULL, NOTZERO);
            query.andIf("orc.dtOrcamento >= ? ", to.getDtOrcamentoInicial(), NOTNULL);
            query.andIf("orc.dtOrcamento <= ? ", to.getDtOrcamentoFinal(), NOTNULL);
            query.andIf("orc.dtCadOrcamento >= ? ", to.getDtCadOrcamentoInicial(), NOTNULL);
            query.andIf("orc.dtCadOrcamento <= ? ", to.getDtCadOrcamentoFinal(), NOTNULL);
            query.andIf("orc.vencedor = ? ", to.getVencedor(), NOTNULL);
            query.andLikeAnywhereIf("orc.observacao", to.getObservacao(), NOTNULL, NOTEMPTYSTRING);
            query.andIf("orc.frete = ? ", to.getFrete(), NOTNULL);
            if (!query.andIf(" produto.id.especificacaoProdutoIdEspecProduto = ? ", to.getIdProduto(), NOTNULL, NOTZERO)) {
                if (!query.andIf(" produto.especificacaoproduto.subgrupo.idGrupo = ? ", to.getIdSubGrupoProduto(), NOTNULL, NOTZERO)) {
                    query.andIf(" produto.especificacaoproduto.subgrupo.pai.idGrupo = ? ", to.getIdGrupoProduto(), NOTNULL, NOTZERO);
                }
            }
            query.orderBy("orc.dtOrcamento, orc.idOrcamento");
            query.find();
            query.disableFind();
            to.getSearcher().addSearch("pesquisaOrcamento", query);
            to.setShowSearchResults(true);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    public OrcamentoTO findProduto(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            HqlQuerySearch<Orcamento> query = new HqlQuerySearch<Orcamento>();
            query.select("esp");
            query.from("Especificacaoproduto esp ");
            query.where(" esp.idEspecProduto not in( select prod.id.especificacaoProdutoIdEspecProduto ");
            query.where("from Produto prod where prod.id.orcamentoIdOrcamento = ? ) ", to.getIdOrcamento());
            query.andLikeAnywhereIf("esp.nome", to.getNomeProduto(), NOTNULL, NOTEMPTYSTRING);
            query.andLikeAnywhereIf("esp.descricao", to.getDescricaoProduto(), NOTNULL, NOTEMPTYSTRING);
            query.andIf("esp.subgrupo.idGrupo = ? ", to.getIdSubGrupoProduto(), NOTNULL, NOTZERO);
            query.andIf("esp.subgrupo.pai.idGrupo = ? ", to.getIdGrupoProduto(), NOTNULL, NOTZERO);
            query.orderBy("esp.nome");
            query.find();
            query.disableFind();
            to.getSearcher().addSearch("produtosList", query);
            to.setShowSearchResults(true);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    @Override
    public OrcamentoTO persist(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            if (to.getUpdate()) {
                ((OrcamentoTO) to.getStoreTO()).setDtCadOrcamento(to.getDtCadOrcamento());
            } else {
                to.setDtCadOrcamento(new Date());
            }
            to = super.persist(to);
            to = super.load(to, false);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    @SuppressWarnings("unchecked")
    @Override
    public OrcamentoTO setUp(OrcamentoTO to) throws BusinessException {
        logger.entering();
        BeanSearch bean = new BeanSearch(new Fornecedor());
        bean.addOrder("nome", true);
        to.getSearcher().addSearch("fornecedorList", bean);
        to.enableSearcher();
        to = super.setUp(to);
        logger.exiting();
        return to;
    }

    @SuppressWarnings("unchecked")
    public OrcamentoTO goPesquisar(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            BeanSearch bean = new BeanSearch(new Fornecedor());
            bean.addOrder("nome", true);
            to.getSearcher().addSearch("fornecedorList", bean);
            to.getSearcher().addSearchNamedQuery("grupoList", QUERY_SelectGruposByNivel, NivelGrupo.GRUPO.getLabel());
            if (IF.chk(to.getIdGrupoProduto(), NOTNULL, NOTZERO)) {
                to.getSearcher().addSearchNamedQuery("subgrupoList", QUERY_SelectFilhosByGrupo, to.getIdGrupoProduto());
            }
            if (IF.chk(to.getIdSubGrupoProduto(), NOTNULL, NOTZERO)) {
                to.getSearcher().addSearchNamedQuery("produtoList", QUERY_SelectEspecProdutosOrcamentoSubGrupoOrderByNome, to.getIdSubGrupoProduto());
            }
            to.enableSearcher();
            to.getSearcher().executeAll();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    @Transaction
    public OrcamentoTO adicionarProdutosEmOrc(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            ProdutoDAO pDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getProdutoDAO();
            Produto produto = null;
            produto = pDAO.newInstance();
            ProdutoId idProduto = pDAO.newIDInstance();
            idProduto.setEspecificacaoProdutoIdEspecProduto(to.getIdProduto());
            idProduto.setOrcamentoIdOrcamento(to.getIdOrcamento());
            produto.setId(idProduto);
            produto.setPreco(to.getPrecoProduto());
            produto.setQuantidade(to.getQuantidadeProduto());
            pDAO.makePersistent(produto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    @Transaction
    public OrcamentoTO atualizarProdutosEmOrc(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            ProdutoDAO pDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getProdutoDAO();
            ProdutoId idProduto = pDAO.newIDInstance();
            idProduto.setEspecificacaoProdutoIdEspecProduto(to.getIdProduto());
            idProduto.setOrcamentoIdOrcamento(to.getIdOrcamento());
            Produto produto = pDAO.findById(idProduto, true);
            produto.setPreco(to.getPrecoProduto());
            produto.setQuantidade(to.getQuantidadeProduto());
            pDAO.makePersistent(produto);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    public OrcamentoTO goCadastrarProdutoEmOrc(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            listaSubGrupos(to);
            to = super.load(to, false);
            to.setNumProdutosCadastrados(to.getProdutos().size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e);
        }
        logger.exiting();
        return to;
    }

    @Transaction
    public OrcamentoTO removerProdutosEmOrc(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            ProdutoDAO pDAO = DAOFactory.instance(DAOFactory.HIBERNATE).getProdutoDAO();
            ProdutoId idProduto = pDAO.newIDInstance();
            idProduto.setEspecificacaoProdutoIdEspecProduto(to.getIdProduto());
            idProduto.setOrcamentoIdOrcamento(to.getIdOrcamento());
            Produto p = pDAO.findById(idProduto, false);
            pDAO.makeTransient(p);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    @SuppressWarnings("unchecked")
    public OrcamentoTO listaSubGrupos(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            to.getSearcher().addSearchNamedQuery("grupoList", QUERY_SelectGruposByNivel, NivelGrupo.GRUPO.getLabel());
            if (to.getIdGrupoProduto() != null && to.getIdGrupoProduto() != 0) {
                to.getSearcher().addSearchNamedQuery("subgrupoList", QUERY_SelectFilhosByGrupo, to.getIdGrupoProduto());
            }
            to.getSearcher().executeAll();
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        return to;
    }

    public OrcamentoTO detalhaEspecificacaoProduto(OrcamentoTO to) throws BusinessException {
        try {
            Especificacaoproduto especProduto = DAOFactory.instance(DAOFactory.HIBERNATE).getEspecificacaoprodutoDAO().findById(to.getIdProduto(), false);
            if (especProduto == null) {
                throw new BusinessException(ERROR_REGISTRO_NAO_ENCONTRADO);
            }
            to.setEspecProduto(especProduto);
        } catch (Exception e) {
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }

    public OrcamentoTO detalhaOrcamento(OrcamentoTO to) throws BusinessException {
        logger.entering();
        try {
            super.load(to, false);
            NamedQuerySearch<Produto> search = new NamedQuerySearch<Produto>(QUERY_SelectProdutosOrcamentoOrderByNome, to.getIdOrcamento());
            search.disablePaging();
            List<Produto> produtos = search.find().getResult();
            to.setSubtotalProdutos(0D);
            if (produtos != null) {
                for (Produto p : produtos) {
                    to.setSubtotalProdutos(to.getSubtotalProdutos() + (p.getPreco() * p.getQuantidade()));
                }
            }
            double frete = to.getFrete() != null ? to.getFrete() : 0D;
            to.setTotal(to.getSubtotalProdutos() + frete);
            search.disableFind();
            to.getSearcher().addSearch("produtosCadastrados", search);
            to.setNumProdutosCadastrados(produtos.size());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new BusinessException(e.getMessage(), e);
        }
        logger.exiting();
        return to;
    }
}
