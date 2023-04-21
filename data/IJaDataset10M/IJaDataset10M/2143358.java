package petshop.gui;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import petshop.classes.BancoDeDados;
import petshop.classes.DocumentoMascara;
import petshop.classes.Produto;

/**
 *
 * @author arthur
 */
public class JanelaProduto extends Janela {

    Produto produto;

    private TipoJanela tipo;

    /** Creates new form JanelaProduto */
    public JanelaProduto(TipoJanela tipo, Produto p) {
        this.tipo = tipo;
        initComponents();
        if (p == null) {
            this.produto = new Produto();
        } else {
            this.produto = p;
        }
        this.setLocationRelativeTo(this.getContentPane());
        campoCodigo.setDocument(new DocumentoMascara(10));
        campoNome.setDocument(new DocumentoMascara(80));
        campoEstoque.setDocument(new DocumentoMascara(6));
        campoPrecoCusto.setDocument(new DocumentoMascara(8));
        campoPrecoVenda.setDocument(new DocumentoMascara(8));
        areaInformacoes.setDocument(new DocumentoMascara(400));
        reiniciar();
        if (this.tipo == TipoJanela.ALTERACAO) {
            campoCodigo.setEnabled(false);
            botaoCadastrar.setText("Alterar");
            preencher();
        } else if (this.tipo == TipoJanela.INFORMACAO) {
            botaoCadastrar.setVisible(false);
            botaoCancelar.setVisible(false);
            desabilitarCampos();
            preencher();
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;
        campoNome = new javax.swing.JTextField();
        scrollInformacoes = new javax.swing.JScrollPane();
        areaInformacoes = new javax.swing.JTextArea();
        botaoCadastrar = new javax.swing.JButton();
        botaoCancelar = new javax.swing.JButton();
        campoCodigo = new javax.swing.JFormattedTextField();
        campoEstoque = new javax.swing.JFormattedTextField();
        campoPrecoCusto = new javax.swing.JFormattedTextField();
        campoPrecoVenda = new javax.swing.JFormattedTextField();
        labelCodigo = new javax.swing.JLabel();
        labelNome = new javax.swing.JLabel();
        labelEstoque = new javax.swing.JLabel();
        labelPrecoCusto = new javax.swing.JLabel();
        labelInformacoes = new javax.swing.JLabel();
        labelPrecoVenda = new javax.swing.JLabel();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Cadastrar Produto");
        setMinimumSize(new java.awt.Dimension(400, 290));
        setResizable(false);
        getContentPane().setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(campoNome, gridBagConstraints);
        areaInformacoes.setColumns(20);
        areaInformacoes.setLineWrap(true);
        areaInformacoes.setRows(5);
        scrollInformacoes.setViewportView(areaInformacoes);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 12;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(scrollInformacoes, gridBagConstraints);
        botaoCadastrar.setText("Cadastrar");
        botaoCadastrar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cadastrar(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(botaoCadastrar, gridBagConstraints);
        botaoCancelar.setText("Cancelar");
        botaoCancelar.addMouseListener(new java.awt.event.MouseAdapter() {

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cancelar(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(botaoCancelar, gridBagConstraints);
        campoCodigo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        campoCodigo.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        campoCodigo.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ganharFoco(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(campoCodigo, gridBagConstraints);
        campoEstoque.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        campoEstoque.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        campoEstoque.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ganharFoco(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(campoEstoque, gridBagConstraints);
        campoPrecoCusto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.##"))));
        campoPrecoCusto.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        campoPrecoCusto.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ganharFoco(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 80;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(campoPrecoCusto, gridBagConstraints);
        campoPrecoVenda.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        campoPrecoVenda.setFocusLostBehavior(javax.swing.JFormattedTextField.PERSIST);
        campoPrecoVenda.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusGained(java.awt.event.FocusEvent evt) {
                ganharFoco(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 5, 5);
        getContentPane().add(campoPrecoVenda, gridBagConstraints);
        labelCodigo.setFont(new java.awt.Font("Ubuntu", 0, 12));
        labelCodigo.setText("Codigo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelCodigo, gridBagConstraints);
        labelNome.setFont(new java.awt.Font("Ubuntu", 1, 12));
        labelNome.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelNome, gridBagConstraints);
        labelEstoque.setFont(new java.awt.Font("Ubuntu", 1, 12));
        labelEstoque.setText("Estoque");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelEstoque, gridBagConstraints);
        labelPrecoCusto.setFont(new java.awt.Font("Ubuntu", 1, 12));
        labelPrecoCusto.setText("Preço de Custo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelPrecoCusto, gridBagConstraints);
        labelInformacoes.setFont(new java.awt.Font("Ubuntu", 0, 12));
        labelInformacoes.setText("Informações Adicionais");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelInformacoes, gridBagConstraints);
        labelPrecoVenda.setFont(new java.awt.Font("Ubuntu", 1, 12));
        labelPrecoVenda.setText("Preço de Venda");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
        getContentPane().add(labelPrecoVenda, gridBagConstraints);
        pack();
    }

    private void cancelar(java.awt.event.MouseEvent evt) {
        cancelar();
    }

    private void cadastrar(java.awt.event.MouseEvent evt) {
        if (!existemDependencias()) {
            produto = gerarProduto();
            if (tipo == TipoJanela.CADASTRO) {
                cadastrar();
            } else if (tipo == TipoJanela.ALTERACAO) {
                alterar();
            }
        }
    }

    private void ganharFoco(java.awt.event.FocusEvent evt) {
        JTextComponent t = (JTextComponent) evt.getComponent();
        int posicao = 0;
        for (int i = 0; i < t.getText().length(); i++) {
            if (!t.getText().substring(posicao, posicao + 1).equals(" ")) {
                posicao++;
            } else {
                break;
            }
        }
        t.setCaretPosition(posicao);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new JanelaProduto(TipoJanela.CADASTRO, null).setVisible(true);
            }
        });
    }

    private javax.swing.JTextArea areaInformacoes;

    private javax.swing.JButton botaoCadastrar;

    private javax.swing.JButton botaoCancelar;

    private javax.swing.JFormattedTextField campoCodigo;

    private javax.swing.JFormattedTextField campoEstoque;

    private javax.swing.JTextField campoNome;

    private javax.swing.JFormattedTextField campoPrecoCusto;

    private javax.swing.JFormattedTextField campoPrecoVenda;

    private javax.swing.JLabel labelCodigo;

    private javax.swing.JLabel labelEstoque;

    private javax.swing.JLabel labelInformacoes;

    private javax.swing.JLabel labelNome;

    private javax.swing.JLabel labelPrecoCusto;

    private javax.swing.JLabel labelPrecoVenda;

    private javax.swing.JScrollPane scrollInformacoes;

    protected final void reiniciar() {
        campoCodigo.setText("");
        campoNome.setText("");
        campoPrecoCusto.setText("");
        campoPrecoVenda.setText("");
        campoEstoque.setText("");
        areaInformacoes.setText("");
    }

    protected final boolean existemDependencias() {
        String msg = "Você esqueceu de preencher os \nseguintes campos obrigatórios:\n\n";
        boolean existeDependencias = false;
        if (campoNome.getText().equals("")) {
            msg += "- NOME\n";
            existeDependencias = true;
        }
        if (campoPrecoCusto.getText().equals("")) {
            msg += "- PREÇO DE CUSTO\n";
            existeDependencias = true;
        }
        if (campoPrecoVenda.getText().equals("")) {
            msg += "- PREÇO DE VENDA\n";
            existeDependencias = true;
        }
        if (campoEstoque.getText().equals("")) {
            msg += "- QTDE EM ESTOQUE\n";
            existeDependencias = true;
        }
        if (existeDependencias) {
            JOptionPane.showMessageDialog(this.getContentPane(), msg);
        }
        return existeDependencias;
    }

    private Produto gerarProduto() {
        int codigo = 0;
        if (!campoCodigo.getText().equals("")) {
            codigo = Integer.valueOf(campoCodigo.getText());
        }
        String nome = campoNome.getText();
        int qtdeEstoque = Integer.valueOf(campoEstoque.getText());
        double precoCusto = Double.valueOf(campoPrecoCusto.getText());
        double precoVenda = Double.valueOf(campoPrecoVenda.getText());
        String informacoes = areaInformacoes.getText();
        return new Produto(codigo, nome, precoCusto, precoVenda, qtdeEstoque, informacoes);
    }

    protected final void desabilitarCampos() {
        campoCodigo.setEditable(false);
        campoNome.setEditable(false);
        campoEstoque.setEditable(false);
        campoPrecoCusto.setEditable(false);
        campoPrecoVenda.setEditable(false);
        areaInformacoes.setEditable(false);
    }

    protected final void preencher() {
        campoCodigo.setText(String.valueOf(produto.getCodigo()));
        campoNome.setText(produto.getNome());
        campoEstoque.setText(String.valueOf(produto.getQtdeEstoque()));
        campoPrecoCusto.setText(String.valueOf(produto.getPrecoCusto()));
        campoPrecoVenda.setText(String.valueOf(produto.getPrecoVenda()));
        areaInformacoes.setText(produto.getInformacoes());
    }

    protected void cadastrar() {
        if (BancoDeDados.cadastrar(produto)) {
            int resp = JOptionPane.showConfirmDialog(this, "Produto cadastrado com sucesso. Deseja realizar novo cadastro?", "Concluído", JOptionPane.YES_NO_OPTION);
            this.reiniciar();
            if (resp == JOptionPane.NO_OPTION) {
                this.dispose();
            }
        } else {
            JOptionPane.showMessageDialog(this.getContentPane(), "Falha ao cadastrar produto!");
        }
    }

    protected void alterar() {
        if (BancoDeDados.alterar(produto)) {
            JOptionPane.showMessageDialog(this.getContentPane(), "Produto alterado com sucesso!");
            reiniciar();
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this.getContentPane(), "Falha ao alterar produto!");
        }
    }
}
