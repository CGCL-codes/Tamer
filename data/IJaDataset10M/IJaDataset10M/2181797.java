package flm.fiado.tela.produto;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import flm.fiado.bean.Produto;
import flm.fiado.dao.memory.ProdutoDAO;
import flm.fiado.exception.DaoException;
import flm.fiado.exception.DataInvalidaException;
import flm.fiado.exception.IdInvalidoException;
import flm.fiado.exception.ProdutoNaoEncontradoException;
import flm.fiado.tela.TelaUtil;
import flm.fiado.util.LeiouteCadastroBuilder;
import flm.fiado.util.SwingUtil;

public class TelaProduto extends JDialog implements ActionListener {

    private JTextField txtId;

    private JButton btBuscarPorId;

    private JTextField txtDescricao;

    private JTextField txtValor;

    private JTextField txtDataCriacao;

    private JButton btApagar;

    private JButton btLimpar;

    private JButton btSalvar;

    private JButton btNovoCancelar;

    private ProdutoDAO produtoDAO;

    public TelaProduto(boolean novo) {
        this(null, novo);
    }

    public TelaProduto(Produto produto) {
        this(produto, false);
    }

    public TelaProduto() {
        this(null, false);
    }

    /**
	 * No construtor s�o definidas as seguintes caracteristicas da tela
	 * Titulo
	 * Tamanho
	 * 
	 * S�o instanciados e atribuidos aos atributos do objeto TelaProduto os campos da tela
	 * alem de serem posicionados
	 */
    public TelaProduto(Produto produto, boolean modoNovoProduto) {
        setModal(true);
        SwingUtil.tentaSetaNimbus(this);
        setTitle("Detalhe produto");
        setSize(550, 200);
        TelaUtil.centralizar(this);
        this.txtId = new JTextField(5);
        this.txtDescricao = new JTextField(20);
        this.txtValor = new JTextField(6);
        this.txtDataCriacao = new JTextField(8);
        txtDataCriacao.setEditable(false);
        this.btBuscarPorId = TelaUtil.criaBotao(this, "Buscar por Id");
        this.btApagar = TelaUtil.criaBotao(this, "Apagar");
        this.btLimpar = TelaUtil.criaBotao(this, "Limpar");
        this.btSalvar = TelaUtil.criaBotao(this, "Salvar");
        this.btNovoCancelar = TelaUtil.criaBotao(this, "Novo");
        LeiouteCadastroBuilder leiaute = new LeiouteCadastroBuilder(this);
        leiaute.inicio(2, 2).dir().add(new JLabel("ID : "), 1).esq().add(this.txtId, 1).add(this.btBuscarPorId, 1).novaLinha().dir().add(new JLabel("Descri��o : "), 1).esq().add(this.txtDescricao, 1).novaLinha().dir().add(new JLabel("Valor : "), 1).esq().add(this.txtValor, 1).novaLinha().dir().add(new JLabel("Data Criacao : "), 1).esq().add(this.txtDataCriacao, 1).novaLinha().center().dir().add(this.btApagar).dir().add(this.btLimpar).dir().add(this.btSalvar).dir().add(this.btNovoCancelar);
        setResizable(false);
        this.produtoDAO = new ProdutoDAO();
        if (modoNovoProduto) {
            setModoNova();
        } else {
            setBean(produto);
            setModoBuscar();
        }
        setVisible(true);
    }

