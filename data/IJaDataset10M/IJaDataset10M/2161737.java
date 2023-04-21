package br.com.jteam.jfcm.controller.listener;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JDialog;

/**
 * 
 * Listener para fechamento de caixas de di&aacute;logo. Captura se a tecla ESC
 * foi pressionada, e fecha a janela.
 * 
 */
public class CloseAboutDialogListener implements KeyListener {

    /**
	 * Caixa de di&aacutelogo.
	 */
    private JDialog dialog;

    /**
	 * Construtor que recebe uma caixa de di&aacutelogo.
	 */
    public CloseAboutDialogListener(JDialog dialog) {
        this.dialog = dialog;
    }

    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_ESCAPE) {
            dialog.dispose();
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }
}
