package graphxt;

import graphxt.gui.MainWindow;

/**
 * Main
 * 
 * @author Giuliano Vilela
 */
public class Main {

    /**
     * Cria uma MainWindow na thread de eventos do Swing
     * e deixa ela visível
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new MainWindow().setVisible(true);
            }
        });
    }
}
