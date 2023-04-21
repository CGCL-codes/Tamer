package gcp.gui;

import gcp.Emprestimo;
import gcp.enums.ItemEstado;
import gcp.gui.modelos.EmprestimosDeAmigosTableModel;
import gcp.gui.modelos.EmprestimosDoUsuarioTableModel;
import gcp.principal.FrameGCP;
import gcp.principal.GCP;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * FrameEmprestimos<br>
 * Frame responsável pela exibicao de todos emprestimos do usuario, tanto os que
 * o usuario emprestou como os que ele recebeu.
 * @author Catharine Quintans
 */
public class FrameEmprestimos extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private FrameGCP frameGcp;

    private GCP gcp;

    private JButton botaoAdicionarEmprestimo, botaoEditar, botaoDevolvido, botaoDevolver, botaoPegarEmprestado;

    private EmprestimosDoUsuarioTableModel modelo;

    private JTable tabela;

    private JScrollPane barraDeRolagem;

    private EmprestimosDeAmigosTableModel modeloAmigos;

    private JTable tabelaAmigos;

    private JScrollPane barraAmigos;

    private Container tela;

    /**
     * Construtor.
     * @param frameGcp Frame principal do GCP.
     */
    public FrameEmprestimos(FrameGCP frameGcp) {
        super("Empréstimos do usuário");
        this.frameGcp = frameGcp;
        gcp = frameGcp.getGcp();
        inicializar();
        posicoes();
        acoes();
        constroiTela();
        carregar();
    }

    /**
     * Inicializa os componentes.
     */
    private void inicializar() {
        setSize(600, 530);
        setClosable(true);
        tela = getContentPane();
        tela.setLayout(null);
        modelo = new EmprestimosDoUsuarioTableModel();
        tabela = new JTable(modelo);
        barraDeRolagem = new JScrollPane(tabela);
        modeloAmigos = new EmprestimosDeAmigosTableModel();
        tabelaAmigos = new JTable(modeloAmigos);
        barraAmigos = new JScrollPane(tabelaAmigos);
        botaoAdicionarEmprestimo = new JButton("Emprestar");
        botaoPegarEmprestado = new JButton("Pegar emprestado");
        botaoEditar = new JButton("Editar");
        botaoDevolvido = new JButton("Devolvido");
        botaoDevolver = new JButton("Devolver");
    }

    /**
     * Carrega os emprestimos em tela.
     */
    private void carregar() {
        modelo.setEmprestimos(gcp.getListEmprestimos(frameGcp.getUsuarioLogado()));
        modeloAmigos.setEmprestimos(gcp.getListEmprestimosDeAmigos(frameGcp.getUsuarioLogado()));
    }

    /**
     * Define as acoes dos objetos (botoes).
     */
    private void acoes() {
        botaoAdicionarEmprestimo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                frameGcp.abrirJanela(new FrameEmprestar(frameGcp, null));
                dispose();
            }
        });
        botaoPegarEmprestado.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                frameGcp.abrirJanela(new FramePegarEmprestado(frameGcp, null));
                dispose();
            }
        });
        botaoDevolvido.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                emprestimoDevolvido();
            }
        });
        botaoDevolver.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent arg0) {
                devolverEmprestimo();
            }
        });
    }

    /**
     * Realiza a devolução do empréstimo selecionado, este removerá o empréstimo
     * da lista de empréstimo, e deixará o item como comigo.
     */
    private void emprestimoDevolvido() {
        int selecao = tabela.getSelectedRow();
        if (selecao < 0) {
            frameGcp.exibirMensagem("Não há nenhum empréstimo selecionado");
            return;
        }
        Emprestimo emprestimoASerRemovido = modelo.get(selecao);
        try {
            gcp.removeEmprestimo(emprestimoASerRemovido);
            emprestimoASerRemovido.getItem().setItemEstado(ItemEstado.COMIGO);
            carregar();
        } catch (Exception e) {
            e.printStackTrace();
            frameGcp.exibirMensagem(e.getMessage());
        }
    }

    /**
     * Realiza a devolução do empréstimo selecionado, este removerá o empréstimo
     * da lista de empréstimo, e excluira o item da colecao.
     */
    private void devolverEmprestimo() {
        int selecao = tabelaAmigos.getSelectedRow();
        if (selecao < 0) {
            frameGcp.exibirMensagem("Não há nenhum empréstimo selecionado");
            return;
        }
        Emprestimo emprestimoASerRemovido = modeloAmigos.get(selecao);
        try {
            gcp.removeEmprestimo(emprestimoASerRemovido);
            carregar();
        } catch (Exception e) {
            frameGcp.exibirMensagem(e.getMessage());
        }
    }

    /**
     * Define as posições dos componentes em tela.
     */
    private void posicoes() {
        barraDeRolagem.setBounds(7, 7, 577, 200);
        botaoAdicionarEmprestimo.setBounds(7, 212, 150, 30);
        botaoDevolvido.setBounds(167, 212, 100, 30);
        barraAmigos.setBounds(7, 250, 577, 200);
        botaoPegarEmprestado.setBounds(7, 455, 150, 30);
        botaoDevolver.setBounds(167, 455, 100, 30);
    }

    /**
     * Adiciona os componentes em tela.
     */
    private void constroiTela() {
        tela.add(barraDeRolagem);
        tela.add(barraAmigos);
        tela.add(botaoAdicionarEmprestimo);
        tela.add(botaoEditar);
        tela.add(botaoDevolvido);
        tela.add(botaoDevolver);
        tela.add(botaoPegarEmprestado);
    }
}
