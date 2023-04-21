package br.uff.javaavancado.controller;

import br.uff.javaavancado.controletransacao.FabricaDeAppService;
import br.uff.javaavancado.modelos.Categoria;
import br.uff.javaavancado.modelos.Cliente;
import br.uff.javaavancado.modelos.ItemPedido;
import br.uff.javaavancado.modelos.Pedido;
import br.uff.javaavancado.modelos.Produto;
import br.uff.javaavancado.service.CategoriaService;
import br.uff.javaavancado.service.PedidoService;
import br.uff.javaavancado.service.ProdutoService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author proac
 */
public class PedidoController extends BaseController {

    private PedidoService pedidoService;

    private CategoriaService categoriaService;

    private ProdutoService produtoService;

    private DataModel tabelaProdutos;

    private DataModel tabelaProdutosDoPedido;

    private List<SelectItem> listaSelectCategorias;

    private String categoriaSelecionada;

    private Produto produtoCorrente;

    private Pedido pedidoCorrente;

    private String busca;

    public PedidoController() {
        try {
            pedidoService = FabricaDeAppService.getAppService(PedidoService.class);
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            produtoService = FabricaDeAppService.getAppService(ProdutoService.class);
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            categoriaService = FabricaDeAppService.getAppService(CategoriaService.class);
        } catch (Exception ex) {
            Logger.getLogger(ProdutoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String fecharCompra() {
        Set<ItemPedido> itens = pedidoCorrente.getItens();
        if (itens == null || itens.size() == 0) {
            error("Seu carrinho ainda está vazio");
            return null;
        }
        pedidoService.fecharCompra(pedidoCorrente);
        Cliente cli = ((LoginController) getBean("LoginController")).getClienteLogado();
        cli.setPedidoAberto(new Pedido());
        categoriaSelecionada = null;
        pedidoCorrente = null;
        tabelaProdutos = null;
        info("Parabéns pela sua compra");
        info("Agradecemos sua preferência");
        return "produtos";
    }

    public String verDetalhesFecharCompra() {
        Cliente cli = ((LoginController) getBean("LoginController")).getClienteLogado();
        pedidoCorrente = cli.getPedidoAberto();
        Set<Produto> produtos = new HashSet();
        if (pedidoCorrente == null) {
            tabelaProdutosDoPedido = null;
            return "carrinho";
        }
        Set<ItemPedido> itens = pedidoCorrente.getItens();
        if (itens == null) {
            tabelaProdutosDoPedido = null;
            return "carrinho";
        }
        for (ItemPedido itp : itens) {
            Produto p = itp.getProduto();
            p.setEstoque(itp.getQtd());
            produtos.add(p);
        }
        tabelaProdutosDoPedido = new ListDataModel(new ArrayList(produtos));
        return "carrinho";
    }

    public String adicionarItem() {
        Produto p = (Produto) tabelaProdutos.getRowData();
        Cliente cli = ((LoginController) getBean("LoginController")).getClienteLogado();
        pedidoCorrente = cli.getPedidoAberto();
        pedidoCorrente.setCliente(cli);
        ItemPedido it = pedidoService.adicionaItem(p, pedidoCorrente);
        if (it == null) {
            error("Esse produto nao se encontra mais no estoque");
        } else {
            Set<ItemPedido> itens = pedidoCorrente.getItens();
            if (itens == null) {
                itens = new HashSet();
            }
            if (!itens.add(it)) {
                for (Iterator<ItemPedido> itt = itens.iterator(); itt.hasNext(); ) {
                    ItemPedido it2 = itt.next();
                    if (it2.equals(it)) {
                        it2.setQtd(it.getQtd());
                        it2.setValor(it.getValor());
                        break;
                    }
                }
            }
            pedidoCorrente.setItens(itens);
        }
        atualizaProdutosCategoria();
        return null;
    }

    public String removerItem() {
        Produto p = (Produto) tabelaProdutosDoPedido.getRowData();
        ItemPedido it = pedidoService.removeItem(p, pedidoCorrente);
        boolean semItem = false;
        if (it == null) {
            error("Erro ao remover item do carrinho");
        } else {
            Set<ItemPedido> itens = pedidoCorrente.getItens();
            semItem = (it.getQtd() == 0);
            if (semItem) {
                itens.remove(it);
            }
            for (Iterator<ItemPedido> itt = itens.iterator(); itt.hasNext(); ) {
                ItemPedido it2 = itt.next();
                if (it2.equals(it)) {
                    it2.setQtd(it.getQtd());
                    it2.setValor(it.getValor());
                    break;
                }
            }
            pedidoCorrente.setItens(itens);
        }
        if (semItem) {
            tabelaProdutosDoPedido = null;
            return "produtos";
        } else {
            Set<Produto> produtos = new HashSet();
            for (ItemPedido itp : pedidoCorrente.getItens()) {
                Produto p1 = itp.getProduto();
                p1.setEstoque(itp.getQtd());
                produtos.add(p1);
            }
            tabelaProdutosDoPedido = new ListDataModel(new ArrayList(produtos));
        }
        categoriaSelecionada = null;
        tabelaProdutos = null;
        return null;
    }

    public String atualizaProdutosCategoria() {
        Categoria c = (Categoria) getObjetoSelecionado(listaSelectCategorias, categoriaSelecionada);
        List<Produto> lista;
        if (c == null) {
            lista = produtoService.getListaCompleta();
        } else {
            lista = produtoService.getListaOrdenada(c);
        }
        if (lista != null) {
            tabelaProdutos = new ListDataModel(lista);
        } else {
            tabelaProdutos = null;
        }
        return null;
    }

    public List<SelectItem> getListaSelectCategorias() {
        if (listaSelectCategorias == null) {
            final List<Categoria> lista = categoriaService.getListaCompleta();
            if (lista != null) {
                listaSelectCategorias = new ArrayList<SelectItem>(lista.size() + 1);
                listaSelectCategorias.add(new SelectItem(null, "Escolha uma categoria"));
                for (Categoria c : lista) {
                    listaSelectCategorias.add(new SelectItem(c, c.toString()));
                }
            }
        }
        return listaSelectCategorias;
    }

    public Object getObjetoSelecionado(List<SelectItem> lista, String selecao) {
        for (SelectItem sel : lista) {
            if (sel.getLabel().equals(selecao)) {
                return sel.getValue();
            }
        }
        return null;
    }

    public String pesquisar() {
        List produtos = produtoService.buscaProdutoPorNome(busca);
        System.out.println(produtos);
        tabelaProdutos = new ListDataModel(produtos);
        return null;
    }

    public void setListaSelectCategorias(List<SelectItem> listaSelectCategorias) {
        this.listaSelectCategorias = listaSelectCategorias;
    }

    public String getCategoriaSelecionada() {
        return categoriaSelecionada;
    }

    public void setCategoriaSelecionada(String categoriaSelecionada) {
        this.categoriaSelecionada = categoriaSelecionada;
    }

    public Produto getProdutoCorrente() {
        return produtoCorrente;
    }

    public void setProdutoCorrente(Produto produtoCorrente) {
        this.produtoCorrente = produtoCorrente;
    }

    public DataModel getTabelaProdutos() {
        return tabelaProdutos;
    }

    public void setTabelaProdutos(DataModel tabelaProdutos) {
        this.tabelaProdutos = tabelaProdutos;
    }

    public DataModel getTabelaProdutosDoPedido() {
        return tabelaProdutosDoPedido;
    }

    public void setTabelaProdutosDoPedido(DataModel tabelaProdutosDoPedido) {
        this.tabelaProdutosDoPedido = tabelaProdutosDoPedido;
    }

    public String getBusca() {
        return busca;
    }

    public void setBusca(String busca) {
        this.busca = busca;
    }
}
