package pdp.scrabble.ia;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import pdp.scrabble.dictionary.DAWGImpl;
import pdp.scrabble.dictionary.DAWGItf;
import pdp.scrabble.game.Board;
import pdp.scrabble.game.GameEnvironment;
import pdp.scrabble.game.exception.BoardWrongWordPlace;
import pdp.scrabble.game.impl.GameEnvImpl;
import pdp.scrabble.ia.impl.BestMoves;
import pdp.scrabble.ia.impl.DawgMoveGen;
import pdp.scrabble.ia.impl.EvaluatorImpl;
import pdp.scrabble.ia.impl.RandomAccumulator;
import pdp.scrabble.ia.impl.SimpleSimulator;
import pdp.scrabble.ihm.BoardPanel;

public class Main {

    static AbstractAlgoStep algo;

    static DecoAI ia;

    static Board b;

    static BoardPanel pan;

    /**
     * @param args
     */
    public static void main(String[] args) {
        GameEnvironment env = new GameEnvImpl(null, 2, "Francais");
        env.bag().fill();
        b = env.board();
        ia = new DecoAI(env, "trololol", 0, null);
        DecoAI ia2 = new DecoAI(env, "loiloiloi", 1, null);
        ia2.getRack().fill();
        ia.getRack().fill();
        env.addPlayer(ia);
        env.addPlayer(ia2);
        algo = new SimpleSimulator(new DawgMoveGen(env, ia, (DAWGItf) env.getDictionary(), new BestMoves(new EvaluatorImpl(env, null), 5)), env, ia);
        JButton bouton = new JButton("Continuer");
        pan = new BoardPanel(b);
        bouton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        long t = System.currentTimeMillis();
                        algo.process();
                        System.out.println(System.currentTimeMillis() - t);
                        MoveModel move = algo.getBestMove();
                        if (move != null) move.play(b, ia);
                        try {
                            b.validate(true);
                        } catch (BoardWrongWordPlace e) {
                            e.printStackTrace();
                        }
                        ia.getRack().fill();
                        pan.updateUI();
                    }
                });
                t.start();
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        JFrame f = new JFrame();
        f.setLayout(new FlowLayout());
        f.add(pan);
        f.add(bouton);
        f.pack();
        f.setVisible(true);
    }
}
