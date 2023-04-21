package gui.janelas;

import gui.painel.PainelJogo;
import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import org.newdawn.slick.SlickException;

public class JanelaJogo extends Janela implements ActionListener {

    public JanelaJogo() throws HeadlessException, IOException, UnsupportedLookAndFeelException {
        super();
        Container cp = this.getContentPane();
        pJogo = new PainelJogo(this);
        cp.add(pJogo);
        cp.setBackground(Color.BLACK);
        cp.setForeground(Color.BLACK);
        pJogo.addActionButton(pJogo.getSair(), this);
        pJogo.addActionButton(pJogo.getObt().getFechar(), this);
        this.pack();
    }

    public void actionPerformed(ActionEvent e) {
        try {
            dispose();
            JanelaPrincipal jPrincipal = new JanelaPrincipal();
            jPrincipal.setBackground(Color.black);
            jPrincipal.setVisible(true);
        } catch (SlickException ex) {
            Logger.getLogger(JanelaJogo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (HeadlessException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(JanelaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private PainelJogo pJogo;
}
