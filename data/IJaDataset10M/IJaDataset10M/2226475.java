package br.gov.ba.mam.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.MaskFormatter;
import br.gov.ba.mam.beans.Artista;
import br.gov.ba.mam.beans.Categoria;
import br.gov.ba.mam.beans.ESTADO;
import br.gov.ba.mam.beans.Endereco;
import br.gov.ba.mam.beans.Mensagem;
import br.gov.ba.mam.beans.Obra;
import br.gov.ba.mam.gerente.GerenteAplicacao;
import br.gov.ba.mam.imagens.Imagens;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class PainelCadastro extends PanelBuilder implements ChangeListener {

    private static final long serialVersionUID = 1L;

    static final int SIM = 0, NAO = 1, TALVEZ = 2, NA = 3;

    private CellConstraints cc = new CellConstraints();

    private TelaCadCategoria telaCad;

    private Artista artista;

    private JTextField txtNome;

    private JTextField txtInsc;

    private JTextArea txtObs;

    private JComboBox cbSexo;

    private JTextField txtTel;

    private JTextField txtRua;

    private JTextField txtBairro;

    private JTextField txtCidade;

    private JTextField txtCep;

    private JComboBox cbEstado;

    private JTextField txtPais;

    private JTextField txtEmail;

    private JTextField txtTituloObra;

    private JTextField txtCodObra;

    private JButton btAddCategoria;

    private JComboBox cbCategoria;

    private JRadioButton btSim;

    private JRadioButton btNao;

    private JRadioButton btTalvez;

    private JRadioButton btNA;

    private TabelaObras tabela;

    private JButton jbRemover;

    private JButton btCancelar;

    private JButton jbLimpar;

    private Integer selected;

    public PainelCadastro() {
        super(new FormLayout("15dlu,45dlu,5dlu,30dlu,30dlu,32dlu,10dlu,14dlu,24dlu,55dlu,5dlu,30dlu,35dlu,15dlu", "10dlu,15dlu,5dlu,15dlu,15dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,15dlu,5dlu,16dlu,5dlu,15dlu,5dlu,15dlu,15dlu,15dlu,15dlu,5dlu,16dlu,5dlu"));
        getPainelArtista();
        getPainelEndereco();
        getPainelObra();
        getPainelBotoes();
    }

    private void getPainelBotoes() {
        JButton jbAplicar = new JButton();
        jbAplicar.setIcon(new ImageIcon(Imagens.loadImage("filesave.png")));
        jbAplicar.setText("Aplicar");
        jbAplicar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                try {
                    Artista a = getArtistaEndObra();
                    GerenteAplicacao.getInstancia().salvarArtista(a);
                    limparArtista();
                    TelaAviso.telaSucesso(getPanel(), Mensagem.SUCESSO_SALVAR_ARTISTA.toString());
                    CustomStatusBar.getInstancia().updateArtista();
                } catch (Exception e) {
                    e.printStackTrace();
                    TelaAviso.telaErro(getPanel(), e.getMessage());
                    System.out.println("PainelCadastro.getPainelBotoes().jbAplicar.addActionListener.actionPerformed()\n" + e.getMessage());
                }
            }
        });
        jbRemover = new JButton();
        jbRemover.setIcon(new ImageIcon(Imagens.loadImage("editdelete.png")));
        jbRemover.setText("Remover");
        jbRemover.setEnabled(false);
        jbRemover.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (TelaAviso.telaConfirma(null, Mensagem.CONFIRMA_REMOVER_ARTISTA.toString()) == 0) {
                    if (!txtInsc.getText().isEmpty()) {
                        GerenteAplicacao.getInstancia().removerArtista(new Integer(txtInsc.getText()));
                        TelaAviso.telaSucesso(getPanel(), Mensagem.SUCESSO_REMOVER_ARTISTA.toString());
                        limparArtista();
                        CustomStatusBar.getInstancia().updateArtista();
                    } else {
                        TelaAviso.telaErro(getPanel(), Mensagem.ERRO_SALVAR_ARTISTA.toString());
                    }
                }
            }
        });
        jbLimpar = new JButton();
        jbLimpar.setIcon(new ImageIcon(Imagens.loadImage("view_text.png")));
        jbLimpar.setText("Limpar");
        jbLimpar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                limparArtista();
                limpaObra();
            }
        });
        JButton sair = new JButton();
        sair.setIcon(new ImageIcon(Imagens.loadImage("exit.png")));
        sair.setText("Sair");
        sair.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                System.exit(0);
            }
        });
        this.add(ButtonBarFactory.buildLeftAlignedBar(jbAplicar, jbRemover, jbLimpar), cc.xywh(2, 30, 8, 3));
        this.add(sair, cc.xywh(13, 30, 2, 3));
    }

    private void limparArtista() {
        txtNome.setText("");
        txtBairro.setText("");
        txtCep.setText("");
        txtCidade.setText("");
        txtCodObra.setText("");
        txtEmail.setText("");
        txtInsc.setText("");
        txtObs.setText("");
        txtPais.setText("Brasil");
        txtRua.setText("");
        txtTel.setText("");
        txtTituloObra.setText("");
        cbCategoria.setSelectedIndex(0);
        cbEstado.setSelectedIndex(0);
        cbSexo.setSelectedIndex(0);
        tabela.limparTabela();
        jbRemover.setEnabled(false);
    }

    private void getPainelObra() {
        this.addSeparator("Obra", cc.xyw(2, 18, 12));
        this.addLabel("T�tulo :", cc.xy(2, 20));
        txtTituloObra = new JTextField();
        this.add(txtTituloObra, cc.xyw(4, 20, 5));
        this.addLabel("C�digo :", cc.xy(10, 20));
        txtCodObra = new JTextField();
        txtCodObra.setEditable(false);
        txtCodObra.setFocusable(false);
        txtCodObra.setForeground(Color.red);
        this.add(txtCodObra, cc.xy(12, 20));
        this.addLabel("Categoria :", cc.xy(2, 22));
        cbCategoria = new JComboBox() {

            private static final long serialVersionUID = 1L;

            /** Preenche a lista caso ocorra alguma adi��o ou remo��o de elemento */
            @Override
            protected void paintComponent(Graphics g) {
                List<Categoria> lista = GerenteAplicacao.getInstancia().getListaCategorias();
                if (this.getItemCount() != lista.size()) {
                    this.removeAllItems();
                    for (Categoria cat : lista) {
                        cbCategoria.addItem(cat);
                    }
                }
                super.paintComponent(g);
            }
        };
        this.add(cbCategoria, cc.xyw(4, 22, 3));
        btAddCategoria = new JButton();
        btAddCategoria.setIcon(new ImageIcon(Imagens.loadImage("add.png")));
        btAddCategoria.setToolTipText("<html>Cadastra ou altera categoria. <br> " + "Para cadastrar selecione a categoria: Sem categoria.<br>" + "Para alterar, seleciona a categoria a ser alterada</html>");
        btAddCategoria.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showTelaAddCategoria();
            }
        });
        this.add(btAddCategoria, cc.xy(8, 22));
        JButton btIncluir = new JButton();
        btIncluir.setIcon(new ImageIcon(Imagens.loadImage("add.png")));
        btIncluir.setText("Obra");
        btIncluir.setToolTipText("Adicionar obra ao artista");
        btIncluir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Obra obra;
                try {
                    obra = getObra();
                    tabela.addObra(obra);
                    limpaObra();
                } catch (Exception e) {
                    TelaAviso.telaErro(getPanel(), e.getMessage());
                    System.err.println("PainelCadastro.getPainelObra().btIncluir.addActionListener.actionPerformed()\n" + e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        btCancelar = new JButton();
        btCancelar.setIcon(new ImageIcon(Imagens.loadImage("delete.png")));
        btCancelar.setText("Obra");
        btCancelar.setToolTipText("Remover a obra");
        btCancelar.setEnabled(false);
        btCancelar.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                if (!(txtCodObra.getText().isEmpty())) {
                    try {
                        tabela.removeObra(getObra());
                        limpaObra();
                    } catch (Exception e) {
                        e.printStackTrace();
                        TelaAviso.telaErro(getPanel(), e.getMessage());
                    }
                }
            }
        });
        this.add(ButtonBarFactory.buildAddRemoveBar(btIncluir, btCancelar), cc.xyw(10, 22, 4));
        this.addLabel("Sele��o :", cc.xy(2, 24));
        ButtonGroup bt = new ButtonGroup();
        EventoRadio er = new EventoRadio();
        btSim = new JRadioButton("Sim");
        btSim.setToolTipText("Obra Selecionada");
        btSim.addItemListener(er);
        bt.add(btSim);
        this.add(btSim, cc.xy(4, 24));
        btNao = new JRadioButton("N�o");
        btNao.setToolTipText("Obra n�o selecionada");
        btNao.addItemListener(er);
        bt.add(btNao);
        this.add(btNao, cc.xy(5, 24));
        btTalvez = new JRadioButton("Talvez");
        btTalvez.setToolTipText("Talvez obra seja selecionada");
        btTalvez.addItemListener(er);
        bt.add(btTalvez);
        this.add(btTalvez, cc.xyw(6, 24, 2));
        btNA = new JRadioButton("N�o Avaliado", true);
        btNA.setToolTipText("Obra ainda n�o avaliada");
        selected = NA;
        btNA.addItemListener(er);
        btNA.setForeground(Color.red);
        bt.add(btNA);
        this.add(btNA, cc.xyw(10, 24, 2));
        ArrayList<Obra> arr = new ArrayList<Obra>();
        tabela = new TabelaObras(arr, this);
        this.add(tabela, cc.xywh(2, 26, 12, 3));
    }

    /** */
    private void limpaObra() {
        txtCodObra.setText("");
        txtTituloObra.setText("");
        btNA.setSelected(true);
        cbCategoria.setSelectedIndex(0);
        btNA.setSelected(true);
        btCancelar.setEnabled(false);
    }

    /**
	 * @throws Exception  */
    private Obra getObra() throws Exception {
        Obra obra = new Obra();
        if (txtTituloObra.getText().isEmpty()) {
            throw new Exception(Mensagem.ERRO_NOME_OBRA.toString());
        }
        if (!txtCodObra.getText().isEmpty()) {
            obra.setCodigo(new Integer(txtCodObra.getText()));
        }
        obra.setTitulo(txtTituloObra.getText());
        obra.setCategoria((Categoria) cbCategoria.getSelectedItem());
        obra.setSelec(selected);
        return obra;
    }

    /** */
    private void showTelaAddCategoria() {
        if (telaCad == null) {
            telaCad = new TelaCadCategoria(this);
        }
        telaCad.setCategoria((Categoria) cbCategoria.getSelectedItem());
        telaCad.show(PainelCadastro.this.getPanel(), btAddCategoria.getX(), btAddCategoria.getY());
    }

    private void getPainelEndereco() {
        this.addSeparator("Endere�o", cc.xyw(2, 8, 12));
        this.addLabel("Logradouro :", cc.xy(2, 10));
        txtRua = new JTextField();
        this.add(txtRua, cc.xyw(4, 10, 5));
        this.addLabel("Bairro :", cc.xy(2, 12));
        txtBairro = new JTextField();
        this.add(txtBairro, cc.xyw(4, 12, 5));
        this.addLabel("Cidade :", cc.xy(10, 12));
        txtCidade = new JTextField();
        this.add(txtCidade, cc.xyw(12, 12, 2));
        this.addLabel("Cep :", cc.xy(2, 14));
        try {
            MaskFormatter mascaraCep = new MaskFormatter("#####'-###");
            txtCep = new JFormattedTextField(mascaraCep);
        } catch (ParseException e) {
            txtCep = new JTextField();
            e.printStackTrace();
        }
        this.add(txtCep, cc.xyw(4, 14, 2));
        this.addLabel("Estado :", cc.xy(10, 14));
        cbEstado = new JComboBox();
        preencherEstado();
        this.add(cbEstado, cc.xyw(12, 14, 2));
        this.addLabel("Pa�s :", cc.xy(2, 16));
        txtPais = new JTextField("Brasil");
        this.add(txtPais, cc.xyw(4, 16, 2));
        this.addLabel("Email :", cc.xy(10, 16));
        txtEmail = new JTextField();
        this.add(txtEmail, cc.xyw(12, 16, 2));
    }

    /** */
    private void preencherEstado() {
        for (ESTADO estado : ESTADO.values()) {
            cbEstado.addItem(estado);
        }
    }

    private void getPainelArtista() {
        this.addLabel("Nome :", cc.xy(2, 2));
        txtNome = new JTextField();
        txtNome.setToolTipText("Nome do artista");
        this.add(txtNome, cc.xyw(4, 2, 5));
        this.addLabel("Inscri��o :", cc.xy(10, 2));
        txtInsc = new JTextField();
        txtInsc.setFocusable(false);
        txtInsc.setEditable(false);
        txtInsc.setForeground(Color.red);
        this.add(txtInsc, cc.xy(12, 2));
        this.addLabel("Observa��o: ", cc.xy(2, 4));
        txtObs = new JTextArea();
        txtObs.setToolTipText("Qualquer observa��o sobre o artista pode ser inserida aqui");
        txtObs.setWrapStyleWord(true);
        txtObs.setLineWrap(true);
        txtObs.addKeyListener(new java.awt.event.KeyAdapter() {

            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_TAB) {
                    txtObs.transferFocus();
                }
            }
        });
        JScrollPane sc = new JScrollPane(txtObs);
        this.add(sc, cc.xywh(4, 4, 5, 3));
        this.addLabel("Sexo :", cc.xy(10, 4));
        cbSexo = new JComboBox();
        cbSexo.addItem("Masculino");
        cbSexo.addItem("Feminino");
        cbSexo.setToolTipText("Selecione o sexo do artista.");
        this.add(cbSexo, cc.xyw(12, 4, 2));
        try {
            MaskFormatter mskTel = new MaskFormatter("##'-####'-####");
            txtTel = new JFormattedTextField(mskTel);
        } catch (ParseException e) {
            txtTel = new JTextField();
            e.printStackTrace();
        }
        this.addLabel("Telefone :", cc.xy(10, 6));
        this.add(txtTel, cc.xyw(12, 6, 2));
    }

    private Artista getArtistaEndObra() throws Exception {
        validarArtista();
        artista = new Artista();
        artista.setNome(txtNome.getText());
        artista.setObs(txtObs.getText());
        if (!txtInsc.getText().isEmpty()) artista.setNumeroInscricao(new Integer(txtInsc.getText()));
        if (cbSexo.getSelectedIndex() == 0) {
            artista.setSexo(true);
        } else {
            artista.setSexo(false);
        }
        artista.setListaObras(tabela.getListaObras());
        artista.setTelefone(txtTel.getText());
        Endereco end = new Endereco();
        artista.setEmail(txtEmail.getText());
        end.setBairro(txtBairro.getText());
        end.setCep(txtCep.getText());
        end.setCidade(txtCidade.getText());
        end.setEstado(cbEstado.getSelectedIndex());
        end.setLogradouro(txtRua.getText());
        end.setPais(txtPais.getText());
        artista.setEndereco(end);
        return artista;
    }

    private void validarArtista() throws Exception {
        String erros = "";
        if (txtNome.getText().isEmpty()) {
            erros += Mensagem.ERRO_NOME_ARTISTA;
        }
        if (tabela.getListaObras().isEmpty()) {
            erros += Mensagem.ERRO_LISTA_OBRA_VAZIA;
        }
        if (!validaTelefone()) {
            erros += Mensagem.ERRO_TELEFONE;
        }
        if (validaCEP()) {
            erros += "- Insira um CEP no formato correto. Ex.: 12345-678\n";
        }
        if (txtEmail.getText().isEmpty() || !txtEmail.getText().matches(".+@.+\\.[a-z]+")) {
            erros += "- Insira um e-mail no formato correto. Ex.: cassioso@gmail.com";
        }
        if (erros != "") {
            throw new Exception(erros);
        }
    }

    /** */
    private boolean validaCEP() {
        return txtTel.getText().matches("\\d{5}-\\d{3}");
    }

    /** */
    private boolean validaTelefone() {
        return txtTel.getText().matches("\\d{2}-\\d{4}-\\d{4}");
    }

    @Override
    public void stateChanged(ChangeEvent arg0) {
        if (arg0.getSource() instanceof Categoria) {
            cbCategoria.removeAllItems();
            for (Categoria cat : GerenteAplicacao.getInstancia().getListaCategorias()) {
                if (cat.getCodigo() == 0) {
                    cbCategoria.addItem(cat);
                }
            }
            for (Categoria cat : GerenteAplicacao.getInstancia().getListaCategorias()) {
                if (!(cat.getCodigo() == 0)) {
                    cbCategoria.addItem(cat);
                }
            }
        } else if (arg0.getSource() instanceof Obra) {
            Obra obra = (Obra) arg0.getSource();
            txtCodObra.setText(obra.getCodigo().toString());
            txtTituloObra.setText(obra.getTitulo());
            cbCategoria.setSelectedItem(obra.getCategoria());
            switch(obra.getSelec()) {
                case SIM:
                    btSim.setSelected(true);
                    break;
                case NAO:
                    btNao.setSelected(true);
                    break;
                case TALVEZ:
                    btTalvez.setSelected(true);
                    break;
                default:
                    btNA.setSelected(true);
                    break;
            }
            btCancelar.setEnabled(true);
        }
        cbCategoria.revalidate();
        cbCategoria.repaint();
    }

    class EventoRadio implements ItemListener {

        @Override
        public void itemStateChanged(ItemEvent arg0) {
            if (btSim.isSelected()) {
                selected = SIM;
            } else if (btNao.isSelected()) {
                selected = NAO;
            } else if (btTalvez.isSelected()) {
                selected = TALVEZ;
            } else if (btNA.isSelected()) {
                selected = NA;
            }
        }
    }

    public void atualizarArtista(Object source) {
        Artista art = (Artista) source;
        txtBairro.setText(art.getEndereco().getBairro());
        txtCep.setText(art.getEndereco().getCep());
        txtCidade.setText(art.getEndereco().getCidade());
        txtEmail.setText(art.getEmail());
        txtInsc.setText(art.getNumeroInscricao().toString());
        txtNome.setText(art.getNome());
        txtObs.setText(art.getObs());
        txtPais.setText(art.getEndereco().getPais());
        txtRua.setText(art.getEndereco().getLogradouro());
        txtTel.setText(art.getTelefone());
        if (art.isSexo()) cbSexo.setSelectedIndex(0); else cbSexo.setSelectedIndex(1);
        cbEstado.setSelectedIndex(art.getEndereco().getEstado());
        cbCategoria.setSelectedIndex(0);
        tabela.limparTabela();
        for (Obra obra : art.getListaObras()) {
            try {
                tabela.addObraConsulta(obra);
            } catch (Exception e) {
                TelaAviso.telaErro(getPanel(), e.getMessage());
                e.printStackTrace();
            }
        }
        jbRemover.setEnabled(true);
    }
}
