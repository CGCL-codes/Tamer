package heisig;

import heisig.process.QuizProcess;
import javax.swing.JFrame;

/**
 * The main class from which the application is started. Creates the UI window
 * and starts the quiz process.
 * 
 * @author abartho
 *
 */
public class Heisig {

    private JFrame jf;

    private QuizProcess process;

    public static void main(String[] args) {
        Heisig heisig = new Heisig();
        heisig.start();
    }

    public Heisig() {
        jf = new JFrame("Heisig Flashcard Trainer");
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        process = new QuizProcess(jf);
        jf.setSize(400, 500);
    }

    public void start() {
        process.startProcess();
        jf.setVisible(true);
    }
}
