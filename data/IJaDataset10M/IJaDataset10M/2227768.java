package Biblioteca.Suporte;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import componere.ResourceManager;

public class JanelaMensagem extends JanelaPainel {

    JButton bOk = new JButton();

    public JanelaMensagem(Frame pai, String titulo, boolean modal) {
        super(pai, titulo, modal);
        acrescentaBotao();
    }

    public JanelaMensagem(Frame pai, String titulo, boolean modal, String pMensagem) {
        super(pai, titulo, modal, pMensagem);
        acrescentaBotao();
    }

    void acrescentaBotao() {
        bOk.setText("Ok");
        bOk.setPreferredSize(new Dimension(130, 36));
        bOk.setIcon(new ImageIcon(ResourceManager.getResourceURL("image/control/b(ok).gif")));
        bOk.setBackground(new Color(255, 255, 206));
        bOk.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent e) {
                okClicado(e);
            }
        });
        painelBotoes.add(bOk, null);
    }

    void okClicado(ActionEvent e) {
        setVisible(false);
    }
}
