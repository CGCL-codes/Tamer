package br.unb.cic.gerval.client.produto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import br.unb.cic.gerval.client.PopUpAguarde;
import br.unb.cic.gerval.client.TabelaCallback;
import br.unb.cic.gerval.client.TabelaListagem;
import br.unb.cic.gerval.client.componentes.TelaComAguarde;
import br.unb.cic.gerval.client.rpc.vo.Produto;
import br.unb.cic.gerval.client.usuario.Atualizavel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TelaProduto extends TelaComAguarde implements TabelaCallback, Atualizavel {

    private PanelEditarProduto pEditar;

    private TabelaListagem tabelaProdutos;

    private ModeloProduto modelo;

    private ControladorProduto controlador;

    public TelaProduto() {
        modelo = new ModeloProduto(this);
        controlador = new ControladorProduto(modelo);
        Collection titulosTabela = new ArrayList();
        titulosTabela.add("Linha");
        titulosTabela.add("Código");
        titulosTabela.add("Nome");
        titulosTabela.add("Responsável");
        titulosTabela.add("");
        tabelaProdutos = new TabelaListagem(this, titulosTabela);
        VerticalPanel main = new VerticalPanel();
        main.setSpacing(5);
        main.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        main.setWidth("100%");
        Label titulo = new Label("Produtos");
        titulo.setStyleName("titulo");
        main.add(titulo);
        ScrollPanel scroll = new ScrollPanel();
        scroll.add(tabelaProdutos);
        scroll.setHeight("300");
        scroll.setWidth("100%");
        main.add(scroll);
        HorizontalPanel pBotoes = new HorizontalPanel();
        Button bNovo = new Button("Novo Produto");
        bNovo.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                clicaNovoProduto();
            }
        });
        pBotoes.add(bNovo);
        main.add(pBotoes);
        pEditar = new PanelEditarProduto(this, modelo);
        main.add(pEditar);
        pEditar.setVisible(false);
        initWidget(main);
    }

    private void clicaNovoProduto() {
        pEditar.limparProdutoCorrente();
        pEditar.editarProduto(new Produto());
    }

    /**
	 * chamado pelo painel de editar
	 * @param p
	 */
    void insere(Produto p) {
        PopUpAguarde.inicia();
        controlador.salvarProduto(p);
    }

    /**
	 * Inicia a tela com dados consultados do servidor
	 *
	 */
    public void start() {
        controlador.inicializaModelo();
    }

    /**
	 *  METODO DA INTERFACE TabelaCallback
	 */
    public void editar(int num) {
        Produto u = (Produto) modelo.getProdutos().get(num);
        pEditar.editarProduto(u);
    }

    /**
	 *  METODO DA INTERFACE TabelaCallback
	 */
    public void excluir(int num) {
        PopUpAguarde.inicia();
        controlador.removerProduto(new Integer(num));
    }

    /**
	 * chamado quando o modelo é alterado
	 */
    public void atualizar() {
        pEditar.atualizar();
        tabelaProdutos.clear();
        tabelaProdutos.resizeRows(1);
        List produtos = modelo.getProdutos();
        if (!produtos.isEmpty()) {
            Iterator iterador = produtos.iterator();
            Collection valores = null;
            for (int i = 1; i <= produtos.size(); i++) {
                valores = new ArrayList();
                Produto produto = (Produto) iterador.next();
                valores.add(produto.getLinha().getNome());
                valores.add(produto.getCodigo());
                valores.add(produto.getNome());
                valores.add(produto.getResponsavel().getNome());
                valores.add("excluir");
                tabelaProdutos.add(valores);
            }
        }
        PopUpAguarde.termina();
    }

    /**
	 * chamado pelo pEditar, qndo se clica no salvar()
	 * @param p
	 */
    void salvarProduto(Produto p) {
        PopUpAguarde.inicia();
        controlador.salvarProduto(p);
    }
}
