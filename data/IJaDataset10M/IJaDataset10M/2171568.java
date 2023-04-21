package reversi;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class ReversiBrett extends JFrame {

    private Reversi reversi;

    private ReversiPanel panel;

    public ReversiBrett(Reversi reversi) throws HeadlessException {
        super("Reversi");
        this.reversi = reversi;
        initFrame();
        initMenu();
        neu();
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initFrame() {
        panel = new ReversiPanel(reversi);
        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
    }

    private void initMenu() {
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu spiel = new JMenu("Spiel");
        menubar.add(spiel);
        JMenuItem neu = new JMenuItem("neu");
        spiel.add(neu);
        neu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                neu();
            }
        });
    }

    private void neu() {
        reversi.neuesSpiel();
        panel.update(null, null);
    }
}
