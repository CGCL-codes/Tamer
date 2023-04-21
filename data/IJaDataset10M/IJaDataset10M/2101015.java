package jp.seraph.sample.puzzle;

import java.awt.*;
import java.awt.event.*;

public class PuzzleApp extends Frame implements ActionListener {

    PuzzleCanvas lc1;

    Button b1;

    public static void main(String[] args) {
        new PuzzleApp().setVisible(true);
    }

    public PuzzleApp() {
        setLayout(null);
        setSize(380, 430);
        lc1 = new PuzzleCanvas(12, 12);
        lc1.setLocation(10, 30);
        add(lc1);
        b1 = new Button("Start");
        add(b1);
        b1.setBounds(10, 400, 100, 20);
        b1.addActionListener(this);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public void actionPerformed(ActionEvent e) {
        lc1.initialData();
    }
}
