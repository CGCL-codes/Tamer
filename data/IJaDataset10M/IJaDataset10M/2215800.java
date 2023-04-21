package interfaceGrafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import bemajava.Bematech;
import servicos.Servicos;

public class SuperJanela extends JFrame {

    MenuPrincipal menu;

    Login login;

    Servicos servicos;

    private static final long serialVersionUID = 1L;

    public SuperJanela() {
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(360, 368);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    public void carregarLogin() {
        login = new Login();
    }

    public void carregarMenu(Servicos servicos) {
        menu = new MenuPrincipal(servicos);
    }

    public void sumir() {
        dispose();
    }

    protected class Logoff implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            carregarLogin();
            sumir();
        }
    }

    public void excluirItem() {
        Bematech.CancelaItemGenerico("1");
    }

    public static void main(String args[]) {
        SuperJanela sup = new SuperJanela();
        sup.carregarLogin();
    }
}
