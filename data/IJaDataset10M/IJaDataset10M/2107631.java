package interfaceGrafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import servicos.Servicos;

public class SuperVendas extends SuperJanela {

    private static Servicos servicos;

    private static JanelaVenda janelaVenda;

    private static AutenticarGerente autenticar;

    private static JanelaCpf janelaCpf;

    private static Pagamento pgto;

    public SuperVendas(Servicos servicos) {
        this.servicos = servicos;
    }

    public static void iniciaPgto(double total) {
        DecimalFormat paraVirgula = new DecimalFormat("0.00");
        paraVirgula.setMaximumFractionDigits(2);
        DecimalFormatSymbols decimalVirgula = new DecimalFormatSymbols();
        decimalVirgula.setDecimalSeparator(',');
        paraVirgula.setDecimalFormatSymbols(decimalVirgula);
        pgto = new Pagamento(servicos, paraVirgula.format(total));
    }

    public static void janelaCpf() {
        janelaCpf = new JanelaCpf(servicos);
    }

    public void iniciarVenda() {
        janelaVenda = new JanelaVenda(servicos);
    }

    public static void autenticarGerente() {
        autenticar = new AutenticarGerente(servicos);
    }

    public static class Inserir implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            int opcao;
            Object[] botoes = { "Sim", "N�o" };
            opcao = JOptionPane.showOptionDialog(null, "Deseja inserir CPF na nota?", "CPF", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, botoes, botoes[0]);
            if (opcao == JOptionPane.YES_OPTION) {
                janelaCpf();
            } else {
                janelaVenda.iniciaVariaveis("");
            }
        }
    }

    public void iniciaVariaveisVenda(String cpf) {
        janelaVenda.iniciaVariaveis(cpf);
    }

    public void finalizarVenda(String valor) {
        janelaVenda.vendeAVista(valor);
    }

    public static class ExcluirItem implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            try {
                if (servicos.getFuncLogado().getCargo().getCodigo() == 1) {
                    janelaVenda.excluirItem();
                } else {
                    autenticarGerente();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Selecione um item da lista\r\nO item nao foi excluido", "Exclus�o", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void autenticar(String usuario, String senha) {
        if (servicos.isGerente(usuario, senha)) {
            autenticar.dispose();
            janelaVenda.excluirItem();
        } else {
            JOptionPane.showMessageDialog(null, "Login Invalido\r\nItem nao pode ser Excluido", "Autentica��o", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static class FinalizarVenda implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (!(janelaVenda.isParcelado())) {
                iniciaPgto(janelaVenda.getTotalVenda());
            } else {
                janelaVenda.vendeParcelado();
            }
        }
    }
}
