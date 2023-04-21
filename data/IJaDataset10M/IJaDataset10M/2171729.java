package com.jaxh.zeusmsn;

import com.jaxh.zeusmsn.mvc.login.usuario.LoginUsuarioDialogo;
import com.jaxh.zeusmsn.mvc.login.usuario.LoginUsuarioModelo;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 *
 * @author andoni
 */
public class Main {

    public static void main(String args[]) {
        Main.inicializaDialogo();
        Main.ejecutaDialogo();
    }

    private static void inicializaDialogo() {
        LoginUsuarioModelo m = new LoginUsuarioModelo();
        LoginUsuarioModelo.dLoginUsuario = new LoginUsuarioDialogo();
        LoginUsuarioModelo.dLoginUsuario.inicializa(m);
        m.inicializa(LoginUsuarioModelo.dLoginUsuario);
        LoginUsuarioModelo.dLoginUsuario.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private static void ejecutaDialogo() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                LoginUsuarioModelo.dLoginUsuario.setVisible(true);
            }
        });
    }
}
