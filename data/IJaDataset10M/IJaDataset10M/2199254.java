package ch6.MagicSquareGame;

import java.awt.EventQueue;
import javax.swing.JApplet;

public class MagicSquareApplet extends JApplet {

    private static final long serialVersionUID = 1L;

    public void init() {
        try {
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    initComponents();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void initComponents() {
        MagicSquareBoard board = new MagicSquareBoard(3);
        board.drawBoard();
    }
}