    private void setBean(Produto produto) {
        if (produto != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            txtId.setText(produto.getId() + "");
            txtDescricao.setText(produto.getDescricao());
            txtValor.setText(produto.getValor() + "");
            Date dataCriacao = produto.getDataCriacao();
            if (dataCriacao != null) {
                txtDataCriacao.setText(sdf.format(dataCriacao));
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        try {
            if (event.getSource() == this.btBuscarPorId) {
                buscarPorId();
            } else if (event.getSource() == this.btSalvar) {
                if (this.btNovoCancelar.getText().equals("Novo")) {
                    salvar();
                    JOptionPane.showMessageDialog(this, "Dados atualizados com sucesso com sucesso");
                } else {
                    criar();
                    JOptionPane.showMessageDialog(this, "Produto criada com sucesso com sucesso");
                    setModoBuscar();
                }
            } else if (event.getSource() == this.btApagar) {
                int opcao = JOptionPane.showConfirmDialog(this, "Deseja realmente apagar '" + this.txtDescricao.getText() + "'?", "Apagar Pessoa", JOptionPane.YES_NO_OPTION);
                if (opcao == JOptionPane.YES_OPTION) {
                    apagar();
                    JOptionPane.showMessageDialog(this, "Produto apagada com sucesso");
                    limparCampos();
                }
            } else if (event.getSource() == this.btNovoCancelar) {
                if (this.btNovoCancelar.getText().equals("Novo")) {
                    setModoNova();
                } else {
                    setModoBuscar();
                }
            } else if (event.getSource() == this.btLimpar) {
                limparCampos();
            }
        } catch (ProdutoNaoEncontradoException e) {
            JOptionPane.showMessageDialog(this, "Produto n�o encontrada");
        } catch (DaoException e) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao acessar a base, contate seu fornecedor", "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IdInvalidoException e) {
            JOptionPane.showMessageDialog(this, "Digite um id v�lido", "Aten��o", JOptionPane.WARNING_MESSAGE);
            e.printStackTrace();
        } catch (DataInvalidaException e) {
            JOptionPane.showMessageDialog(this, "Data " + e.getMessage() + " invalida");
        }
    }

    private void setModoBuscar() {
        this.txtId.setEnabled(true);
        this.btApagar.setVisible(true);
        this.btBuscarPorId.setVisible(true);
        this.btNovoCancelar.setText("Novo");
    }

    private void setModoNova() {
        this.txtId.setEnabled(false);
        limparCampos();
        this.btApagar.setVisible(false);
        this.btBuscarPorId.setVisible(false);
        this.btNovoCancelar.setText("Cancelar");
    }

    private void limparCampos() {
        this.txtId.setText("");
        this.txtDescricao.setText("");
        this.txtValor.setText("0.0");
        this.txtDataCriacao.setText("");
    }

    private void criar() throws DataInvalidaException, DaoException {
        try {
            Produto produto = getBeanPreenchido();
            this.produtoDAO.criar(produto);
            this.txtId.setText("" + produto.getId());
        } catch (IdInvalidoException e) {
            e.printStackTrace();
        }
    }

    private void apagar() throws IdInvalidoException, DaoException {
        int id = getId();
        ProdutoDAO produtoDAO = new ProdutoDAO();
        produtoDAO.apagar(id);
    }

    private void salvar() throws DataInvalidaException, DaoException, IdInvalidoException {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        Produto produto = getBeanPreenchido();
        produtoDAO.salvar(produto);
    }

    private Produto getBeanPreenchido() throws DataInvalidaException, IdInvalidoException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Produto produto = new Produto();
        if (this.txtId.isEnabled()) {
            int id = getId();
            produto.setId(id);
        }
        produto.setDescricao(this.txtDescricao.getText());
        produto.setValor(Double.parseDouble(this.txtValor.getText().trim()));
        if (!this.txtDataCriacao.getText().trim().equals("")) {
            try {
                produto.setDataCriacao(sdf.parse(this.txtDataCriacao.getText()));
            } catch (ParseException e) {
                throw new DataInvalidaException("Nascimento");
            }
        }
        return produto;
    }

    private void buscarPorId() throws DaoException, IdInvalidoException, ProdutoNaoEncontradoException {
        int idPessoa = getId();
        Produto produto = this.produtoDAO.buscarPorId(idPessoa);
        if (produto == null) {
            throw new ProdutoNaoEncontradoException();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.txtId.setText("" + produto.getId());
        this.txtDescricao.setText(produto.getDescricao());
        this.txtValor.setText(produto.getValor() + "");
        if (produto.getDataCriacao() != null) {
            this.txtDataCriacao.setText(sdf.format(produto.getDataCriacao()));
        }
    }

    private int getId() throws IdInvalidoException {
        int id = 0;
        try {
            id = Integer.parseInt(this.txtId.getText());
        } catch (NumberFormatException e) {
            throw new IdInvalidoException(e);
        }
        return id;
    }
}
