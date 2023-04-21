package cxtable.xtable_chess;

import java.awt.*;
import java.awt.event.*;
import cxtable.peer.*;

public class xChessLauncher extends Thread implements ActionListener {

    private Button white = new Button("Select 'White'");

    private Button black = new Button("Select 'Black'");

    private Frame f = new Frame("Launch chess game...");

    private xPlugDataListener xtran;

    public xChessLauncher(xPlugDataListener x) {
        xtran = x;
    }

    public void run() {
        f.setLayout(new BorderLayout());
        f.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent we) {
                f.setVisible(true);
                f = null;
            }
        });
        white.addActionListener(this);
        black.addActionListener(this);
        f.add(white, BorderLayout.WEST);
        f.add(black, BorderLayout.EAST);
        f.pack();
        f.setVisible(true);
    }

    public void send(String s) {
        xtran.reglaunch(s);
        f.setVisible(false);
        f = null;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == white) {
            send("<RSIDE>WHITE</RSIDE><LSIDE>BLACK</LSIDE>");
        }
        if (ae.getSource() == black) {
            send("<RSIDE>BLACK</RSIDE><LSIDE>WHITE</LSIDE>");
        }
    }
}
