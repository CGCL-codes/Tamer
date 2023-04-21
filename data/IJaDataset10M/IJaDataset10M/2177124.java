package br.unb.cic.gerval.client.teste;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import br.unb.cic.gerval.client.PopUpAguarde;
import br.unb.cic.gerval.client.componentes.ButtonConfirmacao;
import br.unb.cic.gerval.client.componentes.PopupAviso;
import br.unb.cic.gerval.client.componentes.TelaComAguarde;
import br.unb.cic.gerval.client.rpc.vo.Linha;
import br.unb.cic.gerval.client.rpc.vo.Produto;
import br.unb.cic.gerval.client.rpc.vo.ProdutoTeste;
import br.unb.cic.gerval.client.rpc.vo.Teste;
import br.unb.cic.gerval.client.usuario.Atualizavel;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ChangeListener;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SourcesTableEvents;
import com.google.gwt.user.client.ui.TableListener;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class TelaSolicitarNovoTeste extends TelaComAguarde implements Atualizavel {

    private List combosLinhas = new ArrayList();

    private List combosProdutos = new ArrayList();

    private Grid tabelaProdutos;

    private final TextBox campoOpSmp;

    ControladorSolicitaTeste controlador;

    ModeloSolicitaTeste modelo;

    private Button bAdicionar;

    private Label titulo;

    private TextArea campoObs;

    public TelaSolicitarNovoTeste() {
        modelo = new ModeloSolicitaTeste(this);
        controlador = new ControladorSolicitaTeste(modelo);
        VerticalPanel pMain = new VerticalPanel();
        pMain.setWidth("100%");
        pMain.setSpacing(5);
        titulo = new Label("Solicitar novo teste");
        titulo.setStyleName("titulo");
        pMain.add(titulo);
        pMain.setCellHorizontalAlignment(titulo, HasHorizontalAlignment.ALIGN_CENTER);
        VerticalPanel pProdutos = createPainelProdutos();
        pMain.add(pProdutos);
        pMain.setCellHorizontalAlignment(pProdutos, HasHorizontalAlignment.ALIGN_CENTER);
        Grid tabelaCampos = new Grid(3, 2);
        tabelaCampos.setStyleName("atomico");
        Label opSMP = new Label("OP/SMP:");
        tabelaCampos.setWidget(0, 0, opSMP);
        campoOpSmp = new TextBox();
        tabelaCampos.setWidget(0, 1, campoOpSmp);
        Label obs = new Label("Obs:");
        tabelaCampos.setWidget(2, 0, obs);
        campoObs = new TextArea();
        campoObs.setSize("200", "50");
        tabelaCampos.setWidget(2, 1, campoObs);
        pMain.add(tabelaCampos);
        pMain.setCellHorizontalAlignment(tabelaCampos, HasHorizontalAlignment.ALIGN_CENTER);
        HorizontalPanel pBotoes = createPainelBotoes();
        pMain.add(pBotoes);
        pMain.setCellHorizontalAlignment(pBotoes, HasHorizontalAlignment.ALIGN_CENTER);
        initWidget(pMain);
    }

    private HorizontalPanel createPainelBotoes() {
        Button bSolicitar = new Button("Solicitar");
        bSolicitar.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                PopUpAguarde.inicia();
                List produtos = getProdutosSalvar();
                if (produtos == null) {
                    return;
                }
                Teste teste = new Teste();
                List pts = new ArrayList();
                int i = 0;
                for (Iterator iter = produtos.iterator(); iter.hasNext(); ) {
                    Produto p = (Produto) iter.next();
                    pts.add(new ProdutoTeste(i, p, teste));
                    i++;
                }
                teste.setProdutos(pts);
                if (teste.getProdutos().size() <= 0) {
                    PopUpAguarde.termina();
                    new PopupAviso("Nenhum produto selecionado", "popup_erro").show();
                    return;
                }
                if (campoOpSmp.getText() != null && campoOpSmp.getText().trim().length() == 0) {
                    PopUpAguarde.termina();
                    new PopupAviso("Você deve entrar com um OP/SMP", "popup_erro").show();
                    return;
                }
                teste.setOpSMP(campoOpSmp.getText());
                teste.setObservacaoSolicitador(campoObs.getText());
                controlador.solicitarTeste(teste);
            }

            private List getProdutosSalvar() {
                List produtosSalvar = new ArrayList();
                Iterator itLinhas = combosLinhas.iterator();
                Iterator itProdutos = combosProdutos.iterator();
                while (itLinhas.hasNext() & itLinhas.hasNext()) {
                    ListBox comboLinhas = (ListBox) itLinhas.next();
                    int indiceLinha = comboLinhas.getSelectedIndex();
                    Linha l = (Linha) modelo.getLinhas().get(indiceLinha);
                    ListBox comboProdutos = (ListBox) itProdutos.next();
                    int indiceProduto = comboProdutos.getSelectedIndex();
                    List produtos = modelo.getProdutos(l);
                    if (produtos == null || produtos.size() == 0) {
                        new PopupAviso("É preciso selecionar um produto válido", "popup_erro").show();
                        return null;
                    }
                    Produto p = (Produto) produtos.get(indiceProduto);
                    if (produtosSalvar.contains(p)) {
                        new PopupAviso("O mesmo produto foi selecionado mais de uma vez", "popup_erro").show();
                        return null;
                    } else {
                        produtosSalvar.add(p);
                    }
                }
                return produtosSalvar;
            }
        });
        Button bCancelar = new ButtonConfirmacao("Cancelar");
        bCancelar.addClickListener(new ClickListener() {

            public void onClick(Widget arg0) {
                History.newItem("agenda");
            }
        });
        HorizontalPanel pBotoes = new HorizontalPanel();
        pBotoes.setSpacing(5);
        pBotoes.add(bSolicitar);
        pBotoes.add(bCancelar);
        return pBotoes;
    }

    private VerticalPanel createPainelProdutos() {
        VerticalPanel pProdutos = new VerticalPanel();
        pProdutos.setSpacing(5);
        pProdutos.setStyleName("agrupamento");
        Label tituloTabela = new Label("Produtos a serem testados");
        pProdutos.setTitle(tituloTabela.getText());
        tabelaProdutos = new Grid(1, 3);
        tabelaProdutos.setTitle("Produtos a testar");
        tabelaProdutos.getRowFormatter().addStyleName(0, "titulo_tabela");
        tabelaProdutos.setText(0, 0, "Linha");
        tabelaProdutos.setText(0, 1, "Produto");
        tabelaProdutos.getCellFormatter().setWidth(0, 0, "100");
        tabelaProdutos.getCellFormatter().setWidth(0, 1, "200");
        tabelaProdutos.getCellFormatter().setWidth(0, 2, "20");
        tabelaProdutos.addTableListener(new TableListener() {

            public void onCellClicked(SourcesTableEvents arg0, int linha, int coluna) {
                if (coluna == 2) {
                    removerProdutoSalvar(linha - 1);
                }
            }

            private void removerProdutoSalvar(int i) {
                combosLinhas.remove(i);
                combosProdutos.remove(i);
                redesenhaTabelaProdutos();
            }
        });
        pProdutos.add(tabelaProdutos);
        pProdutos.setCellHorizontalAlignment(tabelaProdutos, HasHorizontalAlignment.ALIGN_CENTER);
        bAdicionar = new Button("Adicionar...");
        bAdicionar.addClickListener(new ClickListener() {

            public void onClick(Widget sender) {
                adicionaLinhaProduto(null);
                redesenhaTabelaProdutos();
            }
        });
        pProdutos.add(bAdicionar);
        pProdutos.setCellHorizontalAlignment(bAdicionar, HasHorizontalAlignment.ALIGN_RIGHT);
        return pProdutos;
    }

    public void startSolicitaValidacao() {
        controlador.inicializaModelo();
        titulo.setText("Solicitar novo teste");
        bAdicionar.setEnabled(true);
        campoOpSmp.setEnabled(true);
        campoOpSmp.setText("");
        campoObs.setText("");
    }

    public void startSolicitarRevalidacao(Teste teste) {
        controlador.inicializaModeloRevalidacao(teste);
        titulo.setText("Solicitar re-validação do RVP " + teste.getId());
        bAdicionar.setEnabled(false);
        campoOpSmp.setText(teste.getOpSMP());
        campoOpSmp.setEnabled(false);
        campoObs.setText("");
    }

    public void atualizar() {
        GWT.log("Atualizando solicitar", null);
        if (modelo.isSolicitado()) {
            GWT.log("Terminei de solicitar", null);
            History.newItem("agenda");
        }
        combosLinhas.clear();
        combosProdutos.clear();
        redesenhaTabelaProdutos();
        if (controlador.fimRequisicoes()) PopUpAguarde.termina();
    }

    /**
	 * Adiciona uma linha de combos (linha e produto) na tabela de produtos.
	 * seleciona as combos conforme o produto passado
	 * 
	 */
    private void adicionaLinhaProduto(Produto p) {
        if (modelo.getLinhas() == null) {
            return;
        }
        ListBox comboLinhas = new ListBox();
        combosLinhas.add(comboLinhas);
        final ListBox comboProdutos = new ListBox();
        combosProdutos.add(comboProdutos);
        for (Iterator it = modelo.getLinhas().iterator(); it.hasNext(); ) {
            Linha linha = (Linha) it.next();
            comboLinhas.addItem(linha.getNome());
        }
        if (p != null) {
            preencheComboProdutos(p.getLinha(), comboProdutos);
            List todasLinhas = modelo.getLinhas();
            int indiceLinha = todasLinhas.indexOf(p.getLinha());
            comboLinhas.setSelectedIndex(indiceLinha);
            List produtosDaLinha = modelo.getProdutos(p.getLinha());
            if (produtosDaLinha != null) {
                int indiceProduto = produtosDaLinha.indexOf(p);
                comboProdutos.setSelectedIndex(indiceProduto);
            }
        } else {
            preencheComboProdutos((Linha) modelo.getLinhas().get(0), comboProdutos);
        }
        comboLinhas.addChangeListener(new ChangeListener() {

            public void onChange(Widget w) {
                ListBox combo = (ListBox) w;
                Linha l = (Linha) modelo.getLinhas().get(combo.getSelectedIndex());
                preencheComboProdutos(l, comboProdutos);
            }
        });
    }

    /**
	 * Preenche uma combo em relação à outra
	 * @param l
	 * @param comboProdutos
	 */
    private void preencheComboProdutos(Linha l, ListBox comboProdutos) {
        comboProdutos.clear();
        List produtos = modelo.getProdutos(l);
        if (produtos != null && produtos.size() > 0) {
            for (Iterator iter = produtos.iterator(); iter.hasNext(); ) {
                Produto produto = (Produto) iter.next();
                comboProdutos.addItem(produto.getNome());
            }
        }
    }

    /**
	 * redesenha a tabela de produtos baseado nas listas de combos
	 *
	 */
    private void redesenhaTabelaProdutos() {
        tabelaProdutos.clear();
        tabelaProdutos.resize(combosLinhas.size() + 1, 3);
        tabelaProdutos.setText(0, 0, "Linha");
        tabelaProdutos.setText(0, 1, "Produto");
        Iterator it = combosLinhas.iterator();
        Iterator itp = combosProdutos.iterator();
        int linha = 1;
        while (it.hasNext() & itp.hasNext()) {
            ListBox comboLinha = (ListBox) it.next();
            ListBox comboProdutos = (ListBox) itp.next();
            tabelaProdutos.setWidget(linha, 0, comboLinha);
            tabelaProdutos.setWidget(linha, 1, comboProdutos);
            comboLinha.setEnabled(true);
            comboProdutos.setEnabled(true);
            Hyperlink hyperlink = new Hyperlink();
            hyperlink.setText("excluir");
            tabelaProdutos.setWidget(linha, 2, hyperlink);
            linha++;
        }
    }
}
